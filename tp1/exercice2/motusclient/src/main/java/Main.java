import com.fasterxml.jackson.core.JsonProcessingException;
import exceptions.*;
import proxy.MotusProxy;
import proxy.MotusProxyImpl;
import proxy.dto.EtatPartie;

import java.io.IOException;
import java.util.List;

public class Main {


    public static void main(String[] args) throws ErreurConnexionException, TicketInvalideException, PartieInexistanteException, MaxNbCoupsException, MotInexistantException, PseudoDejaPrisException, PartieDejaExistante, IOException {
        MotusProxy motusProxy = new MotusProxyImpl();
        String tokenAuthentification = motusProxy.creerUnCompte("Fabi11");
        //System.out.println("Token:"+tokenAuthentification);
        String tokenPartie = motusProxy.creerUnePartie(tokenAuthentification);
        //System.out.println(tokenPartie);


        List<String> tentatives = motusProxy.getPropositions(tokenPartie);
        EtatPartie etat = motusProxy.proposerMot(tokenPartie,"acheter");
        tentatives = motusProxy.getPropositions(tokenPartie);
        etat = motusProxy.proposerMot(tokenPartie,"blabla");
        tentatives = motusProxy.getPropositions(tokenPartie);
        System.out.println(etat.getVerdict());
        System.out.println(etat.getNbTentativesRestantes());
    }
}
