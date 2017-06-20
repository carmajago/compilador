/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Vistas;

import Modelos.Scanner;
import Modelos.Simbolo;
import Modelos.Sintactico;
import com.sun.org.apache.xerces.internal.util.XMLChar;
import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.HeaderTokenizer;
import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_cup.runtime.Symbol;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import jflex.sym;
import sun.org.mozilla.javascript.internal.Token;

/**
 *
 * @author carlos
 */
public class Editor extends javax.swing.JPanel {

    int contador_lineas = 6;
    
    Main jr;
    boolean eliminado = false;
    String substra = "";
    String[] lista_reservada = {"<page>", "<head>", "<body>", "<div>", "h1", "h2", "p", "check", "list", "onload", "primary key", "bit", "values", "title", "include", "width",
        "height", "text", "create", "read", "update", "delete", "style", "</head>", "</page>", "</div>", "</body>", "button",
        "onClick", "focus", "input", "image", "class", "user", "id", "userid", "password", "varchar2",
        "char", "number"};
    boolean bandera_panel = false;
    StyleContext sc;
    DefaultStyledDocument doc;
    Style azul;
    Style negro;
    Style naranja;
    Style verde;
    Style error;
    Style negrilla;
    PanelPalabras pan;
    DefaultListModel<String> model = new DefaultListModel<>();
    boolean teclas[] = new boolean[256];
    boolean bandera = false;
    LinkedList<String> lista_rutas=new LinkedList<String>();
    LinkedList<String> lista_tokens=new LinkedList<String>();
    public Editor() {
        initComponents();
        
        //estilos
        sc = new StyleContext();
        //doc = new DefaultStyledDocument(sc);

        //this.jEditor.setStyledDocument(doc);
        naranja = sc.addStyle("ConstantWidth", null);
        StyleConstants.setForeground(naranja, Color.orange);
        azul = sc.addStyle("ConstantWidth", null);
        StyleConstants.setForeground(azul, Color.blue);
        negro = sc.addStyle("ConstantWidth", null);
        StyleConstants.setForeground(negro, Color.black);
        verde = sc.addStyle("ConstantWidth", null);
        StyleConstants.setForeground(verde, Color.green);
        error = sc.addStyle("ConstantWidth", null);
        StyleConstants.setForeground(error, Color.red);
        negrilla = sc.addStyle("ConstantWidth", null);
        //StyleConstants.setBold(negrilla, false);
        pan = new PanelPalabras();
        jEditor.add(pan);
        pan.setSize(232, 169);
        pan.setVisible(false);
        bandera_panel = false;
        jEditor.setStyledDocument(new DefaultStyledDocument(sc) {
            @Override
            public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
                super.insertString(offs, str, a); //To change body of generated methods, choose Tools | Templates.

                try {
                    PintarPalabras();
                } catch (IOException ex) {
                    Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        });
        doc = (DefaultStyledDocument) this.jEditor.getDocument();

        for (int i = 0; i < lista_reservada.length; i++) {
            model.addElement(this.lista_reservada[i]);
        }

    }

    public void PintarPalabras() throws FileNotFoundException, IOException {
        String[] lista = jEditor.getText().split("\n");

        int[] tamano_fila = new int[jEditor.getText().split("\n").length];
        int carretpos = jEditor.getCaretPosition();
        Element root = jEditor.getDocument().getDefaultRootElement();
        int fila = root.getElementIndex(carretpos);
        int nfilas = jEditor.getText().split("\n").length;
        numeroLineas(nfilas, fila + 1);
        
       // this.lineCounter.setText(nfilas+"\n");
        
        String cadena_aux="";
        
        for (int i =fila; i <nfilas ; i++) {
            cadena_aux+=lista[i]+"/n";
        }
        try {

            File fichero = new File("fichero.txt");
            PrintWriter writer;
            try {
                writer = new PrintWriter(fichero);
                writer.print(this.jEditor.getText());
                for (int i = 0; i < fila; i++) {
                    tamano_fila[fila] += lista[i].length() + 1;
                }

                writer.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
            }

            Reader reader = new BufferedReader(new FileReader("fichero.txt"));
            Scanner lexer = new Scanner(reader);
            Symbol token;
            int x = 0;
            int len = 0;

            System.out.println("tamano: " + tamano_fila[fila]);
            while (true) {
                token = lexer.next_token();

                if (token.sym == 0) {
                    //jTextField2.setText(resultado);

                    return;
                }

                System.out.println(token.sym + "  este es");
                switch (token.sym) {

                    case Simbolo.MAIN:
                        x = token.left; //+ tamano_fila[fila];
                        
                        this.doc.setCharacterAttributes(x, x + 6, azul,false);

                        break;

                    case Simbolo.MAIN_FIN:
                        x = token.left; //+ tamano_fila[fila];
                        this.doc.setCharacterAttributes(x, x + 7, azul, false);

                        break;
                    case Simbolo.SECCION:
                        x = token.left ;//+ tamano_fila[fila];
                        this.doc.setCharacterAttributes(x, x + 5, azul, false);
                        break;
                    case Simbolo.SECCION_FIN:
                        x = token.left ;//+ tamano_fila[fila];
                        this.doc.setCharacterAttributes(x, x + 6, azul, false);
                        break;

                    case Simbolo.ENCABEZADO:
                        x = token.left ;//+ tamano_fila[fila];
                        this.doc.setCharacterAttributes(x, x + 6, azul, false);
                        break;
                    case Simbolo.ENCABEZADO_FIN:
                        x = token.left ;//+ tamano_fila[fila];
                        this.doc.setCharacterAttributes(x, x + 7, azul, false);
                        break;
                    case Simbolo.CUERPO:
                        x = token.left ;//+ tamano_fila[fila];
                        this.doc.setCharacterAttributes(x, x + 6, azul, false);
                        break;
                    case Simbolo.CUERPO_FIN:
                        x = token.left ;//+ tamano_fila[fila];
                        this.doc.setCharacterAttributes(x, x + 7, azul, false);
                        break;
                    case Simbolo.KADENA:
                        len = token.value.toString().length();
                        x = token.left ;//+ tamano_fila[fila];
                        this.doc.setCharacterAttributes(x, x + len, naranja, false);
                        break;
                    case Simbolo.LIBRERIAS:
                        x = token.left ;//+ tamano_fila[fila];
                        this.doc.setCharacterAttributes(x, x + 7, verde, false);
                        break;
                    case Simbolo.PARRAFO:
                        x = token.left ;//+ tamano_fila[fila];
                        this.doc.setCharacterAttributes(x, x +1, verde, false);
                        break;
                    case Simbolo.SUB:
                        x = token.left ;//+ tamano_fila[fila];
                        this.doc.setCharacterAttributes(x, x + 2, verde, false);
                        break;
                    case Simbolo.SUBT:
                        x = token.left ;//+ tamano_fila[fila];
                        this.doc.setCharacterAttributes(x, x + 2, verde, false);
                        break;
                    case Simbolo.RUTA:
                        x = token.left ;//+ tamano_fila[fila];
                        len = token.value.toString().length();
                        
                        this.doc.setCharacterAttributes(x, x + len, verde, false);
                        break;
                    case Simbolo.IMAGEN:
                        x = token.left ;//+ tamano_fila[fila];
                        //len = token.value.toString().length();
                        this.doc.setCharacterAttributes(x, x + 5, verde, false);
                        break;
                    case Simbolo.BOTON:
                        x = token.left ;//+ tamano_fila[fila];
                        //len = token.value.toString().length();
                        this.doc.setCharacterAttributes(x, x + 6, verde, false);
                        break;
                     case Simbolo.ENTRADA:
                        x = token.left ;//+ tamano_fila[fila];
                        //len = token.value.toString().length();
                        this.doc.setCharacterAttributes(x, x + 5, verde, false);
                        break;
                    case Simbolo.CLASE:
                        x = token.left ;//+ tamano_fila[fila];
                        //len = token.value.toString().length();
                        this.doc.setCharacterAttributes(x, x + 5, azul, false);
                        break;
                    case Simbolo.USUARIO:
                        x = token.left ;//+ tamano_fila[fila];
                        //len = token.value.toString().length();
                        this.doc.setCharacterAttributes(x, x + 4, azul, false);
                        break;
                    case Simbolo.error:
                        x = token.left ;//+ tamano_fila[fila]; 
                        this.doc.setCharacterAttributes(x, x + 1, error, false);
                        System.out.println("error lexico" + token.sym);
                        break;
                    case Simbolo.USID:
                        x = token.left ;//+ tamano_fila[fila];
                       
                        this.doc.setCharacterAttributes(x, x +6, verde,false);
                        break;
                    case Simbolo.CONTRASENA:
                        x = token.left ;//+ tamano_fila[fila];
                       
                        this.doc.setCharacterAttributes(x, x +8, verde,false);
                        
                        break;
                     case Simbolo.VAR:
                        x = token.left ;//+ tamano_fila[fila];
                       
                        this.doc.setCharacterAttributes(x, x +8, verde,false);
                         break;
                        case Simbolo.NUMERO:
                        x = token.left ;//+ tamano_fila[fila];                      
                        this.doc.setCharacterAttributes(x, x +6, verde,false);
                            break;
                    case Simbolo.ALFANUMERICO:

                        x = token.left ;//+ tamano_fila[fila];
                        len = token.value.toString().length();
                        this.doc.setCharacterAttributes(x, x + len, negro, false);
                        break;
                    case Simbolo.TITULO:
                        
                        x = token.left ;//+ tamano_fila[fila];
                       
                        this.doc.setCharacterAttributes(x, x +5, verde,false);
                        
                        break;
                    default:
                        x = token.left ;//+ tamano_fila[fila];
                        this.doc.setCharacterAttributes(x, x +1, negro,false);
                        System.out.println("----");
                }

            }
        } catch (Exception e) {
        }

    }

    private void numeroLineas(int total, int pos) {

        if ((contador_lineas+1) == pos) {
            this.lineCounter.setText(this.lineCounter.getText() + "\n" + pos);
            contador_lineas++;
        }else if(contador_lineas<total){
            for (int i = contador_lineas+1; i <total+1; i++) {
                this.lineCounter.setText(this.lineCounter.getText() + "\n" + i);
            }
            contador_lineas=total;
        }

    }

    public void AnalisisLexico() {
        lista_rutas=new LinkedList<>();
        lista_tokens=new LinkedList<>();
        this.jr.limpiarConsola();
        try {

            File fichero = new File("fichero.txt");
            PrintWriter writer;
            try {
                writer = new PrintWriter(fichero);
                writer.print(jEditor.getText());

                writer.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
            }

            Reader reader = new BufferedReader(new FileReader("fichero.txt"));
            Scanner lexer = new Scanner(reader);
            Symbol token;

            while (true) {
                token = lexer.next_token();

                if (token.sym == 0) {
                    //jTextField2.setText(resultado);

                    return;
                }

                System.out.println(token.sym + "  este es");
                switch (token.sym) {
                    case Simbolo.ACTUALIZAR:
                        this.jr.setTextoConsola("Token 'UPDATE' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.ALFANUMERICO:
                        this.jr.setTextoConsola("Cadena de texto reconocida");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.ALTO:
                        this.jr.setTextoConsola("Token 'heigth' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.ANCHO:
                        this.jr.setTextoConsola("Token 'width' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.ASIGNACION:
                        this.jr.setTextoConsola("Token '=' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.BOOL:
                        this.jr.setTextoConsola("Token 'boolean reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.BOTON:
                        this.jr.setTextoConsola("Token 'Button' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.CARACTER:
                        this.jr.setTextoConsola("Token 'char' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.CLASE:
                        this.jr.setTextoConsola("Token 'class' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.CONTRASENA:
                        this.jr.setTextoConsola("Token 'password' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.CREAR:
                        this.jr.setTextoConsola("Token 'create' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.CUERPO:
                        this.jr.setTextoConsola("Token '<body>' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.CUERPO_FIN:
                        this.jr.setTextoConsola("Token '</body> reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    
                    case Simbolo.ELIMINAR:
                        this.jr.setTextoConsola("Token 'delete' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.ENCABEZADO:
                        this.jr.setTextoConsola("Token '<head>' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.ENCABEZADO_FIN:
                        this.jr.setTextoConsola("Token '</head>' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.ENDLINE:
                        this.jr.setTextoConsola("Token ';' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.ENTRADA:
                        this.jr.setTextoConsola("Token 'input' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.ESTILOS:
                        this.jr.setTextoConsola("Token 'stryle' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.FIN_ATRIBUTOS:
                        this.jr.setTextoConsola("Token ')' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.FIN_SECCION:
                        this.jr.setTextoConsola("Token '}' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.HREF:
                        this.jr.setTextoConsola("Token 'href' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.IDE:
                        this.jr.setTextoConsola("Token 'id' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.IMAGEN:
                        this.jr.setTextoConsola("Token 'image' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.INICIO_ATRIBUTOS:
                        this.jr.setTextoConsola("Token '(' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.INICIO_SECCION:
                        this.jr.setTextoConsola("Token '{' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.INT:
                        this.jr.setTextoConsola("'NUMERO' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.KADENA:
                        this.jr.setTextoConsola("'TEXTO' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.LEER:
                        this.jr.setTextoConsola("Token 'read' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.LIBRERIAS:
                        this.jr.setTextoConsola("Token 'include' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.LISTA:
                        this.jr.setTextoConsola("Token 'list' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.MAIN:
                        this.jr.setTextoConsola("Token '<page>' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.MAIN_FIN:
                        this.jr.setTextoConsola("Token '</page' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.NUMERO:
                        this.jr.setTextoConsola("Token 'number' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.OK:
                        this.jr.setTextoConsola("Token 'check' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.PARRAFO:
                        this.jr.setTextoConsola("Token 'p' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.PASS:
                        this.jr.setTextoConsola("Token 'focus' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.PKEY:
                        this.jr.setTextoConsola("Token 'Primary key' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.PRESS:
                        this.jr.setTextoConsola("Token 'OnClick' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.REFRESH:
                        this.jr.setTextoConsola("Token 'Onload' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.RUTA:
                        this.lista_rutas.add(token.value.toString());
                        lista_tokens.add(""+token.sym);
                        this.jr.setTextoConsola("'ruta' reconocida:" + token.value);
                        break;
                    case Simbolo.SECCION:
                        this.jr.setTextoConsola("Token '<div>' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.SECCION_FIN:
                        this.jr.setTextoConsola("Token '</div>' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.SUB:
                        this.jr.setTextoConsola("Token 'h1' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.SUBT:
                        this.jr.setTextoConsola("Token 'h2' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.TEXTO:
                        this.jr.setTextoConsola("Token 'text' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.TITULO:
                        this.jr.setTextoConsola("Token 'Tittle' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.USID:
                        this.jr.setTextoConsola("Token 'UserId' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.USUARIO:
                        this.jr.setTextoConsola("Token 'user' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.VALORES:
                        this.jr.setTextoConsola("Token 'values' reconocido");
                        lista_tokens.add(""+token.sym);
                        break;
                    case Simbolo.error:
                        this.jr.setTextoConsola("Error de sintaxis");
                        lista_tokens.add(""+token.sym);
                        break;
                      default:
                          System.out.println("otro");
                }

            }
        } catch (Exception e) {

        }
    }

    public void guardar() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");

 //fileChooser.setFileFilter(".txt");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String cade = jEditor.getText();
            File f = fileChooser.getSelectedFile();

            try {
                FileWriter fi = new FileWriter(f.getPath());
                fi.write(cade);
                fi.flush();
                fi.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
            System.out.println("Save as file: " + fileToSave.getAbsolutePath());
        }
    }

    public void cargar() {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Abrir archivo");

        int seleccion = fileChooser.showOpenDialog(null);

        if (seleccion == JFileChooser.APPROVE_OPTION) {
            try {
                BufferedReader br = new BufferedReader(new FileReader(fileChooser.getSelectedFile().getPath()));

                String line = "";
                String s = "";

                while ((line = br.readLine()) != null) {
                    s += line + "\n";
                }
                jEditor.setText(s);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());

            }

        }

    }

    public LinkedList<String> getLista_rutas() {
        return lista_rutas;
    }

    public LinkedList<String> getLista_tokens() {
        return lista_tokens;
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scroll = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        lineCounter = new javax.swing.JTextArea();
        jEditor = new javax.swing.JTextPane();

        scroll.setBackground(new java.awt.Color(85, 0, 255));
        scroll.setPreferredSize(new java.awt.Dimension(700, 520));

        jPanel1.setBackground(new java.awt.Color(254, 254, 254));

        lineCounter.setEditable(false);
        lineCounter.setBackground(new java.awt.Color(205, 205, 205));
        lineCounter.setColumns(2);
        lineCounter.setForeground(new java.awt.Color(1, 1, 1));
        lineCounter.setLineWrap(true);
        lineCounter.setRows(2);
        lineCounter.setTabSize(2);
        lineCounter.setText("1\n2\n3\n4\n5\n6");
        lineCounter.setAutoscrolls(false);
        lineCounter.setCaretColor(new java.awt.Color(51, 51, 255));
        lineCounter.setFocusable(false);

        jEditor.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jEditor.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jEditorMousePressed(evt);
            }
        });
        jEditor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jEditorKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jEditorKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(lineCounter, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jEditor, javax.swing.GroupLayout.DEFAULT_SIZE, 1092, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lineCounter, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
            .addComponent(jEditor)
        );

        scroll.setViewportView(jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 1101, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scroll, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jEditorKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jEditorKeyReleased
        buscarPalabras();
        try {
            teclas[evt.getKeyCode()] = false;
        } catch (Exception e) {
        }
            
       
        if (evt.getKeyCode() == 8) {
            int carretpos = jEditor.getCaretPosition();
            Element root = jEditor.getDocument().getDefaultRootElement();
            int fila = root.getElementIndex(carretpos);
            int nfilas = jEditor.getText().split("\n").length;

            if (nfilas < fila && fila < contador_lineas) {
                this.lineCounter.setText(this.lineCounter.getText().replaceAll("\n" + contador_lineas, ""));
                contador_lineas--;
                System.out.println("passasasasas");
            }
        }


    }//GEN-LAST:event_jEditorKeyReleased

    private void jEditorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jEditorKeyPressed

        if (evt.getKeyCode() == 8 && substra.equals(" ")) {
            pan.setVisible(false);
        }
        
        try {
            teclas[evt.getKeyCode()] = true;
        } catch (Exception e) {
        }
            
          
        
        

        if (evt.getKeyCode() == 32 && bandera) {
            pan.setVisible(false);
            bandera = false;
        }
       if (teclas[17]) {
            try {
                int pos = jEditor.getCaret().getDot();
                int pixelPosition = jEditor.modelToView(pos).x;
                int pixelPositiony = jEditor.modelToView(pos).y;
                pan.setLocation(pixelPosition, pixelPositiony + 20);
            } catch (BadLocationException ex) {
                Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
            }
            
                if (evt.getKeyCode() == 32) {
                   
                    bandera = true;
                    pan.setVisible(true);
                    pan.setFocusable(true);
                    pan.getjList1().setSelectedIndex(0);
                    pan.getjList1().setDragEnabled(true);
                    bandera_panel = true;

                }
            }
        
    }//GEN-LAST:event_jEditorKeyPressed

    private void jEditorMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jEditorMousePressed

        pan.setVisible(false);

    }//GEN-LAST:event_jEditorMousePressed
    int line = 0;

    public void buscarPalabras() {
        String texto = jEditor.getText();
        int pos = jEditor.getCaret().getDot();
        int aux = 0;
        if (pos != 0) {
            for (int i = 0; i < pos; i++) {
                if (texto.charAt(i) == ' ') {
                    aux = i;
                }
            }

            substra = texto.substring(aux, pos);

        }
        //System.out.println("hola: "+substra+" ,"+pos);
    }

    public void compilar(JTextPane consola) throws FileNotFoundException, Exception {
        String programa = jEditor.getText();

        File fichero = new File("fichero.txt");
        PrintWriter writer;
        try {
            writer = new PrintWriter(fichero);
            writer.print(jEditor.getText());
            writer.close();
        } catch (FileNotFoundException ex) {

        }
        Reader reader = new BufferedReader(new FileReader("fichero.txt"));

        Sintactico s = new Sintactico(new Modelos.Scanner(reader));
        //s.setConsola(consola);
        s.parse();
    }
    public void setblack(){
        Color black=new Color(84,84,84);
        this.jEditor.setBackground(black);
    }
    
    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public void setJr(Main jr) {
        this.jr = jr;
    }

    public void shift() {

        Editor[] lista = this.jr.getLista_editor();
        for (int i = 0; i < lista.length - 1; i++) {
            if (lista[i] != null) {
                if (lista[i].isEliminado()) {
                    if (lista[i + 1] != null) {
                        lista[i] = lista[i + 1];
                        lista[i + 1] = new Editor();
                        lista[i + 1].setEliminado(true);

                    } else {
                        lista[i] = null;
                    }
                }
            }

        }
        for (int i = 0; i < lista.length; i++) {

        }
        this.jr.setLista_editor(lista);

    }

    public void actualizarTexto(String texto) {

        this.jEditor.setText(texto);
    }

    public void setFuente(Font fuente) {
        jEditor.setFont(fuente);
        lineCounter.setFont(fuente);
    }

    public JScrollPane getScroll() {
        return scroll;
    }

    public void setScrollSize(int x, int y) {
        this.scroll.setSize(x, y);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextPane jEditor;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextArea lineCounter;
    private javax.swing.JScrollPane scroll;
    // End of variables declaration//GEN-END:variables
}
