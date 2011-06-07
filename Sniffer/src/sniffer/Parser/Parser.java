/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sniffer.Parser;

import java.util.StringTokenizer;
import java.util.ArrayList;
/**
 * G -> G ; G | G | E
 * X OP-> add E |del E
 * E-> E & E | ( E ) |KW
 * KW-> IP | PORT | WAY | KW
 * IP-> IP = T
 * PORT-> PORT = T
 * WAY-> WAY = T
 * T-> String
 * @author mxbg
 */
public class Parser {

    private StringTokenizer tokens;
    private StringTokenizer localToken;

    private ArrayList<String[]> reglas;
    private int parentesisA=0;
    private int parentesisC=0;

    public Parser(String expresion, String separador) {
        expresion=expresion.replace(" ", "");
        expresion=expresion.toUpperCase();
        tokens=new StringTokenizer(expresion,separador);
        /*las reglas se almacenan de la sgt forma
         *Operacion a realizar (ADD|DEL)
         * IP
         * PUERTO
         * DIRECCION
         */
        reglas=new ArrayList<String[]>();
    }
    public ArrayList<String[]> getReglas(){
        return reglas;
    }
    public void parseToRule()throws Exception{
        globalExpresion();
    }
    private void globalExpresion() throws Exception{
        while(tokens.hasMoreTokens()){
            expresion(tokens.nextToken());
        }
    }

    private void expresion(String expresion) throws Exception{
        StringTokenizer local=new StringTokenizer(expresion,"&");
        String values[]={"*","*","*"};
        //values=new String[3];
        while (local.hasMoreTokens()){
            String token=local.nextToken();
            String asig[]=new String[2];
            asig=getIDValue(token);
            int index=getKeyWordIndex(asig[0]);
            values[index]=asig[1];
        }
        reglas.add(values);

    }
    private String []getIDValue(String sentAsig) throws Exception{
        String sentencia[]=new String[2];
        sentAsig.replace("(", "");
        sentAsig.replace(")", "");
        StringTokenizer local= new StringTokenizer(sentAsig, "=");
        int i=0;
        while(local.hasMoreTokens()){
            sentencia[i]=local.nextToken();
            i++;
        }
        return sentencia;
    }

    private boolean isKeyWord(String token){
        if(token.compareTo("IP=")==0)
          return true;
        else if(token.compareTo("PORT=")==0)
            return  true;
        else if(token.compareTo("WAY=")==0)
            return true;
        return false;
    }
    private int getKeyWordIndex(String token){
        if(token.compareTo("IP")==0)
          return 0;
        else if(token.compareTo("PORT")==0)
            return  1;
        else if(token.compareTo("WAY")==0)
            return 2;
        System.out.println("Token: "+token+""+token.compareTo("IP"));
        return -1;
    }
}
