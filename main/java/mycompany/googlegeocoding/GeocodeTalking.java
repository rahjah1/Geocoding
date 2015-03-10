package mycompany.googlegeocoding;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class GeocodeTalking {
    String searchString="", newString;
    String ParsedString;
    JSONObject ReturnInfo;
    LatLng newLocation;
    public GoogleMap Map2;
    String LocationName;
    public GeocodeTalking(String temp, GoogleMap Map){
        searchString=temp;
        Map2=Map;
        CallGeocoding();
    }
    public void CallGeocoding(){
        new HttpAsyncTask().execute(generateURL());
        //Open server, call http provided by generateURL
    }
    public String generateURL(){
        String URLrequest = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        newString=searchString.replace(" ","+");
        URLrequest+=newString;
        return URLrequest;
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return TryHttp(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                ParsedString=result;
                ReturnInfo = new JSONObject(result);
                JSONObject Location=ReturnInfo.getJSONArray("results").getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location");
                newLocation = new LatLng(Location.getDouble("lat"),Location.getDouble("lng"));
                /*Location=ReturnInfo.getJSONArray("results").getJSONObject(0)
                        .getString("formatted_address");
                LocationName=Location.toString();*/
                MoveMap();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            catch(Exception e ){
                System.out.println(e);
            }
        }
    }

    public static String TryHttp(String url){
        InputStream inputStream = null;
        String result="";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = buildJSON(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String buildJSON(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        StringBuilder build=new StringBuilder();
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;
        inputStream.close();
        return result;

    }
      public void MoveMap() {
        try {
            Map2.addMarker(new MarkerOptions()
                    .position(newLocation)
                    //.title(LocationName)
                    .draggable(true));
            Map2.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 15));
        }
        catch (Exception e){
            System.out.println(e);
        }
    }
}
