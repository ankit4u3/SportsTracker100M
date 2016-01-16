/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package printingOnline;
//766-7940-941

import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.event.ItemEvent;
import java.awt.print.PrinterException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.SwingWorker;
import sportstrackeronline.DelayObject;

/**
 *
 * @author emapps
 */
class GoVIP implements Runnable {

    public void run() {
        // See
        System.out.println("Seeing");

        // Don't forget to say thanks to those
        // 50/- and 100/- queue people!! :)
        System.out.println("Thank you all");
    }
}

class FiftyClass extends Thread {

    CyclicBarrier cb;

    public FiftyClass(CyclicBarrier cb) {
        // Know that FiftyCLass has to wait, so is the cyclic barrier
        // This object is used to call the await()
        this.cb = cb;

        // Start the thread
        // In general terms, let the 50/- queue move
        start();
    }

    public void run() {
        // They are just 2.5 km away from the
        // god
        System.out.println("2.5 Km to go");

        // An InterruptedException is raised.
        // In general, sometimes the situation goes
        // uncontrollable due to heavy rush
        try {
            cb.await();
        } catch (Exception e) {
            // Then, say, we can't wait
            System.out.println("We can't wait ");
        }

        // They completed their task!!
        System.out.println("Hurray! We saw our GOD! +50");
    }
}

class FiftyaClass extends Thread {

    CyclicBarrier cb;

    public FiftyaClass(CyclicBarrier cb) {
        // Know that FiftyCLass has to wait, so is the cyclic barrier
        // This object is used to call the await()
        this.cb = cb;

        // Start the thread
        // In general terms, let the 50/- queue move
        start();
    }

    public void run() {
        try {
            // They are just 2.5 km away from the
            // god
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            Logger.getLogger(FiftyaClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("2.5 Km to go");

        // An InterruptedException is raised.
        // In general, sometimes the situation goes
        // uncontrollable due to heavy rush
        try {

            cb.await();
        } catch (Exception e) {
            // Then, say, we can't wait
            System.out.println("We can't wait ");
        }

        // They completed their task!!
        System.out.println("Hurray! We saw our GOD! +50");
    }
}

public class MainPrinting extends javax.swing.JFrame {

    private TCPChatClient fClient = null;
    private DefaultListModel fClientsListModel = new DefaultListModel();
    private Connection connect = null;
    private CyclicBarrier barrier;
    final BlockingQueue<DelayObject> PrintQueue = new java.util.concurrent.DelayQueue<>();
    HashMap<String, Object> isSend = new HashMap();
    public static int ACTIVESHIFT = 0;

    public static String SERVERIP = "192.168.1.221";
    public static String SERVERUSERNAME = "192.168.1.11";
    public static String SERVERPASSWORD = "192.168.1.11";
    public static String SERVERDATABASE = "192.168.1.11";
    public static int MAXWAITTIME = 2510;
    public static int PASSCRITERIA = 255;
    final ReentrantLock rl = new ReentrantLock();

    final ReentrantLock printLock = new ReentrantLock();
    ArrayList<String> cats = new ArrayList<String>();
    ForkJoinPool pool = new ForkJoinPool();

    /**
     * Creates new form MainPrinting
     */
    public MainPrinting() {
        initComponents();
        initializeConnection();
        StartReaderIConsumer();
    }

    public void StartReaderIConsumer() {
        new SwingWorker<Object, Object>() {

            @Override
            protected void done() {

                ConsoleMsg(" START CONTROLLER EXITIED WARNING ERROR OCCURED STOP RACE NOW  ");

            }

            @Override
            protected Object doInBackground() throws Exception {
                ConsoleMsg("Starting Consumer Service for START READER ");
                boolean RunStartReaderAlways = true;

                while (true) {
                    try {
                        printLock.lock();

                        DelayObject object = PrintQueue.take();
                        if (!object.getData().trim().startsWith("NULL")) {

                            ParseAndInsert(object.getData());
                            ConsoleMsg(object.getData() + "SERVICE FOR PRINTING STARTED .. FLAGGING DATA ");

                            Statement st = connect.createStatement();

                            ConsoleMsg(getRaw_Count("1"));

                            CallStoredProcedures(object.getData().trim());
                            ConsoleMsg(getJacketLapsCount(object.getData()));
                      //  GetLaps(object.getData());

                            //  Tone();
                       //     printPDFResultA6R(object.getData().trim(), "215");
                            Thread.sleep(MAXWAITTIME);

                        }
                        printLock.unlock();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        ConsoleMsg("SERVICE FAILED FOR  START READER ");

                    }
                }

                //    return null;
            }

        }.execute();

    }

    public synchronized void CallStoredProcedures(final String jacket) {
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:mysql://" + SERVERIP + "/sportstracker_dev?"
                            + "user=RaceAdmin&password=v721PL7y");

            PreparedStatement clmt = connect.prepareCall("{call getJacketLapsbyShift(?,?)}");
            clmt.setInt(1, Integer.parseInt(jacket));
            clmt.setInt(2, Integer.parseInt(getRaceID.getText().toString()));
            //   clmt.registerOutParameter(2, Types.VARCHAR);
            clmt.execute();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            if (e.getMessage().startsWith("Duplicate")) {

            }
        } finally {

        }
    }

