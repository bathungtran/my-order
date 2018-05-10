package com.project.myorder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnLogin;
    private Button btnRegister;
    private CheckBox cbRemember;
    private ProgressDialog progressDialog;
    private SharedPreferences sharedPreferences;
    private static final String LOGIN_URL= "http://35.189.23.244:8080/customer/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        sharedPreferences = getSharedPreferences("dataLogin",MODE_PRIVATE);
        edtUsername = (EditText) findViewById(R.id.edtUsername);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
        cbRemember = (CheckBox) findViewById(R.id.cbRememberMe);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.setCancelable(false);
        Intent intent = getIntent();
        boolean isLogout = intent.getBooleanExtra("Logout",false);
        if(isLogout){
            edtUsername.setText(sharedPreferences.getString("userName",""));
            cbRemember.setChecked(false);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("userName");
            editor.remove("password");
            editor.remove("remember");
            editor.commit();
        }else{
            CheckLogin();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnRegister:
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.btnLogin:
                if (isOnline()) {
                    String userName = edtUsername.getText().toString();
                    String password = edtPassword.getText().toString();
                    if (TextUtils.isEmpty(userName)) {
                        edtUsername.setError("User name can't be empty");
                        return;
                    }
                    if (TextUtils.isEmpty(password)) {
                        edtPassword.setError("Password can't be empty");
                        return;
                    }

                    Login(userName,password);
                }else {
                    Toast.makeText(getApplicationContext(),"Internet not available",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    class CallApi extends AsyncTask<JSONObject, Integer, Integer> {

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            hideDialog();
           if(s==HttpsURLConnection.HTTP_OK){
               if(cbRemember.isChecked()){
                   SharedPreferences.Editor editor = sharedPreferences.edit();
                   editor.putString("userName",edtUsername.getText().toString());
                   editor.putString("password",edtPassword.getText().toString());
                   editor.putBoolean("remember",cbRemember.isChecked());
                   editor.commit();
               }else {
                   SharedPreferences.Editor editor = sharedPreferences.edit();
                   editor.remove("userName");
                   editor.remove("password");
                   editor.remove("remember");
                   editor.commit();
               }
               Intent intent =new Intent(getApplicationContext(),RestaurantActivity.class);
               intent.putExtra("USERNAME",edtUsername.getText().toString());
               startActivity(intent);
               finish();
           }else
           {
               Toast.makeText(getApplicationContext(),"Login fail!",Toast.LENGTH_SHORT).show();
           }
        }

        @Override
        protected Integer doInBackground(JSONObject... body) {
            int result=0;
            try {
                final HttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 10000);
                HttpClient client = new DefaultHttpClient(httpParams);
                HttpPost post = new HttpPost(LOGIN_URL);
                StringEntity se = new StringEntity(body[0].toString());
                se.setContentEncoding("UTF-8");
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

    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    private void Login(String userName, String password){
        JSONObject json = new JSONObject();
        try {
            json.put("userName", userName);
            json.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        showDialog();
        new CallApi().execute(json);
    }
    private void CheckLogin(){
        String userName =sharedPreferences.getString("userName","");
        String password =sharedPreferences.getString("password","");
        cbRemember.setChecked(sharedPreferences.getBoolean("remember",false));
        edtUsername.setText(userName);
        edtPassword.setText(password);
        if(!userName.equals("") && !password.equals("")){
            Login(userName,password);
        }

    }
}