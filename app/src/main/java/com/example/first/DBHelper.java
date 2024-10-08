package com.example.first;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.content.*;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "kharchaDB";
    private static final int DATABASE_VERSION = 5;  //number of tables

    // User table
    private static final String TABLE_NAME = "user";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FNAME = "fname";
    private static final String COLUMN_UNAME = "uname";
    private static final String COLUMN_PASS = "pass";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_GOAL = "goal";
    private static final String COLUMN_BUDGET = "budget";

    //Expenses Table
    private static final String TABLE_LOGS = "expense";
    private static final String COLUMN_LOG_ID = "log_id";
    private static final String COLUMN_LOG_USERNAME = "uname";
    private static final String COLUMN_LOG_CATEGORY = "category";
    private static final String COLUMN_LOG_ITEM = "item";
    private static final String COLUMN_LOG_AMOUNT = "amount";
    private static final String COLUMN_LOG_DATE = "date";


    // Constructor
    public DBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    //creating a table
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "fname TEXT NOT NULL, " +
                    "uname TEXT NOT NULL, " +
                    "pass TEXT NOT NULL, " +
                    "email TEXT NOT NULL," +
                    "goal TEXT," +
                    "budget REAL)";
            db.execSQL(createTable);
            Log.d("DBHelper", "User table created.");

            String createExpTable = "CREATE TABLE " + TABLE_LOGS + " (" +
                    "log_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "uname TEXT NOT NULL, " +
                    "category TEXT NOT NULL, " +
                    "item TEXT NOT NULL, " +
                    "amount REAL NOT NULL, " +
                    "date TEXT " +