    public String getRaw_Count(final String tag_hash) {
        String query = "select count(*) as 'counter' from raw_timing";

        Statement stmt = null;
        try {
            stmt = connect.createStatement();
            ResultSet rslt = stmt.executeQuery(query);
            if (rslt.next()) {
                String imgArr = rslt.getString("counter") + " ";
                return imgArr;

            }

            rslt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getJacketLapsCount(final String tag_hash) {
        String query = "select (count(0) - 1) AS `Laps`,`sportstracker_dev`.`p`.`a` AS `Jacket`,min(date_format(from_unixtime(`sportstracker_dev`.`p`.`b`),'%d %b %Y %T %f')) AS `Time`,max(date_format(from_unixtime(`sportstracker_dev`.`p`.`b`),'%d %b %Y %T %f')) AS `End Time`,group_concat(date_format(from_unixtime(`sportstracker_dev`.`p`.`b`),' %T %f') order by 1 ASC separator '|') AS `laptimes` from `sportstracker_dev`.`p` where p.b is not null group by `sportstracker_dev`.`p`.`a` ";

        Statement stmt = null;
        try {
            stmt = connect.createStatement();
            ResultSet rslt = stmt.executeQuery(query);
            if (rslt.next()) {
                String imgArr = rslt.getString("Laps") + " ";
                return imgArr;

            }

            rslt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

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

    public synchronized void printPDFResultA6R(final String jacketno, String raceid) {
        new SwingWorker<Void, Void>() {

            @Override
            protected void done() {

            }

            @Override
            protected Void doInBackground() {
                String candidatename = null;
                String fathername = null;
                String rollnumber = null;
                try {
                    Statement st = connect.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                            ResultSet.CONCUR_UPDATABLE);

                    st = connect.createStatement();
                    ResultSet rs = st.executeQuery("select * from lapdetails where chestno=" + jacketno);

                    // get the number of rows from the result set
                    rs.next();
                    rollnumber = rs.getString("starttime");
                    candidatename = rs.getString("endtime");

                } catch (Exception e) {
                    System.out.println("Error in Thread Process a6" + e.getMessage());
                }

                Document document = new Document(PageSize.A6, 80, 50, 5, 40);

                try {
                    PdfWriter writer = PdfWriter.getInstance(document,
                            new FileOutputStream(jacketno + ".pdf"));

                    // step 2
                    document.open();

                    Paragraph heading = new Paragraph("﻿RAILWAY RECRUITMENT  CELL ", new Font(Font.FontFamily.HELVETICA, 4f, Font.BOLD));
                    heading.setAlignment(1);
                    //  heading.setSpacingAfter(5f);

                    Paragraph headingRun = new Paragraph("Run Date: " + rollnumber, new Font(Font.FontFamily.HELVETICA, 4f, Font.BOLD));
                    headingRun.setAlignment(1);
                    //  headingRun.setSpacingAfter(5f);

                    Paragraph heading2 = new Paragraph("﻿Northeast Frontier Railway , Station Colony Guwahati - 781001", new Font(Font.FontFamily.HELVETICA, 4f, Font.BOLD));
                    heading2.setAlignment(1);
                    //  heading2.setSpacingAfter(4f);
                    document.add(heading);
                    document.add(heading2);
                    document.add(headingRun);

                    document.add(new Paragraph("RollNo.....:" + rollnumber, new Font(Font.FontFamily.HELVETICA, 4f, Font.BOLD)));
                    document.add(new Paragraph("Chest No...:" + jacketno, new Font(Font.FontFamily.HELVETICA, 4f, Font.BOLD)));

                    document.add(new Paragraph("Name.......:" + candidatename, new Font(Font.FontFamily.HELVETICA, 4f, Font.BOLD)));
                    document.add(new Paragraph("Father Name:" + fathername, new Font(Font.FontFamily.HELVETICA, 4f, Font.BOLD)));

                    Paragraph lowerheadingnote = new Paragraph("﻿Run for a distance of 1000 Meteres in 4 Minutes & 15 Second in one\n"
                            + "Chance  Found -----\n", new Font(Font.FontFamily.HELVETICA, 4f, Font.BOLD));
                    lowerheadingnote.setAlignment(1);
                    //  lowerheadingnote.setSpacingAfter(4f);
                    //  document.add(lowerheadingnote);
                    Font font = FontFactory.getFont("Times-Roman");

                    Font fontbold = FontFactory.getFont("Times-Roman", 4f, Font.NORMAL);

                    document.close();
                } catch (Exception ex) {

                }

                return null;
            }
        }.execute();
    }

    public synchronized void addtoProducer(String jacket) {

        DelayObject objectLap = new DelayObject(jacket, 15000);
        PrintQueue.add(objectLap);
        System.err.printf("Put object = %s%n", objectLap);

    }

    public void ParseAndInsert(String payload) {
        System.err.println(payload + "Received in Parse ans Insert");
        if (payload.startsWith("TAG_ID:")) {
            try {

                String headerLine = payload;
                StringTokenizer s = new StringTokenizer(headerLine, ",");
                String tag = s.nextToken().replace("TAG_ID: ", "");
                String timestamp = s.nextToken().replace("TIMESTAMP: ", "");
                String hash = s.nextToken().replace("HASH: ", "");
                String antenna = s.nextToken().replace("ANTENNA_ID: ", "");
                System.out.println(timestamp + " in Parse and Insert " + tag);

            } catch (Exception e) {
                ConsoleMsg(e.getMessage());

            }
        }

    }

    public void ConsoleMsg(final String msg) {

        new Runnable() {

            @Override
            public void run() {
                console.append("\n" + msg);
                console.setCaretPosition(console.getDocument().getLength());
            }
        }.run();

    }

    private void Connect() {
        Disconnect();

        try {
            fClient = new TCPChatClient(InetAddress.getByName(SERVERIP), Integer.parseInt("5001"), Inet4Address.getLocalHost().getHostAddress(), this);
            fClient.Start();
        } catch (Exception ex) {
            Disconnect();
        }
    }

    private void Disconnect() {
        if (fClient != null) {
            fClient.Stop();
            fClient = null;
        }

    }

    public void GetLaps(String code) {

        try {
            // This will load the MySQL driver, each DB has its own driver
//            Class.forName("com.mysql.jdbc.Driver");
//            // Setup the connection with the DB
//            connect = DriverManager
//                    .getConnection("jdbc:mysql://" + "127.0.0.1" + "/sportstracker_dev?"
//                            + "user=RaceAdmin&password=v721PL7y");

            PreparedStatement clmt = connect.prepareCall("{call getTimeLaps(?)}");
            clmt.setString(1, code);
            //   clmt.registerOutParameter(2, Types.VARCHAR);
            clmt.execute();
            cats.remove(code);
            DefaultComboBoxModel model = new DefaultComboBoxModel(cats.toArray());
            list_toPrint.setModel(model);

            //   System.out.println(clmt.getString(2));
        } catch (Exception e) {
            System.out.println(e.getMessage());

        } finally {

        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jToolBar1 = new javax.swing.JToolBar();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jLabel2 = new javax.swing.JLabel();
        getRaceID = new javax.swing.JTextField();
        jToggleButton3 = new javax.swing.JToggleButton();
        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        getCustomBarcodeNumber = new javax.swing.JTextField();
        manualPrintResult = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        console = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        list_toPrint = new javax.swing.JList();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jToolBar1.setRollover(true);

        jToggleButton1.setText("Activate");
        jToggleButton1.setFocusable(false);
        jToggleButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jToggleButton1);

        jToggleButton2.setText("  Connect Server ");
        jToggleButton2.setFocusable(false);
        jToggleButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToggleButton2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jToggleButton2ItemStateChanged(evt);
            }
        });
        jToolBar1.add(jToggleButton2);

        jLabel2.setText("Race ID");
        jToolBar1.add(jLabel2);

        getRaceID.setText("jTextField1");
        jToolBar1.add(getRaceID);

        jToggleButton3.setText("jToggleButton3");
        jToggleButton3.setFocusable(false);
        jToggleButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jToggleButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jToggleButton3);

