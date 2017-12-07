/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redes_simulacion;

import static java.lang.Math.log;
import java.util.LinkedList;
import java.util.Random;

/**
 *
 * @author maestria
 */
public class simulacion {

    int n, nbuffer, count_pkt_perdidos, pbuffer;
    float Miu, lambda, time_tx_pkt, rho, tiempo_actual, teoric_E_n, real_E_n;
    float[] taus, linea_tiempo_llegada, linea_tiempo_tx;
    LinkedList<Integer> buffer;
    int C_n[];
    Double[] teoric_P_n, real_P_n;

    Random ran;

    public simulacion(float miu, float lambda) {
        n = 1000;                // número de paquetes a enviar
        nbuffer = 5;             // tamaño del buffer
        this.Miu = miu;              // tasa de servicio [pkt/seg]
        this.lambda = lambda;           // tasa promedio de llegada de pkt [pkt/seg]
        time_tx_pkt = 1 / Miu; // tiempo promedio de tx de paquetes
        rho = lambda / Miu;    // intensidad del tráfico   
        // variables auxiliares
        ran = new Random();   // variable para generar valores aleatorios con distribución normal
        taus = new float[n];  // tiempos entre paquetes [distribución de Poisson] 
        linea_tiempo_llegada = new float[n]; // línea de tiempo de llegada de los paquetes
        buffer = new LinkedList<>(); // simulación del buffer
        linea_tiempo_tx = new float[n]; // línea de tiempo de transmisión de los paquetes
        count_pkt_perdidos = 0;       // total de paqutes perdidos
        C_n = new int[nbuffer + 1]; // conteo de ocupacion de posicion n en el buffer     
        pbuffer = 0;                  // última posición ocupada en el buffer 
        // tiempo_actual;          // tiempo de llegada del último paquete
        // teoric_E_n;                 // cantidad teórica de paquetes promedio en el buffer (E(n))
        // real_E_n = 0;               // cantidad real de paquetes promedio en el buffer (E(n))
        // probabilidades teóricas de ocupación de cada n posición del buffer
        teoric_P_n = new Double[nbuffer + 1];
        // probabilidades reales de ocupación de cada n posición del buffer
        real_P_n = new Double[nbuffer + 1];
    }

