package com.vanessameerkamp.vmworktimer.controller.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DataSource {

    private static final String LOG_TAG = DataSource.class.getSimpleName();

    DbHelper dbHelper;
    SQLiteDatabase database;

    public DataSource(Context context) {
        Log.d(LOG_TAG, "DbHelper wird erzeugt.");
        dbHelper = new DbHelper(context);
    }

    void openWritable() {

        Log.d(LOG_TAG, "Eine schreibende Referenz auf die Datenbank wird angefragt.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    void openReadable() {

        Log.d(LOG_TAG, "Eine lesende Referenz auf die Datenbank wird angefragt.");
        database = dbHelper.getReadableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    void closeDb() {

        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank geschlossen.");
    }
}
