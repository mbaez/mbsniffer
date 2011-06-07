/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sniffer;
import sniffer.Form.*;

/**
 *
 * @author Mbaez
 */
public class Main {

    /**
     * metodo main que se encarga de lanzar el proceso de nuestra ventana principal
     * @author Mbaez
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        java.awt.EventQueue.invokeLater(new Runnable(){
            public void run()
            {
                new VentanaPrincipal();
            }
        });
    }

}
