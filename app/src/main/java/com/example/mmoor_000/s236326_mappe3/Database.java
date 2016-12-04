package com.example.mmoor_000.s236326_mappe3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper {

    private static String TABLE_NAME = "Shealth";
    private static String NAVN = "Navn";
    private static String VEKT = "Vekt";
    private static String HOYDE = "Høyde";
    private static String AAR = "År";
    private static String MANED = "Måned";
    private static String DAG = "Dag";
    private static String ID = "ID";
    private static String DAGLIG = "Daglig";
    private static String REKORD = "Rekord";
    private static String SKRITT = "Skritt";
    private static int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "Shealth";

    SQLiteDatabase db;
    Cursor cur;

    Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String LAG_TABELL = "CREATE TABLE " + TABLE_NAME
                + "("
                + NAVN + " TEXT,"
                + VEKT + " INTEGER,"
                + HOYDE + " INTEGER,"
                + DAG + " TEXT,"
                + MANED + " TEXT,"
                + AAR + " TEXT,"
                + ID + " INTEGER PRIMARY KEY,"
                + DAGLIG + " INTEGER,"
                + REKORD + " INTEGER,"
                + SKRITT + " INREGER"
                + ")";
        Log.d("database", "lagd");
        db.execSQL(LAG_TABELL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );
        onCreate(db);
    }

    void leggTil(Info info) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAVN, info.getNavn());
        values.put(VEKT, info.getVekt());
        values.put(HOYDE, info.getHoyde());
        values.put(DAG, info.getDag());
        values.put(MANED, info.getManed());
        values.put(AAR, info.getAar());
        values.put(ID, info.getId());
        values.put(DAGLIG, info.getDaglig());
        values.put(REKORD, info.getRekord());
        values.put(SKRITT, info.getSkritt());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    int Oppdater(String navn, int vekt, int hoyde, int dag, int maned, int aar) {
        SQLiteDatabase sd = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(NAVN, navn);
        cv.put(VEKT, vekt);
        cv.put(HOYDE, hoyde);
        cv.put(DAG, dag);
        cv.put(MANED, maned);
        cv.put(AAR, aar);
        cv.put(ID, 1);
        int update = sd.update(TABLE_NAME,cv,ID + "='" + 1 +"'",null);
        //db.close();
        return update;
    }
    Cursor Finn(int i) {
        SQLiteDatabase db = this.getWritableDatabase();
        String[] person = {NAVN,VEKT,HOYDE,DAG,MANED,AAR,DAGLIG,REKORD,SKRITT};
        cur = db.query(TABLE_NAME,person,ID + " == " + i,null,null,null,null);
        return cur;
    }
    int Daglig(int i) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DAGLIG, i);
        int update = db.update(TABLE_NAME,cv,ID + "='" + 1 + "'",null);
        return update;
    }
    int setRekord(int i) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(REKORD, i);
        int update = db.update(TABLE_NAME,cv,ID + "='" + 1 + "'",null);
        return update;
    }
    int setSkritt(int i) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(SKRITT, i);
        int update = db.update(TABLE_NAME,cv,ID + "='" + 1 + "'",null);
        return update;
    }
}
