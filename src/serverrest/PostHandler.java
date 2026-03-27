/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package serverrest;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author delfo
 */
public class PostHandler implements HttpHandler {

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("POST")) {
            inviaErrore(exchange, 405, "Metodo non consentito. Usa POST");
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8)
            );
            Request request = gson.fromJson(reader, Request.class);
            reader.close();

            if (request == null) {
                inviaErrore(exchange, 400, "Body della richiesta vuoto o non valido");
                return;
            }

            // FIX [ERRATO - Problema 2]:
            // Il metodo di validazione originale controllava solo il campo "giocata",
            // ignorando completamente il campo "numero". Se il client inviava un body
            // JSON senza "numero", GSON impostava request.numero = null. La validazione
            // passava comunque, e la chiamata a Service.logicaDiGioco(..., request.getNumero())
            // causava una NullPointerException per auto-unboxing di Integer null in int.
            // L'eccezione veniva catturata dal blocco catch generico restituendo HTTP 500
            // invece del corretto HTTP 400.
            //
            // Soluzione: il metodo validazioneAssenzaParametri ora controlla anche che
            // request.getNumero() non sia null prima di proseguire.
            if (validazioneAssenzaParametri(request)) {
                inviaErrore(exchange, 400, "Parametri non validi: giocata (PARI/DISPARI) e numero (0-36) obbligatori");
                return;
            }

            // Logica di calcolo
            boolean vittoria = Service.logicaDiGioco(
                request.getGiocata(),
                request.getNumero()
            );

            // FIX [ERRATO - Problema 1]:
            // Come in GetHandler.java, il campo "vittoria" veniva costruito con:
            //   vittoria == true ? "Vittoria" : "Sconfitta"
            // producendo le stringhe non conformi "Vittoria" / "Sconfitta".
            // La specifica richiede i valori booleani "true" o "false".
            //
            // FIX [LIEVE - Problema 1]:
            // L'espressione "vittoria == true" è stilisticamente ridondante.
            // Corretto usando direttamente String.valueOf(vittoria) che produce
            // la stringa "true" o "false" in modo conciso e conforme alla specifica.
            Response response = new Response(
                request.getGiocata(),
                request.getNumero(),
                String.valueOf(vittoria) // FIX: "true"/"false" invece di "Vittoria"/"Sconfitta"
            );
            String jsonRisposta = gson.toJson(response);
            inviaRisposta(exchange, 200, jsonRisposta);

        } catch (JsonSyntaxException e) {
            inviaErrore(exchange, 400, "JSON non valido: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            inviaErrore(exchange, 400, e.getMessage());
        } catch (Exception e) {
            inviaErrore(exchange, 500, "Errore interno del server: " + e.getMessage());
        }
    }

    // FIX [ERRATO - Problema 2]:
    // Aggiunto controllo su request.getNumero() == null.
    // In precedenza il metodo verificava solo "giocata", permettendo che una richiesta
    // POST priva del campo "numero" superasse la validazione e causasse NullPointerException
    // durante l'auto-unboxing in Service.logicaDiGioco(), producendo una risposta HTTP 500
    // errata. Ora, se "numero" è assente (null), il metodo restituisce true (parametri
    // non validi) e viene inviata correttamente una risposta HTTP 400.
    //
    // Restituisce true se i parametri non sono validi (giocata assente/errata o numero null).
    private boolean validazioneAssenzaParametri(Request request) {
        // Controlla che "giocata" sia presente e non vuota
        if (request.getGiocata() == null || request.getGiocata().trim().isEmpty()) {
            return true;
        }
        // Controlla che "giocata" sia PARI o DISPARI
        String g = request.getGiocata().toUpperCase().trim();
        if (!g.equals("PARI") && !g.equals("DISPARI")) {
            return true;
        }
        // FIX: Controlla che "numero" sia presente (non null).
        // Senza questo controllo, l'auto-unboxing di Integer null in int
        // in Service.logicaDiGioco() generava NullPointerException -> HTTP 500.
        if (request.getNumero() == null) {
            return true;
        }
        return false;
    }

    private void inviaRisposta(HttpExchange exchange, int codice, String jsonRisposta)
            throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        byte[] bytes = jsonRisposta.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(codice, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    private void inviaErrore(HttpExchange exchange, int codice, String messaggio)
            throws IOException {
        Map<String, Object> errore = new HashMap<>();
        errore.put("errore", messaggio);
        errore.put("status", codice);
        inviaRisposta(exchange, codice, gson.toJson(errore));
    }
}
