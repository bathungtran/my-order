package com.project.myorder;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;

public class RegisterActivity extends Activity {
    private EditText inputName, inputPhone, inputPassword, inputRePassword;
    private Button btnLogin;
    private Button btnRegister;
    private ProgressDialog progressDialog;
    private static final  String REGISTER_URL="http://35.189.23.244:8080/customer/create";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);

        inputName = (EditText) findViewById(R.id.edt_name_reg);
        inputPhone = (EditText) findViewById(R.id.edt_phone_reg);
        inputPassword = (EditText) findViewById(R.id.edt_password_reg);
        inputRePassword = (EditText) findViewById(R.id.edt_re_password_reg);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLogin = (Button) findViewById(R.id.btnLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()){
                    submitForm();
                }else {
                    Toast.makeText(getApplicationContext(),"Internet not available",Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void submitForm() {
        String name = inputName.getText().toString();
        if(TextUtils.isEmpty(name)){
            inputName.setError("Please enter your username!");
            return;
        }
        String phone=inputPhone.getText().toString();
        if(TextUtils.isEmpty(phone)){
            inputPhone.setError("Please enter your phone");
            return;
        }
        String password=inputPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            inputPassword.setError("Please enter your password");
            return;
        }
        String rePassword=inputRePassword.getText().toString();
        if(TextUtils.isEmpty(rePassword)){
            inputRePassword.setError("Please enter confirm password");
            return;
        }else if(!password.equals(rePassword)){
            inputRePassword.setError("Confirm password not match");
            return;
        }

        registerUser(name,phone,password,rePassword);
    }

    private void registerUser(final String name, final String phone, final String password, final String repassword) {
        progressDialog.setMessage("Register...");
        JSONObject json = new JSONObject();
        try {
            json.put("userName", name);
            json.put("password", password);
            json.put("name", name);
            json.put("phoneNumber", phone);
            json.put("gender", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        showDialog();
        new CallApi().execute(json);
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

    class CallApi extends AsyncTask<JSONObject, Integer, Integer> {

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);
            hideDialog();
            if(s==HttpsURLConnection.HTTP_CREATED){
                Toast.makeText(getApplicationContext(),"Register succesfully!",Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
                finish();
            }else{
                Toast.makeText(getApplicationContext(),"Register fail. Try agian!",Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected Integer doInBackground(JSONObject... body) {
            int result=0;
            try {
                HttpClient client = new DefaultHttpClient();
                HttpPost post = new HttpPost(REGISTER_URL);
                StringEntity se = new StringEntity(body[0].toString());
                se.setContentEncoding("UTF-8");
                se.setContentType("application/json");
                post.setEntity(se);
                HttpResponse response = client.execute(post);
                 result = response.getStatusLine().getStatusCode();
                Log.e("respones", result+"");

            } catch (Exception e) {
                return 0;
            }
            return result;
        }
    }
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
