package sportstrackeronline;

import Extasys.DataFrame;
import Extasys.Network.TCP.Server.Listener.Exceptions.*;
import Extasys.Network.TCP.Server.Listener.Packets.IncomingTCPClientConnectionPacket;
import Extasys.Network.TCP.Server.Listener.Packets.MessageCollectorTCPClientConnectionPacket;
import Extasys.Network.TCP.Server.Listener.Packets.OutgoingTCPClientConnectionPacket;
import Extasys.Network.TCP.Server.Listener.TCPClientConnection;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingWorker;
import printingOnline.MainPrinting;

/**
 *
 * @author Nikos Siatras
 */
public class TCPChatServer extends Extasys.Network.TCP.Server.ExtasysTCPServer {

    private Hashtable fConnectedClients;
    private String fSPT = String.valueOf(((char) 2)); // Message splitter character.
    private String fMCChar = String.valueOf(((char) 3)); // Message collector character.
    private Thread fPingThread;
    private boolean fServerIsActive;
    private OneReader fMainForm;
    private Connection connect = null;

    public TCPChatServer(InetAddress listenerIP, int port, OneReader frmMain) {
        super("TCP Chat Server", "", 10, 100);
        super.AddListener("Main Listener", listenerIP, port, 99999, 20480, 0, 100, ((char) 3));

        fConnectedClients = new Hashtable();
        fMainForm = frmMain;
        initializeConnection();

    }

