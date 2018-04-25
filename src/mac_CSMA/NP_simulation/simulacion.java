/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mac_CSMA.NP_simulation;

import static java.lang.Math.log;
import java.util.Random;

/**
 *
 * @author maestria
 */
public class simulacion {

    int n, nbuffer, countSucc, countPerd, countColiss;
    double dataRate, g_chik, G, alfa, tiempo_tx, time_succ,
            tiempo_prop, tiempo_actual, teoric_S, real_S, trials_Real, trials_Teoric;
    float[] taus, linea_tiempo_llegada, linea_tiempo_tx;
    boolean colission;

    int C_n[];

    Random ran;

    /*
    Description:
    -   Divides time into slots.
    -   When a packet is produced by a device, it is transmitted in the next slot after produced.
    -   Single-hop system.
    -   Infinite population generating packets of equal length tnat are transmitted in T seconds according to a .
        Poisson process with rate λ packets/s.
    -   Channel is error-free as long as there are no collissions.
    -   We define a point process that consists of the time packets are scheduled for transmission (new or retransmited).
    -   The rate of the scheduled points is g > λ since not all packets are successfully transmitted at first attempt.
    -   To simplify analysis, we will assume that the point process is a Poisson process.
     */
    public simulacion(int n, double g, double dataRate, double tamPkt, double tiempo_prop) {
        this.n = n;                // número de paquetes a enviar
        nbuffer = 100000;             // tamaño del buffer
        this.dataRate = dataRate;              // tasa de servicio [pkt/seg]
        this.g_chik = g;           // tasa promedio de llegada de pkt [pkt/seg] 
        tiempo_tx = tamPkt / dataRate;
        this.tiempo_prop = tiempo_prop;
        // variables auxiliares
        ran = new Random();   // variable para generar valores aleatorios con distribución normal
        taus = new float[n];  // tiempos entre paquetes [distribución de Poisson] 
        linea_tiempo_llegada = new float[n]; // línea de tiempo de llegada de los paquetes
        linea_tiempo_tx = new float[n]; // línea de tiempo de transmisión de los paquetes
        countSucc = 0;
        countPerd = 0;
        countColiss = 0;
        //time_succ=0;
        tiempo_actual = 0;
        colission = false;

    }

    void run() {

        float sum = 0;
        for (int i = 0; i < n; i++) {
            // inicializar los tiempos entre paquetes [distribución de Poisson] 
            taus[i] = (float) (-(1 / g_chik) * log(1 - ran.nextFloat()));
            // definición de los tiempos de llegada de cada paquete 
            sum += taus[i];
            linea_tiempo_llegada[i] = sum;
        }
        //System.out.println("TIEMPO DE SLOT " + timeSlot);

        //transmision del 1er paquete
        tiempo_actual = linea_tiempo_llegada[0];
        // linea_tiempo_tx[0] = (float) (tiempo_actual + tiempo_tx + tiempo_prop);
        linea_tiempo_tx[0] = (float) (tiempo_actual + tiempo_tx);

        // se inicia un ciclo que termina cuando se atienden a cada uno de los n paquetes 
        // y no queden paquetes por transmitir en la cola
        double int_llegada = 0;
        //inicio de ciclo de tx
        int paq=0;
        for (int i = 1; i < n; i++) {
            tiempo_actual = linea_tiempo_llegada[i];
            int_llegada = tiempo_actual - linea_tiempo_llegada[paq];
            //   System.out.println("Nuevo pkt"+linea_tiempo_llegada[i]+" slot---"+slotNuevo);
            if (int_llegada > tiempo_prop && tiempo_actual < (linea_tiempo_tx[i - 1] + tiempo_prop)) {
                //canal ocupado
                linea_tiempo_tx[i] = linea_tiempo_tx[i - 1];
                countPerd++;
            } else if (int_llegada <= tiempo_prop) {
                //canal libre--->colision
                if (!colission) {
                    countColiss++;
                }
                countColiss++;
                colission = true;
                // linea_tiempo_tx[i] = (float) (tiempo_actual + tiempo_tx + tiempo_prop);
                linea_tiempo_tx[i] = (float) (tiempo_actual + tiempo_tx);
            } else {
                //canal libre
                if (!colission) {
                    countSucc++;
                    //        System.out.println("NO COLISION ANTERIOR..se cuenta");
                } else {
                    //         System.out.println("Colisiono anterior..no se cuenta");
                    colission = false;
                }
                // linea_tiempo_tx[i] = (float) (tiempo_actual+ tiempo_tx+tiempo_prop);
                linea_tiempo_tx[i] = (float) (tiempo_actual + tiempo_tx);
                paq=i;
            }
        }

        real_S = ((countSucc) * (tiempo_tx)) / (1.0 * linea_tiempo_tx[n - 1]);
        //real_S=tiempo_tx*Math.exp(-g*tiempo_prop)/1.0 * linea_tiempo_tx[n - 1];
        // System.out.println("Count succ    " + countSucc);

        G = g_chik * tiempo_tx;
        alfa = tiempo_prop / tiempo_tx;
//        //S = gTe–gτ/g (T + 2τ) + e−gτ
         teoric_S = (G * Math.exp(-alfa * G)) / (G * (1 + 2 * alfa) + Math.exp(-alfa * G));
        //  teoric_S=(tiempo_tx*Math.exp(-g*tiempo_prop))/(tiempo_tx+2*tiempo_prop+1/g);
   //     teoric_S = (G * Math.exp(-G * (1 + 2 * alfa)) * (1 + G + alfa * G * (1 + G + (alfa * G) / 2))) / (G * (1 + alfa * 2) - (1 - Math.exp(-G * alfa)) + (1 - G * alfa) * Math.exp(-G * (1 + alfa)));
//
//        trials_Real = n / (1.0 * countSucc);
//        trials_Teoric = Math.exp(G);
    }

    public double getTeoric_S() {
        return teoric_S;
    }

    public double getReal_S() {
        return real_S;
    }

    public int getCountSucc() {
        return countSucc;
    }

    public double getG() {
        return G;
    }

    public double getTrials_Real() {
        return trials_Real;
    }

    public double getTrials_Teoric() {
        return trials_Teoric;
    }

    public int getCountColiss() {
        return countColiss;
    }

    public int getCountPerd() {
        return countPerd;
    }

    public double getG_chik() {
        return g_chik;
    }
    

}
