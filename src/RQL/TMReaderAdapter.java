package RQL;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import RQL.InterfaceRQL;

public class TMReaderAdapter {

    private InterfaceRQL queue;
    private Thread thread;
    private PrintWriter readerOut = null;
    private BufferedReader readerIn = null;
    private Socket socket = null;
    private boolean threadExiting = false;
    private long timeAdjustment;
    private String keywordValue;
    private String debugfile = null;
    private PrintWriter filepw = null;
    private String readerEpc = "";
    private String readerEpcUHF1 = "";
    private String readerEpcUHF2 = "";
    private String readerEpcHF1 = "";
    private String readerEpcHF2 = "";
    private String ipaddr = "192.168.1.110";
    private String whereClause = "";
    private int reset = 1;

    private int port = 8080;
    private int repeat = 0;
    private int debug = 0;

    private final static int DEBUG_CMD = 0x0001;
    private final static int DEBUG_RSP = 0x0002;
    private final static int DEBUG_DATA = 0x0004;
    private final static int DEBUG_KEYWORDS = 0x0008;
    private final static int DEBUG_ALIVE = 0x0010;
    private final static int DEBUG_EXCEPTIONS = 0x0020;

    private final static String CURSOR_CMD_BEGIN
            = "DECLARE c1 CURSOR FOR SELECT id,timestamp,readcount,frequency,dspmicros FROM tag_id  ";
    private final static String CURSOR_CMD_END = " INTO '0x%08lX%08lX';";
    private final static String AUTO_ON_CMD = "SET auto c1 = ON, repeat = ";
    private final static String AUTO_OFF_CMD = "SET auto c1 = OFF;";
    private final static String CLOSE_CMD = "CLOSE c1;";

    private final static String[] keywords = {"ipaddr", "port", "repeat", "protocol",
        "antenna", "reset", "debugflags", "readerepc",
        "readerepcUHF1", "readerepcUHF2",
        "readerepcHF1", "readerepcHF2", "debugfile"};

    /* Constructor */
    public TMReaderAdapter(String initString, InterfaceRQL queue) throws IOException {
        String command;
        this.queue = queue;
        parseInitString(initString);			// decode all the parameters

        if (debugfile != null) {
            try {
                filepw = new PrintWriter(new FileWriter(debugfile, true));	// append to existng file
            } catch (Exception e) {
                if ((debug & DEBUG_EXCEPTIONS) != 0) {
                    System.out.println("Exception opening log file " + debugfile + ": " + e.getMessage());
                }
                throw new IOException();
            }
        }

        /* Compute the # of milliseconds between 1/1/1970 and 1/1/2000 to adjust times */
        GregorianCalendar cal = new GregorianCalendar(2000, 0, 1, 0, 0, 0);
        cal.setTimeZone(new SimpleTimeZone(0, "GMT"));
        // timeAdjustment = cal.getTimeInMillis();			// returns millis since 1/1/1970 for this time (not in Java 1.2)
        timeAdjustment = cal.getTime().getTime();			// returns millis since 1/1/1970 for this time

        try {
            socket = new Socket(ipaddr, port);
            readerOut = new PrintWriter(socket.getOutputStream(), true);
            readerIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            if ((debug & DEBUG_EXCEPTIONS) != 0) {
                log("Unable to connect to ThingMagic reader: " + e.getMessage());
            }
            throw new IOException();
        }

        /*  Make up a reader EPC */
        if (readerEpc == "") {
            byte[] addr = socket.getInetAddress().getAddress();
            for (int i = 0; i < 4; i++) {
                String d = Integer.toHexString(addr[i]).toUpperCase();
                if (d.length() == 1) {
                    d = "0" + d;
                }
                if (d.length() > 2) // occurs when byte portion of address appears negative
                {
                    d = d.substring(d.length() - 2);
                }
                readerEpc = readerEpc + d;
            }
            readerEpc = readerEpc + "00000000";
        }

        /*  Start the reader */
        if (reset > 0) {
            tellReader("RESET;", true);
        }
        //command = CURSOR_CMD_BEGIN + whereClause + CURSOR_CMD_END;
        command = CURSOR_CMD_BEGIN + whereClause + ";";
        System.out.println(command);

        tellReader(command, true);				//todo:  handle error if cursor already defined

        command = AUTO_ON_CMD + repeat + ";";
        tellReader(command, true);
        thread = new Thread(new ReaderThread(this));
        thread.setName("ThingMagic Reader Adapter thread");
        thread.setDaemon(true);
        thread.start();

        /*  Create a thread to read on this socket */
        return;
    }

