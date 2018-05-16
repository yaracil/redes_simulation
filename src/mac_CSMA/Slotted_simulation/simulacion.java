/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mac_CSMA.Slotted_simulation;

import static java.lang.Math.log;
import java.util.Random;

/**
 *
 * @author maestria
 */
public class simulacion {

    int n, nbuffer, countSucc, slotOld, slotTx, slotNuevo, slots4Tx;
    double dataRate, g_chiq, G, timeSlot, rho, tiempo_actual, teoric_S, real_S, trials_Real, trials_Teoric, tiempo_tx;
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
    public simulacion(int n, double g, double dataRate, double tamPkt, double time_tx) {
        this.n = n;                // número de paquetes a enviar
        //  nbuffer = 1000;             // tamaño del buffer
        this.dataRate = dataRate;              // tasa de servicio [pkt/seg]
        this.g_chiq = g;           // tasa promedio de llegada de pkt [pkt/seg]
        this.tiempo_tx = time_tx; // tiempo promedio de tx de paquetes
        timeSlot = tamPkt / dataRate;
        rho = g / this.dataRate;    // intensidad del tráfico   
        // variables auxiliares
        ran = new Random();   // variable para generar valores aleatorios con distribución normal
        taus = new float[n];  // tiempos entre paquetes [distribución de Poisson] 
        linea_tiempo_llegada = new float[n]; // línea de tiempo de llegada de los paquetes
        linea_tiempo_tx = new float[n]; // línea de tiempo de transmisión de los paquetes
        countSucc = 0;                  // terminales en backoff 
        slotOld = 0;
        tiempo_actual = 0;
        colission = false;
        slots4Tx = (int) (tiempo_tx / timeSlot);

    }

    void run() {

        float sum = 0;
        for (int i = 0; i < n; i++) {
            // inicializar los tiempos entre paquetes [distribución de Poisson] 
            taus[i] = (float) (-(1 / g_chiq) * log(1 - ran.nextFloat()));
            // definición de los tiempos de llegada de cada paquete 
            sum += taus[i];
            linea_tiempo_llegada[i] = sum;
        }
        //System.out.println("TIEMPO DE SLOT " + timeSlot);

        //transmision del 1er paquete
        slotOld = (int) Math.ceil(linea_tiempo_llegada[0]);
        slotTx = slotOld + slots4Tx;
        //   tiempo_actual = slotTx * timeSlot;
        //   linea_tiempo_tx[0] = (float) (tiempo_actual + tiempo_tx);

        // se inicia un ciclo que termina cuando se atienden a cada uno de los n paquetes 
        // y no queden paquetes por transmitir en la cola
        for (int i = 1; i < n; i++) {

            slotNuevo = (int) Math.ceil(linea_tiempo_llegada[i] / timeSlot);
            tiempo_actual = slotNuevo * timeSlot;

            //   System.out.println("Nuevo pkt"+linea_tiempo_llegada[i]+" slot---"+slotNuevo);
            if (slotOld == slotNuevo) {
                colission = true;
                //   System.out.println("Colision!!!");
            } else if (slotTx < slotNuevo) {
                slotOld = slotNuevo;
                slotTx = slotNuevo + slots4Tx;
                if (!colission) {
                    countSucc++;
                    //        System.out.println("NO COLISION ANTERIOR..se cuenta");
                } else {
                    //         System.out.println("Colisiono anterior..no se cuenta");
                    colission = false;
                }
            } else if (slotTx > slotNuevo) {
                linea_tiempo_tx[i] = linea_tiempo_tx[i - 1];
                continue;
            }
            linea_tiempo_tx[i] = (float) (tiempo_actual + tiempo_tx);
        }

        real_S = (countSucc * timeSlot * slots4Tx * 1.0) / (1.0 * linea_tiempo_tx[n - 1]);
        G = timeSlot * g_chiq;
        double alfa = timeSlot / tiempo_tx;
//        System.out.println("Tiempo slot--"+timeSlot+" Tiempo tx---"+tiempo_tx);
//        System.out.println("alfa---"+alfa);
        //teoric_S = (alfa * G * Math.exp(-alfa * G)) / (1 + alfa - Math.exp(-alfa * G));
        teoric_S=(tiempo_tx*g_chiq*timeSlot*Math.exp(-g_chiq*timeSlot))/(tiempo_tx+timeSlot-tiempo_tx*Math.exp(-g_chiq*timeSlot));
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

    public double getG_chiq() {
        return g_chiq;
    }

}
