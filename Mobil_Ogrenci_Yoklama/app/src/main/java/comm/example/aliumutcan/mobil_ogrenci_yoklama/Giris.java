package comm.example.aliumutcan.mobil_ogrenci_yoklama;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

import JSON_VERİ_CEKME.POST_METHOD;

public class Giris extends AppCompatActivity {

    TextView goster;
    String stringjson="";
    Fragment_Ogrenci_giris ogrenci;
    Fragment_Ogretmen_Girisi ogretmen;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);


        Button btn_ogrenci_giris=(Button)findViewById(R.id.btn_ogrenci_giris);
        Button btn_ogretmen_giris=(Button)findViewById(R.id.btn_ogretmen_giris);

        ogrenci=new Fragment_Ogrenci_giris();
        ogretmen=new Fragment_Ogretmen_Girisi();

        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentdegis=fragmentManager.beginTransaction();
        fragmentdegis.replace(R.id.relative_fragment,ogrenci);
        fragmentdegis.commit();
        btn_ogrenci_giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager1=getSupportFragmentManager();
                fragmentManager1.beginTransaction().replace(R.id.relative_fragment,ogrenci).commit();
                //FragmentTransaction fragmentdegis1=fragmentManager1.beginTransaction();
                //fragmentdegis1.replace(R.id.relative_fragment,ogrenci);
                //fragmentdegis1.commit();
            }
        });

        btn_ogretmen_giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager2=Giris.this.getSupportFragmentManager();
                fragmentManager2.beginTransaction().replace(R.id.relative_fragment,ogretmen).commit();
                //FragmentTransaction fragmentdegis2=fragmentManager2.beginTransaction();
                //fragmentdegis2.replace(R.id.relative_fragment,ogretmen);
                //fragmentdegis2.commit();
            }
        });

        ImageView selcuk_logo=(ImageView)findViewById(R.id.selcuk_logo);
        selcuk_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent siteye_git=new Intent(Intent.ACTION_VIEW);
                Uri link=Uri.parse("http://www.selcuk.edu.tr/");
                siteye_git.setData(link);
                startActivity(siteye_git);
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Giris Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
/*
    public class KullaniciSorgula extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){
            goster.setText("Bilgiler Kontrol Ediliyor Lütfen Bekleyin....");
        }

        protected String doInBackground(String... parms) {
            HttpURLConnection conn=null;
            try {

                URL url = new URL("http://aliumutcankul.com/api/Ogrenci_islemleri/POST_Giris"); // here is your URL path

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("kullanici_adi", parms[0]);
                postDataParams.put("sifre", parms[1]);

                Log.e("params",postDataParams.toString());

                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000 );
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type","application/json");
                conn.setDoInput(true);
                conn.setDoOutput(true);


                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(postDataParams.toString());

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();
                if (responseCode == 200) {

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {
                        sb.append(line);
                        //break;
                    }

                    in.close();
                    conn.disconnect();
                    return sb.toString();

                }
                else {
                    conn.disconnect();
                    return new String("false");
                }
            }
            catch(Exception e){
                conn.disconnect();
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s!="false"){
                JSONObject json=null;
                try {
                    json=new JSONObject(s);
                    Intent yeni= new Intent(getApplicationContext(),ogrenci_ana_ekran.class);
                    yeni.putExtra("adi_soyadi",json.getString("adi_soyadi"));
                    yeni.putExtra("id",json.getString("id"));
                    startActivity(yeni);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append(",");

            String keyolustur=URLEncoder.encode(key, "UTF-8");
            result.append("\""+keyolustur+"\"");
            result.append(":");
            keyolustur=URLEncoder.encode(value.toString(),"UTF-8");
            result.append("\""+keyolustur+"\"");

        }
        return result.toString();
    }
*/
}