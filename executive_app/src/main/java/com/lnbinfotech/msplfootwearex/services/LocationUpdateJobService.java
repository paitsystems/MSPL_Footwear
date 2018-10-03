package com.lnbinfotech.msplfootwearex.services;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.lnbinfotech.msplfootwearex.R;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.db.DBHandler;
import com.lnbinfotech.msplfootwearex.location.LocationProvider;
import com.lnbinfotech.msplfootwearex.log.WriteLog;
import com.lnbinfotech.msplfootwearex.post.Post;

import org.json.JSONArray;
import org.json.JSONObject;

//Created by Anup on 01-09-2018.

public class LocationUpdateJobService extends JobService {

    private LocationProvider provider;
    LocationManager loc_manager;
    LocationListener loc_listener;
    boolean gps_enabled = false, network_enabled = false;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Constant.showLog("onStartJob");
        //provider = new LocationProvider(getApplicationContext(),this);
        //provider.connect();
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Constant.showLog("onStopJob");
        //writeLog("onStopJob");
        //provider.disconnect();

        try {
            loc_manager = (LocationManager) getSystemService(LOCATION_SERVICE);
            loc_listener = new MyLocationListener();
            gps_enabled = loc_manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            network_enabled = loc_manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!gps_enabled && !network_enabled) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
                builder.setTitle("Error While Updating Location");
                builder.setMessage("Please Turn On Location");
                builder.setPositiveButton("Turn On", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //   Toast.makeText(getBaseContext(), "Location Not Updated", Toast.LENGTH_LONG).show();
                    }
                });
                builder.create().show();
            }
            if (gps_enabled) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    //return 0;
                }
                loc_manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 12000, 1000, loc_listener);
                //loc_manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
            if (network_enabled) {
                loc_manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 12000, 1000, loc_listener);
                //loc_manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        } catch (Exception e) {
            e.printStackTrace();
            //  Toast.makeText(getBaseContext(), "Something Went Wrong", Toast.LENGTH_LONG).show();
        }

        return false;

    }

    private void writeLog(String data) {
        new WriteLog().writeLog(getBaseContext(), "ScheduledJobService_" + data);
    }

    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                    /*loc_manager.removeUpdates(loc_listener);
					String str_latitude = "Latitude : "+ location.getLatitude();
					double latitude = location.getLatitude();
					String str_longitude = "Longitude : "+ location.getLongitude();
					double longitude = location.getLongitude();
					String altitiude = "Altitiude: " + location.getAltitude();
	                String accuracy = "Accuracy: " + location.getAccuracy();
	                String time = "Time: " + location.getTime();*/
                if (ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                loc_manager.removeUpdates(loc_listener);
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                String key = getResources().getString(R.string.google_maps_key);
                String url = "https://maps.googleapis.com/maps/api/geocode/json?latlng="
                        + latitude + "," + longitude + "&sensor=true";
                Log.d("Log", url);
                new GetLocFromGoogleAPI().execute(url);

                /*Geocoder geo = new Geocoder(getBaseContext(), Locale.ENGLISH);
                try {
                    List<Address> address = geo.getFromLocation(latitude, longitude, 1);
                    if (address != null && address.size() != 0) {
                        Address returnedAddress = address.get(0);
                        StringBuilder strReturnedAddress = new StringBuilder();
                        for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                            strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                        }
                        Intent intent = new Intent();
                        intent.putExtra("loc", strReturnedAddress.toString());
                        intent.setAction("location_updated");
                        sendBroadcast(intent);
                        LocationService.this.stopSelf();
                    } else {
                        //   Toast.makeText(getBaseContext(), "Location Not Updated0", Toast.LENGTH_LONG).show();
                        String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&sensor=true";
                        Log.d("Log",url);
                        new GetLocFromGoogleAPI().execute(url);
                        *//*try {
                            JSONObject jsonObj = getJSONfromURL("http://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&sensor=true");
                            String Status = jsonObj.getString("status");
                            if (Status.equalsIgnoreCase("OK")) {
                                JSONArray Results = jsonObj.getJSONArray("results");
                                JSONObject location1 = Results.getJSONObject(0);
                                String finalAddress = location1.getString("formatted_address");
                                Log.d("Log",finalAddress);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }*//*
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    //  Toast.makeText(getBaseContext(), "Location Not Updated1", Toast.LENGTH_LONG).show();
                }*/
            } else {
                //	   Toast.makeText(getBaseContext(), "Location Not Updated2", Toast.LENGTH_LMaONG).show();
            }
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }


        private class GetLocFromGoogleAPI extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... strings) {
                return Post.POST(strings[0]);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.d("Log", s);
                try {
                    JSONObject jsonObj = new JSONObject(s);
                    String Status = jsonObj.getString("status");
                    if (Status.equalsIgnoreCase("OK")) {
                        JSONArray Results = jsonObj.getJSONArray("results");
                        JSONObject location1 = Results.getJSONObject(0);
                        String finalAddress = location1.getString("formatted_address");
                        Log.d("Log", finalAddress);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

}