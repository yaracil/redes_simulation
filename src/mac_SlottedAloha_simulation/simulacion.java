/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mac_SlottedAloha_simulation;

import static java.lang.Math.log;
import java.util.Random;

/**
 *
 * @author maestria
 */
public class simulacion {

    int n, nbuffer, countSucc, slotActual, slotNuevo;
    double dataRate, g, G, timeSlot, rho, tiempo_actual, teoric_S, real_S, trials_Real, trials_Teoric;
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
    public simulacion(int n, double g, double dataRate, double tamPkt) {
        this.n = n;                // número de paquetes a enviar
        nbuffer = 1000;             // tamaño del buffer
        this.dataRate = dataRate;              // tasa de servicio [pkt/seg]
        this.g = g;           // tasa promedio de llegada de pkt [pkt/seg]
        timeSlot = tamPkt / dataRate; // tiempo promedio de tx de paquetes
        rho = g / this.dataRate;    // intensidad del tráfico   
        // variables auxiliares
        ran = new Random();   // variable para generar valores aleatorios con distribución normal
        taus = new float[n];  // tiempos entre paquetes [distribución de Poisson] 
        linea_tiempo_llegada = new float[n]; // línea de tiempo de llegada de los paquetes
        linea_tiempo_tx = new float[n]; // línea de tiempo de transmisión de los paquetes
        countSucc = 0;                  // terminales en backoff 
        slotActual = 0;
        tiempo_actual = 0;
        colission = false;

    }

    void run() {

        float sum = 0;
        for (int i = 0; i < n; i++) {
            // inicializar los tiempos entre paquetes [distribución de Poisson] 
            taus[i] = (float) (-(1 / g) * log(1 - ran.nextFloat()));
            // definición de los tiempos de llegada de cada paquete 
            sum += taus[i];
            linea_tiempo_llegada[i] = sum;
        }
        //System.out.println("TIEMPO DE SLOT " + timeSlot);

        //transmision del 1er paquete
        slotActual = (int) Math.ceil(linea_tiempo_llegada[0] / timeSlot);
        tiempo_actual = slotActual * timeSlot;
        linea_tiempo_tx[0] = (float) (tiempo_actual + timeSlot);

        // se inicia un ciclo que termina cuando se atienden a cada uno de los n paquetes 
        // y no queden paquetes por transmitir en la cola
        for (int i = 1; i < n; i++) {

            slotNuevo = (int) Math.ceil(linea_tiempo_llegada[i] / timeSlot);
            tiempo_actual = slotNuevo * timeSlot;

            //   System.out.println("Nuevo pkt"+linea_tiempo_llegada[i]+" slot---"+slotNuevo);
            if (slotActual != slotNuevo) {
                slotActual = slotNuevo;
                if (!colission) {
                    countSucc++;
                    //        System.out.println("NO COLISION ANTERIOR..se cuenta");
                } else {
                    //         System.out.println("Colisiono anterior..no se cuenta");
                    colission = false;
                }
            } else {
                colission = true;
                //   System.out.println("Colision!!!");
            }
            linea_tiempo_tx[i] = (float) (tiempo_actual + timeSlot);
        }

        real_S = (countSucc * timeSlot) / (1.0 * linea_tiempo_tx[n - 1]);
        G = timeSlot * g;
        //S = gTP suc = gTe −gT
        teoric_S = G * Math.exp(-G);

        trials_Real = n / (1.0*countSucc);
        trials_Teoric = Math.exp(G);
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

}