    @Override
    public void Start() {
        try {
            super.Start();

            fPingThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    while (fServerIsActive) {
                        try {
                            SendToAllClients("Ping" + fSPT);
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
                        }

                    }
                }
            });

            fServerIsActive = true;
            fPingThread.start();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            Stop();
        }
    }

    @Override
    public void Stop() {
        fServerIsActive = false;

        if (fPingThread != null) {
            fServerIsActive = false;
            fPingThread.interrupt();
        }

        super.Stop();
    }

    @Override
    public void OnDataReceive(TCPClientConnection sender, DataFrame data) {
        try {
            System.out.println(new String(data.getBytes()));
            String[] splittedMessage = new String(data.getBytes()).split(fSPT);

            if (splittedMessage[0].equals("Login")) {
                /*  Message: Login ((char)2) Username
                 Client wants to login.
                 Server checks if username is unique.
                 If the username is taken server replys -> "Change_Username ((char)2)"
                 If the username is not taken then server replys -> "Welcome ((char)2)" to the new client
                 and sends "New_User ((char)2) NewUsername" to all other clients.
                 */
                String tmpUsername = splittedMessage[1];
                if (IsUsernameTaken(tmpUsername)) {
                    SendToClient("Change_Username" + fSPT, sender);
                } else {
                    TCPChatUser user = new TCPChatUser(tmpUsername, sender);
                    fConnectedClients.put(sender.getIPAddress(), user);

                    SendToAllClients("New_User" + fSPT + tmpUsername);
                    SendToClient("Welcome" + fSPT, sender);
                }
            } else if (splittedMessage[0].equals("Message")) {
                /*  Message: Message ((char)2) some_text
                 Client sends a chat message to the server.
                 Server checks if the client is registered to the server.
                 If the client is registered to the server 
                 the server sends this message to all the other clients "Message ((char)2) Sender's username : some_text" else
                 it disconnects the client.  
                 */
                if (fConnectedClients.containsKey(sender.getIPAddress())) {
                    SendToAllClients("Message" + fSPT + GetClientName(sender) + ":" + splittedMessage[1]);
                } else {
                    sender.DisconnectMe();
                }
            } else if (splittedMessage[0].equals("Get_List")) {
                /*  Message: Get_List ((char)2)
                 Client requets a list with other connected clients.
                 If the client is registered to the server the server replys the list
                 else it disconnects the client.
                 */
                if (fConnectedClients.containsKey(sender.getIPAddress())) {
                    SendToClient(GetConnectedClientsList(), sender);
                } else {
                    sender.DisconnectMe();
                }
            } else if (splittedMessage[0].equals("Pong")) {
                /* Message: Pong ((char)2)
                 Client response to Ping.
                 */
                System.out.println(GetClientName(sender) + " PONG!");
            } else if (splittedMessage[0].startsWith("MISSING:")) {
                /* Message: Pong ((char)2)
                 Client response to Ping.
                 */
                System.out.println(GetClientName(sender) + " RECEIVED MISSING INFO ");
                SendToAllClients("MISSING:" + splittedMessage[0]);
            }else if (splittedMessage[0].startsWith("TRYPRINTING:")) {
                /* Message: Pong ((char)2)
                 Client response to Ping.
                 */
                System.out.println(GetClientName(sender) + " RECEIVED SUCCESS PRINT COMMAND NOW FORWARDING REQUEST ");
                SendToAllClients("LETSPRINT:" + splittedMessage[0].replace("TRYPRINTING:", ""));
            }  
            
            else if (splittedMessage[0].startsWith("SENDTOCONSOLE:")) {
                //    SendToAllClients(splittedMessage[0]);

                System.out.println(GetClientName(sender) + "   RECIEVED REQUEST FROM CLIENT   " + splittedMessage[0]);
                fMainForm.ConsoleMsg(GetClientName(sender) + "   RECIEVED REQUEST FROM CLIENT   " + splittedMessage[0].replace("SENDTOCONSOLE:", ""));

            } else {
                //    System.out.println(sender.getIPAddress().toString() + " sends wrong message");

            }
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Returns true if the username is taken.
     *
     * @param username is the username to check
     * @return true or false.
     */
    private boolean IsUsernameTaken(String username) {
        for (Enumeration e = fConnectedClients.keys(); e.hasMoreElements();) {
            try {
                if (((TCPChatUser) fConnectedClients.get(e.nextElement())).getUsername().equals(username)) {
                    return true;
                }
            } catch (Exception ex) {
            }
        }
        return false;
    }

    private String GetClientName(TCPClientConnection sender) {
        if (fConnectedClients.containsKey(sender.getIPAddress())) {
            return ((TCPChatUser) fConnectedClients.get(sender.getIPAddress())).getUsername();
        }
        return "";
    }

    
    
    void SendToSpecificClients() {

        
         String list = "";
        for (Enumeration e = fConnectedClients.keys(); e.hasMoreElements();) {
            list = list + ((TCPChatUser) fConnectedClients.get(e.nextElement())).getUsername() + String.valueOf(((char) 1));
            System.out.println(list);
        }
    }

    
    void SendToAllClients(String message) {

        message = message + fMCChar;
        for (Enumeration e = fConnectedClients.keys(); e.hasMoreElements();) {
            try {
                ((TCPChatUser) fConnectedClients.get(e.nextElement())).getConnection().SendData(message);
            } catch (ClientIsDisconnectedException ex) {
                System.err.println(ex.getMessage());
            } catch (OutgoingPacketFailedException ex) {
                System.err.println(ex.getMessage());
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
 void SendToSpecificClients(String message,String clientId) {

        message = message + fMCChar;
        for (Enumeration e = fConnectedClients.keys(); e.hasMoreElements();) {
            try {
                if(((TCPChatUser)fConnectedClients.get(e.nextElement())).getUsername().equalsIgnoreCase(clientId))
                {
                  
                ((TCPChatUser) fConnectedClients.get(e.nextElement())).getConnection().SendData(message);
                }
            } catch (ClientIsDisconnectedException ex) {
                Logger.getLogger(TCPChatServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (OutgoingPacketFailedException ex) {
                Logger.getLogger(TCPChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private void SendToClient(String data, TCPClientConnection sender) {
        try {
            sender.SendData(data + fMCChar);
           
        } catch (ClientIsDisconnectedException ex) {
            // Client disconnected.
            System.err.println(ex.getMessage());
        } catch (OutgoingPacketFailedException ex) {
            // Failed to send packet.
            System.err.println(ex.getMessage());
        } catch (Exception ex) {
        }
    }

    private String GetConnectedClientsList() {
        String list = "";
        for (Enumeration e = fConnectedClients.keys(); e.hasMoreElements();) {
                            System.out.println(e.toString());
            list = list + ((TCPChatUser) fConnectedClients.get(e.nextElement())).getUsername() + String.valueOf(((char) 1));

        }
        return "User_List" + fSPT + list;
    }

    @Override
    public void OnClientConnect(TCPClientConnection client) {

        System.out.println("New client starting connection");
        fMainForm.ConsoleMsg("New client starting connection" + client.getIPAddress());

    }

    public void initializeConnection() {

        new SwingWorker<String, String>() {
            @Override
            protected String doInBackground() {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Thread.sleep(1000);
                    connect = DriverManager
                            .getConnection("jdbc:mysql://" + "127.0.0.1" + "/sportstracker_dev?"
                                    + "user=RaceAdmin&password=v721PL7y");
                    System.out.println("DataBase Connected System Stable ...");
                    Thread.sleep(2000);

                    return "OK";
                } catch (ClassNotFoundException ex) {
                    System.out.println("Connection Failed ...");
                } catch (SQLException ex) {
                    System.out.println("Connection Failed ...");
                } catch (InterruptedException ex) {
                    System.out.println("Connection Failed ...");

                }
                return "ERROR";
            }
        }.execute();

    }

    public void GetTablesMetaData() {
        DatabaseMetaData meta;
        try {
            meta = connect.getMetaData();
            ResultSet res = meta.getTables(null, null, null,
                    new String[]{"TABLE"});
            while (res.next()) {
                System.out.println(
                        "   " + res.getString("TABLE_CAT")
                        + ", " + res.getString("TABLE_SCHEM")
                        + ", " + res.getString("TABLE_NAME")
                        + ", " + res.getString("TABLE_TYPE")
                        + ", " + res.getString("REMARKS"));
                SendToAllClients("Tables Spooling Now" + fSPT);
                Thread.sleep(1000);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainPrinting.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(TCPChatServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void OnClientDisconnect(TCPClientConnection client) {

        if (fConnectedClients.containsKey(client.getIPAddress())) {
            SendToAllClients("Remove_User" + fSPT + ((TCPChatUser) fConnectedClients.get(client.getIPAddress())).getUsername());
            fConnectedClients.remove(client.getIPAddress());
            fMainForm.ConsoleMsg("THE CLIENT DISCONNECTED " + client.getIPAddress());
        }

    }
}
