package application.jobcompare.db;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class DbUtilities {

    /**
     * Check if the database exist and can be read.
     * Taken from https://stackoverflow.com/questions/3386667/query-if-android-database-exists
     *
     * @return true if it exists and can be read, false if it doesn't
     */
    public static boolean checkDb(SQLiteDatabase db) {
        SQLiteDatabase checkDB = null;
        boolean dbExists = false;
        try {
            checkDB = SQLiteDatabase.openDatabase(db.toString(), null,
                    SQLiteDatabase.OPEN_READONLY);
            dbExists = (checkDB.getVersion() > 0);
            checkDB.close();
            Log.i("db", db.toString()+" exists.");
        } catch (SQLiteException e) {
            // database doesn't exist yet.
            Log.e("checkDataBase", "Selected file could not be opened as DB.");
        }
        return dbExists;
    }

    public static boolean doesTableExist(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});

        boolean tableExists = false;

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                tableExists = true;
                Log.i("db", db.toString()+" has table: " + tableName + ".");
            }
            cursor.close();
        }

        return tableExists;
    }

    @SuppressLint("Range")
    public static String getTableSchema(SQLiteDatabase db, String tableName) {
        String schema = null;

        Cursor cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);

        if (cursor != null) {
            StringBuilder schemaBuilder = new StringBuilder();

            while (cursor.moveToNext()) {
                String columnName = cursor.getString(cursor.getColumnIndex("name"));
                String columnType = cursor.getString(cursor.getColumnIndex("type"));
                schemaBuilder.append(columnName).append(" ").append(columnType).append(", ");
            }

            cursor.close();

            // Remove the trailing ", " and store the schema
            if (schemaBuilder.length() > 2) {
                schema = schemaBuilder.substring(0, schemaBuilder.length() - 2);
            }
        }

        Log.i("db", schema);
        return schema;
    }

}
