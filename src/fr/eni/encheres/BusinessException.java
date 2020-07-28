package fr.eni.encheres;

import java.util.ArrayList;
import java.util.List;

public class BusinessException extends Exception {

    private static final long serialvVersionUID = 1L;
    private List<Integer> errorList;

    // Le constructeur instancie la List
    public BusinessException() {
        super();
        this.errorList = new ArrayList<>();
    }

    // Ajouter l'erreur levée à la liste
    public void addError(int code) {
        // Si l'erreur n'est pas déjà présente, elle est ajoutée
        if (!this.errorList.contains(code)) {
            this.errorList.add(code);
        }
    }

    // Retourne un booléen qui permet de savoir si la liste contient ou non des erreurs
    public boolean containsErrors() {
        return this.errorList.size() > 0;
    }

    public List<Integer> getErrorList() {
        return this.errorList;
    }
}
