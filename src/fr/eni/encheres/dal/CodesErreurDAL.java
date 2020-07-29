package fr.eni.encheres.dal;

// Les codes dispos sont entre 10000 19999

public abstract class CodesErreurDAL {

    // Echec de l'insertion

    public static final int ECHEC_INSERT_OBJECT = 10004;

    // Objet null

    public static final int NULL_OBJECT_EXCEPTION = 10003;

    // Echec validation du login
    public static final int ECHEC_VALIDATION_LOGIN = 10000;

    // Echec du signup
    public static final int ECHEC_SIGNUP_PSEUDO_INUSE = 10001;
    public static final int ECHEC_SIGNUP_EMAIL_INUSE = 10002;
}
