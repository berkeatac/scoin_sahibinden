package berkea.scoin;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class historyActivity extends AppCompatActivity {
    LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {});

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        final GraphView graph = (GraphView) findViewById(R.id.graph);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);


        //get the historical data as json from api and then parse it to create a series object.
        //then set the series object as the data source for the graph
        //the properties of the graph are same as the one in main activity

        final RequestQueue ExampleRequestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://devakademi.sahibinden.com/history";

        StringRequest ExampleStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray json = new JSONArray(response);
                    graph.getViewport().setMaxX(json.length());
                    for(int i=0;i<json.length();i++){
                        JSONObject e = json.getJSONObject(i);
                        String date = e.getString("date");
                        String value = e.getString("value");
                        DataPoint dp = new DataPoint(i, Double.parseDouble(value));

                        series.appendData(dp,true,json.length());
                    }

                    graph.getGridLabelRenderer().setGridColor(Color.GRAY);
                    graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);


                    graph.getGridLabelRenderer().setVerticalLabelsColor(Color.GRAY);

                    series.setColor(Color.GREEN);
                    graph.removeAllSeries();
                    graph.addSeries(series);

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
}

