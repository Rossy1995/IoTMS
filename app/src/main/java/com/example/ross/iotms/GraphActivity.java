package com.example.ross.iotms;

import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Random;

public class GraphActivity extends AppCompatActivity {

    ArrayList<Integer> values = new ArrayList<Integer>();
    Random random = new Random();
    int interval;
    int value;
    int sumWattage = random.nextInt(21000) + 200000;
    LineGraphSeries<DataPointInterface> series = new LineGraphSeries<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_graph);

        GraphView graphView = (GraphView) findViewById(R.id.graph);

        TextView average = (TextView)findViewById(R.id.average);
        TextView estimateCost = (TextView)findViewById(R.id.estimateCost);

        GridLabelRenderer gridLabel = graphView.getGridLabelRenderer();

        graphView.setTitleTextSize(45);
        gridLabel.setVerticalAxisTitle("Watt's");
        gridLabel.setHorizontalAxisTitle("Seconds passed");

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
        staticLabelsFormatter.setVerticalLabels(new String[] {"0", "1", "2", "3", "4", "5"});
        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        average.setText("Average: 1503 (kW/h)");

        double val = (70 - 20) * random.nextDouble();
        val = val*100;
        val = Math.round(val);
        val = val/100;

        estimateCost.setText("Estimated monthly cost: £" + val);

        graphView.addSeries(series);
        graphView.setTitle("Live Energy Consumption");

        // set manual X bounds
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(30);

        // set manual Y bounds
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(50);

        // set viewport settings
        Viewport viewport = graphView.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(5);
        viewport.setScrollable(true);
    }

    @Override
    protected void onResume() {

        super.onResume();
        // we're going to simulate real time with thread that append data to the graph
        new Thread(new Runnable() {

            @Override
            public void run() {
                // we add 100 new entries
                for (int i = 0; i < 1000; i++) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            TextView sum = (TextView)findViewById(R.id.sum);        // if it works it works
                            addEntry();
                            sumWattage = sumWattage + value;
                            sum.setText("Total watts used: " + String.valueOf(sumWattage) + " (kW)");
                        }
                    });


                    // sleep to slow down the add of entries
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // manage error ...
                    }
                }
            }
        }).start();
    }

    // add random data to graph
    private void addEntry() {

        // here, we choose to display max 10 points on the viewport and we scroll to end
        value = dataValue();
        series.appendData(new DataPoint(interval++, value), true, 500);

    }

    private int dataValue (){
        return  random.nextInt(5) + 1;
    }

    // public void setVerticalAxisTitle(java.lang.String mVerticalAxisTitle) {

   // }

   // public java.lang.String getVerticalAxisTitle() {

   // }


}