//                    "FOREIGN KEY(uname) REFERENCES " + TABLE_NAME + "(uname) " +
//                    "ON DELETE CASCADE ON UPDATE CASCADE" +
                    ");";
            db.execSQL(createExpTable);
            Log.d("DBHelper", "Logs table created.");

        } catch (Exception e) {
            Log.e("DBHelper", "Error creating tables", e);
        }
    }

    // Drop old table if it exists and create a new one
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGS);

        onCreate(db);
    }

    //insert a new user
    public boolean insertUser(String fname, String uname, String pass, String email) {
        SQLiteDatabase db = this.getWritableDatabase(); //open db in writable mode
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FNAME, fname);
        contentValues.put(COLUMN_UNAME, uname);
        contentValues.put(COLUMN_PASS, pass);
        contentValues.put(COLUMN_EMAIL, email);

        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return result != -1; // If result is -1, insert failed
    }

    //for login
    public boolean checkUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID}; // We only need the ID, but you can select more if needed
        String selection = COLUMN_UNAME + " = ? AND " + COLUMN_PASS + " = ?";
        String[] selectionArgs = {username, password};

        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count > 0; // Returns true if a matching record is found
    }

    //for update password
    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_PASS, newPassword);

        int rowsAffected = db.update(TABLE_NAME, contentValues, COLUMN_UNAME + "=?", new String[]{username});
        db.close();

        return rowsAffected > 0; // If rowsAffected > 0, the update was successful
    }

    //insert in expense table
    public boolean insertLogs(String uname, String category, String item, double amount, String date) {
            SQLiteDatabase db = null;
            try {
                db = this.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put(COLUMN_LOG_USERNAME, uname);
                contentValues.put(COLUMN_LOG_CATEGORY, category);
                contentValues.put(COLUMN_LOG_ITEM, item);
                contentValues.put(COLUMN_LOG_AMOUNT, amount);
                contentValues.put(COLUMN_LOG_DATE, date);

                long result = db.insert(TABLE_LOGS, null, contentValues);

                if (result == -1) {
                    return false;
                }
                return true;
            } catch (Exception e) {
                Log.e("DB_EXCEPTION", "Exception during insertLogs", e); // Log exception details
                return false;
            } finally {
                if (db != null) {
                    db.close();
                }
            }
    }
    //for prediction data
    public Cursor getDailyTransactionSums(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Selecting date and the sum of amounts for each date
        return db.rawQuery("SELECT date, SUM(amount) as totalAmount FROM expense WHERE uname = ? GROUP BY date ORDER BY date", new String[]{username});
    }

    //to get fullname of user
    public String getFullNameByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("user", new String[]{"fname"}, "uname = ?", new String[]{username}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String fullName = cursor.getString(cursor.getColumnIndex("fname"));
            cursor.close();
            return fullName;
        }
        return null;
    }

    //delete logs
    public boolean deleteLog(int logId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_LOGS, COLUMN_LOG_ID + "=?", new String[]{String.valueOf(logId)});
        db.close();
        return rowsAffected > 0; // Return true if a log was deleted
    }

    // to update goal and budget
    public boolean updateGoal(String uname, String goal, double budget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("goal", goal);
        contentValues.put("budget", budget);

        int rowsAffected = db.update(TABLE_NAME, contentValues, COLUMN_UNAME + "=?", new String[]{uname});
        db.close();

        return rowsAffected > 0;  // If rowsAffected > 0, the update was successful
    }

    //to set goal and budget in the profile
    @SuppressLint("Range")
    public String[] getGoalAndBudget(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"goal", "budget"};
        String selection = COLUMN_UNAME + "=?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        String[] result = new String[2]; // Array to hold goal and budget
        if (cursor != null && cursor.moveToFirst()) {
            result[0] = cursor.getString(cursor.getColumnIndex("goal"));
            result[1] = cursor.getString(cursor.getColumnIndex("budget"));
            cursor.close();
        } else {
            result[0] = null;
            result[1] = null;
        }

        db.close();
        return result;
    }

    // to get the list of logs from db
    public List<LogEntry> getLogsByUsername(String username) {
        List<LogEntry> logList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_LOGS + " WHERE " + COLUMN_LOG_USERNAME + " = ? ORDER BY " + COLUMN_LOG_DATE+ " DESC";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_LOG_ID)); // Retrieve log_id
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex(COLUMN_LOG_CATEGORY));
                @SuppressLint("Range") String item = cursor.getString(cursor.getColumnIndex(COLUMN_LOG_ITEM));
                @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex(COLUMN_LOG_AMOUNT));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COLUMN_LOG_DATE));

                LogEntry logEntry = new LogEntry(id, category, item, amount, date); // Create LogEntry with id
                logList.add(logEntry);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return logList;
    }

    //update the detail of the user
    public void updateUser(String fname, String uname, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fname", fname);
        values.put("uname", uname);
        values.put("email", email);
        db.update("user", values, "uname = ?", new String[]{uname});
        db.close();
    }

    //calculate total spending of month
    public double calculateTotalSpentForMonth(String username) {
        double totalSpent = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Query to sum amounts spent in the current month for the logged-in user
            String query = "SELECT SUM(amount) FROM expense WHERE strftime('%Y-%m', date) = strftime('%Y-%m', 'now') AND uname = ?";
            cursor = db.rawQuery(query, new String[]{username});

            if (cursor != null && cursor.moveToFirst()) {
                totalSpent = cursor.getDouble(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return totalSpent;
    }

    //retrieve budget for the current user
    public double getBudget(String username) {
        double budget = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            String query = "SELECT budget FROM user WHERE uname = ?";
            cursor = db.rawQuery(query, new String[]{username});

            if (cursor != null && cursor.moveToFirst()) {
                budget = cursor.getDouble(0);
            } else {
                Log.e("DBHelper", "No budget found for username: " + username);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        Log.d("DBHelper", "Budget retrieved for username " + username + ": " + budget);
        return budget;
    }
    //to get the categories of user from db
    public List<CategoryExpense> getCategoryExpenses(String uname) {
        List<CategoryExpense> expenses = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT category, SUM(amount) as total FROM expense WHERE uname = ? GROUP BY category";
        Cursor cursor = db.rawQuery(query, new String[]{uname});

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("category"));
            @SuppressLint("Range") float total = cursor.getFloat(cursor.getColumnIndex("total"));
            expenses.add(new CategoryExpense(category, total));
        }
        cursor.close();
        return expenses;
    }
}

