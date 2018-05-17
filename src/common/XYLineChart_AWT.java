/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import java.awt.Color;
import java.awt.BasicStroke;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.ui.ApplicationFrame;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

public class XYLineChart_AWT extends ApplicationFrame {

    public XYLineChart_AWT(String applicationTitle, String chartTitle, Object Data[][], String ejeX, String ejeY) {
        super(applicationTitle);
        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                chartTitle,
                "G",
                "Throughput",
                createDataset(Data, ejeX, ejeY),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(xylineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
        final XYPlot plot = xylineChart.getXYPlot();

//        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
//        renderer.setSeriesPaint(0, Color.GREEN);
//        renderer.setSeriesPaint(1, Color.RED);
//        // renderer.setSeriesPaint(2, Color.YELLOW);
//        renderer.setSeriesStroke(0, new BasicStroke(0.0f));
//        renderer.setSeriesStroke(1, new BasicStroke(2.0f));
//        // renderer.setSeriesStroke(2, new BasicStroke(2.0f));
//        plot.setRenderer(renderer);
        setContentPane(chartPanel);
    }

    private XYDataset createDataset(Object[][] Data, String ejeX, String ejeY) {

        final XYSeriesCollection dataset = new XYSeriesCollection();
        int i = 0;
//        System.out.println(Data[0].length);
//        System.out.println(Data.length);
        do {
            String nombreSerie = (String) Data[2][i];
            final XYSeries aux = new XYSeries(nombreSerie);
            while (i < Data[0].length && Data[2][i].equals(nombreSerie)) {
                aux.add((Double) Data[1][i], (Double) Data[0][i]);
                i++;
            }
          //  System.out.println(nombreSerie);
            dataset.addSeries(aux);
          //  System.out.println(Data[0].length);
          //  System.out.println(i);
        } while (i < Data[0].length);
        return dataset;
    }

//    public static void main(String[] args) {
//        XYLineChart_AWT chart = new XYLineChart_AWT("Browser Usage Statistics",
//                "Which Browser are you using?");
//        chart.pack();
//        RefineryUtilities.centerFrameOnScreen(chart);
//        chart.setVisible(true);
//    }
}
