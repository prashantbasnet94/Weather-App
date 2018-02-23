package com.example.prashant.weatherapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView=(TextView) findViewById(R.id.textView);
        editText=(EditText) findViewById(R.id.editText);
        Button button=(Button) findViewById(R.id.button);


    }

    public void goOperation(View view){

        ApiReader apiReader = new ApiReader();
        try {
            InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(),0);

            String result = apiReader.execute("http://openweathermap.org/data/2.5/weather?q="+editText.getText().toString()+"&appid=b6907d289e10d714a6e88b30761fae22").get();
            Log.i("Execute","http://openweathermap.org/data/2.5/weather?q="+editText.getText().toString()+"&appid=b6907d289e10d714a6e88b30761fae22");
        } catch (InterruptedException e) {
            Toast.makeText(this, "The location entered in invalid", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
            Toast.makeText(this, "The location entered in invalid", Toast.LENGTH_SHORT).show();

        }


    }



    public class ApiReader extends AsyncTask<String, Void, String> {



        @Override
        protected String doInBackground(String... urls) {

            URL url;
            HttpURLConnection connection;
            String result = "";

            try {
                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();


                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;

                    data = inputStreamReader.read();


                }

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();

                Toast.makeText(MainActivity.this, "The location entered in invalid", Toast.LENGTH_SHORT).show();
                return "failed";

            } catch (IOException e) {
                e.printStackTrace();
                return "failed";
            }


        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                String weather = jsonObject.getString("weather");

                JSONArray jsonArray = new JSONArray(weather);

                String result="";
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    result+=jsonObject1.getString("main")+" : "+jsonObject1.getString("description")+"\n";


                    Log.i("Main", jsonObject1.getString("main"));
             //       Log.i("Temp", jsonObject1.getString("temp"));
                  //  Log.i("pressure", jsonObject1.getString("pressure"));
                    Log.i("Description", jsonObject1.getString("description"));
                   // Log.i("humidity", jsonObject1.getString("humidity"));
                }
                textView.setText(result);


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "The location entered in invalid", Toast.LENGTH_SHORT).show();
            }


        }
    }
}