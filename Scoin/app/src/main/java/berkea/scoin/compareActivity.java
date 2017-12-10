package berkea.scoin;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

public class compareActivity extends AppCompatActivity {

    String usd = "0";
    String euro = "0";
    TextView valueText;
    Spinner s;
    String valueS = "0";
    DecimalFormat numberFormat = new DecimalFormat("#.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        s=(Spinner)findViewById(R.id.spin);
        valueText=(TextView) findViewById(R.id.valueText);

        String[] arraySpinner = new String[]{"TRY", "EURO"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, arraySpinner);
        s.setAdapter(adapter);


        final RequestQueue ExampleRequestQueue = Volley.newRequestQueue(getApplicationContext());

        /////////////////////////////////


        String urlTicker = "https://devakademi.sahibinden.com/ticker";
        StringRequest coinValReq = new StringRequest(Request.Method.GET, urlTicker, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject oneObject = new JSONObject(response);
                    valueS = oneObject.getString("value");
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

        ExampleRequestQueue.add(coinValReq);


        /////////////////////////////////


        //pull usd and euro and try values from an api (outsource)
        String url = "http://www.apilayer.net/api/live?access_key=6d2c39677a9a6e6bfb9a97d711e955e6";
        StringRequest ExampleStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("YES", "onResponse: " + response);
                try {
                    JSONObject oneObject = new JSONObject(response);
                    // Pulling items from the array
                    usd = oneObject.getJSONObject("quotes").getString("USDTRY");
                    usd = usd.substring(0,4);

                    euro = oneObject.getJSONObject("quotes").getString("USDEUR");
                    euro = euro.substring(0,4);

                    double dValS = Double.parseDouble(valueS);
                    double dUsd = Double.parseDouble(usd);

                    String val = numberFormat.format(dValS * dUsd);
                    valueText.setText(val + " TRY");

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


        //change the value shown as different currencies are selected from the spinner
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                double dValS = Double.parseDouble(valueS);
                double dUsd = Double.parseDouble(usd);
                double dEuro = Double.parseDouble(euro);

                if(position == 0){
                    String val = numberFormat.format(dValS*dUsd);
                    valueText.setText(val + " TRY");
                }
                else if(position == 1){
                    String val = numberFormat.format(dValS*dEuro);
                    valueText.setText(val + " €");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                //valueText.setText(Double.toString((Double.parseDouble(valueS))*(Double.parseDouble(usd))) + " $");
            }

        });




    }

}
