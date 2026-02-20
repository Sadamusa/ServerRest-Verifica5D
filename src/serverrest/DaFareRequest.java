/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serverrest;

/**
 *
 * @author delfo
 */
public class DaFareRequest {
    private String giocata;
    private Integer numero;
    
    
    // Costruttore vuoto necessario per GSON
    public DaFareRequest() {
    }

    public DaFareRequest(String giocata, Integer numero) {
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

    @Override
    public String toString() {
        return "DaFareRequest{" + "giocata=" + giocata + ", numero=" + numero + '}';
    }
    
    
    
}