/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package printingOnline;

import Extasys.DataFrame;
import Extasys.Network.TCP.Client.Connectors.Packets.IncomingTCPClientPacket;
import Extasys.Network.TCP.Client.Connectors.Packets.MessageCollectorTCPClientPacket;
import Extasys.Network.TCP.Client.Connectors.Packets.OutgoingTCPClientPacket;
import Extasys.Network.TCP.Client.Connectors.TCPConnector;

/**
 *
 * @author Nikos Siatras
 */
public interface ITCPClient
{

    public void OnDataReceive(TCPConnector connector, DataFrame data);
    public void OnConnect(TCPConnector connector);
    public void OnDisconnect(TCPConnector connector);
    public void FailedToReadTCPPacket(TCPConnector connector, IncomingTCPClientPacket packet);
    public void FailedToReadTCPMessageCollectorPacket(TCPConnector connector, MessageCollectorTCPClientPacket packet);
    public void FailedToSendTCPPacket(TCPConnector connector,OutgoingTCPClientPacket packet);
}