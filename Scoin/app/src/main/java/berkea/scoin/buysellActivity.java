package berkea.scoin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.Timer;
import java.util.TimerTask;

public class buysellActivity extends AppCompatActivity {

    double credit;
    TextView textDolar, textCoin, textD;
    EditText buyET, sellET;
    Button buyB, sellB;
    double sCoinCurrent;
    double sCoin;
    DecimalFormat numberFormat = new DecimalFormat("#.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buysell);

        credit = getIntent().getDoubleExtra("credit", 0.0);
        sCoin = getIntent().getDoubleExtra("scoin", 0.0);


        textD = (TextView) findViewById(R.id.text3);

        textDolar = (TextView) findViewById(R.id.creditT);
        textDolar.setText("Credit USD: " + numberFormat.format(credit) + " $");

        textCoin = (TextView) findViewById(R.id.creditST);
        textCoin.setText("Credit Scoin: " + numberFormat.format(sCoin) + " S");

        buyB = (Button) findViewById(R.id.buyB);
        sellB = (Button) findViewById(R.id.sellB);

        buyET = (EditText) findViewById(R.id.buyET);
        sellET = (EditText) findViewById(R.id.sellET);



        //an api pulling interval to get latest scoin value
        final RequestQueue ExampleRequestQueue = Volley.newRequestQueue(getApplicationContext());

        final String url2 = "https://devakademi.sahibinden.com/ticker";

        new Timer().scheduleAtFixedRate(new TimerTask(){

            @Override
            public void run(){
                StringRequest ExampleStringRequest = new StringRequest(Request.Method.GET, url2, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject oneObject = new JSONObject(response);

                            String value = oneObject.getString("value");
                            textD.setText("1 Scoin =" + value + " $");
                            sCoinCurrent = Double.parseDouble(value);
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


        //buy and sell operations are done here
        buyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double buy = Double.parseDouble(buyET.getText().toString());
                double moneySpent = sCoinCurrent*buy;

                if(credit>=moneySpent){
                    credit = credit - moneySpent;
                    sCoin = sCoin + buy;

                    textCoin.setText("Credit Scoin: " + numberFormat.format(sCoin) + " S");
                    textDolar.setText("Credit USD: " + numberFormat.format(credit) + " $");
                }
            }
        });

        sellB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double sell = Double.parseDouble(sellET.getText().toString());
                double moneyEarned = sCoinCurrent*sell;

                if(sCoin>=sell){
                    credit = credit + moneyEarned;
                    sCoin = sCoin - sell;

                    textCoin.setText("Credit Scoin: " + numberFormat.format(sCoin) + " S");
                    textDolar.setText("Credit USD: " + numberFormat.format(credit) + " $");
                }
            }
        });
    }

    //keeps the amount of money and scoin the user has by passing it to main activity on return.
    //as long as the main activity lives the users' data are stored
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("credit", credit);
        intent.putExtra("scoin", sCoin);
        startActivity(intent);
    }
}
