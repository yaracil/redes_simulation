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
        int n = 2000; //número de paquetes
        int nbuffer = 5; //tamaño del buffer
        float Miu = 20; //tasa de servicio
        float lambda = 30; //tiempos de llegadas entre paquetes
        float time_tx_pkt = 1 / Miu; //tiempo de tx de cada paquete
        int count_pkt_perdidos = 0; //total de paqutes perdidos
        float rho = lambda / Miu;

        int C_n[] = new int[nbuffer + 1];    // conteo de ocupacion de posicion n en el buffer
        float[] taus = new float[n];

//        File vector = new File("C:\\Users\\Yoe\\Documents\\NetBeansProjects\\TareaSimulacion\\vect.txt");
//            FileWriter txt;
//            txt = new FileWriter(vector);
        Random ran = new Random();
//            BufferedWriter bf = new BufferedWriter(txt);
        for (int i = 0; i < n - 1; i++) {
            taus[i] = (float) (-(1 / lambda) * log(1 - ran.nextFloat()));
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
        float tiempo_ultimo_pkt;

        System.out.println(taus[0]);
        //transmision del 1er paquete
        System.out.println("LLEGÓ PAQUETE " + 1 + " TIEMPO -> " + linea_tiempo_llegada[0]);
        linea_tiempo_tx[0] = linea_tiempo_llegada[0] + time_tx_pkt;
        System.out.println("NO HAY PAQUETES EN EL BUFFER...SE TRANSMITE");
        System.out.println("BUFFER--->" + buffer);
        C_n[pbuffer] += 1;

        for (int i = 1; i < n || !buffer.isEmpty(); i++) {
            if (i < n) {
                tiempo_ultimo_pkt = linea_tiempo_llegada[i];
            } else {
                tiempo_ultimo_pkt = 99999;
            }
            for (int j = 0; j < pbuffer; j++) {
                if (linea_tiempo_tx[buffer.get(j) - 1] <= tiempo_ultimo_pkt) {
                    System.out.println("SE TRASMITE PAQUETE " + (buffer.get(j)) + " TIEMPO -> " + linea_tiempo_tx[buffer.get(j) - 1]);
                    buffer.remove(j);
                    pbuffer--;
                    //C_n[pbuffer]++;
                    System.out.println("BUFFER--->" + buffer);
                    j--;
                } else {
                    break;
                }
            }
            if (i < n) {
                System.out.println("LLEGÓ PAQUETE " + (i + 1) + " TIEMPO -> " + tiempo_ultimo_pkt);
                if (linea_tiempo_tx[i - 1] > tiempo_ultimo_pkt) {
                    if (pbuffer < nbuffer) { //acumulo paquete
                        buffer.add(i + 1);
                        pbuffer++;
                        linea_tiempo_tx[i] = linea_tiempo_tx[i - 1] + time_tx_pkt;
                        System.out.println("SE ACUMULA");
                        C_n[pbuffer] += 1;
                    } else //desecho paquete 
                    {
                        linea_tiempo_tx[i] = linea_tiempo_tx[i - 1];
                        System.out.println("SE DESECHA PAQUETE");
                        count_pkt_perdidos++;
                    }
                } else {
                    //no hay paquete en buffer se tx
                    linea_tiempo_tx[i] = tiempo_ultimo_pkt + time_tx_pkt;
                    System.out.println("NO HAY PAQUETES EN EL BUFFER...SE TRANSMITE");
                    C_n[pbuffer] += 1;
                }
                System.out.println("BUFFER--->" + buffer);
            }
        }

        // Calculando probabilidad real de buffer vacío P(0) 
        float real_P_0 = (float) C_n[0] / n;
        System.out.println("Probabilidad de que el buffer esté vacío " + real_P_0);

        // Calculando probabilidades reales de posición n ocupada en buffer    
        Double real_P_n[] = new Double[nbuffer + 1];
        for (int i = 0; i < C_n.length; i++) {
            real_P_n[i] = new Double((float)C_n[i] / (n - count_pkt_perdidos));
        }

        // Calculando probabilidades teoricas de posicion n ocupada en buffer
        Double teoric_P_n[] = new Double[nbuffer + 1];
        for (int i = 0; i < real_P_n.length; i++) {
            teoric_P_n[i] = Math.pow(rho, i) * ((1 - rho) / (1 - Math.pow(rho, nbuffer + 1)));
        }

        // Calculando probabilidad real de ocupación del canal E(n)
        float real_E_n = 0;
        for (int i = 0; i < real_P_n.length; i++) {
            real_E_n += real_P_n[i] * i;
            System.out.println("Probabilidad real de " + i + " es " + real_P_n[i]);
            System.out.println("Probabilidad teorica de " + i + " es " + teoric_P_n[i]);
        }

        //Calculando probabilidad teórica de ocupación 
        float teoric_E_n;
        if (rho > 1) {
            teoric_E_n = nbuffer;
        } else if (rho < 1) {
            teoric_E_n = (float) (rho / ((1 - Math.pow(rho, nbuffer + 1)) * (1 - rho)));
        } else {
            teoric_E_n = -99;
        }
        System.out.println("Probabilidad de ocupación real " + real_E_n);
        System.out.println("Probabilidad de ocupación teórica " + teoric_E_n);

//        System.out.println("Paquetes perdidos " + count_pkt_perdidos);
//        // Calculando probabilidad de bloqueo real
//        float real_Pb = (float) count_pkt_perdidos / n;
//        
//        // Calculando probabilidad de bloqueo teórica
//        float teoric_Pb =  (float) (Math.pow(rho, nbuffer) * ((1 - rho) / (1 - Math.pow(rho, nbuffer + 1))));
//        
//        System.out.println("Probabilidad real de bloque "+real_Pb);
//        System.out.println("Probabilidad teórica de bloque "+teoric_Pb);
        System.out.println("Paquetes enviados inmediatamente " + C_n[0]);
        System.out.println("Paquetes rechazados " + count_pkt_perdidos);

        // Creando gráficas
        Object data[][] = new Object[3][2 * (nbuffer + 1)];
        for (int i = 0; i < 2*(nbuffer + 1); i++) {
            if (i < (nbuffer + 1)) {
                data[0][i] = real_P_n[i];
                data[1][i] = i;
                data[2][i] = "Probabilidad real";               
            } else {
                data[0][i] = teoric_P_n [i - (nbuffer + 1)];
                data[1][i] = i - (nbuffer + 1);
                data[2][i] = "Probabilidad teórica";                
            }
            System.out.println(data[0][i]+"--"+data[1][i]+"--"+data[2][i]);
        }

        chart probabilidades_n = new chart(data);
        probabilidades_n.setVisible(true);

        //Calculando probabilidad de bloqueo teórica
        //float teoric_Pb
        //   = //            txt.close();
        //        } catch (Exception ex) {
        //            Logger.getLogger(Redes_simulacion.class.getName()).log(Level.SEVERE, null, ex);
        //        }
    }

}
