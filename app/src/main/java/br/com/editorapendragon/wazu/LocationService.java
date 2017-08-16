package br.com.editorapendragon.wazu;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.StrictMode;
import android.os.Vibrator;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Josué on 23/05/2016.
 */
public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener {

    private static final String TAG = "LocationService";
    private static final int LOCATION_INTERVAL = 1000;
    private Context mContext;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;
    private FusedLocationProviderApi fusedLocationProviderApi;

    private NotificationManager mNM;
    Bundle b;
    Intent notificationIntent;
    private final IBinder mBinder = new LocalBinder();
    ArrayList<LatLng> sAlertas;
    private Uri alert;

    public class LocalBinder extends Binder {
        LocationService getService() {
            return LocationService.this;
        }
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate(){

        Log.e(TAG, "onCreate");
        mContext = this;
        getLocation();
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(LocationService.this, 0, new Intent(LocationService.this,   MapsActivity.class), 0);
        notificationIntent = new Intent(this, LocationService.class);

        sAlertas = MapsActivity.tAlertas;
        showNotification("Alarme Ativo. Toque para abrir.");
    }

    private void showNotification(String text) {
        PendingIntent contentIntent = PendingIntent.getActivity(LocationService.this, 0, new Intent(LocationService.this,   MapsActivity.class), 0);
        Notification notification = new Notification.Builder(mContext)
                .setContentTitle("Wazu")
                .setContentText(text)
                .setSmallIcon(R.drawable.ic_notification_wazu)
                .setContentIntent(contentIntent)
                .build();

        notification.flags = Notification.FLAG_ONGOING_EVENT | Notification.FLAG_NO_CLEAR;
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        mNM.notify(R.string.local_service_started, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        try{
            if(googleApiClient!=null){
                googleApiClient.disconnect();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        Log.e(TAG, "onDestroy");
        mNM.cancel(R.string.local_service_started);
        stopSelf();

    }

    private void getLocation(){
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(LOCATION_INTERVAL);
        locationRequest.setFastestInterval(LOCATION_INTERVAL);
        fusedLocationProviderApi = LocationServices.FusedLocationApi;
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
//  Location location = fusedLocationProviderApi.getLastLocation(googleApiClient);
        fusedLocationProviderApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int arg0) {

    }

    @Override
    public void onLocationChanged(Location locat) {

        DbHelper dbHelper = new DbHelper(getApplicationContext());
        List<Preferencias> prefs = dbHelper.selectPreferencias();
        Double distanciapr = 0.0;
        Location pdistance;
        pdistance = new Location("");
        int Som = 0;
        int Vibra = 0;

        for (Iterator iterator = prefs.iterator(); iterator.hasNext(); ) {
            Preferencias iprefs = (Preferencias) iterator.next();
            distanciapr = (double) iprefs.getDistancia();
            Som = iprefs.getSom();
            Vibra = iprefs.getVibra();
        }
        if (sAlertas!=null) {
            if (!sAlertas.isEmpty()) {
                for (int i = 0; i < sAlertas.size(); i++) {
                    LatLng point = (LatLng) sAlertas.get(i);
                    pdistance.setLatitude(point.latitude);
                    pdistance.setLongitude(point.longitude);
                    double dponto = locat.distanceTo(pdistance);
                    Log.i("LOCALSERVICE", "Dponto:" + String.valueOf(dponto));
                    Log.i("LOCALSERVICE", "Dponto:" + String.valueOf(distanciapr));
                    if (dponto <= distanciapr) {
                        try {

                            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
                            if (alert == null) {
                                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                                if (alert == null) {
                                    alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                                }
                            }
                            if (!MapsActivity.isAlarme) {
                                if (Som==1) {
                                    MediaPlayer mp = new MediaPlayer();
                                    mp.setAudioStreamType(AudioManager.STREAM_ALARM);
                                    mp.setDataSource(LocationService.this, alert);
                                    mp.setLooping(true);
                                    mp.prepare();
                                    mp.start();
                                }
                                if (Vibra==1){
                                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                    long[] pattern = {0, 500, 500};
                                    v.vibrate(pattern, 0);
                                }
                                MapsActivity.isAlarme = true;
                                showNotification("Toque para desativar o alarme.");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                sAlertas = null;
            }
        }

    }
    @Override
    public void onConnectionFailed(ConnectionResult arg0) {

    }

 }