package com.dn_alan.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.dn_alan.router_annotation.Extra;
import com.dn_alan.router_annotation.Route;

import java.util.HashMap;
import java.util.Map;

@Route(path = "/main/test")
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        Map<String, String> map = new HashMap<>();
//        DNRouter$$Group$$main dnRouter$$Group$$main = new DNRouter$$Group$$main();
//        dnRouter$$Group$$main.loadInto(map);
//        Intent intent = new Intent(this, map.get(path).class);
//        startActivity(intent);
    }
}
