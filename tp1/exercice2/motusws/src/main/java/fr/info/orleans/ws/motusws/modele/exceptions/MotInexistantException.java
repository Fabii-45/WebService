package fr.info.orleans.ws.motusws.modele.exceptions;

public class MotInexistantException extends Exception {

    public MotInexistantException(String mot) {
        super("mot inconnu "+mot);
    }

}
