/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mac_CSMA.Slottedd;

import mac_CSMA.Slotted.*;
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
                lambda = 1,
                tamPakt = 10 * Math.pow(10, 3);
        int n = 100000;
        int cant_lambda = 6000;
        double paso = 10;
        int cantFuncXGraf = 2;
        int cant_datos_muestra = (int) ((1 / paso) * cant_lambda);
        double timeSlot=50*Math.pow(10, -3);

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
            simulacion slotted_aloha = new simulacion(n, lambda, velMaxCanal, tamPakt,timeSlot);
            slotted_aloha.run();
            lambda = k;
            dataThroughput[0][i] = slotted_aloha.getReal_S();
            dataThroughput[1][i] = slotted_aloha.getG();
            dataThroughput[2][i] = "Valor real cant. infinita de usuarios";

            dataThroughput[0][i + cant_datos_muestra] = slotted_aloha.getTeoric_S();
            dataThroughput[1][i + cant_datos_muestra] = slotted_aloha.getG();
            dataThroughput[2][i + cant_datos_muestra] = "Valor teórico cant. infinita de usuarios";

//            dataThroughput[0][i + cant_datos_muestra * 2] = slotted_aloha.getTeoric_S() / 10;
//            dataThroughput[1][i + cant_datos_muestra * 2] = slotted_aloha.getG();
//            dataThroughput[2][i + cant_datos_muestra * 2] = "Valor teóric. para 10 usuarios";
//
//            dataThroughput[0][i + cant_datos_muestra * 3] = slotted_aloha.getTeoric_S() / 100;
//            dataThroughput[1][i + cant_datos_muestra * 3] = slotted_aloha.getG();
//            dataThroughput[2][i + cant_datos_muestra * 3] = "Valor teóric. para 100 usuarios";
//
//            dataDelay[0][i] = (1 + 10 / slotted_aloha.getTeoric_S() - 1 / lambda);
//            dataDelay[1][i] = slotted_aloha.getG();
//            dataDelay[2][i] = "Valor teóric. para 10 usuarios";
//
//            dataDelay[0][i + cant_datos_muestra] = (1 + 100 / slotted_aloha.getTeoric_S() - 1 / lambda);
//            dataDelay[1][i + cant_datos_muestra] = slotted_aloha.getG();
//            dataDelay[2][i + cant_datos_muestra] = "Valor teóric. para 100 usuarios";
//
//            dataTries[0][i] = slotted_aloha.getTrials_Real();
//            dataTries[1][i] = slotted_aloha.getG();
//            dataTries[2][i] = "Valor real";
//
//            dataTries[0][i + cant_datos_muestra] = slotted_aloha.getTrials_Teoric();
//            dataTries[1][i + cant_datos_muestra] = slotted_aloha.getG();
//            dataTries[2][i + cant_datos_muestra] = "Valor teórico";

//            System.out.println("G****** " + slotted_aloha.getG());
//            System.out.println("REAL THROUGPUT********* " + slotted_aloha.getReal_S());
//            System.out.println("TEOR THROUGPUT********* " + slotted_aloha.getTeoric_S());
//            System.out.println("No. Intentos Real****** " + slotted_aloha.getTrials_Real());
//            System.out.println("No. Intentos Teoric****** " + slotted_aloha.getTrials_Teoric());
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

        XYLineChart_AWT chart= new XYLineChart_AWT("Char", "Throughput", data, "Throughput", "G");
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
