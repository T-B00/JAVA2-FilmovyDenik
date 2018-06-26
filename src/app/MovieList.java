package app;

import utils.CompareByName;
import utils.CompareByRating;
import utils.CompareByYear;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;

/**
 * Seznam filmu
 *
 * @author Jaroslav Vít
 */
public class MovieList {

    private static final ArrayList<Movie> movies = new ArrayList<>();

    /**
     * Vytvoreni tabulky se seznamem filmu do PDF ve slozce tables
     *
     * @param dest = nazev souboru
     * @throws IOException
     * @throws DocumentException
     */
    public void createPdf(String dest) throws IOException, DocumentException {
        File file = new File(dest);
        file.getParentFile().mkdirs();
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(dest));
        document.open();
        PdfPTable table = new PdfPTable(7);
        table.addCell("Jmeno");
        table.addCell("Rok");
        table.addCell("Hodnoceni");
        table.addCell("Popis");
        table.addCell("Poznamka");
        table.addCell("Videl?");
        table.addCell("Datum");
        for (Movie m : movies) {
            table.addCell(m.getJmeno());
            table.addCell(String.valueOf(m.getRok()));
            table.addCell(String.valueOf(m.getHodnoceni()));
            table.addCell(m.getPopis());
            table.addCell(m.getPoznamka());
            table.addCell(String.valueOf(m.getVidel()));
            table.addCell(m.getDate());
        }
        document.add(table);
        document.close();
    }

    /**
     * Prida film do seznamu a zkontroluje duplicitu pouze na zaklade nazvu a
     * roku.
     *
     * @param m
     */
    public void addMovieToList(Movie m) {
        Boolean duplicity = false;
        for (Movie v : movies) {
            if ((v.getJmeno()).equals(m.getJmeno()) && (v.getRok()) == (m.getRok())) {
                duplicity = true;
            }
        }

        if (!duplicity) {
            movies.add(m);
            System.out.println("Film pridan!");
        } else {
            System.out.println("Uz ho mame...");
        }
    }

    /**
     * Setridi list dle nazvu filmu A-Z
     */
    public void sortByName() {
        Collections.sort(movies, new CompareByName());
    }

    /**
     * Setridi list dle roku natoceni filmu od nejstarsiho po nejnovejsi
     */
    public void sortByYear() {
        Collections.sort(movies, new CompareByYear());
    }

    /**
     * Setridi list dle hodnoceni od nejlepsiho po nejhorsi
     */
    public void sortByRating() {
        Collections.sort(movies, new CompareByRating());
    }

    /**
     * Vypise seznam filmu
     *
     * @param videl
     * @return
     */
    public String displayMovieList(int videl) {
        StringBuilder sb = new StringBuilder();
        sb.append("+-----+--------------------------------+------+------+-------------------------------------------------------+-------------------------+-------+----------------------+\n");
        sb.append(String.format("| %3s | %30s | %4s | %4s | %53s | %23s | %5s | %20s |","ID", "Nazev filmu", "Rok", "Hod.", "Popis", "Poznamka", "Videl", "Datum"));
        //sb.append("\n| ID | \t\t  Jméno\t   Rok  Hodnoceni\t\t\tPopis\t\t\t\t\tPoznámka\t Videl?  Datum pridani");
        sb.append("\n");
        sb.append("+-----+--------------------------------+------+------+-------------------------------------------------------+-------------------------+-------+----------------------+");
        sb.append("\n");
        for (Movie a : movies) {
            if (a.getVidel() != videl) {
                sb.append("| ").append(String.format("%2d",movies.indexOf(a))).append(" ");
                sb.append(a);
                sb.append("\n");
            }
        }
        sb.append("+-----+--------------------------------+------+------+-------------------------------------------------------+-------------------------+-------+----------------------+\n");
        return sb.toString();
    }

    /**
     * Nacte filmy ze souboru. Predpoklada se oddeleni atributu strednikem
     *
     * @param filename
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void readFromFile(String filename) throws FileNotFoundException, IOException {
        File f = new File(filename);
        if (f.exists()) {
            try (BufferedReader in = new BufferedReader(new FileReader(f));) {
                String line = in.readLine();// preskoceni radku s nadpisy
                while ((line = in.readLine()) != null) {
                    String[] casti = line.split(";");
                    String jmeno = casti[0];
                    int rok = Integer.parseInt(casti[1]);
                    double hod = Double.parseDouble(casti[2]);
                    String popis = casti[3];
                    String poznamka = casti[4];
                    int videl = Integer.parseInt(casti[5]);
                    Movie m = new Movie(jmeno, rok, hod, popis);
                    m.setPoznamka(poznamka);
                    m.setVidel(videl);
                    addMovieToList(m);
                }
            }
            
        }
        else
            throw new FileNotFoundException("Could not find file: " + filename);
    }

    /**
     * Zapise filmy do souboru. Atributy oddeli strednikem
     *
     * @param filename
     * @throws IOException
     */
    public void writeToFile(String filename) throws IOException {
        File f = new File(filename);
        //File f = new File("filmy.txt");
        try (BufferedWriter out = new BufferedWriter(new FileWriter(f));) {
            out.write("Jmeno;Rok;Hodnoceni;Popis;Poznamka;Videl?;DatumPridani");
            out.newLine();
            for (Movie m : movies) {  // se sezV nejde iterovat
                StringBuilder sb = new StringBuilder();
                sb.append(m.getJmeno()).append(";").append(m.getRok()).append(";").append(m.getHodnoceni()).append(";").append(m.getPopis()).append(";").append(m.getPoznamka()).append(" ").append(";").append(m.getVidel()).append(";").append(m.getDate());
                out.write(sb.toString());
                out.newLine();
            }
        }
    }

    /**
     * U filmu dle vyberu ID nastavi, ze film uz videl
     *
     * @param id
     */
    public void seenListUpdate(int id) {
        for (Movie m : movies) {
            if (movies.indexOf(m) == id) {
                m.setVidel(1);
            }
        }
    }

    /**
     * Doporuci nahodny film z tech, co jeste nevidel
     */
    public void recommendFromList() {
        Boolean temp = false;
        for (Movie m : movies) {
            if (m.getVidel() == 0) {
                temp = true;
            }
        }
        if (temp) {
            int rand = 0;
            do {
                rand = (int) (Math.random() * movies.size());
            } while (movies.get(rand).videl == 1);
            System.out.println(movies.get(rand).getJmeno() + ", " + movies.get(rand).getRok() + " (" + movies.get(rand).getHodnoceni() + "/10)");
        } else {
            System.out.println("Vsechno si videl");
        }
    }

    /**
     * Vymaze seznam filmu
     */
    public void delete() {
        movies.clear();
    }
}
