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
import javax.swing.SwingWorker;

/**
 *
 * @author Nikos Siatras
 */
public class TCPClientExit extends Extasys.Network.TCP.Client.ExtasysTCPClient {

    private String fSPT = String.valueOf(((char) 2));

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;

    public static String CONNECTION = "127.0.0.1";
    FileWriter outFile;

    OneReader j;

    public TCPClientExit(String name, String description, InetAddress remoteHostIP, int remoteHostPort, int corePoolSize, int maximumPoolSize) {

        super(name, description, corePoolSize, maximumPoolSize);
        try {

            super.AddConnector(name, remoteHostIP, remoteHostPort, 102400, "\n");
        } catch (Exception ex) {
        }
        System.out.println("In Constructor");
    }

    public TCPClientExit(OneReader jx, String name, String description, InetAddress remoteHostIP, int remoteHostPort, int corePoolSize, int maximumPoolSize) {

        super(name, description, corePoolSize, maximumPoolSize);
        try {
            j = jx;
            super.AddConnector(name, remoteHostIP, remoteHostPort, 10240, "\n");
            System.out.println("In Constructor");
        } catch (Exception ex) {
        }

    }
    String startmsg = "TCP/IP connection with reader client established";

    @Override
    public void OnDataReceive(TCPConnector connector, DataFrame data) {
        //  System.out.println("Data received: " + new String(data.getBytes()).toString());

        if (new String(data.getBytes()).toString().startsWith(startmsg)) {
            System.out.println("Reader has been freshly Connected");

        }
        j.addtoProducer(new String(data.getBytes()).toString(), connector.getName());
        System.out.println(new String(data.getBytes()).toString() + "\t" + connector.getName());
   System.out.println("Attempting on Data Received");
    }

    @Override
    public void OnConnect(TCPConnector connector) {
        System.out.println("Connected to server");

    }

    @Override
    public void OnDisconnect(TCPConnector connector) {
        System.out.println("Disconnected from server");
       

    }

}
