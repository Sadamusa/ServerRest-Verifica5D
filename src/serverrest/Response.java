/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serverrest;

/**
 *
 * @author delfo
 */
public class Response {

    private String giocata;
    private Integer numero;

    // NOTA [LIEVE - Problema 2]:
    // Il campo "vittoria" è dichiarato di tipo String. La specifica mostra negli esempi
    // i valori booleani nativi JSON true/false (senza virgolette), ma include una nota
    // che ammette anche una "stringa contenente il valore booleano".
    // Il tipo String è mantenuto per coerenza con la scelta progettuale originale;
    // i valori prodotti dal handler sono ora "true" / "false" grazie a String.valueOf().
    private String vittoria;

    // Costruttore vuoto necessario per GSON
    public Response() {
    }

    // Costruttore con parametri
    public Response(String giocata, Integer numero, String vittoria) {
        this.giocata = giocata;
        this.numero = numero;
        this.vittoria = vittoria;
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

    public String getVittoria() {
        return vittoria;
    }

    public void setVittoria(String vittoria) {
        this.vittoria = vittoria;
    }

    // FIX [LIEVE - Problema 1]:
    // Il toString() originale restituiva "DaFareResponse{...}", un prefisso residuo
    // del template di verifica che non corrispondeva al nome reale della classe.
    // Corretto in "Response{...}" per allinearlo al nome effettivo della classe.
    @Override
    public String toString() {
        return "Response{" + "giocata=" + giocata + ", numero=" + numero + ", vittoria=" + vittoria + '}';
    }
}
