package proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import exceptions.*;
import proxy.dto.EtatPartie;

import java.io.IOException;
import java.util.List;

public interface MotusProxy {

    String creerUnCompte(String pseudo) throws PseudoDejaPrisException, ErreurConnexionException;
    String creerUnePartie(String tokenAuthentification) throws PartieDejaExistante, ErreurConnexionException;
    EtatPartie proposerMot(String tokenPartie, String proposition) throws MotInexistantException, MaxNbCoupsException, TicketInvalideException, IOException;
    List<String> getPropositions(String tokenPartie) throws TicketInvalideException, PartieInexistanteException, JsonProcessingException;

}
