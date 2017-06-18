/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;

/**
 *
 * @author carlos
 */
public class LeerArchivos {
    
    private String ruta;
    
    
    public LeerArchivos() {
        
    }

    
    
    public void leer(String ruta){
    String cadena = "";
        String cadenas[];
        int cont = 0;
         try {
             FileReader f = new FileReader(ruta);
              BufferedReader b = new BufferedReader(f);
               while((cadena = b.readLine())!=null) {
                     System.out.println(cadena);        
        }
        b.close();
        System.out.println(cont);
         } catch (Exception e) {
             System.out.println("Error al abrir archivo");
         }
         
    }

   
}
