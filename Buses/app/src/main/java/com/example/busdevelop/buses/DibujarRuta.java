package com.example.busdevelop.buses;

import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DibujarRuta {

    private List<LatLng> mMarkerParadas;
    private List<Marker> mMarkerRuta;
    private Usuario mUsuario;
    private GoogleMap mMap;

    /*
    * Dibuja la ruta en el mapa dado,
    * necesita la ruta para la cual se quiere dibujar
    * la lista de rutas y de paradas
    *
    */
    public DibujarRuta(Usuario usuario, GoogleMap map,Ruta rutaName){
        mUsuario = usuario;
        mMap = map;
        mMap.clear();
        mMarkerParadas = new ArrayList<LatLng>();
        mMarkerRuta = new ArrayList<Marker>();

        // Removes all the points in the ArrayList
        mMarkerParadas.clear();

        // Marcador para las paradas
        MarkerOptions paradas;

        //Agregar parada inicial
        mMarkerParadas.add(new LatLng(Double.parseDouble(rutaName.getParadaInicial().getLatitud().replaceAll(" ",".")),
                Double.parseDouble(rutaName.getParadaInicial().getLongitud().replaceAll(" ", "."))));
        // Poner marcador de parada inicial rojo
        paradas = new MarkerOptions();
        paradas.position(mMarkerParadas.get(0));
        paradas.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        Marker m = mMap.addMarker(paradas);
        mMarkerRuta.add(m);

        //Agregar parada final verde
        mMarkerParadas.add(new LatLng(Double.parseDouble(rutaName.getParadaFinal().getLatitud().replaceAll(" ",".")),
                Double.parseDouble(rutaName.getParadaFinal().getLongitud().replaceAll(" ","."))));
        // Poner marcador de parada inicial
        paradas = new MarkerOptions();
        paradas.position(mMarkerParadas.get(1));
        paradas.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mMap.addMarker(paradas);
        mMarkerRuta.add(m);

        //Agregar paradas intermedias azules
        List<Parada> listaParadas = rutaName.getParadasIntermedias();
        for(Parada p: listaParadas){

            String s = p.getLatitud();
            String s2 = p.getLongitud();
            mMarkerParadas.add(new LatLng(Double.parseDouble(p.getLatitud().replaceAll(" ",".")),
                    Double.parseDouble(p.getLongitud().replaceAll(" ","."))));
            // Poner marcadores de paradas intermedias
            paradas = new MarkerOptions();
            paradas.position(new LatLng(Double.parseDouble(p.getLatitud().replaceAll(" ",".")),
                    Double.parseDouble(p.getLongitud().replaceAll(" ","."))));
            paradas.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            mMap.addMarker(paradas);
            mMarkerRuta.add(m);
        }

            new ShowBuses(mUsuario,mMap,rutaName.getBuses());

        String url = getDirectionsUrl();
        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }

    private String getDirectionsUrl(){

        // Origin of route
        String str_origin = "origin="+mMarkerParadas.get(0).latitude+","+
                mMarkerParadas.get(0).longitude;

        // Destination of route
        String str_dest = "destination="+mMarkerParadas.get(1).latitude+","+
                mMarkerParadas.get(1).longitude;

        //paradas
        String str_waypts = "";
        for(int i = 2; i<mMarkerParadas.size(); i++){
            if(i==2) {
                str_waypts = "&waypoints=";
            }
            str_waypts += mMarkerParadas.get(i).latitude + "," + mMarkerParadas.get(i).longitude + "|";
        }

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+str_waypts+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            try{
                super.onPostExecute(result);

                ParserTask parserTask = new ParserTask();

                // Invokes the thread for parsing the JSON data
                parserTask.execute(result);
            } catch(IllegalArgumentException i){
                Log.e("Error de argumento",""+i.getMessage());
            }
        }
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception while downloading url", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }


    /** A class to parse the Google Places in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            try{
                ArrayList<LatLng> points = null;
                PolylineOptions lineOptions = null;

                // Traversing through all the routes
                for(int i=0;i<result.size();i++){
                    points = new ArrayList<LatLng>();
                    lineOptions = new PolylineOptions();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);

                    // Fetching all the points in i-th route
                    for(int j=0; j<path.size(); j++){
                        HashMap<String,String> point = path.get(j);

                        double lat = Double.parseDouble(point.get("lat"));
                        double lng = Double.parseDouble(point.get("lng"));
                        LatLng position = new LatLng(lat, lng);

                        points.add(position);
                    }

                    // Adding all the points in the route to LineOptions
                    lineOptions.addAll(points);
                    lineOptions.width(10);
                    lineOptions.color(Color.RED);
                }

                // Drawing polyline in the Google Map for the i-th route
                mMap.addPolyline(lineOptions);
            } catch(IllegalArgumentException i){
                Log.e("Error de argumento",""+i.getMessage());
            }
        }
    }
}
