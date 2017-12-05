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
        // TODO code application logic here

//        try {
        int n = 1000; //número de paquetes
        int ns = 100; //número de grupos para el histograma
        float Lambda = 10; //tasa de llegada
        int nbuffer = 5; //tamaño del buffer
        float Miu = 20; //tasa de servicio
        float tau = 10;
        float time_tx_pkt = 1 / Miu;

        float[] taus = new float[n];

//        File vector = new File("C:\\Users\\Yoe\\Documents\\NetBeansProjects\\TareaSimulacion\\vect.txt");
//            FileWriter txt;
//            txt = new FileWriter(vector);
        Random ran = new Random();
//            BufferedWriter bf = new BufferedWriter(txt);
        for (int i = 0; i < n - 1; i++) {
            taus[i] = (float) (-(1 / tau) * log(1 - ran.nextFloat()));
            //tiempo de llegada de un paquete respecto del anterior
//                bf.write((-(1 / tau) * log(1 - ran.nextInt(2000) / 2000)) + "\n");
            //calculando tiempo de llegada y escribiendo            
        }
        float linea_tiempo_llegada[] = new float[n];
        float sum = 0;
        for (int i = 0; i < linea_tiempo_llegada.length; i++) {
            sum += taus[i];
            linea_tiempo_llegada[i] = sum;
            //  System.out.println(linea_tiempo_llegada[i]);
        }
        LinkedList<Integer> buffer = new LinkedList<>();
        int pbuffer = 0; // ultima posicion ocupada en el buffer 
        float linea_tiempo_tx[] = new float[n];

        for (int i = 1; i < linea_tiempo_llegada.length; i++) {
            System.out.println("TIEMPO -> " + linea_tiempo_llegada[i]);
            System.out.println("LLEGÓ PKT -> " + i);
            if (linea_tiempo_tx[i - 1] > linea_tiempo_llegada[i]) {
                if (pbuffer < 5) { //acumulo paquete
                    buffer.add(i);
                    pbuffer++;
                    linea_tiempo_tx[i] = linea_tiempo_tx[i - 1] + time_tx_pkt;
                    System.out.println("Se acumula");
                    System.out.println(pbuffer + "---" + buffer);

                } else //desecho paquete 
                {
                    linea_tiempo_tx[i] = linea_tiempo_tx[i - 1];
                    System.out.println("Se desecha pakt");
                    System.out.println(pbuffer + "---" + buffer);
                }
                for (int j = 0; j < pbuffer; j++) {
                    if (linea_tiempo_tx[buffer.get(j)] <= linea_tiempo_llegada[i]) {
                        buffer.remove(j);
                        pbuffer--;
                        System.out.println("se tx pkt " + buffer.get(j) + "TIEMPO -> " + linea_tiempo_tx[buffer.get(j)]);
                        System.out.println(pbuffer + "---" + buffer);
                    } else {
                        break;
                    }
                }
            } else {
                //no hay paquete en buffer se tx
                linea_tiempo_tx[i] = linea_tiempo_llegada[i] + time_tx_pkt;
                System.out.println("NO HAY PKT EN EL BUFFER...SE TX");
                System.out.println(pbuffer);
                System.out.println(buffer);
            }
        }

//            txt.close();
//        } catch (Exception ex) {
//            Logger.getLogger(Redes_simulacion.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

}
