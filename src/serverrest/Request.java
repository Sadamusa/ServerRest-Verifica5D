/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serverrest;

/**
 *
 * @author delfo
 */
public class Request {

    private String giocata;

    // NOTA [LIEVE - Problema 2]:
    // Il campo "numero" è dichiarato di tipo Integer (wrapper) anziché int (primitivo).
    // Questo è funzionale per la deserializzazione GSON: se il campo manca nel JSON,
    // GSON imposta il wrapper a null invece di lanciare un'eccezione, permettendo
    // a PostHandler di rilevare l'assenza del campo nella fase di validazione.
    // Se fosse int primitivo, GSON impostarebbe il valore a 0 per default, rendendo
    // impossibile distinguere un numero mancante da un numero esplicitamente uguale a 0.
    // Il tipo Integer è quindi mantenuto intenzionalmente.
    private Integer numero;

    // Costruttore vuoto necessario per GSON
    public Request() {
    }

    public Request(String giocata, Integer numero) {
        this.giocata = giocata;
        this.numero = numero;
    }

    public String getGiocata() {
        return giocata;
    }

    public void setGiocata(String giocata) {
        this.giocata = giocata;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    // FIX [LIEVE - Problema 1]:
    // Il toString() originale restituiva "DaFareRequest{...}", un prefisso residuo
    // del template di verifica che non corrispondeva al nome reale della classe.
    // Corretto in "Request{...}" per allinearlo al nome effettivo della classe.
    @Override
    public String toString() {
        return "Request{" + "giocata=" + giocata + ", numero=" + numero + '}';
    }
}
