/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estaticos;

import java.util.ArrayList;

/**
 *
 * @author Pedro e Yan
 */
public class Valores {

    private static ArrayList<String> DOMINIOS;
    private static int atributosST = 0;
    private static int atributosCT = 3; 

    public static ArrayList<String> getDOMINIOS() {
        DOMINIOS = new ArrayList<>();
        DOMINIOS.add("INTEGER");
        DOMINIOS.add("DOUBLE");
        DOMINIOS.add("FLOAT");
        DOMINIOS.add("VARCHAR");
        DOMINIOS.add("CHAR");
        return DOMINIOS;
    }

    public static int getAtributosST() {
        return atributosST;
    }

    public static int getAtributosCT() {
        return atributosCT;
    }

}
