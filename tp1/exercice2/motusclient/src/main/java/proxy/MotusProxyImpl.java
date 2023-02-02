package proxy;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import exceptions.*;
import proxy.dto.EtatPartie;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MotusProxyImpl implements MotusProxy{

    private HttpClient httpClient = HttpClient.newHttpClient();

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String creerUnCompte(String pseudo) throws PseudoDejaPrisException, ErreurConnexionException {
        HttpResponse response = null;
        try{
             HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/motus/joueur"))
                    .setHeader("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString("pseudo="+pseudo))
                    .build();
             response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
        } catch (Exception e){
            System.out.println(e);
        }
        if(response.statusCode() == 201) {
            Optional<String> rep = response.headers().firstValue("token");
            if (rep.isPresent()) {
                return rep.get();
            } else {
                throw new ErreurConnexionException();
            }
        } else if(response.statusCode() == 409){
            throw new PseudoDejaPrisException();
        } else {
            throw new ErreurConnexionException();
        }
    }

    @Override
    public String creerUnePartie(String tokenAuthentification) throws PartieDejaExistante, ErreurConnexionException {
        HttpResponse response = null;
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/motus/partie"))
                    .setHeader("Content-Type", "application/x-www-form-urlencoded")
                    .header("token",tokenAuthentification)
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
        } catch (Exception e){
            System.out.println(e);
        }
        if(response.statusCode() == 201) {
            System.out.println(response.headers());
            Optional<String> rep = response.headers().firstValue("tokenpartie");
            if (rep.isPresent()) {
                return rep.get();
            } else {
                throw new ErreurConnexionException();
            }
        } else if(response.statusCode() == 409){
            throw new PartieDejaExistante();
        } else {
            throw new ErreurConnexionException();
        }
    }

    @Override
    public EtatPartie proposerMot(String tokenPartie, String proposition) throws MotInexistantException, MaxNbCoupsException, TicketInvalideException, IOException {
        HttpResponse<String> response = null;
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/motus/partie"))
                    .setHeader("Content-Type", "application/x-www-form-urlencoded")
                    .header("tokenpartie",tokenPartie)
                    .PUT(HttpRequest.BodyPublishers.ofString("proposition="+proposition))
                    .build();
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
        } catch (Exception e){
            System.out.println(e);
        }

        if(response.statusCode() == 200) {
            System.out.println(response.headers());
            return objectMapper.readValue(response.body(), EtatPartie.class);
        } else if(response.statusCode() == 404){
            throw new MotInexistantException();
        } else if(response.statusCode() == 406){
            throw new MaxNbCoupsException();
        } else {
            throw new TicketInvalideException();
        }
    }

    @Override
    public List<String> getPropositions(String tokenPartie) throws TicketInvalideException, PartieInexistanteException, JsonProcessingException {
        HttpResponse response = null;
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/motus/partie"))
                    .setHeader("Content-Type", "application/x-www-form-urlencoded")
                    .header("tokenpartie",tokenPartie)
                    .GET()
                    .build();
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response);
        } catch (Exception e){
            System.out.println(e);
        }

        if(response.statusCode() == 200) {
            String coups = (String) response.body();
            CollectionType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, String.class);
            System.out.println(coups);
            return objectMapper.readValue(coups, javaType);
        } else if(response.statusCode() == 401){
            throw new TicketInvalideException();
        } else {
            throw new PartieInexistanteException();
        }
    }

}
