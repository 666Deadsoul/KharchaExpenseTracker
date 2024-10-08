package com.example.first.layout;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.first.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class menu extends AppCompatActivity {

    BottomNavigationView b;
    homeFragment h = new homeFragment();
    logsFragment l = new logsFragment();
    plusFragment p= new plusFragment();
    statsFragment st = new statsFragment();
    settingsFragment se = new settingsFragment();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        b = findViewById(R.id.bottom);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, h).commit(); //replace frame layout to container when opening app
        b.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId= item.getItemId();
                    if (itemId == R.id.home) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, h).commit();
                        return true;
                    } else if (itemId == R.id.logs) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, l).commit();
                        return true;
                    } else if (itemId == R.id.plus) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, p).commit();
                        return true;
                    } else if (itemId == R.id.stats) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, st).commit();
                        return true;
                    } else if (itemId == R.id.settings) {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, se).commit();
                        return true;
                    } else {
                        return false;
                    }
            }
        });
    }
}