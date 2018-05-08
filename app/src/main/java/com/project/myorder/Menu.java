package com.project.myorder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import adapter.MenuAdapter;
import model.FoodModel;
import model.OrderModel;
import utils.DownloadURL;


public class Menu extends AppCompatActivity {

    private List<FoodModel> data = new ArrayList<FoodModel>();
    public static List<OrderModel> orderLists = new ArrayList<>();
    Toolbar toolbar;
    RecyclerView recyclerView;
    public static int RES_ID=0;
    private static String serverURL="http://35.189.23.244:8080/";
    int customerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        //TODO: Set restaurant name here

        recyclerView = (RecyclerView) findViewById(R.id.menu_recyclerView);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        getDataFromHome();
        orderLists.clear();
    }

    private void getDataFromHome() {
        Intent i = getIntent();
        String resName = i.getStringExtra("RES_NAME");
        RES_ID = i.getIntExtra("RES_ID",0);
        customerId = i.getIntExtra("CUSTOMER_ID",0);
        System.out.println("RES_NAME: "+resName);
        System.out.println("RES_ID: "+RES_ID);
        getSupportActionBar().setTitle(resName);
        LoadData();

    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_sceen_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == R.id.done) {
            if(orderLists.isEmpty())
            {
                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Menu.this);
                builder.setMessage("Oops! Your cart is empty!")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .show();
                return true;
            }
            Intent i = new Intent(this,BillActivity.class);
            i.putExtra("CUSTOMER_ID",customerId);
            startActivity(i);
        }
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    private void LoadData(){
        if(isOnline()) {
            final String FOOD_URL = serverURL+"food/restaurants/" + RES_ID + "/foods";
            Object object[] = new Object[1];
            object[0] = FOOD_URL;
            LoadFoodData loadFoodData = new LoadFoodData();
            loadFoodData.execute(object);
        }else{
            Toast.makeText(getApplicationContext(),"Internet not available",Toast.LENGTH_LONG).show();
        }
    }
    public class LoadFoodData extends AsyncTask<Object,String,String> {
        @Override
        protected String doInBackground(Object... objects) {
            String url =(String) objects[0];
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
            Gson gson = new Gson();
            Type listType = new TypeToken<List<FoodModel>>(){}.getType();
            data = gson.fromJson(s, listType);

            if (!data.isEmpty()) {
                MenuAdapter menuRecyclerViewAdapter = new MenuAdapter(data,getApplicationContext());
                recyclerView.setAdapter(menuRecyclerViewAdapter);

                }

        }
    }
}
