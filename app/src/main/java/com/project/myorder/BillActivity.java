package com.project.myorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import adapter.BillAdapter;

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
        BillAdapter adapter = new BillAdapter(getApplicationContext(),Menu.orderLists);
        listView.setAdapter(adapter);

        Double total =0.0;
        for(int i=0;i<Menu.orderLists.size();i++){
            total+=Menu.orderLists.get(i).getAmount();
        }
        txtTotal.setText(total+"");
        orderSuccess();
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
