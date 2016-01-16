package sportstrackeronline;

import Extasys.DataFrame;
import Extasys.Network.TCP.Client.Connectors.TCPConnector;
import Extasys.Network.TCP.Client.Exceptions.ConnectorCannotSendPacketException;
import Extasys.Network.TCP.Client.Exceptions.ConnectorDisconnectedException;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.StringTokenizer;
import java.util.concurrent.BlockingQueue;
import javax.swing.SwingWorker;


/**
 *
 * @author Nikos Siatras
 */

public class TCPClient extends Extasys.Network.TCP.Client.ExtasysTCPClient {

    final BlockingQueue<DelayObject> StartQueue = new java.util.concurrent.DelayQueue<>();
    final BlockingQueue<DelayObject> LapQueue = new java.util.concurrent.DelayQueue<>();
    final BlockingQueue<DelayObject> ExitQueue = new java.util.concurrent.DelayQueue<>();

    private String fSPT = String.valueOf(((char) 2));
    private AutoSendMessages fAutoSendMessagesThread;

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    public int isConnected = 0;
    public static String CONNECTION = "127.0.0.1";
    FileWriter outFile;
    SwingWorker worker;
    SwingWorker Logworker;
    SwingWorker consoleWorker;
    OneReader j;

    
    public TCPClient(String name, String description, InetAddress remoteHostIP, int remoteHostPort, int corePoolSize, int maximumPoolSize) {

        super(name, description, corePoolSize, maximumPoolSize);
        try {

            super.AddConnector(name, remoteHostIP, remoteHostPort, 10240, "\n");
        } catch (Exception ex) {
        }
        System.out.println("In Constructor");
    }

    
    public TCPClient(OneReader jx, String name, String description, InetAddress remoteHostIP, int remoteHostPort, int corePoolSize, int maximumPoolSize) {

        super(name, description, corePoolSize, maximumPoolSize);
        try {
            j = jx;
            super.AddConnector(name, remoteHostIP, remoteHostPort, 10240, "\n");
            System.out.println("In Constructor");
        } catch (Exception ex) {
        }

    }
    
