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
        File file_withEnhanc = null;

        FileReader fr = null;
        FileReader fr2 = null;
        FileReader fr_withEnhanc = null;

        BufferedReader br = null;
        BufferedReader br2 = null;
        BufferedReader br_withEnhanc = null;

        // int cantMuestrasxSeg = 60;
        int cantPntos = 5;
        int cantGraf = 2;
        Object[][] throughput = new Object[3][cantGraf * cantPntos];
        Object[][] energy = new Object[3][cantGraf * cantPntos];
        Object[][] Delay = new Object[3][cantGraf * cantPntos];
        Object[][] PacketDeliveryRatio = new Object[3][cantGraf * cantPntos];

        try {

            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).            
            for (int i = 0; i < cantPntos; i++) {
                archivo = new File("/home/claudia/workspace/bake/source/ns-3.28/test_1/simulation" + i + "_monitor");
                fr = new FileReader(archivo);
                br = new BufferedReader(fr);

//                file = new File("/home/claudia/workspace/bake/source/ns-3.28/test" + i + "_energy");
//                fr2 = new FileReader(file);
//                br2 = new BufferedReader(fr2);
                file_withEnhanc = new File("/home/claudia/workspace/bake/source/ns-3.28/test_60/simulation" + i + "_monitor");
                fr_withEnhanc = new FileReader(file_withEnhanc);
                br_withEnhanc = new BufferedReader(fr_withEnhanc);

                //1er archivo                
                String[] aux1;
                br.readLine();
                aux1 = br.readLine().split(",");

                //2do archivo
                String[] aux2;
                br_withEnhanc.readLine();//dejar pasar primera linea               
                aux2 = br_withEnhanc.readLine().split(",");

//                System.out.println("CantNodos " + numberNodes);
//                System.out.println("PauseTime " + tiempoPausa);
                //1ro
                energy[0][i] = Double.parseDouble(aux1[3]);
                energy[1][i] = i;
                energy[2][i] = "AODV_PURE";

                energy[0][i + cantPntos] = Double.parseDouble(aux2[3]);
                energy[1][i + cantPntos] = i;
                energy[2][i + cantPntos] = "AODV_EnrgyEnhance";

                //2do
                throughput[0][i] = Double.parseDouble(aux1[0]);
                throughput[1][i] = i;
                throughput[2][i] = "AODV_PURE";

                throughput[0][i + cantPntos] = Double.parseDouble(aux2[0]);
                throughput[1][i + cantPntos] = i;
                throughput[2][i + cantPntos] = "AODV_EnrgyEnhance";

                //3ro
                Delay[0][i] = Double.parseDouble(aux1[1]);
                Delay[1][i] = i;
                Delay[2][i] = "AODV_PURE";

                Delay[0][i + cantPntos] = Double.parseDouble(aux2[1]);
                Delay[1][i + cantPntos] = i;
                Delay[2][i + cantPntos] = "AODV_PURE_monitor";

                //4to
                PacketDeliveryRatio[0][i] = Double.parseDouble(aux1[2]);
                PacketDeliveryRatio[1][i] = i;
                PacketDeliveryRatio[2][i] = "AODV_PURE";

                PacketDeliveryRatio[0][i + cantPntos] = Double.parseDouble(aux2[2]);
                PacketDeliveryRatio[1][i + cantPntos] = i;
                PacketDeliveryRatio[2][i + cantPntos] = "AODV_PURE_monitor";

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

        showChart(throughput, "Parameters", "Throughput");
        showChart(energy, "Parameters", "Energy");
        showChart(PacketDeliveryRatio, "Parameters", "PacketDeliveryRatio");
        showChart(Delay, "Parameters", "Delay");

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
