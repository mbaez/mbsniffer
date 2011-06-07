/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sniffer.Form;
import java.io.*;
import java.util.*;

/**
 *
 * @author mxbg
 */

public class Terminal {
    private String traficoScript="./src/Scripts/get_current_trafic.sh";
    private String SH="/bin/sh";

    Terminal(){

    }
    public String[] exec(String args, String separador){
        String[] out=new String [7];
        try {
            Process p=Runtime.getRuntime().exec(args);
            InputStream is = p.getInputStream();
            BufferedReader br = new BufferedReader (new InputStreamReader (is));
            String tmp = br.readLine();
            while (tmp!=null){
                out=getTokens(tmp,separador);
                tmp =br.readLine();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    public ArrayList<String> exec(String args){
        ArrayList<String> out=new ArrayList<String>();
        try {
            System.out.println(args);
            Process p=Runtime.getRuntime().exec (args);
            InputStream is = p.getInputStream();
            BufferedReader br = new BufferedReader (new InputStreamReader (is));
            String tmp = br.readLine();
            System.out.println(tmp);
            while (tmp!=null){
                System.out.println(tmp);
                out.add(tmp);
                tmp =br.readLine();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    private String [] getTokens(String inStr,String separador){
	StringTokenizer tokens=new StringTokenizer(inStr,separador);
        ArrayList<String> token=new ArrayList<String>();
        while(tokens.hasMoreTokens()){
            token.add(tokens.nextToken());
        }
        Object tmp[]=token.toArray();
        String aux[]=new String[tmp.length];
        for(int i=0;i<tmp.length;i++)
            aux[i]=tmp[i].toString();
        return aux;
    }

    public String[] getOutput(){
        return exec(SH+" "+traficoScript,"|");
    }
}
