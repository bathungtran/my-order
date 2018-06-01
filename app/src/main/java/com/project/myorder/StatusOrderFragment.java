package com.project.myorder;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import adapter.StatusAdapter;
import model.StatusModel;
import utils.DownloadURL;

import static android.content.ContentValues.TAG;

/**
 * Created by harry on 31/05/2018.
 */
public class StatusOrderFragment extends android.support.v4.app.Fragment {
    private RecyclerView recyclerView;
    List<StatusModel> list = new ArrayList<>();
    private static String TAG = "StatusOrderFragment";
    Context context;
    int cusId;
    private String URL = "http://35.189.23.244:8080/order/orders";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_status_order,container,false);
        recyclerView = view.findViewById(R.id.rec_status_order);
        recyclerView.setHasFixedSize(true);
        context = view.getContext();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoratio = new DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.restaurant_item_divider);
        recyclerView.addItemDecoration(dividerItemDecoratio);
        dividerItemDecoratio.setDrawable(drawable);

        return  view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //connectToView(view);
        new GetStatusInfo().execute(URL);
        Log.i(TAG, "onViewCreated: ");
        RestaurantActivity restaurantActivity = new RestaurantActivity();
        cusId = restaurantActivity.getCustomerId();

    }

    private void connectToView(View v) {
        recyclerView = v.findViewById(R.id.rec_status_order);
        context = v.getContext();


    }
    public class GetStatusInfo extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            DownloadURL downloadURL = new DownloadURL();
            String data="";
            try {
                data=downloadURL.readUrl(URL);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray array = new JSONArray(s);
                for(int i=0;i<array.length();i++){

                    JSONObject object = array.getJSONObject(i);
                    int id = object.getInt("customerId");
                    String time = object.getString("date");
                    long timeStamp = Long.parseLong(time);
                    Date date = new Date(timeStamp);
                    String foodOrder = object.getString("foodOrder");
                    if(cusId == id){
                        StatusModel model = new StatusModel(date.toString(),foodOrder,1);
                        Log.i("DATA FRAGMENT","FOOD: "+ model.getTime()+
                                    " ID: "+id);
                        list.add(model);
                    }

                }
                StatusAdapter statusAdapter = new StatusAdapter(context,list);
                recyclerView.setAdapter(statusAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
