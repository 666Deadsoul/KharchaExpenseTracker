package com.example.first.layout;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.first.DBHelper;
import com.example.first.R;
//import com.example.first.login; // Adjust import according to your package structure

public class settingsFragment extends Fragment {

    private static final String PREFS_NAME = "UserSession"; // Updated to match your stored preferences name
    private DBHelper dbHelper; // Declare dbHelper

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize the DBHelper
        dbHelper = new DBHelper(getActivity()); // Use getActivity() to provide context

        // Access the logout button
        Button logoutButton = view.findViewById(R.id.button_logout);
        logoutButton.setOnClickListener(v -> logout());

        Button editUserButton = view.findViewById(R.id.button_about);
        editUserButton.setOnClickListener(v -> showEditUserDialog());

        return view;
    }

    private void logout() {
        // Clear SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear(); // Clear all data
        editor.apply(); // Commit changes
        Toast.makeText(getContext(), "Logout Successfully", Toast.LENGTH_LONG).show();
        // Redirect to LoginActivity
        Intent intent = new Intent(getActivity(), login.class);
        startActivity(intent);
        requireActivity().finish(); // Finish the current activity to remove it from the back stack
    }

    private void showEditUserDialog() {
        // Retrieve the current username from SharedPreferences
        SharedPreferences sharedPref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", null);

        // Create a LinearLayout to hold the EditTexts and error label
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);

        // Create EditTexts for full name and email
        EditText editTextFullName = new EditText(getContext());
        editTextFullName.setHint("Full Name");

        EditText editTextEmail = new EditText(getContext());
        editTextEmail.setHint("Email Address");

        // Create a TextView for displaying error messages
        TextView errorLabel = new TextView(getContext());
        errorLabel.setTextColor(Color.RED); // Set text color to red for errors
        errorLabel.setVisibility(View.GONE); // Initially hidden

        // Add EditTexts and the error label to the layout
        layout.addView(editTextFullName);
        layout.addView(editTextEmail);
        layout.addView(errorLabel);

        // Create the AlertDialog
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Edit Profile")
                .setView(layout)
                .setPositiveButton("Set", null) // Set null to override the click listener
                .setNegativeButton("Cancel", null)
                .create();

        dialog.show(); // Show the dialog

        // Set the click listener for the "Set" button
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String fullName = editTextFullName.getText().toString().trim();
            String email = editTextEmail.getText().toString().trim();

            // Reset error message visibility
            errorLabel.setVisibility(View.GONE);

            // Validate input
            if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email)) {
                errorLabel.setText("All fields are required");
                errorLabel.setVisibility(View.VISIBLE); // Show error
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                errorLabel.setText("Invalid email address");
                errorLabel.setVisibility(View.VISIBLE); // Show error
            }else if (!fullName.matches("[a-zA-Z\\s]+")) { // Regex to allow only letters and spaces
            errorLabel.setText("Full name must not contain numbers or symbols");
            errorLabel.setVisibility(View.VISIBLE); // Show error
            } else if (fullName.length() < 1 || fullName.length() > 20) { // Check length range
            errorLabel.setText("Full name must be between 1 and 20 characters");
            errorLabel.setVisibility(View.VISIBLE);
            }else {
                // Update user information in the database
                dbHelper.updateUser(fullName, username, email);
                Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }
}
