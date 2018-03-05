package comm.example.aliumutcan.mobil_ogrenci_yoklama;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class ogrenci_ana_ekran extends AppCompatActivity {

    Spinner spn_dersler;

    Button btn_yoklamaya_al;
    Button btn_yoklamdan_cik;

    JSONArray dersler;
    int id = 0, acilan_ders_id = 0, yoklama_id = 0;
    String ders_adi="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ogrenci_ana_ekran);
        spn_dersler =(Spinner) findViewById(R.id.spn_acik_dersler);

        btn_yoklamaya_al= (Button) findViewById(R.id.btn_yoklamaya_al);
        btn_yoklamdan_cik= (Button) findViewById(R.id.btn_yoklamadan_cik);
        btn_yoklamdan_cik.setVisibility(View.INVISIBLE);

        TextView et_adi_soyadi = (TextView) findViewById(R.id.et_adi_soyadi);
        TextView et_sinif_no = (TextView) findViewById(R.id.et_sinif_no);
        TextView et_fakute = (TextView) findViewById(R.id.et_fakulte);
        TextView et_bolum = (TextView) findViewById(R.id.et_bolum);
        Intent gelenveri = getIntent();

        id = Integer.parseInt(gelenveri.getStringExtra("id"));
        et_adi_soyadi.setText(gelenveri.getStringExtra("adi_soyadi"));
        et_sinif_no.setText("sinif:"+gelenveri.getStringExtra("sinif"));
        et_fakute.setText(gelenveri.getStringExtra("fakulte"));
        et_bolum.setText(gelenveri.getStringExtra("bolum"));
        yoklama_id=Integer.parseInt(gelenveri.getStringExtra("yoklama_id"));
        ders_adi=gelenveri.getStringExtra("ders_adi");




        btn_yoklamdan_cik.setText(ders_adi+" dersinden cık");
        Button btn_yoklama_bilgisi=(Button)findViewById(R.id.btn_yoklama_bilgisi);
        Yoklama_işlemeleri();

        Acik_Dersler dersleri_getir = new Acik_Dersler();
        dersleri_getir.execute();


        spn_dersler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (dersler != null && position > 0)
                    {
                        acilan_ders_id=(position-1);

                        for (int i=0;dersler.get(i)!=null;i++){
                            if(dersler.getJSONObject(i).getString("ders_adi")==spn_dersler.getSelectedItem().toString()){
                                acilan_ders_id=Integer.parseInt(dersler.getJSONObject(i).getString("id"));
                                ders_adi=dersler.getJSONObject(i).getString("ders_adi");
                            }
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btn_yoklamaya_al.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Yoklamaya_Katil yoklamaya_katil=new Yoklamaya_Katil();
                yoklamaya_katil.execute();
            }
        });

        btn_yoklamdan_cik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Yoklamadan_Cik().execute();
            }
        });

        btn_yoklama_bilgisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent yeni= new Intent(ogrenci_ana_ekran.this,Ogrenci_ders_yoklama_bilgisi.class);
                yeni.putExtra("ogrenci_id",String.format("%d",id));
                startActivity(yeni);
            }
        });
    }

    public class Acik_Dersler extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            try {
                url = new URL("http://aliumutcankul.com/api/Ogrenci_islemleri/GET_Acilandersler/" + id);
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(false);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");

                int responseCode = conn.getResponseCode();

                if (responseCode == 200) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        //break;
                    }

                    in.close();
                    return sb.toString();

                } else {
                    return new String("false");
                }

            } catch (Exception e) {
                return new String(e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == "{false}") {

            }
            Log.d("acık dersler", s);
            try {
                List<String> gelen_dersler2 = new ArrayList<String>();
                gelen_dersler2.add("Bir ders secin");
                dersler = new JSONArray(s);
                int sayac = 0;
                while (sayac <= 0) {

                    gelen_dersler2.add(dersler.getJSONObject(sayac).getString("ders_adi"));
                    sayac++;
                }


                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, gelen_dersler2);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                spn_dersler.setAdapter(dataAdapter);


            } catch (Exception e) {
                Log.d("postExecute", e.getMessage());
            }

        }
    }

    public class Yoklamaya_Katil extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void... params) {
            URL url;
            HttpURLConnection conn;

            try {

                url = new URL("http://aliumutcankul.com/api/Ogrenci_islemleri/POST_Yoklamaya_Gir");
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");


                JSONObject yoklama_bilgileri = new JSONObject();
                yoklama_bilgileri.put("ogrenci_id", id);
                yoklama_bilgileri.put("acilan_ders_id", acilan_ders_id);
                WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = manager.getConnectionInfo();
                yoklama_bilgileri.put("mac_adres", info.getMacAddress());

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(yoklama_bilgileri.toString());

                writer.flush();
                writer.close();
                os.close();

                Log.d("gelen cevap", yoklama_bilgileri.toString()+" " + conn.getResponseCode());
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
            Log.d("oldu",s+" "+ders_adi);
            btn_yoklamdan_cik.setText(ders_adi+" dersinden cık");
            yoklama_id=Integer.parseInt(s);
            Yoklama_işlemeleri();
        }
    }

    public class Yoklamadan_Cik extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... params) {
            URL url;
            HttpURLConnection conn;
            try{
                url=new URL("http://aliumutcankul.com/api/Ogrenci_islemleri/GET_Yoklamdan_cik/"+yoklama_id);
                conn=(HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(false);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-Type", "application/json");

                int responseCode = conn.getResponseCode();
                Log.d("yc_kod",String.format("%d %d",responseCode,yoklama_id));
                if(responseCode==200){

                    BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb=new StringBuffer("");
                    String satir="";
                    while ((satir=br.readLine())!=null){
                        sb.append(satir);
                    }

                    return sb.toString();
                }else
                    Log.d("YC_responseCode",String.format("%d",conn.getResponseCode()));


            }catch (Exception e){
                Log.d("Yoklamdan_cık_error",e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            JSONObject jo;
            try{

                jo=new JSONObject(s);
                if (Boolean.parseBoolean(jo.getString("durum"))==true){
                    yoklama_id=0;
                    acilan_ders_id=0;
                    Yoklama_işlemeleri();
                }else{
                    Log.d("gelencevapdurumu:",s);
                }

            }catch (Exception e){

            }
        }
    }

    private void Yoklama_işlemeleri(){
        if (yoklama_id>0){
            btn_yoklamdan_cik.setVisibility(View.VISIBLE);
            btn_yoklamaya_al.setVisibility(View.INVISIBLE);
            spn_dersler.setVisibility(View.INVISIBLE);
        }else{
            btn_yoklamdan_cik.setVisibility(View.INVISIBLE);
            btn_yoklamaya_al.setVisibility(View.VISIBLE);
            spn_dersler.setVisibility(View.VISIBLE);
            Acik_Dersler dersleri_getir = new Acik_Dersler();
            dersleri_getir.execute();
        }
    }

}