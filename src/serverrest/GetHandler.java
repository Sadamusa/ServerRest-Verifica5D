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

            if (validazioneParametri(parametri)) {
                inviaErrore(exchange, 400, "Parametri mancanti. Necessari: giocata, numero");
                return;
            }

            // Parsing dei valori
            String giocata = parametri.get("giocata");
            int    numero  = Integer.parseInt(parametri.get("numero"));

            // Logica di calcolo
            boolean vittoria = Service.logicaDiGioco(giocata, numero);

            // Risposta
            Response response = new Response(giocata, numero, vittoria == true ? "Vittoria" : "Sconfitta" );
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

    // Restituisce true se manca qualche parametro obbligatorio
    private boolean validazioneParametri(Map<String, String> parametri) {
        return !parametri.containsKey("giocata") || !parametri.containsKey("numero");
    }

    private Map<String, String> estraiParametri(String query) {
        Map<String, String> parametri = new HashMap<>();
        if (query == null || query.isEmpty()) return parametri;

        for (String coppia : query.split("&")) {
            String[] keyValue = coppia.split("=");
            if (keyValue.length == 2) {
                try {
                    parametri.put(
                        URLDecoder.decode(keyValue[0], "UTF-8"),
                        URLDecoder.decode(keyValue[1], "UTF-8")
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