        jButton1.setText("SELECT");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton1);

        jLabel1.setText("Enter Candidate Roll Number");

        getCustomBarcodeNumber.setText("jTextField1");

        manualPrintResult.setText("Barcode Prining");
        manualPrintResult.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manualPrintResultActionPerformed(evt);
            }
        });

        console.setColumns(20);
        console.setRows(5);
        jScrollPane1.setViewportView(console);

        list_toPrint.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(list_toPrint);

        jButton2.setText("jButton2");

        jButton3.setText("jButton3");

        jButton4.setText("LIST ALL TABLES");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("CHECK TABLE EXISTS");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("FUCK OFF SERVER");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 483, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(getCustomBarcodeNumber)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(manualPrintResult)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton6, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, 43, Short.MAX_VALUE)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(manualPrintResult, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(getCustomBarcodeNumber))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void manualPrintResultActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manualPrintResultActionPerformed
//
//        Connection dbConnection = connect;
//        PreparedStatement preparedStatementInsert = null;
//        PreparedStatement preparedStatementUpdate = null;
//
//        String insertTableSQL = "INSERT INTO activerace"
//                + "(shift, RaceName) VALUES"
//                + "(?,?)";
//
//        String updateTableSQL = "UPDATE activerace SET RaceName =? "
//                + "WHERE shift = ?";
//        try {
//
//            dbConnection.setAutoCommit(false);
//
//            preparedStatementInsert = dbConnection.prepareStatement(insertTableSQL);
//            preparedStatementInsert.setInt(1, 9);
//            preparedStatementInsert.setString(2, "mkyong101");
//
//            preparedStatementInsert.executeUpdate();
//
//            preparedStatementUpdate = dbConnection.prepareStatement(updateTableSQL);
//            // preparedStatementUpdate.setString(1,
//            // "A very very long string caused db error");
//            preparedStatementUpdate.setString(1, "new string");
//            preparedStatementUpdate.setInt(2, 183);
//            //     preparedStatementUpdate.executeUpdate();
//
//            dbConnection.commit();
//
//            System.out.println("Done!");
//
//        } catch (SQLException e) {
//
//            System.out.println(e.getMessage());
//            try {
//                dbConnection.rollback();
//            } catch (SQLException ex) {
//                Logger.getLogger(MainPrinting.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        }

        new SwingWorker<Object, Object>() {

            @Override
            protected void done() {
                manualPrintResult.setEnabled(true);
            }

            @Override
            protected Object doInBackground() throws Exception {
               
                manualPrintResult.setEnabled(false);
             
             

                if (!getCustomBarcodeNumber.getText().toString().startsWith("NULL")) {

                 
                    ConsoleMsg(getCustomBarcodeNumber.getText().toString() + "SERVICE FOR PRINTING STARTED .. FLAGGING DATA ");

                    Statement st = connect.createStatement();

                    ConsoleMsg(getRaw_Count("1"));

                    CallStoredProcedures(getCustomBarcodeNumber.getText().toString());
                    ConsoleMsg(getJacketLapsCount(getCustomBarcodeNumber.getText().toString()));
                      //  GetLaps(object.getData());

                    //  Tone();
                    printPDFResultA6R(getCustomBarcodeNumber.getText().toString(), getRaceID.getText().toString());
                }
                return null;

            }
        }.execute();

