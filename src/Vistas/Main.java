/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Modelos.FileSystemModel;
import com.sun.org.apache.bcel.internal.generic.InstructionConstants;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.file.spi.FileTypeDetector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.accessibility.Accessible;
import javax.print.DocFlavor;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.basic.BasicMenuBarUI;
import javax.swing.text.Caret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.tree.DefaultTreeCellRenderer;
import sun.management.FileSystem;
import sun.swing.SwingUtilities2;

/**
 *
 * @author carlos
 */
public class Main extends javax.swing.JFrame {

    Font Letra = new Font("Ubuntu", Font.PLAIN, 16);
    StyleContext sc;
    DefaultStyledDocument doc;
    Caret cursor;
    String[] lista_reservada = {"<page>", "<head>", "<body>", "<div>", "h1", "h2", "p", "check", "list", "onload", "primary key", "bit", "values", "title", "include", "width",
        "height", "text", "create", "read", "update", "delete", "style", "</div>", "</body>", "button",
        "onClick", "focus", "input", "image", "class", "user", "id", "userid", "password", "varchar2",
        "char", "number"};
    DefaultListModel<String> model = new DefaultListModel<>();

    Editor lista_editor[] = new Editor[10];
    int editor_cont = 0;
    //estilos
    Style azul;
    Style negro;
    Point pos_consola_y;

    public Main() {
        initComponents();
        crearEditor("new table");
        
        for (int i = 0; i < lista_reservada.length; i++) {
            model.addElement(lista_reservada[i]);
        }
        this.jListTokens.setModel(model);

        ImageIcon image = new ImageIcon("src/Imagenes/save.png");
        Icon icono = new ImageIcon(image.getImage().getScaledInstance(btnsave.getWidth(), btnsave.getHeight(), Image.SCALE_DEFAULT));
        btnsave.setIcon(icono);

        ImageIcon imageload = new ImageIcon("src/Imagenes/load.png");
        Icon iconoload = new ImageIcon(imageload.getImage().getScaledInstance(btnload.getWidth(), btnload.getHeight(), Image.SCALE_DEFAULT));
        btnload.setIcon(iconoload);
        ImageIcon imageplay = new ImageIcon("src/Imagenes/play.png");
        Icon iconoplay = new ImageIcon(imageplay.getImage().getScaledInstance(btnplay.getWidth(), btnplay.getHeight(), Image.SCALE_DEFAULT));
        btnplay.setIcon(iconoplay);
        btnplay.setPressedIcon(icono);

        ImageIcon imagenew = new ImageIcon("src/Imagenes/new.png");
        Icon icononew = new ImageIcon(imagenew.getImage().getScaledInstance(btnnew.getWidth(), btnnew.getHeight(), Image.SCALE_DEFAULT));
        btnnew.setIcon(icononew);
        ImageIcon imagent = new ImageIcon("src/Imagenes/token.png");
        Icon icont = new ImageIcon(imagent.getImage().getScaledInstance(btntoken.getWidth(), btntoken.getHeight(), Image.SCALE_DEFAULT));
        btntoken.setIcon(icont);
        ImageIcon imagemap = new ImageIcon("src/Imagenes/map.png");
        Icon iconmap = new ImageIcon(imagemap.getImage().getScaledInstance(btnmap.getWidth(), btnmap.getHeight(), Image.SCALE_DEFAULT));
        btnmap.setIcon(iconmap);

        sc = new StyleContext();
        doc = new DefaultStyledDocument(sc);
//        jEditor.setStyledDocument(doc);

        //estilos
        azul = sc.addStyle("ConstantWidth", null);
        StyleConstants.setForeground(azul, Color.blue);
        negro = sc.addStyle("ConstantWidth", null);
        StyleConstants.setForeground(negro, Color.black);

        this.setExtendedState(MAXIMIZED_BOTH);

        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) jTree1.getCellRenderer();
        ImageIcon imag = new ImageIcon("src/Imagenes/tree.png");
        Icon ico = new ImageIcon(imag.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));
        ImageIcon imagca = new ImageIcon("src/Imagenes/carpeta.png");
        Icon icoca = new ImageIcon(imagca.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT));

        Icon closedIcon = new ImageIcon("src/Imagenes/new.png");
        Icon openIcon = new ImageIcon("src/Imagenes/new.png");
        Icon leafIcon = new ImageIcon("src/Imagenes/new.png");
        renderer.setClosedIcon(icoca);
        renderer.setOpenIcon(icoca);
        renderer.setLeafIcon(ico);

        jmenu.setUI(new BasicMenuBarUI() {
            public void paint(Graphics g, JComponent c) {
                Color col = new Color(40, 40, 40);
                g.setColor(col);

                g.fillRect(0, 0, c.getWidth(), c.getHeight());
            }
        });
        jmenu.setForeground(Color.red);

        pos_consola_y=this.jPanelconsola.getLocation();
