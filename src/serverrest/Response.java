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
    /*
    Sinceramente qua non ho capito cosa ho sbagliato.
    Nella verifica poteva essere sia Boolean che String e io per problemi progettuali ho scelto String
    */
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

    @Override
    public String toString() {
        return "Response{" + "giocata=" + giocata + ", numero=" + numero + ", vittoria=" + vittoria + '}';
    }
}
