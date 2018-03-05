package comm.example.aliumutcan.mobil_ogrenci_yoklama;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.opengl.Visibility;
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

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Ogretmen_Ana_Ekran extends AppCompatActivity {

    Spinner spn_dersler;
    Button btn_yoklama_baslat;
    Button btn_acik_ders_kapat;
    TextView tv_kacaklar;
    JSONArray dersler;
    TextView tv_acik_ders_bilgilendirme;
    int id = 0, acilacak_ders_id = 0, acilan_ders_id = 0;
    String ders_adi = "";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ogretmen__ana__ekran);
        spn_dersler = (Spinner) findViewById(R.id.spin_tum_dersler);
        Intent gelenveri = getIntent();

        TextView tv_adi_soyadi = (TextView) findViewById(R.id.tv_adi_soyadi);
        TextView tv_fakulte = (TextView) findViewById(R.id.tv_fakulte);
        TextView tv_bolum = (TextView) findViewById(R.id.tv_bolum);
        btn_yoklama_baslat = (Button) findViewById(R.id.btn_yoklamayi_baslat);
        btn_acik_ders_kapat = (Button) findViewById(R.id.btn_acik_ders_kapat);
        btn_acik_ders_kapat.setVisibility(View.INVISIBLE);
        tv_acik_ders_bilgilendirme = (TextView) findViewById(R.id.tv_acik_ders_bilgilendirme);
        tv_acik_ders_bilgilendirme.setVisibility(View.INVISIBLE);

        tv_adi_soyadi.setText(gelenveri.getStringExtra("adi_soyadi"));
        tv_fakulte.setText(gelenveri.getStringExtra("fakulte"));
        tv_bolum.setText(gelenveri.getStringExtra("bolum"));

        id = Integer.parseInt(gelenveri.getStringExtra("id"));
        acilan_ders_id = Integer.parseInt(gelenveri.getStringExtra("acilan_ders_id"));
        ders_adi = gelenveri.getStringExtra("acik_ders_adi");
        tv_kacaklar=(TextView)findViewById(R.id.tv_kacaklar);
        tv_kacaklar.setText(" ");
        Gorsel_ac_kapa();

        Log.d("acik olan ders id", String.format("%d", acilan_ders_id));
        new Tumdersler().execute();

        spn_dersler.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (dersler != null && position > 0) {
                        acilacak_ders_id = (position - 1);

                        for (int i = 0; dersler.get(i) != null; i++) {
                            if (dersler.getJSONObject(i).getString("ders_adi") == spn_dersler.getSelectedItem().toString()) {
                                acilacak_ders_id = Integer.parseInt(dersler.getJSONObject(i).getString("id"));
                                ders_adi = dersler.getJSONObject(i).getString("ders_adi");
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

        btn_yoklama_baslat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_kacaklar.setText(" ");
                new Yoklam_ac().execute();
            }
        });

        btn_acik_ders_kapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Yoklamayi_Kapat().execute();
            }
        });


    }

    private void Gorsel_ac_kapa() {
        if (acilan_ders_id > 0) {
            spn_dersler.setVisibility(View.INVISIBLE);
            btn_yoklama_baslat.setVisibility(View.INVISIBLE);
            btn_acik_ders_kapat.setVisibility(View.VISIBLE);
            tv_acik_ders_bilgilendirme.setVisibility(View.VISIBLE);
            tv_acik_ders_bilgilendirme.setText(ders_adi + " dersi şuanda acık");
        } else {
            btn_acik_ders_kapat.setVisibility(View.INVISIBLE);
            tv_acik_ders_bilgilendirme.setVisibility(View.INVISIBLE);
            spn_dersler.setVisibility(View.VISIBLE);
            btn_yoklama_baslat.setVisibility(View.VISIBLE);
        }
    }

    public class Tumdersler extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            try {
                url = new URL("http://aliumutcankul.com/api/Ogretmenler/GET_Derler/" + id);
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
                while (dersler.isNull(sayac) == false) {
                    Log.d("dersler", dersler.getJSONObject(sayac).getString("ders_adi"));
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

    public class Yoklam_ac extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            try {
                url = new URL("http://aliumutcankul.com/api/Ogretmenler/POST_Ders_Ac/");
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");

                JSONObject acilacak_ders_bilgileri = new JSONObject();
                acilacak_ders_bilgileri.put("ders_id", acilacak_ders_id);
                acilacak_ders_bilgileri.put("ogretmen_id", id);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(acilacak_ders_bilgileri.toString());

                writer.flush();
                writer.close();
                os.close();


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
            Log.d("acilan ders cevap", s);
            JSONObject cevap;
            try {
                cevap = new JSONObject(s);
                acilan_ders_id = Integer.parseInt(cevap.getString("id"));
                Log.d("cavap", String.format("%d", acilan_ders_id));
                Gorsel_ac_kapa();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class Yoklamayi_Kapat extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            try {
                url = new URL("http://aliumutcankul.com/api/Ogretmenler/GET_Ders_Kapa/" + acilan_ders_id);
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
            Log.d("acık dersler", s);

            String cevap= String.format("%s",s);
            JSONArray veri;

            try{
                if (cevap.indexOf("durum")>0 && Boolean.parseBoolean(new JSONObject(s).getString("durum"))==true){
                    acilan_ders_id=0;
                    Gorsel_ac_kapa();
                }else if (cevap.indexOf("durum")>0 && Boolean.parseBoolean(new JSONObject(s).getString("durum"))==false){
                    tv_acik_ders_bilgilendirme.setText(ders_adi + " dersi kapatılamadı");
                }else if (cevap.indexOf("id")>0){
                    veri=new JSONArray(s);
                    for(int i=0;veri.length()>i;i++){
                        tv_kacaklar.setText(tv_kacaklar.getText()+"\nAdı soyadı: "+veri.getJSONObject(i).getString("adi_soyadi")+" Numarası: "+veri.getJSONObject(i).getString("no"));
                    }
                    acilan_ders_id=0;
                    Gorsel_ac_kapa();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e){
                Log.d("hata: ",e.getMessage());
            }


        }
    }
}
