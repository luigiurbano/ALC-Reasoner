package Launcher;

import java.awt.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UrbanoReasoner_GUI extends javax.swing.JFrame {
    private final Test hermit_comparison;   // Oggetto per eseguire i test
    private final JFileChooser fc;  // Selettore di file per visualizzare log
    private final JPanel infoPanel;
    private final JLabel jLabel10;
    private final JLabel jLabel12;
    private final JLabel jLabel13;
    private final JLabel jLabel14;
    private final JLabel jLabel15;
    private final JLabel jLabel16;
    private final JLabel jLabel17;
    private final JLabel jLabel18;
    private final JLabel jLabel19;
    private final JLabel jLabel20;
    private final JLabel jLabel21;
    private final JLabel jLabel22;
    private final JLabel jLabel23;
    private final JLabel jLabel24;
    private final JLabel jLabel25;
    private final JLabel jLabel26;
    
    private File[] concept_files;
    private File dirPath;
    
    // Costruttore GUI
    public UrbanoReasoner_GUI() {
        setLayout(new FlowLayout());  // Imposta FlowLayout
        initComponents();   // Inizializza componenti della GUI
        hermit_comparison = new Test(); // Inizializza oggetto per eseguire
                                        // i test
                                        
        fc = new JFileChooser("LOG"); // Di default è settata la directory LOG
        
        infoPanel = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
                
        infoPanel.setBackground(new java.awt.Color(254, 252, 183));
               
        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Start_Owl.png"))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        jLabel12.setText("Benvenuto nel reasoner basato su API Owl2! ");

        jLabel13.setFont(new java.awt.Font("Times New Roman", 3, 18)); // NOI18N
        jLabel13.setText("Ecco una piccola guida introduttiva delle funzioni disponibili:");

        jLabel14.setFont(new java.awt.Font("Times New Roman", 3, 16)); // NOI18N
        jLabel14.setText("START: esegue la batteria di test sulle ontologie presenti nella cartella 'Ontologie'");

        jLabel15.setFont(new java.awt.Font("Times New Roman", 3, 16)); // NOI18N
        jLabel15.setText("OPENLOG: visualizza uno dei file di log disponibili nella cartella 'Log'");

        jLabel16.setFont(new java.awt.Font("Times New Roman", 3, 16)); // NOI18N
        jLabel16.setText("ALGORITHM: indica quale algoritmo di ottimizzazione utilizzare per risolvere il tableau.");

        jLabel17.setFont(new java.awt.Font("Times New Roman", 3, 16)); // NOI18N
        jLabel17.setText("OUTPUT OPTIONS:");

        jLabel18.setFont(new java.awt.Font("Times New Roman", 3, 16)); // NOI18N
        jLabel18.setText("se abilitato, mostra in output il modello che soddisfa l'ontologia.");

        jLabel19.setFont(new java.awt.Font("Times New Roman", 3, 16)); // NOI18N
        jLabel19.setText(" se abilitato, mostra in output il rendering in DL syntax e Manchester syntax dei concetti.");

        jLabel20.setFont(new java.awt.Font("Times New Roman", 3, 16)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 51, 0));
        jLabel20.setText("- LOG:");

        jLabel21.setFont(new java.awt.Font("Times New Roman", 3, 16)); // NOI18N
        jLabel21.setText("se abilitato, genera un insieme di file di log, corrispondenti ad i nomi dei file delle ontologie, nella cartella 'Log'. ");

        jLabel22.setFont(new java.awt.Font("Times New Roman", 3, 16)); // NOI18N
        jLabel22.setText("Developed by Luigi Urbano & Marco Urbano, Apache license 2.0 ©");

        jLabel23.setFont(new java.awt.Font("Times New Roman", 3, 16)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 51, 0));
        jLabel23.setText("- SOLUTION:");

        jLabel24.setFont(new java.awt.Font("Times New Roman", 3, 16)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 51, 0));
        jLabel24.setText("- RENDERING:");

        jLabel25.setFont(new java.awt.Font("Times New Roman", 3, 16)); // NOI18N
        jLabel25.setText("LOG OPTIONS:");

        jLabel26.setFont(new java.awt.Font("Times New Roman", 3, 16)); // NOI18N
        jLabel26.setText("Based on OWL2 API and HermiT reasoner");
        
        javax.swing.GroupLayout infoPanelLayout = new javax.swing.GroupLayout(infoPanel);
        infoPanel.setLayout(infoPanelLayout);
        infoPanelLayout.setHorizontalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13)
                    .addGroup(infoPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel16)
                            .addComponent(jLabel17)
                            .addComponent(jLabel14)
                            .addComponent(jLabel22)
                            .addComponent(jLabel26)
                            .addComponent(jLabel25))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel10)
                .addContainerGap())
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(infoPanelLayout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel19))
                    .addGroup(infoPanelLayout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel18)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, infoPanelLayout.createSequentialGroup()
                .addContainerGap(57, Short.MAX_VALUE)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel21)
                .addGap(0, 0, Short.MAX_VALUE)
            .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(infoPanelLayout.createSequentialGroup()
                    .addGap(30, 30, 30)
                    .addContainerGap(751, Short.MAX_VALUE))))
        );
        infoPanelLayout.setVerticalGroup(
            infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(infoPanelLayout.createSequentialGroup()
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(infoPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel16)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel17)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel24))
                .addGap(37, 37, 37)
                .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel22)
                .addGap(1, 1, 1)
                .addComponent(jLabel26)
                .addContainerGap())
            .addGroup(infoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, infoPanelLayout.createSequentialGroup()
                    .addContainerGap(230, Short.MAX_VALUE)
                    .addComponent(jLabel25)
                    .addGap(125, 125, 125)))
        );

        /*
        ISTRUZIONI USATE PER DEBUGGARE UNA FINESTRA ADATTA ALLE VARIE RISOLUZIONI.
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        pack();
        System.out.println(screenSize);
        setSize(screenSize.width,screenSize.height);
         */
        dirPath = new File(System.getProperty("user.dir"));
        showInfo.doClick(); // Simula click per stampare info.
        selectedDirectory.setText("Concetti");
        concept_files = (new File(selectedDirectory.getText())).listFiles();

    }
   
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        startButton = new javax.swing.JButton();
        openLogButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        showInfo = new javax.swing.JButton();
        optComboBox = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        renderCheck = new javax.swing.JCheckBox();
        modelCheck = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        logCheck = new javax.swing.JCheckBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        outArea = new javax.swing.JTextArea();
        jLabel27 = new javax.swing.JLabel();
        selectedDirectory = new javax.swing.JLabel();
        selectFolderButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("UrbanoReasoner");
        setAutoRequestFocus(false);
        setBackground(new java.awt.Color(114, 32, 40));
        setForeground(java.awt.Color.red);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(114, 32, 40));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 204, 0)), "Reasoner based on OWL2 API", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(255, 255, 0)), "UrbanoReasoner ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 3, 18), new java.awt.Color(255, 153, 0))); // NOI18N
        jPanel1.setForeground(new java.awt.Color(255, 153, 51));
        jPanel1.setName("UrbanoBrothers"); // NOI18N

        jLabel1.setForeground(new java.awt.Color(114, 32, 40));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/owl_logo.png"))); // NOI18N

        startButton.setBackground(new java.awt.Color(220, 49, 7));
        startButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        startButton.setForeground(new java.awt.Color(255, 255, 255));
        startButton.setText("START");
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                startButtonMouseClicked(evt);
            }
        });
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        openLogButton.setBackground(new java.awt.Color(220, 49, 7));
        openLogButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        openLogButton.setForeground(new java.awt.Color(255, 255, 255));
        openLogButton.setText("OPEN LOG");
        openLogButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openLogButtonActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 3, 16)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 153, 51));
        jLabel2.setText("Apache license 2.0 ©");

        jLabel3.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 153, 51));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Developed by");

        jLabel4.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 153, 51));
        jLabel4.setText("Marco Urbano N97000268");

        jLabel5.setFont(new java.awt.Font("Arial", 3, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 153, 51));
        jLabel5.setText("Luigi Urbano N97000293");

        jLabel6.setFont(new java.awt.Font("Arial", 3, 11)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 153, 51));
        jLabel6.setText("Progetto SemanticWeb Prof. Sauro");

        jLabel7.setFont(new java.awt.Font("Arial", 3, 11)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 153, 51));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("A.A. 2019-2020");

        showInfo.setBackground(new java.awt.Color(220, 49, 7));
        showInfo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        showInfo.setForeground(new java.awt.Color(255, 255, 255));
        showInfo.setText("INFO");
        showInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showInfoActionPerformed(evt);
            }
        });

        optComboBox.setBackground(new java.awt.Color(220, 49, 7));
        optComboBox.setFont(new java.awt.Font("Arial", 3, 11)); // NOI18N
        optComboBox.setForeground(new java.awt.Color(255, 255, 255));
        optComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dependency-Directed Backtracking" }));
        optComboBox.setToolTipText("");
        optComboBox.setBorder(null);
        optComboBox.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        optComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optComboBoxActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 153, 51));
        jLabel9.setText("Algorithm:");

        jPanel2.setBackground(new java.awt.Color(175, 43, 24));
        jPanel2.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 153, 0)));

        renderCheck.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        renderCheck.setForeground(new java.awt.Color(255, 255, 255));
        renderCheck.setText("RENDERING");
        renderCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                renderCheckActionPerformed(evt);
            }
        });

        modelCheck.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        modelCheck.setForeground(new java.awt.Color(255, 255, 255));
        modelCheck.setText("SOLUTION");
        modelCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modelCheckActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Arial", 3, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 153, 51));
        jLabel8.setText("OUTPUT OPTIONS");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(modelCheck)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(renderCheck)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel8)
                .addGap(26, 26, 26))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(modelCheck)
                    .addComponent(renderCheck))
                .addContainerGap())
        );

        jPanel4.setBackground(new java.awt.Color(175, 43, 24));
        jPanel4.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 102, 0)));

        jLabel11.setFont(new java.awt.Font("Arial", 3, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 153, 51));
        jLabel11.setText("LOG OPTIONS");

        logCheck.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        logCheck.setForeground(new java.awt.Color(255, 255, 255));
        logCheck.setText("LOG");
        logCheck.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logCheckActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(logCheck, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logCheck)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        outArea.setEditable(false);
        outArea.setBackground(new java.awt.Color(254, 252, 183));
        outArea.setColumns(20);
        outArea.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        outArea.setLineWrap(true);
        outArea.setRows(5);
        outArea.setWrapStyleWord(true);
        outArea.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        outArea.setFocusable(false);
        outArea.setMargin(new java.awt.Insets(10, 10, 10, 10));
        outArea.setRequestFocusEnabled(false);
        jScrollPane1.setViewportView(outArea);

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 153, 51));
        jLabel27.setText("Directory:");

        selectedDirectory.setFont(new java.awt.Font("Arial", 3, 14));
        selectedDirectory.setForeground(new java.awt.Color(255, 255, 255));

        selectFolderButton.setBackground(new java.awt.Color(220, 49, 7));
        selectFolderButton.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        selectFolderButton.setForeground(new java.awt.Color(255, 255, 255));
        selectFolderButton.setText("SELEZIONA");
        selectFolderButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                selectFolderButtonMouseClicked(evt);
            }
        });
        selectFolderButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectFolderButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(31, 31, 31)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jLabel2))))
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(54, 54, 54)
                                .addComponent(jLabel5)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(startButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(openLogButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(showInfo)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel27)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(selectedDirectory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel9)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(optComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(selectFolderButton, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 263, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addGap(2, 2, 2)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addGap(1, 1, 1)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(selectedDirectory)
                            .addComponent(selectFolderButton))
                        .addGap(1, 1, 1)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE, false)
                            .addComponent(openLogButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(startButton)
                            .addComponent(jLabel9)
                            .addComponent(optComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(showInfo)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 407, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButtonActionPerformed
        
    }//GEN-LAST:event_startButtonActionPerformed

    private void openLogButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openLogButtonActionPerformed
        // Metodo per gestire l'apertura di un log. 
        
        try {
            fc.setCurrentDirectory(dirPath);
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            // Verifica se il file selezionato dall'utente è valido.
            if(fc.showOpenDialog(UrbanoReasoner_GUI.this) == JFileChooser.APPROVE_OPTION){
                // Seleziona il file.
                File file = fc.getSelectedFile();
                
                try{
                    // Imposta reader per leggere il contenuto del file.
                    FileReader reader = new FileReader(file);
                    
                    try (BufferedReader buff = new BufferedReader(reader)) {
                        // Leggi contenuto del file.
                        outArea.read(buff, null );
                    }
                    jScrollPane1.setViewportView(outArea);
                    // Simula input focus.
                    outArea.requestFocus();
                } catch(IOException e2) { 
                   outArea.append("\n[ERROR]: "+e2.toString()+"\n");
                }
            }
        } catch (HeadlessException ex) {
            outArea.append("\n[ERROR]: No file selected - "+ex.toString()+"\n");
        }
    }//GEN-LAST:event_openLogButtonActionPerformed

    private void logCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logCheckActionPerformed
    }//GEN-LAST:event_logCheckActionPerformed

    private void startButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startButtonMouseClicked
        jScrollPane1.setViewportView(outArea);
        try {
            if(concept_files.length > 0)
                outArea.setText(hermit_comparison.execute(optComboBox.getSelectedItem().toString(), logCheck.isSelected(), modelCheck.isSelected(), renderCheck.isSelected(), concept_files));
            else
                outArea.setText("Nessun file valido nella cartella selezionata!");
        } catch(Exception ex) {
            outArea.setText(ex.getMessage());
        }
    }//GEN-LAST:event_startButtonMouseClicked

    private void showInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showInfoActionPerformed
        jScrollPane1.setViewportView(infoPanel);
    }//GEN-LAST:event_showInfoActionPerformed

    private void optComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optComboBoxActionPerformed

    }//GEN-LAST:event_optComboBoxActionPerformed

    private void renderCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_renderCheckActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_renderCheckActionPerformed

    private void selectFolderButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectFolderButtonActionPerformed
        try {
            fc.setCurrentDirectory(dirPath);

            // Verifica se il file selezionato dall'utente è valido.
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if(fc.showOpenDialog(UrbanoReasoner_GUI.this) == JFileChooser.APPROVE_OPTION){
                // Seleziona il file.
                concept_files = (new File(fc.getSelectedFile().getAbsolutePath())).listFiles();

                selectedDirectory.setText(fc.getSelectedFile().getName());
            }
        } catch (HeadlessException ex) {
            outArea.append("\n[ERROR]: No file selected - "+ex.toString()+"\n");
        }
    }//GEN-LAST:event_selectFolderButtonActionPerformed

    private void selectFolderButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_selectFolderButtonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_selectFolderButtonMouseClicked

    private void modelCheckActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modelCheckActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_modelCheckActionPerformed

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
            java.util.logging.Logger.getLogger(UrbanoReasoner_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UrbanoReasoner_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UrbanoReasoner_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UrbanoReasoner_GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UrbanoReasoner_GUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JCheckBox logCheck;
    private javax.swing.JCheckBox modelCheck;
    private javax.swing.JButton openLogButton;
    private javax.swing.JComboBox<String> optComboBox;
    private javax.swing.JTextArea outArea;
    private javax.swing.JCheckBox renderCheck;
    private javax.swing.JButton selectFolderButton;
    private javax.swing.JLabel selectedDirectory;
    private javax.swing.JButton showInfo;
    private javax.swing.JButton startButton;
    // End of variables declaration//GEN-END:variables
}
