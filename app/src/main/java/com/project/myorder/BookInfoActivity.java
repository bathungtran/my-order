package com.project.myorder;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

import model.OrderModel;

public class BookInfoActivity extends AppCompatActivity {
    private Button order;
    private EditText editName;
    private EditText editPhone;
    private EditText editAddress;
    private EditText editNote;
    private ImageButton btnBack;
    private ProgressDialog progressDialog;
    int customerId;
    private static final String ORDER_URL="http://35.189.23.244:8080/order/record";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        getDataFromBill();
        order = findViewById(R.id.btn_info_order);
        editName = findViewById(R.id.edt_info_name);
        editPhone = findViewById(R.id.edt_info_phone);
        editAddress = findViewById(R.id.edt_info_address);
        editNote = findViewById(R.id.edt_info_note);
        btnBack = findViewById(R.id.btn_info_back);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCancelable(false);

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void getDataFromBill(){
        Intent i = getIntent();
        customerId = i.getIntExtra("CUSTOMER_ID",0);
        Log.i("CUSTOMER_ID", String.valueOf(customerId));
    }
    private void Order() {
       String name = editName.getText().toString();
       if(TextUtils.isEmpty(name)){
           editName.setError("Please enter your name!");
           return;
       }
       String phone = editPhone.getText().toString();
       if(TextUtils.isEmpty(phone)){
           editPhone.setError("Please enter your phone number!");
           return;
       }
      String address = editAddress.getText().toString();
       if(TextUtils.isEmpty(address)){
           editAddress.setError("Please enter your address!");
           return;
       }
       String note = editNote.getText().toString();
        if(isOnline()){
               String listFoodName="";
               for(OrderModel model: MenuActivity.orderLists){
                   listFoodName+=model.getFoodName()+",";
               }
                JSONObject object = new JSONObject();
                try {
                    object.put("customer",name);
                    object.put("address",address);
                    object.put("foodOrder",listFoodName);
                    object.put("phoneNumber",phone);
                    object.put("resOrder", MenuActivity.RES_ID);
                    object.put("customerId",customerId);
                } catch (JSONException e) {
                    e.printStackTrace();

            }
            showDialog();
            new CallApi().execute(object);
        }
    }

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    class CallApi extends AsyncTask<JSONObject, Integer, Integer> {

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            hideDialog();
            if(s==HttpsURLConnection.HTTP_CREATED){
                final AlertDialog.Builder builder = new android.app.AlertDialog.Builder(BookInfoActivity.this);
                builder.setMessage("Order successfully!")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                editAddress.setText("");
                                editName.setText("");
                                editPhone.setText("");
                                editNote.setText("");
                                dialog.cancel();
                            }
                        })
                        .show();
            }else {
                Toast.makeText(getApplicationContext(),"Order fail! Try again",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Integer doInBackground(JSONObject... body) {
            int result=0;
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(ORDER_URL);
                StringEntity se = new StringEntity(body[0].toString(),"UTF-8");
                se.setContentType("application/json");
                post.setEntity(se);
                HttpResponse response = client.execute(post);
                result = response.getStatusLine().getStatusCode();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }
    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

}
