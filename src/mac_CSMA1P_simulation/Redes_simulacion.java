/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mac_CSMA1P_simulation;

import mac_CSMA.NP_simulation.*;
import mac_SlottedAloha_simulation.*;
import common.TimeSeries_AWT;
import common.chart;
import org.jfree.ui.RefineryUtilities;

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
                tamPakt = 10* Math.pow(10, 3);
        int n = 100000;
        int cant_lambda = 50000;
        double paso = 10;
        int cantFuncXGraf = 2;
        int cant_datos_muestra = (int) ((1 / paso) * cant_lambda);
      //  double tau = 25.6 * Math.pow(10, -6);
      double tau=5*Math.pow(10, -6);

        //int clients[] = {1,5,10,11,12,13,14,15,16,17,18,19,20};
        Object[][] dataThroughput = new Object[3][cant_datos_muestra * cantFuncXGraf];
//        Object[][] dataTries = new Object[3][cant_datos_muestra * 2];
//        Object[][] dataDelay = new Object[3][cant_datos_muestra * 2];

        int i = 0;
        //t, i2, t2;
        for (int k = 10; i < cant_datos_muestra; k += paso, i++) {

            //            t = i + cant_datos_muestra;
            //            i2 = i + cant_datos_muestra * 2;
            //            t2 = i + cant_datos_muestra * 3;
            //  public simulacion(int n, double g, double dataRate, double tamPkt) 
            //  System.out.println("LAMBDA----" + lambda);
            simulacion csma = new simulacion(n, lambda, velMaxCanal, tamPakt, tau);
            csma.run();
            lambda = k;
            dataThroughput[0][i] = csma.getReal_S();
            dataThroughput[1][i] = csma.getG();
            dataThroughput[2][i] = "Valor real";

            dataThroughput[0][i + cant_datos_muestra] = csma.getTeoric_S();
            dataThroughput[1][i + cant_datos_muestra] = csma.getG();
            dataThroughput[2][i + cant_datos_muestra] = "Valor teórico";
//
//            dataThroughput[0][i + cant_datos_muestra * 2] = csma.getTeoric_S() / 10;
//            dataThroughput[1][i + cant_datos_muestra * 2] = csma.getG();
//            dataThroughput[2][i + cant_datos_muestra * 2] = "Valor teóric. para 10 usuarios";
//
//            dataThroughput[0][i + cant_datos_muestra * 3] = csma.getTeoric_S() / 100;
//            dataThroughput[1][i + cant_datos_muestra * 3] = csma.getG();
//            dataThroughput[2][i + cant_datos_muestra * 3] = "Valor teóric. para 100 usuarios";
//
//            dataDelay[0][i] = (1 + 10 / csma.getTeoric_S() - 1 / lambda);
//            dataDelay[1][i] = csma.getG();
//            dataDelay[2][i] = "Valor teóric. para 10 usuarios";
//
//            dataDelay[0][i + cant_datos_muestra] = (1 + 100 / csma.getTeoric_S() - 1 / lambda);
//            dataDelay[1][i + cant_datos_muestra] = csma.getG();
//            dataDelay[2][i + cant_datos_muestra] = "Valor teóric. para 100 usuarios";
//
//            dataTries[0][i] = csma.getTrials_Real();
//            dataTries[1][i] = csma.getG();
//            dataTries[2][i] = "Valor real";
//
//            dataTries[0][i + cant_datos_muestra] = csma.getTrials_Teoric();
//            dataTries[1][i + cant_datos_muestra] = csma.getG();
//            dataTries[2][i + cant_datos_muestra] = "Valor teórico";
//            System.out.println("G****** " + csma.getG());
//            System.out.println("g*********"+csma.getG_chik());
//            System.out.println("REAL THROUGPUT********* " + csma.getReal_S());
//            System.out.println("TEOR THROUGPUT********* " + csma.getTeoric_S());
//            System.out.println("No. Paquetes Succ****** " + csma.getCountSucc());
//            System.out.println("Perdidos*******"+csma.getCountPerd());
//            System.out.println("Colisionados*********"+csma.getCountColiss());
//            System.out.println("No. Intentos Teoric****** " + csma.getTrials_Teoric());
        }

//        for (int j = 0; j < dataTries.length; j++) {
//            Object[] dataTry = dataTries[j];
//
//        }
        showChartThroughput(dataThroughput);
//        showChartTrails(dataTries);
//        showChartDelays(dataDelay);

    }

    static void showChartThroughput(Object data[][]) {
        // Creando gráficas

        chart chart = new chart(data, "G", "Throughput");
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
