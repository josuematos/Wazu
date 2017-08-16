package br.com.editorapendragon.wazu;


import br.com.editorapendragon.wazu.util.IabHelper;
import br.com.editorapendragon.wazu.util.IabHelper.IabAsyncInProgressException;
import br.com.editorapendragon.wazu.util.IabResult;
import br.com.editorapendragon.wazu.util.Inventory;
import br.com.editorapendragon.wazu.util.Purchase;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.provider.Settings;

import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;

import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.LikeView;
import com.facebook.share.widget.ShareButton;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.example.games.basegameutils.BaseGameUtils;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;






public class MapsActivity extends FragmentActivity  implements OnMapReadyCallback, OnMapClickListener, OnMapLongClickListener,NavigationView.OnNavigationItemSelectedListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener  {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "UQiGX8wtCVEPotL14yOKiWow4";
    private static final String TWITTER_SECRET = "HtmZhHscXZEyFSY2GfRlXXFTiQAgm87Tu5Lfu4XvggdIxaDIBI";


    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public String Duracao,Distancia,enderecoS,enderecoD;
    public Double distanciaprefs = 0.0;
    public GoogleMap mMap;
    private LatLng destino;
    private LatLng Mycoord;
    private Location locationD, locationS;
    private Uri alert;
    private MediaPlayer mp;
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    private Location bestloc;
    private Boolean alarmeativado = false;
    private Double precisao;
    private Boolean conectado = false;
    ArrayList<LatLng> markerPoints;
    ArrayList<LatLng> mAlertas;
    public static ArrayList<LatLng> tAlertas;
    public TextView alerta;
    private List<String> facebookPermitions;
    private CallbackManager callbackManager;
    private TwitterSession twsession;
    private AdView mAdView;
    IabHelper mHelper;
    public static boolean isService = false;
    public static boolean isAlarme = false;
    private VideoView myVideoView;
    private MediaController mediaController;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isReceiverRegistered;

