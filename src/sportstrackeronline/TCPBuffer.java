package sportstrackeronline;

import Extasys.DataFrame;
import Extasys.Network.TCP.Client.Connectors.TCPConnector;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.StringTokenizer;
import javax.swing.SwingWorker;

/**
 *
 * @author Nikos Siatras
 */
public class TCPBuffer extends Extasys.Network.TCP.Client.ExtasysTCPClient {

    private String fSPT = String.valueOf(((char) 2));
    private AutoSendMessages fAutoSendMessagesThread;
   
    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
  
    public static String CONNECTION = "127.0.0.1";
    FileWriter outFile;
    SwingWorker worker;
    SwingWorker Logworker;
    SwingWorker consoleWorker;



    public TCPBuffer(String name, String description, InetAddress remoteHostIP, int remoteHostPort, int corePoolSize, int maximumPoolSize) {

        super(name, description, corePoolSize, maximumPoolSize);
        try {

            super.AddConnector(name, remoteHostIP, remoteHostPort, 10240, "\n");
        } catch (Exception ex) {
        }
    }
    String startmsg = "TCP/IP connection with reader client established, commencing sending RFID tag search results!\n"
            + "TCP/IP connection with reader client established, commencing sending RFID tag search results!\n"
            + "TCP/IP\n"
            + "connection\n"
            + "with";

    @Override
    public void OnDataReceive(TCPConnector connector, DataFrame data) {
        //  System.out.println("Data received: " + new String(data.getBytes()).toString());

        if (new String(data.getBytes()).toString().equalsIgnoreCase(startmsg)) {
            System.out.println("Reader has been freshly Connected");

        }

        //TagParse(new String(data.getBytes()).toString());
     //   WriteLogSwingWorker(new String(data.getBytes()).toString());
        System.out.println(new String(data.getBytes()).toString());
        
//        doinSwingWorker(new String(data.getBytes()).toString());
//  String[] splittedMessage = new String(data.getBytes()).split(fSPT);
        //  System.out.println(splittedMessage);
        //     System.out.println(data.getBytes());
    }

    public static synchronized void appendContents(String sFileName, String sContent) {
        try {

            File oFile = new File(sFileName);
            if (!oFile.exists()) {
                oFile.createNewFile();
            }
            if (oFile.canWrite()) {
                BufferedWriter oWriter = new BufferedWriter(new FileWriter(sFileName, true));
                oWriter.write(sContent);
                oWriter.close();
            }

        } catch (IOException oException) {
            throw new IllegalArgumentException("Error appending/File cannot be written: \n" + sFileName);
        }
    }

  

  

    public void WriteLogSwingWorker(final String msg) {
        if (Logworker != null) {
            Logworker.cancel(true);
        }
        Logworker = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
                appendContents("Dump_other.txt", msg);
                return null;

            }
        };
        Logworker.execute();

    }

    void TagClearing() {
    }

    @Override
    public void OnConnect(TCPConnector connector) {
        System.out.println("Connected to server");
      
    }

    @Override
    public void OnDisconnect(TCPConnector connector) {
        System.out.println("Disconnected from server");
      
        StopSendingMessages();

    }

    public void StartSendingMessages() {
        StopSendingMessages();

    }

    public void StopSendingMessages() {
        if (fAutoSendMessagesThread != null) {
            fAutoSendMessagesThread.Dispose();
            fAutoSendMessagesThread.interrupt();
            fAutoSendMessagesThread = null;
        }
    }
}
