package com.example.aliumutcan.mobilogrenciyoklamauygulamasi;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class giris extends ActionBarActivity {

    EditText goster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        goster=(EditText) findViewById(R.id.et_kullaniciadi);

        //KullaniciVerileriGetir getirr = new KullaniciVerileriGetir.execute();
        new KullaniciVerileriGetir2.execute();

    }

    private class KullaniciVerileriGetir2 extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {

            try {
                URL url=new URL("http://aliumutcankul.com/api/Ogrenci_islemleri/5");
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

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            goster.setText(s);
        }
    }
}


