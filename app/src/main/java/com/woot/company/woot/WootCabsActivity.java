package com.woot.company.woot;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

public class WootCabsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {

    protected GoogleApiClient mGoogleApiClient;
    private static final String TAG = "NOTE";
    private PlaceAutocompleteAdapter mAdapter, mAdapter2;
    private AutoCompleteTextView mAutocompleteView, mAutocompleteView2;
    private TextView mPlaceDetailsText, textView;
    private TextView mPlaceDetailsAttribution;
    private static final LatLngBounds BOUNDS_BANGALORE = new LatLngBounds(new LatLng(-33.682247, 151.383362), new LatLng(13.2045, 77.7077));
    private GoogleMap mMap;
    Double fromLatitude, fromLongitude, toLatitude, toLongitude;
    String globaldistance = " ", route = null, vehicle = null, origin = null, destination = null;
    Button button;
    SharedPreferences prefs;
    LatLng from, to;
    String mylatitude, mylongitude;
    String myloc;
    Marker marker1;
    private static long back_pressed;
    private static final int REQUEST_LOCATION = 0;
    private View mLayout;

    int variable = 0, fromdragvariable = 1,todragvariable = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_woot_cabs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(WootCabsActivity.this);

        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, 0, this).addApi(Places.GEO_DATA_API).build();

        mAutocompleteView = (AutoCompleteTextView) findViewById(R.id.autocomplete_places);
        mAutocompleteView2 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);

        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);
        mAutocompleteView2.setOnItemClickListener(mAutocompleteClickListener2);

        mAdapter = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_BANGALORE, null);
        mAdapter2 = new PlaceAutocompleteAdapter(this, mGoogleApiClient, BOUNDS_BANGALORE, null);

        mAutocompleteView.setAdapter(mAdapter);
        mAutocompleteView2.setAdapter(mAdapter2);

        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkFare(v);
            }
        });
        View header = navigationView.getHeaderView(0);

        String name, phone, email;
        SharedPreferences prefs;

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        name = prefs.getString("name", null);
        name.toString();
        //myloc = prefs.getString("myloc", null);
        //myloc.toString();
        phone = prefs.getString("phone", null);
        email = prefs.getString("email", null);
        email.toString();
        TextView names = (TextView) header.findViewById(R.id.username);

        if (name != null) {
            names.setText(name);
        } else
            names.setText(" ");
        TextView emails = (TextView) header.findViewById(R.id.emailid);
        emails.setText(email);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if ((ActivityCompat.checkSelfPermission(WootCabsActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) ||
                (ActivityCompat.checkSelfPermission(WootCabsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            requestLocationPermission();
        } else {
            final Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            if (location != null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(location.getLatitude(), location.getLongitude()), 13));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                        .zoom(17)                   // Sets the zoom
                        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mMap.setMyLocationEnabled(true);
                mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        fromdragvariable++;
                        mylatitude = String.valueOf(location.getLatitude());
                        mylongitude = String.valueOf(location.getLongitude());
                        BackgroundTaskLOCF backgroundTaskLOCF = new BackgroundTaskLOCF(WootCabsActivity.this);
                        backgroundTaskLOCF.execute(mylatitude, mylongitude);
                        return false;
                    }
                });
            }
            if (mMap != null) {
                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location arg0) {
                        // TODO Auto-generated method stub
                        mylatitude = String.valueOf(arg0.getLatitude());
                        mylongitude = String.valueOf(arg0.getLongitude());
                        BackgroundTaskLOCF backgroundTaskLOCF = new BackgroundTaskLOCF(WootCabsActivity.this);
                        backgroundTaskLOCF.execute(mylatitude,mylongitude);
                    }
                });
            }
        }

        mMap.setPadding(0, 280, 0, 0);
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final AutocompletePrediction item = mAdapter.getItem(position);
            final CharSequence primaryText = item.getPrimaryText(null);
            Log.i(TAG, "Autocomplete item selected: " + primaryText);
            from = getLocationFromAddress(WootCabsActivity.this, "Source");
            mMap.addMarker(new MarkerOptions().position(from).icon(BitmapDescriptorFactory.fromResource(R.drawable.greenmarker)).title("Origin")).setDraggable(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(from));
            fromLatitude = from.latitude;
            fromLongitude = from.longitude;
            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {
                    fromdragvariable++;
                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    /*fromLatitude = marker.getPosition().latitude;
                    fromLongitude = marker.getPosition().longitude;
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));*/
                    //marker.remove();
                   // from = marker.getPosition();
                    fromLatitude = marker.getPosition().latitude;
                    fromLongitude = marker.getPosition().longitude;
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                    BackgroundTaskLOCF backgroundTaskLOCF = new BackgroundTaskLOCF(WootCabsActivity.this);
                    backgroundTaskLOCF.execute(fromLatitude.toString(), fromLongitude.toString());
                    //from = getLocationFromAddress(WootCabsActivity.this, "Dragged");
                }

            });
        }
    };

    private AdapterView.OnItemClickListener mAutocompleteClickListener2 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            to = getLocationFromAddress(WootCabsActivity.this, "Destination");
            mMap.addMarker(new MarkerOptions().position(to).icon(BitmapDescriptorFactory.fromResource(R.drawable.redmarker)).title("Destination")).setDraggable(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(to));
            toLatitude = to.latitude;

            toLongitude = to.longitude;
            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {
                    todragvariable++;
                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {
                    toLatitude = marker.getPosition().latitude;
                    toLongitude = marker.getPosition().longitude;
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                    BackgroundTaskLOCT backgroundTaskLOCT = new BackgroundTaskLOCT(WootCabsActivity.this);
                    backgroundTaskLOCT.execute(toLatitude.toString(), toLongitude.toString());
                    marker.remove();
                    //from = getLocationFromAddress(WootCabsActivity.this, "Dragged");
                }

            });
            final String distance = getDistance(fromLatitude, fromLongitude, toLatitude, toLongitude);
            globaldistance = distance;
        }
    };

    public LatLng getLocationFromAddress(Context context, String method) {
        try {
            if (method.equals("Source")) {
                mMap.clear();
                String strAddress = mAutocompleteView.getText().toString();
                origin = mAutocompleteView.getText().toString();
                Geocoder coder = new Geocoder(context);
                List<android.location.Address> address;
                address = coder.getFromLocationName(strAddress, 1);
                if (address == null) {
                    return null;
                }
                android.location.Address location = address.get(0);
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                return latLng;
            }
            else if (method.equals("Destination")) {
                String strAddress = mAutocompleteView2.getText().toString();
                destination = mAutocompleteView2.getText().toString();
                Geocoder coder = new Geocoder(context);
                List<android.location.Address> address;
                address = coder.getFromLocationName(strAddress, 1);
                if (address == null) {
                    return null;
                }
                android.location.Address location = address.get(0);
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                return latLng;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public String getDistance(final double lat1, final double lon1, final double lat2, final double lon2) {
        final String[] parsedDistance = new String[1];
        final String[] response = new String[1];
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    URL url = new URL("http://maps.googleapis.com/maps/api/directions/json?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&sensor=false&units=metric&mode=driving");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoInput(true);
                    urlConnection.connect();
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    inputStream.close();
                    response[0] = stringBuilder.toString();
                    JSONObject jsonObject = new JSONObject(response[0]);
                    JSONArray array = jsonObject.getJSONArray("routes");
                    JSONObject routes = array.getJSONObject(0);
                    JSONArray legs = routes.getJSONArray("legs");
                    JSONObject steps = legs.getJSONObject(0);
                    JSONObject distance = steps.getJSONObject("distance");
                    parsedDistance[0] = distance.getString("text");

                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return parsedDistance[0];
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
        Toast.makeText(this, "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(), Toast.LENGTH_SHORT).show();
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
        getMenuInflater().inflate(R.menu.woot_cabs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit().clear().commit();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent move;
        if (id == R.id.nav_refer_activity) {
            move = new Intent(this, ReferActivity.class);
            startActivity(move);
        } else if (id == R.id.nav_promo_activity) {
            move = new Intent(this, PromoActivity.class);
            startActivity(move);
        } else if (id == R.id.nav_slideshow) {
            move = new Intent(this, RidesActivity.class);
            startActivity(move);
        } else if (id == R.id.nav_manage) {
            move = new Intent(this, WootShareActivity.class);
            startActivity(move);
        } else if (id == R.id.nav_share) {
            move = new Intent(this, City.class);
            startActivity(move);
        } else if (id == R.id.nav_send) {
            prefs = PreferenceManager.getDefaultSharedPreferences(this);
            prefs.edit().clear().commit();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_rate_card) {
            move = new Intent(this, RateCardActivity.class);
            startActivity(move);
        } else if (id == R.id.nav_about) {
            move = new Intent(this, AboutActivity.class);
            startActivity(move);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onRadioButtonClicked(View view) {
        Boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.hatchback:
                if (checked)
                    vehicle = "Hatchback";
                break;
            case R.id.sedan:
                if (checked)
                    vehicle = "Sedan";
                break;
            case R.id.suv:
                if (checked)
                    vehicle = "SUV";
                break;
            case R.id.tt:
                if (checked)
                    vehicle = "TempoTraveller";
                break;
        }
    }

    public void onRouteClicked(View view) {
        Boolean checked = ((RadioButton) view).isChecked();
        switch (view.getId()) {
            case R.id.oneway:
                if (checked)
                    route = "OneWay";
                break;
            case R.id.twoway:
                if (checked)
                    route = "TwoWay";
                break;
        }
    }

    public void checkFare(View view) {
        if (vehicle != null && route != null && from != null && to != null) {
            Intent intent = new Intent(this, ConfirmationPage.class);
            intent.putExtra("Vehicle", vehicle);
            intent.putExtra("Route", route);
            intent.putExtra("Distance", globaldistance);
           /* intent.putExtra("mylatf", fromLatitude);
            intent.putExtra("mylonf", fromLongitude);
            intent.putExtra("mylatt", toLatitude);
            intent.putExtra("mylont", toLongitude);*/
            intent.putExtra("Origins", origin);
            intent.putExtra("Destination", destination);
            startActivity(intent);
        } else if (from == null)
            Toast.makeText(this, "Choose origin", Toast.LENGTH_SHORT).show();
        else if (to == null)
            Toast.makeText(this, "Choose destination", Toast.LENGTH_SHORT).show();
        else if (vehicle == null)
            Toast.makeText(this, "Choose vehicle", Toast.LENGTH_LONG).show();
        else if (route == null)
            Toast.makeText(this, "Choose trip", Toast.LENGTH_LONG).show();
    }

    public class BackgroundTaskLOCF extends AsyncTask<String, Void, String> {
        Context ctx;
        BackgroundTaskLOCF(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            String mylat = params[0];
            String mylon = params[1];
            String response = "";
            try {
                URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + mylat + "," + mylon + "&key=AIzaSyBPci7PWwi-E9Pog_6sc1htP1XByipmUGg");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                inputStream.close();
                response = stringBuilder.toString();
                JSONObject jsonObject = new JSONObject(response);
                jsonObject = jsonObject.getJSONArray("results").getJSONObject(0);
                myloc = jsonObject.getString("formatted_address");
                return myloc;
            } catch (Exception e) {
                Log.e("error", "problem at from.php... " + e);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            if (variable == 0 || fromdragvariable != 0 ) {
                mAutocompleteView.setText(myloc);
                variable++;
                fromdragvariable = 0;
            }
            //mMap.setOnMyLocationChangeListener(null);
        }
    }

    public class BackgroundTaskLOCT extends AsyncTask<String, Void, String> {
        Context ctx;
        BackgroundTaskLOCT(Context ctx) {
            this.ctx = ctx;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            String mylat = params[0];
            String mylon = params[1];
            String response = "";
            try {
                URL url = new URL("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + mylat + "," + mylon + "&key=AIzaSyBPci7PWwi-E9Pog_6sc1htP1XByipmUGg");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                StringBuilder stringBuilder = new StringBuilder();
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                inputStream.close();
                response = stringBuilder.toString();
                JSONObject jsonObject = new JSONObject(response);
                jsonObject = jsonObject.getJSONArray("results").getJSONObject(0);
                myloc = jsonObject.getString("formatted_address");
                return myloc;
            } catch (Exception e) {
                Log.e("error", "problem at from.php... " + e);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            if (variable == 0 || todragvariable != 0 ) {
                mAutocompleteView2.setText(myloc);
                variable++;
                todragvariable = 0;
            }
            //mMap.setOnMyLocationChangeListener(null);
        }
    }
    private void requestLocationPermission() {
        if ((ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) ||
                (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION))){
            Snackbar.make(mLayout, "Permission is needed to access your location", Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(WootCabsActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
                        }
                    }).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
    }
}



   /* http://stackoverflow.com/questions/11745314/why-retrieving-google-directions-for-android-using-kml-data-is-not-working-anymo/11745316#11745316
    http://about-android.blogspot.in/2010/03/sample-google-map-driving-direction.html
    http://maps.googleapis.com/maps/api/directions/json?origin=13.0854932,77.5843767&destination=13.0654932,77.5843767%20&sensor=false&units=metric&mode=driving
*/