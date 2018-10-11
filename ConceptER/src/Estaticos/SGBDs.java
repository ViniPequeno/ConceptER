/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Estaticos;

import java.util.ArrayList;

/**
 *
 * @author User
 */
public class SGBDs {
    
    private static ArrayList<String> SGBDs;
    
    public static ArrayList<String> getSGBDs(){
        SGBDs = new ArrayList<>();
        SGBDs.add("mysql");
        SGBDs.add("sqlserver");
        SGBDs.add("mariadb");
        return SGBDs;
    }

}
