package com.example.first;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConnDB {
    public static final String Key_RowId = "id";
    public static final String Key_Name = "fname";
    public static final String Key_Username = "uname";
    public static final String Key_Password = "pass";
    public static final String Key_Email = "email";

    public static final String Key_LogId = "log_id";
    public static final String Key_LogUsername = "uname";
    public static final String Key_LogCategory = "category";
    public static final String Key_LogItem = "item";
    public static final String Key_LogAmount = "amt";
    public static final String Key_LogDate = "date";
    private final String Expense_Table = "expense";

    private final String Database_Name = "kharchaDB"; //Database Name
    private final String Database_Table = "user";
    private final int Database_Version = 2;
    private DBHelper ourHelper;
    private final Context ourContext;
    private SQLiteDatabase ourdatabase;

    public ConnDB(Context context) {
        ourContext = context;
    }


    private class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, Database_Name, null, Database_Version);

        }

        @Override
        public void onCreate(SQLiteDatabase db) {
//            String sqlcode = "CREATE TABLE user(id INTEGER PRIMARY KEY AUTOINCREMENT, fname TEXT NOT NULL, uname TEXT NOT NULL, pass TEXT NOT NULL, email TEXT NOT NULL);";
//            db.execSQL(sqlcode);

            String createLogsTable = "CREATE TABLE expense(log_id INTEGER PRIMARY KEY AUTOINCREMENT, uname TEXT NOT NULL, category TEXT NOT NULL," +
                    "item TEXT NOT NULL, amt REAL NOT NULL, DATE TEXT NOT NULL);";
            db.execSQL(createLogsTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            db.execSQL("DROP TABLE IF EXISTS " + Database_Table);
            db.execSQL("DROP TABLE IF EXISTS " + Expense_Table);
            onCreate(db);
        }
    }

    public ConnDB open() throws SQLException {
        ourHelper = new DBHelper(ourContext);
        ourdatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        ourHelper.close();
    }

    public long create(String name, String username, String password, String email) {
        ContentValues cv = new ContentValues();
        cv.put(Key_Name, name);
        cv.put(Key_Username, username);
        cv.put(Key_Password, password);
        cv.put(Key_Email, email);
        return ourdatabase.insert(Database_Table, null, cv);
    }
    // Method to insert data into logs table
    public long createLog(String username, String category, String item, double amt, String date) {
        ContentValues cv = new ContentValues();
        cv.put(Key_LogUsername, username);
        cv.put(Key_LogCategory, category);
        cv.put(Key_LogItem, item);
        cv.put(Key_LogAmount, amt);
        cv.put(Key_LogDate, date);
        return ourdatabase.insert(Expense_Table, null, cv);
    }
}
//    public List<String> returndata() {
//        // Define the columns you want to query
//        String[] columns = new String[] {
//                Key_RowId,
//                Key_Name,
//                Key_Username,
//                Key_Password,
//                Key_Email
//        };
//
//        // Query the database
//        Cursor cursor = ourdatabase.query(Database_Table, columns, null, null, null, null, null);
//
//        List<User> users = new ArrayList<>();
//        while (cursor.moveToNext()) {
//            // Create a User object and add it to the list
//            User user = new User(
//                    cursor.getString(cursor.getColumnIndex(Key_Name)),
//                    cursor.getString(cursor.getColumnIndex(Key_Username)),
//                    cursor.getString(cursor.getColumnIndex(Key_Password)),
//                    cursor.getString(cursor.getColumnIndex(Key_Email))
//            );
//            users.add(user);
//        }
//        cursor.close(); // Close the cursor to avoid memory leaks
//
//        // Extract user names or other details as needed
//        String[] userNames = User.getUserNames(users);
//
//        return Arrays.asList(userNames);
//    }
//
//    public long deleteEnter(String rowId) {
//        return ourdatabase.delete(Database_Table, Key_RowId + "=?", new String[] {
//                rowId
//        });
//    }

//    public long update(String rowId, String cell, String name) {
//        ContentValues cu = new ContentValues();
//        cu.put(Key_Name, name);
//        cu.put(Key_Cell, cell);
//        return ourdatabase.update(Database_Table, cu, Key_RowId + "=?", new String[] {
//                rowId
//        });
//    }

