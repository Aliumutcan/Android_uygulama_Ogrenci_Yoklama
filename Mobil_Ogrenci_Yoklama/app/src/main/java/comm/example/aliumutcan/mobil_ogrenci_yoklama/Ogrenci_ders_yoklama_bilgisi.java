package comm.example.aliumutcan.mobil_ogrenci_yoklama;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.StreamHandler;

public class Ogrenci_ders_yoklama_bilgisi extends AppCompatActivity {

    int ogrenci_id=0;
    Button btn_ogrenci_yoklama_bilgilerini_getir;
    Spinner spn_ogrencinin_dersleri;
    ListView lv_ogrenci_yoklam_bilgisi;
    JSONArray dersler;
    JSONArray yoklama_tarihleri;
    int ders_id=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ogrenci_ders_yoklama_bilgisi);

        Intent ana_erkrandan_gelen=getIntent();
        ogrenci_id=Integer.parseInt(ana_erkrandan_gelen.getStringExtra("ogrenci_id"));

        Log.d("ogrenci id",String.format("%d",ogrenci_id));
        btn_ogrenci_yoklama_bilgilerini_getir=(Button)findViewById(R.id.btn_yoklama_bilgisini_getir);
        spn_ogrencinin_dersleri=(Spinner)findViewById(R.id.spn_ogrencinin_dersleri);
        lv_ogrenci_yoklam_bilgisi=(ListView)findViewById(R.id.lv_devamsizlik_bilgis);

        new Ogrencinin_Dersleri().execute();
        spn_ogrencinin_dersleri.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (dersler != null && position > 0)
                    {
                        for (int i=0;dersler.get(i)!=null;i++){
                            if(dersler.getJSONObject(i).getString("ders_adi")==spn_ogrencinin_dersleri.getSelectedItem().toString()){
                                ders_id=Integer.parseInt(dersler.getJSONObject(i).getString("id"));
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


        btn_ogrenci_yoklama_bilgilerini_getir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("idler",String.format("ders id=%d ogrenci_id=%d",ders_id,ogrenci_id));
                new Ogrenci_Derse_Gelis_Tarihleri().execute();
            }
        });
    }
    public class Ogrencinin_Dersleri extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            try {
                url = new URL("http://aliumutcankul.com/api/Ogrenci_islemleri/GET_Tum_Dersler/" + ogrenci_id);
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
            Log.d("Öğrencinin dersleri", s);
            String cevap=String.format("%s",s).substring(1,s.length()-1);
            Log.d("dersleri vevap", cevap);
            try {
                List<String> gelen_dersler2 = new ArrayList<String>();
                gelen_dersler2.add("Bir ders secin");
                dersler = new JSONArray(cevap);
                int sayac = 0;
                while (dersler.isNull(sayac)==false) {
                    gelen_dersler2.add(dersler.getJSONObject(sayac).getString("ders_adi"));
                    Log.d("tek tek",dersler.getJSONObject(sayac).getString("ders_adi"));
                    sayac++;
                }
                // Creating adapter for spinner
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, gelen_dersler2);

                // Drop down layout style - list view with radio button
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spn_ogrencinin_dersleri.setAdapter(dataAdapter);


            } catch (Exception e) {
                Log.d("postExecute", e.getMessage());
            }

        }
    }

    public class Ogrenci_Derse_Gelis_Tarihleri extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            URL url;
            HttpURLConnection conn;
            try {
                url = new URL("http://aliumutcankul.com/api/Ogrenci_islemleri/GET_Ogrenci_Yoklama_Bilgisi/"+ders_id +"/"+ ogrenci_id);
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
            Log.d("yoklama",s);
            String[] gelenveriler= String.format(s).toString().replace('[',' ').replace('"',' ').split(",");
           for(int i=0;i<gelenveriler.length;i++){
                gelenveriler[i]=gelenveriler[i].trim().substring(0,10);
            }
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, gelenveriler);

            lv_ogrenci_yoklam_bilgisi.setAdapter(dataAdapter);


        }
    }

}