    public void startReading() throws IOException {

    }

    public void reset() {
        try {
            tellReader("RESET;", true);
        } catch (IOException ex) {
            Logger.getLogger(TMReaderAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
     public void custom() {
        try {
            tellReader("SELECT tx_power FROM saved_settings;", true);
        } catch (IOException ex) {
            Logger.getLogger(TMReaderAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
       
     public void customQuery(final String query) {
         
        try {
            tellReader(query, true);
        } catch (IOException ex) {
            Logger.getLogger(TMReaderAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
        public void custom(String dbpower) {
        try {
            tellReader("UPDATE saved_settings SET tx_power='1000';", true);
             tellReader("SELECT tx_power FROM saved_settings;", true);
        } catch (IOException ex) {
            Logger.getLogger(TMReaderAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
        
        
        
         public void customHigh() {
        try {
            tellReader("UPDATE saved_settings SET tx_power='3250';", true);
             tellReader("SELECT tx_power FROM saved_settings;", true);
        } catch (IOException ex) {
            Logger.getLogger(TMReaderAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
         
          public void customLow() {
        try {
            tellReader("UPDATE saved_settings SET tx_power='1000';", true);
             tellReader("SELECT tx_power FROM saved_settings;", true);
        } catch (IOException ex) {
            Logger.getLogger(TMReaderAdapter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     //
    //

    /* Methods */
    public void shutdown() {

        thread = null;					// Tell reader thread to exit
        try {
            tellReader(AUTO_OFF_CMD, false);
            tellReader(CLOSE_CMD, false);
            readerOut.close();
            readerIn.close();
            socket.close();
        } catch (Exception e) {
            log("Exception in TMReaderAdapter.shutdown(): " + e.getMessage());
        } finally {
            socket = null;
        }
        if (filepw != null) {
            filepw.close();
        }
    }

    private void tellReader(String cmd, boolean getResponse) throws IOException {
        String data;
        if ((debug & DEBUG_CMD) != 0) {
            log("Tell reader: " + cmd);
        }
        try {
            readerOut.println(cmd);			// Send command to reader
        } catch (Exception e) {
            if ((debug & DEBUG_EXCEPTIONS) != 0) {
                log("Exception sending command to ThingMagic reader: " + e.getMessage());
            }
            throw new IOException();
        }
        if (!getResponse) {
            return;
        }
        while (true) {					// Loop until success or error.  Ignore data
            try {
                data = readerIn.readLine();
            } catch (Exception e) {
                if ((debug & DEBUG_EXCEPTIONS) != 0) {
                    log("Exception reading command from ThingMagic reader: " + e.getMessage());
                }
                throw new IOException();
            }
            if (data.length() == 0) {
                return;					// success
            }
            if ((debug & DEBUG_RSP) != 0) {
                log("Command error from ThingMagic reader: " + data);
            }
            if (data.length() >= 5) {
                if (data.startsWith("Error")) {
                    if ((debug & DEBUG_EXCEPTIONS) != 0) {
                        log("Command error from ThingMagic reader: " + data);
                    }
                    throw new IOException();
                }
            }
        }
    }

    private void parseInitString(String initString) {
        StringTokenizer st = new StringTokenizer(initString, " ,;\t\n\r\f");
        while (st.hasMoreTokens()) {
            switch (getKeywordValue(st.nextToken(), keywords)) {
                case 0:		// ipaddr
                    ipaddr = keywordValue;
                    break;

                case 1:		// port
                    port = Integer.parseInt(keywordValue);
                    break;

                case 2:		// repeat
                    repeat = Integer.parseInt(keywordValue);
                    break;

                case 3:		// protocol
                    if (whereClause == "") {
                        whereClause = "WHERE protocol_id='" + keywordValue + "'";
                    } else {
                        whereClause = whereClause + " AND protocol_id=" + keywordValue;
                    }
                    break;

                case 4:		// antenna
                    if (whereClause == "") {
                        whereClause = "WHERE antenna_id=" + keywordValue;
                    } else {
                        whereClause = whereClause + " AND antenna_id=" + keywordValue;
                    }
                    break;

                case 5:		// reset
                    reset = Integer.parseInt(keywordValue);
                    break;

                case 6:		// debugflags
                    if ((keywordValue.length() >= 2)
                            && (keywordValue.charAt(0) == '0' && keywordValue.charAt(1) == 'x')) {
                        debug = Integer.parseInt(keywordValue.substring(2), 16);		// debug is only variable supported in hex
                    } else {
                        debug = Integer.parseInt(keywordValue);
                    }
                    break;

                case 7:		// readerepc
                    readerEpc = keywordValue;
                    break;

                case 8:		// readerepcUHF1
                    readerEpcUHF1 = keywordValue;
                    break;

                case 9:		// readerepcUHF2
                    readerEpcUHF2 = keywordValue;
                    break;

                case 10:		// readerepcHF1
                    readerEpcHF1 = keywordValue;
                    break;

                case 11:		// readerepcHF2
                    readerEpcHF2 = keywordValue;
                    break;

                case 12:		// debugfile
                    debugfile = keywordValue;
                    break;

                default:	// Anything else is ignored
            }
        }
    }

    private int getKeywordValue(String token, String[] table) {
        int i, p;
        if ((p = token.indexOf('=')) < 0) {
            return (-1);							// No '='
        }
        String keyword = token.substring(0, p);
        for (i = 0; i < table.length; i++) {
            if (keyword.equalsIgnoreCase(table[i])) {
                keywordValue = token.substring(p + 1);
                if ((debug & DEBUG_KEYWORDS) != 0) {
                    log(keyword + "=" + keywordValue);
                }
                return (i);
            }
        }
        return (-1);										// Keyword not in table
    }

    private void log(String text) {

        if (filepw != null) {
            filepw.println(text);
            filepw.flush();
        } else {
            System.out.println(text);
        }
    }

    class ReaderThread implements Runnable {

        /*  Data */
        private TMReaderAdapter reader;

        /* Constructor */
        ReaderThread(TMReaderAdapter reader) {
            this.reader = reader;
        }

        /*  Methods */
        public void run() {
            long timestamp;
            String data;
            String epc;
            Thread thisThread = Thread.currentThread();
            while (thisThread == thread) {
                try {
                    data = readerIn.readLine();
                    queue.ConsoleMsg(String.valueOf(data.replace("0x", "")));
// wait for next line from reader
                } catch (Exception e) {

                    continue;
                }
//                if (data.length() == 0) {
//                    if ((debug & DEBUG_ALIVE) != 0) {
//                        log("Raw reader data (0)");
//                    }
//                    continue;
//                }
//                if ((debug & DEBUG_DATA) != 0) {
//                    log("Raw reader data (" + data.length() + "): '" + data + "'");
//                }
//                if (data.length() >= 26) {
//                    if (data.charAt(0) == '0' && data.charAt(1) == 'x') {
//
//                        /* Determine which readerEpc to use */
//                        String rdrEpc = "";
//                        int protocol = Integer.parseInt(data.substring(23, 24));
//                        int antenna = Integer.parseInt(data.substring(25, 26));
//                        if (protocol == 1 && antenna == 1) {
//                            rdrEpc = readerEpcUHF1;
//                        }
//                        if (protocol == 1 && antenna == 2) {
//                            rdrEpc = readerEpcUHF2;
//                        }
//                        if (protocol == 3 && antenna == 1) {
//                            rdrEpc = readerEpcHF1;
//                        }
//                        if (protocol == 3 && antenna == 2) {
//                            rdrEpc = readerEpcHF2;
//                        }
//                        if (rdrEpc == "") {
//                            rdrEpc = readerEpc;				// Use default if all else fails
//                        }
//                        /* format data, timestamp, and epc */
//
//                        epc = data.substring(6, 22);			// epc is 16 digits
//                        timestamp = System.currentTimeMillis() - timeAdjustment;
//                        //queue.logEpcEvent(timestamp, epc, rdrEpc);
//                  //      queue.ConsoleMsg(" Antenna ID "+String.valueOf(antenna)+"  "+String.valueOf(timeAdjustment) + " " + String.valueOf(epc) + " " + String.valueOf(rdrEpc));
//                        //  String.valueOf(epc)
//                        continue;
//                    }
//                }
//                if (data.length() >= 5) {
//                    if (data.startsWith("Error")) {
//                        break;
//                    }
//                }
            }  //end while
            // reader.threadExiting = true;
        } //end run

    } //end ReaderThread class
}
