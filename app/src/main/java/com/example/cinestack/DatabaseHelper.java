package com.example.cinestack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * DatabaseHelper - Manages SQLite database for CineStack application
 * Handles user authentication with secure password hashing (SHA-256)
 * 
 * @author ICT3214 Group Project
 * @version 1.0
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Information
    private static final String DATABASE_NAME = "CineStack.db";
    private static final int DATABASE_VERSION = 1;

    // Users Table
    private static final String TABLE_USERS = "users";
    
    // Users Table Columns
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_FULL_NAME = "full_name";
    private static final String COLUMN_CREATED_AT = "created_at";

    // SQL Query to Create Users Table
    private static final String CREATE_USERS_TABLE = 
        "CREATE TABLE " + TABLE_USERS + " (" +
        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COLUMN_USERNAME + " TEXT NOT NULL UNIQUE, " +
        COLUMN_EMAIL + " TEXT NOT NULL UNIQUE, " +
        COLUMN_PASSWORD + " TEXT NOT NULL, " +
        COLUMN_FULL_NAME + " TEXT NOT NULL, " +
        COLUMN_CREATED_AT + " TEXT NOT NULL" +
        ")";

    /**
     * Constructor - Creates or opens the database
     * @param context Application context
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when database is created for the first time
     * Creates the Users table
     * @param db SQLite database instance
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create Users table
        db.execSQL(CREATE_USERS_TABLE);
    }

    /**
     * Called when database needs to be upgraded
     * @param db SQLite database instance
     * @param oldVersion Old database version
     * @param newVersion New database version
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing table if database is upgraded
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        
        // Recreate tables
        onCreate(db);
    }

    /**
     * Hashes password using SHA-256 algorithm
     * This ensures passwords are never stored in plain text
     * 
     * @param password Plain text password
     * @return Hashed password in hexadecimal format
     */
    private String hashPassword(String password) {
        try {
            // Create SHA-256 MessageDigest instance
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            
            // Hash the password bytes
            byte[] hashBytes = digest.digest(password.getBytes());
            
            // Convert byte array to hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            
            return hexString.toString();
            
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Gets current timestamp in a readable format
     * @return Formatted timestamp string
     */
    private String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    /**
     * Registers a new user in the database
     * 
     * @param username User's chosen username
     * @param email User's email address
     * @param password User's password (will be hashed automatically)
     * @param fullName User's full name
     * @return true if registration successful, false otherwise
     */
    public boolean registerUser(String username, String email, String password, String fullName) {
        // Get writable database
        SQLiteDatabase db = this.getWritableDatabase();
        
        try {
            // Hash the password for security
            String hashedPassword = hashPassword(password);
            
            if (hashedPassword == null) {
                return false; // Hashing failed
            }
            
            // Prepare data for insertion
            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME, username.toLowerCase().trim());
            values.put(COLUMN_EMAIL, email.toLowerCase().trim());
            values.put(COLUMN_PASSWORD, hashedPassword);
            values.put(COLUMN_FULL_NAME, fullName.trim());
            values.put(COLUMN_CREATED_AT, getCurrentTimestamp());
            
            // Insert user into database
            long result = db.insert(TABLE_USERS, null, values);
            
            // result will be -1 if insertion failed
            return result != -1;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    /**
     * Validates user login credentials
     * 
     * @param username Username or email
     * @param password Password to verify
     * @return true if credentials are valid, false otherwise
     */
    public boolean loginUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        try {
            // Hash the input password
            String hashedPassword = hashPassword(password);
            
            if (hashedPassword == null) {
                return false;
            }
            
            // Query to check if user exists with matching credentials
            // Allow login with either username or email
            String query = "SELECT * FROM " + TABLE_USERS + 
                          " WHERE (" + COLUMN_USERNAME + " = ? OR " + COLUMN_EMAIL + " = ?)" +
                          " AND " + COLUMN_PASSWORD + " = ?";
            
            String usernameLower = username.toLowerCase().trim();
            Cursor cursor = db.rawQuery(query, new String[]{usernameLower, usernameLower, hashedPassword});
            
            // Check if any matching record found
            boolean isValid = cursor.getCount() > 0;
            
            cursor.close();
            return isValid;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    /**
     * Checks if username already exists in database
     * 
     * @param username Username to check
     * @return true if username exists, false otherwise
     */
    public boolean checkUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        try {
            String query = "SELECT * FROM " + TABLE_USERS + 
                          " WHERE " + COLUMN_USERNAME + " = ?";
            
            Cursor cursor = db.rawQuery(query, new String[]{username.toLowerCase().trim()});
            boolean exists = cursor.getCount() > 0;
            
            cursor.close();
            return exists;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    /**
     * Checks if email already exists in database
     * 
     * @param email Email to check
     * @return true if email exists, false otherwise
     */
    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        try {
            String query = "SELECT * FROM " + TABLE_USERS + 
                          " WHERE " + COLUMN_EMAIL + " = ?";
            
            Cursor cursor = db.rawQuery(query, new String[]{email.toLowerCase().trim()});
            boolean exists = cursor.getCount() > 0;
            
            cursor.close();
            return exists;
            
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }

    /**
     * Gets user's full name by username
     * 
     * @param username Username to look up
     * @return User's full name, or null if not found
     */
    public String getUserFullName(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        
        try {
            String query = "SELECT " + COLUMN_FULL_NAME + " FROM " + TABLE_USERS + 
                          " WHERE " + COLUMN_USERNAME + " = ?";
            
            Cursor cursor = db.rawQuery(query, new String[]{username.toLowerCase().trim()});
            
            String fullName = null;
            if (cursor.moveToFirst()) {
                fullName = cursor.getString(0);
            }
            
            cursor.close();
            return fullName;
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            db.close();
        }
    }
}
