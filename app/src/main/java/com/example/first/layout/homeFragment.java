package com.example.first.layout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.first.DBHelper;
import com.example.first.R;

public class homeFragment extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false); // Inflate the layout for this fragment

        DBHelper dbHelper = new DBHelper(getActivity());   // Initialize DBHelper

        // Retrieve username from SharedPreferences (session)
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);

        String fullName = dbHelper.getFullNameByUsername(username);

        TextView fullNameTextView = view.findViewById(R.id.userName);
        if (fullName != null) {
            fullNameTextView.setText(fullName);
        } else {
            Toast.makeText(getContext(), "Username Not Found", Toast.LENGTH_LONG).show();
        }

        Button buttonGoal = view.findViewById(R.id.goal);
        buttonGoal.setOnClickListener(v -> showInputDialog());
        displayGoalAndBudget(username);

        Button buttonShowAlert = view.findViewById(R.id.predictButton);
        buttonShowAlert.setOnClickListener(v -> {
            double predictedSpending = predictSpendingFromRecentTransactions(username);  // predicted spending from recent transactions
            double budget =calculateDailySpending(username);   // daily spending limit calculation

            if (predictedSpending > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                String message = String.format("Predicted Spending: %.1f\nEstimated Daily Limit: %.2f\nPlease stay within your budget!", predictedSpending, budget);
                builder.setMessage(message)
                        .setCancelable(false)  // Prevent dismissal by tapping outside the dialog
                        .setPositiveButton("OK", (dialog, which) -> {
                            dialog.dismiss();  // Dismiss the dialog when "OK" is clicked
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        return view;
    }
    //daily spending
    public double calculateDailySpending(String username) {
        DBHelper dbHelper = new DBHelper(getContext());
        double budget = dbHelper.getBudget(username);

        Calendar calendar = Calendar.getInstance();
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        return budget / daysInMonth;
    }

    //Linear Regression algorithm for prediction
    private double predictSpendingFromRecentTransactions(String username) {
        if (username == null) {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
            return 0;
        }

        DBHelper dbHelper = new DBHelper(getContext());
        Cursor cursor = dbHelper.getDailyTransactionSums(username); //returns the total amount grouped by date

        List<Double> amounts = new ArrayList<>();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") double totalAmount = cursor.getDouble(cursor.getColumnIndex("totalAmount"));
                amounts.add(totalAmount);
            }
            cursor.close();
        }

        if (amounts.size() < 3) {
            Toast.makeText(getContext(), "Not enough data to predict spending", Toast.LENGTH_SHORT).show();
            return 0;
        }

        double[] coefficients = calculateLinearRegression(amounts);   // Calculate linear regression coefficients

        return predictNextSpending(coefficients);
    }

    private double[] calculateLinearRegression(List<Double> recentTransactions) {
        int n = recentTransactions.size();
        if (n == 0) {
            return new double[]{0, 0}; // No data
        }

        // Calculate means
        double sumX = 0;
        double sumY = 0;
        double sumXY = 0;
        double sumX2 = 0;

        for (int i = 0; i < n; i++) {
            double x = i; // x is the index of the transaction
            double y = recentTransactions.get(i);

            sumX += x;
            sumY += y;
            sumXY += x * y;
            sumX2 += x * x;
        }

        // Calculate slope (m) and intercept (b)
        double m = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        double b = (sumY - m * sumX) / n;

        return new double[]{m, b};
    }

    private double predictNextSpending(double[] coefficients) {
        double m = coefficients[0];
        double b = coefficients[1];

        // Assuming the next transaction is index n (the next one after the existing data)
        double nextTransactionIndex = coefficients.length;
        return m * nextTransactionIndex + b; // Predicted spending for the next transaction
    }

    // Show the input dialog for setting goal and budget
    private void showInputDialog() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.dialog_input, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter Goal and Budget")
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> {
                    EditText editTextGoal = dialogView.findViewById(R.id.goalText);
                    EditText editTextBudget = dialogView.findViewById(R.id.budgetText);

                    String goal = editTextGoal.getText().toString();
                    String budgetString = editTextBudget.getText().toString();
                    Double budget = budgetString.isEmpty() ? null : Double.parseDouble(budgetString);

                    // Retrieve username from session
                    SharedPreferences sharedPref = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
                    String username = sharedPref.getString("username", null);

                    handleInputData(username, goal, budget);

                    displayGoalAndBudget(username); // Update UI with new goal and budget
                })
                .setNegativeButton("Cancel", null);

        builder.create().show();
    }

    //display goal and budget
    public void displayGoalAndBudget(String username) {
        if (username != null) {
            DBHelper dbHelper = new DBHelper(getActivity());
            String[] goalAndBudget = dbHelper.getGoalAndBudget(username);

            String goal = (goalAndBudget[0] == null) ? "Please set goal" : goalAndBudget[0];
            String budget = (goalAndBudget[1] == null) ? "Please set budget" : goalAndBudget[1];

            TextView goalTextView = view.findViewById(R.id.goalDescription);
            TextView budgetTextView = view.findViewById(R.id.budgetAmount);
            TextView errorTextView = view.findViewById(R.id.errorTextView);

            goalTextView.setText(goal);
            budgetTextView.setText(budget);

            // Hide or show the reminder section based on the goal and budget being set
            View reminderSection = view.findViewById(R.id.reminderSection);
            if (goalAndBudget[0] == null || goalAndBudget[1] == null) {
                //errorTextView.setText("Please enter both fields.");
                reminderSection.setVisibility(View.GONE);
            } else {
                reminderSection.setVisibility(View.VISIBLE);
                updateReminderSection(username);
            }
        } else {
            Toast.makeText(getContext(), "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    // handle input data (saving goal and budget in the DB)
    private void handleInputData(String username, String goal, Double budget) {
        DBHelper dbHelper = new DBHelper(getContext());
        boolean isUpdated = dbHelper.updateGoal(username, goal, budget);
        if (isUpdated) {
            Toast.makeText(getContext(), "Goal and Budget Updated Successfully", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getContext(), "Update Unsuccessful", Toast.LENGTH_LONG).show();
        }
    }

    //update reminder section with total spent and remaining budget
    private void updateReminderSection(String username) {
        DBHelper dbHelper = new DBHelper(getContext());
        double totalExpenses = dbHelper.calculateTotalSpentForMonth(username);  // Retrieve total spent from DB
        double budget = dbHelper.getBudget(username);  // Retrieve budget from DB
        double amountToSave = budget - totalExpenses;

        TextView totalSpentTextView = view.findViewById(R.id.totalSpent);
        TextView saveAmountTextView = view.findViewById(R.id.saveAmount);
        TextView savingMessageTextView = view.findViewById(R.id.savingMessage);

        totalSpentTextView.setText(String.format("Total spent this month: Rs. %.1f", totalExpenses));
        saveAmountTextView.setText(String.format("Remaining Balance: Rs. %.1f", Math.max(amountToSave, 0)));

        if (budget > 0) {
                if (totalExpenses > budget) {
                    savingMessageTextView.setText("You have exceeded your budget for this month. Consider adjusting your spending habits.");
                } else if (totalExpenses == budget) {
                    savingMessageTextView.setText("You have reached your budget limit for this month. Be careful with further spending.");
                } else {
                    savingMessageTextView.setText("You are within your budget. Keep up the good work!");
                }
        } else {
            savingMessageTextView.setText("No Budget is set.");
        }
    }

}



