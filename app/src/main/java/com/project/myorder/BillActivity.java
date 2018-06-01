package com.project.myorder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import adapter.BillAdapter;
import model.OrderModel;

public class BillActivity extends AppCompatActivity {

    private Button mOkBill;
    private ImageButton btnBack;
    private ListView listView;
    private TextView txtTotal ;
    Double total = 0.0;
    int customerId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);
        getDataFromMenu();
        mOkBill = (Button) findViewById(R.id.btn_ok_bill);
        btnBack =(ImageButton)findViewById(R.id.btnBill_Back);
        txtTotal =(TextView)findViewById(R.id.txtTotal);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        listView = (ListView)findViewById(R.id.lvOrderList);
        BillAdapter adapter = new BillAdapter(getApplicationContext(), MenuActivity.orderLists);
        listView.setAdapter(adapter);
        for(int i = 0; i< MenuActivity.orderLists.size(); i++){
            total+= MenuActivity.orderLists.get(i).getAmount();
        }
        txtTotal.setText(String.format("%.0f VND",total));
        orderSuccess();
    }


    private void orderSuccess(){
        mOkBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(BillActivity.this,BookInfoActivity.class);
                bundle.putInt("CUSTOMER_ID",customerId);
                intent.putExtra("MENU_LIST",bundle);
                startActivity(intent);
            }
        });
    }
    private void getDataFromMenu(){
        Intent i = getIntent();
        customerId = i.getIntExtra("CUSTOMER_ID",0);
    }
}
