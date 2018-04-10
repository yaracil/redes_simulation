/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mac_TDMA_simulation;

import common.chart;

/**
 *
 * @author maestria
 */
public class Redes_simulacion {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        double velMaxCanal = 20 * Math.pow(10, 6),
                lambda = 100,
                tamPakt = 10 * Math.pow(10, 3),
                miu, c;
        int n = 100000;
        int cant_clientes = 20;
        double paso = 0.5;
        int cant_datos_muestra = (int) ((1 / paso) * cant_clientes);
        int cant_graficas = 2;
        int tamano_dataArray = (int) (cant_graficas * cant_datos_muestra);

        //int clients[] = {1,5,10,11,12,13,14,15,16,17,18,19,20};
        Object dataOcupacion[][] = new Object[3][tamano_dataArray];
        Object dataTiempoSistema[][] = new Object[3][tamano_dataArray];
        Object dataTiempoCola[][] = new Object[3][tamano_dataArray];

        Object dataOcupacionTFijo[][] = new Object[3][cant_datos_muestra *2];
        Object dataTiempoSistemaTFijo[][] = new Object[3][cant_datos_muestra *2 ];
        Object dataTiempoColaTFijo[][] = new Object[3][cant_datos_muestra * 2];

        Object dataOcupacionTVariab[][] = new Object[3][cant_datos_muestra * 2];
        Object dataTiempoSistemaTVariab[][] = new Object[3][cant_datos_muestra * 2];
        Object dataTiempoColaTVariab[][] = new Object[3][cant_datos_muestra * 2];

        int i = 0, t, i2, t2;
        for (double k = 0.5; k <= cant_clientes; k += paso, i++) {

            c = (float) (velMaxCanal / k);
            t = i + cant_datos_muestra;
            i2 = i + cant_datos_muestra * 2;
            t2 = i + cant_datos_muestra * 3;

            miu = (float) (c / tamPakt);

            // TAMANNO FIJO 
            simulacion simulacionTamFijo = new simulacion(n, lambda, miu,55);
            simulacionTamFijo.run();
            dataOcupacion[0][i] = simulacionTamFijo.getReal_E_n();
            dataOcupacion[1][i] = k;
            dataOcupacion[2][i] = "Probabilidad real tamaño fijo";

            dataTiempoSistema[0][i] = simulacionTamFijo.getReal_TPsistema();
            dataTiempoSistema[1][i] = k;
            dataTiempoSistema[2][i] = "Probabilidad real tamaño fijo";

            dataTiempoCola[0][i] = simulacionTamFijo.getRealTPcola();
            dataTiempoCola[1][i] = k;
            dataTiempoCola[2][i] = "Probabilidad real tamaño fijo";

            dataOcupacion[0][t] = simulacionTamFijo.getTeoric_E_n();
            dataOcupacion[1][t] = k;
            dataOcupacion[2][t] = "Probabilidad teórica tamaño fijo";

            dataTiempoCola[0][t] = simulacionTamFijo.getTeoric_TPcola();
            dataTiempoCola[1][t] = k;
            dataTiempoCola[2][t] = "Probabilidad teórica tamaño fijo";

            dataTiempoSistema[0][t] = simulacionTamFijo.getTeoric_TPsistema();
            dataTiempoSistema[1][t] = k;
            dataTiempoSistema[2][t] = "Probabilidad teórica tamaño fijo";

            // graficas II
            dataOcupacionTFijo[0][i] = simulacionTamFijo.getReal_E_n();
            dataOcupacionTFijo[1][i] = k;
            dataOcupacionTFijo[2][i] = "Probabilidad real tamaño fijo";

            dataOcupacionTFijo[0][t] = simulacionTamFijo.getTeoric_E_n();
            dataOcupacionTFijo[1][t] = k;
            dataOcupacionTFijo[2][t] = "Probabilidad teórica tamaño fijo";

            dataTiempoSistemaTFijo[0][i] = simulacionTamFijo.getReal_TPsistema();
            dataTiempoSistemaTFijo[1][i] = k;
            dataTiempoSistemaTFijo[2][i] = "Probabilidad real tamaño fijo";

            dataTiempoSistemaTFijo[0][t] = simulacionTamFijo.getTeoric_TPsistema();
            dataTiempoSistemaTFijo[1][t] = k;
            dataTiempoSistemaTFijo[2][t] = "Probabilidad teórica tamaño fijo";

            dataTiempoColaTFijo[0][i] = simulacionTamFijo.getRealTPcola();
            dataTiempoColaTFijo[1][i] = k;
            dataTiempoColaTFijo[2][i] = "Probabilidad real tamaño fijo";            
            
            dataTiempoColaTFijo[0][t] = simulacionTamFijo.getTeoric_TPcola();
            dataTiempoColaTFijo[1][t] = k;
            dataTiempoColaTFijo[2][t] = "Probabilidad teórica tamaño fijo";
            

            System.out.println("Cantidad de clientes---" + k);
            System.out.println("Velocidad de tx---" + c);
            System.out.println("Miu-----" + miu);
            System.out.println("Cantidad promedio real de paquetes en cola---" + simulacionTamFijo.getReal_E_n());
            System.out.println("Cantidad promedio teorica de paquetes en cola---" + simulacionTamFijo.getTeoric_E_n());

            System.out.println("Tiempo promedio real en sistema---" + simulacionTamFijo.getReal_TPsistema());
            System.out.println("Tiempo promedio teorico en sistema---" + simulacionTamFijo.getTeoric_TPsistema());
            System.out.println("Tiempo promedio real en cola---" + simulacionTamFijo.getRealTPcola());
            System.out.println("Tiempo promedio teorico en cola---" + simulacionTamFijo.getTeoric_TPcola());

        }
        showChartOcupacion(dataOcupacion);
        showChartTiempoenCola(dataTiempoCola);
        showChartTiempoenSistema(dataTiempoSistema);
        
        showChartOcupacion(dataOcupacionTFijo);
        showChartTiempoenCola(dataTiempoColaTFijo);
        showChartTiempoenSistema(dataTiempoSistemaTFijo);
        showChartOcupacion(dataOcupacionTVariab);
        showChartTiempoenCola(dataTiempoColaTVariab);
        showChartTiempoenSistema(dataTiempoSistemaTVariab);
        

    }

    static void showChartOcupacion(Object data[][]) {
        // Creando gráficas

        chart probabilidades_n = new chart(data, "Cantidad de clientes", "Cantidad de paquetes en cola");
        probabilidades_n.setVisible(true);
    }

    static void showChartTiempoenCola(Object data[][]) {
        // Creando gráficas

        chart probabilidades_n = new chart(data, "Cantidad de clientes", "Tiempo en cola");
        probabilidades_n.setVisible(true);
    }

    static void showChartTiempoenSistema(Object data[][]) {
        // Creando gráficas

        chart probabilidades_n = new chart(data, "Cantidad de clientes", "Tiempo en el sistema");
        probabilidades_n.setVisible(true);
    }
}
