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

            if (validazioneaAssenzaParametri(request)) {
                inviaErrore(exchange, 400, "Parametri non validi: giocata (PARI/DISPARI) obbligatoria");
                return;
            }

            // Logica di calcolo
            boolean vittoria = Service.logicaDiGioco(
                request.getGiocata(),
                request.getNumero()
            );

            // Risposta
            /*
            TODO:
            --- File: GetHandler.java ---

            Il valore del campo "vittoria" nella risposta JSON viene costruito con
            l'espressione ternaria `vittoria == true ? "Vittoria" : "Sconfitta"`, producendo
            le stringhe "Vittoria" oppure "Sconfitta". La specifica richiede esplicitamente
            che il campo "vittoria" contenga i valori booleani "true" o "false" (come
            booleano nativo JSON o come stringa contenente la parola "true"/"false"). I
            valori "Vittoria" e "Sconfitta" non corrispondono in alcun modo al formato
            atteso e rendono la risposta non conforme alla specifica in tutti i casi in cui
            viene invocato l'endpoint GET.
            */
            Response response = new Response(
                request.getGiocata(),
                request.getNumero(),
                vittoria == true ? "Vittoria" : "Sconfitta"       );
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

    // Restituisce true se i parametri non sono validi
    /*
    TODO:
    Il metodo `validazioneParametri(Request request)` controlla unicamente la
    presenza e la validita' del campo "giocata", ma non verifica se il campo
    "numero" e' stato incluso nel JSON della richiesta. Quando il client invia un
    body JSON privo del campo "numero", GSON deserializza l'oggetto Request con il
    campo `numero` impostato a `null`. Il metodo di validazione restituisce
    comunque `false` (parametri ritenuti validi), consentendo l'esecuzione fino
    alla chiamata di `Service.logicaDiGioco(request.getGiocata(), request.getNumero())`,
    dove l'auto-unboxing dell'`Integer` null in tipo primitivo `int` genera una
    `NullPointerException`. L'eccezione viene catturata dal blocco `catch (Exception e)`
    generico e produce una risposta con codice HTTP 500 anziche' il corretto 400,
    segnalando un errore interno del server invece di un errore di validazione
    dell'input.
    */
    private boolean validazioneaAssenzaParametri(Request request) {
        if (request.getGiocata() == null || request.getGiocata().trim().isEmpty()) {
            return true;
        }
        String g = request.getGiocata().toUpperCase().trim();
        return !g.equals("PARI") && !g.equals("DISPARI");
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
