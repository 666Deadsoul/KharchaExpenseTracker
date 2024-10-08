package com.example.first;

import android.database.SQLException;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class Data extends AppCompatActivity {
    //show in list view
//    ListView listview;
//    List<User> contacts;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.data);
//        listview = findViewById(R.id.listView);
//        try {
//            ContactsDB db = new ContactsDB(this);
//            db.open();
//            //List<String> contactNames = Arrays.asList(Contact.getContacts(contacts));
//            //textView.setText(db.returndata());
//            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
//                    android.R.layout.simple_list_item_1, db.returndata());
//
//            listview.setAdapter(adapter);
//            db.close();
//        } catch (SQLException e) {
//            Toast.makeText(Data.this, e.getMessage(), Toast.LENGTH_LONG)
//                    .show();
//        }
//    }
}