// UIManager.put("MenuBar.background", Color.RED);
        // jTabbedPane.setTabComponentAt(jTabbedPane.indexOfComponent(this.jScrollPane), getTitlePanel(jTabbedPane, this.jScrollPane, "Tab1"));
    }

    private static JPanel getTitlePanel(final JTabbedPane tabbedPane, final Editor panel, String title) {
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setOpaque(false);
        JLabel titleLbl = new JLabel(title);
        titleLbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        titlePanel.add(titleLbl);
        JButton closeButton = new JButton("x");
        closeButton.setFont(new Font("Ubuntu", Font.BOLD, 13));
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);

        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        //closeButton.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                panel.setEliminado(true);
                panel.shift();
                tabbedPane.remove(panel);
               
            }
        });
        titlePanel.add(closeButton);

        return titlePanel;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jTabbedPEdtidor = new javax.swing.JTabbedPane();
        opt = new javax.swing.JPanel();
        btnsave = new javax.swing.JButton();
        btnload = new javax.swing.JButton();
        btnplay = new javax.swing.JButton();
        btnnew = new javax.swing.JButton();
        btntoken = new javax.swing.JButton();
        btnmap = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTree1 = new javax.swing.JTree();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        jListTokens = new javax.swing.JList();
        jPanelconsola = new javax.swing.JPanel();
        btnCerrarConsola = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPaneconsola = new javax.swing.JTextPane();
        jButton2 = new javax.swing.JButton();
        jmenu = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        m_new = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(234, 234, 234));
        jPanel2.setAlignmentX(0.0F);
        jPanel2.setAlignmentY(1.0F);

        jTabbedPEdtidor.setBackground(new java.awt.Color(234, 234, 234));
        jTabbedPEdtidor.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jTabbedPEdtidor.setAlignmentX(0.2F);
        jTabbedPEdtidor.setAlignmentY(0.2F);
        jTabbedPEdtidor.setAutoscrolls(true);
        jTabbedPEdtidor.setName(""); // NOI18N
        jTabbedPEdtidor.setRequestFocusEnabled(false);
        jTabbedPEdtidor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPEdtidorMouseClicked(evt);
            }
        });
        jTabbedPEdtidor.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                jTabbedPEdtidorMouseMoved(evt);
            }
        });
        jTabbedPEdtidor.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentRemoved(java.awt.event.ContainerEvent evt) {
                jTabbedPEdtidorComponentRemoved(evt);
            }
        });

        opt.setBackground(new java.awt.Color(234, 234, 234));
        opt.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(173, 173, 173)));
        opt.setAlignmentX(0.0F);
        opt.setAlignmentY(0.0F);

        btnsave.setBackground(new java.awt.Color(251, 249, 255));
        btnsave.setToolTipText("");
        btnsave.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnsave.setBorderPainted(false);
        btnsave.setContentAreaFilled(false);
        btnsave.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnsave.setDefaultCapable(false);
        btnsave.setFocusPainted(false);
        btnsave.setFocusable(false);
        btnsave.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/save.png"))); // NOI18N
        btnsave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsaveActionPerformed(evt);
            }
        });

        btnload.setBackground(new java.awt.Color(251, 249, 255));
        btnload.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnload.setBorderPainted(false);
        btnload.setContentAreaFilled(false);
        btnload.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnload.setDefaultCapable(false);
        btnload.setFocusPainted(false);
        btnload.setFocusable(false);
        btnload.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/save.png"))); // NOI18N
        btnload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnloadActionPerformed(evt);
            }
        });

        btnplay.setBackground(new java.awt.Color(251, 249, 255));
        btnplay.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnplay.setBorderPainted(false);
        btnplay.setContentAreaFilled(false);
        btnplay.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnplay.setDefaultCapable(false);
        btnplay.setFocusPainted(false);
        btnplay.setFocusable(false);
        btnplay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnplayActionPerformed(evt);
            }
        });

        btnnew.setBackground(new java.awt.Color(251, 249, 255));
        btnnew.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        btnnew.setBorderPainted(false);
        btnnew.setContentAreaFilled(false);
        btnnew.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnnew.setDefaultCapable(false);
        btnnew.setFocusPainted(false);
        btnnew.setFocusable(false);
        btnnew.setPressedIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/save.png"))); // NOI18N
        btnnew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnewActionPerformed(evt);
            }
        });

        btntoken.setBorderPainted(false);
        btntoken.setContentAreaFilled(false);
        btntoken.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntokenActionPerformed(evt);
            }
        });

        btnmap.setBorderPainted(false);
        btnmap.setContentAreaFilled(false);
        btnmap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmapActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout optLayout = new javax.swing.GroupLayout(opt);
        opt.setLayout(optLayout);
        optLayout.setHorizontalGroup(
            optLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(btnnew, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnload, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnsave, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnplay, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btntoken, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnmap, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(451, Short.MAX_VALUE))
        );
        optLayout.setVerticalGroup(
            optLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, optLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(optLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(btnmap, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .addComponent(btntoken, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnplay, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnload, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .addComponent(btnnew, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnsave, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.tree.DefaultMutableTreeNode treeNode1 = new javax.swing.tree.DefaultMutableTreeNode("root");
        javax.swing.tree.DefaultMutableTreeNode treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("colors");
        javax.swing.tree.DefaultMutableTreeNode treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("blue");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("violet");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("red");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("yellow");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("sports");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("basketball");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("soccer");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("football");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("hockey");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        treeNode2 = new javax.swing.tree.DefaultMutableTreeNode("food");
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("hot dogs");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("pizza");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("ravioli");
        treeNode2.add(treeNode3);
        treeNode3 = new javax.swing.tree.DefaultMutableTreeNode("bananas");
        treeNode2.add(treeNode3);
        treeNode1.add(treeNode2);
        jTree1.setModel(new javax.swing.tree.DefaultTreeModel(treeNode1));
        jTree1.setModel(new FileSystemModel(new File("/home/carlos/NetBeansProjects/ProyectoLenguajes/src")));
        jTree1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTree1MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTree1);

        jTabbedPane1.addTab("Proyectos", jScrollPane3);

        jTabbedPane2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jListTokens.setFont(new java.awt.Font("Ubuntu", 0, 16)); // NOI18N
        jListTokens.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "<page>", "</page>", "<head>", "</head>" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(jListTokens);

        jTabbedPane2.addTab("Tokens", jScrollPane2);

        jPanelconsola.setBackground(new java.awt.Color(234, 234, 234));
        jPanelconsola.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(203, 203, 203)));
        jPanelconsola.setOpaque(false);

        btnCerrarConsola.setFont(new java.awt.Font("Ubuntu", 1, 13)); // NOI18N
        btnCerrarConsola.setText("x");
        btnCerrarConsola.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnCerrarConsola.setBorderPainted(false);
        btnCerrarConsola.setContentAreaFilled(false);
        btnCerrarConsola.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnCerrarConsola.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarConsolaActionPerformed(evt);
            }
        });

        jScrollPane1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jTextPaneconsola.setEditable(false);
        jTextPaneconsola.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jScrollPane1.setViewportView(jTextPaneconsola);

        jButton2.setText("Consola");
        jButton2.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.setDefaultCapable(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanelconsolaLayout = new javax.swing.GroupLayout(jPanelconsola);
        jPanelconsola.setLayout(jPanelconsolaLayout);
        jPanelconsolaLayout.setHorizontalGroup(
            jPanelconsolaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelconsolaLayout.createSequentialGroup()
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(94, 94, 94)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(btnCerrarConsola, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelconsolaLayout.setVerticalGroup(
            jPanelconsolaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnCerrarConsola, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(opt, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                    .addComponent(jTabbedPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPEdtidor)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanelconsola, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(opt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTabbedPEdtidor, javax.swing.GroupLayout.PREFERRED_SIZE, 535, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanelconsola, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jmenu.setBackground(new java.awt.Color(21, 17, 1));
        jmenu.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jMenu1.setForeground(new java.awt.Color(254, 254, 254));
        jMenu1.setText("Archivo");
        jMenu1.setToolTipText("");
        jMenu1.setFont(new java.awt.Font("Ubuntu", 0, 16)); // NOI18N

        m_new.setText("Nuevo");
        m_new.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                m_newActionPerformed(evt);
            }
        });
        jMenu1.add(m_new);

        jMenuItem2.setText("Abrir");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("Guardar");
        jMenu1.add(jMenuItem3);

        jmenu.add(jMenu1);

        jMenu2.setBackground(new java.awt.Color(254, 254, 254));
        jMenu2.setForeground(new java.awt.Color(254, 254, 254));
        jMenu2.setText("Editar");
        jMenu2.setFont(new java.awt.Font("Ubuntu", 0, 16)); // NOI18N

        jMenuItem1.setText("Temas");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuItem5.setText("fuentes");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        jmenu.add(jMenu2);

        jMenu3.setBackground(new java.awt.Color(254, 254, 254));
        jMenu3.setForeground(new java.awt.Color(254, 254, 254));
        jMenu3.setText("Run");
        jMenu3.setFont(new java.awt.Font("Ubuntu", 0, 16)); // NOI18N

        jMenuItem6.setText("Analizador lexico");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem6);

        jMenuItem7.setText("Analizador sintactico");
        jMenu3.add(jMenuItem7);

        jmenu.add(jMenu3);

        jMenu4.setForeground(new java.awt.Color(254, 254, 254));
        jMenu4.setText("Ventana");
        jMenu4.setFont(new java.awt.Font("Ubuntu", 0, 16)); // NOI18N

        jMenuItem8.setText("Consola");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem8);

        jMenuItem9.setText("Nueva ventana");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem9);

        jmenu.add(jMenu4);

        setJMenuBar(jmenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnsaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsaveActionPerformed

        lista_editor[this.jTabbedPEdtidor.getSelectedIndex()].guardar();
    }//GEN-LAST:event_btnsaveActionPerformed


    private void btnloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnloadActionPerformed

        lista_editor[this.jTabbedPEdtidor.getSelectedIndex()].cargar();

    }//GEN-LAST:event_btnloadActionPerformed

    private void btnplayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnplayActionPerformed
        abrirconsola();
        limpiarConsola();
        try {
            this.lista_editor[this.jTabbedPEdtidor.getSelectedIndex()].compilar(this.jTextPaneconsola);
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnplayActionPerformed

    

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        Temas tema=new Temas();
        tema.setVisible(true);
        tema.setJpanel(this);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        Configuracion config = new Configuracion();
        config.setVisible(true);
        config.setPj(this);
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void m_newActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_m_newActionPerformed

        crearEditor("new table");
    }//GEN-LAST:event_m_newActionPerformed

    public void  cambiar_temas(){
        for (int i = 0; i < lista_editor.length; i++) {
            if(lista_editor[i]!=null)
            lista_editor[i].setblack();
        }
    }
    
    public void crearEditor(String nombre) {
        editor_cont = this.jTabbedPEdtidor.getTabCount();
        if (this.editor_cont < 9) {

            this.lista_editor[this.editor_cont] = new Editor();
            this.lista_editor[this.editor_cont].setScrollSize(this.jTabbedPEdtidor.getWidth(),this.jTabbedPEdtidor.getHeight());
            this.lista_editor[this.editor_cont].setJr(this);
            this.lista_editor[this.editor_cont].setOpaque(false);
            this.lista_editor[this.editor_cont].setFuente(Letra);
            jTabbedPEdtidor.add(this.lista_editor[this.editor_cont]);
            jTabbedPEdtidor.setTabComponentAt(jTabbedPEdtidor.indexOfComponent(this.lista_editor[this.editor_cont]), getTitlePanel(jTabbedPEdtidor, this.lista_editor[this.editor_cont], nombre));
            this.jTabbedPEdtidor.setSelectedIndex(this.editor_cont);
         
        }

        System.out.println(this.jTabbedPEdtidor.getComponentCount());
        System.out.println(this.jTabbedPEdtidor.getTabCount());
    }

    public Editor[] getLista_editor() {
        return lista_editor;
    }

    public void setLista_editor(Editor[] lista_editor) {
        this.lista_editor = lista_editor;
    }

    public void setLetra(Font Letra) {
        this.Letra = Letra;
        for (int i = 0; i < lista_editor.length; i++) {
            if (lista_editor[i] != null) {
                lista_editor[i].setFuente(Letra);
            }
        }
    }


    private void jTabbedPEdtidorComponentRemoved(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_jTabbedPEdtidorComponentRemoved
        // TODO add your handling code here:
    }//GEN-LAST:event_jTabbedPEdtidorComponentRemoved

    private void btnnewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnewActionPerformed
        crearEditor("new table");
    }//GEN-LAST:event_btnnewActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        
        abrirconsola();
        
    }//GEN-LAST:event_jMenuItem8ActionPerformed
    public void abrirconsola() {
        int tama = 150;
            
        this.jPanelconsola.setSize(this.jTabbedPEdtidor.getWidth()-5, tama);

        this.jPanelconsola.setLocation(this.jPanelconsola.getLocation().x, this.jPanel2.getHeight() - tama - 15);

        jTabbedPEdtidor.setSize(this.jTabbedPEdtidor.getWidth(), this.jPanelconsola.getLocation().y - this.jTabbedPEdtidor.getLocation().y);

        Label lbl = new Label();
        this.jPanelconsola.add(lbl);
        lbl.setSize(100, 30);
        lbl.setLocation(5, 2);

        lbl.setVisible(true);
        lbl.setText("Consola");
        lbl.setFont(Letra);

        this.jScrollPane1.setSize(this.jTabbedPEdtidor.getWidth(), tama - 2 - lbl.getHeight());
        this.jTextPaneconsola.setSize(this.jTabbedPEdtidor.getWidth(), tama - 2 - lbl.getHeight());
        this.jScrollPane1.setLocation(0, lbl.getHeight() + 2);

        this.btnCerrarConsola.setLocation(this.jPanelconsola.getWidth() - 30, 10);

    }
    public String getTextoConsola()
    {
        return this.jTextPaneconsola.getText();
    }
    public void setTextoConsola(String tex){
    this.jTextPaneconsola.setText(this.jTextPaneconsola.getText()+"\n"+tex);
    }
    public void limpiarConsola(){
        this.jTextPaneconsola.setText("");
    }

    String time;
    String jtreevar;
    private void jTabbedPEdtidorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPEdtidorMouseClicked


    }//GEN-LAST:event_jTabbedPEdtidorMouseClicked

    private void jTabbedPEdtidorMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPEdtidorMouseMoved

    }//GEN-LAST:event_jTabbedPEdtidorMouseMoved

    private void jTree1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTree1MouseClicked
        try {
            jtreevar = this.jTree1.getSelectionPath().toString().replaceAll("[\\[\\]]", "").replace(", ", "/");
            if (evt.getClickCount() == 2) {

                String line = null;
                String text = "";
                try {
                    String name = jTree1.getSelectionPath().toString();
                    String name_result = "";
                    for (int i = name.length() - 1; i > 0; i--) {
                        if (name.charAt(i) != ' ') {
                            name_result += name.charAt(i);
                        } else {
                            break;
                        }

                    }
                    name = "";

                    for (int j = name_result.length() - 1; j > 0; j--) {
                        name += name_result.charAt(j);

                    }
                    crearEditor(name);

                    FileReader fileReader = new FileReader(new File(jtreevar));
                    BufferedReader bufferedReader
                            = new BufferedReader(fileReader);

                    while ((line = bufferedReader.readLine()) != null) {
                        text += line + "\n";
                    }

                    lista_editor[this.jTabbedPEdtidor.getSelectedIndex()].actualizarTexto(text);
                    
                    bufferedReader.close();

                } catch (FileNotFoundException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        } catch (Exception e) {
        }

    }//GEN-LAST:event_jTree1MouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        abrirconsola();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        abrirconsola();
        lista_editor[this.jTabbedPEdtidor.getSelectedIndex()].AnalisisLexico();
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void btnCerrarConsolaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarConsolaActionPerformed
     
         this.jPanelconsola.setSize(100, 20);

        this.jPanelconsola.setLocation(this.jPanelconsola.getLocation().x,pos_consola_y.y);
        
        jTabbedPEdtidor.setSize(this.jTabbedPEdtidor.getWidth(), this.jPanelconsola.getLocation().y - this.jTabbedPEdtidor.getLocation().y);
    }//GEN-LAST:event_btnCerrarConsolaActionPerformed

    private void btntokenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntokenActionPerformed
        
        PanelTokens tokens= new PanelTokens();
        tokens.setVisible(true);
        tokens.setTable(this.lista_editor[this.jTabbedPEdtidor.getSelectedIndex()].getLista_tokens());
    }//GEN-LAST:event_btntokenActionPerformed

    private void btnmapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmapActionPerformed
        Mapa nav=new Mapa();
        nav.setVisible(true);
        nav.setTable(this.lista_editor[this.jTabbedPEdtidor.getSelectedIndex()].getLista_rutas());
    }//GEN-LAST:event_btnmapActionPerformed

    public void palabrasReservadas(String cadena, int c) {
        for (int i = 0; i < lista_reservada.length; i++) {
            if (cadena.equals(lista_reservada[i])) {
//                this.cursor=this.jEditor.getCaret();
                int aux = this.cursor.getDot();

                // Aplica el estilo
                doc.setCharacterAttributes(c - cadena.length(), c, azul, false);
                System.out.println(aux);
            } else {
                doc.setCharacterAttributes(c - cadena.length(), c, negro, false);
            }
        }
    }

    int aux = 18;

    public void contarFilas() {

//        int totalrows = jEditor.getBaseline(0, 0);
//        System.out.println(totalrows);
        //for(int i=2; i<=totalrows;i++){
        //   if(totalrows>aux){
        // lineCounter.setText(lineCounter.getText()+totalrows+"\n");
        // aux=totalrows;
        // }/*else if(totalrows==aux-1){
        //lineCounter.setText(lineCounter.getText().substring(0,2)+"\n");
        //}
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
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrarConsola;
    private javax.swing.JButton btnload;
    private javax.swing.JButton btnmap;
    private javax.swing.JButton btnnew;
    private javax.swing.JButton btnplay;
    private javax.swing.JButton btnsave;
    private javax.swing.JButton btntoken;
    private javax.swing.JButton jButton2;
    private javax.swing.JList jListTokens;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanelconsola;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPEdtidor;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextPane jTextPaneconsola;
    private javax.swing.JTree jTree1;
    private javax.swing.JMenuBar jmenu;
    private javax.swing.JMenuItem m_new;
    private javax.swing.JPanel opt;
    // End of variables declaration//GEN-END:variables
}
