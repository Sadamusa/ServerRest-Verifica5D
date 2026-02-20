/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serverrest;

/**
 *
 * @author delfo
 */
public class DaFareResponse {

    private String giocata;
    private Integer numero;
    private Boolean vittoria;

    // Costruttore vuoto necessario per GSON
    public DaFareResponse() {
    }

    // Costruttore con parametri
    public DaFareResponse(String giocata, Integer numero, Boolean vittoria) {
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

    public Boolean getVittoria() {
        return vittoria;
    }

    public void setVittoria(Boolean vittoria) {
        this.vittoria = vittoria;
    }

    @Override
    public String toString() {
        return "DaFareResponse{" + "giocata=" + giocata + ", numero=" + numero + ", vittoria=" + vittoria + '}';
    }

}
