/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.DefaultListModel;
import javax.swing.JList;

/**
 *
 * @author carlos
 */
public class PanelPalabras extends javax.swing.JPanel {

   
    String [] lista_reservada={"<page>","<head>","<body>","<div>","h1","h2","p","check","list","onload","primary key","bit","values","title","include","width",
    "height","text","create","read","update","delete","style","</div>","</body>","button",
    "onClick","focus","input","image","class","user","id","userid","password","varchar2",
    "char","number"};
    DefaultListModel<String> model = new DefaultListModel<>();
    public PanelPalabras() {
        initComponents();
        setOpaque(false);
        this.setBackground(new Color(0, 0, 0,50));
        this.setBorder(null);
        this.setBounds(360, 155, 215, 215);
        
        for (int i = 0; i <lista_reservada.length; i++) {
            model.addElement(this.lista_reservada[i]);
        }
        
    this.jList1.setModel(model);
    }

    public JList getjList1() {
        return jList1;
    }

    public void setjList1(JList jList1) {
        this.jList1 = jList1;
    }

    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();

        setBackground(new java.awt.Color(93, 59, 162));
        setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        jScrollPane1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jList1.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jList1.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "casc", "sac", "asc", "cassacacs", "ascacscas", "ascasc", "acsasc", "scaasc" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jList1.setToolTipText("");
        jList1.setInheritsPopupMenu(true);
        jList1.setSelectedIndex(0);
        jScrollPane1.setViewportView(jList1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 170, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

public void paintComponent(Graphics g) {
        g.setColor(getBackground());
        Rectangle r = g.getClipBounds();
        g.fillRect(r.x, r.y, r.width, r.height);
        super.paintComponent(g);
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}