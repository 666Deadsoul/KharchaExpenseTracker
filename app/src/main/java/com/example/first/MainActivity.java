package com.example.first;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.first.layout.homeFragment;
import com.example.first.layout.login;
import com.example.first.layout.logsFragment;
import com.example.first.layout.plusFragment;
import com.example.first.layout.settingsFragment;
import com.example.first.layout.statsFragment;

public class MainActivity extends AppCompatActivity {
    homeFragment hf;
    logsFragment lf;
    plusFragment pf;
    statsFragment sf;
    settingsFragment setf;
    Spinner s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent); // Correct method to start a single activity
                finish();
            }
        }, 1500);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new logsFragment())
                    .commit();
        }


    }
}




