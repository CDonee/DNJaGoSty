package com.dn_alan.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dn_alan.service.DNAIdl;
import com.dn_alan.service.Person;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DNAIdl dnaIdl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindService();
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(
                "com.dn_alan.service",
                "com.dn_alan.service.DNAidlService"));
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            dnaIdl = DNAIdl.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    public void click(View view) {
        try {
            dnaIdl.addPerson(new Person("dn", 10));
            List<Person> people = dnaIdl.getPersonList();
            Toast.makeText(this, people.toString(), Toast.LENGTH_SHORT).show();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
