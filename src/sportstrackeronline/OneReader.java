/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sportstrackeronline;

import com.healthmarketscience.jackcess.ColumnBuilder;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Table;
import com.healthmarketscience.jackcess.TableBuilder;
import com.healthmarketscience.jackcess.util.ImportUtil;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ItemEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.security.SecureRandom;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javax.sound.midi.Synthesizer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import org.apache.commons.lang.math.NumberUtils;
import printingOnline.MainPrinting;

/**
 *
 * @author emapps
 */
public class OneReader extends javax.swing.JFrame {

    private static final Random RANDOM = new SecureRandom();
    private TCPChatServer fServer = null;
    private TCPChatServer fServer2 = null;
    public static String LAPCANOPY = "192.168.1.103";
    public static int ACTIVESHIFT = 0;
    public static int ACTIVESHIFTID = 0;
    public static int ACTIVERACEID = 0;
    public static String LAPCANOPYII = "192.168.1.104";
    public static String STARTCANOPY = "192.168.1.102";
    public static String STARTCANOPYII = "192.168.1.105";
    public static String EXITCANOPY = "192.168.1.106";
    public static String EXITCANOPYII = "192.168.1.102";
    static Synthesizer synth;
    HashMap<String, Object> isSend = new HashMap();
    HashMap<String, Object> isExited = new HashMap();
    HashMap<String, Object> incremental = new HashMap();
    HashMap<String, Object> mapper = new HashMap();
    final ReentrantLock rl = new ReentrantLock();
    ArrayList<String> started = new ArrayList<String>();
    ArrayList<String> exited = new ArrayList<String>();
    private static final Random rnd = new Random();
    /**
     *
     * Extrasys THread Controllers
     *
     */
    boolean bypass;
    private TCPClient fTCPClient;
    private TCPClient fStartTCPClient;

    /*
     //Create a New Canopy Reader Handler 
     */
    private TCPClient fEntryReaderTCP;
    private Thread fEntryReaderTCPThread;
    private boolean fEntryReaderTCPStatusActive = true;

    /*
     //Create a New Exit Canopy Reader Handler 
     */
    private TCPClient fExitReaderTCP;
    private Thread fExitReaderTCPThread;
    private boolean fExitReaderTCPStatusActive = true;

    private TCPClient fEntryTCPClient;
    private TCPClient fEntryIITCPClient;
    private TCPClient fExitTCPClient;
    private TCPBuffer BTCPClient;
    private TCPBuffer BTCPClientB;
    private Connection connect = null;
    private Connection connectRegistration = null;
    private Connection connectSports = null;
    private TCPClient fTCPClientB;
    private TCPClientExit fTCPClientExitA;
    private Thread fUpdateStatusThread;
    private Thread fUpdateStartStatusThread;

    private Thread fUpdateEntryStatusThread;
    private Thread fUpdateEntryIIStatusThread;

    private Thread fUpdateExitStatusThread;

    private Thread fUpdateStatusThreadB;
    private boolean fUpdateStatusActive = true;
    private boolean fUpdateStartStatusActive = true;
    private boolean fUpdateEntryStatusActive = true;
    private boolean fUpdateEntryIIStatusActive = true;
    private boolean fUpdateExitStatusActive = true;
    private boolean fUpdateStatusActiveB = true;

    private String fSPT = String.valueOf(((char) 2));
    private PreparedStatement preparedStatement = null;

    /**
     *
     * Producer And Consumer Queue Used
     *
     */
    final BlockingQueue<DelayObject> StartQueue = new java.util.concurrent.DelayQueue<>();
    final BlockingQueue<DelayObject> LapQueue = new java.util.concurrent.DelayQueue<>();
    final BlockingQueue<DelayObject> ExitQueue = new java.util.concurrent.DelayQueue<>();
    //CandidateDelayedQueue
    //CandidateDelayedQueue
    // A Holder to place and save the  candidate raceid and timing for processing 
    final BlockingQueue<CandidateDelayedQueue> CandidateQueue = new java.util.concurrent.DelayQueue<>();
    final BlockingQueue<CandidateDelayedQueue> UpdateServerHostCandidateQueue = new java.util.concurrent.DelayQueue<>();
    final Image image = Toolkit.getDefaultToolkit().getImage("images/clicknrun.png");

    final TrayIcon trayIcon = new TrayIcon(image, "Race Tracker 2014 Online Module");
    Logger logger = Logger.getLogger("MyLog");
    FileHandler fh;

    /**
     * Creates new form OneReader
     */
    public OneReader() {
        initComponents();
        jToolBar1.setVisible(false);

        initializeConnection();
        StartTCPServer();
        StartTCPServer2();
        StartReaderIConsumer();
        ExitReaderIConsumer();
        LapReaderConsumer();
        StartLoggerBusiness();
        StartCandidateConsumer();
        // BurstTimer();
        // BurstTimer();
        //   verifyShiftAndRace();
        //   verifyPassword();
    }

