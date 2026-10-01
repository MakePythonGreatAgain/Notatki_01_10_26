import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class SzablonKolekcje {

    public static void main(String[] args) {

        // ==============================================================================
        // ZADANIE 1: WYLOSUJ 100 LICZB (Wykorzystanie ArrayList i Random)
        // ==============================================================================
        // Dlaczego ArrayList? Bo trzyma kolejność i pozwala na powtórki.
        List<Integer> liczby = new ArrayList<>();
        Random maszynaLosujaca = new Random();

        // Pętla wykona się równo 100 razy
        for (int i = 0; i < 100; i++) {
            // JAK MODYFIKOWAĆ LOSOWANIE?
            // Wzór: nextInt(Maksymalna - Minimalna + 1) + Minimalna
            // Losujemy od 1 do 100:
            int wylosowana = maszynaLosujaca.nextInt(100) + 1;

            // Jak użyć z Math.random()?
            // int wylosowana = (int)(Math.random() * 100) + 1;

            liczby.add(wylosowana);
        }
        System.out.println("Zad 1. Wylosowano " + liczby.size() + " liczb. Pierwsza to: " + liczby.get(0));


        // ==============================================================================
        // ZADANIE 2: POLICZ ILE JEST UNIKATOWYCH (Wykorzystanie HashSet)
        // ==============================================================================
        // Magia HashSetu polega na tym, że ignoruje duplikaty.
        // Konstruktor new HashSet<>(liczby) automatycznie wrzuca całą starą listę
        // i filtruje ją bez ani jednego 'if-a'!
        Set<Integer> unikalne = new HashSet<>(liczby);
        System.out.println("Zad 2. Z 100 liczb unikalnych (bez powtórek) jest: " + unikalne.size());


        // ==============================================================================
        // ZADANIE 3: ZAPISZ WARTOŚCI PARZYSTE DO NOWEJ LISTY (Filtrowanie ifami)
        // ==============================================================================
        List<Integer> parzyste = new ArrayList<>();

        // Przechodzimy przez każdą 'liczba' z naszej wylosowanej listy
        for (int liczba : liczby) {
            // Operator % (modulo) wyciąga resztę z dzielenia.
            // Jeśli podzielimy przez 2 i nie ma reszty (== 0), to jest parzysta!
            if (liczba % 2 == 0) {
                parzyste.add(liczba);
            }
            // Zobacz do pliku TXT jak zmienić tego if-a na nieparzyste, ujemne itp.
        }
        System.out.println("Zad 3. Z wylosowanych liczb parzystych jest: " + parzyste.size());


        // ==============================================================================
        // ZADANIE 4: ILE WYLOSOWAŁO SIĘ LICZB PIERWSZYCH? (Algorytmy)
        // ==============================================================================

        // METODA 1: SITO ERATOSTENESA (Dobre gdy masz narzucony mały zakres np. 1 do 100)
        // Sito to po prostu tablica z odpowiedziami "prawda/fałsz".
        boolean[] odpowiedziSita = generujSito(100);
        int zliczPierwszeSito = 0;

        for (int liczba : liczby) {
            // Jeśli pod indeksem wylosowanej liczby w tablicy kryje się true, jest to pierwsza!
            if (odpowiedziSita[liczba] == true) {
                zliczPierwszeSito++;
            }
        }
        System.out.println("Zad 4. Liczb pierwszych (według Sita) było: " + zliczPierwszeSito);


        // METODA 2: KLASYCZNA PĘTLA (Uniwersalna, bezpieczniejsza dla losowych/wielkich liczb)
        int zliczPierwszeZwykla = 0;
        for (int liczba : liczby) {
            if (czyJestPierwsza(liczba)) { // Wywołujemy metodę pomocniczą
                zliczPierwszeZwykla++;
            }
        }
        System.out.println("Zad 4. Liczb pierwszych (klasyczna metoda) było: " + zliczPierwszeZwykla);


        // ==============================================================================
        // DODATKOWE TRIKI (Do wykorzystania wedle potrzeb na kartkówce)
        // ==============================================================================

        // SZUKANIE MAXYMALNEJ / MINIMALNEJ (Gotowce z klasy Collections)
        int najwyzsza = Collections.max(liczby);
        int najnizsza = Collections.min(liczby);
        System.out.println("EXTRA: Największa wylosowana to: " + najwyzsza);

        // SORTOWANIE
        List<Integer> doPosortowania = new ArrayList<>(liczby); // Robimy kopię
        Collections.sort(doPosortowania); // Ustawia rosnąco

        // LICZENIE SUMY I ŚREDNIEJ
        int suma = 0;
        for (int x : liczby) {
            suma = suma + x;
        }
        // Uwaga: aby średnia miała przecinek, musisz dopisać słówko (double)!
        double srednia = (double) suma / liczby.size();
        System.out.println("EXTRA: Średnia wylosowanych liczb wynosi: " + srednia);
    }


    // ==============================================================================
    // GOTOWE METODY DO SKOPIOWANIA POZA "MAIN" (Tzw. narzędziownik)
    // ==============================================================================

    // NARZĘDZIE 1: SITO ERATOSTENESA (Zwraca tablicę gotowych odpowiedzi T/N)
    // Jak działa: zakładamy że wszystkie są pierwsze, a potem wykreślamy wielokrotności (np. 4,6,8,10).
    public static boolean[] generujSito(int max) {
        boolean[] tablica = new boolean[max + 1];

        // Krok 1: Wypełnij wszystko na start jako 'true' (od dwójki)
        for (int i = 2; i <= max; i++) {
            tablica[i] = true;
        }

        // Krok 2: Wykreślanie (Sito). Skoro 2 jest pierwsze, skreśl 4,6,8...
        for (int i = 2; i * i <= max; i++) {
            if (tablica[i] == true) { // Znaleźliśmy liczbę pierwszą!
                for (int j = i * i; j <= max; j += i) {
                    tablica[j] = false; // To jest jej wielokrotność, więc usuwamy z puli pierwszych
                }
            }
        }
        return tablica;
    }

    // NARZĘDZIE 2: POJEDYNCZE SPRAWDZANIE LICZBY PIERWSZEJ
    // Jak działa: po prostu próbuje podzielić przez wszystko aż do jej pierwiastka (Math.sqrt).
    // Jeśli się przez coś podzieli (reszta 0), to odpada.
    public static boolean czyJestPierwsza(int liczba) {
        if (liczba < 2) return false; // 0 i 1 nie są pierwsze

        // Math.sqrt() to pierwiastek. Przyspiesza kod, bo nie musimy sprawdzać dzielników w nieskończoność.
        for (int i = 2; i <= Math.sqrt(liczba); i++) {
            if (liczba % i == 0) {
                return false; // Znalazł się dzielnik - uciekamy!
            }
        }
        return true; // Przeszła test - jest pierwsza!
    }
}