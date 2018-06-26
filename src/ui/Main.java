package ui;

import app.Movie;
import app.MovieList;
import com.itextpdf.text.DocumentException;
import java.io.IOException;
import java.util.Scanner;

//Dokumentace: Class Diagram, testování
/**
 * Filmový Deník
 *
 * @author Jaroslav Vít
 */
public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final MovieList l = new MovieList();

    /**
     * Hlavní trida pro ovladani aplikace
     *
     * @param args
     * @throws IOException
     * @throws DocumentException
     */
    public static void main(String[] args) throws IOException, DocumentException, Exception {
           
        boolean end = false;
        do {
            menu();
            switch (sc.nextInt()) {
                case 1:
                    addMovie();
                    break;
                case 2:
                    displayMovies();
                    break;
                case 3:
                    fileWrite();
                    break;
                case 4:
                    fileRead();
                    break;
                case 5:
                    seenUpdate();
                    break;
                case 6:
                    recommend();
                    break;
                case 7:
                    l.delete();
                    break;
                case 8:
                    exportToPDF();
                    break;
                default:
                    end = true;
                    break;
            }
        } while (!end);
    }

    /**
     * Slouzi pouze pro vypsani voleb menu.
     */
    private static void menu() {
        System.out.println("\n\n\nMENU");
        System.out.println("----------------------");
        System.out.println("1. Pridani filmu");
        System.out.println("2. Vypsani vsech filmu");
        System.out.println("3. Zapsani filmu do souboru");
        System.out.println("4. Cteni filmu ze souboru");
        System.out.println("5. Videl jsem film");
        System.out.println("6. Doporuc film");
        System.out.println("7. Vymaz seznam");
        System.out.println("8. Exportuj do PDF");
        System.out.println("0. Konec");
        System.out.println("----------------------");
        System.out.println("Volbu proved zadanim cislice: ");
    }

    /**
     * Po zvoleni vypisu se vetvi rozhodovani na filmy, ktere chceme vypsat a v
     * jakem poradi
     */
    private static void displayMovies() {
        System.out.printf("\n\nINFORMACE O FILMECH:\n\n");
        System.out.printf("Zobrazit ty, ktere jsem:");
        System.out.printf("\n 0=Videl, 1=Nevidel, 2=Vsechny\n");
        int videl = sc.nextInt();

        System.out.printf("\nVyberte metodu setrideni:");
        System.out.printf("\n 1=Jmeno, 2=Rok, 3=Hodnoceni\n");

        switch (sc.nextInt()) {
            case 1:
                l.sortByName();
                break;
            case 2:
                l.sortByYear();
                break;
            case 3:
                l.sortByRating();
                break;
            default:
                break;
        }

        System.out.println(l.displayMovieList(videl));
        System.out.println("\nPokracujte stiskem libovolne klavesy...");
        System.out.println(sc.next());
    }

    /**
     * Slouzi pro volani pridani filmu. Vetvi se na automaticke a manualni.
     *
     * @throws IOException
     */
    private static void addMovie() throws IOException {
        Movie m;
        System.out.println("Pridavej film...");
        System.out.println("Automaticky? 1/0");
        int a = sc.nextInt();
        if (a == 1) {

            while (true) {
                try {
                    System.out.println("Zadejte IMDB ID (napr. 0133093, 1219827, 0816692)");
                    m = new Movie(sc.next());
                    break;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

        } else {
            System.out.println("Zadejte nazev:");
            String jmeno = sc.nextLine();
            jmeno = sc.nextLine();
            int rok = 0;
            double hodnoceni=0;
            String temp;

            while (true) {
                System.out.println("Zadejte rok:");
                temp = sc.nextLine();
                if (temp.matches("[0-9]{4}")) {
                    rok = Integer.parseInt(temp);
                    break;
                }
            }

            while (true) {
                System.out.println("Zadejte hodnoceni:");
                temp = sc.nextLine();
                if (temp.matches("[0-9]{1}") || temp.equals("10")) {
                    hodnoceni = Double.parseDouble(temp);
                    break;
                }
            }

            System.out.println("Zadejte popisek:");
            //String popisek = sc.nextLine();
            String popisek = sc.nextLine();
            m = new Movie(jmeno, rok, hodnoceni, popisek);
        }

        System.out.println("Chcete pridat poznamku? 1/0");
        a = sc.nextInt();
        if (a == 1) {
            System.out.println("Zadejte poznamku");
            String poznamka = sc.nextLine();
            m.setPoznamka(sc.nextLine());
        }
        System.out.println("Videl jste film? 1/0");
        a = sc.nextInt();
        if (a == 1) {
            m.setVidel(1);
        }
        l.addMovieToList(m);
    }

    /**
     * Slouzi pro volani zapisu do souboru.
     *
     * @throws IOException
     */
    private static void fileWrite() throws IOException {
        System.out.printf("\n\nZapisuju do souboru...\n\n");
        System.out.println("Zadej nazev souboru:");
        String filename = sc.nextLine();
        l.writeToFile(sc.nextLine());
    }

    /**
     * Slouzi pro volani cteni ze souboru.
     *
     * @throws IOException
     */
    private static void fileRead() throws IOException {
        System.out.printf("\n\nCtu ze souboru...\n\n");
        String filename = sc.nextLine();
        while (true) {
            try {
                System.out.println("Zadej nazev souboru:");
                l.readFromFile(sc.nextLine());
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Slouzi pro volani aktualizace filmu na: "uz videl".
     */
    private static void seenUpdate() {
        System.out.println("Videl jste film? Napiste jeho ID.");
        int id = sc.nextInt();
        l.seenListUpdate(id);
    }

    /**
     * Slouzi pro volani generovani doporuceneho filmu
     */
    private static void recommend() {
        System.out.println("Doporucuji film:\n");
        l.recommendFromList();
        System.out.println("\nPokracuj stiskem libovolne klavesy...");
        String wait = sc.nextLine();
        wait = sc.nextLine();
    }

    /**
     * Slouzi pro volani zapisu do PDF do slozky "tables"
     *
     * @throws IOException
     * @throws DocumentException
     */
    private static void exportToPDF() throws IOException, DocumentException {
        System.out.println("Zadej nazev PDF souboru");
        String pdfname = sc.nextLine();
        pdfname = sc.nextLine();
        l.createPdf("tables/" + pdfname + ".pdf");
    }
}
