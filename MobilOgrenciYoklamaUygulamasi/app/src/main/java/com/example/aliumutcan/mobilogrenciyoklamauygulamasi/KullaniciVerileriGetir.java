package com.example.aliumutcan.mobilogrenciyoklamauygulamasi;

import android.database.CursorJoiner;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by AliUmutcan on 29.03.2017.
 */

public class KullaniciVerileriGetir extends AsyncTask<Void,Void,String> {

    @Override
    protected String doInBackground(Void... voids) {

        try {
            URL url=new URL("asdas");
            HttpURLConnection conn=(HttpURLConnection) url.openConnection();

            conn.setDoInput(true);
            conn.setDoOutput(false);
            conn.setRequestMethod("GET");

            if (conn.getResponseCode()==200){

                InputStream veriler=new BufferedInputStream(conn.getInputStream());

                return veriler.toString();


            }


        }catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
