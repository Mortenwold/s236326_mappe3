package com.example.mmoor_000.s236326_mappe3;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

/**
 * Created by mmoor_000 on 02-Dec-16.
 */

public class Fil {
    final static String fileName = "data.txt";
    final static String path = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS + "/" + "Android").getAbsolutePath() + "/";

    public static  String ReadFile(Context context){
        String line = null;

        try {
            FileInputStream fis = new FileInputStream (new File(path + fileName));
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ( (line = br.readLine()) != null )
            {
                sb.append(line + System.getProperty("line.separator"));
            }
            fis.close();
            line = sb.toString();

            br.close();
        }
        catch(FileNotFoundException ex) {
            ex.printStackTrace();
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        return line;
    }

    public static boolean saveToFile( String data){
        try {
            new File(path  ).mkdir();
            File fil = new File(path + fileName);
            if (!fil.exists()) {
                fil.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(fil,true);
            fos.write((data + System.getProperty("line.separator")).getBytes());

            return true;
        }  catch(FileNotFoundException ex) {
            ex.printStackTrace();
        }  catch(IOException ex) {
            ex.printStackTrace();
        }
        return  false;


    }

}
