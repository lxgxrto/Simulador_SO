/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package simulador2;

import java.awt.Color;
import java.util.Map;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;

/**
 *
 * @author Fernando
 */
public class LineChart extends ApplicationFrame {

    public LineChart(String title, Map<Double,Double> lista , String nombre) {
        super(title);
        final XYDataset dataset = createDataset(lista,nombre);
        final JFreeChart chart = createChart(dataset, nombre);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(1000,800));
        setContentPane(chartPanel);
    }
    
    private XYDataset createDataset(Map<Double,Double> lista , String nombre){
        XYSeriesCollection dataset;
        dataset = new XYSeriesCollection();
        final XYSeries serie = new XYSeries(nombre);
        for(double primero : lista.keySet()){
            serie.add(primero,lista.get(primero));
        }
        dataset.addSeries(serie);
        return dataset;  
    }
    
    private JFreeChart createChart(final XYDataset dataset, String nombre){
        final JFreeChart chart = ChartFactory.createXYLineChart(
                nombre,
                "Cuantos",
                "% de fragmentación",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        ); 
        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        chart.setBackgroundPaint(Color.lightGray);

//        final StandardLegend legend = (StandardLegend) chart.getLegend();
  //      legend.setDisplaySeriesShapes(true);
        
        // get a reference to the plot for further customisation...
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.white);
    //    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
        plot.setDomainGridlinePaint(Color.black);
        plot.setRangeGridlinePaint(Color.black);
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(1, false);
        renderer.setSeriesShapesVisible(0, false);
        plot.setRenderer(renderer);

        // change the auto tick unit selection to integer units only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        // OPTIONAL CUSTOMISATION COMPLETED.
                
        return chart;
    }
}
