/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mac_CSMA._1P_simulation;

import static java.lang.Math.log;
import java.util.Random;

/**
 *
 * @author maestria
 */
public class simulacion_csma1p {

    int n, nbuffer, countSucc, countPerd, countColiss, countPacketWaiting;
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
    public simulacion_csma1p(int n, double g, double dataRate, double tamPkt, double tiempo_prop) {
        this.n = n;                // número de paquetes a enviar
        nbuffer = 10000;             // tamaño del buffer
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
        countPacketWaiting = 0;

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
        double int_llegada_anterior = 0;
        double int_llegada_siguiente = 0;
        //inicio de ciclo de tx
        int lastPaqOnTx = 0;
        int lastPaqColiding = 0;
        int lastPaqWaiting = 0;

        int estado_actual = 1;
        for (int i = 1; i < n; i++) {

            tiempo_actual = linea_tiempo_llegada[i];

            if (tiempo_actual >= linea_tiempo_tx[lastPaqOnTx] + tiempo_prop) {
                //canal libre--->colision
                if (!colission) {
                    countSucc++;
                }
                colission = false;
                if (countPacketWaiting >= 1) {
                    if (countPacketWaiting > 1) {
                        colission = true;
                    }
                    if (!colission) {
                        countSucc++;
                    }
                    lastPaqOnTx = i - 1;
                    countPacketWaiting = 0;
                } else if (countPacketWaiting == 0) {
                    lastPaqOnTx = i;
                    colission = false;
                    linea_tiempo_tx[i] = (float) (tiempo_actual + tiempo_tx);
                    continue;
                }
            }

            //   System.out.println("Nuevo pkt"+linea_tiempo_llegada[i]+" slot---"+slotNuevo);
            //el paquete llego y estaba el canal ocupado
            if (tiempo_actual < (linea_tiempo_tx[lastPaqOnTx] + tiempo_prop) && (linea_tiempo_llegada[lastPaqOnTx] - tiempo_actual) > tiempo_prop) {
                //canal ocupado
                //  linea_tiempo_tx[i] = linea_tiempo_tx[i - 1];
                linea_tiempo_llegada[i] = (float) (linea_tiempo_tx[i - 1] + tiempo_prop);
                linea_tiempo_tx[i] = (float) (linea_tiempo_llegada[i] + tiempo_tx);
                countPacketWaiting++;
            } else if ((linea_tiempo_llegada[lastPaqOnTx] - tiempo_actual) < tiempo_prop) {
                colission = true;
                countColiss++;
            } else {
                countSucc++;
            }
            // linea_tiempo_tx[i] = (float) (tiempo_actual + tiempo_tx + tiempo_prop);
                    linea_tiempo_tx[i] = (float) (tiempo_actual + tiempo_tx
        
        );
        }

        real_S = ((countSucc) * (tiempo_tx)) / (1.0 * linea_tiempo_tx[n - 1]);
        //real_S=tiempo_tx*Math.exp(-g*tiempo_prop)/1.0 * linea_tiempo_tx[n - 1];
        // System.out.println("Count succ    " + countSucc);

        G = g_chik * tiempo_tx * 1.0;
        alfa = tiempo_prop / tiempo_tx * 1.0;
//        //S = gTe–gτ/g (T + 2τ) + e−gτ
        // teoric_S = (G*Math.exp(-G*(1+2*alfa))*(1+G+alfa*G*(1+G+alfa*G/2)))/(G*(1+2*alfa)-(1-Math.exp(-G*alfa)+(1+G*alfa)*Math.exp(-G*(1+alfa))));
        teoric_S = (g_chik * tiempo_tx * Math.exp(-g_chik * (tiempo_tx + 2 * tiempo_prop)) * (1 + g_chik * tiempo_tx + g_chik * tiempo_prop * (1 + g_chik * tiempo_tx + g_chik * tiempo_prop / 2))) / (g_chik * (tiempo_tx + 2 * tiempo_prop) - (1 - Math.exp(-g_chik * tiempo_prop)) + (1 + g_chik * tiempo_prop) * Math.exp(-g_chik * (tiempo_tx + tiempo_prop)));
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
