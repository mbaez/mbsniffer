/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sniffer.Form;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel.*;
import javax.swing.table.*;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.text.MessageFormat;
import sniffer.Parser.Parser;
import javax.swing.UIManager;
import java.util.concurrent.Semaphore;

/**
 *
 * @author Mbaez
 */
public class ListarTrafico extends JPanel {
//public class ListarTrafico extends javax.swing.JFrame{
    private int ancho;
    private int largo;
    //botones
    private JButton clearButton = new JButton();
    private JButton startButton = new JButton();
    private JTextField expresionTextField = new JTextField();
    private JLabel expresionLabel = new JLabel();
    private JTable tabla = new JTable();
    private EditableTableModel modeloTabla;
    private JScrollPane scroll = new JScrollPane();
    private Terminal xterm = new Terminal();
    private Trafico traficoThread;

    private Semaphore mutex;
    /**
     * Constructor de la Clase ListarTrafico
     * resive el acho y el largo de largo del intrenalframe pra que este se adecue a su ventana  padre
     * @author Mbaez
     */
    ListarTrafico(int ancho, int largo) {
        this.ancho = ancho - 15;
        this.largo = largo - 65;
        try {
            //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            mutex = new Semaphore(1, true);
            initInternalApp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * initInternalApp()
     * se encarga de manejar el internal frame
     * establece operaciones bascas como:
     *  -Titulo
     *  -Cerrar
     *  -Tamaño
     *  -Color de fondo
     *  -iniciar los labels
     *  -iniciar los botones
     *  -iniciar la Tabla
     *  -hacer Visible
     * @author Mbaez
     */
    private void initInternalApp() throws Exception {
        //this.getContentPane().
        setLayout(null);
       // this.setTitle("Listar Trafico");
        //this.setClosable(true);
        this.setPreferredSize(new Dimension(ancho, largo));
        //this.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
       // this.setBackground(new java.awt.Color(217, 212, 204));
        initLabel();
        initBotones();
        initTable();
        traficoThread = new Trafico(modeloTabla, xterm,mutex);
        traficoThread.start();
        this.setVisible(true);
    }

    /**
     * initLabel()
     * Se encarga de manipular los Lablels y los textfield, establece:
     * -el texto
     * -el tamaño
     * @author Mbaez
     */
    private void initLabel() throws Exception {
        expresionLabel.setText("Expresion:");
        expresionLabel.setBounds(10, 20, 100, 20);
        expresionTextField.setBounds(90, 20, 400, 24);
        expresionTextField.setText("IP = * & PORT = * & WAY = *");

        //this.getContentPane().
        add(expresionTextField);
       // this.getContentPane().
        add(expresionLabel);
    }

    /**
     * initBotones()
     * Inicializa los botones, estableciendo operaciones basicas como:
     * -establecer la imagen
     * -el tamaño y posicion
     * -hacer visible
     * -la operacion a realizarce con un actionPerformed
     * -agregar al panel
     * @author Mbaez
     */
    private void initBotones() throws Exception {
        clearButton = new JButton();
        clearButton.setIcon(new ImageIcon(getClass().getResource("/24x24/clearButton.png")));
        clearButton.setBounds(ancho / 2 - 12, largo - 30, 24, 24);
        clearButton.setVisible(true);
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed();
            }
        });

        startButton.setIcon(new ImageIcon(getClass().getResource("/24x24/iniciarButton.png")));
        startButton.setBounds(ancho -110, 20, 24, 24);
        startButton.setBorderPainted(false);
        startButton.setVisible(true);
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed();
            }
        });

        //this.getContentPane().
        add(clearButton);
       // this.getContentPane().
        add(startButton);
    }

    /**
     * initTable()
     * crea la tabla dinamica, vease EditableTableModel.java
     * ademas agrega el scroll y el tamaño de la tabla y la posicion
     * @author Mbaez
     */
    private void initTable() throws Exception {
        String puertoColumnas[] = {"IP Origen", "IP Destino", "Puerto Origen", "Puerto Destino", " MAC Origen ", " MAC Destino "};
        Class classes[] = {String.class, String.class, Integer.class, Integer.class, String.class, String.class};
        boolean editable[] = {false, false, false, false, false, false};
        modeloTabla = new EditableTableModel(puertoColumnas, classes, editable);
        tabla.setModel(modeloTabla);
        scroll.setViewportView(tabla);
        scroll.setBounds(10, 55, ancho -20, largo - 100);
        this.add(scroll);

        scroll.getVerticalScrollBar().addAdjustmentListener(
                new AdjustmentListener() {
                    private int valor;
                    public void adjustmentValueChanged(AdjustmentEvent evt) {
                        if (valor != scroll.getVerticalScrollBar().getMaximum()) {
                            scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
                        }
                        valor = scroll.getVerticalScrollBar().getMaximum();
                    }
                });
    }

    private void clearButtonActionPerformed() {
        try{
            mutex.acquire();
            modeloTabla.removeRow();
            mutex.release();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void startButtonActionPerformed() {
        Parser p=new Parser(this.expresionTextField.getText(), ";");
        try{
            p.parseToRule();
        }catch(Exception e){
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this,"Error de sintaxis");
            return;
        }
        Terminal xterm=new Terminal();
        xterm.exec("src/Modulo/cliente_sniffer.bin DEL 0 0 *");
        ArrayList<String[]> reglas=p.getReglas();
        for(int i=0; i<reglas.size();i++){
            String []regla=reglas.get(i);
            xterm.exec("src/Modulo/cliente_sniffer.bin ADD "+
                                            regla[0]+" "+
                                            regla[1]+" "+
                                            regla[2]);
        }
    }
    public void printButtonActionPerformed() {
        MessageFormat header = new MessageFormat("Page {0,number,integer}");
        try {
            tabla.print(JTable.PrintMode.FIT_WIDTH, header, null);
        } catch (java.awt.print.PrinterException e) {
            System.err.format("Cannot print %s%n", e.getMessage());
        }

    }
}
