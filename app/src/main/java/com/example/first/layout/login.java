package com.example.first.layout;

import android.content.*;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.example.first.DBHelper;
import com.example.first.R;

public class login extends AppCompatActivity {
    private EditText Luname, Lpass;
    private TextView LError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Luname = findViewById(R.id.Luname);
        Lpass = findViewById(R.id.Lpass);
        LError = findViewById(R.id.lerror);

        TextView createacc = findViewById(R.id.createac);
        TextView frgpass = findViewById(R.id.frgpass);
        Button Lbtn = findViewById(R.id.login_btn);

        createacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login.this, regi.class);
                startActivity(i);
            }
        });

        frgpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login.this, forgotpass.class);
                startActivity(i);
            }
        });

        Lbtn.setOnClickListener(v -> checkLogin());
    }

    private void checkLogin() {
        String username = Luname.getText().toString();
        String password = Lpass.getText().toString();

        DBHelper dbHelper = new DBHelper(this);
        boolean isValid = dbHelper.checkUserCredentials(username, password);

        if (isValid) {
            handleLogin(username);

            // Proceed to the next activity or main screen
            Intent intent = new Intent(login.this, menu.class);
            startActivity(intent);
            finish(); // Optionally close the login activity
        } else {
            LError.setText("Invalid username or password");
            //Toast.makeText(login.this, "Invalid username or password", Toast.LENGTH_LONG).show();
        }
    }

    private void handleLogin(String username) {
        // Get SharedPreferences (session)
        SharedPreferences sharedPref = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // Save username
        editor.putString("username", username);
        editor.apply(); // or editor.commit() for synchronous saving
    }
}
