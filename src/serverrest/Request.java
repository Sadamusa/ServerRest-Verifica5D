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
    /*
    Non ho commesso errore:
    Nel codice originale numero era Integer. 
    A quanto pare in linea teorica int sarebbe più coerente con la natura del campo, 
    Ma l'uso di Integer era intenzionale e corretto ai fini della deserializzazione GSON.       
    */
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
    /*
    Il toString() era chiamato DaFareRequest invece che Request normale
    */
    @Override
    public String toString() {
        return "Request{" + "giocata=" + giocata + ", numero=" + numero + '}';
    }
}
