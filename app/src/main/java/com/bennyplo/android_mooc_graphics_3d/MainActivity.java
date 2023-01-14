package com.bennyplo.android_mooc_graphics_3d;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private MyView mMyView=null;//a custom view for drawing
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        getSupportActionBar().hide();//hide the title bar
        mMyView=new MyView(this);
        setContentView(mMyView);
    }
}
