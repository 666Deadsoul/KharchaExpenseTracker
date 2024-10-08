package com.example.first.layout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.TextUtils;
import androidx.appcompat.app.AppCompatActivity;
import com.example.first.DBHelper;
import com.example.first.R;


public class forgotpass extends AppCompatActivity {
EditText usernameField, passwordField, confirmPasswordField;
TextView Ferror;
Button reset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);

        TextView backtol = findViewById(R.id.backtologin);

        backtol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(forgotpass.this, login.class);
                startActivity(i);
            }
        });
        usernameField = findViewById(R.id.Funame);
        passwordField = findViewById(R.id.Fpass);
        confirmPasswordField = findViewById(R.id.FCpass);
        reset = findViewById(R.id.Freset);
        Ferror = findViewById(R.id.ferror);

        reset.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        String username = usernameField.getText().toString();
        String newPassword = passwordField.getText().toString();
        String confirmPassword = confirmPasswordField.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Ferror.setText("All fields are required");
            return;
        }
        if (newPassword.length() < 8) {
            Ferror.setText("Password must be at least 8 characters");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            Ferror.setText("Passwords do not match");
            return;
        }

        DBHelper dbHelper = new DBHelper(this);
        boolean isUpdated = dbHelper.updatePassword(username, newPassword);

        if (isUpdated) {
            Toast.makeText(forgotpass.this, "Password reset successful", Toast.LENGTH_LONG).show();
            Intent i = new Intent(forgotpass.this, login.class);
            startActivity(i);
        } else {
            Ferror.setText("Username not found");
        }
    }
}