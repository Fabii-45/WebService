package fr.info.orleans.ws.motusws.modele.exceptions;

public class MaxNbCoupsException extends Exception {

    public MaxNbCoupsException() {
        super("nombre maximum de coups atteint");
    }

}
