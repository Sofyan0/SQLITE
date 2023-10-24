package com.adi.logsingsql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nama database
    private static final String DATABASE_NAME = "mydatabase.db";

    // Versi database
    private static final int DATABASE_VERSION = 1;

    // Nama tabel
    private static final String TABLE_NAME = "users";

    // Kolom-kolom dalam tabel
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_ALAMAT = "alamat";
    private static final String COLUMN_TELEPON = "telepon";
    private static final String COLUMN_TANGGAL_LAHIR = "tanggal_lahir";
    private static final String COLUMN_JENIS_KELAMIN = "jenis_kelamin";
    private static final String COLUMN_AGAMA = "agama";

    // Query untuk membuat tabel
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    COLUMN_EMAIL + " TEXT," +
                    COLUMN_PASSWORD + " TEXT," +
                    COLUMN_NAME + " TEXT," +
                    COLUMN_USERNAME + " TEXT," +
                    COLUMN_ALAMAT + " TEXT," +
                    COLUMN_TELEPON + " TEXT," +
                    COLUMN_TANGGAL_LAHIR + " TEXT," +
                    COLUMN_JENIS_KELAMIN + " TEXT," +
                    COLUMN_AGAMA + " TEXT);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String email, String password, String name, String username, String alamat, String telepon, String tanggalLahir, String jenisKelamin, String agama) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_EMAIL, email);
        contentValues.put(COLUMN_PASSWORD, password);
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_ALAMAT, alamat);
        contentValues.put(COLUMN_TELEPON, telepon);
        contentValues.put(COLUMN_TANGGAL_LAHIR, tanggalLahir);
        contentValues.put(COLUMN_JENIS_KELAMIN, jenisKelamin);
        contentValues.put(COLUMN_AGAMA, agama);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public Boolean checkEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_USERNAME + " = ?", new String[]{email});
        return cursor.getCount() > 0;
    }


    public Boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID};
        String selection = COLUMN_USERNAME + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }
}
