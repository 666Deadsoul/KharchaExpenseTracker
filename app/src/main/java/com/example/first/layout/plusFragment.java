package com.example.first.layout;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.*;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.ParseException;
import com.example.first.DBHelper;
import com.example.first.R;

import java.util.Date;
import java.util.Locale;

public class plusFragment extends Fragment {
    private TextView logItem, logAmount, Perror;
    private AutoCompleteTextView ac_spinner;
    private EditText editTextSelectDate;
    private Button add;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_plus, container, false);

        ac_spinner = rootView.findViewById(R.id.ac_spinner);
        logItem = rootView.findViewById(R.id.item);
        logAmount = rootView.findViewById(R.id.amt);
        add = rootView.findViewById(R.id.Rbtn);
        Perror = rootView.findViewById(R.id.Perror);

        setupAutoComplete();

        add.setOnClickListener(v -> saveLog());

        editTextSelectDate = rootView.findViewById(R.id.editTextSelectDate);

        editTextSelectDate.setOnClickListener(v -> showDatePickerDialog());
        return rootView;
    }

    private void setupAutoComplete() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getActivity(),
                R.array.expense_categories,
                android.R.layout.simple_dropdown_item_1line
        );

        ac_spinner.setAdapter(adapter);
        ac_spinner.setThreshold(1); // Start showing suggestions after typing 1 character

        ac_spinner.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);
            Log.v("Selected Item", selectedItem);
        });
    }
    //datepicker
    private void showDatePickerDialog() {
        // Get the current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Date selected by the user
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);

                    // Validate the selected date
                    if (validateDate(selectedDate)) {
                        // Use String.format to ensure leading zeros for month and day
                        String formattedDate = String.format("%04d-%02d-%02d", selectedYear, (selectedMonth + 1), selectedDay);
                        editTextSelectDate.setText(formattedDate);
                    } else {
                        Toast.makeText(getContext(), "Invalid Date Selected", Toast.LENGTH_SHORT).show();
                    }
                },
                year, month, day);

        datePickerDialog.show();
    }

    // Validate the date
    private boolean validateDate(Calendar selectedDate) {
        Calendar currentDate = Calendar.getInstance();
        return !selectedDate.after(currentDate);  // Check if selected date is in the past or present
    }
    //logsaving
    private void saveLog() {
        if (getContext() == null) {
            Toast.makeText(getContext(), "Context is null", Toast.LENGTH_LONG).show();
            return;
        }

        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", null);

        String category = ac_spinner.getText().toString();
        String item = logItem.getText().toString();
        String amountStr = logAmount.getText().toString();
        String selectedDateValue = editTextSelectDate.getText().toString();

        if (category.isEmpty() || item.isEmpty() || amountStr.isEmpty()) {
            Perror.setText("Please fill all fields");
            //Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
            return;
        }
        if (!item.matches("[a-zA-Z ]+")) {
            Perror.setText("Item must contain only letters");
            return;
        }

        // Parse the selected date from String to Calendar
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-M-d", Locale.getDefault()); // Input format without leading zeroes
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // Output format with leading zeroes

        Calendar selectedDate = Calendar.getInstance();
        try {
            // Parse the date using the input format (this handles single digit days/months)
            Date date = inputFormat.parse(selectedDateValue);

            // Convert to the desired output format (which ensures leading zeros)
            String formattedDate = outputFormat.format(date);

            // Now set the calendar with the correctly formatted date
            selectedDate.setTime(outputFormat.parse(formattedDate));

        } catch (ParseException e) {
            Perror.setText("Invalid date format");
            return;
        }

        Calendar currentDate = Calendar.getInstance();


        // Check if the selected date is after the current date
        if (selectedDate.after(currentDate)) {
            Perror.setText("Please select a valid date");
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            Perror.setText("Invalid amount format");
            //Toast.makeText(getContext(), "Invalid amount format", Toast.LENGTH_LONG).show();
            return;
        }

        if (username != null) {
            DBHelper dbHelper = new DBHelper(getContext());
            boolean success = dbHelper.insertLogs(username, category, item, amount, selectedDateValue);

            if (success) {
                Perror.setText("");
                Toast.makeText(getContext(), "Log saved successfully", Toast.LENGTH_LONG).show();

                // Clear the input fields
                ac_spinner.setText("");
                logItem.setText("");
                logAmount.setText("");
                editTextSelectDate.setText("");
            } else {
                Perror.setText("Error saving log");
                //Toast.makeText(getContext(), "Error saving log", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_LONG).show();
        }
    }
}

