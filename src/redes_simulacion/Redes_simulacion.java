/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redes_simulacion;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import static java.lang.Math.log;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maestria
 */
public class Redes_simulacion {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        try {
            int n = 1000;
            float tau = 10;
            File vector = new File("C:\\Users\\Yoe\\Documents\\NetBeansProjects\\TareaSimulacion\\vect.txt");
            FileWriter txt;
            txt = new FileWriter(vector); 
            Random ran = new Random();
            BufferedWriter bf = new BufferedWriter(txt); 
            for (int i = 1; i < n; i++) {
                bf.write((-(1 / tau) * log(1 - ran.nextInt(2000) / 2000)) + "\n");
                //calculando tiempo de llegada y escribiendo
            }
            txt.close();
        } catch (Exception ex) {
            Logger.getLogger(Redes_simulacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
