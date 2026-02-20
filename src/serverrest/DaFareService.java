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

    /**
     * Controlla se la giocata è vincente.
     *
     * @param giocata  il tipo puntato ("PARI" o "DISPARI")
     * @param numero   il numero estratto (0-36)
     * @return true se ha vinto, false se ha perso
     * @throws IllegalArgumentException se i parametri non sono validi
     */
    public static boolean logicaDiCalcolo(String giocata, int numero)
            throws IllegalArgumentException {

        if (!parametriValidi(giocata, numero)) {
            throw new IllegalArgumentException(
                "Parametri non validi. Giocata deve essere PARI o DISPARI, numero tra 0 e 36"
            );
        }

        String giocataUpper = giocata.toUpperCase().trim();

        if (giocataUpper.equals("PARI")) {
            return numero % 2 == 0;
        } else {
            // DISPARI
            return numero % 2 != 0;
        }
    }

    // Metodo di validazione dei parametri
    private static boolean parametriValidi(String giocata, int numero) {
        if (giocata == null || giocata.trim().isEmpty()) {
            return false;
        }
        String g = giocata.toUpperCase().trim();
        if (!g.equals("PARI") && !g.equals("DISPARI")) {
            return false;
        }
        if (numero < 0 || numero > 36) {
            return false;
        }
        return true;
    }
}
