package comm.example.aliumutcan.mobil_ogrenci_yoklama;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import JSON_VERİ_CEKME.POST_METHOD;

/**
 * Created by AliUmutcan on 10.04.2017.
 */

public class Fragment_Ogrenci_giris extends Fragment {

    TextView tv_bilgilendirme;
    EditText et_kullanici_adi;
    EditText et_sifre;
    Button btn_giris;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_ogrenci_giris,container,false);

        tv_bilgilendirme=(TextView)view.findViewById(R.id.tv_obilgilendirme);
        et_kullanici_adi=(EditText)view.findViewById(R.id.et_okullanici_adi);
        et_sifre=(EditText)view.findViewById(R.id.et_osifre);
        btn_giris=(Button)view.findViewById(R.id.btn_ogiris);

        btn_giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KullaniciSorgula sorgula=new KullaniciSorgula();
                String liste[] = new String[2];
                liste[0] = et_kullanici_adi.getText().toString();
                liste[1] = et_sifre.getText().toString();
                sorgula.execute(liste);

            }
        });

        et_kullanici_adi.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et_kullanici_adi.setText("");
                return false;
            }
        });

        et_sifre.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                et_sifre.setText("");
                return false;
            }
        });

        return view;
    }

    public class KullaniciSorgula extends AsyncTask<String, Void, String> {

        protected void onPreExecute(){
            tv_bilgilendirme.setText("Bilgiler Kontrol Ediliyor Lütfen Bekleyin....");
        }

        protected String doInBackground(String... parms) {


            HttpURLConnection conn=null;
            try {

                URL url = new URL("http://aliumutcankul.com/api/Ogrenci_islemleri/POST_Giris");

                JSONObject postDataParams = new JSONObject();
                postDataParams.put("kullanici_adi", parms[0]);
                postDataParams.put("sifre", parms[1]);


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
                Log.e("params",postDataParams.toString()+" "+String.format("%d",responseCode));
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

           String durum=String.format(""+s);
           if (durum.indexOf("false")<0){
                JSONObject json=null;
                try {
                    tv_bilgilendirme.setText("");
                    json=new JSONObject(s);
                    Intent yeni= new Intent(getContext(),ogrenci_ana_ekran.class);
                    yeni.putExtra("adi_soyadi",json.getString("adi_soyadi"));
                    yeni.putExtra("id",json.getString("id"));
                    yeni.putExtra("sinif",json.getString("sinif"));
                    yeni.putExtra("bolum",json.getString("bolum"));
                    yeni.putExtra("fakulte",json.getString("fakulte"));
                    yeni.putExtra("yoklama_id",json.getString("y_id"));
                    yeni.putExtra("ders_adi",json.getString("ders_adi"));
                    startActivity(yeni);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else{
               tv_bilgilendirme.setText("Kullanıcı Adı Veya Şifreniz yanlış");
           }

        }

    }

}
