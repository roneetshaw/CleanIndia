package com.test.hello.myhelloworld;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

/**
 * Created by Roneet on 20-05-2016.
 */
public class MapFragment extends SupportMapFragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener {
        private GoogleApiClient mGoogleApiClient;
        private Location mCurrentLocation;

        private final int[] MAP_TYPES = { GoogleMap.MAP_TYPE_SATELLITE,
                GoogleMap.MAP_TYPE_NORMAL,
                GoogleMap.MAP_TYPE_HYBRID,
                GoogleMap.MAP_TYPE_TERRAIN,
                GoogleMap.MAP_TYPE_NONE };
        private int curMapTypeIndex = 1;

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
                super.onViewCreated(view, savedInstanceState);

                setHasOptionsMenu(true);
                if (!((LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE))
                        .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        Intent gpsOptionsIntent = new Intent(
                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(gpsOptionsIntent);//prompt user to enable gps
                }else{
                        //gps is enabled
                        //Toast.makeText(getActivity(),"GPS enabled",Toast.LENGTH_SHORT).show();

                }
                mGoogleApiClient = new GoogleApiClient.Builder( getActivity() )
                        .addConnectionCallbacks( this )
                        .addOnConnectionFailedListener( this )
                        .addApi( LocationServices.API )
                        .build();

                initListeners();
        }
        private void initListeners() {
                getMap().setOnMarkerClickListener(this);
                getMap().setOnMapLongClickListener(this);
                getMap().setOnInfoWindowClickListener(this);
                getMap().setOnMapClickListener(this);
        }
        @Override
        public void onStart() {
                super.onStart();
                mGoogleApiClient.connect();
        }

        @Override
        public void onStop() {
                super.onStop();
                if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
                        mGoogleApiClient.disconnect();
                }
        }

        @Override
        public void onConnected(Bundle bundle) {
                mCurrentLocation = LocationServices
                        .FusedLocationApi
                        .getLastLocation( mGoogleApiClient );

                initCamera(mCurrentLocation);
        }
        private void initCamera( Location location ) {
                if (!((LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE))
                        .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        Intent gpsOptionsIntent = new Intent(
                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(gpsOptionsIntent);//prompt user to enable gps
                }else{
                        //gps is enabled
                        //Toast.makeText(getActivity(),"GPS enabled",Toast.LENGTH_SHORT).show();
                        CameraPosition position = CameraPosition.builder()
                                .target( new LatLng( location.getLatitude(),
                                        location.getLongitude() ) )
                                .zoom( 16f )
                                .bearing( 0.0f )
                                .tilt( 0.0f )
                                .build();

                        getMap().animateCamera( CameraUpdateFactory
                                .newCameraPosition(position), null );

                        getMap().setMapType(MAP_TYPES[curMapTypeIndex]);
                        getMap().setTrafficEnabled(true);
                        getMap().setMyLocationEnabled(true);
                        getMap().getUiSettings().setZoomControlsEnabled(true);
                }

        }
        @Override
        public void onMapClick(final LatLng latLng) {
                getMap().clear();
                MarkerOptions options = new MarkerOptions().position(latLng);
                options.title(getAddressFromLatLng(latLng));
                getMap().setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                                Intent intent1 = new Intent(getActivity(), FillDescriptionActivity.class);
                                intent1.putExtra("address", getAddressFromLatLng(latLng));
                                        startActivity(intent1);
                        }
                });
                options.icon(BitmapDescriptorFactory.fromResource(R.drawable.broom));
                getMap().addMarker(options);
        }
        private String getAddressFromLatLng( LatLng latLng ) {
                Geocoder geocoder = new Geocoder( getActivity() );

                String address = "";
                try {
                        address = geocoder
                                .getFromLocation( latLng.latitude, latLng.longitude, 1 )
                                .get( 0 ).getAddressLine(0);
                } catch (IOException e ) {
                }

                return address;
        }

        protected void search(List<Address> addresses) {

                Address address = (Address) addresses.get(0);
                double home_long = address.getLongitude();
                double home_lat = address.getLatitude();
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                String addressText = String.format(
                        "%s, %s",
                        address.getMaxAddressLineIndex() > 0 ? address
                                .getAddressLine(0) : "", address.getCountryName());

                MarkerOptions markerOptions = new MarkerOptions();

                markerOptions.position(latLng);
                markerOptions.title(addressText);

                getMap().clear();
                getMap().addMarker(markerOptions);
                getMap().moveCamera(CameraUpdateFactory.newLatLng(latLng));
                getMap().animateCamera(CameraUpdateFactory.zoomTo(15));
        }
        @Override
        public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                return true;
        }
        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {

        }

        @Override
        public void onInfoWindowClick(Marker marker) {

        }

        @Override
        public void onMapLongClick(LatLng latLng) {
                MarkerOptions options = new MarkerOptions().position(latLng);
                options.title(getAddressFromLatLng(latLng));

                options.icon(BitmapDescriptorFactory.fromBitmap(
                        BitmapFactory.decodeResource(getResources(),
                                R.mipmap.ic_launcher)));

                getMap().addMarker(options);
        }
}
