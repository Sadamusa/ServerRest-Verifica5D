/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package serverrest;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author delfo
 */
public class GetHandler implements HttpHandler {

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            inviaErrore(exchange, 405, "Metodo non consentito. Usa GET");
            return;
        }

        try {
            Map<String, String> parametri = estraiParametri(exchange.getRequestURI().getQuery());

            // FIX [LIEVE - Problema 1]:
            // Il nome "validazioneParametri" era fuorviante: il metodo restituiva TRUE
            // quando i parametri erano ASSENTI (logica invertita rispetto al nome).
            // Rinominato in "parametriAssenti" per rendere esplicita l'intenzione:
            // il blocco if entra quando mancano i parametri obbligatori.
            if (parametriAssenti(parametri)) {
                inviaErrore(exchange, 400, "Parametri mancanti. Necessari: giocata, numero");
                return;
            }

            // Parsing dei valori
            String giocata = parametri.get("giocata");
            int    numero  = Integer.parseInt(parametri.get("numero"));

            // Logica di calcolo
            boolean vittoria = Service.logicaDiGioco(giocata, numero);

            // FIX [ERRATO]:
            // In precedenza il campo "vittoria" veniva costruito con l'espressione
            //   vittoria == true ? "Vittoria" : "Sconfitta"
            // producendo le stringhe "Vittoria" / "Sconfitta", non conformi alla specifica.
            // La specifica richiede i valori booleani "true" o "false".
            //
            // FIX [LIEVE - Problema 3]:
            // L'espressione "vittoria == true" è una ridondanza stilistica: confrontare
            // un booleano con il letterale 'true' è inutile. Corretto usando direttamente
            // il valore booleano 'vittoria' come stringa tramite String.valueOf().
            //
            // Soluzione adottata: String.valueOf(vittoria) produce la stringa "true" o
            // "false" a seconda del valore booleano, rispettando la specifica.
            Response response = new Response(giocata, numero, String.valueOf(vittoria));
            String jsonRisposta = gson.toJson(response);
            inviaRisposta(exchange, 200, jsonRisposta);

        } catch (NumberFormatException e) {
            inviaErrore(exchange, 400, "Il parametro 'numero' deve essere un intero");
        } catch (IllegalArgumentException e) {
            inviaErrore(exchange, 400, e.getMessage());
        } catch (Exception e) {
            inviaErrore(exchange, 500, "Errore interno del server: " + e.getMessage());
        }
    }

    // FIX [LIEVE - Problema 1]:
    // Metodo rinominato da "validazioneParametri" a "parametriAssenti" per riflettere
    // correttamente il comportamento: restituisce TRUE quando i parametri obbligatori
    // sono assenti, FALSE quando sono entrambi presenti.
    // La logica interna rimane invariata.
    private boolean parametriAssenti(Map<String, String> parametri) {
        return !parametri.containsKey("giocata") || !parametri.containsKey("numero");
    }

    // FIX [LIEVE - Problema 2]:
    // Il metodo utilizzava URLDecoder.decode(String, String) con la stringa letterale
    // "UTF-8", firma deprecata a partire da Java 10 perché può lanciare
    // UnsupportedEncodingException (eccezione checked inutile per un encoding sempre
    // disponibile) e non è type-safe.
    // Corretto usando URLDecoder.decode(String, Charset) con StandardCharsets.UTF_8,
    // che è la forma moderna, type-safe e non deprecata.
    private Map<String, String> estraiParametri(String query) {
        Map<String, String> parametri = new HashMap<>();
        if (query == null || query.isEmpty()) return parametri;

        for (String coppia : query.split("&")) {
            String[] keyValue = coppia.split("=");
            if (keyValue.length == 2) {
                try {
                    parametri.put(
                        URLDecoder.decode(keyValue[0], StandardCharsets.UTF_8), // FIX: Charset al posto di String
                        URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8)  // FIX: Charset al posto di String
                    );
                } catch (Exception e) { /* ignora parametri malformati */ }
            }
        }
        return parametri;
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
