package fr.eni.encheres.bll;



public class CodesErreurBLL {

    // Champs requis vides
    public static final int CHAMPS_VIDE_ERREUR = 40000;

    // Pseudo ne contient pas que des caractères alphanumériques

    public static final int ERREUR_FORMAT_PSEUDO = 40001;

    // Mdp et confirmation diffèrent

    public static final int ERREUR_SAISIE_MDP = 40002;

    // Les dates de début et/ou de fin d'enchères sont invalides

    public static final int ERREUR_DATE = 40003;

    // La description est trop longue

    public static final int ERREUR_FORMAT_DESCRIPTION = 40004;
}