    public void BurstTimer() {
        new SwingWorker<Object, Object>() {

            @Override
            protected Object doInBackground() throws Exception {

                return null;
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }.execute();
    }
    /*
    
     Changes on 13-4 2014 
     Reader id introduced in insert rawtiming
     1 : for Start
     2:  for Lap
     3:  for Exit
    
       
     */

    public void verifyShiftAndRace() {
        String str = JOptionPane.showInputDialog(null, "Kindly Enter the Shift ID : ",
                "Shift ID ", 1);
        if (str != null) {

            JOptionPane.showMessageDialog(null, "You Entered Shift ID : " + str,
                    "JACKET SCANNING", 1);

        }
    }
//ArrayList<String> cats = new ArrayList<String>();
//    cats.add (tag);
//    DefaultComboBoxModel model = new DefaultComboBoxModel(cats.toArray());
//    list_toPrint.setModel (model);

    public void addToStarted(final String chestno) {
        new SwingWorker<Object, Object>() {

            @Override
            protected Object doInBackground() throws Exception {
                started.add(chestno);
                DefaultComboBoxModel model = new DefaultComboBoxModel(started.toArray());
                startedList.setModel(model);
                return null;
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }.execute();

    }

    public void addToExited(final String chestno) {

        new SwingWorker<Object, Object>() {

            @Override
            protected Object doInBackground() throws Exception {
                exited.add(chestno);
                started.remove(chestno);
                DefaultComboBoxModel model = new DefaultComboBoxModel(exited.toArray());
                ExitedList.setModel(model);
                return null;
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }.execute();

    }

    public void addToUnRegistered(final String chestno) {
        new SwingWorker<Object, Object>() {

            @Override
            protected Object doInBackground() throws Exception {
                started.add(chestno);
                DefaultComboBoxModel model = new DefaultComboBoxModel(started.toArray());
                startedList.setModel(model);
                return null;
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }.execute();

    }

    public void verifyPassword() {
        String str = JOptionPane.showInputDialog(null, "Kindly Enter Your Password : ",
                "Todays Password ", 1);
        if (str != null) {

        }
    }

    public void StartLoggerBusiness() {

        try {

            // This block configure the logger with handler and formatter  
            fh = new FileHandler("MyLogFile.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            // the following statement is used to log any messages  
            logger.info("Program Was Started");

        } catch (SecurityException e) {
            e.printStackTrace();

        } catch (IOException ex) {
            Logger.getLogger(OneReader.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

        logger.info("Hi How r u?");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator20 = new javax.swing.JToolBar.Separator();
        jSeparator21 = new javax.swing.JToolBar.Separator();
        jSeparator22 = new javax.swing.JToolBar.Separator();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jToolBar1 = new javax.swing.JToolBar();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        toggleActivate = new javax.swing.JToggleButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        toggleStart = new javax.swing.JToggleButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jToggleButton1 = new javax.swing.JToggleButton();
        jSeparator10 = new javax.swing.JToolBar.Separator();
        toggleLapI = new javax.swing.JToggleButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        toggleLapII = new javax.swing.JToggleButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        toggleExit = new javax.swing.JToggleButton();
        jSeparator6 = new javax.swing.JToolBar.Separator();
        jToggleButton2 = new javax.swing.JToggleButton();
        jSeparator11 = new javax.swing.JToolBar.Separator();
        jButton1 = new javax.swing.JButton();
        jSeparator8 = new javax.swing.JToolBar.Separator();
        jToolBar2 = new javax.swing.JToolBar();
        jSeparator19 = new javax.swing.JToolBar.Separator();
        jLabel1 = new javax.swing.JLabel();
        jSeparator15 = new javax.swing.JToolBar.Separator();
        jLabel2 = new javax.swing.JLabel();
        jSeparator17 = new javax.swing.JToolBar.Separator();
        jLabel3 = new javax.swing.JLabel();
        jSeparator18 = new javax.swing.JToolBar.Separator();
        jLabel4 = new javax.swing.JLabel();
        jSeparator16 = new javax.swing.JToolBar.Separator();
        jLabel5 = new javax.swing.JLabel();
        jSeparator23 = new javax.swing.JToolBar.Separator();
        jLabel6 = new javax.swing.JLabel();
        jSeparator24 = new javax.swing.JToolBar.Separator();
        jLabel8 = new javax.swing.JLabel();
        jSeparator25 = new javax.swing.JToolBar.Separator();
        jLabel7 = new javax.swing.JLabel();
        jSeparator26 = new javax.swing.JToolBar.Separator();
        jLabel12 = new javax.swing.JLabel();
        jSeparator27 = new javax.swing.JToolBar.Separator();
        jLabel11 = new javax.swing.JLabel();
        jSeparator28 = new javax.swing.JToolBar.Separator();
        jSeparator29 = new javax.swing.JToolBar.Separator();
        jButton2 = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        getRaceName = new javax.swing.JTextField();
        newRaceBtn = new javax.swing.JButton();
        jSeparator13 = new javax.swing.JToolBar.Separator();
        jButton7 = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        jButton8 = new javax.swing.JButton();
        jSeparator32 = new javax.swing.JToolBar.Separator();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        console = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jButton9 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        bursttime = new javax.swing.JLabel();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jButton19 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        jButton28 = new javax.swing.JButton();
        jButton33 = new javax.swing.JButton();
        jButton34 = new javax.swing.JButton();
        jToolBar3 = new javax.swing.JToolBar();
        mdbexport = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jToolBar4 = new javax.swing.JToolBar();
        jButton22 = new javax.swing.JButton();
        jSeparator14 = new javax.swing.JToolBar.Separator();
        jButton23 = new javax.swing.JButton();
        jSeparator31 = new javax.swing.JToolBar.Separator();
        jButton24 = new javax.swing.JButton();
        jSeparator30 = new javax.swing.JToolBar.Separator();
        jButton29 = new javax.swing.JButton();
        jSeparator9 = new javax.swing.JToolBar.Separator();
        jButton30 = new javax.swing.JButton();
        jSeparator12 = new javax.swing.JToolBar.Separator();
        jButton32 = new javax.swing.JButton();
        jButton36 = new javax.swing.JButton();
        jButton37 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        ExitedList = new javax.swing.JList();
        jScrollPane4 = new javax.swing.JScrollPane();
        startedList = new javax.swing.JList();
        jPanel6 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jButton31 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jButton35 = new javax.swing.JButton();

        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jList1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jToolBar1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jToolBar1.setRollover(true);
        jToolBar1.add(jSeparator5);

        toggleActivate.setText("Activate Controls");
        toggleActivate.setFocusable(false);
        toggleActivate.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toggleActivate.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(toggleActivate);
        jToolBar1.add(jSeparator1);

        toggleStart.setText("Connect Start Canopy");
        toggleStart.setFocusable(false);
        toggleStart.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toggleStart.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toggleStart.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                toggleStartItemStateChanged(evt);
            }
        });
        toggleStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleStartActionPerformed(evt);
            }
        });
        jToolBar1.add(toggleStart);
        jToolBar1.add(jSeparator2);

        jToggleButton1.setText("Connect Start Reader II");
        jToggleButton1.setFocusable(false);
        jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButton1ItemStateChanged(evt);
            }
        });
        jToolBar1.add(jToggleButton1);
        jToolBar1.add(jSeparator10);

        toggleLapI.setText("Connect Lap Canopy I");
        toggleLapI.setFocusable(false);
        toggleLapI.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toggleLapI.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toggleLapI.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                toggleLapIItemStateChanged(evt);
            }
        });
        jToolBar1.add(toggleLapI);
        jToolBar1.add(jSeparator3);

        toggleLapII.setText("Connect Lap Canopy II");
        toggleLapII.setFocusable(false);
        toggleLapII.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toggleLapII.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toggleLapII.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                toggleLapIIItemStateChanged(evt);
            }
        });
        jToolBar1.add(toggleLapII);
        jToolBar1.add(jSeparator4);

        toggleExit.setText("Connect Exit Canopy ");
        toggleExit.setFocusable(false);
        toggleExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        toggleExit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        toggleExit.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                toggleExitItemStateChanged(evt);
            }
        });
        toggleExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toggleExitActionPerformed(evt);
            }
        });
        jToolBar1.add(toggleExit);
        jToolBar1.add(jSeparator6);

        jToggleButton2.setText("Connect Exit Reader II");
        jToggleButton2.setFocusable(false);
        jToggleButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButton2ItemStateChanged(evt);
            }
        });
        jToolBar1.add(jToggleButton2);
        jToolBar1.add(jSeparator11);

        jButton1.setText("Refresh ");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);
        jToolBar1.add(jSeparator8);

        jToolBar2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), "Bytes/Received"));
        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);
        jToolBar2.add(jSeparator19);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel1.setText("0");
        jToolBar2.add(jLabel1);
        jToolBar2.add(jSeparator15);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel2.setText("0");
        jToolBar2.add(jLabel2);
        jToolBar2.add(jSeparator17);

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel3.setText("0");
        jToolBar2.add(jLabel3);
        jToolBar2.add(jSeparator18);

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel4.setText("0");
        jToolBar2.add(jLabel4);
        jToolBar2.add(jSeparator16);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel5.setText("0");
        jToolBar2.add(jLabel5);
        jToolBar2.add(jSeparator23);

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel6.setText("0");
        jToolBar2.add(jLabel6);
        jToolBar2.add(jSeparator24);

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel8.setText("0");
        jToolBar2.add(jLabel8);
        jToolBar2.add(jSeparator25);

        jLabel7.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel7.setText("0");
        jToolBar2.add(jLabel7);
        jToolBar2.add(jSeparator26);

        jLabel12.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel12.setText("0");
        jToolBar2.add(jLabel12);
        jToolBar2.add(jSeparator27);

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel11.setText("0");
        jToolBar2.add(jLabel11);
        jToolBar2.add(jSeparator28);
        jToolBar2.add(jSeparator29);

        jButton2.setText("PING");
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton2);

        jLabel10.setText("Enter New Race Name : => ");
        jToolBar2.add(jLabel10);

        getRaceName.setText("Enter Race Name for This ");
        getRaceName.setMaximumSize(new java.awt.Dimension(200, 50));
        getRaceName.setMinimumSize(new java.awt.Dimension(200, 50));
        jToolBar2.add(getRaceName);

        newRaceBtn.setText("Create Race");
        newRaceBtn.setFocusable(false);
        newRaceBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        newRaceBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        newRaceBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newRaceBtnActionPerformed(evt);
            }
        });
        jToolBar2.add(newRaceBtn);
        jToolBar2.add(jSeparator13);

        jButton7.setText("RESET");
        jButton7.setFocusable(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jToolBar2.add(jButton7);
        jToolBar2.add(jSeparator7);

        jButton8.setText("CLOSE RACE");
        jButton8.setFocusable(false);
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jButton8.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jButton8FocusGained(evt);
            }
        });
        jToolBar2.add(jButton8);
        jToolBar2.add(jSeparator32);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), "Console Messages ", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));

        console.setBackground(new java.awt.Color(0, 0, 0));
        console.setColumns(20);
        console.setFont(new java.awt.Font("Monospaced", 0, 18)); // NOI18N
        console.setForeground(new java.awt.Color(255, 255, 255));
        console.setRows(5);
        jScrollPane1.setViewportView(console);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 0));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED), "Counter", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP));
        jPanel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 99)); // NOI18N
        jLabel9.setText("0");
        jLabel9.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLabel9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Advance Settings"));
        jPanel3.setBackground(new Color(0,0,0,65));

        jButton9.setText("JACKET COUNT");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jButton12.setText("Start Registration");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        jButton13.setText("Simulate Tags");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.MatteBorder(null), "Scanning Time Remaining "));

        bursttime.setFont(new java.awt.Font("Tahoma", 3, 48)); // NOI18N
        bursttime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        bursttime.setText("0");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(bursttime, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bursttime, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jButton15.setText("Shift Info");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setText("Print Info");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        jButton6.setText("Stop Registration");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton17.setText("EOF");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jButton18.setText("FORCE INSERT");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );

        jButton19.setText("SEND TO ALL");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jButton20.setText("SEND TO PRINTER");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jButton25.setText("MISSING DETECTED");
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        jButton28.setText("RESEND");
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        jButton33.setText("Candidate Q");
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });

        jButton34.setText("Candidate Q Eat");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButton15, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton6)
                            .addComponent(jButton17, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton18, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jButton33)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton34)))
                        .addGap(76, 76, 76)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(229, 229, 229))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton25, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                            .addComponent(jButton19, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(6, 6, 6))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton9)
                    .addComponent(jButton28))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton12)
                    .addComponent(jButton6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton15)
                    .addComponent(jButton17))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton16)
                    .addComponent(jButton18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton25)
                    .addComponent(jButton20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton19)
                    .addComponent(jButton13)))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton33)
                            .addComponent(jButton34))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.getAccessibleContext().setAccessibleName("jck");

        jToolBar3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Extra"));
        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);

        mdbexport.setText("    EXTRACT MSACCESS MDB   ");
        mdbexport.setFocusable(false);
        mdbexport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        mdbexport.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        mdbexport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mdbexportActionPerformed(evt);
            }
        });
        jToolBar3.add(mdbexport);

        jButton10.setText("GET CONNECTED CLIENTS INFO  ");
        jButton10.setFocusable(false);
        jButton10.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton10.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(jButton10);

        jButton11.setText("    GET REGISTERED DETAILS  ");
        jButton11.setFocusable(false);
        jButton11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton11.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        jToolBar3.add(jButton11);

        jButton3.setText("Clear");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar3.add(jButton3);

        jButton5.setText("Reprint");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar3.add(jButton5);

        jButton4.setText("+");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar3.add(jButton4);

        jButton14.setText("R");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jToolBar3.add(jButton14);

        jButton21.setText("Female Module SETUP");
        jButton21.setFocusable(false);
        jButton21.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton21.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });
        jToolBar3.add(jButton21);

        jButton26.setText("START RESULT GENERATION");
        jButton26.setFocusable(false);
        jButton26.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton26.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });
        jToolBar3.add(jButton26);

        jButton27.setText("STOP FORCE CLOSE");
        jButton27.setFocusable(false);
        jButton27.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton27.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });
        jToolBar3.add(jButton27);

        jToolBar4.setFloatable(false);
        jToolBar4.setRollover(true);
        jToolBar4.setEnabled(false);

        jButton22.setText("EXIT CANOPY READERS  =>      ");
        jButton22.setFocusable(false);
        jButton22.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton22.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar4.add(jButton22);
        jToolBar4.add(jSeparator14);

        jButton23.setText("EXIT CANOPY READER I");
        jButton23.setFocusable(false);
        jButton23.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton23.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton23.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jButton23ItemStateChanged(evt);
            }
        });
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });
        jToolBar4.add(jButton23);
        jToolBar4.add(jSeparator31);

        jButton24.setText("EXIT CANOPY READER II");
        jButton24.setFocusable(false);
        jButton24.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton24.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });
        jToolBar4.add(jButton24);
        jToolBar4.add(jSeparator30);

        jButton29.setText("List IP");
        jButton29.setFocusable(false);
        jButton29.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton29.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });
        jToolBar4.add(jButton29);
        jToolBar4.add(jSeparator9);

        jButton30.setText("CLEAR COUNTER TO ZERO");
        jButton30.setFocusable(false);
        jButton30.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton30.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });
        jToolBar4.add(jButton30);
        jToolBar4.add(jSeparator12);

        jButton32.setText("PINGER");
        jButton32.setFocusable(false);
        jButton32.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton32.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });
        jToolBar4.add(jButton32);

        jButton36.setText("PASS IN HIGH JUMP ");
        jButton36.setFocusable(false);
        jButton36.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton36.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar4.add(jButton36);

        jButton37.setText("PASS IN LONG JUMP");
        jButton37.setFocusable(false);
        jButton37.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton37.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar4.add(jButton37);

        ExitedList.setBackground(new java.awt.Color(255, 204, 255));
        jScrollPane3.setViewportView(ExitedList);

        startedList.setBackground(new java.awt.Color(153, 255, 0));
        jScrollPane4.setViewportView(startedList);

        jPanel6.setBackground(new java.awt.Color(255, 255, 0));

        jLabel13.setText("100M Race Controller ");

        jButton31.setBackground(new java.awt.Color(255, 0, 0));
        jButton31.setFont(new java.awt.Font("Tahoma", 1, 48)); // NOI18N
        jButton31.setText("START");
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });
        jButton31.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jButton31FocusGained(evt);
            }
        });

        jTextField1.setText("FOCUS HERE TO CONTINUE TO NEXT RACE");

        jButton35.setText("NEW RACE");
        jButton35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton35ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton31, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                    .addComponent(jTextField1)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jButton35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton35, javax.swing.GroupLayout.DEFAULT_SIZE, 92, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton31, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jToolBar4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 551, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jToolBar3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3)
                            .addComponent(jScrollPane4))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public String searchJacketbyTag(final String jacket) {
        synchronized (this) {
            if (mapper.containsKey(jacket) == true) {
                Object obj = mapper.get(jacket);

                String s = ((jackets) obj).getJacketno();
                return s;
            } else {
                return "0420";
            }

        }

    }

    protected void getJacketHashes() {
        new SwingWorker<Object, Object>() {

            @Override
            protected void done() {
                System.err.println("Total Number of Jackets Loaded into Memory" + mapper.size()); //To change body of generated methods, choose Tools | Templates.
                getRaceIDRestoredOnRrestart();
                jToolBar1.setVisible(true);
            }

            @Override
            protected Object doInBackground() throws Exception {

                jToolBar1.setVisible(false);
                String query = "select * from jacket ";

                Statement stmt = null;
                Dictionary jackets = new Hashtable();

                try {
                    stmt = connect.createStatement();
                    ResultSet rslt = stmt.executeQuery(query);
                    while (rslt.next()) {

                        mapper.put(String.valueOf(rslt.getString("tag_one")), new jackets(rslt.getString("jacket_number"), rslt.getString("tag_one"), rslt.getString("tag_two")));
                        mapper.put(String.valueOf(rslt.getString("tag_two")), new jackets(rslt.getString("jacket_number"), rslt.getString("tag_one"), rslt.getString("tag_two")));

                        Object obj = mapper.get(String.valueOf(rslt.getString("tag_two")));
                        System.out.println(((jackets) obj).getJacketno());

                    }
                    Thread.sleep(2000);

                    // return mapper;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }

                return null;
            }
        }.execute();

    }

    public void initializeConnection() {

        if (SystemTray.isSupported()) {
            SystemTray tray = SystemTray.getSystemTray();

            trayIcon.setImageAutoSize(true);

            try {
                tray.add(trayIcon);
                trayIcon.displayMessage("Tray", "Icon", TrayIcon.MessageType.INFO);
            } catch (AWTException e) {
                System.err.println("TrayIcon could not be added.");
            }
        }
        new SwingWorker<String, String>() {
            @Override
            protected String doInBackground() {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Thread.sleep(1000);
                    connectSports = DriverManager
                            .getConnection("jdbc:mysql://" + "127.0.0.1" + "/sportstracker_dev?"
                                    + "user=RaceAdmin&password=v721PL7y");

                    connect = DriverManager
                            .getConnection("jdbc:mysql://" + "127.0.0.1" + "/sportstracker_dev?"
                                    + "user=RaceAdmin&password=v721PL7y");
                    trayIcon.displayMessage("Connecting to Database Race", "DataBase Connected System Stable ...", TrayIcon.MessageType.INFO);

                    Thread.sleep(1000);
                    connectRegistration = DriverManager
                            .getConnection("jdbc:mysql://" + "127.0.0.1" + "/registration?"
                                    + "user=RaceAdmin&password=v721PL7y");
                    System.out.println("DataBase Connected System Stable ...");
                    trayIcon.displayMessage("Connecting to Database Registration", "DataBase Connected System Stable ...", TrayIcon.MessageType.INFO);
                    Thread.sleep(2000);
                    getJacketHashes();
                    trayIcon.displayMessage("Connecting to Jacket Database", "Saving Jacket Info in Memory ...", TrayIcon.MessageType.INFO);
                    getActiveRaceIDOnStart();
                    getActiveShiftIDOnStart();
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

    public void StartTCPServer() {
        new SwingWorker<Object, Object>() {

            @Override
            protected Object doInBackground() throws Exception {
                rl.lock();
                Thread.currentThread().setName("TCP SERVER");

                ConsoleMsg(Thread.currentThread().getName());

                InetAddress ip = InetAddress.getLocalHost();

                try {
                    //     ConsoleMsg("Starting TCP SERVER......");
                    fServer = new TCPChatServer(InetAddress.getByName(ip.getHostAddress()), Integer.parseInt("5001"), OneReader.this);
                    fServer.Start();

                    //     ConsoleMsg("TCP SERVER..OK....");
                    ConsoleMsg("TCP SERVER..OK....ON PORT 5001     " + ip.getHostAddress());
                    //     ConsoleMsg("NOW START THE PRINTER SERVER AND WAIT TILL IT SHOWS CONNECTED MESSAGES");
                    trayIcon.displayMessage("Connecting", "Server Started " + ip.getHostAddress(), TrayIcon.MessageType.INFO);
                    rl.unlock();
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    ConsoleMsg("FAILED TCP SERVER......");
                    JOptionPane.showMessageDialog(null, ex.getMessage(),
                            "JACKET SCANNING", 1);
                    rl.unlock();

                }

                return null;

            }
        }.execute();

    }

    public void StartTCPServer2() {
        new SwingWorker<Object, Object>() {

            @Override
            protected Object doInBackground() throws Exception {
                rl.lock();
                Thread.currentThread().setName("TCP SERVER");

                ConsoleMsg(Thread.currentThread().getName());

                InetAddress ip = InetAddress.getLocalHost();

                try {
                    //     ConsoleMsg("Starting TCP SERVER......");
                    fServer2 = new TCPChatServer(InetAddress.getByName(ip.getHostAddress()), Integer.parseInt("6001"), OneReader.this);
                    fServer2.Start();

                    //     ConsoleMsg("TCP SERVER..OK....");
                    ConsoleMsg("TCP SERVER..OK....ON PORT 6001     " + ip.getHostAddress());
                    //     ConsoleMsg("NOW START THE PRINTER SERVER AND WAIT TILL IT SHOWS CONNECTED MESSAGES");
                    trayIcon.displayMessage("Connecting", "Server Started " + ip.getHostAddress(), TrayIcon.MessageType.INFO);
                    rl.unlock();
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                    ConsoleMsg("FAILED TCP SERVER......");
                    JOptionPane.showMessageDialog(null, ex.getMessage(),
                            "JACKET SCANNING", 1);
                    rl.unlock();

                }

                return null;

            }
        }.execute();

    }

    public void StartReaderIConsumer() {
        new SwingWorker<Object, Object>() {

            @Override
            protected void done() {
                Tone();
                ConsoleMsg(" START CONTROLLER EXITIED WARNING ERROR OCCURED STOP RACE NOW  ");
                Tone();
            }

            @Override
            protected Object doInBackground() throws Exception {
                ConsoleMsg("Starting Consumer Service for START READER ");
                boolean RunStartReaderAlways = true;

                while (true) {
                    try {

                        DelayObject object = StartQueue.take();
                        ParseAndInsert(object.getData());
                        Thread.sleep(10);
                        ///  this.publish(StartQueue.size());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        ConsoleMsg("SERVICE FAILED FOR  START READER ");

                    }
                }

                //    return null;
            }

        }.execute();

    }

    public void StartCandidateConsumer() {
        new SwingWorker<Object, Object>() {

            @Override
            protected void done() {
                Tone();
                ConsoleMsg(" CANDIDATE  CONTROLLER EXITIED WARNING ERROR OCCURED STOP RACE NOW  ");
                Tone();
            }

            @Override
            protected Object doInBackground() throws Exception {
                ConsoleMsg("Starting CANDIDATE Service for START READER ");
                boolean RunStartReaderAlways = true;

                while (true) {
                    try {

                        CandidateDelayedQueue object = CandidateQueue.take();
                        ConsoleMsg(" D: " + object.getData() + " R: " + object.getRaceid() + " S: " + object.getShiftid());
                        InsertProcessingQueue(String.valueOf(object.getData()), Integer.valueOf(object.getRaceid()), Integer.valueOf(object.getShiftid()));
                        Thread.sleep(10);
                        ///  this.publish(StartQueue.size());
                        if (CandidateQueue.isEmpty()) {
                            jButton31.setEnabled(true);
                        } else {
                            jButton31.setEnabled(false);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        ConsoleMsg("SERVICE FAILED FOR  CANDIDATE READER ");

                    }
                }

                //    return null;
            }

        }.execute();

    }

    
    
      public void StartServerHostUpdater() {
        new SwingWorker<Object, Object>() {

            @Override
            protected void done() {
                Tone();
                ConsoleMsg(" Update to Server Host Failed...  Terminating Program Imminent  ");
                Tone();
            }

            @Override
            protected Object doInBackground() throws Exception {
                ConsoleMsg("Starting Server Host");
                boolean RunStartReaderAlways = true;

                while (true) {
                    try {

                        CandidateDelayedQueue object = UpdateServerHostCandidateQueue.take();
                        ConsoleMsg(" D: " + object.getData() + " R: " + object.getRaceid() + " S: " + object.getShiftid());
                        InsertProcessingQueue(String.valueOf(object.getData()), Integer.valueOf(object.getRaceid()), Integer.valueOf(object.getShiftid()));
                        Thread.sleep(10);
                        ///  this.publish(StartQueue.size());
                        if (UpdateServerHostCandidateQueue.isEmpty()) {
                            jButton31.setEnabled(true);
                        } else {
                            jButton31.setEnabled(false);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        ConsoleMsg("SERVICE FAILED FOR  UpdateServerHostCandidateQueue ");

                    }
                }

                //    return null;
            }

        }.execute();

    }

    
    
    /**
     *
     *
     * @param payload
     */
    public void LapReaderConsumer() {
        new SwingWorker<Object, Object>() {

            @Override
            protected void done() {
                Tone();
                ConsoleMsg(" START CONTROLLER EXITIED WARNING ERROR OCCURED STOP RACE NOW  ");
                Tone();
            }

            @Override
            protected Object doInBackground() throws Exception {
                ConsoleMsg("Starting Consumer Service for LAP  READER ");
                boolean RunStartReaderAlways = true;

                while (true) {
                    try {

                        DelayObject object = LapQueue.take();
                        ParseAndInsertLap(object.getData());
                        Thread.sleep(10);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        ConsoleMsg("SERVICE FAILED FOR  LAP READER");

                    }
                }

                //    return null;
            }

        }.execute();

    }

    public void ExitReaderIConsumer() {
        new SwingWorker<Object, Object>() {

            @Override
            protected void done() {
                Tone();
                ConsoleMsg(" START CONTROLLER EXITIED WARNING ERROR OCCURED STOP RACE NOW  ");
                Tone();
            }

            @Override
            protected Object doInBackground() throws Exception {
                ConsoleMsg("Starting Consumer Service for EXIT READER ");
                boolean RunStartReaderAlways = true;

                while (true) {
                    try {

                        DelayObject object = ExitQueue.take();
                        ParseAndInsertExit(object.getData());
                        Thread.sleep(10);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        ConsoleMsg("SERVICE FAILED FOR  EXIT READER ");

                    }
                }

                //    return null;
            }

        }.execute();

    }

    void Insert_Tags_into_rawtiming(String tag, String timestamp, String hash, String antennaid, int raceid, int readerid) {

//	1`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
//	1`version` BIGINT(20) NOT NULL,
//	2`antenna_id` VARCHAR(255) NULL DEFAULT NULL,
//	3`canopy_id` BIGINT(20) NULL DEFAULT NULL,
//	4`race_id` BIGINT(20) NULL DEFAULT NULL,
//	5`reader_id` BIGINT(20) NULL DEFAULT NULL,
//	6`reader_tag_hash` VARCHAR(255) NULL DEFAULT NULL,
//	7`reader_timestamp` VARCHAR(255) NULL DEFAULT NULL,
//	`8tag` VARCHAR(255) NULL DEFAULT NULL,
        //     long startTime = System.currentTimeMillis();
        try {

            String query = "INSERT INTO raw_timing"
                    + "(id,version,antenna_id,canopy_id,race_id,reader_id,reader_tag_hash,reader_timestamp,tag) "
                    + "VALUES(DEFAULT,?,?,?,?,?,?,?,?)";

            preparedStatement = connect
                    .prepareStatement(query);
//	1`version` BIGINT(20) NOT NULL,
            preparedStatement.setInt(1, 1);
            //	2`antenna_id` VARCHAR(255) NULL DEFAULT NULL,
            preparedStatement.setString(2, antennaid);
            //	3`canopy_id` BIGINT(20) NULL DEFAULT NULL,
            preparedStatement.setInt(3, 1);
            //	4`race_id` BIGINT(20) NULL DEFAULT NULL,
            preparedStatement.setInt(4, raceid);
            //	5`reader_id` BIGINT(20) NULL DEFAULT NULL,
            preparedStatement.setInt(5, readerid);
            //	6`reader_tag_hash` VARCHAR(255) NULL DEFAULT NULL,
            preparedStatement.setString(6, hash);
            //	7`reader_timestamp` VARCHAR(255) NULL DEFAULT NULL,
            preparedStatement.setString(7, timestamp);
            //	`8tag` VARCHAR(255) NULL DEFAULT NULL,
            preparedStatement.setString(8, tag);

            preparedStatement.executeUpdate();

        } catch (Exception e) {

            ConsoleMsg(e.getMessage() + "Error Detected in Inserting Raw_timing");
            appendContents("errors.txt", tag + "," + timestamp + ";");
        } finally {
            appendContents("dump.txt", tag + "," + timestamp + ";");
        }
//        long endTime = System.currentTimeMillis();
//        long elapsedTime = (endTime - startTime) / 1000; //in seconds
//        System.out.println("Total time required to execute 1000 SQL INSERT queries using PreparedStatement without JDBC batch update is :" + elapsedTime);

    }

    public void insertTicket(final String msg) {

        try {

            String query = "INSERT INTO ticket"
                    + "(chestno,raceid,shiftid,type,memo) "
                    + "VALUES(?,?,?,?,?)";

            preparedStatement = connect
                    .prepareStatement(query);
//	1`version` BIGINT(20) NOT NULL,
            preparedStatement.setString(1, msg);
            preparedStatement.setInt(2, ACTIVESHIFT);
            preparedStatement.setInt(3, ACTIVESHIFT);
            preparedStatement.setInt(4, 0);
            preparedStatement.setString(5, "MISSING TAGS ");
            preparedStatement.executeUpdate();

        } catch (Exception e) {

            appendContents("missings.txt", msg + "," + "MISSING WERE ADDED" + ";");
        } finally {

        }

    }

    public static synchronized void appendContents(final String sFileName, final String sContent) {

        new SwingWorker<Object, Object>() {

            @Override
            protected Object doInBackground() throws Exception {

                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
                return null;
            }
        }.execute();
    }

    public void ParseAndInsert(String payload) {
        System.err.println(payload + "Received Tag from La Queue");
        if (payload.startsWith("TAG_ID:")) {
            try {

                String headerLine = payload;
                StringTokenizer s = new StringTokenizer(headerLine, ",");
                String tag = s.nextToken().replace("TAG_ID: ", "");
                String timestamp = s.nextToken().replace("TIMESTAMP: ", "");
                String hash = s.nextToken().replace("HASH: ", "");
                String antenna = s.nextToken().replace("ANTENNA_ID: ", "");
                //   System.out.println(timestamp + " in Parse and Insert " + tag);
                //   System.out.println("Hunman Time" + getTimefromUnixtimestamp(timestamp));

                boolean isinLaps = isSend.containsKey(searchJacketbyTag(tag.trim()));
                if (isinLaps == false) {
                    //     fServer.SendToAllClients(payload + "Jacket No " + searchJacketbyTag(tag.trim()));

                    fServer.SendToAllClients("PRINT: " + "\t" + searchJacketbyTag(tag.trim()).toString());
                    incremental.put(searchJacketbyTag(tag.trim()), searchJacketbyTag(tag.trim()));
                    Thread.sleep(10);
                    fServer.SendToAllClients("COUNTER: " + incremental.size());
                    isSend.put(searchJacketbyTag(tag.trim()), searchJacketbyTag(tag.trim()));

                    /*
                    
                    
                     */
                    jLabel9.setText(String.valueOf(isSend.size()));
                    ConsoleMsg("Jacket Started The Race : = > " + searchJacketbyTag(tag.trim()));
                    appendContents("SendLog.txt", searchJacketbyTag(tag.trim()) + "\n,");

                }
                //      ConsoleMsg("Lap Canopy Se Gujra " + searchJacketbyTag(tag.trim()));
// autocounter.setSelected(true);
                Insert_Tags_into_rawtiming(tag, timestamp, searchJacketbyTag(tag.trim()), antenna, ACTIVESHIFT, 1);

            } catch (Exception e) {
                ConsoleMsg(e.getMessage());

                logger.info(e.getMessage() + "Error Detected While Inserting ");

                //  JOptionPane.showMessageDialog(OneReader.this, "here comes the text.");
            } finally {

            }
        }

    }

    public void ParseAndInsertExit(String payload) {
        System.err.println(payload + "Received Tag from Exit Queue");
        if (payload.startsWith("TAG_ID:")) {
            try {

                String headerLine = payload;
                StringTokenizer s = new StringTokenizer(headerLine, ",");
                String tag = s.nextToken().replace("TAG_ID: ", "");
                String timestamp = s.nextToken().replace("TIMESTAMP: ", "");
                String hash = s.nextToken().replace("HASH: ", "");
                String antenna = s.nextToken().replace("ANTENNA_ID: ", "");
                //   System.out.println(timestamp + " in Parse and Insert " + tag);
                //   System.out.println("Hunman Time" + getTimefromUnixtimestamp(timestamp));

                boolean isinExit = isExited.containsKey(searchJacketbyTag(tag.trim()));
                if (isinExit == false) {
                    //     fServer.SendToAllClients(payload + "Jacket No " + searchJacketbyTag(tag.trim()));

                    fServer.SendToAllClients("PRINTEXIT: " + "\t" + searchJacketbyTag(tag.trim()).toString());

                    /*
                    
                    
                     All the Exiting tags received here will be added to 
                     the Candidate Processing Queue
                    
                    
                     */
                    addToCandidateQueue(searchJacketbyTag(tag.trim()).toString());
                    Thread.sleep(10);
                    fServer.SendToAllClients("EXITED: " + "\t" + searchJacketbyTag(tag.trim()).toString());
                    isExited.put(searchJacketbyTag(tag.trim()), searchJacketbyTag(tag.trim()));
                    Thread.sleep(5);
                    //fServer.SendToAllClients("COUNTER:" + String.valueOf(isSend.size()));
                    //Thread.sleep(5);
                    /*
                     removes the jacket number from the isSend Queue
                     */
                    isSend.remove(searchJacketbyTag(tag.trim()));

                    /*
                     Updates the fied count  
                     */
                    jLabel9.setText(String.valueOf(isSend.size()));

                    started.add(searchJacketbyTag(tag.trim()).toString());
                    DefaultComboBoxModel model = new DefaultComboBoxModel(started.toArray());
                    startedList.setModel(model);
                    ConsoleMsg("Jacket Exited The Race : = > " + searchJacketbyTag(tag.trim()));

                    appendContents("SendLog.txt", searchJacketbyTag(tag.trim()) + "\n,");

                }
                //    ConsoleMsg("Exit Canopy Se Exit Kiya " + searchJacketbyTag(tag.trim()));
// autocounter.setSelected(true);
                Insert_Tags_into_rawtiming(tag, timestamp, searchJacketbyTag(tag.trim()), antenna, ACTIVESHIFT, 3);

            } catch (Exception e) {
                ConsoleMsg(e.getMessage());

                logger.info(e.getMessage() + "Error Detected While Inserting ");

                //  JOptionPane.showMessageDialog(OneReader.this, "here comes the text.");
            } finally {

            }
        }

    }

    public void ParseAndInsertStart(String payload) {
        System.err.println(payload + "Received Tag from Start Queue");
        if (payload.startsWith("TAG_ID:")) {
            try {

                String headerLine = payload;
                StringTokenizer s = new StringTokenizer(headerLine, ",");
                String tag = s.nextToken().replace("TAG_ID: ", "");
                String timestamp = s.nextToken().replace("TIMESTAMP: ", "");
                String hash = s.nextToken().replace("HASH: ", "");
                String antenna = s.nextToken().replace("ANTENNA_ID: ", "");
                //  System.out.println(timestamp + " in Parse and Insert " + tag);
                //   System.out.println("Hunman Time" + getTimefromUnixtimestamp(timestamp));

                boolean isinLaps = isSend.containsKey(searchJacketbyTag(tag.trim()));
                if (isinLaps == false) {
                    //     fServer.SendToAllClients(payload + "Jacket No " + searchJacketbyTag(tag.trim()));

                    fServer.SendToAllClients("PRINT: " + "\t" + searchJacketbyTag(tag.trim()).toString());
                    Thread.sleep(10);
                    isSend.put(searchJacketbyTag(tag.trim()), searchJacketbyTag(tag.trim()));
                    jLabel9.setText(String.valueOf(isSend.size()));
                    ConsoleMsg("Jacket Started The Race : = > " + searchJacketbyTag(tag.trim()));

                    appendContents("SendLog.txt", searchJacketbyTag(tag.trim()) + "\n,");
                }
                ConsoleMsg("Lap Canopy Se Gujra " + searchJacketbyTag(tag.trim()));
// autocounter.setSelected(true);
                Insert_Tags_into_rawtiming(tag, timestamp, searchJacketbyTag(tag.trim()), antenna, ACTIVESHIFT, 1);

            } catch (Exception e) {
                ConsoleMsg(e.getMessage());

                logger.info(e.getMessage() + "Error Detected While Inserting ");

                //  JOptionPane.showMessageDialog(OneReader.this, "here comes the text.");
            } finally {

            }
        }

    }

    public void ParseAndInsertLap(String payload) {
        System.err.println(payload + "Received Tag from Lap Queue");
        if (payload.startsWith("TAG_ID:")) {
            try {

                String headerLine = payload;
                StringTokenizer s = new StringTokenizer(headerLine, ",");
                String tag = s.nextToken().replace("TAG_ID: ", "");
                String timestamp = s.nextToken().replace("TIMESTAMP: ", "");
                String hash = s.nextToken().replace("HASH: ", "");
                String antenna = s.nextToken().replace("ANTENNA_ID: ", "");
                System.out.println(timestamp + " in Parse and Insert " + tag);
                //  System.out.println("Hunman Time" + getTimefromUnixtimestamp(timestamp));

                boolean isinLaps = isSend.containsKey(searchJacketbyTag(tag.trim()));
                if (isinLaps == false) {
                    //     fServer.SendToAllClients(payload + "Jacket No " + searchJacketbyTag(tag.trim()));

                    fServer.SendToAllClients("PRINT: " + "\t" + searchJacketbyTag(tag.trim()).toString());
                    System.out.println("Send Print Singnal");
                    Thread.sleep(10);
                    fServer.SendToAllClients("COUNTER: " + searchJacketbyTag(tag.trim()).toString());
                    incremental.put(searchJacketbyTag(tag.trim()), searchJacketbyTag(tag.trim()));
                    jLabel9.setText(String.valueOf(incremental.size()));
                    System.out.println("Send Started Print Singnal");
                    Thread.sleep(10);
                    isSend.put(searchJacketbyTag(tag.trim()), searchJacketbyTag(tag.trim()));
                    Tone();
//    addToStarted(searchJacketbyTag(tag.trim().toString()));
                    //   fServer.SendToAllClients("COUNTER:" + String.valueOf(isSend.size()));
                    jLabel9.setText(String.valueOf(isSend.size()));

                    ConsoleMsg("Jacket Started The Race : = > " + searchJacketbyTag(tag.trim()));

                    appendContents("SendLog.txt", searchJacketbyTag(tag.trim()) + "\n,");
                }
                ConsoleMsg("Lap Canopy Se Gujra " + searchJacketbyTag(tag.trim()));
// autocounter.setSelected(true);
                Insert_Tags_into_rawtiming(tag, timestamp, searchJacketbyTag(tag.trim()), antenna, ACTIVESHIFT, 2);

            } catch (Exception e) {
                ConsoleMsg(e.getMessage());

                logger.info(e.getMessage() + "Error Detected While Inserting ");

                //  JOptionPane.showMessageDialog(OneReader.this, "here comes the text.");
            } finally {

            }
        }

    }

    public String getTimeDifffromUnixtimestamp(String maxtime, String timetwo) {

        try {

            Statement st = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            st = connect.createStatement();

            ResultSet rs = st.executeQuery("select time_to_sec(timediff(max(date_format(from_unixtime(" + maxtime + "),' %T')),min(date_format(from_unixtime(" + timetwo + "),' %T')))) as diff");

            // get the number of rows from the result set
            rs.next();
            String rowCount = rs.getString(1);
            System.out.println(rowCount);

            return (String.valueOf(rowCount));
        } catch (SQLException ex) {

        }
        return null;
    }

    public String getTimefromUnixtimestamp(String jacket) {

        try {

            Statement st = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE);

            st = connect.createStatement();

            ResultSet rs = st.executeQuery("select max(date_format(from_unixtime(" + jacket + "),'%d %b %Y %T')) AS `one`");

            // get the number of rows from the result set
            rs.next();
            String rowCount = rs.getString(1);
            System.out.println(rowCount);

            return (String.valueOf(rowCount));
        } catch (SQLException ex) {

        }
        return null;
    }

    public static String unixTimestampToString(long t) {
        String str = null;
        long d = t * 1000L;
        str = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date(d));
        return str;
    }
    private void toggleStartItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_toggleStartItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {

            Thread threadIncrement = new Thread() {
                @Override
                public void run() {

                    try {
                        String one = "1000";
                        String port = "8802";
                        fStartTCPClient = new TCPClient(OneReader.this, "START", "LAP", InetAddress.getByName(STARTCANOPY),
                                Integer.parseInt(port), Integer.parseInt(one),
                                Integer.parseInt(one));
                        fUpdateStartStatusActive = true;
                        fUpdateStartStatusThread = new Thread(new Runnable() {

                            public void run() {
                                try {
                                    while (fUpdateStartStatusActive) {
                                        if (fStartTCPClient != null) {

                                            jLabel1.setText(String.valueOf(fStartTCPClient.getBytesIn()));
                                            //   jLabel3.setText(String.valueOf(fEntryTCPClient.getBytesOut()));

                                        }
                                        Thread.sleep(10);
                                    }
                                } catch (Exception ex) {
                                    System.err.println("<fUpdateStatusThread>" + ex.getMessage());
                                }
                            }
                        });

                        fStartTCPClient.Start();
                        fUpdateStartStatusThread.start();

                    } catch (Exception ex) {
                    }
                }

            };
            threadIncrement.start();
        }
        if (evt.getStateChange() == ItemEvent.DESELECTED) {

            if (fStartTCPClient != null) {

                fStartTCPClient.Stop();
                fUpdateStartStatusThread.interrupt();

            }
        }
    }//GEN-LAST:event_toggleStartItemStateChanged

    private void toggleLapIItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_toggleLapIItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {

            Thread threadIncrement = new Thread() {
                @Override
                public void run() {
                    try {
                        String one = "1";
                        String port = "8802";
                        fEntryTCPClient = new TCPClient(OneReader.this, "END", "LAP", InetAddress.getByName(LAPCANOPY),
                                Integer.parseInt(port), Integer.parseInt(one),
                                Integer.parseInt(one));
                        fUpdateEntryStatusActive = true;
                        fUpdateEntryStatusThread = new Thread(new Runnable() {

                            public void run() {
                                try {
                                    while (fUpdateEntryStatusActive) {
                                        if (fEntryTCPClient != null) {

                                            jLabel2.setText(String.valueOf(fEntryTCPClient.getBytesIn()));
                                            //   jLabel3.setText(String.valueOf(fEntryTCPClient.getBytesOut()));

                                        }
                                        Thread.sleep(10);
                                    }
                                } catch (Exception ex) {
                                    System.err.println("<fUpdateStatusThread>" + ex.getMessage());
                                }
                            }
                        });

                        fEntryTCPClient.Start();
                        fUpdateEntryStatusThread.start();

                    } catch (Exception ex) {
                    }
                }

            };
            threadIncrement.start();
        }
        if (evt.getStateChange() == ItemEvent.DESELECTED) {
            if (fEntryTCPClient != null) {
                fEntryTCPClient.Stop();
                fUpdateEntryStatusThread.interrupt();

            }
        }
    }//GEN-LAST:event_toggleLapIItemStateChanged

    private void toggleLapIIItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_toggleLapIIItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {

            Thread threadIncrement = new Thread() {
                @Override
                public void run() {
                    try {
                        String one = "1";
                        String port = "8802";
                        fEntryIITCPClient = new TCPClient(OneReader.this, "END", "LAP", InetAddress.getByName(LAPCANOPYII),
                                Integer.parseInt(port), Integer.parseInt(one),
                                Integer.parseInt(one));
                        fUpdateEntryIIStatusActive = true;
                        fUpdateEntryIIStatusThread = new Thread(new Runnable() {

                            public void run() {
                                try {
                                    while (fUpdateEntryIIStatusActive) {
                                        if (fEntryIITCPClient != null) {

                                            jLabel3.setText(String.valueOf(fEntryIITCPClient.getBytesIn()));
                                            //   jLabel3.setText(String.valueOf(fEntryTCPClient.getBytesOut()));

                                        }
                                        Thread.sleep(10);
                                    }
                                } catch (Exception ex) {
                                    System.err.println("<fUpdateIIStatusThread>" + ex.getMessage());
                                }
                            }
                        });

                        fEntryIITCPClient.Start();
                        fUpdateEntryIIStatusThread.start();

                    } catch (Exception ex) {
                    }
                }

            };
            threadIncrement.start();
        }
        if (evt.getStateChange() == ItemEvent.DESELECTED) {
            if (fEntryIITCPClient != null) {
                fEntryIITCPClient.Stop();
                fUpdateEntryIIStatusThread.interrupt();

            }
        }

    }//GEN-LAST:event_toggleLapIIItemStateChanged

    private void toggleExitItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_toggleExitItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {

            Thread threadIncrement = new Thread() {
                @Override
                public void run() {
                    try {
                        String one = "1";
                        String port = "8802";
                        fExitTCPClient = new TCPClient(OneReader.this, "END", "LAP", InetAddress.getByName(EXITCANOPY),
                                Integer.parseInt(port), Integer.parseInt(one),
                                Integer.parseInt(one));
                        fUpdateExitStatusActive = true;
                        fUpdateExitStatusThread = new Thread(new Runnable() {

                            public void run() {
                                try {
                                    while (fUpdateExitStatusActive) {
                                        if (fExitTCPClient != null) {

                                            jLabel4.setText(String.valueOf(fExitTCPClient.getBytesIn()));

                                        }
                                        Thread.sleep(10);
                                    }
                                } catch (Exception ex) {
                                    System.err.println("<fUpdateExitStatusThread>" + ex.getMessage());
                                }
                            }
                        });

                        fExitTCPClient.Start();
                        fUpdateExitStatusThread.start();

                    } catch (Exception ex) {
                    }
                }

            };
            threadIncrement.start();
        }
        if (evt.getStateChange() == ItemEvent.DESELECTED) {
            if (fExitTCPClient != null) {

                fExitTCPClient.Stop();
                fUpdateExitStatusThread.interrupt();

            }
        }

    }//GEN-LAST:event_toggleExitItemStateChanged

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        fServer.SendToAllClients("REBOOT: HELLO FROM SERVER PING TEST");
        //   printDataSet();

// TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed


    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed

        new SwingWorker<Object, Object>() {

            @Override
            protected Object doInBackground() throws Exception {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery("select * from raw_timing where race_id=" + ACTIVESHIFT);

                // It creates and displays the table
                JTable table = new JTable();

                table.setModel(buildTableModel(rs));
                //   table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

                JOptionPane.showMessageDialog(null, new JScrollPane(table));

//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                return null;
            }

        }.execute();

//        final java.util.Timer timer = new java.util.Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//            int i = Integer.parseInt("30");
//
//            public void run() {
//
//                if (i < 0) {
//                    timer.cancel();
//
//                }
//            }
//        }, 0, 1000);      // TODO add your handling code here:
    }//GEN-LAST:event_jButton9ActionPerformed

    private void newRaceBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newRaceBtnActionPerformed
        logger.info("New Race was Attempted to Be Created");
        Statement st;
        try {
            st = connect.createStatement();
            st.execute("INSERT INTO activerace (RaceName) VALUES ('" + getRaceName.getText().toUpperCase() + "')",
                    Statement.RETURN_GENERATED_KEYS);

            ResultSet keys = st.getGeneratedKeys();
            int id = 1;
            while (keys.next()) {
                id = keys.getInt(1);
                System.err.println(id);

            }
            trayIcon.displayMessage("Race Tracker Update", "New Race Has Been Created ", TrayIcon.MessageType.INFO);

        } catch (SQLException ex) {
            trayIcon.displayMessage("Race Creation Failed", "Can't Create A New Race Contact Developer", TrayIcon.MessageType.WARNING);
            Logger
                    .getLogger(OneReader.class
                            .getName()).log(Level.SEVERE, null, ex);
            logger.info(ex.getMessage());
        }

