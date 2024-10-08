package com.example.first.layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.first.DBHelper;
import com.example.first.LogAdapter;
import com.example.first.LogEntry;
import com.example.first.R;
import java.util.List;

public class logsFragment extends Fragment {
    private ListView logsListView;
    private LogAdapter logAdapter;
    private DBHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logs, container, false);

        logsListView = view.findViewById(R.id.logsListView);
        dbHelper = new DBHelper(getActivity());

        try {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", null);

            if (username != null) {
                List<LogEntry> logs = dbHelper.getLogsByUsername(username); // Retrieve logs from the database

                // Check if logs are available
                if (logs != null && !logs.isEmpty()) {
                    logAdapter = new LogAdapter(getActivity(), logs);
                    logsListView.setAdapter(logAdapter);
                } else {
                    Toast.makeText(getActivity(), "No logs available", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "User not logged in", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Failed to load logs", Toast.LENGTH_SHORT).show();
        }
        return view;
    }
}