    String startmsg = "TCP/IP";

    
    @Override
    public void OnDataReceive(TCPConnector connector, DataFrame data) {

        
//  System.out.println("Data received: " + new String(data.getBytes()).toString());

        if (new String(data.getBytes()).toString().startsWith(startmsg)) {
            System.out.println("Reader has been freshly Connected");

        }

        System.out.println(new String(data.getBytes()).toString() + "\t" + connector.getName());

        j.addtoProducer(new String(data.getBytes()).toString(), connector.getName());

        try {
            appendContents("Dump.txt", new String(data.getBytes()).toString());
        } catch (Exception e) {

        }

        //j.ParseAndShow(new String(data.getBytes()).toString());
        // TagParse(new String(data.getBytes()).toString(), connector.getName());
    }

    
    public synchronized void addtoProducer(String jacket, String by) {

        switch (by.trim()) {
            case "LAP":
                DelayObject objectLap = new DelayObject(jacket, 20);
                LapQueue.add(objectLap);
                System.out.printf("Put object = %s%n", objectLap);
                break;

            case "START":
                DelayObject objectStart = new DelayObject(jacket, 22);
                StartQueue.add(objectStart);
                System.out.printf("Put object = %s%n", objectStart);
                break;
    
            case "END":
                DelayObject objectEnd = new DelayObject(jacket, 24);
                System.out.printf("Put object = %s%n", objectEnd);
                ExitQueue.add(objectEnd);
                break;

            default:
                appendContents("default.txt", jacket);
                break;

        }
    }

    
    void write2DumpFile() throws IOException {
        outFile = new FileWriter("TimingDump.txt");
        BufferedWriter outStream = new BufferedWriter(outFile);
        String outString = "Race Was Activated @at";
        outStream.append(outString);
        outStream.newLine();
        outStream.close();
        System.out.println("Clients.txt is created\n");
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
         //   throw new IllegalArgumentException("Error appending/File cannot be written: \n" + sFileName);
        }
    }

    public static synchronized void rawTimingSql(String sFileName, String sContent) {
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

    public void doinSwingWorker(final String msg) {
        if (worker != null) {
            worker.cancel(true);
        }
        worker = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
                TagParse(msg);
                return null;

            }
        };
        worker.execute();

    }

    public void WriteLogSwingWorker(final String msg) {
        if (Logworker != null) {
            Logworker.cancel(true);
        }
        Logworker = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
                appendContents("Dump.txt", msg);
                return null;

            }
        };
        Logworker.execute();

    }

    public void WriteSQLLogSwingWorker(final String msg) {
        if (Logworker != null) {
            Logworker.cancel(true);
        }
        Logworker = new SwingWorker() {

            @Override
            protected Object doInBackground() throws Exception {
                appendContents("Dump.txt", msg);
                return null;

            }
        };
        Logworker.execute();

    }

    void TagParseCreateDump(String payload) {

        payload.replace("TCP/IP connection with reader client established, commencing sending RFID tag search results!", "");
        if (payload.startsWith("TAG_ID:")) {

            try {

                String headerLine = payload;

                //System.out.println(headerLine);
                StringTokenizer s = new StringTokenizer(headerLine, ",");

//This prints Tag id
                String tag = s.nextToken().replace("TAG_ID: ", "");

                //This prints TimeStamp
                String timestamp = s.nextToken().replace("TIMESTAMP: ", "");
           //     System.out.println(temp2);

                //This prints Hash Value
                String hash = s.nextToken().replace("HASH: ", "");
           //     System.out.println(temp3);

                //This Prints Antenna Id
                String antenna = s.nextToken().replace("ANTENNA_ID: ", "");
                //     System.out.println(temp4);
//String tag, String timestamp, String hash, String antennaid
                //  rTmysql.write_to_raw_timing(tag.trim(), timestamp.trim(), hash.trim(), antenna.trim());
                System.out.println("(1, 1, '2', 1, 55, 1, '" + hash.trim() + "', '" + timestamp.trim() + "', '" + tag.trim() + "'),\n");
                String sqlHead = "INSERT INTO `raw_timing` (`id`, `version`, `antenna_id`, `canopy_id`, `race_id`, `reader_id`, `reader_tag_hash`, `reader_timestamp`, `tag`)\n"
                        + " VALUES ";
                rawTimingSql("rawtiming.txt", sqlHead + "(default, 1, '2', 1, 55, 1, '" + hash.trim() + "', '" + timestamp.trim() + "', '" + tag.trim() + "');");
            } catch (Exception e) {

                System.out.println(e.getMessage());

            }
        }
    }

    void TagParse(String payload) {

        payload.replace("TCP/IP connection with reader client established, commencing sending RFID tag search results!", "");
        if (payload.startsWith("TAG_ID:")) {

            try {

                String headerLine = payload;

                //System.out.println(headerLine);
                StringTokenizer s = new StringTokenizer(headerLine, ",");

//This prints Tag id
                String tag = s.nextToken().replace("TAG_ID: ", "");

                //This prints TimeStamp
                String timestamp = s.nextToken().replace("TIMESTAMP: ", "");
           //     System.out.println(temp2);

                //This prints Hash Value
                String hash = s.nextToken().replace("HASH: ", "");
           //     System.out.println(temp3);

                //This Prints Antenna Id
                String antenna = s.nextToken().replace("ANTENNA_ID: ", "");

                //    RTref.addtoProducer(payload);
            } catch (Exception e) {

                System.out.println(e.getMessage());

            }
        }

    }

    void TagParse(String payload, String by) {

        payload.replace("TCP/IP connection with reader client established, commencing sending RFID tag search results!", "");
        if (payload.startsWith("TAG_ID:")) {

            try {

                String headerLine = payload;

                //System.out.println(headerLine);
                StringTokenizer s = new StringTokenizer(headerLine, ",");

//This prints Tag id
                String tag = s.nextToken().replace("TAG_ID: ", "");

                //This prints TimeStamp
                String timestamp = s.nextToken().replace("TIMESTAMP: ", "");
           //     System.out.println(temp2);

                //This prints Hash Value
                String hash = s.nextToken().replace("HASH: ", "");
           //     System.out.println(temp3);

                //This Prints Antenna Id
                String antenna = s.nextToken().replace("ANTENNA_ID: ", "");
                //     System.out.println(temp4);
//String tag, String timestamp, String hash, String antennaid
                //   rTmysql.write_to_raw_timing(tag.trim(), timestamp.trim(), hash.trim(), antenna.trim());
                //  addConsole(tag.trim() + "\t" + timestamp.trim() + "\t" + antenna.trim());
                //   addConsole(tag.trim());
                //    System.out.println("Entered to db Successfully");
                System.out.println(payload.trim());
//                RTref.addtoProducer(payload, by);
            } catch (Exception e) {

                System.out.println(e.getMessage());

            }
        }

    }

    @Override
    public void OnConnect(TCPConnector connector) {
        System.out.println("Connected to server");
        isConnected = 1;

    }

    @Override
    public void OnDisconnect(TCPConnector connector) {
        System.out.println("Disconnected from server");
        isConnected = 0;
        appendContents("disconnected.txt", "Reader Was Disconnected");
        // RTref.jLabel1.setText("Disconnected from server");

    }

    public int getConnectedStatus() {
        return isConnected;
    }

    public void StartSendingMessages() {
        StopSendingMessages();
        fAutoSendMessagesThread = new AutoSendMessages(this);
        fAutoSendMessagesThread.start();
    }

    public void StopSendingMessages() {
        if (fAutoSendMessagesThread != null) {
            fAutoSendMessagesThread.Dispose();
            fAutoSendMessagesThread.interrupt();
            fAutoSendMessagesThread = null;
        }
    }
}

class AutoSendMessages extends Thread {

    private TCPClient fMyClient;
    private boolean fActive = true;

    public AutoSendMessages(TCPClient client) {
        fMyClient = client;
    }

    @Override
    public void run() {
        int messageCount = 0;
        while (fActive) {
            try {
                messageCount++;

                fMyClient.SendData(String.valueOf(messageCount) + "#SPLITTER#"); // Char 2 is the message splitter the server's message collector uses.
                System.out.println(String.valueOf(messageCount) + "\n");

                Thread.sleep(10);
            } catch (ConnectorDisconnectedException ex) {
                System.err.println(ex.getConnectorInstance().getName() + " connector disconnected!");
                fActive = false;
                fMyClient.StopSendingMessages();
            } catch (ConnectorCannotSendPacketException ex) {
                System.err.println("Connector " + ex.getConnectorInstance().getName() + " cannot send packet" + ex.getOutgoingPacket().toString());
                fActive = false;
                fMyClient.StopSendingMessages();
            } catch (Exception ex) {
                Dispose();
                fMyClient.StopSendingMessages();
            }
        }
    }

    public void Dispose() {
        fActive = false;
    }
}
