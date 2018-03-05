package DATA_MODEL;

/**
 * Created by AliUmutcan on 1.05.2017.
 */

public class Ogrenciler {

    private int id;
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    private String adi_soyadi;
    public String getAdi_soyadi(){
        return adi_soyadi;
    }
    public  void setAdi_soyadi(String adi_soyadi){
        this.adi_soyadi=adi_soyadi;
    }
    private String no;
    public  String getNo(){
        return no;
    }
    public void setNo(String no){
        this.no=no;
    }
    private String kullanici_adi;
    public String getKullanici_adi(){
        return kullanici_adi;
    }
    public void setKullanici_adi(String kullanici_adi){
        this.kullanici_adi=kullanici_adi;
    }
    private String sifre;
    public String getSifre(){
        return sifre;
    }
    public void setSifre(String sifre){
        this.sifre=sifre;
    }
    private String fakulte;
    public String getFakulte()
    {
        return fakulte;
    }
    public void setFakulte(String fakulte){
        this.fakulte=fakulte;
    }
    private String bolum;
    public String getBolum(){
        return bolum;
    }
    public void setBolum(String bolum){
        this.bolum=bolum;
    }
    private byte sinif;
    public byte getSinif(){
        return sinif;
    }
    public void setSinif(byte sinif){
        this.sinif=sinif;
    }

}
