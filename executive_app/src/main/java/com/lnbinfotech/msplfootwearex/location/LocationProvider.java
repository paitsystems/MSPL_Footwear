package com.lnbinfotech.msplfootwearex.location;

//Created by ANUP on 25-04-2018.

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.lnbinfotech.msplfootwearex.constant.Constant;
import com.lnbinfotech.msplfootwearex.permission.GetPermission;

public class LocationProvider implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Context context;
    private LocationCallback1 locationCallback;

    public abstract interface LocationCallback1 {
        public void handleNewLocation(Location location);
    }

    public LocationProvider(Context _context, LocationCallback1 _locationCallback) {
        Constant.showLog("LocationProvider");
        googleApiClient = new GoogleApiClient.Builder(_context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setSmallestDisplacement(0)
                .setInterval(30 * 1000)
                .setFastestInterval(10 * 1000);

        this.context = _context;
        this.locationCallback = _locationCallback;

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

    @Override
    public void onLocationChanged(Location location) {
        locationCallback.handleNewLocation(location);
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
                locationCallback.handleNewLocation(location);
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
}
