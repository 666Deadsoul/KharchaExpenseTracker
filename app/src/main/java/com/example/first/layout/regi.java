package com.example.first.layout;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import com.example.first.ConnDB;
import com.example.first.api.ConnectionClass;
import com.example.first.DBHelper;
import com.example.first.R;

import java.sql.Connection;
import java.sql.ResultSet;


public class regi extends AppCompatActivity {

    ConnectionClass conClass;
    Connection conn;
    ResultSet rs;
    String name, str;

    TextView te, Rerror;
    EditText fname, uname, pass, email;
    Button create;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regi);

        TextView createacc = findViewById(R.id.acc);
        fname = findViewById(R.id.fname);
        uname = findViewById(R.id.uname);
        pass = findViewById(R.id.pass);
        email = findViewById(R.id.email);
        create = findViewById(R.id.Rbtn);
        Rerror = findViewById(R.id.Rerror);

        createacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(regi.this, login.class);
                startActivity(i);
            }
        });


        create.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String fullname = fname.getText().toString();
        String username = uname.getText().toString();
        String password = pass.getText().toString();
        String emailaa = email.getText().toString();
        // Input validation
        if (fullname.isEmpty() || username.isEmpty() || password.isEmpty() || emailaa.isEmpty()) {
            Rerror.setText("All fields are required");
            //Toast.makeText(this, "All fields are required", Toast.LENGTH_LONG).show();
            return;
        }
        if (!fullname.matches("[a-zA-Z ]+")) {
            Rerror.setText("Full name should not contain numbers or symbols");
            //Toast.makeText(this, "Full name should not contain numbers", Toast.LENGTH_LONG).show();
            return;
        }
        if (fullname.length() > 20) {
            Rerror.setText("Full name cannot be that long");
            //Toast.makeText(this, "Full name should not contain numbers", Toast.LENGTH_LONG).show();
            return;
        }
        // Email validation
        if (!Patterns.EMAIL_ADDRESS.matcher(emailaa).matches()) {
            Rerror.setText("Invalid email address");
            //Toast.makeText(this, "Invalid email address", Toast.LENGTH_LONG).show();
            return;
        }

        if (password.length() < 8) {
            Rerror.setText("Password must be at least 8 characters");
            //Toast.makeText(this, "Password must be at least 8 characters", Toast.LENGTH_LONG).show();
            return;
        }

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM user WHERE uname = ?", new String[]{username});
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();

        if (count > 0) {
            Toast.makeText(this, "Username already exists", Toast.LENGTH_LONG).show();
            return;
        }

        try {

            boolean isInserted = dbHelper.insertUser(fullname, username, password, emailaa);
            if (isInserted) {
                Toast.makeText(regi.this, "Successfully saved", Toast.LENGTH_LONG).show();
                fname.setText("");
                uname.setText("");
                pass.setText("");
                email.setText("");
                Intent i = new Intent(regi.this, login.class);
                startActivity(i);
            } else {
                Toast.makeText(regi.this, "Failed to Register", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(regi.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


}