// TODO add your handling code here:
    }//GEN-LAST:event_newRaceBtnActionPerformed

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            NumberUtils.isDigits("1");

            NumberUtils.isNumber("22");
            return false;
        }
        return true;
    }

    public void fixMissingTags() {
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connectorin = DriverManager
                    .getConnection("jdbc:mysql://" + "127.0.0.1" + "/sportstracker_dev?"
                            + "user=RaceAdmin&password=v721PL7y");

            stmt = connectorin.createStatement();
            ResultSet rslt = stmt.executeQuery("select t.*  from\n"
                    + "(SELECT a,\n"
                    + "             ceil(@diff)                              AS starttime,\n"
                    + "             ceil(b)                                        endtime,\n"
                    + "             IF(@diff = 0, 0, ceil(b - @diff) ) AS diff,\n"
                    + "             @diff := b\n"
                    + "      FROM   p,\n"
                    + "             (SELECT @diff := 0) AS x\n"
                    + "      \n"
                    + "			ORDER  BY p.b) t");

            while (rslt.next()) {
                String a = rslt.getString("a");
                int starttime = (int) (Integer.valueOf(rslt.getString("starttime")));
                int endtime = (int) (Integer.valueOf(rslt.getString("endtime")));
                int diff = (int) (Integer.valueOf(rslt.getString("diff")));

                System.out.println(a + " " + starttime + " " + endtime + " " + diff);
                ConsoleMsg(a + " " + starttime + " " + endtime + " " + diff);
                System.out.println();
//                        System.out.println(endtime);
//                        System.out.println(diff);

                if (diff == 0) {
                    ConsoleMsg("nothing to fix");
                }
                if (diff > 115 && diff < 249) {
                    System.out.println(a + "has a lap missing");
                    System.out.println(" to be added  " + (endtime - diff));
                    ConsoleMsg(" to be added  " + (endtime - diff));

                    fixTiming(a, endtime);

                }
                if (diff > 250 && diff < 350) {
                    System.out.println(a + "has 2 lap missing");
                    ConsoleMsg(a + "has 2 lap missing");
                    fixTiming(a, endtime);
                    fixTiming(a, endtime);
                }
                if (diff > 351) {
                    System.out.println(a + "has a lot of missing laps");
                    ConsoleMsg(a + "has 2 lap missing");
                    fixTiming(a, endtime - 120);
                    fixTiming(a, endtime - 60);
                    fixTiming(a, endtime - 30);

                }

            }

            rslt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }

    }

    public synchronized void fixTiming(String jacket, int time) {
        //(endtime - diff)
        try {
            PreparedStatement pStmnt = null;

            Class.forName(
                    "com.mysql.jdbc.Driver");
            Connection connectorin = DriverManager
                    .getConnection("jdbc:mysql://" + "127.0.0.1" + "/sportstracker_dev?"
                            + "user=RaceAdmin&password=v721PL7y");
            String preQueryStatement = "INSERT  INTO  sportstracker_dev.p  VALUES  (?,?,?)";
            pStmnt = connectorin.prepareStatement(preQueryStatement);

            pStmnt.setString(1, jacket);
            pStmnt.setInt(2, time);
            pStmnt.setInt(3, time);
            pStmnt.executeUpdate();
            ConsoleMsg("MIssing were fixed");
        } catch (Exception e) {
            ConsoleMsg("Error with fixing detected");
        }

    }

    public void getActiveRaceIDOnStart() {
        Statement st;
        try {
            PreparedStatement pstmt = null;
            String query = "select RaceName,id from activerace where id=(select max(id) from activerace)";
            pstmt = connect.prepareStatement(query);
            ResultSet keys = pstmt.executeQuery();
            int id;
            String idname;
            while (keys.next()) {
                idname = keys.getString(1);
                id = keys.getInt(2);
                System.err.println(id + " : " + idname);
                trayIcon.displayMessage("Your Race ID is ", id + " : " + idname, TrayIcon.MessageType.WARNING);
                ACTIVESHIFT = id;

            }
        } catch (SQLException ex) {
            Logger.getLogger(OneReader.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getActiveShiftIDOnStart() {
        Statement st;
        try {
            PreparedStatement pstmt = null;
            String query = "select RaceName,id,shift from activerace where id=(select max(id) from activerace)";
            pstmt = connectSports.prepareStatement(query);
            ResultSet keys = pstmt.executeQuery();
            int id;
            int idRace;
            String idname;
            while (keys.next()) {
                idname = keys.getString(1);
                id = keys.getInt(3);
                idRace = keys.getInt(2);
                System.err.println(id + " : " + idname);
                trayIcon.displayMessage("Your Shift ID is ", id + " : " + idname, TrayIcon.MessageType.WARNING);
                ConsoleMsg("Shift ID :" + id + "  Race Name " + idname + " With Race ID: " + idRace);
                ACTIVESHIFTID = id;

            }
        } catch (SQLException ex) {
            Logger.getLogger(OneReader.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        Statement st;
        try {
            PreparedStatement pstmt = null;
            String query = "select RaceName,id from activerace where id=(select max(id) from activerace)";
            pstmt = connect.prepareStatement(query);
            ResultSet keys = pstmt.executeQuery();
            int id;
            String idname;
            while (keys.next()) {
                idname = keys.getString(1);
                id = keys.getInt(2);
                System.err.println(id + " : " + idname);
                trayIcon.displayMessage("Your Race ID is ", id + " : " + idname, TrayIcon.MessageType.WARNING);
                ACTIVESHIFT = id;

            }
        } catch (SQLException ex) {
            Logger.getLogger(OneReader.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

// TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    public void getRaceIDRestoredOnRrestart() {
        Statement st;
        try {
            PreparedStatement pstmt = null;
            String query = "select RaceName,id from activerace where id=(select max(id) from activerace)";
            pstmt = connect.prepareStatement(query);
            ResultSet keys = pstmt.executeQuery();
            int id;
            String idname;
            while (keys.next()) {
                idname = keys.getString(1);
                id = keys.getInt(2);
                System.err.println(id + " : " + idname);
                trayIcon.displayMessage("Your Race ID is ", id + " : " + idname, TrayIcon.MessageType.WARNING);
                ACTIVESHIFT = id;

            }
        } catch (SQLException ex) {
            Logger.getLogger(OneReader.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed

        fServer.SendToAllClients("REBOOT: START REGISTRATION NOW ");
// TODO add your handling code here:
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed

        new SwingWorker<Object, Object>() {

            @Override
            protected void done() {
                ConsoleMsg(" Sending to Printer TASK COMPLETED");
            }

            @Override
            protected Object doInBackground() throws Exception {
                rl.lock();
                Statement stmt;
                try {
                    stmt = connect.createStatement();
                    ResultSet rslt = stmt.executeQuery("select distinct(raw_timing.reader_tag_hash) from raw_timing where race_id=" + ACTIVESHIFT);

                    while (rslt.next()) {
                        String a = rslt.getString("reader_tag_hash");
                        System.out.println(a);
                        ConsoleMsg(" Sending to Printer for Laps Counting and Printing " + a);
                        fServer.SendToAllClients("PRINT: " + a);
                        Thread.sleep(150);
                        appendContents("SendLog.txt", "\n" + a);
                    }
                    rl.unlock();

                } catch (SQLException ex) {
                    Logger.getLogger(OneReader.class
                            .getName()).log(Level.SEVERE, null, ex);

                }

                return null;
            }
        }
                .execute();

// TODO add your handling code here:
    }//GEN-LAST:event_jButton13ActionPerformed

    public synchronized void fixonLapCheck() {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {

                Statement stmt = null;
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection connectorin = DriverManager
                            .getConnection("jdbc:mysql://" + "127.0.0.1" + "/sportstracker_dev?"
                                    + "user=RaceAdmin&password=v721PL7y");

                    stmt = connectorin.createStatement();
                    ResultSet rslt = stmt.executeQuery("select t.*  from\n"
                            + "(SELECT a,\n"
                            + "             ceil(@diff)                              AS starttime,\n"
                            + "             ceil(b)                                        endtime,\n"
                            + "             IF(@diff = 0, 0, ceil(b - @diff) ) AS diff,\n"
                            + "             @diff := b\n"
                            + "      FROM   p,\n"
                            + "             (SELECT @diff := 0) AS x\n"
                            + "      \n"
                            + "			ORDER  BY p.b) t");

                    while (rslt.next()) {
                        String a = rslt.getString("a");
                        int starttime = (int) (Integer.valueOf(rslt.getString("starttime")));
                        int endtime = (int) (Integer.valueOf(rslt.getString("endtime")));
                        int diff = (int) (Integer.valueOf(rslt.getString("diff")));

                        System.out.println(a + " " + starttime + " " + endtime + " " + diff);

                        System.out.println();
//                        System.out.println(endtime);
//                        System.out.println(diff);

                        if (diff == 0) {

                        }
                        if (diff > 115 && diff < 249) {
                            System.out.println(a + "has a lap missing");
                            System.out.println(" to be added  " + (endtime - diff));

                        }
                        if (diff > 250 && diff < 350) {
                            System.out.println(a + "has 2 lap missing");

                        }
                        if (diff > 351) {
                            System.out.println(a + "has a lot of missing laps");

                        }

                    }

                    rslt.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {

                }

                return null;
            }
        }.execute();

    }


    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        console.setText("");
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed

        new SwingWorker<Object, Object>() {

            @Override
            protected Object doInBackground() throws Exception {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery("select * from activerace");

                // It creates and displays the table
                JTable table = new JTable();

                table.setModel(buildTableModel(rs));
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                JOptionPane.showMessageDialog(null, new JScrollPane(table));
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                return null;
            }

        }.execute();
// TODO add your handling code here:
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        new SwingWorker<Object, Object>() {

            @Override
            protected Object doInBackground() throws Exception {
                Statement stmt = connect.createStatement();
                ResultSet rs = stmt.executeQuery("select * from lapdetails");

                // It creates and displays the table
                JTable table = new JTable();

                table.setModel(buildTableModel(rs));
                table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

                JOptionPane.showMessageDialog(null, new JScrollPane(table));

//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                return null;
            }

        }.execute();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton16ActionPerformed

    void gethprbMarks() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
        connect = DriverManager
                .getConnection("jdbc:mysql://" + "127.0.0.1" + "/hprb100?"
                        + "user=root&password=toor");

        String query = "select 100m.DESM_ESM,100m.800_barcode,100m.800_Jacket,100m.100_timetaken,(\n"
                + "case when \n"
                + "100m.DESM_ESM=1 and 100m.100_timetaken  between 0 and 16 then '5'\n"
                + "when 100m.DESM_ESM=1 and 100m.100_timetaken between 16 and 19 then '3'\n"
                + "when 100m.DESM_ESM=1 and 100m.100_timetaken between 19 and 300 then '0'\n"
                + "\n"
                + "\n"
                + "when 100m.DESM_ESM=2 and 100m.100_timetaken  between 0 and 14.5 then '5'\n"
                + "when 100m.DESM_ESM=2 and 100m.100_timetaken between 14.5 and 15.5 then '3'\n"
                + "when 100m.DESM_ESM=2 and 100m.100_timetaken between 15.5 and 300 then '0'\n"
                + "end) as '100mMarks',100m.800_timetaken,100m.Laps,\n"
                + "(case \n"
                + "\n"
                + "when 100m.DESM_ESM=1 and 100m.Laps=2 and 100m.800_timetaken between 0 and 181 then '5'\n"
                + "when 100m.DESM_ESM=1 and 100m.Laps=2 and 100m.800_timetaken between 181 and 241 then '3'\n"
                + "when 100m.DESM_ESM=1 and 100m.Laps=2 and 100m.800_timetaken between 241 and 1000 then '0'\n"
                + "when 100m.DESM_ESM=1 and 100m.Laps<2  then '0'\n"
                + "when 100m.DESM_ESM=2 and 100m.Laps=2 and 100m.800_timetaken between 0 and 151 then '5'\n"
                + "when 100m.DESM_ESM=2 and 100m.Laps=2 and 100m.800_timetaken between 151 and 171 then '3'\n"
                + "when 100m.DESM_ESM=2 and 100m.Laps=2 and 100m.800_timetaken between 171 and 1000 then '0'\n"
                + "when 100m.DESM_ESM=2 and 100m.Laps<2 then '0'\n"
                + "end) '800mMarks'\n"
                + " from 100m\n"
                + " \n"
                + " ";

        Statement stmt = null;

        try {
            stmt = connect.createStatement();
            ResultSet rslt = stmt.executeQuery(query);
            Database db = DatabaseBuilder.open(new File("new.mdb"));
            new ImportUtil.Builder(db, "800m").importResultSet(rslt);

            db.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR");
        }
    }

    void getResultMaleMarks() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
        connect = DriverManager
                .getConnection("jdbc:mysql://" + "127.0.0.1" + "/sportstracker_dev?"
                        + "user=root&password=toor");

        String query = "select \n"
                + "lapdetails.chestno,\n"
                + "lapdetails.barcode,\n"
                + "lapdetails.cname,\n"
                + "lapdetails.fname,\n"
                + "lapdetails.starttime,\n"
                + "lapdetails.endtime,\n"
                + "lapdetails.racecode,\n"
                + "lapdetails.shift,\n"
                + "lapdetails.autotime,\n"
                + "lapdetails.laps,\n"
                + "lapdetails.status\n"
                + " from lapdetails\n"
                + " ";

        Statement stmt = null;

        try {
            stmt = connect.createStatement();
            ResultSet rslt = stmt.executeQuery(query);
            Database db = DatabaseBuilder.open(new File("new.mdb"));

            new ImportUtil.Builder(db, "LapDetails").importResultSet(rslt);
            rslt = stmt.executeQuery("select * from activerace");
            new ImportUtil.Builder(db, "ActiveRace").importResultSet(rslt);
            //        rslt = stmt.executeQuery("select * from jacket");
            //      new ImportUtil.Builder(db, "Jacket").importResultSet(rslt);
            rslt = stmt.executeQuery("select * from completejacketlink_info");
            new ImportUtil.Builder(db, "CompleteRegistrationLink").importResultSet(rslt);
//            rslt = stmt.executeQuery("select * from raw_timing");
            //          new ImportUtil.Builder(db, "RawTimings").importResultSet(rslt);

            db.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR" + e.getMessage());
        }
    }

    void ExportResultRRC() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
        connect = DriverManager
                .getConnection("jdbc:mysql://" + "127.0.0.1:3307" + "/processedrrc?"
                        + "user=root&password=toor");

        String query = "Select * from lapdetails";

        Statement stmt = null;

        try {
            stmt = connect.createStatement();
            ResultSet rslt = stmt.executeQuery(query);
            Database db = DatabaseBuilder.open(new File("new.mdb"));
            new ImportUtil.Builder(db, "LapDetails").importResultSet(rslt);

            rslt = stmt.executeQuery("select * from completejacketlink_info");
            new ImportUtil.Builder(db, "CompleteRegistrationLink").importResultSet(rslt);

            db.close();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR");
        }
    }

    private void mdbexportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mdbexportActionPerformed

        new SwingWorker<Object, Object>() {

            @Override
            protected void done() {
                JOptionPane.showMessageDialog(null, "MDB CREATED");
                ConsoleMsg("File Created Successfully");
            }

            @Override
            protected Object doInBackground() throws Exception {

                Database db;
                try {
                    ConsoleMsg("Creating MDB File Please Wait");
                    db = DatabaseBuilder.create(Database.FileFormat.V2000, new File("new.mdb"));
                    Table newTable;
                    try {
                        newTable = new TableBuilder("NewTable")
                                .addColumn(new ColumnBuilder("a")
                                        .setSQLType(Types.INTEGER))
                                .addColumn(new ColumnBuilder("b")
                                        .setSQLType(Types.VARCHAR))
                                .toTable(db);
                        //   newTable.addRow(1, "foo");
                        try {
                            getResultMaleMarks();
                            //gethprbMarks();
                            db.close();

                        } catch (ClassNotFoundException ex) {
                            JOptionPane.showMessageDialog(null, "ERROR");
                            ConsoleMsg("An Error was Detected In Creating File");
                        }

                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "ERROR");
                        ConsoleMsg("An Error was Detected In Creating File");
                    }

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "ERROR");
                    ConsoleMsg("An Error was Detected In Creating File");
                }

                return null;

//  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        }.execute();

// TODO add your handling code here:
    }//GEN-LAST:event_mdbexportActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        logger.info("New Race was Attempted to Be Created");
        Statement st;
        try {
            st = connect.createStatement();
            java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
            st.execute("INSERT INTO activerace (RaceName) VALUES ('" + "RACE CLOSED:AT:" + sqlDate + ":" + sqlDate.getTime() + "')",
                    Statement.RETURN_GENERATED_KEYS);

            ResultSet keys = st.getGeneratedKeys();
            int id = 1;
            while (keys.next()) {
                id = keys.getInt(1);
                System.err.println(id);

            }
            trayIcon.displayMessage("Race Tracker Update", "Race Has Been Colsed", TrayIcon.MessageType.INFO);
            getActiveRaceIDOnStart();
            getActiveShiftIDOnStart();
        } catch (SQLException ex) {
            trayIcon.displayMessage("Race Creation Failed", "Can't Close", TrayIcon.MessageType.WARNING);
            Logger
                    .getLogger(OneReader.class
                            .getName()).log(Level.SEVERE, null, ex);
            logger.info(ex.getMessage());
        }

        try {

            try {

                PreparedStatement pstmt = null;
                String query = "select RaceName,id from activerace where id=(select max(id) from activerace)";
                pstmt = connect.prepareStatement(query);
                ResultSet keys = pstmt.executeQuery();
                int id;
                String idname;
                while (keys.next()) {
                    idname = keys.getString(1);
                    id = keys.getInt(2);
                    System.err.println(id + " : " + idname);
                    trayIcon.displayMessage("Your Race ID is ", id + " : " + idname, TrayIcon.MessageType.WARNING);
                    ACTIVESHIFT = id;

                }
            } catch (SQLException ex) {
                Logger.getLogger(OneReader.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception e) {

        }
// TODO add your handling code here:
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        fServer.SendToAllClients("REBOOT: STOP REGISTRATION NOW ");
        trayIcon.displayMessage("Race Tracker Update", String.valueOf(ACTIVESHIFT), TrayIcon.MessageType.INFO);        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed

    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed

        new SwingWorker<Object, Object>() {

            @Override
            protected Object doInBackground() throws Exception {

                String str = JOptionPane.showInputDialog(null, "Enter Missing Jacket Number Only ",
                        " This Event is Being Logged ", 1);
                if (str != null) {

                    CallableStatement stmt = connect.prepareCall("{call ForceTimeInsert (?, ?)}");

                    //
                    // Defines all the required parameter values.
                    // 
                    stmt.setString(1, str);
                    stmt.setString(2, String.valueOf(ACTIVESHIFT));

                    stmt.execute();
                    JOptionPane.showMessageDialog(null, " Force Insert Done .");
                }
                return null;
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }.execute();

// TODO add your handling code here:
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed

        String str = JOptionPane.showInputDialog(null, "Kindly Enter the Shift ID : ",
                "Shift ID ", 1);
        if (str != null) {

            JOptionPane.showMessageDialog(null, "You Entered Shift ID : " + str,
                    "JACKET SCANNING", 1);

            ResultSet rs = null;
            PreparedStatement prepstmt = null;
            try {
                Statement st = connectRegistration.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);

                st = connectRegistration.createStatement();
                if (connectRegistration.isClosed() == true) {
                    ConsoleMsg("Registration Database Seems to Be Disconnected");

                }

                prepstmt = connectRegistration
                        .prepareStatement("select count(*) as 'count' from completejacketlink_info  where completejacketlink_info.shift=?");
                prepstmt.setInt(1, Integer.parseInt(str));
                //     prepstmt.setInt(2, 111);

                rs = prepstmt.executeQuery();

                if (rs.next()) {
                    String rowCount = rs.getString("count");

                    ConsoleMsg("Registration Count for SHift " + str + "   iS   " + rowCount);
                    str = null;
                }
            } catch (Exception e) {
                ConsoleMsg("Error Detected ");
                System.out.println("Error in Thread Process a6" + e.getMessage());
            }

        }
// TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    public void CreateMysqlDump() {
//        
//        CREATE USER 'myuser'@'localhost' IDENTIFIED BY 'myuser';
//GRANT ALL ON *.* TO 'myuser'@'localhost' IDENTIFIED BY 'myuser';
//        

        new SwingWorker<Object, Object>() {

            @Override
            protected Object doInBackground() throws Exception {
                setTitle(" Performing Backup Please Wait ");
                setTitle("Backing Up Registration Database");
                isBackup("C:\\Program Files\\MySQL\\MySQL Server 5.6\\bin\\mysqldump -uRaceAdmin -pv721PL7y registration completejacketlink_info -r  d:\\registrationdumpfile.sql");
                isBackup("C:\\Program Files\\MySQL\\MySQL Server 5.6\\bin\\mysqldump -uRaceAdmin -pv721PL7y sportstracker_dev raw_timing -r  d:\\sportstracker_devdumpfile.sql");
                setTitle("Completed Registration Database");
                return null;

            }
        }.execute();
    }

    public static boolean isBackup(String path) {

        //"cmd.exe /c " + "mysqldump --user=myuser --password=mypass --host=localhost --all-databases > c:\\temp\\dumpfile.sql"; // For Windows
        //   String[] arg = new String[]{"-u root", "-h localhost"};
        //String[] arg = new String[]{"C:\\Program Files\\MySQL\\MySQL Server 5.6\\bin\\mysqldump -umyuser -pmypass --all-databases -r  d:\\dumpfile.sql"};
        String[] arg = new String[]{path};

        try {
            String ss = null;
            Runtime obj = null;
            //Process p = Runtime.getRuntime().exec("/C:/Program Files/MySQL/MySQL Server 5.5/bin/mysql --user=root --host=localhost --port=3306 --password=urihurih --database=hiru select * from emp;");
            //Process p = Runtime.getRuntime().exec("cmd.exe /c start ");
            Process p = Runtime.getRuntime().exec(arg[0]);

            //obj.exec("cmd.exe /dir");
            BufferedWriter writeer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
            writeer.write("dir");
            writeer.flush();
            //p = Runtime.getRuntime().exec(" urihurih");
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            //obj.exec(ss);
            System.out.println("Here is the standard output of the command:\n");
            while ((ss = stdInput.readLine()) != null) {
                System.out.println(ss);
            }
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((ss = stdError.readLine()) != null) {
                System.out.println(ss);
            }

        } catch (IOException e) {
            System.out.println("FROM CATCH" + e.toString());
        }
        return false;

    }

    public void getExplorer() {
        String startDir = System.getProperty("user.dir");
        try {
            Runtime.getRuntime().exec("explorer /select,  " + startDir);
        } catch (IOException ex) {
        }
    }
    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed

        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws InterruptedException {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection connectorin = DriverManager
                            .getConnection("jdbc:mysql://" + "127.0.0.1" + "/?"
                                    + "user=RaceAdmin&password=v721PL7y");
                    Statement stmt = connectorin.createStatement();

                    stmt.execute("USE sportstracker_dev;");
                    stmt.execute("CREATE DEFINER=`ankit`@`%` PROCEDURE `getJacketLapsbyShiftTestingINAPP`(IN `jacketnumber` INT, IN `raceid` INT, IN `sshiftid` INT)\n"
                            + "	LANGUAGE SQL\n"
                            + "	NOT DETERMINISTIC\n"
                            + "	CONTAINS SQL\n"
                            + "	SQL SECURITY DEFINER\n"
                            + "	COMMENT ''\n"
                            + "BEGIN\n"
                            + "\n"
                            + "SET @prevValue:=0;\n"
                            + "SET @lap0:=0;\n"
                            + "SET @lap1:=0;\n"
                            + "SET @lap2:=0;\n"
                            + "SET @lap3:=0;\n"
                            + "SET @lap4:=0;\n"
                            + "SET @lap5:=0;\n"
                            + "SET @ETAFACTOR:=30;\n"
                            + "SET @MINTIME:=0;\n"
                            + "SET @MAXTIME:=0;\n"
                            + "SET @LAPSDONE:=0;\n"
                            + "SET @TIMETAKEN:=0;\n"
                            + "delete from p;\n"
                            + "\n"
                            + "\n"
                            + "SELECT @kilo := MIN(raw_timing.reader_timestamp) 'kil'  from raw_timing where raw_timing.reader_tag_hash=jacketnumber and raw_timing.race_id=raceid;\n"
                            + "SET @lap0:=@kilo;\n"
                            + "insert into p values(jacketnumber,@kilo,@lap0+@ETAFACTOR);\n"
                            + "SET @lap1:=@kilo;\n"
                            + "\n"
                            + "\n"
                            + "SELECT MIN(raw_timing.reader_timestamp),@kilo := MIN(raw_timing.reader_timestamp)  from raw_timing where raw_timing.reader_tag_hash=jacketnumber and \n"
                            + "raw_timing.reader_timestamp between @kilo+@ETAFACTOR and (select max(raw_timing.reader_timestamp) from raw_timing where raw_timing.reader_tag_hash=jacketnumber and raw_timing.race_id=raceid) and raw_timing.race_id=raceid;\n"
                            + "SET @lap0:=@kilo;\n"
                            + "insert into p values(jacketnumber,@kilo,@lap0+@ETAFACTOR);\n"
                            + "SET @lap2:=@kilo;\n"
                            + "\n"
                            + "\n"
                            + "SELECT MIN(raw_timing.reader_timestamp),@kilo := MIN(raw_timing.reader_timestamp)  from raw_timing where raw_timing.reader_tag_hash=jacketnumber and \n"
                            + "raw_timing.reader_timestamp between @kilo+@ETAFACTOR and (select max(raw_timing.reader_timestamp) from raw_timing where raw_timing.reader_tag_hash=jacketnumber and raw_timing.race_id=raceid) and raw_timing.race_id=raceid;\n"
                            + "SET @lap0:=@kilo;\n"
                            + "insert into p values(jacketnumber,@kilo,@lap0+@ETAFACTOR);\n"
                            + "SET @lap3:=@kilo;\n"
                            + "\n"
                            + "\n"
                            + "SELECT MIN(raw_timing.reader_timestamp),@kilo := MIN(raw_timing.reader_timestamp)  from raw_timing where raw_timing.reader_tag_hash=jacketnumber and \n"
                            + "raw_timing.reader_timestamp between @kilo+@ETAFACTOR and (select max(raw_timing.reader_timestamp) from raw_timing where raw_timing.reader_tag_hash=jacketnumber and raw_timing.race_id=raceid) and raw_timing.race_id=raceid;\n"
                            + "SET @lap0:=@kilo;\n"
                            + "insert into p values(jacketnumber,@kilo,@lap0+@ETAFACTOR);\n"
                            + "SET @lap4:=@kilo;\n"
                            + "\n"
                            + "SELECT MIN(raw_timing.reader_timestamp),@kilo := MIN(raw_timing.reader_timestamp)  from raw_timing where raw_timing.reader_tag_hash=jacketnumber and \n"
                            + "raw_timing.reader_timestamp between @kilo+@ETAFACTOR and (select max(raw_timing.reader_timestamp) from raw_timing where raw_timing.reader_tag_hash=jacketnumber and raw_timing.race_id=raceid) and raw_timing.race_id=raceid;\n"
                            + "SET @lap0:=@kilo;\n"
                            + "insert into p values(jacketnumber,@kilo,@lap0+@ETAFACTOR);\n"
                            + "SET @lap5:=@kilo;\n"
                            + "\n"
                            + "select count(*)-1 into @LAPSDONE from p where p.b is NOT NULL;\n"
                            + "select min(p.b) into @MINTIME from p where p.b is NOT NULL;\n"
                            + "select max(p.b) into @MAXTIME from p where p.b is NOT NULL;\n"
                            + " \n"
                            + "SET @TOTALTIME=@MAXTIME-@MINTIME;\n"
                            + "SET @STATUSPASS=0;\n"
                            + "\n"
                            + "SELECT IF(@LAPSDONE >=3 \n"
                            + "          AND @TOTALTIME<255.00, 1 ,0) INTO @STATUSPASS;\n"
                            + "\n"
                            + "SET @BARCODE:=0;\n"
                            + "select sportstracker_dev.completejacketlink_info.barcode into @BARCODE from sportstracker_dev.completejacketlink_info\n"
                            + "where completejacketlink_info.jacket=jacketnumber and completejacketlink_info.shift=sshiftid;\n"
                            + "\n"
                            + "SET @cname:=0;\n"
                            + "select @cname:=total.total.cname ,@SEX:=total.total.sex,@edate:=total.total.edate ,@fname:=total.total.fname from total.total\n"
                            + "where total.total.rollno=@BARCODE;\n"
                            + "\n"
                            + "SET @UNREG:=\"UNREGISTERED\";\n"
                            + "\n"
                            + "delete from p where p.b IS NULL;\n"
                            + "insert into lapdetails values(default,jacketnumber,IFNULL((date_format(from_unixtime(@lap1),' %T ')),0),IFNULL((date_format(from_unixtime(@lap2),' %T ')),0),IFNULL((date_format(from_unixtime(@lap3),' %T ')),0),IFNULL((date_format(from_unixtime(@lap4),' %T ')),0),IFNULL((@lap2-@lap0),0),IFNULL((date_format(from_unixtime(@MINTIME),' %T ')),0),IFNULL((date_format(from_unixtime(@MAXTIME),' %T ')),0),IFNULL((@LAPSDONE),0),TRUNCATE(IFNULL((@MAXTIME-@MINTIME),0),2),IFNULL((@STATUSPASS),0),raceid,sshiftid,IFNULL((@BARCODE),0),IFNULL((@cname),@UNREG),IFNULL((@fname),@UNREG),IFNULL((date_format(from_unixtime(@lap1),' %D %M %Y ')),0));\n"
                            + "\n"
                            + "\n"
                            + "END;");
                    JOptionPane.showMessageDialog(null, "Database Created ,Database name is Jacket_Dev with Table name jacket");
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "ERROR" + e.getMessage());
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "ERROR" + e.getMessage());
                } finally {

                }
                return null;
            }
        }.execute();
// TODO add your handling code here:
    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed

        String str = JOptionPane.showInputDialog(null, "Enter Jacket Number To Print : ",
                " Waiting for Jacket Number ", 1);
        if (str != null) {

            fServer.SendToAllClients("PRINT: " + "\t" + str);

        }
// TODO add your handling code here:
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed

        String str = JOptionPane.showInputDialog(null, "Enter Custom Messages to Send to All : ",
                " Ensure EveryOne Gets it ", 1);
        if (str != null) {

            fServer.SendToSpecificClients(str, str);

        }
// TODO add your handling code here:
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed

        new SwingWorker<Object, Object>() {

            @Override
            protected void done() {
                JOptionPane.showMessageDialog(null, "MDB CREATED");
                ConsoleMsg("File Created Successfully");
            }

            @Override
            protected Object doInBackground() throws Exception {

                Database db;
                try {
                    ConsoleMsg("Creating MDB File Please Wait");
                    db = DatabaseBuilder.create(Database.FileFormat.V2000, new File("new.mdb"));
                    Table newTable;
                    try {
                        newTable = new TableBuilder("NewTable")
                                .addColumn(new ColumnBuilder("a")
                                        .setSQLType(Types.INTEGER))
                                .addColumn(new ColumnBuilder("b")
                                        .setSQLType(Types.VARCHAR))
                                .toTable(db);
                        //   newTable.addRow(1, "foo");
                        try {
                            ExportResultRRC();

                            db.close();

                        } catch (ClassNotFoundException ex) {
                            JOptionPane.showMessageDialog(null, "ERROR");
                            ConsoleMsg("An Error was Detected In Creating File");
                        }

                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(null, "ERROR");
                        ConsoleMsg("An Error was Detected In Creating File");
                    }

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(null, "ERROR");
                    ConsoleMsg("An Error was Detected In Creating File");
                }

                return null;

//  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        }.execute();

// TODO add your handling code here:
    }//GEN-LAST:event_jButton14ActionPerformed

    private void toggleStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleStartActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_toggleStartActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
//        if ((isReachableByPing("192.168.1.101") == true)) {
//
//            ConsoleMsg("Trying to Check Connectivity ....");
//            ConsoleMsg("Device Found Connected Attempting Connection to Device");
//        } else {
//            ConsoleMsg("Device was not Reachable Aborting Reader will not Be Connected ");
//            return;
//
//        }
        {

            new SwingWorker<Object, Object>() {

                @Override
                protected Object doInBackground() throws Exception {

                    try {
                        String one = "1";
                        String port = "8802";
                        fTCPClientExitA = new TCPClientExit(OneReader.this, "END", "END", InetAddress.getByName("192.168.1.101"),
                                Integer.parseInt("8802"), Integer.parseInt(one),
                                Integer.parseInt(one));
                        final boolean fexitA = true;

                        fTCPClientExitA.Start();

                        Thread fExitA = new Thread(new Runnable() {

                            public void run() {
                                try {
                                    while (fexitA) {
                                        if (fTCPClientExitA != null) {

                                            // jLabel2.setText(String.valueOf(fTCPClientExitA.getBytesIn()));
                                            jLabel3.setText(String.valueOf(fTCPClientExitA.getBytesIn()));

                                        }
                                        Thread.sleep(10);
                                    }
                                } catch (Exception ex) {
                                    System.err.println("<fUpdateStatusThread>" + ex.getMessage());
                                }
                            }
                        });

                        fExitA.start();

                    } catch (Exception ex) {
                    }

                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    return null;
                }
            }
                    .execute();

        }

// TODO add your handling code here:
    }//GEN-LAST:event_jButton23ActionPerformed

    public static boolean isReachableByPing(String host) {
        try {
            String cmd = "";

            if (System.getProperty("os.name").startsWith("Windows")) {
                cmd = "ping -n 1 " + host; // For Windows
            } else {
                cmd = "ping -c 1 " + host; // For Linux and OSX
            }
            Process myProcess = Runtime.getRuntime().exec(cmd);
            myProcess.waitFor();

            return myProcess.exitValue() == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed

        Thread exitLapThreadA = new Thread() {
            @Override
            public void run() {

                try {
                    String one = "1";
                    String port = "8802";
                    fTCPClientExitA = new TCPClientExit(OneReader.this, "EXIT", "GETS EXIT CANOPY READING", InetAddress.getByName("192.168.1.110"),
                            Integer.parseInt("8080"), Integer.parseInt(one),
                            Integer.parseInt(one));
                    final boolean fexitA = true;
                    Thread fExitA = new Thread(new Runnable() {

                        public void run() {
                            try {
                                while (fexitA) {
                                    if (fTCPClientExitA != null) {

                                        jLabel2.setText(String.valueOf(fTCPClientExitA.getBytesIn()));
                                        //   jLabel3.setText(String.valueOf(fEntryTCPClient.getBytesOut()));

                                    }
                                    Thread.sleep(100);
                                }
                            } catch (Exception ex) {
                                System.err.println("<fUpdateStatusThread>" + ex.getMessage());
                            }
                        }
                    });

                    fTCPClientExitA.Start();
                    fExitA.start();

                } catch (Exception ex) {
                }
            }

        };
        exitLapThreadA.start();

// TODO add your handling code here:
    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed

        String str = JOptionPane.showInputDialog(null, "Enter Missing Jacket Number : ",
                "ALL DVR OPERATOR WILL GET THIS MESSAGES", 1);
        if (str != null) {

            fServer.SendToAllClients("MISSING:" + str);
            insertTicket(str);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed

        new SwingWorker<Object, Object>() {

            @Override
            protected Object doInBackground() throws Exception {

                String raceid = JOptionPane.showInputDialog(null, "Enter Race Id TO Proceed ",
                        " This Event is Being Logged ", 1);

                String shiftid = JOptionPane.showInputDialog(null, "Enter Shift ID ",
                        " This Event is Being Logged ", 1);
                if (raceid != null) {

                    CallableStatement stmt = connect.prepareCall("{call ResultGenerator (?, ?)}");

                    //
                    // Defines all the required parameter values.
                    // 
                    stmt.setString(1, raceid);
                    stmt.setString(2, shiftid);
                    ConsoleMsg("Starting Result Generation .........\n\n");
                    stmt.execute();
                    JOptionPane.showMessageDialog(null, "Result Generation Completed");
                }
                return null;
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }.execute();

// TODO add your handling code here:
    }//GEN-LAST:event_jButton26ActionPerformed

    private void toggleExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toggleExitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_toggleExitActionPerformed

    private void jButton23ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jButton23ItemStateChanged

        // TODO add your handling code here:
    }//GEN-LAST:event_jButton23ItemStateChanged

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton27ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed

        new SwingWorker<Object, Object>() {

            @Override
            protected void done() {
                ConsoleMsg(" Sending to Printer TASK COMPLETED");
            }

            @Override
            protected Object doInBackground() throws Exception {

                rl.lock();
                String str = JOptionPane.showInputDialog(null, "Enter Race ID  ",
                        "On the Printer Module enter race id and shift", 1);
                if (str == null) {
                    return null;
                }

                Statement stmt;
                try {
                    stmt = connect.createStatement();
                    ResultSet rslt = stmt.executeQuery("select distinct(raw_timing.reader_tag_hash) from raw_timing where race_id=" + str);

                    while (rslt.next()) {
                        String a = rslt.getString("reader_tag_hash");
                        System.out.println(a);
                        ConsoleMsg(" Sending to Printer for Laps Counting and Printing " + a);
                        fServer.SendToAllClients("PRINT: " + a);
                        Thread.sleep(150);
                        appendContents("SendLog.txt", "\n" + a);
                    }
                    rl.unlock();

                } catch (SQLException ex) {
                    Logger.getLogger(OneReader.class
                            .getName()).log(Level.SEVERE, null, ex);

                }

                return null;
            }
        }
                .execute();

// TODO add your handling code here:
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed

        new SwingWorker<Object, Object>() {

            @Override
            protected Object doInBackground() throws Exception {

                fServer.SendToSpecificClients();

                return null;
//    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }.execute();
// TODO add your handling code here:
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jToggleButton1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButton1ItemStateChanged

//    /*
//    //Create a New Canopy Reader Handler 
//    */
//    private TCPClient fEntryReaderTCP;
//    private Thread fEntryReaderTCPThread;
//    private boolean fEntryReaderTCPStatusActive = true;
//    
//   
        if (evt.getStateChange() == ItemEvent.SELECTED) {

            Thread threadIncrementfEntryReaderTCPThread = new Thread() {
                @Override
                public void run() {

                    try {
                        String one = "1000";
                        String port = "8802";
                        fEntryReaderTCP = new TCPClient(OneReader.this, "START", "LAP", InetAddress.getByName(STARTCANOPYII),
                                Integer.parseInt(port), Integer.parseInt(one),
                                Integer.parseInt(one));
                        fEntryReaderTCPStatusActive = true;
                        fEntryReaderTCPThread = new Thread(new Runnable() {

                            public void run() {
                                try {
                                    while (fEntryReaderTCPStatusActive) {
                                        if (fEntryReaderTCP != null) {

                                            jLabel12.setText(String.valueOf(fEntryReaderTCP.getBytesIn()));
                                            //   jLabel3.setText(String.valueOf(fEntryTCPClient.getBytesOut()));

                                        }
                                        Thread.sleep(10);
                                    }
                                } catch (Exception ex) {
                                    System.err.println("<fUpdateStatusThread>" + ex.getMessage());
                                }
                            }
                        });

                        fEntryReaderTCP.Start();
                        fEntryReaderTCPThread.start();

                    } catch (Exception ex) {
                    }
                }

            };
            threadIncrementfEntryReaderTCPThread.start();
        }
        if (evt.getStateChange() == ItemEvent.DESELECTED) {

            if (fEntryReaderTCP != null) {

                fEntryReaderTCP.Stop();
                fEntryReaderTCPThread.interrupt();

            }
        }

// TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton1ItemStateChanged

    private void jToggleButton2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButton2ItemStateChanged

//    /*
//    //Create a New Exit Canopy Reader Handler 
//    */
//    private TCPClient fExitReaderTCP;
//    private Thread fExitReaderTCPThread;
//    private boolean fExitReaderTCPStatusActive = true;
//            
        if (evt.getStateChange() == ItemEvent.SELECTED) {

            Thread threadIncrementfExitReaderTCP = new Thread() {
                @Override
                public void run() {
                    try {
                        String one = "1";
                        String port = "8802";
                        fExitReaderTCP = new TCPClient(OneReader.this, "END", "LAP", InetAddress.getByName(EXITCANOPYII),
                                Integer.parseInt(port), Integer.parseInt(one),
                                Integer.parseInt(one));
                        fExitReaderTCPStatusActive = true;
                        fExitReaderTCPThread = new Thread(new Runnable() {

                            public void run() {
                                try {
                                    while (fExitReaderTCPStatusActive) {
                                        if (fExitReaderTCP != null) {

                                            jLabel11.setText(String.valueOf(fExitReaderTCP.getBytesIn()));

                                        }
                                        Thread.sleep(10);
                                    }
                                } catch (Exception ex) {
                                    System.err.println("<fUpdateExitStatusThread>" + ex.getMessage());
                                }
                            }
                        });

                        fExitReaderTCP.Start();
                        fExitReaderTCPThread.start();

                    } catch (Exception ex) {
                    }
                }

            };
            threadIncrementfExitReaderTCP.start();
        }
        if (evt.getStateChange() == ItemEvent.DESELECTED) {
            if (fExitReaderTCP != null) {

                fExitReaderTCP.Stop();
                fExitReaderTCPThread.interrupt();

            }
        }

// TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton2ItemStateChanged

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed

        isSend.clear();
        ConsoleMsg("Counter has been set to Zero ");
// TODO add your handling code here:
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed

        new SwingWorker<Object, Object>() {

            @Override
            protected Object doInBackground() throws Exception {

                try {
                    StartTone();
                    String sql = "UPDATE activerace SET flag = ? WHERE id = ?";
                    PreparedStatement prest = connect.prepareStatement(sql);
                    prest.setInt(1, 1);
                    prest.setInt(2, ACTIVESHIFT);
                    prest.executeUpdate();
                    System.out.println("Updating Successfully!");
                    ConsoleMsg("Race Started Time has Been Logged in Database ");

                } catch (SQLException s) {
                    System.out.println("SQL statement is not executed!");
                    ConsoleMsg("Experiencing Issues In Saving Race Data will not be Saved  ");
                }
                return null;
            }
        }.execute();

// TODO add your handling code here:
    }//GEN-LAST:event_jButton31ActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        new SwingWorker<Object, Object>() {

            @Override
            protected Object doInBackground() throws Exception {
                InetAddress address = InetAddress.getByName("173.194.127.1");

                boolean reachable = address.isReachable(10000);

                System.out.println("Is host reachable? " + reachable);

                return null;
            }
        }.execute();
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton32ActionPerformed
    public static String generateRandomPassword() {
        // Pick from some letters that won't be easily mistaken for each
        // other. So, for example, omit o O and 0, 1 l and L.
        String letters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789+@";

        String pw = "";
        for (int i = 0; i < 50; i++) {
            int index = (int) (RANDOM.nextDouble() * letters.length());
            pw += letters.substring(index, index + 1);
        }
        return pw;
    }
    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed

        addToCandidateQueue("2005");
// TODO add your handling code here:
    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton35ActionPerformed

        logger.info("New Race was Attempted to Be Created");
        Statement st;
        try {
            st = connect.createStatement();
            java.sql.Date sqlDate = new java.sql.Date(new java.util.Date().getTime());
            st.execute("INSERT INTO activerace (RaceName,unix) VALUES ('" + "RACE CREATED:AT:" + sqlDate + ":" + sqlDate.getTime() + "',"+sqlDate.getTime()/1000+")",
                    Statement.RETURN_GENERATED_KEYS);

            ResultSet keys = st.getGeneratedKeys();
            int id = 1;
            while (keys.next()) {
                id = keys.getInt(1);
                System.err.println(id);

            }
            trayIcon.displayMessage("Race Tracker Update", "Race Has Been CREATED", TrayIcon.MessageType.INFO);
            getActiveRaceIDOnStart();
            getActiveShiftIDOnStart();
        } catch (SQLException ex) {
            trayIcon.displayMessage("Race CREATED Failed", "Can't CREATED", TrayIcon.MessageType.WARNING);
            Logger
                    .getLogger(OneReader.class
                            .getName()).log(Level.SEVERE, null, ex);
            logger.info(ex.getMessage());
        }

        try {

            try {

                PreparedStatement pstmt = null;
                String query = "select RaceName,id from activerace where id=(select max(id) from activerace)";
                pstmt = connect.prepareStatement(query);
                ResultSet keys = pstmt.executeQuery();
                int id;
                String idname;
                while (keys.next()) {
                    idname = keys.getString(1);
                    id = keys.getInt(2);
                    System.err.println(id + " : " + idname);
                    trayIcon.displayMessage("Your Race ID is ", id + " : " + idname, TrayIcon.MessageType.WARNING);
                    ACTIVESHIFT = id;

                }
            } catch (SQLException ex) {
                Logger.getLogger(OneReader.class
                        .getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception e) {

        }
// TODO add your handling code here:
    }//GEN-LAST:event_jButton35ActionPerformed

    private void jButton31FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jButton31FocusGained
        jButton31.setBackground(new Color(rnd.nextInt()));        // TODO add your handling code here:
    }//GEN-LAST:event_jButton31FocusGained

    private void jButton8FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jButton8FocusGained
        jButton8.setBackground(new Color(rnd.nextInt()));         // TODO add your handling code here:
    }//GEN-LAST:event_jButton8FocusGained

    void ScheduledTask() {

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
                break;

        }

    }

    //\\
    /*
    
     A placeholder to saveExiting candidate Race and Shift ID...
    
     */
    public synchronized void addToCandidateQueue(final String jacket) {
        CandidateDelayedQueue cdQ = new CandidateDelayedQueue(jacket, String.valueOf(ACTIVESHIFT), String.valueOf(ACTIVESHIFTID), 24);
        System.out.println(ACTIVESHIFT + "  " + ACTIVESHIFTID);
        CandidateQueue.add(cdQ);

    }

    public Thread Process(final String jacket, final int race, final int shift) {
        new SwingWorker<Object, Object>() {

            @Override
            protected Object doInBackground() throws Exception {

                CallableStatement stmt = connectSports.prepareCall("{call getJacketLapsbyShift (?, ?,?)}");

                    //
                // Defines all the required parameter values.
                // 
                stmt.setInt(1, Integer.valueOf(jacket));
                stmt.setInt(2, race);
                stmt.setInt(3, shift);
                stmt.execute();
                ConsoleMsg("Processing Completed Results Saved ");

                return null;
//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        }.execute();
        return null;
    }

    public Thread InsertProcessingQueue(String userID, int type, int marks) {
        try {
            String insertTableSQL = "INSERT INTO ProcessingQueue"
                    + "( jacket,raceid, shiftid) VALUES"
                    + "(?,?,?)";
            PreparedStatement preparedStatement = connectSports.prepareStatement(insertTableSQL);

            preparedStatement.setString(1, userID);
            preparedStatement.setInt(2, type);
            preparedStatement.setInt(3, marks);

            preparedStatement.executeUpdate();
            System.out.println("Saved ...");
            Process(userID, type, marks);
        } catch (SQLException ex) {

            System.out.println(ex.getMessage());

        }
        return null;
    }

      public Thread InsertCompleteLinkInfoQueue(String userID, int type, int marks) {
        try {
            String insertTableSQL = "INSERT INTO ProcessingQueue"
                    + "( jacket,raceid, shiftid) VALUES"
                    + "(?,?,?)";
            PreparedStatement preparedStatement = connectSports.prepareStatement(insertTableSQL);

            preparedStatement.setString(1, userID);
            preparedStatement.setInt(2, type);
            preparedStatement.setInt(3, marks);

            preparedStatement.executeUpdate();
            System.out.println("Saved ...");
            Process(userID, type, marks);
        } catch (SQLException ex) {

            System.out.println(ex.getMessage());

        }
        return null;
    }
    
    void printDataSet() {
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
                fServer.SendToAllClients(res.getString("TABLE_NAME"));

            }
        } catch (SQLException ex) {
            Logger.getLogger(MainPrinting.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void Warningtone(int hz, int msecs, double vol)
            throws LineUnavailableException {
        byte[] buf = new byte[1];
        AudioFormat af
                = new AudioFormat(
                        8000f, // sampleRate
                        8, // sampleSizeInBits
                        1, // channels
                        true, // signed
                        false);      // bigEndian
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        sdl.open(af);
        sdl.start();
        for (int i = 0; i < msecs * 8; i++) {
            double angle = i / (8000f / hz) * 2.0 * Math.PI;
            buf[0] = (byte) (Math.sin(angle) * 127.0 * vol);
            sdl.write(buf, 0, 1);
        }
        sdl.drain();
        sdl.stop();
        sdl.close();
    }

    public void Tone() {
        new SwingWorker<Object, Object>() {

            @Override
            protected Object doInBackground() throws Exception {

                try {
                    Warningtone(1000, 100, 0.9);

                } catch (LineUnavailableException ex) {

                }
                return null;
//    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        }.execute();

    }

    public void StartTone() {
        new SwingWorker<Object, Object>() {

            @Override
            protected Object doInBackground() throws Exception {

                try {
                    Warningtone(1000, 3000, 0.9);

                } catch (LineUnavailableException ex) {

                }
                return null;
//    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

        }.execute();

    }

    public void ConsoleMsg(String msg) {

        console.append("\n" + msg);
        console.setCaretPosition(console.getDocument().getLength());
    }

    public void TCPMsg(String msg) {

        console.append("\n" + msg);
        console.setCaretPosition(console.getDocument().getLength());
    }

    public static DefaultTableModel buildTableModel(ResultSet rs)
            throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<String>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return new DefaultTableModel(data, columnNames);

    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(OneReader.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OneReader.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OneReader.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OneReader.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OneReader().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList ExitedList;
    private javax.swing.JLabel bursttime;
    private javax.swing.JTextArea console;
    private javax.swing.JTextField getRaceName;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton37;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator10;
    private javax.swing.JToolBar.Separator jSeparator11;
    private javax.swing.JToolBar.Separator jSeparator12;
    private javax.swing.JToolBar.Separator jSeparator13;
    private javax.swing.JToolBar.Separator jSeparator14;
    private javax.swing.JToolBar.Separator jSeparator15;
    private javax.swing.JToolBar.Separator jSeparator16;
    private javax.swing.JToolBar.Separator jSeparator17;
    private javax.swing.JToolBar.Separator jSeparator18;
    private javax.swing.JToolBar.Separator jSeparator19;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator20;
    private javax.swing.JToolBar.Separator jSeparator21;
    private javax.swing.JToolBar.Separator jSeparator22;
    private javax.swing.JToolBar.Separator jSeparator23;
    private javax.swing.JToolBar.Separator jSeparator24;
    private javax.swing.JToolBar.Separator jSeparator25;
    private javax.swing.JToolBar.Separator jSeparator26;
    private javax.swing.JToolBar.Separator jSeparator27;
    private javax.swing.JToolBar.Separator jSeparator28;
    private javax.swing.JToolBar.Separator jSeparator29;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator30;
    private javax.swing.JToolBar.Separator jSeparator31;
    private javax.swing.JToolBar.Separator jSeparator32;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JToolBar.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JToolBar.Separator jSeparator8;
    private javax.swing.JToolBar.Separator jSeparator9;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JButton mdbexport;
    private javax.swing.JButton newRaceBtn;
    private javax.swing.JList startedList;
    private javax.swing.JToggleButton toggleActivate;
    private javax.swing.JToggleButton toggleExit;
    private javax.swing.JToggleButton toggleLapI;
    private javax.swing.JToggleButton toggleLapII;
    private javax.swing.JToggleButton toggleStart;
    // End of variables declaration//GEN-END:variables
}
