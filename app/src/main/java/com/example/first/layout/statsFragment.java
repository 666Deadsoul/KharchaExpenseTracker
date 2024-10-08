package com.example.first.layout;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.first.CategoryExpense;
import com.example.first.DBHelper;
import com.example.first.PieChartView;
import com.example.first.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
public class statsFragment extends Fragment {
    //private PieChart pieChart;
    private DBHelper dbHelper;
    private PieChartView pieChartView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);
        dbHelper = new DBHelper(requireActivity());
        SharedPreferences sharedPref = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String username = sharedPref.getString("username", null);

        // Fetch category expenses
        List<CategoryExpense> expenseDataList = dbHelper.getCategoryExpenses(username);

        if (expenseDataList == null || expenseDataList.isEmpty()) {
            // Handle case when there are no logs
            TextView messageTextView = new TextView(requireActivity());
            messageTextView.setText("No logs found. Please insert logs.");
            messageTextView.setTextSize(18);
            messageTextView.setTextColor(Color.RED);
            messageTextView.setGravity(Gravity.CENTER);

            // Add the message to the layout (you can customize placement)
            RelativeLayout layout = view.findViewById(R.id.pieChartContainer);
            layout.addView(messageTextView);
        } else {
            // Continue with normal pie chart creation if logs exist
            List<Float> values = new ArrayList<>();
            List<String> categoryNames = new ArrayList<>();
            List<String> categoryPercentages = new ArrayList<>();

            float totalExpense = 0f;

            // Extract total expenses and calculate total
            for (CategoryExpense data : expenseDataList) {
                float total = (float) data.getTotal();  // Convert double to float for the PieChart
                values.add(total);
                categoryNames.add(data.getCategory());
                totalExpense += total;
            }

            // Calculate percentages and prepare text to be shown in layout
            for (int i = 0; i < values.size(); i++) {
                float percentage = (values.get(i) / totalExpense) * 100;
                String text = String.format("%s: %.1f%%", categoryNames.get(i), percentage);
                categoryPercentages.add(text);  // Store the formatted text with the category and percentage
            }

            // Add the PieChartView with smaller size
            pieChartView = new PieChartView(requireActivity(), values);
            RelativeLayout layout = view.findViewById(R.id.pieChartContainer);
            layout.addView(pieChartView, new RelativeLayout.LayoutParams(800, 800)); // Set the size of the PieChartView

            // Set total expense amount
            TextView totalExpenseTextView = view.findViewById(R.id.totalExpense);
            totalExpenseTextView.setText(String.format("Total Expense: Rs. %.2f", totalExpense));

            // Add TextViews for category info (percentages) outside the pie chart
            LinearLayout categoryInfoContainer = view.findViewById(R.id.categoryInfoContainer);
            for (String percentageInfo : categoryPercentages) {
                // Create a TextView for each category's percentage
                TextView textView = new TextView(requireActivity());
                textView.setText(percentageInfo);
                textView.setTextSize(16);
                textView.setPadding(0, 8, 0, 8);

                // Add the TextView to the LinearLayout
                categoryInfoContainer.addView(textView);
            }
        }

        return view;
    }
}


//
//    private List<Float> getTotalExpenses(List<CategoryExpense> expenses) {
//        List<Float> values = new ArrayList<>();
//        for (CategoryExpense expense : expenses) {
//            values.add((float) expense.getTotal());  // Convert double to float
//        }
//        return values;
//    }
//}
//    public void displayPieChart() {
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
//        String username = sharedPreferences.getString("username", null);
//
//        if (username != null) {
//            List<CategoryExpense> categoryExpenses = dbHelper.getSpendingByCategory(username);
//
//            // Prepare data for the pie chart
//            List<PieEntry> entries = new ArrayList<>();
//            for (CategoryExpense expense : categoryExpenses) {
//                entries.add(new PieEntry((float) expense.getTotalAmount(), expense.getCategory()));
//            }
//
//            // Create PieDataSet and PieData
//            PieDataSet dataSet = new PieDataSet(entries, "Spending by Category");
//            PieData data = new PieData(dataSet);
//            pieChart.setData(data);
//            pieChart.invalidate(); // Refresh the chart
//        }
//    }
