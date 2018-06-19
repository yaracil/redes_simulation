/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package charting;

import mac_SlottedAloha_simulation.*;
import common.TimeSeries_AWT;
import common.XYLineChart_AWT;
import common.chart;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author maestria
 */
public class Redes_simulacion1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] arg) {

        File archivo = null;
        File file = null;
        File file2 = null;

        FileReader fr = null;
        FileReader fr2 = null;
        FileReader fr3 = null;

        BufferedReader br = null;
        BufferedReader br2 = null;
        BufferedReader br3 = null;

       // int cantMuestrasxSeg = 60;
        int cantPntos = 9;
        Object[][] throughput = new Object[3][5 * cantPntos];
        Object[][] energy = new Object[3][cantPntos];
        Object[][] Delay = new Object[3][cantPntos];
        Object[][] PacketDeliveryRatio = new Object[3][cantPntos];

        try {

            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).            
            for (int i = 0; i < cantPntos; i++) {
                archivo = new File("/home/yoe/workspace/bake/source/ns-3.28/testing_8/simulation" + i);
                fr = new FileReader(archivo);
                br = new BufferedReader(fr);

                file = new File("/home/yoe/workspace/bake/source/ns-3.28/testing_8/simulation" + i + "_energy");
                fr2 = new FileReader(file);
                br2 = new BufferedReader(fr2);

                file2 = new File("/home/yoe/workspace/bake/source/ns-3.28/testing_8/simulation" + i + "_monitor");
                fr3 = new FileReader(file2);
                br3 = new BufferedReader(fr3);

                //1er archivo                
                String aux1;
                br2.readLine();//dejar pasar primera linea
                double energyAve = Double.parseDouble(br2.readLine().split(",")[1]);

                //2do archivo
                String aux2;
                br.readLine();//dejar pasar primera linea

                //3do archivo
                String[] aux3;
                br3.readLine();//dejar pasar primera linea
                aux3 = br3.readLine().split(",");

                String[] linea;

                linea = br.readLine().split(",");
                double ave = Integer.parseInt(linea[1]);
                String numberNodes = linea[3];
                String tiempoPausa = linea[6];

                while ((aux1 = br2.readLine()) != null && ((aux2 = br.readLine()) != null)) {
                    //1ero
                    energyAve += Double.parseDouble(aux1.split(",")[1]);

                    //2do
                    linea = aux2.split(",");
                    ave += Double.parseDouble(linea[1]);
                }

                System.out.println("CantNodos " + numberNodes);
                System.out.println("PauseTime " + tiempoPausa);
                //1ro
                energy[0][i] = energyAve;
                energy[1][i] = Double.parseDouble(numberNodes + "." + Integer.parseInt(tiempoPausa));
                energy[2][i] = "AODV_PURE";

                //2do
                throughput[0][i] = ave;
                throughput[1][i] = Double.parseDouble(numberNodes + "." + Integer.parseInt(tiempoPausa));
                throughput[2][i] = "AODV_PURE_tracing";

                //3do
                throughput[0][i + cantPntos] = Double.parseDouble(aux3[0]);
                throughput[1][i + cantPntos] = Double.parseDouble(numberNodes + "." + Integer.parseInt(tiempoPausa));
                throughput[2][i + cantPntos] = "AODV_PURE_monitor";
                
                throughput[0][i + 2*cantPntos] = Double.parseDouble(aux3[1]);
                throughput[1][i + 2*cantPntos] = Double.parseDouble(numberNodes + "." + Integer.parseInt(tiempoPausa));
                throughput[2][i + 2*cantPntos] = "AODV_PURE_monitor_2";
                
                throughput[0][i + 3*cantPntos] = Double.parseDouble(aux3[2]);
                throughput[1][i + 3*cantPntos] = Double.parseDouble(numberNodes + "." + Integer.parseInt(tiempoPausa));
                throughput[2][i + 3*cantPntos] = "AODV_PURE_monitor_3";
                
                throughput[0][i + 4*cantPntos] = Double.parseDouble(aux3[7])*512/1024;
                throughput[1][i + 4*cantPntos] = Double.parseDouble(numberNodes + "." + Integer.parseInt(tiempoPausa));
                throughput[2][i + 4*cantPntos] = "AODV_PURE_monitor_4";

                Delay[0][i] = Double.parseDouble(aux3[3]);
                Delay[1][i] = Double.parseDouble(numberNodes + "." + Integer.parseInt(tiempoPausa));
                Delay[2][i] = "AODV_PURE_monitor";

                PacketDeliveryRatio[0][i] = Double.parseDouble(aux3[5]);
                PacketDeliveryRatio[1][i] = Double.parseDouble(numberNodes + "." + Integer.parseInt(tiempoPausa));
                PacketDeliveryRatio[2][i] = "AODV_PURE_monitor";

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta
            // una excepcion.
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        showChart(throughput,"Parameters","Throughput");
        showChart(energy,"Parameters","Energy");
        showChart(PacketDeliveryRatio,"Parameters","PacketDeliveryRatio");
        showChart(Delay,"Parameters","Delay");

    }

    static void showChart(Object data[][], String X, String Y) {
        // Creando gráficas

//        XYLineChart_AWT chart = new XYLineChart_AWT("Char", "", data, "Throughput", "Parameters");
//        // chart chart = new chart(data, "G", "Throughput");
//        chart.setVisible(true);
        chart chart = new chart(data, X, Y);
        chart.setVisible(true);
    }
}