    void run() {

        float sum = 0;
        for (int i = 0; i < n - 1; i++) {
            // inicializar los tiempos entre paquetes [distribución de Poisson] 
            taus[i] = (float) (-(1 / lambda) * log(1 - ran.nextFloat()));
            // definición de los tiempos de llegada de cada paquete 
            sum += taus[i];
            linea_tiempo_llegada[i] = sum;
        }

        //transmision del 1er paquete
        //("LLEGÓ PAQUETE " + 1 + " TIEMPO -> " + linea_tiempo_llegada[0]);
        linea_tiempo_tx[0] = linea_tiempo_llegada[0] + time_tx_pkt;
        //("NO HAY PAQUETES EN EL BUFFER...SE TRANSMITE");
        //("BUFFER--->" + buffer);
        C_n[pbuffer] += 1;

        // se inicia un ciclo que termina cuando se atienden a cada uno de los n paquetes 
        // y no queden paquetes por transmitir en el buffer
        for (int i = 1; i < n || !buffer.isEmpty(); i++) {
            if (i < n) {
                // se obtiene el tiempo de llegada del paquete actual
                tiempo_actual = linea_tiempo_llegada[i];
            } else {
                // si se atendieron a los n paquetes se asume un tiempo actual alto 
                // para que se atiendan los paquetes que quedan por transmitir en el buffer
                tiempo_actual = 99999;
            }
            // se recorren los paquetes en el buffer
            for (int j = 0; j < pbuffer; j++) {
                // se sacan del buffer en orden los que debieron salir 
                // según el tiempo actual y sus respectivos tiempos de transmisión
                if (linea_tiempo_tx[buffer.get(j) - 1] <= tiempo_actual) {
                    // texto de salida por consola
                    //("SE TRASMITE PAQUETE " + (buffer.get(j))
                    //        + " TIEMPO -> " + linea_tiempo_tx[buffer.get(j) - 1]);
                    // se transmite el paquete
                    buffer.remove(j);
                    pbuffer--;
                    j--;
                    //("BUFFER--->" + buffer);
                } else {
                    break;
                }
            }
            // si queda algún paquete por llegar al buffer
            if (i < n) {
                // texto de salida por consola
                //("LLEGÓ PAQUETE " + (i + 1) + " TIEMPO -> "
                //        + tiempo_actual);
                // analizo qué debo hacer con el paquete teniendo en cuenta 
                // el tiempo de tx del paquete anterior y el tiempo actual
                // si el paquete anterior no se ha transmitido 
                // (tiempo de transmisión es mayor que el actual) 
                // debo intentar poner al buffer al nuevo paquete 
                if (linea_tiempo_tx[i - 1] > tiempo_actual) {
                    if (pbuffer < nbuffer) { //acumulo paquete
                        buffer.add(i + 1);
                        pbuffer++;
                        // defino el tiempo de tx del paquete luego de que se tx el anterior
                        linea_tiempo_tx[i] = linea_tiempo_tx[i - 1] + time_tx_pkt;
                        //("SE ACUMULA");
                        // aumento contador de la posición actual del buffer
                        C_n[pbuffer] += 1;
                    } else {   // desecho paquete porque el buffer está lleno
                        linea_tiempo_tx[i] = linea_tiempo_tx[i - 1];
                        //("SE DESECHA PAQUETE");
                        count_pkt_perdidos++;
                    }
                } else {
                    // si el tiempo de transmisión del paquete anterior es menor al tiempo
                    // actual, significa que no hay paquetes pendientes en el buffer       
                    linea_tiempo_tx[i] = tiempo_actual + time_tx_pkt;
                    // texto de salida por consola
                    //("NO HAY PAQUETES EN EL BUFFER...SE TRANSMITE");
                    C_n[pbuffer] += 1;
                }
                // texto de salida por consola
                //("BUFFER--->" + buffer);
            }
        }
        // Calculando probabilidad real de buffer vacío P(0) 
        //("Probabilidad de que el buffer esté vacío " + real_P_n[0]);

        for (int i = 0; i < nbuffer + 1; i++) {
            // Calculando probabilidades reales de posición n ocupada en buffer
            real_P_n[i] = new Double((float) C_n[i] / (n - count_pkt_perdidos));

            // Calculando probabilidades teoricas de posicion n ocupada en buffer
            teoric_P_n[i] = Math.pow(rho, i) * ((1 - rho) / (1 - Math.pow(rho, nbuffer + 1)));

            // Calculando probabilidad real de ocupación del canal E(n)
            real_E_n += real_P_n[i] * i;
            // texto de salida por consola
            //("Probabilidad real de " + i + " es " + real_P_n[i]);
            //("Probabilidad teorica de " + i + " es " + teoric_P_n[i]);
        }
        //Calculando probabilidad teórica de ocupación 
        if (rho > 1) {
            teoric_E_n = nbuffer;
        } else {   // rho < 1 
            teoric_E_n = (float) (rho / (1 - rho));
        }

        //("Probabilidad de ocupación real " + real_E_n);
        //("Probabilidad de ocupación teórica " + teoric_E_n);
        // //("Paquetes enviados inmediatamente " + C_n[0]);
        //("Paquetes rechazados " + count_pkt_perdidos);
    }

    void showChart() {
        // Creando gráficas
        Object data[][] = new Object[3][2 * (nbuffer + 1)];
        for (int i = 0; i < 2 * (nbuffer + 1); i++) {
            if (i < (nbuffer + 1)) {
                data[0][i] = real_P_n[i];
                data[1][i] = i;
                data[2][i] = "Probabilidad real";
            } else {
                data[0][i] = teoric_P_n[i - (nbuffer + 1)];
                data[1][i] = i - (nbuffer + 1);
                data[2][i] = "Probabilidad teórica";
            }
            //(data[0][i] + "--" + data[1][i] + "--" + data[2][i]);
        }
        chart probabilidades_n = new chart(data);
        probabilidades_n.setVisible(true);
    }

    public int getCount_pkt_perdidos() {
        return count_pkt_perdidos;
    }
    public float getTeoric_E_n() {
        return teoric_E_n;
    }
    public float getReal_E_n() {
        return real_E_n;
    }
    public Double[] getReal_P_n() {
        return real_P_n;
    }
    public Double[] getTeoric_P_n() {
        return teoric_P_n;
    } 
}
