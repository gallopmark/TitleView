package com.holike.titleview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;

import pony.xcode.widget.TitleBar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TitleBar titleBar = findViewById(R.id.titleBar);
        setSupportActionBar(titleBar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_more,menu);
        return super.onCreateOptionsMenu(menu);
    }
}