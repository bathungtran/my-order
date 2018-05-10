package com.project.myorder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.RestaunrantAdapter;
import model.CustomerModel;
import model.RestaurantModel;
import utils.DataParser;
import utils.DownloadURL;
import utils.GPSTracker;

public class RestaurantActivity extends Activity {
    ProgressBar progressBar;
    SearchView searchView;
    Toolbar toolbar;
    RecyclerView recyclerView;
    TextView txtHelloUser;
    Switch swNearBy;
    GPSTracker gpsTracker;
    int PROXIMITY_RADIUS = 2000;
    double latitude, longitude;
    private List<HashMap<String, String>> nearbyPlaceList;
    private List<RestaurantModel> restaurantModelList;
    String user;
    int customerId;
    NetworkReceiver receiver;
    private static String serverURL="http://35.189.23.244:8080/";
    private static String API_KEY="AIzaSyBZ8xvu_-TXtnME5l40ACe_A3UwMzQw-64";
    private static String ORDER_RECORDS = "http://35.189.23.244:8080/customer/customers";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        receiver = new NetworkReceiver();
        this.registerReceiver(receiver, filter);
        InitComponent();
        InitView();
    }

    private void InitComponent() {
        searchView = findViewById(R.id.sevMain_Restaurant);
        progressBar = findViewById(R.id.prbLoadData);
        // Get text view id of search widget
        int id = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        // Set search text color
        textView.setTextColor(Color.WHITE);
        textView.setHintTextColor(Color.GRAY);
        toolbar = findViewById(R.id.toolbarMain);
        recyclerView = findViewById(R.id.revRestaurant);
        txtHelloUser = findViewById(R.id.txtMain_HelloUser);
        final Intent intent = getIntent();
        user = intent.getStringExtra("USERNAME");
        txtHelloUser.setText(user);
        txtHelloUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowLogoutDialog();
            }
        });

        swNearBy = findViewById(R.id.sw_near_by);
        swNearBy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                LoadData(b);
            }
        });
        new CustomerId().execute(ORDER_RECORDS+"/"+user);

    }

    private void InitView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoratio = new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.restaurant_item_divider);
        dividerItemDecoratio.setDrawable(drawable);
        recyclerView.addItemDecoration(dividerItemDecoratio);
        LoadData(false);


    }

    private String NearByURL(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location=" + latitude + "," + longitude);
        googlePlaceUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type=" + "restaurant");
        googlePlaceUrl.append("&keyword=" + nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key=" + API_KEY);

        Log.d("MapsActivity", "url = " + googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }


    public class GetNearbyPlacesData extends AsyncTask<String, String, String> {
        private String googlePlacesData;
        String url;

        @Override
        protected String doInBackground(String... strings) {
            url = strings[0];
            DownloadURL downloadURL = new DownloadURL();
            try {
                googlePlacesData = downloadURL.readUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return googlePlacesData;
        }

        @Override
        protected void onPostExecute(String s) {
            DataParser parser = new DataParser();
            nearbyPlaceList = new ArrayList<>();
            nearbyPlaceList = parser.parse(s);
            ShowListRestaurant(true);
        }

    }

    public class LoadRestaurantData extends AsyncTask<String, String, String> {
        Boolean nearBy;

        public LoadRestaurantData(Boolean nearBy) {
            this.nearBy = nearBy;
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = (String) strings[0];
            DownloadURL downloadURL = new DownloadURL();
            String response = null;
            try {
                response = downloadURL.readUrl(url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            restaurantModelList = new ArrayList<>();
                Gson gson = new Gson();
                Type listType = new TypeToken<List<RestaurantModel>>(){}.getType();
                restaurantModelList =  gson.fromJson(s, listType);
                    if (nearBy) {
                        gpsTracker = new GPSTracker(getApplicationContext());
                        if (gpsTracker.canGetLocation()) {
                            latitude = gpsTracker.getLatitude();
                            longitude = gpsTracker.getLongitude();
                            String NEAR_URL = NearByURL(latitude, longitude, "Nhà+Hàng");
                            new GetNearbyPlacesData().execute(NEAR_URL);
                        } else {
                            swNearBy.setChecked(false);
                            ShowRequestLocationDialog();
                        }
                    } else {
                        ShowListRestaurant(nearBy);
                    }

        }

    }


    public void LoadData(boolean nearBy) {
        if (isOnline()) {
            progressBar.setVisibility(View.VISIBLE);
            String RES_DATA_URL = serverURL+"api/restaurants";
            new LoadRestaurantData(nearBy).execute(RES_DATA_URL);
        } else {
            Toast.makeText(getApplicationContext(), "Internet not available", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public class NetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isOnline()) {
                LoadData(swNearBy.isChecked());
            }

        }
    }

    private void ShowLogoutDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantActivity.this);
        builder.setMessage("Do you want to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);
                        intent1.putExtra("Logout",true);
                        startActivity(intent1);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setTitle("Confirm logout...")
                .show();
    }

    private void ShowRequestLocationDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantActivity.this);
        builder.setMessage("Application want to access your location")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent1 = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(intent1);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                })
                .setTitle("Request location...")
                .show();
    }

    private void ShowIncreaseRadiusDialog(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantActivity.this);
        builder.setMessage("Do you want to increase search radius?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PROXIMITY_RADIUS += 500;
                        LoadData(true);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        swNearBy.setChecked(false);
                        dialog.cancel();
                    }
                })
                .setTitle("Can't find any restaurant...")
                .show();
    }

    private void ShowListRestaurant(boolean nearBy) {
        List<RestaurantModel> listRestaurant = new ArrayList<>();
        if (nearBy) {
            if (!nearbyPlaceList.isEmpty()) {
                for (RestaurantModel model : restaurantModelList) {
                    for (int i = 0; i < nearbyPlaceList.size(); i++) {
                        if (model.getResName().equals(nearbyPlaceList.get(i).get("place_name"))) {
                            listRestaurant.add(model);
                            break;
                        }
                    }
                }
            }
        } else {
            listRestaurant = restaurantModelList;
        }
        progressBar.setVisibility(View.GONE);
        if (!listRestaurant.isEmpty()) {
            // create new adapter for recyclerview and set adapter for view
            final RestaunrantAdapter restaunrantAdapter = new RestaunrantAdapter(
                    listRestaurant, getApplicationContext(),customerId);
            recyclerView.setAdapter(restaunrantAdapter);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    restaunrantAdapter.getFilter().filter(newText);
                    return false;
                }
            });
        } else {
            ShowIncreaseRadiusDialog();
        }
    }
    public class CustomerId extends AsyncTask<String,String,String>{

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            CustomerModel customerModel = new Gson().fromJson(s,CustomerModel.class);
            customerId = customerModel.getCustomerId();
            Log.i("CUSTOMER_ID", String.valueOf(customerId));
        }

        @Override
        protected String doInBackground(String... strings) {
            DownloadURL downloadURL = new DownloadURL();
            String response = null;
            try {
                response = downloadURL.readUrl(ORDER_RECORDS+"/"+user);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response;
        }
    }
}
