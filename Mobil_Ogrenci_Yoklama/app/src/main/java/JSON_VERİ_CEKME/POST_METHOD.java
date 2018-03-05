package JSON_VERİ_CEKME;


import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import comm.example.aliumutcan.mobil_ogrenci_yoklama.Fragment_Ogrenci_giris;
import comm.example.aliumutcan.mobil_ogrenci_yoklama.R;
import comm.example.aliumutcan.mobil_ogrenci_yoklama.ogrenci_ana_ekran;

public class POST_METHOD  extends AsyncTask<String, Void , String> {
    private String sonuc="";
    public String getSonuc(){
        return sonuc;
    }
        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;

            try {

                url = new URL(params[0]);
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");


                JSONObject yoklama_bilgileri = new JSONObject(params[1]);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(yoklama_bilgileri.toString());

                writer.flush();
                writer.close();
                os.close();

                Log.d("Post_method", yoklama_bilgileri.toString()+" " + conn.getResponseCode());
                if (conn.getResponseCode() == 200) {

                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String satir = "";
                    while ((satir = br.readLine()) != null) {
                        sb.append(satir);
                    }
                    return sb.toString();
                }

            } catch (Exception e) {
                Log.d("hata mesajı", e.getMessage());
            }
            return null;
        }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        sonuc=s;
    }

}
