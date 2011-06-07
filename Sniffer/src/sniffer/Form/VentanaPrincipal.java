/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sniffer.Form;

import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.UIManager;
/**
 *
 * @author Mbaez
 */
public class VentanaPrincipal {

    private JFrame frame = new JFrame();
    private JPanel panel = new JPanel();
    private int ancho;
    private int largo;
    private JMenuBar barraMenu = new JMenuBar();
    private JMenu inicioMenu = new JMenu();
    private JMenuItem salirItem = new JMenuItem();
    private JMenuItem exportarItem = new JMenuItem();
    private JMenu listarMenu = new JMenu();
    private JMenuItem listarTraficoItem = new JMenuItem();
    private JMenu ayudaMenu = new JMenu();
    private JMenuItem ayudaItem = new JMenuItem();
    private ListarTrafico listarTrafico = null;
    private Ayuda ayuda = new Ayuda();
    private JInternalFrame internal = new JInternalFrame();

    /**
     * Constructor de la Clase Ventana principal
     * establece el ancho y el largo de la ventana
     * @author Mbaez
     */
    public VentanaPrincipal() {
        ancho = 780;
        largo = 600;
                try{
        //UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
         setLookandFeel() ;
        }catch(Exception e){}
        cargarModulo();
        initApp();
    }

    /**
     * void initApp():se encarga de realizar operaciones bacicas sobre el frame:
     * -Cerrar
     * -tamaño
     * -titulo
     * -color
     * -iniciar los menus
     * hacer visible
     * @author Mbaez
     */
    private void initApp() {
        
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {
                salirItemActionPerformed();
            }
        });
        frame.setSize(ancho, largo);
        frame.setTitle("Mbaez-Sniffer");
        frame.setResizable(false);
        //panel.setBackground(new java.awt.Color(45, 113, 113));
        initMenu();
        frame.add(panel);
        listarTrafico = new ListarTrafico(ancho, largo);
        //panel.add(listarTrafico);
        //internal=listarTrafico;
        //panel.add(internal);
        panel.add(listarTrafico);
        frame.setVisible(true);
    }
    private void setLookandFeel() throws Exception{
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            javax.swing.JFrame.setDefaultLookAndFeelDecorated(true);
            javax.swing.JDialog.setDefaultLookAndFeelDecorated(true);
    }
    /**
     * initMenu():se encarga de realizar operaciones bacicas sobre el el menubar, menu y menuItem:
     * -establecer Imagen
     * -añadir el texto
     * -shortcut key
     * -pocicion
     * -hacer visible
     * -Evento ActionPerformed
     * -añadir todo al panel
     * @author Mbaez
     */
    private void initMenu() {
        inicioMenu.setIcon(new ImageIcon(getClass().getResource("/24x24/inicioMenu.png")));
        exportarItem.setText("Imprimir");
        exportarItem.setIcon(new ImageIcon(getClass().getResource("/24x24/imprimirItem.png")));
        exportarItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.ALT_MASK));
        exportarItem.setBounds(0, 0, 48, 48);
        exportarItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listarTrafico.printButtonActionPerformed();
            }
        });
        salirItem.setText("Salir");
        salirItem.setIcon(new ImageIcon(getClass().getResource("/24x24/salirItem.png")));
        salirItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.ALT_MASK));
        salirItem.setBounds(0, 0, 48, 48);
        salirItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirItemActionPerformed();
            }
        });
        inicioMenu.add(exportarItem);
        inicioMenu.add(salirItem);

        listarMenu.setIcon(new ImageIcon(getClass().getResource("/24x24/listarMenu.png")));
        listarTraficoItem.setText("Lista Trafico");
        listarTraficoItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_2, java.awt.event.InputEvent.ALT_MASK));
        listarTraficoItem.setIcon(new ImageIcon(getClass().getResource("/24x24/listarTraficoItem.png")));
        listarTraficoItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                listarTraficoItemActionPerformed();
            }
        });
        listarMenu.add(listarTraficoItem);

        ayudaMenu.setIcon(new ImageIcon(getClass().getResource("/24x24/ayudaMenu.png")));
        ayudaItem.setText("Acerca de...");
        ayudaItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(KeyEvent.VK_H, 0));
        ayudaItem.setIcon(new ImageIcon(getClass().getResource("/24x24/acercaItem.png")));
        ayudaItem.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ayudaItemActionPerformed();
            }
        });
        ayudaMenu.add(ayudaItem);

        barraMenu.add(inicioMenu);
        barraMenu.add(listarMenu);
        barraMenu.add(ayudaMenu);
        frame.setJMenuBar(barraMenu);

    }

    private void salirItemActionPerformed() {
        Terminal xterm = new Terminal();
        xterm.exec("/bin/sh ./src/Modulo/rmmod.sh");
        freeMemory();
        System.exit(0);
    }

    private void listarTraficoItemActionPerformed() {
        internal.setVisible(false);
        listarTrafico.setVisible(true);
        //internal = listarTrafico;
        //listarTrafico.requestFocus(true);
        panel.add(internal);
    }

    private void ayudaItemActionPerformed() {
        internal.setVisible(false);
        ayuda.setVisible(true);
        internal = ayuda;
        ayuda.requestFocus(true);
        panel.add(internal);
    }

    private void cargarModulo() {
        Terminal xterm = new Terminal();
        xterm.exec("/bin/sh ./src/Modulo/insmod.sh");
    }

    /**
     * freeMemory():hace que todas la referencias sean nulas, para que el recolector de basura de java
     * pueda liberar la memoria , asi aseguramos que el recolector va realizar correctamente su trabajo
     * @author Mbaez
     */
    private void freeMemory() {
        panel = null;
        inicioMenu = null;
        listarMenu = null;
        ayudaMenu = null;
        barraMenu = null;
        ayudaItem = null;
        listarTraficoItem = null;
        salirItem = null;
        listarTrafico = null;
        ayuda = null;
    }
}
