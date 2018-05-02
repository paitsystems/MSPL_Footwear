package com.lnbinfotech.msplfootwear.location;

//Created by ANUP on 30-04-2018.

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.lnbinfotech.msplfootwear.constant.Constant;
import com.lnbinfotech.msplfootwear.log.WriteLog;
import com.lnbinfotech.msplfootwear.permission.GetPermission;

import java.util.List;
import java.util.Locale;

public class LocationProvider implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Context context;
    private LocationCallback1 locationCallback;
    private Activity activity;
    public static final int REQUEST_CHECK_SETTINGS = 1000;

    public abstract interface LocationCallback1 {
        void handleNewLocation(Location location,String address);
        void locationAvailable();
    }

    public LocationProvider(Context _context, LocationCallback1 _locationCallback, Activity _activity) {
        Constant.showLog("LocationProvider");
        googleApiClient = new GoogleApiClient.Builder(_context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setSmallestDisplacement(0)
                .setInterval(5*60*1000)
                .setFastestInterval(10 * 1000);

        this.context = _context;
        this.locationCallback = _locationCallback;
        this.activity = _activity;

        checkLocationAvailability();
    }

    public void connect() {
        googleApiClient.connect();
    }

    public void disconnect() {
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
            googleApiClient.disconnect();
        }
    }

    public void checkLocationAvailability(){
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        writeLog("LocationSettingsStatusCodes.SUCCESS");
                        Constant.showLog("LocationSettingsStatusCodes.SUCCESS");
                        locationCallback.locationAvailable();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        writeLog("checkLocationAvailability_"+LocationSettingsStatusCodes.RESOLUTION_REQUIRED);
                        try {
                            Constant.showLog("LocationSettingsStatusCodes.RESOLUTION_REQUIRED");
                            status.startResolutionForResult(activity, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                            writeLog("checkLocationAvailability_"+e.getMessage());
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        writeLog("LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE");
                        Constant.showLog("LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE");
                        break;
                }
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        getAddress(location);
        //locationCallback.handleNewLocation(location);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Constant.showLog("onConnected");
        if(new GetPermission().checkCoarseLocationPermission(context) &&
                new GetPermission().checkFineLocationPermission(context)) {
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
            } else {
                //locationCallback.handleNewLocation(location);
                getAddress(location);
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Constant.showLog("onConnectionFailed");
        if (connectionResult.hasResolution() && context instanceof Activity) {
            try {
                Activity activity = (Activity)context;
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(activity, 9000);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Constant.showLog("Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    private void getAddress(Location location){
        String str = null;
        try {
            final double lat = location.getLatitude();
            final double lon = location.getLongitude();
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(context, Locale.getDefault());
            addresses = geocoder.getFromLocation(lat, lon, 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            Constant.showLog(address + "-" + city + "-" + state + "-" + country + "-" + postalCode + "-" + knownName);
            str = address;
            locationCallback.handleNewLocation(location,str);
        }catch (Exception e){
            locationCallback.handleNewLocation(location,"NA");
            e.printStackTrace();
            writeLog("getAddress_"+e.getMessage());
        }
    }

    private void writeLog(String _data){
        new WriteLog().writeLog(context,"LocationProvider_"+_data);
    }
}
