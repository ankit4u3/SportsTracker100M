/*Copyright (c) 2008 Nikos Siatras

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.*/
package printingOnline;

import Extasys.DataFrame;
import Extasys.Network.TCP.Client.Connectors.Packets.IncomingTCPClientPacket;
import Extasys.Network.TCP.Client.Connectors.Packets.MessageCollectorTCPClientPacket;
import Extasys.Network.TCP.Client.Connectors.Packets.OutgoingTCPClientPacket;
import Extasys.Network.TCP.Client.Connectors.TCPConnector;
import Extasys.Network.TCP.Client.Exceptions.*;
import java.net.InetAddress;

/**
 *
 * @author Nikos Siatras
 */
public class TCPChatClient extends Extasys.Network.TCP.Client.ExtasysTCPClient implements ITCPClient {

    private InetAddress fServerIP;
    private int fPort;
    private String fUsername;
    private MainPrinting fMainForm;
    private String fSPT = String.valueOf(((char) 2)); // Message splitter character.
    private String fMCChar = String.valueOf(((char) 3)); // Message collector character.

    public TCPChatClient(InetAddress serverIP, int port, String username, MainPrinting frmMain) {
        super("TCP Chat Client", "", 4, 8);
        fServerIP = serverIP;
        fPort = port;
        fUsername = username;
        fMainForm = frmMain;

        super.AddConnector("Main Connector", serverIP, port, 20480, ((char) 3));
    }

    public void Connect() {
        try {
            super.Start();
        } catch (Exception ex) {
            // fMainForm.MarkAsDisconnected();
        }
    }

    @Override
    public void OnDataReceive(TCPConnector connector, DataFrame data) {
        String[] splittedMessage = new String(data.getBytes()).split(fSPT);
     //   fMainForm.ConsoleMsg(splittedMessage[0]);
        fMainForm.ParseMessageFromServer(splittedMessage[0]);
   
    }

    @Override
    public void OnConnect(TCPConnector connector) {

        SendDataToServer("Login" + fSPT + fUsername);
        System.out.println("DataBase Connected System Stable ...");
        fMainForm.ConsoleMsg("Connection to Server Completed \n Ensure The Readers are Connected Try Sending a Hearbeat for Checking ");

    }

    @Override
    public void OnDisconnect(TCPConnector connector) {
        System.out.println("DataBase On Disconnect ...");
        //  fMainForm.MarkAsDisconnected();
        fMainForm.ConsoleMsg("Connection to Server Failed \n Ensure That Firewall is Down \n ESET SMART SECURITY DISABLE FIREWALL HAS BEEN INTITIATED ");

    }

    public void SendDataToServer(String data) {
        try {
            super.SendData(data + fMCChar);
        } catch (ConnectorDisconnectedException ex) {
            System.err.println(ex.getMessage());
        } catch (ConnectorCannotSendPacketException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void FailedToReadTCPPacket(TCPConnector connector, IncomingTCPClientPacket packet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void FailedToReadTCPMessageCollectorPacket(TCPConnector connector, MessageCollectorTCPClientPacket packet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void FailedToSendTCPPacket(TCPConnector connector, OutgoingTCPClientPacket packet) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
