/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mac_simulacion;

import redes_simulacion.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import static java.lang.Math.log;
import java.util.LinkedList;
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

        int n = 30; // valores de prueba
        
        // simulacion(float lambda, float miu, boolean tamano_variable)
        
        float miu=(float) (20*Math.pow(10, 6)/10*Math.pow(10, 3));

        simulacion sim = new simulacion(100, miu, false);
        sim.run();
        
        System.out.println("Cantidad promedio real de paquetes en cola"+sim.getReal_E_n());
        System.out.println("Cantidad promedio teorica de paquetes en cola"+sim.getTeoric_E_n());
        
        System.out.println("Tiempo promedio real en sistema"+sim.getReal_TPsistema()*1000000);
        System.out.println("Tiempo promedio teorico en sistema"+sim.getTeoric_TPsistema()*1000000);
        System.out.println("Tiempo promedio real en cola"+sim.getRealTPcola()*1000000);
        System.out.println("Tiempo promedio teorico en cola"+sim.getTeoric_TPcola()*1000000);


////        probabilidades_n.setVisible(true);
//        Object datas[][] = new Object[3][2 * n];
//        //  Object dates[][] = new Object[3][n];
//        for (int i = 0; i < 2 * n; i++) {
//            if (i < n) {
//                datas[0][i] = prob_bloqueo_real[i];
//                datas[1][i] = i + 1;
//                datas[2][i] = "Valor real";
//                //  System.out.println(prob_ocupacion_real[i]);
//            } else {
//                datas[0][i] = prob_bloqueo_teoric[i - n];
//                datas[1][i] = i - n + 1;
//                datas[2][i] = "Valor Teórico";
////                System.out.println(prob_ocupacion_teoric[i - 30]);
//            }
//            //(data[0][i] + "--" + data[1][i] + "--" + data[2][i]);
//        }

//        chart probabili_n = new chart(datas, "Valor Mu", "Probabilidad de bloqueo");
//        probabili_n.setVisible(true);  
    }
}

