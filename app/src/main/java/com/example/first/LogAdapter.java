package com.example.first;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.first.LogEntry;
import com.example.first.R;
import com.example.first.layout.CategoryLogoMapper;

import java.util.List;

//public class LogAdapter extends ArrayAdapter<LogEntry> {
//    private DBHelper dbHelper;
//    private List<LogEntry> logs;
//    private Context context;
//
//    public LogAdapter(Context context, List<LogEntry> logs) {
//        super(context, 0, logs);
//        this.context = context;
//        this.logs = logs;
//        dbHelper = new DBHelper(context);
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        LogEntry logEntry = getItem(position);
//        if (logEntry == null) {
//            return convertView;
//        }
//
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_log, parent, false);
//        }
//
//        // Lookup views
//        TextView categoryTextView = convertView.findViewById(R.id.textCategory);
//        TextView itemTextView = convertView.findViewById(R.id.textItem);
//        TextView amountTextView = convertView.findViewById(R.id.textAmount);
//        TextView dateTextView = convertView.findViewById(R.id.textDate);
//        Button deleteButton = convertView.findViewById(R.id.btnDelete); // Add this line
//
//        categoryTextView.setText(logEntry.getCategory());
//        itemTextView.setText(logEntry.getItem());
//        amountTextView.setText(String.valueOf(logEntry.getAmount()));
//        dateTextView.setText(logEntry.getDate());
//
//        // Set delete button click listener
//        deleteButton.setOnClickListener(v -> {
//            // Call method to delete log
//            DBHelper dbHelper = new DBHelper(getContext());
//            if (dbHelper.deleteLog(logEntry.getId())) {
//                Toast.makeText(getContext(), "Log deleted" , Toast.LENGTH_SHORT).show();
//                remove(logEntry); // Remove item from adapter
//                notifyDataSetChanged(); // Refresh the list
//            } else {
//                Toast.makeText(getContext(), "Failed to delete log" , Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        return convertView;
//    }
//}
public class LogAdapter extends ArrayAdapter<LogEntry> {
    private DBHelper dbHelper;
    private List<LogEntry> logs;
    private Context context;

    public LogAdapter(Context context, List<LogEntry> logs) {
        super(context, 0, logs);
        this.context = context;
        this.logs = logs;
        dbHelper = new DBHelper(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LogEntry logEntry = getItem(position);
        if (logEntry == null) {
            return convertView;
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_log, parent, false);
        }

        // Lookup views
        ImageView categoryLogoImageView = convertView.findViewById(R.id.categoryLogo);
        TextView itemTextView = convertView.findViewById(R.id.itemName);
        TextView amountTextView = convertView.findViewById(R.id.itemPrice);
        TextView dateTextView = convertView.findViewById(R.id.dateRecorded);
        Button deleteButton = convertView.findViewById(R.id.btnDelete);

        itemTextView.setText(logEntry.getItem());
        amountTextView.setText(String.valueOf(logEntry.getAmount()));
        dateTextView.setText(logEntry.getDate());

        deleteButton.setOnClickListener(v -> {
            // Call method to delete log
            DBHelper dbHelper = new DBHelper(getContext());
            if (dbHelper.deleteLog(logEntry.getId())) {
                Toast.makeText(getContext(), "Log deleted" , Toast.LENGTH_SHORT).show();
                remove(logEntry); // Remove item from adapter
                notifyDataSetChanged(); // Refresh the list
            } else {
                Toast.makeText(getContext(), "Failed to delete log" , Toast.LENGTH_SHORT).show();
            }
        });


        // Set category logo based on category name
        CategoryLogoMapper logoMapper = new CategoryLogoMapper();
        Integer logoResource = logoMapper.getLogoResource(logEntry.getCategory());
        if (logoResource != null) {
            categoryLogoImageView.setImageResource(logoResource);
        } else {
            categoryLogoImageView.setImageResource(R.drawable.food); // Default logo
        }

        return convertView;
    }
}



//        categoryTextView.setText("Test Category");
//        itemTextView.setText("Test Item");
//        amountTextView.setText("100.00");
//        dateTextView.setText("2024-08-01");
// Populate the data into the template view using the data object