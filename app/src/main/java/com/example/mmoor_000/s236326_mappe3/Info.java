package com.example.mmoor_000.s236326_mappe3;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by mmoor_000 on 11/10/2016.
 */

public class Info {
    String navn;
    int vekt;
    int hoyde;
    int Aar;
    int Maned;
    int Dag;
    int id;
    int daglig;

    public Info(String navn, int vekt, int hoyde, int dag, int maned, int aar, int id, int daglig) {
        this.navn = navn;
        this.vekt = vekt;
        this.hoyde = hoyde;
        Aar = aar;
        Maned = maned;
        Dag = dag;
        this.id = id;
        this.daglig = daglig;

    }

    public int getDaglig() {
        return daglig;
    }

    public String getNavn() {
        return navn;
    }

    public int getVekt() {
        return vekt;
    }

    public int getHoyde() {
        return hoyde;
    }

    public int getAar() {
        return Aar;
    }

    public int getManed() {
        return Maned;
    }

    public int getDag() {
        return Dag;
    }

    public int getId() {
        return id;
    }
}
