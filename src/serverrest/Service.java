/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serverrest;

/**
 *
 * @author delfo
 */
public class Service {

    // FIX [LIEVE - Problema 1]:
    // In precedenza erano presenti DUE blocchi Javadoc consecutivi per il metodo
    // logicaDiGioco: il primo era un residuo del template con tag @param e @return
    // vuoti e privi di descrizione; il secondo era il Javadoc corretto e completo.
    // La coesistenza creava ambiguità sulla documentazione ufficiale del metodo.
    // Rimosso il blocco incompleto; rimane solo il Javadoc corretto qui sotto.

    /**
     * Controlla se la giocata è vincente.
     *
     * @param giocata il tipo puntato ("PARI" o "DISPARI")
     * @param numero il numero estratto (0-36)
     * @return true se ha vinto, false se ha perso
     * @throws IllegalArgumentException se i parametri non sono validi
     */
    public static boolean logicaDiGioco(String giocata, int numero)
            throws IllegalArgumentException {

        if (!parametriValidi(giocata, numero)) {
            throw new IllegalArgumentException(
                    "Parametri non validi. Giocata deve essere PARI o DISPARI, numero tra 0 e 36"
            );
        }

        if (numero == 0) {
            return false;
        }

        String giocataUpper = giocata.toUpperCase().trim();

        if (giocataUpper.equals("PARI")) {
            return numero % 2 == 0;
        } else {
            // DISPARI
            return numero % 2 != 0;
        }
    }

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
