package sniffer.Form;

import java.awt.BorderLayout;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

public class Ayuda extends JInternalFrame {

    private JLabel imagenLabel=new JLabel();
    public Ayuda() {
        try
        {
            initInternalApp();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    private void initInternalApp()throws Exception
    {
        this.getContentPane().setLayout(null);
        this.setClosable(true);
        this.setPreferredSize(new Dimension(350, 300));
        this.setTitle("Acerca de..");
        this.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        this.setBackground(new java.awt.Color(202, 218, 246));
        imagenLabel.setIcon(new ImageIcon(getClass().getResource("/24x24/ayudatext.png")));
        imagenLabel.setBounds(0,0,300,300);
        this.getContentPane().add(imagenLabel);
        this.setVisible(true);
    }
}
