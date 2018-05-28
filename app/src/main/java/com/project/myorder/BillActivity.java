package com.project.myorder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import adapter.BillAdapter;
import model.OrderModel;

public class BillActivity extends AppCompatActivity {

    private Button mOkBill;
    private ImageButton btnBack;
    private ListView listView;
    private TextView txtTotal ;
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
        saveOrdered(MenuActivity.orderLists);
        Double total =0.0;
        for(int i = 0; i< MenuActivity.orderLists.size(); i++){
            total+= MenuActivity.orderLists.get(i).getAmount();
        }
        txtTotal.setText(String.format("%.0f VND",total));
        orderSuccess();
    }

    private void saveOrdered(List<OrderModel> saveOrderModel) {
        SharedPreferences prefs = getSharedPreferences("ORDERED",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String ordered = gson.toJson(saveOrderModel);
        Log.i("ORDER_SAVE",ordered);
        editor.putString("ORDERED",ordered);
        editor.commit();

    }

    private void orderSuccess(){
        mOkBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BillActivity.this,BookInfoActivity.class);
                intent.putExtra("CUSTOMER_ID",customerId);
                startActivity(intent);
            }
        });
    }
    private void getDataFromMenu(){
        Intent i = getIntent();
        customerId = i.getIntExtra("CUSTOMER_ID",0);
    }
}