    private GoogleApiClient mGoogleApiClient;
    private static int RC_SIGN_IN = 9001;
    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInflow = true;
    private boolean mSignInClicked = false;
    int REQUEST_ACHIEVEMENTS = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .detectNetwork()
                .detectAll() // for all detectable problems
                .penaltyLog()
                .build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                .detectLeakedSqlLiteObjects()
                //.detectLeakedClosableObjects()
                .penaltyLog()
                .penaltyDeath()
                .build());

        super.onCreate(savedInstanceState);


        // Create the Google Api Client with access to the Play Games services
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                // add other APIs and scopes here as needed
                .build();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        Context mContext = getApplicationContext();
        Toast.makeText(mContext, "Iniciando Serviços", Toast.LENGTH_LONG).show();

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED){

            //GCM
            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    SharedPreferences sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(context);
                    boolean sentToken = sharedPreferences
                            .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                    Context mContext = getApplicationContext();
                    if (sentToken) {

                        Toast.makeText(mContext, "Conectado ao GCM", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(mContext, "Não Conectado ao GCM", Toast.LENGTH_LONG).show();
                    }
                }
            };

            // Registering BroadcastReceiver
            registerReceiver();

            if (checkPlayServices()) {
                // Start IntentService to register this application with GCM.

                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }

            //Fim GCM

            //Billing

            String base64EncodedPublicKey =
                    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmj8KhsfNWLzsE7HBLgxY8CRDiYPLLABEwk/RPX+S2fGg2brtUv1jitAZT0cjZR4lzzua4GSWk6Qt3/R1AT8rdBDBTiyivobhfli7w3YMICvnZItJoXk/JtLyvAdcJQA4UKjQfL6/lCowHDBm1sI46RFZ3pOlyqqNnkxE7qOwtIMetbm9btxIly88kMFy0YCLAJQ80X+6tsi/4Pg6cSoNfesR2XOAgF6ym0zWCWyiki9lm/+zcueX8HeyiOCxzNa78OYvHOr9MyKweEJ+nAP/WmFy9AVRu3qeCSDGN4NNMgfTTa7RVlHykwFZoVT+2c6jCLPHTW5JBDFw93IhTZQrZwIDAQAB";

            mHelper = new IabHelper(this, base64EncodedPublicKey);

            mHelper.startSetup(new
                                       IabHelper.OnIabSetupFinishedListener() {
                                           public void onIabSetupFinished(IabResult result)
                                           {
                                               if (!result.isSuccess()) {
                                                   Log.d("Billing", "In-app Billing setup failed: " +
                                                           result);
                                               } else {
                                                   Log.d("Billing", "In-app Billing is set up OK");
                                               }
                                           }
                                       });

            //

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            FacebookSdk.sdkInitialize(getApplicationContext());
            setContentView(R.layout.activity_maps);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            TwitterAuthConfig twauthConfig =  new TwitterAuthConfig("UQiGX8wtCVEPotL14yOKiWow4", "HtmZhHscXZEyFSY2GfRlXXFTiQAgm87Tu5Lfu4XvggdIxaDIBI");
            Fabric.with(this, new TwitterCore(twauthConfig), new TweetComposer());
            TwitterLoginButton loginButton = (TwitterLoginButton) findViewById(R.id.twbutton);
            loginButton.setCallback(new Callback<TwitterSession>() {
                    @Override
                    public void success(Result<TwitterSession> result) {
                        // The TwitterSession is also available through:
                        // Twitter.getInstance().core.getSessionManager().getActiveSession()
                        twsession = result.data;
                        String msg = "@" + twsession.getUserName() + " logado! (#" + twsession.getUserId() + ")";
                        FrameLayout fl = (FrameLayout) findViewById(R.id.flTwitter);
                        fl.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        // Do something on failure
                        Log.d("TwitterKit", "Login with Twitter failure", exception);
                    }
                });


            markerPoints = new ArrayList<LatLng>();

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            mAdView = (AdView) findViewById(R.id.adView);
            AdRequest.Builder adRequest = new AdRequest.Builder().addTestDevice("C2D5F07C1522DBFE7DD42FE0FAE70832");
            AdRequest aaa=adRequest.build();
            //AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(aaa);

            //Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);

            //////Codigo FriendList/////
            funcListaAmigos();
            //////Fim Codigo FriendList//////

        } else {
            showAlert("Permissões Desabilitadas", "Por favor ative as permissões do aplicativo em seu aparelho.");
        }

    }

    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        //Modo de Transporte
        String modo = "mode=transit";
        String tipotransporte="alternatives=true";
        String lingua="language=pt-BR";

        // Waypoints
        String waypoints = "";
        for(int i=2;i<markerPoints.size();i++){
            LatLng point  = (LatLng) markerPoints.get(i);
            if(i==2)
                waypoints = "waypoints=";
            waypoints += point.latitude + "," + point.longitude + "|";
        }

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+waypoints+"&"+modo+"&"+tipotransporte+"&"+lingua;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException{
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

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
                Log.i("URL",line);
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
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();
            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

            ParserTrechos parserTrechos = new ParserTrechos();
            parserTrechos.execute(result);
        }
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
                routes = parser.parse(jObject);

                JSONObject jObject2;
                jObject2 = new JSONObject(jsonData[0]);
                JSONArray jRoutes2 = null;
                JSONArray jLegs2 = null;
                jRoutes2 = jObject2.getJSONArray("routes");
                jLegs2 = ((JSONObject) jRoutes2.get(0)).getJSONArray("legs");
                Duracao = (String) ((JSONObject) ((JSONObject) jLegs2.get(0)).get("duration")).get("text");
                Distancia = (String) ((JSONObject) ((JSONObject) jLegs2.get(0)).get("distance")).get("text");
                enderecoS = (String) (((JSONObject) jLegs2.get(0)).get("start_address"));
                enderecoD = (String) (((JSONObject) jLegs2.get(0)).get("end_address"));
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }
        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {


            // Traversing through all the routes
            if (result!=null) {
                for (int i = 0; i < result.size(); i++) {
                    ArrayList<LatLng> points = null;
                    points = new ArrayList<LatLng>();

                    // Fetching i-th route
                    List<HashMap<String, String>> path = result.get(i);
                    String data = "";
                        try {
                            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(Mycoord.latitude, Mycoord.longitude, 1);
                            String cidade = addresses.get(0).getLocality();
                            String pais = addresses.get(0).getCountryCode();
                            cidade = URLEncoder.encode(cidade, "UTF-8");
                            pais = URLEncoder.encode(pais, "UTF-8");
                            // Fetching the data from web service
                            data = downloadUrl("http://www.wazu.com.br/webservice/getalertas.php?cidade=" + cidade + "&estado=" + pais);
                            Log.i("JSON", data);
                            JSONObject respAlerta = new JSONObject(data);
                            JSONArray arr, arr2;
                            arr = respAlerta.getJSONArray("dados");
                            String id, tipo_alerta, hora, comentario, t_comentario_usuario, t_usuario, t_comentario, endereco;


                            // Fetching all the points in i-th route
                            int cor=0;
                                for (int j = 0; j < path.size(); j++) {
                                    HashMap<String, String> point = path.get(j);
                                    if (point.get("lat").contains("Cab.SRota")) {
                                        if (points.size()>0) {
                                            PolylineOptions lineOptions = null;
                                            lineOptions = new PolylineOptions();
                                            cor++;
                                            if (cor == 1) {
                                                lineOptions.color(Color.BLUE);
                                                lineOptions.width(16);
                                            } else if (cor == 2) {
                                                lineOptions.color(Color.MAGENTA);
                                                lineOptions.width(15);
                                            } else if (cor == 3) {
                                                lineOptions.color(Color.YELLOW);
                                                lineOptions.width(14);
                                            } else if (cor == 4) {
                                                lineOptions.color(Color.CYAN);
                                                lineOptions.width(13);
                                            } else if (cor == 5) {
                                                lineOptions.color(Color.GRAY);
                                                lineOptions.width(12);
                                            } else if (cor == 6) {
                                                lineOptions.color(Color.GREEN);
                                                lineOptions.width(11);
                                            } else {
                                                lineOptions.color(Color.LTGRAY);
                                                lineOptions.width(10);
                                            }
                                            lineOptions.addAll(points);
                                            mMap.addPolyline(lineOptions);
                                            points.clear();
                                        }

                                    } else {
                                        double lat = Double.parseDouble(point.get("lat"));
                                        double lng = Double.parseDouble(point.get("lng"));
                                        double distancia = 0.0;
                                        double Alat = 0.0;
                                        double Alng = 0.0;
                                        LatLng position = new LatLng(lat, lng);

                                        //Marca Alerta Trecho
                                        if (j < path.size() - 1) {
                                            try {
                                                for (int z = 0; z < arr.length(); z++) {
                                                    JSONObject oneByOne = arr.getJSONObject(z);
                                                    Location locationA = new Location("");
                                                    Location locationB = new Location("");
                                                    Alat = Double.valueOf(oneByOne.getString("lat"));
                                                    Alng = Double.valueOf(oneByOne.getString("lng"));
                                                    String tem_coment = oneByOne.getString("tem_coment");
                                                    locationA.setLatitude(Alat);
                                                    locationA.setLongitude(Alng);
                                                    locationB.setLatitude(lat);
                                                    locationB.setLongitude(lng);

                                                    distancia = locationB.distanceTo(locationA);

                                                    if (distancia < 50) {
                                                        id = oneByOne.getString("id");
                                                        tipo_alerta = oneByOne.getString("id_tipo_alerta");
                                                        hora = oneByOne.getString("hora");
                                                        comentario = hora + ": " + oneByOne.opt("comentario").toString();
                                                        if (tipo_alerta.contains("1")) {
                                                            tipo_alerta = "Acidente";
                                                        } else if (tipo_alerta.contains("2")) {
                                                            tipo_alerta = "Assalto";
                                                        } else if (tipo_alerta.contains("3")) {
                                                            tipo_alerta = "Congestionamento";
                                                        } else if (tipo_alerta.contains("4")) {
                                                            tipo_alerta = "Obras";
                                                        } else if (tipo_alerta.contains("5")) {
                                                            tipo_alerta = "Polícia";
                                                        } else if (tipo_alerta.contains("6")) {
                                                            tipo_alerta = "Ônibus";
                                                        }

                                                        comentario = hora + ": " + oneByOne.opt("comentario").toString();
                                                        if (!tem_coment.contains("0")) {
                                                            String data2 = downloadUrl("http://www.wazu.com.br/webservice/getcomentarioalerta.php?id_alerta=" + id);
                                                            JSONObject respAlerta2 = new JSONObject(data2);
                                                            arr2 = respAlerta2.getJSONArray("dados");
                                                            comentario = comentario + "\n\nComentários deste Alerta:\n";
                                                            t_comentario = "";
                                                            for (int x = 0; x < arr2.length(); x++) {
                                                                JSONObject tcoment = arr2.getJSONObject(x);
                                                                t_comentario_usuario = tcoment.getString("comentario");
                                                                t_usuario = tcoment.getString("nome");
                                                                t_comentario = t_comentario + t_usuario + ": " + t_comentario_usuario + "\n";
                                                            }
                                                            comentario = comentario + t_comentario;
                                                        }
                                                        funcMarcaAlerta(Alat, Alng, tipo_alerta, comentario, id, false, false);
                                                    }
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            //Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                                            List<Address> addressesD = geocoder.getFromLocation(lat, lng, 1);
                                            endereco = addressesD.get(0).getAddressLine(0).toString();
                                            mMap.addMarker(new MarkerOptions()
                                                    .position(destino)
                                                    .title(endereco)
                                                    .snippet("Tempo de chegada: " + Duracao + "\nAlarme em " + String.valueOf(distanciaprefs) + "m antes do destino.")
                                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcadordestino))
                                            );
                                            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                                                @Override
                                                public View getInfoWindow(Marker arg0) {
                                                    return null;
                                                }

                                                @Override
                                                public View getInfoContents(Marker marker) {
                                                    LinearLayout info = new LinearLayout(getApplicationContext());
                                                    info.setOrientation(LinearLayout.VERTICAL);

                                                    TextView title = new TextView(getApplicationContext());
                                                    title.setTextColor(Color.BLACK);
                                                    title.setGravity(Gravity.CENTER);
                                                    title.setTypeface(null, Typeface.BOLD);
                                                    title.setText(marker.getTitle());

                                                    TextView snippet = new TextView(getApplicationContext());
                                                    snippet.setTextColor(Color.GRAY);
                                                    snippet.setText(marker.getSnippet());

                                                    info.addView(title);
                                                    info.addView(snippet);

                                                    return info;
                                                }
                                            });
                                            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                                @Override
                                                public void onInfoWindowClick(Marker marker) {
                                                    RelativeLayout rlfav = (RelativeLayout) findViewById(R.id.rlFavDestino);
                                                    rlfav.setVisibility(View.VISIBLE);
                                                }
                                            });
                                        }
                                        points.add(position);
                                    }
                                }
                            PolylineOptions lineOptions = null;
                            lineOptions = new PolylineOptions();
                            lineOptions.width(12);
                            lineOptions.color(Color.RED);
                            lineOptions.addAll(points);
                            mMap.addPolyline(lineOptions);

                        } catch (Exception e) {
                            Log.d("Background Task", e.toString());
                        }
                }
            }else{
                Context mContext = getApplicationContext();
                Toast.makeText(mContext, "Não foram encontradas rotas para o destino informado. Por favor tente novamente.", Toast.LENGTH_LONG).show();
            }
    }
        //End New Code
    }

    /** A class to parse the Google Places in JSON format */
    private class ParserTrechos extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject3;
            List<List<HashMap<String, String>>> trechos = null;

            try{
                jObject3 = new JSONObject(jsonData[0]);
                RoutesJSONParser parser = new RoutesJSONParser();
                trechos = parser.parse(jObject3);
            }catch(Exception e){
                e.printStackTrace();
            }
            return trechos;
        }
        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            List<String> arrayOfGRoute;
            HashMap<String, List<String>> arrayOfRoute;

            arrayOfGRoute = new ArrayList<String>();
            arrayOfRoute = new HashMap<String, List<String>>();

                List<HashMap<String, String>> path = result.get(0);
                int z = 0;
                mAlertas = new ArrayList<LatLng>();
                int indice = 0;
                List<String> childMap;
                List<String> childMap1;
                List<String> childMap2;
                List<String> childMap3;

                childMap = new ArrayList<String>();
                childMap1 = new ArrayList<String>();
                childMap2 = new ArrayList<String>();
                childMap3 = new ArrayList<String>();

                String GTjson = "";
                String Tjson = "";
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);
                    String rduration = point.get("rdescricao");
                    String rdistance = point.get("rdistance");
                    String jLat = "";
                    String jLng = "";
                    String eLat = "";
                    String eLng = "";


                    try {
                        JSONObject jObject = new JSONObject(point.get("rdistance"));
                        JSONArray jRoutes;
                        jRoutes = jObject.getJSONArray("dados");
                        JSONObject data = jRoutes.getJSONObject(0);
                        jLat = data.optString("lat");
                        jLng = data.optString("lng");
                        if (point.get("rdistance").contains("cabecalho")) {
                            if (indice>0) {
                                if (indice==1) {
                                    arrayOfRoute.put(GTjson, childMap);
                                } else if (indice==2) {
                                    arrayOfRoute.put(GTjson, childMap1);
                                } else if (indice==3) {
                                    arrayOfRoute.put(GTjson, childMap2);
                                } else if (indice==4) {
                                    arrayOfRoute.put(GTjson, childMap3);
                                }
                            }
                            GTjson = "Rota "+String.valueOf(indice+1)+". \n"+point.get("rdescricao");
                            arrayOfGRoute.add(GTjson);
                            indice++;
                        } else if (point.get("rdistance").contains("cabwalking")) {
                            Tjson = "{\"dados\" : [{ \"icone\" : \"HWALKING\",\"lat\" : \""+jLat+"\",\"lng\" : \""+jLng+"\",\"elat\" : \""+jLat+"\",\"elng\" : \""+jLng+"\",\"descricao\" : \""+point.get("rdescricao")+"\" }]}";
                            if (indice==1) {
                                childMap.add(Tjson);
                            } else if (indice==2) {
                                childMap1.add(Tjson);
                            } else if (indice==3) {
                                childMap2.add(Tjson);
                            } else if (indice==4) {
                                childMap3.add(Tjson);
                            }
                        } else if (point.get("rdistance").contains("cabtransit")) {
                            eLat = data.optString("elat");
                            eLng = data.optString("elng");
                            mAlertas.add(z, new LatLng(Double.valueOf(eLat), Double.valueOf(eLng)));
                            z = z + 1;
                            Tjson = "{\"dados\" : [{ \"icone\" : \"HTRANSIT\",\"lat\" : \""+jLat+"\",\"lng\" : \""+jLng+"\",\"elat\" : \""+eLat+"\",\"elng\" : \""+eLng+"\",\"descricao\" : \""+point.get("rdescricao")+"\" }]}";
                            if (indice==1) {
                                childMap.add(Tjson);
                            } else if (indice==2) {
                                childMap1.add(Tjson);
                            } else if (indice==3) {
                                childMap2.add(Tjson);
                            } else if (indice==4) {
                                childMap3.add(Tjson);
                            }
                        } else if (point.get("rdistance").contains("cabmetro")) {
                            eLat = data.optString("elat");
                            eLng = data.optString("elng");
                            mAlertas.add(z, new LatLng(Double.valueOf(eLat), Double.valueOf(eLng)));
                            z = z + 1;
                            Tjson = "{\"dados\" : [{ \"icone\" : \"HSUBWAY\",\"lat\" : \""+jLat+"\",\"lng\" : \""+jLng+"\",\"elat\" : \""+eLat+"\",\"elng\" : \""+eLng+"\",\"descricao\" : \""+point.get("rdescricao")+"\" }]}";
                            if (indice==1) {
                                childMap.add(Tjson);
                            } else if (indice==2) {
                                childMap1.add(Tjson);
                            } else if (indice==3) {
                                childMap2.add(Tjson);
                            } else if (indice==4) {
                                childMap3.add(Tjson);
                            }
                        } else if (point.get("rdistance").contains("cabtrem")) {
                            eLat = data.optString("elat");
                            eLng = data.optString("elng");
                            mAlertas.add(z, new LatLng(Double.valueOf(eLat), Double.valueOf(eLng)));
                            z = z + 1;
                            Tjson = "{\"dados\" : [{ \"icone\" : \"HTRAIN\",\"lat\" : \""+jLat+"\",\"lng\" : \""+jLng+"\",\"elat\" : \""+eLat+"\",\"elng\" : \""+eLng+"\",\"descricao\" : \""+point.get("rdescricao")+"\" }]}";
                            if (indice==1) {
                                childMap.add(Tjson);
                            } else if (indice==2) {
                                childMap1.add(Tjson);
                            } else if (indice==3) {
                                childMap2.add(Tjson);
                            } else if (indice==4) {
                                childMap3.add(Tjson);
                            }
                        } else if (point.get("rdistance").contains("coordt")) {
                            Tjson = "{\"dados\" : [{ \"icone\" : \"TRANSIT\",\"lat\" : \""+jLat+"\",\"lng\" : \""+jLng+"\",\"elat\" : \""+jLat+"\",\"elng\" : \""+jLng+"\",\"descricao\" : \""+point.get("rdescricao")+"\" }]}";
                            if (indice==1) {
                                childMap.add(Tjson);
                            } else if (indice==2) {
                                childMap1.add(Tjson);
                            } else if (indice==3) {
                                childMap2.add(Tjson);
                            } else if (indice==4) {
                                childMap3.add(Tjson);
                            }
                        } else if (point.get("rdistance").contains("coordm")) {
                            Tjson = "{\"dados\" : [{ \"icone\" : \"SUBWAY\",\"lat\" : \""+jLat+"\",\"lng\" : \""+jLng+"\",\"elat\" : \""+jLat+"\",\"elng\" : \""+jLng+"\",\"descricao\" : \""+point.get("rdescricao")+"\" }]}";
                            if (indice==1) {
                                childMap.add(Tjson);
                            } else if (indice==2) {
                                childMap1.add(Tjson);
                            } else if (indice==3) {
                                childMap2.add(Tjson);
                            } else if (indice==4) {
                                childMap3.add(Tjson);
                            }
                        } else if (point.get("rdistance").contains("coordTrem")) {
                            Tjson = "{\"dados\" : [{ \"icone\" : \"TRAIN\",\"lat\" : \""+jLat+"\",\"lng\" : \""+jLng+"\",\"elat\" : \""+jLat+"\",\"elng\" : \""+jLng+"\",\"descricao\" : \""+point.get("rdescricao")+"\" }]}";
                            if (indice==1) {
                                childMap.add(Tjson);
                            } else if (indice==2) {
                                childMap1.add(Tjson);
                            } else if (indice==3) {
                                childMap2.add(Tjson);
                            } else if (indice==4) {
                                childMap3.add(Tjson);
                            }
                        } else if (point.get("rdistance").contains("coordw")){
                            Tjson = "{\"dados\" : [{ \"icone\" : \"WALKING\",\"lat\" : \""+jLat+"\",\"lng\" : \""+jLng+"\",\"elat\" : \""+jLat+"\",\"elng\" : \""+jLng+"\",\"descricao\" : \""+point.get("rdescricao")+"\" }]}";
                            if (indice==1) {
                                childMap.add(Tjson);
                            } else if (indice==2) {
                                childMap1.add(Tjson);
                            } else if (indice==3) {
                                childMap2.add(Tjson);
                            } else if (indice==4) {
                                childMap3.add(Tjson);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                tAlertas = mAlertas;
                arrayOfRoute.put(GTjson, childMap3);
            final RouteList adapterR = new RouteList(getApplicationContext(), arrayOfGRoute, arrayOfRoute);
            ExpandableListView LVRoute = (ExpandableListView) findViewById(R.id.Routelist);
            LVRoute.setVisibility(View.VISIBLE);
            LVRoute.setAdapter(adapterR);

            LVRoute.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {

                    Object arrayR;
                    arrayR = adapterR.getChild(groupPosition, childPosition);
                    try {
                        LatLng Coord;
                        JSONObject jObject = new JSONObject(arrayR.toString());
                        JSONArray jRoutes;
                        jRoutes = jObject.getJSONArray("dados");
                        JSONObject data = jRoutes.getJSONObject(0);
                        Double RLat = Double.valueOf(data.optString("lat"));
                        Double RLng = Double.valueOf(data.optString("lng"));
                        parent.setVisibility(View.INVISIBLE);
                        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlTitListas);
                        rl.setVisibility(View.INVISIBLE);
                        Coord = new LatLng(RLat, RLng);
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Coord, 16));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            });


        }
        //End New Code
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
            Location location = null;

            LocationManager lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {

            mMap = googleMap;
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            location = lManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                location = lManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if (location != null) {
                Double lati1 = location.getLatitude();
                Double long1 = location.getLongitude();
                Mycoord = new LatLng(lati1, long1);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Mycoord, 16));
                Context mContext = getApplicationContext();
                Toast.makeText(mContext, "Localização Confirmada!", Toast.LENGTH_LONG).show();
            }else{
                Context mContext = getApplicationContext();
                Toast.makeText(mContext, "Localização Ainda Não Confirmada!", Toast.LENGTH_LONG).show();
            }
            DbHelper dbHelper = new DbHelper(this);
            List<Preferencias> prefs = dbHelper.selectPreferencias();
            for (Iterator iterator = prefs.iterator(); iterator.hasNext(); ) {
                Preferencias iprefs = (Preferencias) iterator.next();
                if (iprefs.getTrafego()==1){
                    mMap.setTrafficEnabled(true);
                }else{
                    mMap.setTrafficEnabled(false);
                }
                distanciaprefs = (double) iprefs.getDistancia();

                if (iprefs.getDultlat() != 0 && iprefs.getDultlng() != 0) {
                    locationD = new Location("");
                    locationS = new Location("");
                    locationD.setLatitude(iprefs.getDultlat());
                    locationD.setLongitude(iprefs.getDultlng());
                    locationS.setLatitude(iprefs.getSultlat());
                    locationS.setLongitude(iprefs.getSultlng());

                    destino = new LatLng(iprefs.getDultlat(), iprefs.getDultlng());
                    Mycoord  = new LatLng(iprefs.getSultlat(), iprefs.getSultlng());
                    if (destino != null && Mycoord != null) {
                        LatLng dest = destino;
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(destino, 11));
                        String url = getDirectionsUrl(Mycoord, dest);
                        DownloadTask downloadTask = new DownloadTask();
                        downloadTask.execute(url);
                        distanciaprefs = (double) iprefs.getDistancia();
                        if (Duracao==null){
                            Duracao="";
                        }
                        /*mMap.addMarker(new MarkerOptions()
                                .position(destino)
                                .title("Destino")
                                .snippet("Alarme em "+String.valueOf(distanciaprefs)+"m antes do destino.")
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcadordestino))
                        );
                        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                            @Override
                            public View getInfoWindow(Marker arg0) {
                                return null;
                            }

                            @Override
                            public View getInfoContents(Marker marker) {
                                LinearLayout info = new LinearLayout(getApplicationContext());
                                info.setOrientation(LinearLayout.VERTICAL);

                                TextView title = new TextView(getApplicationContext());
                                title.setTextColor(Color.BLACK);
                                title.setGravity(Gravity.CENTER);
                                title.setTypeface(null, Typeface.BOLD);
                                title.setText(marker.getTitle());

                                TextView snippet = new TextView(getApplicationContext());
                                snippet.setTextColor(Color.GRAY);
                                snippet.setText(marker.getSnippet());

                                info.addView(title);
                                info.addView(snippet);

                                return info;
                            }
                        });
                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                RelativeLayout rlfav = (RelativeLayout) findViewById(R.id.rlFavDestino);
                                rlfav.setVisibility(View.VISIBLE);
                            }
                        });*/
                    }
                }
            }

            mMap.setOnMapClickListener(this);
            mMap.setOnMapLongClickListener(this);
            mMap.getUiSettings().setZoomControlsEnabled(true);

            LocationListener lListener = new LocationListener() {

                    public void onLocationChanged(Location locat) {

                        if (!conectado) {
                            conectado = true;
                            Context mContext = getApplicationContext();
                            Toast.makeText(mContext, "Sistema de Movimento Funcionando!", Toast.LENGTH_LONG).show();
                        }

                        if (locationD != null) {
                            precisao = distanciaprefs;
                            if (!alarmeativado) {
                                if (!isBetterLocation(locat, bestloc)) {
                                    locat = bestloc;
                                    Mycoord = new LatLng(bestloc.getLatitude(),bestloc.getLongitude());
                                } else {
                                    bestloc = locat;
                                    Mycoord = new LatLng(locat.getLatitude(),locat.getLongitude());
                                }
                                if (destino != null) {
                                    double distance = locat.distanceTo(locationD);
                                    if (!isAlarme) {
                                        if (mAlertas != null) {
                                            if (!mAlertas.isEmpty()) {
                                                for (int i = 0; i < mAlertas.size(); i++) {
                                                    LatLng point = (LatLng) mAlertas.get(i);
                                                    Location pdistance;
                                                    pdistance = new Location("");
                                                    pdistance.setLatitude(point.latitude);
                                                    pdistance.setLongitude(point.longitude);
                                                    double dponto = locat.distanceTo(pdistance);
                                                    Log.i("Dponto","Estou Aqui");
                                                    if (dponto <= precisao) {
                                                        LigaAlarme();
                                                    }
                                                }
                                            } else {
                                                mAlertas = null;
                                            }
                                        } else {
                                            if (distance <= precisao) {
                                                LigaAlarme();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    public void onProviderEnabled(String provider) {

                    }

                    public void onProviderDisabled(String provider) {
                    }
                };
                if (lManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    lManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, lListener);
                } else if (lManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    lManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, lListener);
                } else {
                    showAlert("Permissões Desabilitadas", "Por favor ative as permissões do aplicativo em seu aparelho.");
                }

            }else {
                showAlert("Permissões Desabilitadas", "Por favor ative as permissões do aplicativo em seu aparelho.");
            }
    }

    public void funcParar() {

        int trafego = 0;
        int som = 0;
        int vibra = 0;

        float distancia = 0;

        DbHelper dbHelper = new DbHelper(this);

        List<Preferencias> xprefs = dbHelper.selectPreferencias();
        for (Iterator iterator = xprefs.iterator(); iterator.hasNext(); ) {
            Preferencias iprefs = (Preferencias) iterator.next();
            trafego = iprefs.getTrafego();
            distancia = iprefs.getDistancia();
            som = iprefs.getSom();
            vibra = iprefs.getVibra();
        }

        if (alarmeativado) {
            alarmeativado = false;
            isAlarme = false;
            if (som==1) {
                mp.stop();
            }
            if (vibra==1) {
                Vibrar(0);
            }
        }
        EditText localizacao = (EditText)findViewById(R.id.textEndereco);
        localizacao.setText("");
        ExpandableListView el = (ExpandableListView) findViewById(R.id.Routelist);
        el.setAdapter((BaseExpandableListAdapter)null);
        locationD = null;
        locationS = null;
        destino=null;
        mMap.clear();

        Preferencias prefs = new Preferencias();
        prefs.setDultlat(0);
        prefs.setDultlng(0);
        prefs.setSultlat(0);
        prefs.setSultlng(0);
        prefs.setTrafego(trafego);
        prefs.setDistancia(distancia);
        prefs.setSom(som);
        prefs.setVibra(vibra);
        dbHelper.updatePrefs(prefs);

    }

    public void LigaAlarme(){



        try {
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alert == null) {
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                if (alert == null) {
                    alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                }
            }
            DbHelper dbHelper = new DbHelper(this);
            List<Preferencias> prefs = dbHelper.selectPreferencias();
            int Som = 0;
            int Vibra = 0;
            for (Iterator iterator = prefs.iterator(); iterator.hasNext(); ) {
                Preferencias iprefs = (Preferencias) iterator.next();
                Som = iprefs.getSom();
                Vibra = iprefs.getVibra();
            }
            if (Som==1) {
                mp = new MediaPlayer();
                mp.setAudioStreamType(AudioManager.STREAM_ALARM);
                mp.setDataSource(MapsActivity.this, alert);
                mp.setLooping(true);
                mp.prepare();
                mp.start();
            }
            if (Vibra==1){
                Vibrar(1);
            }
            alarmeativado = true;
            isAlarme = true;
            ImageButton bt = (ImageButton) findViewById(R.id.imgParar);
            bt.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void PararAlarme(View view) {
        Location location = null;
        LocationManager lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        ImageButton bt = (ImageButton) findViewById(R.id.imgParar);
        Switch swsom = (Switch) findViewById(R.id.swSom);
        Switch swvibra = (Switch) findViewById(R.id.swVibra);
        bt.setVisibility(View.INVISIBLE);
        if (swsom.isChecked()) {
            mp.stop();
        }
        if (swvibra.isChecked()) {
            Vibrar(0);
        }
        alarmeativado = false;
        isAlarme = false;
        location = lManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            location = lManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (mAlertas!=null) {
            for (int i = 0; i < mAlertas.size(); i++) {
                LatLng point = (LatLng) mAlertas.get(i);
                Location pdistance;
                pdistance = new Location("");
                pdistance.setLatitude(point.latitude);
                pdistance.setLongitude(point.longitude);
                double dponto = location.distanceTo(pdistance);
                if (dponto <= precisao) {
                    mAlertas.remove(i);
                }
            }
        }else{
            if (swsom.isChecked()) {
                mp.stop();
            }
            if (swvibra.isChecked()) {
                Vibrar(0);
            }
            isAlarme = false;
            funcParar();
            Context mContext = getApplicationContext();
            Toast.makeText(mContext, "Você Chegou Ao Seu Destino!", Toast.LENGTH_LONG).show();
        }
    }

    public void funcGostei (View view){
        TextView t = (TextView) findViewById(R.id.edtComentarioAlerta);
        String gostei = t.getText().toString();
        t.setText(gostei+"Obrigado pela ajuda \uD83D\uDE03");

    }

    public void funcNaoGostei (View view){
        TextView t = (TextView) findViewById(R.id.edtComentarioAlerta);
        String gostei = t.getText().toString();
        t.setText(gostei+"Alerta falso.  \uD83D\uDE1E");

    }

    public void funcBuscar(View view)     {
        EditText localizacao = (EditText)findViewById(R.id.textEndereco);
        String locationb = localizacao.getText().toString();
        List<Address> addressList = null;
        if(locationb != null || !locationb.equals(""))
        {
            Geocoder geocoder = new Geocoder(this,Locale.getDefault());
            try {
                addressList = geocoder.getFromLocationName(locationb, 1);
                if (!addressList.isEmpty()) {
                    mMap.clear();
                    Address address = addressList.get(0);
                    destino = new LatLng(address.getLatitude(), address.getLongitude());
                    final RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlFavDestino);
                    final TextView clng = (TextView) findViewById(R.id.txtFavDLng);
                    final TextView clat = (TextView) findViewById(R.id.txtFavDLat);
                    final EditText cnome = (EditText) findViewById(R.id.edtFavDNomeDestino);
                    final TextView ctit = (TextView) findViewById(R.id.txtTitDFavorito);
                    clng.setText(String.valueOf(address.getLongitude()));
                    clat.setText(String.valueOf(address.getLatitude()));
                    cnome.setHint("Digite o nome do destino favorito");
                    ctit.setText("Adicionar Destino Favoritos:");
                    DbHelper dbHelper = new DbHelper(this);
                    List<Preferencias> prefs = dbHelper.selectPreferencias();
                    for (Iterator iterator = prefs.iterator(); iterator.hasNext(); ) {
                        Preferencias iprefs = (Preferencias) iterator.next();
                        distanciaprefs = (double) iprefs.getDistancia();
                    }

                    mMap.addMarker(new MarkerOptions()
                            .position(destino)
                            .title(locationb)
                            .snippet("Alarme em " + String.valueOf(distanciaprefs) + "m antes do destino.")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcadordestino))

                    );
                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                        @Override
                        public View getInfoWindow(Marker arg0) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {
                            LinearLayout info = new LinearLayout(getApplicationContext());
                            info.setOrientation(LinearLayout.VERTICAL);

                            TextView title = new TextView(getApplicationContext());
                            title.setTextColor(Color.BLACK);
                            title.setGravity(Gravity.CENTER);
                            title.setTypeface(null, Typeface.BOLD);
                            title.setText(marker.getTitle());

                            TextView snippet = new TextView(getApplicationContext());
                            snippet.setTextColor(Color.GRAY);
                            snippet.setText(marker.getSnippet());

                            info.addView(title);
                            info.addView(snippet);

                            return info;
                        }
                    });
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            rl.setVisibility(View.VISIBLE);
                        }
                    });

                    LatLng origin = Mycoord;
                    if (Mycoord != null) {
                        LatLng dest = destino;
                        String url = getDirectionsUrl(origin, dest);
                        Context mContext = getApplicationContext();
                        Toast.makeText(mContext, "Buscando Rotas!", Toast.LENGTH_LONG).show();
                        DownloadTask downloadTask = new DownloadTask();
                        downloadTask.execute(url);
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(destino));
                    } else {
                        Context mContext = getApplicationContext();
                        Toast.makeText(mContext, "Sua Localização Ainda Não Foi Confirmada!", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Context mContext = getApplicationContext();
                    Toast.makeText(mContext, "Endereço Não Encontrado!", Toast.LENGTH_LONG).show();
                }
                RelativeLayout busca = (RelativeLayout) findViewById(R.id.rlBuscar);
                busca.setVisibility(View.INVISIBLE);
            } catch (IOException e) {
                Context mContext = getApplicationContext();
                Toast.makeText(mContext, "Demora na Conexão com o Servidor!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }


    }

    @Override
    public void onMapClick(LatLng point) {
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlDetails);
        RelativeLayout rl2 = (RelativeLayout) findViewById(R.id.rlPrefs);
        RelativeLayout rl3 = (RelativeLayout) findViewById(R.id.rlDivulgue);
        ListView rl4 = (ListView) findViewById(R.id.Alertaslist);
        RelativeLayout rl5 = (RelativeLayout) findViewById(R.id.rlComentario);
        RelativeLayout rl6 = (RelativeLayout) findViewById(R.id.rlFavOnibus);
        RelativeLayout rl7 = (RelativeLayout) findViewById(R.id.rlFavDestino);
        ListView rl8 = (ListView) findViewById(R.id.FavOnibuslist);
        ListView rl9 = (ListView) findViewById(R.id.FavLocallist);
        RelativeLayout rl10 = (RelativeLayout) findViewById(R.id.rlTermos);
        RelativeLayout rl11 = (RelativeLayout) findViewById(R.id.rlAlertar);
        RelativeLayout rl12 = (RelativeLayout) findViewById(R.id.rlInfOnibus);
        RelativeLayout rl13 = (RelativeLayout) findViewById(R.id.rlInfCheckin);
        RelativeLayout rl14 = (RelativeLayout) findViewById(R.id.rlVerAlertas);
        ListView rl15 = (ListView) findViewById(R.id.friendlist);
        RelativeLayout rl17 = (RelativeLayout) findViewById(R.id.rlComentarioAlerta);
        ListView rl18 = (ListView) findViewById(R.id.Routelist);
        RelativeLayout rl19 = (RelativeLayout) findViewById(R.id.rlVideoTour);
        RelativeLayout rl20 = (RelativeLayout) findViewById(R.id.rlTitListas);


        rl.setVisibility(View.INVISIBLE);
        rl2.setVisibility(View.INVISIBLE);
        rl3.setVisibility(View.INVISIBLE);
        rl4.setVisibility(View.INVISIBLE);
        rl5.setVisibility(View.INVISIBLE);
        rl6.setVisibility(View.INVISIBLE);
        rl7.setVisibility(View.INVISIBLE);
        rl8.setVisibility(View.INVISIBLE);
        rl9.setVisibility(View.INVISIBLE);
        rl10.setVisibility(View.INVISIBLE);
        rl11.setVisibility(View.INVISIBLE);
        rl12.setVisibility(View.INVISIBLE);
        rl13.setVisibility(View.INVISIBLE);
        rl14.setVisibility(View.INVISIBLE);
        rl15.setVisibility(View.INVISIBLE);
        rl17.setVisibility(View.INVISIBLE);
        rl18.setVisibility(View.INVISIBLE);
        rl19.setVisibility(View.INVISIBLE);
        rl20.setVisibility(View.INVISIBLE);
        mMap.animateCamera(CameraUpdateFactory.newLatLng(point));
    }

    @Override
    public void onMapLongClick(LatLng point) {
        TraçaMapa(point,true);
    }

    public void TraçaMapa(LatLng point, boolean exibemarcador){

        destino = point;
        Log.i("TraçaMapa","Entrei");
        Context mContext = getApplicationContext();
        Toast.makeText(mContext, "Buscando Rotas!", Toast.LENGTH_LONG).show();

        mMap.animateCamera(CameraUpdateFactory.newLatLng(point));

        if (destino != null) {

            mMap.clear();
            locationD = new Location("");
            locationD.setLatitude(destino.latitude);
            locationD.setLongitude(destino.longitude);
            final RelativeLayout rlfav = (RelativeLayout) findViewById(R.id.rlFavDestino);
            final TextView clng = (TextView) findViewById(R.id.txtFavDLng);
            final TextView clat = (TextView) findViewById(R.id.txtFavDLat);
            final EditText cnome = (EditText) findViewById(R.id.edtFavDNomeDestino);
            final TextView ctit = (TextView) findViewById(R.id.txtTitDFavorito);
            clng.setText(String.valueOf(destino.longitude));
            clat.setText(String.valueOf(destino.latitude));
            cnome.setHint("Digite o nome do destino favorito");
            ctit.setText("Adicionar Destino Favoritos:");
            DbHelper dbHelper = new DbHelper(this);
            String endereco ="";
            try {
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(destino.latitude, destino.longitude, 1);
                endereco = addresses.get(0).getAddressLine(0).toString();
            }catch (IOException e){
                e.printStackTrace();
            }

            if (exibemarcador) {
                List<Preferencias> prefs = dbHelper.selectPreferencias();
                for (Iterator iterator = prefs.iterator(); iterator.hasNext(); ) {
                    Preferencias iprefs = (Preferencias) iterator.next();
                    distanciaprefs = (double) iprefs.getDistancia();
                }

                mMap.addMarker(new MarkerOptions()
                        .position(destino)
                        .title(endereco)
                        .snippet("Alarme em "+String.valueOf(distanciaprefs)+"m antes do destino.")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcadordestino))

                );
                mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                    @Override
                    public View getInfoWindow(Marker arg0) {
                        return null;
                    }

                    @Override
                    public View getInfoContents(Marker marker) {
                        LinearLayout info = new LinearLayout(getApplicationContext());
                        info.setOrientation(LinearLayout.VERTICAL);

                        TextView title = new TextView(getApplicationContext());
                        title.setTextColor(Color.BLACK);
                        title.setGravity(Gravity.CENTER);
                        title.setTypeface(null, Typeface.BOLD);
                        title.setText(marker.getTitle());

                        TextView snippet = new TextView(getApplicationContext());
                        snippet.setTextColor(Color.GRAY);
                        snippet.setText(marker.getSnippet());

                        info.addView(title);
                        info.addView(snippet);

                        return info;
                    }
                });
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        rlfav.setVisibility(View.VISIBLE);
                    }
                });
            }
            if (Mycoord!=null) {
                LatLng origin = Mycoord;
                LatLng dest = point;
                locationS = new Location("");
                locationS.setLatitude(Mycoord.latitude);
                locationS.setLongitude(Mycoord.longitude);
                String url = getDirectionsUrl(origin, dest);
                DownloadTask downloadTask = new DownloadTask();
                downloadTask.execute(url);
            }else{
                //Context mContext = getApplicationContext();
                Toast.makeText(mContext, "Sua Localização Ainda Não Foi Confirmada!", Toast.LENGTH_LONG).show();
            }

        }
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }

    /** Checks whether two providers are the same */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }

    private void showAlert(String titulo, String msg) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(titulo)
                .setMessage(msg)
                .setPositiveButton("Location Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        finish();
                    }
                });
        dialog.show();
    }

    public void funcRotas(View view) {
        ExpandableListView rota = (ExpandableListView) findViewById(R.id.Routelist);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlTitListas);
        TextView text = (TextView) findViewById(R.id.txtTitListas);
        if (rl.getVisibility()==View.INVISIBLE) {
            rl.setVisibility(View.VISIBLE);
            rota.setVisibility(View.VISIBLE);
            int qrotas = 0;
            try {
                qrotas = rota.getExpandableListAdapter().getGroupCount();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
            if (qrotas>0) {
                text.setText("Foram encontradas " + qrotas + " rotas. Escolha uma:");
            }else{
                text.setText("Sem rotas para exibir.");
            }
        } else{
            rl.setVisibility(View.INVISIBLE);
            rota.setVisibility(View.INVISIBLE);
            text.setText("");
        }
    }

    public void funcAlertar(View view) {
        RelativeLayout alertar = (RelativeLayout) findViewById(R.id.rlAlertar);
        if (alertar.getVisibility()==View.INVISIBLE) {
            alertar.setVisibility(View.VISIBLE);
        } else{
            alertar.setVisibility(View.INVISIBLE);
        }
    }

    public void funcInformar(View view) {
        RelativeLayout onibus = (RelativeLayout) findViewById(R.id.rlInfOnibus);
        if (onibus.getVisibility()==View.INVISIBLE) {
            onibus.setVisibility(View.VISIBLE);
        } else{
            onibus.setVisibility(View.INVISIBLE);
        }
    }

    public void funcVerAlertas(View view) {
        RelativeLayout alertar = (RelativeLayout) findViewById(R.id.rlVerAlertas);
        if (alertar.getVisibility()==View.INVISIBLE) {
            alertar.setVisibility(View.VISIBLE);
        } else{
            alertar.setVisibility(View.INVISIBLE);
        }
    }

    public void funcAlertaPolicia(View view) {
        RelativeLayout alertar = (RelativeLayout) findViewById(R.id.rlComentario);
        RelativeLayout janalertar = (RelativeLayout) findViewById(R.id.rlAlertar);
        EditText comentario = (EditText) findViewById(R.id.edtComentario);
        TextView titulo = (TextView) findViewById(R.id.txtTitComentario);
        TextView tipo = (TextView) findViewById(R.id.txtTipoAlertaComentario);
        ImageButton check = (ImageButton) findViewById(R.id.btnSalvarAlerta);
        if (alertar.getVisibility()==View.INVISIBLE) {
            titulo.setText("Criar Alerta de Polícia");
            comentario.setHint("Digite informações sobre o Alerta de Policia ou deixe o campo em branco apenas para informar a localização.");
            tipo.setText("5");
            alertar.setVisibility(View.VISIBLE);
            janalertar.setVisibility(View.INVISIBLE);
        }
    }

    public void funcVerAlertaPolicia (View view) {
        funcGetAlertas("5",null);
        RelativeLayout alertar = (RelativeLayout) findViewById(R.id.rlVerAlertas);
        alertar.setVisibility(View.INVISIBLE);
    }

    public void funcAlertaAcidente(View view) {
        RelativeLayout alertar = (RelativeLayout) findViewById(R.id.rlComentario);
        RelativeLayout janalertar = (RelativeLayout) findViewById(R.id.rlAlertar);
        EditText comentario = (EditText) findViewById(R.id.edtComentario);
        TextView titulo = (TextView) findViewById(R.id.txtTitComentario);
        TextView tipo = (TextView) findViewById(R.id.txtTipoAlertaComentario);
        ImageButton check = (ImageButton) findViewById(R.id.btnSalvarAlerta);
        if (alertar.getVisibility()==View.INVISIBLE) {
            titulo.setText("Criar Alerta de Acidente");
            comentario.setHint("Digite informações sobre o Alerta de Acidente ou deixe o campo em branco apenas para informar a localização.");
            tipo.setText("1");
            alertar.setVisibility(View.VISIBLE);
            janalertar.setVisibility(View.INVISIBLE);
        }
    }

    public void funcVerAlertaAcidente(View view) {
        funcGetAlertas("1",null);
        RelativeLayout alertar = (RelativeLayout) findViewById(R.id.rlVerAlertas);
        alertar.setVisibility(View.INVISIBLE);
    }

    public void funcAlertaAssalto(View view) {
        RelativeLayout alertar = (RelativeLayout) findViewById(R.id.rlComentario);
        RelativeLayout janalertar = (RelativeLayout) findViewById(R.id.rlAlertar);
        EditText comentario = (EditText) findViewById(R.id.edtComentario);
        TextView titulo = (TextView) findViewById(R.id.txtTitComentario);
        TextView tipo = (TextView) findViewById(R.id.txtTipoAlertaComentario);
        ImageButton check = (ImageButton) findViewById(R.id.btnSalvarAlerta);
        if (alertar.getVisibility()==View.INVISIBLE) {
            titulo.setText("Criar Alerta de Assalto");
            comentario.setHint("Digite informações sobre o Alerta de Assalto ou deixe o campo em branco apenas para informar a localização.");
            tipo.setText("2");
            alertar.setVisibility(View.VISIBLE);
            janalertar.setVisibility(View.INVISIBLE);
        }
    }

    public void funcVerAlertaAssalto(View view) {
        funcGetAlertas("2",null);
        RelativeLayout alertar = (RelativeLayout) findViewById(R.id.rlVerAlertas);
        alertar.setVisibility(View.INVISIBLE);
    }

    public void funcAlertaCongestionamento(View view) {
        RelativeLayout alertar = (RelativeLayout) findViewById(R.id.rlComentario);
        RelativeLayout janalertar = (RelativeLayout) findViewById(R.id.rlAlertar);
        EditText comentario = (EditText) findViewById(R.id.edtComentario);
        TextView titulo = (TextView) findViewById(R.id.txtTitComentario);
        TextView tipo = (TextView) findViewById(R.id.txtTipoAlertaComentario);
        ImageButton check = (ImageButton) findViewById(R.id.btnSalvarAlerta);
        if (alertar.getVisibility()==View.INVISIBLE) {
            titulo.setText("Criar Alerta de Congestionamento");
            comentario.setHint("Digite informações sobre o Alerta de Congestionamento ou deixe o campo em branco apenas para informar a localização.");
            tipo.setText("3");
            alertar.setVisibility(View.VISIBLE);
            janalertar.setVisibility(View.INVISIBLE);
        }
    }

    public void funcVerAlertaCongestionamento(View view) {
        funcGetAlertas("3",null);
        RelativeLayout alertar = (RelativeLayout) findViewById(R.id.rlVerAlertas);
        alertar.setVisibility(View.INVISIBLE);
    }

    public void funcAlertaPonto(View view) {
        RelativeLayout alertar = (RelativeLayout) findViewById(R.id.rlComentario);
        RelativeLayout janalertar = (RelativeLayout) findViewById(R.id.rlAlertar);
        EditText comentario = (EditText) findViewById(R.id.edtComentario);
        TextView titulo = (TextView) findViewById(R.id.txtTitComentario);
        TextView tipo = (TextView) findViewById(R.id.txtTipoAlertaComentario);
        ImageButton check = (ImageButton) findViewById(R.id.btnSalvarAlerta);
        if (alertar.getVisibility()==View.INVISIBLE) {
            titulo.setText("Criar Alerta de Ponto de Ônibus");
            comentario.setHint("Digite informações sobre o Alerta de Ponto de Ônibus ou deixe o campo em branco apenas para informar a localização.");
            tipo.setText("6");
            alertar.setVisibility(View.VISIBLE);
            janalertar.setVisibility(View.INVISIBLE);
        }
    }

    public void funcVerAlertaPonto(View view) {
        funcGetAlertas("6",null);
        RelativeLayout alertar = (RelativeLayout) findViewById(R.id.rlVerAlertas);
        alertar.setVisibility(View.INVISIBLE);
    }

    public void funcAlertaObras(View view) {
        RelativeLayout alertar = (RelativeLayout) findViewById(R.id.rlComentario);
        RelativeLayout janalertar = (RelativeLayout) findViewById(R.id.rlAlertar);
        EditText comentario = (EditText) findViewById(R.id.edtComentario);
        TextView titulo = (TextView) findViewById(R.id.txtTitComentario);
        TextView tipo = (TextView) findViewById(R.id.txtTipoAlertaComentario);
        ImageButton check = (ImageButton) findViewById(R.id.btnSalvarAlerta);
        if (alertar.getVisibility()==View.INVISIBLE) {
            titulo.setText("Criar Alerta de Obras");
            comentario.setHint("Digite informações sobre o Alerta de Obras ou deixe o campo em branco apenas para informar a localização.");
            tipo.setText("4");
            alertar.setVisibility(View.VISIBLE);
            janalertar.setVisibility(View.INVISIBLE);
        }
    }

    public void funcInfPublico(View view) {
        RelativeLayout alertar = (RelativeLayout) findViewById(R.id.rlInfCheckin);
        RelativeLayout janalertar = (RelativeLayout) findViewById(R.id.rlInfOnibus);
        EditText linha = (EditText) findViewById(R.id.edtLinha);
        EditText nome = (EditText) findViewById(R.id.edtNomeLinha);
        TextView titulo = (TextView) findViewById(R.id.txtTitInformar);
        TextView tipo = (TextView) findViewById(R.id.txtTipoCheckin);
        if (alertar.getVisibility()==View.INVISIBLE) {
            titulo.setText("Informar ônibus");
            linha.setHint("Digite o número da linha.");
            nome.setHint("Digite o nome da linha.");
            tipo.setText("1");
            alertar.setVisibility(View.VISIBLE);
            janalertar.setVisibility(View.INVISIBLE);
        }
    }

    public void funcInfCondominio(View view) {
        RelativeLayout alertar = (RelativeLayout) findViewById(R.id.rlInfCheckin);
        RelativeLayout janalertar = (RelativeLayout) findViewById(R.id.rlInfOnibus);
        EditText linha = (EditText) findViewById(R.id.edtLinha);
        EditText nome = (EditText) findViewById(R.id.edtNomeLinha);
        TextView titulo = (TextView) findViewById(R.id.txtTitInformar);
        TextView tipo = (TextView) findViewById(R.id.txtTipoCheckin);
        if (alertar.getVisibility()==View.INVISIBLE) {
            titulo.setText("Informar ônibus");
            linha.setHint("Digite o número da linha.");
            nome.setHint("Digite o nome da linha.");
            tipo.setText("2");
            alertar.setVisibility(View.VISIBLE);
            janalertar.setVisibility(View.INVISIBLE);
        }
    }

    public void funcInfEspecial(View view) {
        RelativeLayout alertar = (RelativeLayout) findViewById(R.id.rlInfCheckin);
        RelativeLayout janalertar = (RelativeLayout) findViewById(R.id.rlInfOnibus);
        EditText linha = (EditText) findViewById(R.id.edtLinha);
        EditText nome = (EditText) findViewById(R.id.edtNomeLinha);
        TextView titulo = (TextView) findViewById(R.id.txtTitInformar);
        TextView tipo = (TextView) findViewById(R.id.txtTipoCheckin);
        if (alertar.getVisibility()==View.INVISIBLE) {
            titulo.setText("Informar ônibus");
            linha.setHint("Digite o número da linha.");
            nome.setHint("Digite o nome da linha.");
            tipo.setText("3");
            alertar.setVisibility(View.VISIBLE);
            janalertar.setVisibility(View.INVISIBLE);
        }
    }

    public void funcVerAlertaObras(View view) {
        funcGetAlertas("4",null);
        RelativeLayout alertar = (RelativeLayout) findViewById(R.id.rlVerAlertas);
        alertar.setVisibility(View.INVISIBLE);
    }

    public void funcSalvarAlerta(View view) {
        RelativeLayout alertar = (RelativeLayout) findViewById(R.id.rlComentario);
        TextView tipo = (TextView) findViewById(R.id.txtTipoAlertaComentario);
        EditText coment = (EditText) findViewById(R.id.edtComentario);
        String tipo_alerta = tipo.getText().toString();
        String comentario = coment.getText().toString();
        insertBDAlerta(tipo_alerta,comentario);
        alertar.setVisibility(View.INVISIBLE);
    }

    public void funcSalvarComentarioAlerta(View view) {
        RelativeLayout alertar = (RelativeLayout) findViewById(R.id.rlComentarioAlerta);
        TextView Rid = (TextView) findViewById(R.id.txtIdAlertaComentario);
        EditText coment = (EditText) findViewById(R.id.edtComentarioAlerta);
        String rid = Rid.getText().toString();
        String comentario = coment.getText().toString();
        insertBDAlertaComentario(rid,comentario);
        alertar.setVisibility(View.INVISIBLE);
    }

    public void funcSalvarInformar(View view) {
        RelativeLayout informar = (RelativeLayout) findViewById(R.id.rlInfCheckin);
        TextView tipo = (TextView) findViewById(R.id.txtTipoCheckin);
        EditText linha = (EditText) findViewById(R.id.edtLinha);
        EditText nome = (EditText) findViewById(R.id.edtNomeLinha);
        String tipo_linha = tipo.getText().toString();
        String ilinha = linha.getText().toString();
        String inome = nome.getText().toString();
        insertBDCheckin(tipo_linha,ilinha,inome);
        informar.setVisibility(View.INVISIBLE);
        ImageButton bt = (ImageButton) findViewById(R.id.imgObrigado);
        bt.setVisibility(View.VISIBLE);
    }

    public void funcSalvarInfoMarcadorDestino(View view) {
        RelativeLayout informar = (RelativeLayout) findViewById(R.id.rlFavDestino);
        TextView lat = (TextView) findViewById(R.id.txtFavDLat);
        TextView lng = (TextView) findViewById(R.id.txtFavDLng);
        EditText nome = (EditText) findViewById(R.id.edtFavDNomeDestino);
        Double ilat = Double.valueOf(lat.getText().toString());
        Double ilng = Double.valueOf(lng.getText().toString());
        String inome = nome.getText().toString();
        inome=URLDecoder.decode(inome);
        insertFavoritoDestino(inome,ilat,ilng);
        informar.setVisibility(View.INVISIBLE);
    }

    public void funcSalvarInfoMarcadorOnibus(View view) {
        RelativeLayout informar = (RelativeLayout) findViewById(R.id.rlFavOnibus);
        TextView numero = (TextView) findViewById(R.id.txtFavOLinha);
        TextView linha = (TextView) findViewById(R.id.txtFavONomeLinha);
        TextView tipo = (TextView) findViewById(R.id.txtFavTipoLinha);
        String inumero = numero.getText().toString();
        String ilinha = linha.getText().toString();
        String itipo = tipo.getText().toString();
        ilinha=URLDecoder.decode(ilinha);
        inumero=URLDecoder.decode(inumero);
        insertFavoritoOnibus(itipo,ilinha,inumero);
        informar.setVisibility(View.INVISIBLE);
    }

    public void funcMostrarBusca(View view) {
        RelativeLayout busca = (RelativeLayout) findViewById(R.id.rlBuscar);
        funcGetLinhas();
        if (busca.getVisibility()==View.INVISIBLE) {
            busca.setVisibility(View.VISIBLE);
        } else{
            busca.setVisibility(View.INVISIBLE);
        }
    }

    public void PararObrigado(View view) {
        ImageButton bt = (ImageButton) findViewById(R.id.imgObrigado);
        bt.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void funcConfig(View view) {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    public void funcMarcaLinha(View view){
        AutoCompleteTextView Linha
                = (AutoCompleteTextView)findViewById(
                R.id.textEndereco);

        funcVerLinhas(Linha.getText().toString());
        RelativeLayout busca = (RelativeLayout) findViewById(R.id.rlBuscar);
        busca.setVisibility(View.INVISIBLE);
    }

    public void funcGravarPrefs(View view) {

        Double Dultlat = 0.0;
        Double Dultlng = 0.0;
        Double Sultlat = 0.0;
        Double Sultlng = 0.0;

        Switch trafego = (Switch) findViewById(R.id.swTrafego);
        Switch som = (Switch) findViewById(R.id.swSom);
        Switch vibra = (Switch) findViewById(R.id.swVibra);

        EditText distancia = (EditText) findViewById(R.id.edtAlarme);

        Preferencias prefs = new Preferencias();
        DbHelper dbHelper = new DbHelper(this);

        List<Preferencias> xprefs = dbHelper.selectPreferencias();
        for (Iterator iterator = xprefs.iterator(); iterator.hasNext(); ) {
            Preferencias iprefs = (Preferencias) iterator.next();
            Dultlat = iprefs.getDultlat();
            Dultlng = iprefs.getDultlng();
            Sultlat = iprefs.getSultlat();
            Sultlng = iprefs.getSultlng();
        }

        if (trafego.isChecked()) {
            prefs.setTrafego(1);
            mMap.setTrafficEnabled(true);
        }else{
            prefs.setTrafego(0);
            mMap.setTrafficEnabled(false);
        }
        if (som.isChecked()) {
            prefs.setSom(1);
        }else{
            prefs.setSom(0);
        }
        if (vibra.isChecked()) {
            prefs.setVibra(1);
        }else{
            prefs.setVibra(0);
        }

        Float fdist = Float.parseFloat(distancia.getText().toString());
        prefs.setDistancia(fdist);
        distanciaprefs = Double.valueOf(fdist);
        prefs.setDultlat(Dultlat);
        prefs.setDultlng(Dultlng);
        prefs.setSultlat(Sultlat);
        prefs.setSultlng(Sultlng);
        dbHelper.updatePrefs(prefs);

        Context mContext = getApplicationContext();
        Toast.makeText(mContext, "Dados Gravados!", Toast.LENGTH_LONG).show();


    }

    public void insertFavoritoDestino(String nome, Double lat, Double lng) {

        FavLocal fav = new FavLocal();

        fav.setNome(nome);
        fav.setLat(lat);
        fav.setLng(lng);
        DbHelper dbHelper = new DbHelper(this);
        dbHelper.insertFavLocal(fav);

        RelativeLayout rl =(RelativeLayout) findViewById(R.id.rlFavDestino);
        rl.setVisibility(View.INVISIBLE);

        CarregaFavLocal();

        Context mContext = getApplicationContext();
        Toast.makeText(mContext, "Favorito Adicionado!", Toast.LENGTH_LONG).show();

    }

    public void insertFavoritoOnibus(String tipo, String linha, String numero) {

        FavOnibus fav = new FavOnibus();

        fav.setNumero(numero);
        fav.setLinha(linha);
        fav.setTipo(tipo);
        DbHelper dbHelper = new DbHelper(this);
        dbHelper.insertFavOnibus(fav);

        RelativeLayout rl =(RelativeLayout) findViewById(R.id.rlFavOnibus);
        rl.setVisibility(View.INVISIBLE);

        CarregaFavOnibus();

        Context mContext = getApplicationContext();
        Toast.makeText(mContext, "Favorito Adicionado!", Toast.LENGTH_LONG).show();

    }

    public void funcFriendList(View view){

        ListView listView = (ListView) findViewById(R.id.friendlist);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlTitListas);
        TextView txt = (TextView) findViewById(R.id.txtTitListas);
        if (listView.getVisibility()==View.INVISIBLE) {
            listView.setVisibility(View.VISIBLE);
            rl.setVisibility(View.VISIBLE);
            txt.setText("Lista de Amigos");
        } else{
            listView.setVisibility(View.INVISIBLE);
            rl.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menu_onibus) {
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlTitListas);
            ExpandableListView el = (ExpandableListView) findViewById(R.id.Routelist);
            TextView txt = (TextView) findViewById(R.id.txtTitListas);
            ListView listView = (ListView) findViewById(R.id.FavOnibuslist);
            if (listView.getVisibility()==View.INVISIBLE) {
                listView.setVisibility(View.VISIBLE);
                rl.setVisibility(View.VISIBLE);
                el.setVisibility(View.INVISIBLE);
                txt.setText("Lista de Linhas Favoritas");

                CarregaFavOnibus();
            } else{
                listView.setVisibility(View.INVISIBLE);
                rl.setVisibility(View.INVISIBLE);

            }
        } else if (id == R.id.menu_favoritos) {
            ListView listView = (ListView) findViewById(R.id.FavLocallist);
            ExpandableListView el = (ExpandableListView) findViewById(R.id.Routelist);
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlTitListas);
            TextView txt = (TextView) findViewById(R.id.txtTitListas);

            if (listView.getVisibility()==View.INVISIBLE) {
                listView.setVisibility(View.VISIBLE);
                el.setVisibility(View.INVISIBLE);
                rl.setVisibility(View.VISIBLE);
                txt.setText("Lista de Locais Favoritos");
                CarregaFavLocal();
            } else{
                listView.setVisibility(View.INVISIBLE);
                rl.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.menu_divulgue) {
            Divulgue();
        } else if (id == R.id.menu_prefs) {
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlPrefs);
            if (rl.getVisibility() == View.INVISIBLE) {
                rl.setVisibility(View.VISIBLE);
            } else {
                rl.setVisibility(View.INVISIBLE);
            }
        } else if (id == R.id.menu_conta) {
            RegistroConta();

        } else if (id == R.id.menu_logs) {

        } else if (id == R.id.menu_termos) {
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlTermos);
            if (rl.getVisibility() == View.INVISIBLE) {
                rl.setVisibility(View.VISIBLE);
            } else {
                rl.setVisibility(View.INVISIBLE);
            }
        } else if (id == R.id.menu_retire) {
            IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
                    = new IabHelper.OnIabPurchaseFinishedListener() {
                public void onIabPurchaseFinished(IabResult result, Purchase purchase)
                {
                    if (result.isFailure()) {
                        Log.d("Billing", "Error purchasing: " + result);
                        return;
                    }
                    else if (purchase.getSku().equals("assanual001")) {
                        // consume the gas and update the UI
                        String versao = "assanual001";
                        String fid = "";
                        String fnome = "";
                        String femail = "";
                        DbHelper dbHelper = new DbHelper(getApplicationContext());
                        Cadastro cad = new Cadastro();
                        List<Cadastro> xcad = dbHelper.selectCadastro();
                        for (Iterator iterator = xcad.iterator(); iterator.hasNext(); ) {
                            Cadastro icad = (Cadastro) iterator.next();
                            fid = (String) icad.getId();
                            fnome = (String) icad.getNome();
                            femail = (String) icad.getEmail();
                            versao = (String) icad.getVersao();
                        }
                        cad.setId(fid);
                        cad.setNome(fnome);
                        cad.setEmail(femail);
                        cad.setVersao(versao);
                        dbHelper.updateCad(cad);
                    }
                }
            };
            DbHelper dbHelper = new DbHelper(getApplicationContext());
            Cadastro cad = new Cadastro();
            List<Cadastro> xcad = dbHelper.selectCadastro();
            String sid="";
            for (Iterator iterator = xcad.iterator(); iterator.hasNext(); ) {
                Cadastro icad = (Cadastro) iterator.next();
                sid = (String) icad.getId();
            }
            try {
                mHelper.launchPurchaseFlow(this, "assanual001", 10001, mPurchaseFinishedListener, sid);
            }catch (IabAsyncInProgressException e) {
                e.printStackTrace();
            }


        } else if (id == R.id.menu_conquistas) {
            try {
                startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient),
                        REQUEST_ACHIEVEMENTS);
            }catch (IllegalStateException e){
                Context mContext = getApplicationContext();
                Toast.makeText(mContext, "Você precisa se conectar ao Google Play!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else if (id == R.id.menu_video_guiado) {
            //set the media controller buttons
            if (mediaController == null) {
                mediaController = new MediaController(this);
            }
            //initialize the VideoView
            RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlVideoTour);
            RelativeLayout rlP = (RelativeLayout) findViewById(R.id.rlPrincipal);
            myVideoView = (VideoView) findViewById(R.id.vwVideoTour);
            myVideoView.setMediaController(mediaController);
            rl.setVisibility(View.VISIBLE);
            rlP.setVisibility(View.INVISIBLE);
            Context mContext = getApplicationContext();
            Toast.makeText(mContext, "Coloque seu device na horizontal!", Toast.LENGTH_LONG).show();

            try {
                    //set the uri of the video to be played
                myVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.wazuvideotour));
                myVideoView.start();
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            myVideoView.requestFocus();

            myVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
            {
                public void onCompletion(MediaPlayer mp)
                {
                    // Do whatever u need to do here
                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlVideoTour);
                    RelativeLayout rlP = (RelativeLayout) findViewById(R.id.rlPrincipal);
                    rl.setVisibility(View.INVISIBLE);
                    rlP.setVisibility(View.VISIBLE);
                }
            });

        }else if (id == R.id.menu_limpar) {
            funcParar();
        }else if (id == R.id.menu_meusalertas) {
            DbHelper dbHelper = new DbHelper(getApplicationContext());
            Cadastro cad = new Cadastro();
            List<Cadastro> xcad = dbHelper.selectCadastro();
            String fid="";
            for (Iterator iterator = xcad.iterator(); iterator.hasNext(); ) {
                Cadastro icad = (Cadastro) iterator.next();
                fid = (String) icad.getId();
            }
            funcGetAlertas(null,fid);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        Fragment fragment = getFragmentManager().findFragmentById(R.id.map);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == RC_SIGN_IN) {
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                // Bring up an error dialog to alert the user that sign-in
                // failed. The R.string.signin_failure should reference an error
                // string in your strings.xml file that tells the user they
                // could not be signed in, such as "Unable to sign in."
                BaseGameUtils.showActivityResultError(this,
                        requestCode, resultCode, R.string.signin_failure);
            }
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        if (isService) {
            stopService(new Intent(MapsActivity.this, LocationService.class));
            isService = false;
        }
        Log.i("LOCALGPS","Resumiu");
        try {
            mAdView.resume();
        } catch (NullPointerException e){
            e.printStackTrace();
        }

        DbHelper dbHelper = new DbHelper(this);
        Switch trafego = (Switch) findViewById(R.id.swTrafego);
        Switch som = (Switch) findViewById(R.id.swSom);
        Switch vibra = (Switch) findViewById(R.id.swVibra);
        EditText distancia = (EditText) findViewById(R.id.edtAlarme);

        List<FavLocal> favlocal = dbHelper.selectFavLocais();
        for (Iterator iterator = favlocal.iterator(); iterator.hasNext();) {
            FavLocal favLocal = (FavLocal) iterator.next();
        }
        List<Preferencias> prefs = dbHelper.selectPreferencias();
        for (Iterator iterator = prefs.iterator(); iterator.hasNext(); ) {
            Preferencias iprefs = (Preferencias) iterator.next();
            try {
                distancia.setText(String.valueOf(iprefs.getDistancia()));
                if (iprefs.getTrafego() == 1) {
                    trafego.setChecked(true);
                } else {
                    trafego.setChecked(false);
                }
                if (iprefs.getSom() == 1) {
                    som.setChecked(true);
                } else {
                    som.setChecked(false);
                }
                if (iprefs.getVibra() == 1) {
                    vibra.setChecked(true);
                } else {
                    vibra.setChecked(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        registerReceiver();
    }

    protected void onPause(){
        super.onPause();

        AsyncTask taskD = new DownloadTask().execute();
        taskD.cancel(true);

        AsyncTask taskP = new ParserTask().execute();
        taskP.cancel(true);

        AsyncTask taskT = new ParserTrechos().execute();
        taskT.cancel(true);

        Log.i("LOCALGPS","Pausou");
        try {
            mAdView.pause();
        }catch (NullPointerException e){
            e.printStackTrace();
        }

        int trafego = 0;
        float distancia = 0;

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        isReceiverRegistered = false;

        /*if (locationD!=null) {
            Log.i("LOCALGPS","PausouBD");
            DbHelper dbHelper = new DbHelper(this);

            List<Preferencias> xprefs = dbHelper.selectPreferencias();
            for (Iterator iterator = xprefs.iterator(); iterator.hasNext(); ) {
                Preferencias iprefs = (Preferencias) iterator.next();
                trafego = iprefs.getTrafego();
                distancia = iprefs.getDistancia();
            }

            Preferencias prefs = new Preferencias();
            prefs.setDultlat(locationD.getLatitude());
            prefs.setDultlng(locationD.getLongitude());
            prefs.setSultlat(locationS.getLatitude());
            prefs.setSultlng(locationS.getLongitude());

            prefs.setTrafego(trafego);
            prefs.setDistancia(distancia);
            dbHelper.updatePrefs(prefs);
        }*/
        if (!isService) {
            if (destino!=null) {
                //stopService(new Intent(MapsActivity.this, LocationService.class));
                startService(new Intent(MapsActivity.this, LocationService.class));
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.setFlags(Intent.FLAG_DEBUG_LOG_RESOLUTION);
                startActivity(startMain);
                isService = true;
                Context mContext = getApplicationContext();
                Toast.makeText(mContext, "Alarmes Wazu Ativados!", Toast.LENGTH_LONG).show();

            }
        }
    }

    protected void onDestroy(){
        super.onDestroy();
        stopService(new Intent(MapsActivity.this, LocationService.class));
        Context mContext = getApplicationContext();
        Toast.makeText(mContext, "Alarmes Wazu Desativados!", Toast.LENGTH_LONG).show();

        if (mHelper != null) {
            try {
                mHelper.dispose();
                mHelper = null;
            } catch (IabAsyncInProgressException e) {
                e.printStackTrace();
            }
        }
        if (mAdView != null) {
            mAdView.destroy();
        }

        int trafego = 0;
        int Som = 0;
        int Vibra = 0;
        float distancia = 0;

        if (locationD!=null) {
            DbHelper dbHelper = new DbHelper(this);

            List<Preferencias> xprefs = dbHelper.selectPreferencias();
            for (Iterator iterator = xprefs.iterator(); iterator.hasNext(); ) {
                Preferencias iprefs = (Preferencias) iterator.next();
                trafego = iprefs.getTrafego();
                distancia = iprefs.getDistancia();
                Som = iprefs.getSom();
                Vibra = iprefs.getVibra();
            }

            Preferencias prefs = new Preferencias();
            prefs.setDultlat(locationD.getLatitude());
            prefs.setDultlng(locationD.getLongitude());
            prefs.setSultlat(locationS.getLatitude());
            prefs.setSultlng(locationS.getLongitude());
            prefs.setTrafego(trafego);
            prefs.setDistancia(distancia);
            prefs.setVibra(Vibra);
            prefs.setSom(Som);
            dbHelper.updatePrefs(prefs);
        }
    }

    private void RegistroConta(){
        // Inicio Codigo Facebook
        final TextView tface = (TextView) findViewById(R.id.textFace);
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlDetails);
        LoginButton bface = (LoginButton) findViewById(R.id.btfacelogin);

        DbHelper dbHelper = new DbHelper(getApplicationContext());
        Cadastro cad = new Cadastro();
        List<Cadastro> xcad = dbHelper.selectCadastro();
        for (Iterator iterator = xcad.iterator(); iterator.hasNext(); ) {
            Cadastro icad = (Cadastro) iterator.next();
            String fid = (String) icad.getId();
            String fnome = (String) icad.getNome();
            String femail = (String) icad.getEmail();
            String versao = (String) icad.getVersao();
            if (fid!=null){
                if (AccessToken.getCurrentAccessToken()!=null) {
                    tface.setText("Logado Como:\n\n\nId: " + fid + "\n\nNome: " + fnome + "\n\nEmail: " + femail + "\n\nFacebook: Conectado.");
                }else {
                    tface.setText("Logado Como:\n\n\nId: " + fid + "\n\nNome: " + fnome + "\n\nEmail: " + femail + "\n\nFacebook: Ainda não conectado.");
                }
            }else{
                tface.setText("Você ainda não fez seu registro. Registre-se para ter acesso as localizações de outros membros e poder realizar check-ins e alertas.");
            }
        }

        if (rl.getVisibility() == View.INVISIBLE) {
            rl.setVisibility(View.VISIBLE);
        } else {
            rl.setVisibility(View.INVISIBLE);
        }

        callbackManager = CallbackManager.Factory.create();
        facebookPermitions = Arrays.asList("email", "public_profile", "user_friends");
        bface.setReadPermissions(facebookPermitions);

        bface.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Do whatever u want
                Context mContext = getApplicationContext();
                if(AccessToken.getCurrentAccessToken()!=null){
                    Toast.makeText(mContext, "Pedido de Desconexão Realizado.", Toast.LENGTH_LONG).show();
                    RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlDetails);
                    rl.setVisibility(View.INVISIBLE);
                }else{
                    Toast.makeText(mContext, "Pedido de Conexão Realizado.", Toast.LENGTH_LONG).show();
                }
            }
        });


        bface.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult arg0) {
                GraphRequest request = GraphRequest.newMeRequest(arg0.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        try {
                            String fid = (String) object.get("id");
                            String fnome = (String) object.get("name");
                            String femail = (String) object.get("email");
                            String versao = "gratuita";
                            String token = "111";
                            tface.setText("Logado Como:\n\n\nId: " + fid + "\n\nNome: " + fnome + "\n\nEmail: " + femail + "\n\nFacebook: Conectado.");
                            Context mContext = getApplicationContext();
                            Toast.makeText(mContext, "Conexão Realizada Com Sucesso!", Toast.LENGTH_LONG).show();

                            DbHelper dbHelper = new DbHelper(getApplicationContext());
                            Cadastro cad = new Cadastro();
                            List<Cadastro> xcad = dbHelper.selectCadastro();
                            for (Iterator iterator = xcad.iterator(); iterator.hasNext(); ) {
                                Cadastro icad = (Cadastro) iterator.next();
                                versao = icad.getVersao();
                                if (icad.getId() == null) {
                                    insertBDUsuario(fid, fnome, femail, versao, token);
                                }
                            }
                            cad.setId(fid);
                            cad.setNome(fnome);
                            cad.setEmail(femail);
                            cad.setVersao(versao);
                            dbHelper.updateCad(cad);
                            funcListaAmigos();
                        }catch (NullPointerException n){
                            n.printStackTrace();
                        } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields","id,name,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onError(FacebookException arg0) {
                Context mContext = getApplicationContext();
                Toast.makeText(mContext, "ERRO!", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Context mContext = getApplicationContext();
                Toast.makeText(mContext, "Ação Cancelada!", Toast.LENGTH_LONG).show();
            }
        });

        // Fim Codigo Facebook

    }

    private void Divulgue(){

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlDivulgue);
        if (rl.getVisibility() == View.INVISIBLE) {
            rl.setVisibility(View.VISIBLE);
        } else {
            rl.setVisibility(View.INVISIBLE);
        }
        FrameLayout lb = (FrameLayout) findViewById(R.id.flTwitter);
        FrameLayout imtw = (FrameLayout) findViewById(R.id.flTweet);
        TwitterSession session = Twitter.getSessionManager().getActiveSession();
        String Sessao = "";
        lb.setVisibility(View.INVISIBLE);
        imtw.setVisibility(View.VISIBLE);

        try{
            if (session.getAuthToken().token.isEmpty()) {
                Sessao = session.getAuthToken().token;
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        Log.i("Sessao",Sessao);

        /*if (Sessao==""){
            lb.setVisibility(View.VISIBLE);
            imtw.setVisibility(View.INVISIBLE);
        }else{
            lb.setVisibility(View.INVISIBLE);
            imtw.setVisibility(View.VISIBLE);
        }*/
        if (AccessToken.getCurrentAccessToken()!=null) {
            TextView txtView = (TextView) findViewById(R.id.textDivulgue);
            txtView.setText("Curta e Compartilhe nossa página no Facebook!");
            LikeView likeView = (LikeView) findViewById(R.id.likeView);
            likeView.setLikeViewStyle(LikeView.Style.STANDARD);
            likeView.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);
            likeView.setObjectIdAndType(
                    "https://www.facebook.com/wazuapp/",
                    LikeView.ObjectType.PAGE);
            ShareLinkContent content = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("https://www.facebook.com/wazuapp/"))
                    .setContentTitle("Wazu")
                    .build();
            ShareButton shareButton = (ShareButton) findViewById(R.id.ShareView);
            shareButton.setShareContent(content);
            callbackManager = CallbackManager.Factory.create();
            shareButton.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result arg0) {
                    Context mContext = getApplicationContext();
                    Toast.makeText(mContext, "Compartilhado!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(FacebookException arg0) {
                    Context mContext = getApplicationContext();
                    Toast.makeText(mContext, "Erro no Compartilhamento!", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCancel() {
                    Context mContext = getApplicationContext();
                    Toast.makeText(mContext, "Compartilhamento Cancelado!", Toast.LENGTH_LONG).show();
                }
            });
        }else{
            TextView txtView = (TextView) findViewById(R.id.textDivulgue);
            txtView.setText("Para Curtir e Compartilhar nossa página, você deve estar conectado no Facebook. Vá até seu registro de conta no menu lateral do App e conecte-se ao Facebook.");
        }

    }

    public void CarregaFavLocal(){
        ArrayList<FavLocal> arrayOfLocals = new ArrayList<FavLocal>();
        final LocalList adapterD = new LocalList(this, arrayOfLocals);
        ListView LVFavLocal = (ListView) findViewById(R.id.FavLocallist);
        LVFavLocal.setAdapter(adapterD);
        DbHelper dbHelperD = new DbHelper(this);
        List<FavLocal> favlocal = dbHelperD.selectFavLocais();
        for (Iterator iterator = favlocal.iterator(); iterator.hasNext();){
            FavLocal favLocal = (FavLocal) iterator.next();
            FavLocal newFavLocal = new FavLocal(favLocal.getId(),favLocal.getNome().toString(),favLocal.getLat(),favLocal.getLng());
            adapterD.add(newFavLocal);
        }
        LVFavLocal.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, final View v, int position, long id) {
                AlertDialog.Builder adb=new AlertDialog.Builder(v.getContext());
                adb.setTitle("O que você deseja fazer?");
                adb.setMessage("Traçar a rota até o destino favorito ou apagar o favorito da lista?");
                int positionToRemove = position;
                final String Rid = String.valueOf(adapterD.getItem(positionToRemove).getId());
                final String Title = String.valueOf(adapterD.getItem(positionToRemove).getNome());
                final Double Lat = adapterD.getItem(positionToRemove).getLat();
                final Double Lng = adapterD.getItem(positionToRemove).getLng();
                Log.i("ADB",Rid);
                adb.setNegativeButton("Traçar Rota", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        View vw = (View) v.getParent();
                        ListView lv = (ListView) vw.findViewById(R.id.FavLocallist);
                        lv.setVisibility(View.INVISIBLE);
                        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlTitListas);
                        rl.setVisibility(View.INVISIBLE);
                        Context mContext = v.getContext();
                        Toast.makeText(mContext, "Buscando Rotas!", Toast.LENGTH_LONG).show();
                        funcBtnMarcaLocal(Title,Lat,Lng);
                        LatLng point = new LatLng(Lat,Lng);
                        TraçaMapa(point,false);
                    }});
                adb.setPositiveButton("Apagar Favorito", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DbHelper dbHelper = new DbHelper(v.getContext());
                        dbHelper.deleteFavLocal(Rid);
                        View vw = (View) v.getParent();
                        ListView lv = (ListView) vw.findViewById(R.id.FavLocallist);
                        lv.setVisibility(View.INVISIBLE);
                        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlTitListas);
                        rl.setVisibility(View.INVISIBLE);
                        Context mContext = v.getContext();
                        Toast.makeText(mContext, "Favorito removido!", Toast.LENGTH_LONG).show();

                    }});
                adb.show();
            }
        });
    }

    public void CarregaFavOnibus(){
        ArrayList<FavOnibus> arrayOfOnibus = new ArrayList<FavOnibus>();
        final OnibusList adapterO = new OnibusList(this, arrayOfOnibus);
        ListView LVFavOnibus = (ListView) findViewById(R.id.FavOnibuslist);
        LVFavOnibus.setAdapter(adapterO);
        DbHelper dbHelperO = new DbHelper(this);
        List<FavOnibus> favonibus = dbHelperO.selectFavOnibus();
        for (Iterator iterator = favonibus.iterator(); iterator.hasNext();){
            FavOnibus favOnibus = (FavOnibus) iterator.next();
            FavOnibus newFavOnibus = new FavOnibus(favOnibus.getId(),favOnibus.getNumero().toString(),favOnibus.getLinha().toString(),favOnibus.getTipo().toString());
            adapterO.add(newFavOnibus);
        }
        LVFavOnibus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, final View v, int position, long id) {
                AlertDialog.Builder adb=new AlertDialog.Builder(v.getContext());
                adb.setTitle("O que você deseja fazer?");
                adb.setMessage("Buscar a linha favorita ou apagar o favorito da lista?");
                int positionToRemove = position;
                final String Rid = String.valueOf(adapterO.getItem(positionToRemove).getId());
                final String Linha = String.valueOf(adapterO.getItem(positionToRemove).getNumero());
                Log.i("Linha",Linha);
                adb.setNegativeButton("Buscar Linha", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        View vw = (View) v.getParent();
                        ListView lv = (ListView) vw.findViewById(R.id.FavOnibuslist);
                        lv.setVisibility(View.INVISIBLE);
                        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlTitListas);
                        rl.setVisibility(View.INVISIBLE);
                        Context mContext = v.getContext();
                        Toast.makeText(mContext, "Buscando Linhas!", Toast.LENGTH_LONG).show();
                        funcVerLinhas(Linha);
                    }});
                adb.setPositiveButton("Apagar Favorito", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DbHelper dbHelper = new DbHelper(v.getContext());
                        dbHelper.deleteFavOnibus(Rid);
                        View vw = (View) v.getParent();
                        ListView lv = (ListView) vw.findViewById(R.id.FavOnibuslist);
                        lv.setVisibility(View.INVISIBLE);
                        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlTitListas);
                        rl.setVisibility(View.INVISIBLE);
                        Context mContext = v.getContext();
                        Toast.makeText(mContext, "Favorito removido!", Toast.LENGTH_LONG).show();

                    }});
                adb.show();
            }
        });

    }

    private void insertBDUsuario(String... arg0){
                try{
                    String id = (String)arg0[0];
                    String nome = (String)arg0[1];
                    String email = (String)arg0[2];
                    String versao = (String)arg0[3];
                    String token = (String)arg0[4];

                    String link="http://www.wazu.com.br/webservice/usuarios.php?";
                    String data  = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
                    data += "&" + URLEncoder.encode("nome", "UTF-8") + "=" + URLEncoder.encode(nome, "UTF-8");
                    data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(email, "UTF-8");
                    data += "&" + URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(token, "UTF-8");
                    data += "&" + URLEncoder.encode("versao", "UTF-8") + "=" + URLEncoder.encode(versao, "UTF-8");

                    URL url = new URL(link+data);
                    url.openStream();
                    Games.Achievements.increment(mGoogleApiClient, "my_incremental_achievment_id", 1);
                }
                catch(Exception e){
                    e.printStackTrace();
                }
    }

    private void insertBDCheckin(String... arg0){
        try{
            Log.i("CHK","1");

            Location location = null;
            LocationManager lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            location = lManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                location = lManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if (location != null) {
                Double lat = location.getLatitude();
                Double lng = location.getLongitude();
                Timestamp hoje = new Timestamp(System.currentTimeMillis());

                String linha = (String) arg0[1];
                String ordem = (String) arg0[2];
                String tipo_linha = (String) arg0[0];
                String velocidade = "";
                String id_usuario = "";
                Geocoder geocoder = new Geocoder(this,Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(Mycoord.latitude,Mycoord.longitude,1);
                String cidade = addresses.get(0).getLocality();
                String estado = addresses.get(0).getCountryCode();
                DbHelper dbHelper = new DbHelper(getApplicationContext());
                Cadastro cad = new Cadastro();
                List<Cadastro> xcad = dbHelper.selectCadastro();
                for (Iterator iterator = xcad.iterator(); iterator.hasNext(); ) {
                    Cadastro icad = (Cadastro) iterator.next();
                    id_usuario = icad.getId();
                }
                if (!id_usuario.equals("")) {
                    String link = "http://www.wazu.com.br/webservice/checkin.php?";
                    String data = URLEncoder.encode("hoje", "UTF-8") + "=" + URLEncoder.encode(hoje.toString(), "UTF-8");
                    data += "&" + URLEncoder.encode("ordem", "UTF-8") + "=" + URLEncoder.encode(ordem, "UTF-8");
                    data += "&" + URLEncoder.encode("linha", "UTF-8") + "=" + URLEncoder.encode(linha, "UTF-8");
                    data += "&" + URLEncoder.encode("lat", "UTF-8") + "=" + URLEncoder.encode(lat.toString(), "UTF-8");
                    data += "&" + URLEncoder.encode("lng", "UTF-8") + "=" + URLEncoder.encode(lng.toString(), "UTF-8");
                    data += "&" + URLEncoder.encode("velocidade", "UTF-8") + "=" + URLEncoder.encode(velocidade, "UTF-8");
                    data += "&" + URLEncoder.encode("cidade", "UTF-8") + "=" + URLEncoder.encode(cidade, "UTF-8");
                    data += "&" + URLEncoder.encode("estado", "UTF-8") + "=" + URLEncoder.encode(estado, "UTF-8");
                    data += "&" + URLEncoder.encode("id_usuario", "UTF-8") + "=" + URLEncoder.encode(id_usuario, "UTF-8");
                    //data += "&" + URLEncoder.encode("nome", "UTF-8") + "=" + URLEncoder.encode(nome, "UTF-8");
                    data += "&" + URLEncoder.encode("tipo_linha", "UTF-8") + "=" + URLEncoder.encode(tipo_linha, "UTF-8");

                    URL url = new URL(link + data);
                    url.openStream();
                    Log.i("CHK", link + data);
                    Context mContext = getApplicationContext();
                    Toast.makeText(mContext, "Informação Enviada com Sucesso!", Toast.LENGTH_LONG).show();
                    Games.Achievements.increment(mGoogleApiClient, "my_incremental_achievment_id", 1);
                    Tweet("Acabei de informar a posição de uma linha no @wazuapp!");
                }else{
                    Context mContext = getApplicationContext();
                    Toast.makeText(mContext, "Login no Facebook Não Realizado!", Toast.LENGTH_LONG).show();
                }
            }else {
                Context mContext = getApplicationContext();
                Toast.makeText(mContext, "Informação Não Enviada. Tente Novamente!", Toast.LENGTH_LONG).show();
            }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void insertBDAlerta(String... arg0){
        try{
             Geocoder geocoder = new Geocoder(this,Locale.getDefault());
             List<Address> addresses = geocoder.getFromLocation(Mycoord.latitude,Mycoord.longitude,1);
             String cidade = addresses.get(0).getLocality();
             String pais = addresses.get(0).getCountryCode();
             cidade = URLEncoder.encode(cidade, "UTF-8");
             pais = URLEncoder.encode(pais, "UTF-8");

             DbHelper dbHelper = new DbHelper(getApplicationContext());
             Cadastro cad = new Cadastro();
             String fid=null;
             List<Cadastro> xcad = dbHelper.selectCadastro();
             for (Iterator iterator = xcad.iterator(); iterator.hasNext(); ) {
                    Cadastro icad = (Cadastro) iterator.next();
                    fid = (String) icad.getId();
             }
             if (fid!=null){
                Log.i("BDA", "2");
                 Location location = null;
                 LocationManager lManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                 location = lManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                 if (location == null) {
                     location = lManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                 }
                 if (location != null) {
                     Double lat = location.getLatitude();
                     Double lng = location.getLongitude();
                     Timestamp hoje = new Timestamp(System.currentTimeMillis());
                    String id_tipo_alerta = (String) arg0[0];
                    String comentario = (String) arg0[1];

                    String link = "http://www.wazu.com.br/webservice/alerta.php?";
                    String data = URLEncoder.encode("id_usuario", "UTF-8") + "=" + URLEncoder.encode(fid, "UTF-8");
                    data += "&" + URLEncoder.encode("id_tipo_alerta", "UTF-8") + "=" + URLEncoder.encode(id_tipo_alerta, "UTF-8");
                    data += "&" + URLEncoder.encode("comentario", "UTF-8") + "=" + URLEncoder.encode(comentario, "UTF-8");
                    data += "&" + URLEncoder.encode("lat", "UTF-8") + "=" + URLEncoder.encode(lat.toString(), "UTF-8");
                    data += "&" + URLEncoder.encode("lng", "UTF-8") + "=" + URLEncoder.encode(lng.toString(), "UTF-8");
                    data += "&" + URLEncoder.encode("hoje", "UTF-8") + "=" + URLEncoder.encode(hoje.toString(), "UTF-8");
                    data += "&" + URLEncoder.encode("cidade", "UTF-8") + "=" + URLEncoder.encode(cidade, "UTF-8");
                    data += "&" + URLEncoder.encode("estado", "UTF-8") + "=" + URLEncoder.encode(pais, "UTF-8");

                    URL url = new URL(link + data);
                    url.openStream();

                    Context mContext = getApplicationContext();
                    Toast.makeText(mContext, "Alerta Enviado com Sucesso!", Toast.LENGTH_LONG).show();
                    Games.Achievements.increment(mGoogleApiClient, "my_incremental_achievment_id", 1);
                    Tweet("Acabei de enviar um Alerta no @wazuapp!");
                 }else {
                     Context mContext = getApplicationContext();
                     Toast.makeText(mContext, "Alerta Não Enviado. Tente Novamente!", Toast.LENGTH_LONG).show();
                 }

             }else{
                Context mContext = getApplicationContext();
                Toast.makeText(mContext, "Alerta Não Enviado!", Toast.LENGTH_LONG).show();
             }

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void insertBDAlertaComentario(String... arg0){
        try{
            DbHelper dbHelper = new DbHelper(getApplicationContext());
            Cadastro cad = new Cadastro();
            String fid=null;
            List<Cadastro> xcad = dbHelper.selectCadastro();
            for (Iterator iterator = xcad.iterator(); iterator.hasNext(); ) {
                Cadastro icad = (Cadastro) iterator.next();
                fid = (String) icad.getId();
            }
            if (fid!=null){
                    String id_alerta = (String) arg0[0];
                    String comentario = (String) arg0[1];

                    String link = "http://www.wazu.com.br/webservice/comentario_alerta.php?";
                    String data = URLEncoder.encode("id_usuario", "UTF-8") + "=" + URLEncoder.encode(fid, "UTF-8");
                    data += "&" + URLEncoder.encode("id_alerta", "UTF-8") + "=" + URLEncoder.encode(id_alerta, "UTF-8");
                    data += "&" + URLEncoder.encode("comentario", "UTF-8") + "=" + URLEncoder.encode(comentario, "UTF-8");

                    URL url = new URL(link + data);
                    url.openStream();

                    Log.i("BDA",link+data);
                    Games.Achievements.increment(mGoogleApiClient, "my_incremental_achievment_id", 1);
                    Context mContext = getApplicationContext();
                    Toast.makeText(mContext, "Comentário Enviado com Sucesso!", Toast.LENGTH_LONG).show();
                    Tweet("Acabei de enviar um Comentário no @wazuapp!");

            }else{
                Context mContext = getApplicationContext();
                Toast.makeText(mContext, "Comentário Não Enviado! Tente Novamente.", Toast.LENGTH_LONG).show();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private void funcGetAlertas(String tipo, String friendId){
        String data = "";
        String data2 = "";

        try{

            Geocoder geocoder = new Geocoder(this,Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(Mycoord.latitude,Mycoord.longitude,1);
            String cidade = addresses.get(0).getLocality();
            String pais = addresses.get(0).getCountryCode();
            cidade = URLEncoder.encode(cidade, "UTF-8");
            pais = URLEncoder.encode(pais, "UTF-8");
            // Fetching the data from web service
            if (friendId==null) {
                data = downloadUrl("http://www.wazu.com.br/webservice/getalertas.php?id_tipo_alerta=" + tipo+"&cidade="+cidade+"&estado="+pais);
            }else{
                data = downloadUrl("http://www.wazu.com.br/webservice/getalertas.php?id_tipo_alerta=" + tipo+"&id_usuario="+friendId+"&cidade="+cidade+"&estado="+pais);
            }
            ListView listView = (ListView) findViewById(R.id.Alertaslist);
            ExpandableListView el = (ExpandableListView) findViewById(R.id.Routelist);
            JSONObject respAlerta = new JSONObject(data);
            JSONArray arr,arr2;
            try {
                listView.setVisibility(View.VISIBLE);
                arr = respAlerta.getJSONArray("dados");
                String id, id_tipo_alerta, tipo_alerta, id_usuario, usuario, idata, hora, lat, lng, comentario, tem_coment,tem_comentarios, t_comentario_usuario, t_usuario, t_comentario;
                ArrayList<Alertas> arrayOfUsers = new ArrayList<Alertas>();
                final AlertasList adapter = new AlertasList(this, arrayOfUsers);
                listView.setAdapter(adapter);
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlTitListas);
                TextView txt = (TextView) findViewById(R.id.txtTitListas);
                rl.setVisibility(View.VISIBLE);
                el.setVisibility(View.INVISIBLE);
                txt.setText("Lista de Alertas");
                for(int i=0;i<arr.length();i++){
                    JSONObject oneByOne = arr.getJSONObject(i);
                    id = oneByOne.getString("id");
                    id_tipo_alerta = oneByOne.getString("id_tipo_alerta");
                    tipo_alerta = oneByOne.getString("tipo_alerta");
                    id_usuario = oneByOne.getString("id_usuario");
                    usuario = oneByOne.getString("usuario");
                    idata = oneByOne.getString("data");
                    hora = oneByOne.getString("hora");
                    lat = oneByOne.getString("lat");
                    lng = oneByOne.getString("lng");
                    tem_coment = oneByOne.getString("tem_coment");
                    comentario = hora + ": " + oneByOne.opt("comentario").toString();
                    if (!tem_coment.equals("0")) {
                        data2 = downloadUrl("http://www.wazu.com.br/webservice/getcomentarioalerta.php?id_alerta=" + id);
                        JSONObject respAlerta2 = new JSONObject(data2);
                        arr2 = respAlerta2.getJSONArray("dados");
                        comentario = comentario + "\n\nComentários deste Alerta:\n";
                        t_comentario="";
                        for(int j=0;j<arr2.length();j++) {
                            JSONObject tcoment = arr2.getJSONObject(j);
                            t_comentario_usuario = tcoment.getString("comentario");
                            t_usuario = tcoment.getString("nome");
                            t_comentario = t_comentario +  t_usuario + ": " + t_comentario_usuario+"\n";
                        }
                        comentario = comentario + t_comentario;
                    }
                    Alertas newAlerta = new Alertas(id,tipo_alerta, id_tipo_alerta,id_usuario,usuario, idata,hora,lat, lng,comentario);
                    adapter.add(newAlerta);
                }

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> a, final View v, int position, long id) {
                        AlertDialog.Builder adb = new AlertDialog.Builder(v.getContext());
                        int positionToRemove = position;
                        final String Rid = adapter.getItem(positionToRemove).id;
                        View vw = (View) v.getParent();
                        ListView lv = (ListView) vw.findViewById(R.id.Alertaslist);
                        Double Lat = Double.valueOf(adapter.getItem(positionToRemove).lat);
                        Double Lng = Double.valueOf(adapter.getItem(positionToRemove).lng);
                        String Title = adapter.getItem(positionToRemove).tipo_alerta;
                        String Comentario = adapter.getItem(positionToRemove).comentario+"\nPor: "+adapter.getItem(positionToRemove).usuario;
                        lv.setVisibility(View.INVISIBLE);
                        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlTitListas);
                        rl.setVisibility(View.INVISIBLE);
                        Context mContext = v.getContext();
                        if (Title.contains("1")) {
                            Title = "Acidente";
                        } else if (Title.contains("2")) {
                            Title = "Assalto";
                        }else if (Title.contains("3")) {
                            Title = "Congestionamento";
                        }else if (Title.contains("4")) {
                            Title = "Obras";
                        }else if (Title.contains("5")) {
                            Title = "Polícia";
                        }else if (Title.contains("6")) {
                            Title = "Ônibus";
                        }
                        Toast.makeText(mContext, "Buscando Alerta!", Toast.LENGTH_LONG).show();
                        funcMarcaAlerta(Lat,Lng,Title,Comentario,Rid,true,true);

                    }
                });


            } catch (JSONException e) {
                listView.setVisibility(View.INVISIBLE);
                e.printStackTrace();
            }
        }catch(Exception e){
            Log.d("Background Task",e.toString());
        }
    }

    private void funcGetLinhas(){
        String data = "";

        try{
            // Fetching the data from web service

            Geocoder geocoder = new Geocoder(this,Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(Mycoord.latitude,Mycoord.longitude,1);
            String cidade = addresses.get(0).getLocality();
            String pais = addresses.get(0).getCountryCode();
            cidade = URLEncoder.encode(cidade, "UTF-8");
            pais = URLEncoder.encode(pais, "UTF-8");
            data = downloadUrl("http://www.wazu.com.br/webservice/getlinhas.php?cidade="+cidade+"&estado="+pais);
            JSONObject respAlerta = new JSONObject(data);
            JSONArray arr;
            try {
                arr = respAlerta.getJSONArray("dados");
                String Linhas[] = new String[arr.length()];
                for(int i=0;i<arr.length();i++){
                    JSONObject oneByOne = arr.getJSONObject(i);
                    Linhas[i] = oneByOne.getString("linha");
                }
                AutoCompleteTextView myAutoCompleteTextView
                        = (AutoCompleteTextView)findViewById(
                        R.id.textEndereco);

                myAutoCompleteTextView.setAdapter(
                        new ArrayAdapter<String>(this,
                                android.R.layout.simple_dropdown_item_1line, Linhas));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }catch(Exception e){
            Log.d("Background Task",e.toString());
        }
    }

    private void funcVerLinhas(String linha){
        String data = "";

        try{
            // Fetching the data from web service
            Geocoder geocoder = new Geocoder(this,Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(Mycoord.latitude,Mycoord.longitude,1);
            String cidade = addresses.get(0).getLocality();
            String estado = addresses.get(0).getCountryCode();
            cidade = URLEncoder.encode(cidade, "UTF-8");
            estado = URLEncoder.encode(estado, "UTF-8");
            linha = URLEncoder.encode(linha, "UTF-8");

            data = downloadUrl("http://www.wazu.com.br/webservice/getonline.php?cidade="+cidade+"&estado="+estado+"&linha="+linha);
            JSONObject respAlerta = new JSONObject(data);
            JSONArray arr;
            try {
                arr = respAlerta.getJSONArray("dados");
                String lat,lng,ordem, hora, tipo,usuario;
                LatLng MAlerta;
                for(int i=0;i<arr.length();i++){
                    JSONObject oneByOne = arr.getJSONObject(i);
                    lat = oneByOne.getString("lat");
                    lng = oneByOne.getString("lng");
                    ordem = oneByOne.getString("ordem");
                    hora = oneByOne.getString("hora");
                    tipo = oneByOne.getString("tipo_linha");
                    usuario = oneByOne.getString("usuario");
                    MAlerta = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
                    final RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlFavOnibus);
                    final TextView ctipo = (TextView) findViewById(R.id.txtFavTipoLinha);
                    final TextView cnumero = (TextView) findViewById(R.id.txtFavOLinha);
                    final TextView ctit = (TextView) findViewById(R.id.txtTitFavOnibus);
                    cnumero.setText(linha);
                    linha=URLDecoder.decode(linha);
                    ordem=URLDecoder.decode(ordem);
                    ctit.setText("Adicionar a Favoritos:\n\nLinha: "+linha+"\nNome: "+ordem+"\nHora: "+hora+"\nPor: "+usuario+"\n");

                    if (tipo.contains("1")) {
                        mMap.addMarker(new MarkerOptions()
                                .position(MAlerta)
                                .title(linha+" - "+ordem)
                                .snippet("Hora: " + hora + " - Tipo: Público" + "\nPor: "+usuario)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.buspublico))
                        );
                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                ctipo.setText("1");
                                rl.setVisibility(View.VISIBLE);
                            }
                        });
                    }else if (tipo.contains("2")) {
                        mMap.addMarker(new MarkerOptions()
                                .position(MAlerta)
                                .title(linha+" - "+ordem)
                                .snippet("Hora: " + hora + " - Tipo: Condominio" + "\nPor: "+usuario)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.buscondominio))
                        );
                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                ctipo.setText("2");
                                rl.setVisibility(View.VISIBLE);
                            }
                        });
                    }else if (tipo.contains("3")) {
                        mMap.addMarker(new MarkerOptions()
                                .position(MAlerta)
                                .title(linha+" - "+ordem)
                                .snippet("Hora: " + hora + " - Tipo: Especial" + "\nPor: "+usuario)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.busespecial))
                        );
                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                            @Override
                            public void onInfoWindowClick(Marker marker) {
                                ctipo.setText("3");
                                rl.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                    mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                        @Override
                        public View getInfoWindow(Marker arg0) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {
                            LinearLayout info = new LinearLayout(getApplicationContext());
                            info.setOrientation(LinearLayout.VERTICAL);

                            TextView title = new TextView(getApplicationContext());
                            title.setTextColor(Color.BLACK);
                            title.setGravity(Gravity.CENTER);
                            title.setTypeface(null, Typeface.BOLD);
                            title.setText(marker.getTitle());

                            TextView snippet = new TextView(getApplicationContext());
                            snippet.setTextColor(Color.GRAY);
                            snippet.setText(marker.getSnippet());

                            info.addView(title);
                            info.addView(snippet);

                            return info;
                        }
                    });
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(MAlerta, 10));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }catch(Exception e){
            Log.d("Background Task",e.toString());
        }
    }

    public void funcMarcaAlerta(double lat, double lng, String title, String comentario, String id, boolean zoom, boolean geo){
        LatLng MAlerta;
        String endereco = "";
        final RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlComentarioAlerta);
        final TextView IdAlerta = (TextView) findViewById(R.id.txtIdAlertaComentario);
        IdAlerta.setText(id);

        MAlerta = new LatLng(lat, lng);
        if (zoom == true) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(MAlerta, 16));
        }
        if (geo) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                endereco = addresses.get(0).getAddressLine(0).toString();
            } catch (IOException e1) {
                Context mContext = getApplicationContext();
                Toast.makeText(mContext, "Demora na Conexão com o Servidor!", Toast.LENGTH_LONG).show();
                e1.printStackTrace();
            }
        }
        if (title.contains("Acidente")){
                mMap.addMarker(new MarkerOptions()
                    .position(MAlerta)
                    .title(title)
                    .snippet(endereco+"\n"+comentario)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcadorambulance))
            );
        }else if (title.contains("Assalto")){
            mMap.addMarker(new MarkerOptions()
                    .position(MAlerta)
                    .title(title)
                    .snippet(endereco+"\n"+comentario)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcadorassalto))
            );
        } else if (title.contains("Congestionamento")){

            mMap.addMarker(new MarkerOptions()
                    .position(MAlerta)
                    .title(title)
                    .snippet(endereco+"\n"+comentario)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcadortraffic))
            );
        } else if (title.contains("Polícia")){

            mMap.addMarker(new MarkerOptions()
                    .position(MAlerta)
                    .title(title)
                    .snippet(endereco+"\n"+comentario)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcadorpolice))
            );
        }else if (title.contains("Ônibus")){

            mMap.addMarker(new MarkerOptions()
                    .position(MAlerta)
                    .title(title)
                    .snippet(endereco+"\n"+comentario)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcadorpontoonibus))
            );
        }else if (title.contains("Obras")){

            mMap.addMarker(new MarkerOptions()
                    .position(MAlerta)
                    .title(title)
                    .snippet(endereco+"\n"+comentario)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcadorobras))
            );
        }

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                rl.setVisibility(View.VISIBLE);
            }
        });
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                LinearLayout info = new LinearLayout(getApplicationContext());
                info.setOrientation(LinearLayout.VERTICAL);
                TextView title = new TextView(getApplicationContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());
                TextView snippet = new TextView(getApplicationContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());
                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

    }

    public void funcBtnMarcaLocal(String title, Double FLat, Double FLng){

        LatLng point = new LatLng(FLat,FLng);

        DbHelper dbHelper = new DbHelper(this);
        List<Preferencias> prefs = dbHelper.selectPreferencias();
        for (Iterator iterator = prefs.iterator(); iterator.hasNext(); ) {
            Preferencias iprefs = (Preferencias) iterator.next();
            distanciaprefs = (double) iprefs.getDistancia();
        }

        mMap.addMarker(new MarkerOptions()
                .position(point)
                .title(title.toString())
                .snippet("Alarme em "+String.valueOf(distanciaprefs)+"m antes do destino.")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marcadorfavorito))
        );
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlFavDestino);
                rl.setVisibility(View.VISIBLE);
            }
        });
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                LinearLayout info = new LinearLayout(getApplicationContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getApplicationContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getApplicationContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });
    }

    private void funcListaAmigos(){
        if (AccessToken.getCurrentAccessToken()!=null) {
            final HashMap<String, String> jsonValues = new HashMap<String, String>();

            ArrayList<Friends> arrayOfUsers = new ArrayList<Friends>();
            final FriendList adapter = new FriendList(this, arrayOfUsers);
            ListView listView = (ListView) findViewById(R.id.friendlist);
            listView.setAdapter(adapter);
            GraphRequest rlist =
                    new GraphRequest(
                            AccessToken.getCurrentAccessToken(),
                            "/me/friends",
                            null,
                            HttpMethod.GET,
                            new GraphRequest.Callback() {
                                public void onCompleted(GraphResponse response) {
                                    JSONObject obj = response.getJSONObject();

                                    try {
                                        try {
                                            JSONArray arr;
                                            arr = obj.getJSONArray("data");
                                            String nome, foto = "";
                                            for (int j = 0; j < arr.length(); j++) {
                                                JSONObject oneByOne = arr.getJSONObject(j);
                                                JSONArray fotos;
                                                foto = (String) ((JSONObject) ((JSONObject) oneByOne.get("picture")).get("data")).get("url");
                                                try {
                                                    URL imgUrl = new URL(foto);
                                                    try {
                                                        InputStream in = (InputStream) imgUrl.getContent();
                                                        Bitmap bitmap = BitmapFactory.decodeStream(in);
                                                        Friends newFriend = new Friends(oneByOne.opt("name").toString(), bitmap, oneByOne.opt("id").toString());
                                                        adapter.add(newFriend);
                                                    } catch (IOException i) {
                                                        i.printStackTrace();
                                                    }
                                                } catch (MalformedURLException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }catch (NullPointerException e){
                                            e.printStackTrace();
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                    );

            Bundle parameters = new Bundle();
            parameters.putString("fields", "name, id, picture");
            rlist.setParameters(parameters);
            rlist.executeAsync();

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> a, final View v, int position, long id) {
                    AlertDialog.Builder adb=new AlertDialog.Builder(v.getContext());
                    int positionToRemove = position;
                    final String Rid = adapter.getItem(positionToRemove).id;
                    adb.setTitle("O que você deseja fazer?");
                    adb.setMessage("Deseja ver os Alertas de seu amigo?");
                    adb.setNegativeButton("Não", null);
                    adb.setPositiveButton("Sim", new AlertDialog.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            View vw = (View) v.getParent();
                            ListView lv = (ListView) vw.findViewById(R.id.friendlist);
                            lv.setVisibility(View.INVISIBLE);
                            RelativeLayout rl = (RelativeLayout) findViewById(R.id.rlTitListas);
                            rl.setVisibility(View.INVISIBLE);
                            Context mContext = v.getContext();
                            Toast.makeText(mContext, "Buscando Alertas!", Toast.LENGTH_LONG).show();
                            funcGetAlertas(null,Rid);

                        }});
                    adb.show();
                }
            });

        }

    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i("TAG", "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    private void Tweet (String msg){
        TweetComposer.Builder builder = new TweetComposer.Builder(this)
                .text(msg);
        builder.show();
    }

    public void funcTweet(View view) {
           Tweet("Estou usando o @wazuapp!");
    }

    public void Vibrar(int ligado){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 500, 500};
        if (ligado==1) {
            v.vibrate(pattern, 0);
        }else{
            v.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // The player is signed in. Hide the sign-in button and allow the
        // player to proceed.
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            // already resolving
            return;
        }

        // if the sign-in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInflow) {
            mAutoStartSignInflow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            // Attempt to resolve the connection failure using BaseGameUtils.
            // The R.string.signin_other_error value should reference a generic
            // error string in your strings.xml file, such as "There was
            // an issue with sign-in, please try again later."
            if (!BaseGameUtils.resolveConnectionFailure(this,
                    mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, "Error")) {
                mResolvingConnectionFailure = false;
            }
        }

        // Put code here to display the sign-in button
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Attempt to reconnect
        mGoogleApiClient.connect();
    }
}
