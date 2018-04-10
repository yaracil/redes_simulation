/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mac_SlottedAloha_simulation;

import mac_TDMA_simulation.*;
import static java.lang.Math.log;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author maestria
 */
public class simulacion {

    int n, nbuffer, countSucc, slotActual,slotNuevo;
    double dataRate, g, timeSlot, rho, tiempo_actual, teoric_E_n, real_E_n, 
          teoric_TPsistema, real_TPsistema, teoric_TPcola, realTPcola, inicio_envio;
    float[] taus, linea_tiempo_llegada, linea_tiempo_tx;

    int C_n[];
    Double[] teoric_P_n, real_P_n;

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

        //transmision del 1er paquete
        slotActual = (int) Math.ceil(linea_tiempo_llegada[0] % timeSlot);
        inicio_envio = slotActual * timeSlot;
        linea_tiempo_tx[0] = (float) (inicio_envio + timeSlot);

        // se inicia un ciclo que termina cuando se atienden a cada uno de los n paquetes 
        // y no queden paquetes por transmitir en la cola
        for (int i = 1; i < n; i++) {
            slotNuevo=(int) Math.ceil(linea_tiempo_llegada[i] % timeSlot);
            if (slotActual != slotNuevo) {
                
                if (linea_tiempo_tx[i - 1] > tiempo_actual) {
//                    buffer.add(i + 1);
//                    backOffCount++;
                    // defino el tiempo de tx del paquete luego de que se tx el anterior
//                    linea_tiempo_tx[i] = (float) (linea_tiempo_tx[i - 1] + tiempoto_nextframe + timeSlot);
                    //             System.out.println("SE ACUMULA");
                    // aumento contador de la posición actual del buffer
//                    C_n[backOffCount] += 1;

                } else {
                    // si el tiempo de transmisión del paquete anterior es menor al tiempo
                    // actual, significa que no hay paquetes pendientes en el buffer       
                    linea_tiempo_tx[i] = (float) (tiempo_actual + timeSlot);
                    // texto de salida por consola
                    //     System.out.println("NO HAY PAQUETES EN EL BUFFER...SE TRANSMITE");
//                    C_n[backOffCount] += 1;

                }
            }
        }
        //System.out.println("Paquetes perdidos****** " + count_pkt_perdidos);

        for (int i = 0; i < nbuffer + 1; i++) {
            // Calculando probabilidades reales de posición n ocupada en buffer
//            real_P_n[i] = new Double((float) C_n[i] / (n - count_pkt_perdidos));

            // Calculando probabilidades teoricas de posicion n ocupada en buffer
            //teoric_P_n[i] = Math.pow(rho, i) * ((1 - rho) / (1 - Math.pow(rho, nbuffer + 1)));
            // Calculando cantidad promedio real de paquetes esperados
            real_E_n += real_P_n[i] * i;

        }

        //Calculando tiempo promedio en sistema
        float tiempos_ensistema = 0;
        float tiempos_encola = 0;

        for (int i = 0; i < n; i++) {
            //System.out.println("llegada "+linea_tiempo_llegada[i]+" salida "+linea_tiempo_tx);
            tiempos_ensistema += linea_tiempo_tx[i] - linea_tiempo_llegada[i];

            // System.out.println("llegada "+linea_tiempo_llegada[i]+" salida "+linea_tiempo_tx[i]+ "tiempo en sistema" + (linea_tiempo_tx[i]-linea_tiempo_llegada[i]));
            tiempos_encola += linea_tiempo_tx[i] - linea_tiempo_llegada[i] - timeSlot;
        }
        real_TPsistema = tiempos_ensistema / n;

        //Calculando tiempo promedio en cola
        realTPcola = tiempos_encola / n;

        //Calculando cantidad promedio teórica de paquetes esperados
        if (rho >= 1) {
            teoric_E_n = nbuffer;
//            teoric_TPsistema = 1 / dataRate + (g * servicio_2do_moment) / (2 * (1 - 0.9999));
//            teoric_TPcola = (g * servicio_2do_moment) / (2 * (1 - 0.9999));

        } else {   // rho < 1 
//            teoric_E_n = (rho + (Math.pow(g, 2) * servicio_2do_moment) / (2 * (1 - rho)));

            //Calculando tiempo teórico promedio en el sistema
//            teoric_TPsistema = 1 / dataRate + (g * servicio_2do_moment) / (2 * (1 - rho));

            //Calculando tiempo teórico promedio en la cola
//            teoric_TPcola = (g * servicio_2do_moment) / (2 * (1 - rho));
        }
    }

    public double getTeoric_E_n() {
        return teoric_E_n;
    }

    public double getReal_E_n() {
        return real_E_n;
    }

    public Double[] getTeoric_P_n() {
        return teoric_P_n;
    }

    public double getRealTPcola() {
        return realTPcola;
    }

    public double getReal_TPsistema() {
        return real_TPsistema;
    }

    public double getTeoric_TPcola() {
        return teoric_TPcola;
    }

    public double getTeoric_TPsistema() {
        return teoric_TPsistema;
    }
}
