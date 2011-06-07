/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sniffer.Form;

import java.util.*;
import javax.swing.JScrollPane;
import java.awt.Point;

import java.util.concurrent.Semaphore;

/**
 *
 * @author mxbg
 */
public class Trafico extends Thread {

    private EditableTableModel modeloTabla;
    private Terminal xterm;
    private Semaphore mutex;

    public Trafico(EditableTableModel modeloTabla, Terminal xterm, Semaphore mutex) {
        this.modeloTabla = modeloTabla;
        this.xterm = xterm;
        this.mutex = mutex;
    }

    public void run() {
        String buffer = "";
        while (this.isAlive()) {
            try {
                mutex.acquire();
                String tmp[] = xterm.getOutput();
                if (tmp[0]!=null && buffer!=null && tmp.length == 7 && buffer.compareTo(arrayToString(tmp)) != 0 ) {
                    modeloTabla.addRow(subStr(tmp));
                    buffer = arrayToString(tmp);
                }
                mutex.release();
                sleep(500);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private String arrayToString(String[] str){
        String cad="";
        for (int i=0; i<str.length; i++){
            cad+=str[i];
        }
        return cad;
    }
    private String[] subStr(String[] in) {
        String[] out = new String[in.length - 1];
        for (int i = 0; i < out.length; i++) {
            out[i] = in[i + 1];
        }
        return out;
    }
}
