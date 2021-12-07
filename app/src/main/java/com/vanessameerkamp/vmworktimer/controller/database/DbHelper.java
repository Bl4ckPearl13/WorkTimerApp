package com.vanessameerkamp.vmworktimer.controller.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.vanessameerkamp.vmworktimer.controller.helper.SQL;

public class DbHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = DbHelper.class.getSimpleName();

    public static final String DB_NAME = "work_timer.db3";
    public static final int DB_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        try {
            Log.d(LOG_TAG, "Tabelle wird mit dem Befehl: " + SQL.CREATE_TABLE_CUSTOMER + " angelegt.");
            db.execSQL(SQL.CREATE_TABLE_CUSTOMER);

            Log.d(LOG_TAG, "Tabelle wird mit dem Befehl: " + SQL.CREATE_TABLE_PROJECT + " angelegt.");
            db.execSQL(SQL.CREATE_TABLE_PROJECT);

            Log.d(LOG_TAG, "Tabelle wird mit dem Befehl: " + SQL.CREATE_TABLE_WORK_TIME + " angelegt.");
            db.execSQL(SQL.CREATE_TABLE_WORK_TIME);

        } catch (SQLException e) {
            Log.e(LOG_TAG, "Fehler beim Anlegen von Tabelle: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d(LOG_TAG, "Die Datenbank mit der Versionsnummer " + oldVersion + " wird entfernt.");

        db.execSQL(SQL.DROP_DB);
        onCreate(db);
    }
}
