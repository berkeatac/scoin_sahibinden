package berkea.scoin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    String url;
    TextView textD, textT;
    Button comB, histB, tradeB;
    String TAG = "RRR";
    int grai = -1;

    double credit = 10000;
    double scoin = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0);


        credit = getIntent().getDoubleExtra("credit", 10000.0);
        scoin = getIntent().getDoubleExtra("scoin", 0.0);

        textD = (TextView) findViewById(R.id.text);
        textT = (TextView) findViewById(R.id.text2);
        comB = (Button) findViewById(R.id.comB);
        histB = (Button) findViewById(R.id.histB);
        tradeB = (Button) findViewById(R.id.tradeB);

        final GraphView graph = (GraphView) findViewById(R.id.graph);

        //set graph properties
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(110);
        graph.getViewport().setScrollable(true);
        graph.getGridLabelRenderer().setGridColor(Color.GRAY);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getGridLabelRenderer().setVerticalLabelsColor(Color.GRAY);

        //An interval API caller that pulls data from the python server
        //that keeps track of the last 100 values of scoin it pulled from the ticker
        //so that it can draw the recent real time graph on initial start up

        final RequestQueue ExampleRequestQueue = Volley.newRequestQueue(getApplicationContext());

        /**
         *
         * THIS URL IS DYNAMIC. SET IT TO THE IP OF THE SERVER THAT IS RUNNING THE PYTHON SCRIPT
         */
        url = "http://172.16.55.43:5000";

        new Timer().scheduleAtFixedRate(new TimerTask(){

            @Override
            public void run(){
                StringRequest ExampleStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //get the array from server and parse it to create a series object
                        //then pass the series object to the graph
                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {});
                        String[] items = response.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll("\\s", "").split(",");

                        double[] results = new double[items.length];

                        for (int i = 0; i < items.length; i++) {
                            try {
                                results[i] = Double.parseDouble(items[i]);
                                DataPoint dp = new DataPoint(i,results[i]);
                                series.appendData(dp,true,100);
                            } catch (NumberFormatException nfe) {
                                //NOTE: write something here if you need to recover from formatting errors
                            };
                        }

                        series.setColor(Color.GREEN);
                        graph.removeAllSeries();
                        graph.addSeries(series);
                        Log.d(TAG, "onResponse: " + Arrays.toString(results));

                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //This code is executed if there is an error.
                    }
                });

                ExampleRequestQueue.add(ExampleStringRequest);
            }
        },0,10000);



        //Interval for API caller to get real time value of Scoin

        final String url2 = "https://devakademi.sahibinden.com/ticker";

        new Timer().scheduleAtFixedRate(new TimerTask(){

            @Override
            public void run(){
                StringRequest ExampleStringRequest = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "onResponse: " + response);
                        try {
                            JSONObject oneObject = new JSONObject(response);

                            // Pull date from object and write on screen
                            String date = oneObject.getString("date");
                            long epochtime = Long.parseLong(date);
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
                            Log.d(TAG, "onResponse: " + sdf.format(new Date(epochtime)) );
                            textT.setText(sdf.format(new Date(epochtime)));

                            //Pull value and write on screen
                            String value = oneObject.getString("value");
                            textD.setText(value + " $");
                        } catch (JSONException e) {
                            // Oops
                        }
                    }
                }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //This code is executed if there is an error.
                    }
                });

                ExampleRequestQueue.add(ExampleStringRequest);
            }
        },0,10000);


        // Button Click Listeners

        comB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), compareActivity.class);
                startActivity(intent);
            }
        });

        histB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), historyActivity.class);
                startActivity(intent);
            }
        });

        tradeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), buysellActivity.class);
                intent.putExtra("credit", credit);
                intent.putExtra("scoin", scoin);
                startActivity(intent);
            }
        });


    }
}