// TODO add your handling code here:
    }//GEN-LAST:event_manualPrintResultActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

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
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainPrinting.class.getName()).log(Level.SEVERE, null, ex);
        }

// TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        DatabaseMetaData dbm;
        try {
            dbm = connect.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "activerace", null);
            if (tables.next()) {
                System.out.println("ActiveRace Exists");
            } else {
                System.out.println("ActiveRace DoesNot Exists");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainPrinting.class.getName()).log(Level.SEVERE, null, ex);
        }
// check if "employee" table is there
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    public void ParseMessageFromServer(final String payload) {
        if (payload.equals("Ping")) {
            //     System.out.println("System Received a Ping from Server:");
            //     ConsoleMsg(payload);
        }
        if (payload.startsWith("PRINT:")) {
            System.out.println("System Received a Ping from Server:");
            ConsoleMsg(payload);
            boolean isinLaps = isSend.containsKey(payload.replace("PRINT:", ""));
            if (isinLaps == false) {
                addtoProducer(payload.replace("PRINT:", ""));
                cats.add(payload.replace("PRINT:", ""));
                DefaultComboBoxModel model = new DefaultComboBoxModel(cats.toArray());
                list_toPrint.setModel(model);
            }
        }

        if (payload.startsWith("LOG:")) {
            ConsoleMsg(payload);

        }

        if (payload.startsWith("ALERT:")) {
            ConsoleMsg(payload);

        }
        if (payload.startsWith("REBOOT:")) {
            ConsoleMsg(payload);

        }
        if (payload.startsWith("SEARCH:")) {
            ConsoleMsg(payload);

        }
        if (payload.startsWith("TAG_ID:")) {
            try {

                String headerLine = payload;
                StringTokenizer s = new StringTokenizer(headerLine, ",");
                String tag = s.nextToken().replace("TAG_ID: ", "");
                String timestamp = s.nextToken().replace("TIMESTAMP: ", "");
                String hash = s.nextToken().replace("HASH: ", "");
                String antenna = s.nextToken().replace("ANTENNA_ID: ", "");
                System.out.println(timestamp + " in Parse and Insert " + tag);
                ConsoleMsg(tag + ":" + timestamp);
                boolean isinLaps = isSend.containsKey(tag);
                if (isinLaps == false) {
                    addtoProducer(payload);
                    cats.add(tag);
                    DefaultComboBoxModel model = new DefaultComboBoxModel(cats.toArray());
                    list_toPrint.setModel(model);
                }

            } catch (Exception e) {
                ConsoleMsg(e.getMessage());
            }

            //  System.err.println(payload);
        }
    }

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
//        CyclicBarrier cb = new CyclicBarrier(2, new GoVIP());
//
//        // Create two threads,
//        // a 50/- line and a 100/- line
//        new FiftyClass(cb);
//
//        new FiftyaClass(cb);
//        int i = 5;
//        assert i > 5;

        fClient.SendDataToServer("SERVER FUCK OFF");
        PrintQueue.clear();
// TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    public void RemoveTagAfterPrinting(final String tag) {
        cats.remove(tag);
        DefaultComboBoxModel model = new DefaultComboBoxModel(cats.toArray());
        list_toPrint.setModel(model);
    }
    private void jToggleButton2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jToggleButton2ItemStateChanged

        if (evt.getStateChange() == ItemEvent.SELECTED) {
            Connect();

        }
        if (evt.getStateChange() == ItemEvent.DESELECTED) {
            Disconnect();
        }
// TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton2ItemStateChanged

    public void initializeConnection() {

        new SwingWorker<String, String>() {
            @Override
            protected String doInBackground() {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Thread.sleep(1000);
                    connect = DriverManager
                            .getConnection("jdbc:mysql://" + SERVERIP + "/sportstracker_dev?"
                                    + "user=RaceAdmin&password=v721PL7y");
                    System.out.println("DataBase Connected System Stable ...");
                    Thread.sleep(2000);
                    Connect();
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

    public void fixMissingTags() {
        Statement stmt = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connectorin = DriverManager
                    .getConnection("jdbc:mysql://" + SERVERIP + "/sportstracker_dev?"
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

                    //        fixTiming(a, endtime);
                }
                if (diff > 250 && diff < 350) {
                    System.out.println(a + "has 2 lap missing");
                    ConsoleMsg(a + "has 2 lap missing");
                    //         fixTiming(a, endtime);
                    //         fixTiming(a, endtime);
                }
                if (diff > 351) {
                    System.out.println(a + "has a lot of missing laps");
                    ConsoleMsg(a + "has 2 lap missing");
                    //         fixTiming(a, endtime - 120);
//                    fixTiming(a, endtime - 60);
                    //                  fixTiming(a, endtime - 30);

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
                    .getConnection("jdbc:mysql://" + SERVERIP + "/sportstracker_dev?"
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

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(MainPrinting.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainPrinting.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainPrinting.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainPrinting.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainPrinting().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea console;
    private javax.swing.JTextField getCustomBarcodeNumber;
    private javax.swing.JTextField getRaceID;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JList list_toPrint;
    private javax.swing.JButton manualPrintResult;
    // End of variables declaration//GEN-END:variables
}
