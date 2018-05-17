/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mac_CSMA.Slotted_simulation;

import common.XYLineChart_AWT;
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
                lambda = 2,
                tamPakt = 10 * Math.pow(10, 3);
        int n = 100000;
        int cant_lambda = 60000;
        double paso = 10;
        int cantFuncXGraf = 2;
        int cant_datos_muestra = (int) ((1 / paso) * cant_lambda);
        double timeSlot = 500 * Math.pow(10, -3);

        //int clients[] = {1,5,10,11,12,13,14,15,16,17,18,19,20};
        Object[][] dataThroughput = new Object[3][cant_datos_muestra * cantFuncXGraf];
//        Object[][] dataTries = new Object[3][cant_datos_muestra * 2];
//        Object[][] dataDelay = new Object[3][cant_datos_muestra * 2];

        int i = 0;
        //t, i2, t2;
        for (int k = 10; k <= cant_lambda; k += paso, i++) {

            //            t = i + cant_datos_muestra;
            //            i2 = i + cant_datos_muestra * 2;
            //            t2 = i + cant_datos_muestra * 3;
            //  public simulacion(int n, double g, double dataRate, double tamPkt) 
            //  System.out.println("LAMBDA----" + lambda);
            simulacion slotted_csma = new simulacion(n, lambda, velMaxCanal, tamPakt, timeSlot);
            slotted_csma.run();            
            dataThroughput[0][i] = slotted_csma.getReal_S();
            dataThroughput[1][i] = slotted_csma.getG();
            dataThroughput[2][i] = "Valor real";

            dataThroughput[0][i + cant_datos_muestra] = slotted_csma.getTeoric_S();
            dataThroughput[1][i + cant_datos_muestra] = slotted_csma.getG();
            dataThroughput[2][i + cant_datos_muestra] = "Valor teórico";

//            if (lambda == 2 || lambda == 20 || lambda == 200 || lambda == 2000 || lambda == 20000||lambda==200000) {
//                System.out.println("Lambda****"+lambda);
//                System.out.println("g_chiquita****"+slotted_csma.getG_chiq());
//                System.out.println("G****** " + slotted_csma.getG());
//                System.out.println("REAL THROUGPUT********* " + slotted_csma.getReal_S());
//                System.out.println("TEOR THROUGPUT********* " + slotted_csma.getTeoric_S());
//            }
            lambda = k;
        }

        showChartThroughput(dataThroughput);
//        showChartTrails(dataTries);
//        showChartDelays(dataDelay);

    }

    static void showChartThroughput(Object data[][]) {
        // Creando gráficas

        XYLineChart_AWT chart = new XYLineChart_AWT("Char", "", data, "", "G");
        // chart chart = new chart(data, "G", "Throughput");
        chart.setVisible(true);
    }

    static void showChartTrails(Object data[][]) {
        // Creando gráficas

        chart chart = new chart(data, "G", "Número de intentos");
        chart.setVisible(true);
    }

    static void showChartDelays(Object data[][]) {
        // Creando gráficas
//
//        TimeSeries_AWT chart = new chart(data, "G", "Delay");
//        chart.setVisible(true);

        chart chart = new chart(data, "G", "Delay");
        chart.setVisible(true);
    }

}
