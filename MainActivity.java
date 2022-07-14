package com.mayurcodes.weatherappbeta;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;




public class MainActivity extends AppCompatActivity {
    String CITY1;
    TextView addressT, updated_atT, statusT, tempT, temp_minTxt, temp_maxT, sunriseT,sunsetT, windT, pressureT, humidityT;
    EditText CITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);
              //  findViewById(R.id.errorText).setVisibility(View.VISIBLE);
                findViewById(R.id.city).setVisibility(View.VISIBLE);
                findViewById(R.id.button2).setVisibility(View.VISIBLE);

              // findViewById(R.id.mainContainer).setVisibility(View.GONE);
            //    findViewById(R.id.imageContainer).setVisibility(View.GONE);
                findViewById(R.id.overviewContainer).setVisibility(View.GONE);
                findViewById(R.id.addressContainer).setVisibility(View.GONE);
                findViewById(R.id.presd).setVisibility(View.GONE);
                findViewById(R.id.sund).setVisibility(View.GONE);
                findViewById(R.id.sunsd).setVisibility(View.GONE);
                findViewById(R.id.wnd).setVisibility(View.GONE);
                findViewById(R.id.humd).setVisibility(View.GONE);
              //  CITY=findViewById(R.id.city);
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        setContentView(R.layout.activity_main);
        findViewById(R.id.presd).setVisibility(View.GONE);
        findViewById(R.id.sund).setVisibility(View.GONE);
        findViewById(R.id.sunsd).setVisibility(View.GONE);
        findViewById(R.id.wnd).setVisibility(View.GONE);
        findViewById(R.id.humd).setVisibility(View.GONE);


        CITY=findViewById(R.id.city);
        //boolean t = getLatitudeAndLongitudeFromGoogleMapForAddress("california");

    }
    public void run(View view){
        CITY1 = CITY.getText().toString();
        new weatherTask().execute();
    }


    /* public boolean getLatitudeAndLongitudeFromGoogleMapForAddress(String searchedAddress){

        // Context mContext = ApplicationProvider.getApplicationContext();
        Geocoder coder = new Geocoder(getApplicationContext(),Locale.getDefault());
        List<Address> address;
        try
        {
            address = coder.getFromLocationName(searchedAddress,5);
            if (address == null) {
                Log.d("TAG", "############Address not correct #########");
            }
            Address location = address.get(0);

            Log.d("TAG", "Address Latitude : "+ location.getLatitude() + "Address Longitude : "+ location.getLongitude());
            return true;

        }
        catch(Exception e)
        {
            Log.d("TAG", "MY_ERROR : ############Address Not Found");
            return false;
        }
    }
*/

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    class weatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            findViewById(R.id.loader).setVisibility(View.VISIBLE);
            findViewById(R.id.mainContainer).setVisibility(View.GONE);
            findViewById(R.id.errorText).setVisibility(View.GONE);
            findViewById(R.id.city).setVisibility(View.GONE);
            findViewById(R.id.button2).setVisibility(View.GONE);


        }

        protected String doInBackground(String args[]) {
            String response = HttpRequest.excuteGet("http://api.weatherapi.com/v1/forecast.json?key=YOUR_API_KEY&q="+ CITY1 + "&days=1&aqi=no&alerts=yes\n");

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            addressT = findViewById(R.id.address);
            updated_atT = findViewById(R.id.updated_at);
            statusT = findViewById(R.id.status);
            tempT = findViewById(R.id.temp);
            temp_minTxt = findViewById(R.id.temp_min);
            temp_maxT = findViewById(R.id.temp_max);
            sunriseT = findViewById(R.id.sunrise);
            sunsetT = findViewById(R.id.sunset);
            windT = findViewById(R.id.wind);
            pressureT = findViewById(R.id.pressure);
            humidityT = findViewById(R.id.humidity);

            if(result!=null) {
                // Do you work here on success

                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONObject main = jsonObj.getJSONObject("current");
                    JSONObject location = jsonObj.getJSONObject("location");
                    JSONObject sys = jsonObj.getJSONObject("forecast").getJSONArray("forecastday").getJSONObject(0).getJSONObject("astro");
                    //  JSONObject sys = jsonObj.getJSONObject("sys");
                    String wind = main.getString("wind_kph");
//                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

                    //    Long updatedAt = jsonObj.getJSONObject("current").getString("last_updated");
                    String updatedAtText = "Updated at: " + jsonObj.getJSONObject("current").getString("last_updated"); // new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                    String temp = main.getString("temp_c") + "°C";
                    //   String tempMin = "Min Temp: " + main.getString("temp_min") + "°C";
                    //  String tempMax = "Max Temp: " + main.getString("temp_max") + "°C";
                    String pressure = main.getString("pressure_in");
                    String humidity = main.getString("humidity");

                    String urlImage = "https:" + main.getJSONObject("condition").getString("icon");

                    String sunrise = sys.getString("sunrise");
                    String sunset = sys.getString("sunset");
                    String windSpeed = main.getString("wind_kph");
                    String weatherDescription = main.getJSONObject("condition").getString("text");

                    String address = location.getString("name") + ", " + location.getString("country");
                    addressT.setText(address);
                    updated_atT.setText(updatedAtText);
                    statusT.setText(weatherDescription.toUpperCase());
                    tempT.setText(temp);
                    // temp_minTxt.setText(tempMin);
                    // temp_maxT.setText(tempMax);
                    sunriseT.setText(sunrise);
                    sunsetT.setText(sunset);//new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
                    windT.setText(windSpeed);
                    pressureT.setText(pressure);
                    humidityT.setText(humidity);

                    try {

                        new DownloadImageTask((ImageView) findViewById(R.id.image))
                                .execute(urlImage);
                    } catch (Exception e) {
                        throw new Error(e);
                    }

                    findViewById(R.id.loader).setVisibility(View.GONE);
                    findViewById(R.id.overviewContainer).setVisibility(View.VISIBLE);
                    findViewById(R.id.addressContainer).setVisibility(View.VISIBLE);
                    findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);
                    // findViewById(R.id.imageContainer).setVisibility(View.VISIBLE);
                    findViewById(R.id.presd).setVisibility(View.VISIBLE);
                    findViewById(R.id.sund).setVisibility(View.VISIBLE);
                    findViewById(R.id.sunsd).setVisibility(View.VISIBLE);
                    findViewById(R.id.wnd).setVisibility(View.VISIBLE);
                    findViewById(R.id.image).getLayoutParams().height = 250;
                    findViewById(R.id.image).getLayoutParams().width = 250;


                    findViewById(R.id.humd).setVisibility(View.VISIBLE);


                } catch (JSONException e) {
                    findViewById(R.id.loader).setVisibility(View.GONE);
                    findViewById(R.id.errorText).setVisibility(View.VISIBLE);
                    throw new Error(e);

                }
            }
            else{

                findViewById(R.id.errorText).setVisibility(View.VISIBLE);
              //  findViewById(R.id.errorText).setText("Error occured");
                // null response or Exception occur
            }

        }
    }

}



class HttpRequest {
    public static String excuteGet(String targetURL) {
        URL url;
        HttpURLConnection connection = null;
        try {
            url = new URL(targetURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            InputStream is;
            int status = connection.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK)
                is = connection.getErrorStream();
            else
                is = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

    }


}



