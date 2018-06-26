package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Trida pro jednotlive filmy
 * @author Jaroslav Vít
 */
public class Movie {
    //int id;
    //private static int pocet = 0;
    String jmeno;
    int rok;
    Double hodnoceni;
    String popis;
    String poznamka;
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    Date date;
    int videl;

    /**
     * Konstruktor na zaklade jednotlivych atributu pro manualni zadavani
     * @param jmeno
     * @param rok
     * @param hodnoceni
     * @param popis
     */
    public Movie(String jmeno, int rok, Double hodnoceni, String popis) {
        this.jmeno = jmeno;
        this.rok = rok;
        this.hodnoceni = hodnoceni;
        this.popis = popis;
        this.poznamka = "";
        date = new Date();
        videl = 0;
        //this.id = ++pocet;
    }

    /**
     * Konstruktor na zaklade IMDb ID. Atributy se vplni automaticky
     * @param kod
     * @throws IOException
     * @throws Exception
     */
    public Movie(String kod) throws IOException, Exception {
        if (!kod.matches("[0-9]{7}")) {
            throw new Exception("Invalid IMDb code");
        }
        kod = htmlsrc2string("http://www.imdb.com/title/tt" + kod);
        Matcher regex = Pattern.compile("og:title' content=\"(.{1,30}) \\((\\d{4}).+ratingValue\">(\\d{1}.\\d{1}).+description.{1,30}<p>(.{1,70})").matcher(kod);

        if (regex.find()) {
            this.jmeno = regex.group(1);
            this.rok = Integer.parseInt(regex.group(2));
            this.hodnoceni = Double.parseDouble(regex.group(3));
            this.popis = regex.group(4);
            this.poznamka = "";
            date = new Date();
            videl = 0;
            //this.id = ++pocet;
            //sezA.pridejFilm(regex.group(1), Integer.parseInt(regex.group(2)), Double.parseDouble(regex.group(3)), regex.group(4), poznamka);
        } else {
            throw new Exception("Film neexistuje");
        }
    }

     /**
     * Z URL odkazu stranky ziskame String s celym zdrojovym kodem dane stranky v jednom radku
     * @param url
     * @throws IOException
     */
    private String htmlsrc2string(String url) throws IOException {
        System.out.println("Stahovani....");
        URL seznam = new URL(url);
        URLConnection yc = seznam.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        yc.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuilder kod = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            kod.append(inputLine);
        }
        in.close();
        return kod.toString();
    }

    public String getJmeno() {
        return jmeno;
    }

    public void setJmeno(String jmeno) {
        this.jmeno = jmeno;
    }

    public int getRok() {
        return rok;
    }

    public void setRok(int rok) {
        this.rok = rok;
    }

    public Double getHodnoceni() {
        return hodnoceni;
    }

    public void setHodnoceni(Double hodnoceni) {
        this.hodnoceni = hodnoceni;
    }

    public String getPopis() {
        return popis;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }

    public String getPoznamka() {
        return poznamka;
    }

    public void setPoznamka(String poznamka) {
        this.poznamka = poznamka;
    }

    public String getDate() {
        return dateFormat.format(date);
    }

    public int getVidel() {
        return videl;
    }

    public void setVidel(int videl) {
        this.videl = videl;
    }

     /**
     * toString metoda, ktera dokaze zkratit popis a poznamku pro citelnejsi a prehlednejsi vypis
     */
    @Override
    public String toString() {
        String temp = popis.length() > 50 ? popis.substring(0, 50) + "..." : popis;
        String temp2 = poznamka.length() > 20 ? poznamka.substring(0, 20) + "..." : poznamka;
        return String.format(" | %30s | %4d | %3.0f%% | %53s | %23s | %5s | %20s |", jmeno, rok, hodnoceni * 10, temp, temp2, videl == 1 ? "Ano" : "Ne", dateFormat.format(date));
    }
}
