import java.util.ArrayList;
import java.util.List;

// ==============================================================================
// ZADANIE 5: OBIEKTÓWKA - SZABLON DO KOPIOWANIA
// ==============================================================================

// --- KLASA 1: Zwykły model danych (np. Czytelnik, Mechanik, Klient) ---
class Czytelnik {
    // POLA PRYWATNE (Hermetyzacja) - Nikt z zewnątrz nie może ich bezpośrednio zmienić.
    private String imie;
    private String nazwisko;
    private int idCzytelnika;

    // KONSTRUKTOR - Używany, gdy piszesz w mainie: new Czytelnik("Jan", "Kowalski", 1)
    // Słówko 'this' oznacza "pole tej konkretnej klasy, a nie zmienną z nawiasu".
    public Czytelnik(String imie, String nazwisko, int idCzytelnika) {
        this.imie = imie;
        this.nazwisko = nazwisko;
        this.idCzytelnika = idCzytelnika;
    }

    // GETTER - pozwala odczytać prywatne pole z zewnątrz.
    public String getPelneImie() {
        return imie + " " + nazwisko;
    }
}

// --- KLASA 2: Obiekt docelowy, który ma stan (np. Książka, Samochód, Pokój w hotelu) ---
class Ksiazka {
    private String tytul;
    private String autor;

    // Zmienne stanu: czy wypożyczona, czy naprawiona, czy zajęta?
    private boolean czyWypozyczona;

    // OBIEKT W OBIEKCIE: Książka "pamięta" kto ją ma. To bardzo ważne na sprawdzianach!
    private Czytelnik ktoWypozyczyl;

    public Ksiazka(String tytul, String autor) {
        this.tytul = tytul;
        this.autor = autor;
        this.czyWypozyczona = false; // Domyślnie na start książka jest wolna
        this.ktoWypozyczyl = null;   // Null oznacza "pusto", nikt jej jeszcze nie trzyma
    }

    // Gettery do odczytu stanu
    public String getTytul() { return tytul; }
    public boolean isCzyWypozyczona() { return czyWypozyczona; }

    // METODA Z LOGIKĄ (Najważniejsza część)
    // Odpowiada za zmianę stanu. Zwraca boolean (true jak się udało, false jak był błąd)
    public boolean przypiszDoCzytelnika(Czytelnik czytelnik) {
        // Zabezpieczenie: najpierw sprawdzamy, czy w ogóle da się to zrobić
        if (this.czyWypozyczona == true) {
            System.out.println(" BŁĄD: Ksiazka '" + tytul + "' jest juz zajęta!");
            return false; // Przerwij działanie metody
        }

        // Jeśli doszliśmy tutaj, to znaczy że jest wolna. Przypisujemy:
        this.czyWypozyczona = true;
        this.ktoWypozyczyl = czytelnik;
        System.out.println(" SUKCES: '" + tytul + "' wedruje do: " + czytelnik.getPelneImie());
        return true;
    }
}

// --- KLASA 3: Menadżer / Magazyn (np. Biblioteka, Warsztat, Hotel) ---
class Biblioteka {
    // Kolekcja wszystkich obiektów. Tutaj lądują książki tworzone w mainie.
    private List<Ksiazka> katalog = new ArrayList<>();

    // Metoda dodająca nową książkę do systemu (zwykłe add do listy)
    public void dodajKsiazke(Ksiazka nowaKsiazka) {
        katalog.add(nowaKsiazka);
        System.out.println("Dodano na półkę: " + nowaKsiazka.getTytul());
    }

    // Skomplikowana metoda wyszukująca: szuka książki po tytule i wypożycza
    public void wypozyczSystemowo(String szukanyTytul, Czytelnik czytelnik) {
        // Pętla foreach - "Dla każdej Książki (nazwijmy ją 'k') z listy 'katalog'"
        for (Ksiazka k : katalog) {
            // .equalsIgnoreCase() porównuje teksty ignorując DUŻE/małe litery!
            if (k.getTytul().equalsIgnoreCase(szukanyTytul)) {
                // Znaleźliśmy książkę! Zlecamy jej zmianę stanu.
                k.przypiszDoCzytelnika(czytelnik);
                return; // Znaleźliśmy i załatwiliśmy sprawę - uciekamy z metody.
            }
        }
        // Jeśli pętla przejdzie cała i nie zrobi 'return', to znaczy że nie ma takiej książki.
        System.out.println(" BŁĄD: Nie mamy takiej ksiazki w systemie!");
    }
}

// --- GŁÓWNA KLASA URUCHOMIENIOWA ---
public class Main {
    public static void main(String[] args) {
        // 1. Inicjalizacja głównego systemu
        Biblioteka miejska = new Biblioteka();

        // 2. Tworzenie zasobów
        Ksiazka k1 = new Ksiazka("Lalka", "Bolesław Prus");
        Ksiazka k2 = new Ksiazka("Dziady", "Adam Mickiewicz");
        miejska.dodajKsiazke(k1);
        miejska.dodajKsiazke(k2);

        // 3. Tworzenie użytkowników
        Czytelnik c1 = new Czytelnik("Adam", "Nowak", 100);
        Czytelnik c2 = new Czytelnik("Ewa", "Kowalska", 200);

        System.out.println("\n--- START TESTÓW ---");

        // Sukces - Lalka jest wolna
        miejska.wypozyczSystemowo("Lalka", c1);

        // Błąd - Lalkę wziął już Adam
        miejska.wypozyczSystemowo("Lalka", c2);

        // Sukces - Dziady są wolne
        miejska.wypozyczSystemowo("Dziady", c2);

        // Błąd - Literówka lub brak w systemie
        miejska.wypozyczSystemowo("Dziay", c1);
    }
}