package proxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import exceptions.*;
import proxy.dto.EtatPartie;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.rmi.server.ExportException;
import java.util.List;

public class MotusProxyImpl implements MotusProxy{

    private HttpClient httpClient = HttpClient.newHttpClient();

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String creerUnCompte(String pseudo) throws PseudoDejaPrisException {
        HttpResponse response = null;
        try{
             HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/motus/joueur"))
                    .setHeader("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString("pseudo="+pseudo))
                    .build();
             response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e){
            System.out.println(e);
        }
        return response.toString();
    }

    @Override
    public String creerUnePartie(String tokenAuthentification) {
        HttpResponse response = null;
        try{
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8080/motus/partie"))
                    .setHeader("Content-Type", "application/x-www-form-urlencoded")
                    .header("Token=",tokenAuthentification)
                    .build();
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e){
            System.out.println(e);
        }
        return response.toString();
    }

    @Override
    public EtatPartie proposerMot(String tokenPartie, String proposition) throws MotInexistantException, MaxNbCoupsException, TicketInvalideException {
        return null;
    }

    @Override
    public List<String> getPropositions(String tokenPartie) throws TicketInvalideException, PartieInexistanteException {
        return null;
    }

}
