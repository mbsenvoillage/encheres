package fr.eni.encheres.messages;

// Permet de lire les messages d'erreur du fichier error_messages.properties

import java.util.ResourceBundle;

public abstract class LecteurMessage {
    private static ResourceBundle rb;

    static {
        try {
            // Intancie le ResourceBundle en lui attachant le fichier .properties
            rb = ResourceBundle.getBundle("fr.eni.encheres.messages.error_message");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Retourne les messages d'erreurs
    public static String getErrorMessage(int code) {
        String message = "";
        try {
            // Si le resource bundle n'est pas vide, on assigne le message du fichier error_message.properties à la variable message(string)
            if(rb != null) {
                message = rb.getString(String.valueOf(code));
            } else {

                // SI le rb est vide, alors on assigne un message d'erreur de lecture
                message = "Impossible de lire le fichier de messages d'erreurs";
            }

        } catch (Exception e) {
            // SI le code est inconnu, on assigne un message d'erreur inconnue
            e.printStackTrace();
            message = "Une erreur inconnue est survenue";
        }

        return message;
    }

}
