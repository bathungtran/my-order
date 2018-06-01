package com.project.myorder;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class StatusOrderActivity extends FragmentActivity {
    private static String TAG="StatusOrderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status_order);
//        if (savedInstanceState == null){
//            Log.i(TAG, "onCreate: ");
//            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.frame_container,new StatusOrderFragment());
//            ft.addToBackStack(null);
//            ft.commit();
//        }

    }

}
