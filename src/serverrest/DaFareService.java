/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serverrest;

/**
 *
 * @author delfo
 */
public class DaFareService {

    /**
     * Esegue l'operazione matematica richiesta
     *
     * @param
     * @param
     * @param
     * @return
     * @throws IllegalArgumentException se ...
     */
    public static Boolean logicaDiGioco(String giocata, Integer numero, Boolean vittoria) throws IllegalArgumentException {

        // Controllo se i parametri passati sono validi
        if (!parametriValidi(giocata, numero)) {
            throw new IllegalArgumentException("Operatore non può essere vuoto");
        }

        try {
            
            if(numero == 0){
                vittoria = false;
            }
            else if(giocata.equalsIgnoreCase("dispari")&& numero%2 != 0){
                vittoria = true;
            }
            else if(giocata.equalsIgnoreCase("dispari")&& numero%2 == 0){
                vittoria = false;
            }
            else if(giocata.equalsIgnoreCase("pari")&& numero%2 != 0){
                vittoria =false;
            }
            else if(giocata.equalsIgnoreCase("pari")&& numero%2 != 0){
                vittoria = true;
            }

        } catch (Exception e) {
            throw new IllegalArgumentException(
                    "Opzione non valida. Opzione deve essere DA FARE");
        }
        return vittoria; // Placeholder, da sostituire con il risultato della logica di calcolo
    }

    // Metodo di validazione dei parametri (da implementare)
    private static boolean parametriValidi(String giocata, Integer numero) {        
        if(numero.equals("")){
            return false;
        }
        if(giocata.equalsIgnoreCase("dispari")){
            return true;
        }
        if(giocata.equalsIgnoreCase("pari")){
            return true;
        }
        return false;
    }
}
