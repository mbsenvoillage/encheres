package fr.eni.encheres.bll;

import fr.eni.encheres.BusinessException;
import fr.eni.encheres.bo.userBean;
import fr.eni.encheres.dal.CodesErreurDAL;
import fr.eni.encheres.dal.DAOFactory;
import fr.eni.encheres.dal.UserDAO;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserManager {
    private UserDAO user;

    public UserManager() {
        this.user = DAOFactory.getUserDAO();
    }

    // PERMET DE METTRE A JOUR LE CREDIT DE L'UTILISATEUR APRES ENCHERE

    public void updateUserCredit(int credit, int userNb) throws BusinessException {
        user.updateUserCredit(credit, userNb);
    }

    public userBean checkCredentials(userBean login) throws BusinessException {
        return this.user.checkID(login);
    }

    public userBean addUser(Map parametres, String pseudo, String email, userBean utilisateur, HttpServletRequest req) throws BusinessException {
        BusinessException bizEx = new BusinessException();

        // Tous le champs ont-ils étés remplis ?
        this.allFieldsAreFilled(parametres, bizEx);

        // Le format du pseudo est-il bon ?
        this.pseudoFormatOk(pseudo, bizEx);

        // Le mdp est-il identique à la confirmation

        if (!req.getParameter("mdp").equals(req.getParameter("confirmation"))) {
            bizEx.addError(CodesErreurBLL.ERREUR_SAISIE_MDP);
            throw bizEx;
        }

        // Si les tests ci-dessus sont passés, on vérifie si le pseudo et l'email sont dispos
        // Pour enfin ajouter l'utilisateur
        if (!bizEx.containsErrors()) {
            this.validerPseudo(pseudo);
            this.validerEmail(email);
            user.insertUser(utilisateur);
        } else {
            throw bizEx;
        }

        return utilisateur;
    }

    // Cette méthode vérifie si le pseudo est disponible

    public void validerPseudo(String pseudo) throws BusinessException {
        this.user.checkPseudo(pseudo);
    }

    // Cette méthode vérifie si l'email est disponible

    public void validerEmail(String email) throws BusinessException {
        this.user.checkEmail(email);
    }

    // Cette méthode s'assure que tous les champs obligatoires sont remplis

    private void allFieldsAreFilled(Map parametres, BusinessException biz) {
        for (Object o : parametres.keySet()) {
            String key = (String) o;
            String value = ((String[]) parametres.get(key))[0];
            // Si un champ n'est pas renseigné et que ce n'est pas le champ téléphone alors
            // l'utilisateur n'a pas rempli tous les champs
            if (value.trim().isEmpty() && !key.equals("telephone")) {
                biz.addError(CodesErreurBLL.CHAMPS_VIDE_ERREUR);
                break;
            }
        }
    }

    // Cette méthode s'assure que le pseudo ne contient que caractères alphanumériques

    private void pseudoFormatOk(String pseudo, BusinessException biz) {
        Pattern p = Pattern.compile("[^a-zA-Z0-9]");
        Matcher m = p.matcher(pseudo);
        while(m.find()) {
            if(!m.matches()) {
                biz.addError(CodesErreurBLL.ERREUR_FORMAT_PSEUDO);
                break;
            }
        }

    }

    public userBean displayUserPublicInfo(String pseudo) throws BusinessException {
        return this.user.selectUserPublicInfo(pseudo);
    }

    public userBean getUserPrivateInfo(String pseudo) throws BusinessException {
        return this.user.selectUserPrivateInfo(pseudo);
    }

    public boolean passwordCheck(userBean user) throws BusinessException {
        return this.user.passwordIsValid(user);
    }

    public userBean updateUserProfile(Map parametres, userBean currentUser, userBean modifs, HashMap<String, String> mdp, HttpServletRequest req) throws BusinessException {
        BusinessException bizEx = new BusinessException();

        // Tous le champs ont-ils étés remplis ?
        this.allFieldsAreFilled(parametres, bizEx);

        // Le format du pseudo est-il bon ?
        this.pseudoFormatOk(modifs.getPseudo(), bizEx);

        // Le nouveau mdp est-il identique à la confirmation ?

        if (!req.getParameter("nouveaumdp").equals(req.getParameter("confirmation"))) {
            bizEx.addError(CodesErreurBLL.ERREUR_SAISIE_MDP);
            throw bizEx;
        }

        // Si les test ci-dessus sont passés, on vérifie si le pseudo et l'email sont dispos
        // Pour enfin updater l'utilisateur
        if (!bizEx.containsErrors()) {

            // SI l'utilisateur a changé son pseudo, il faut vérifier sa disponibilité
            if (!modifs.getPseudo().equals(currentUser.getPseudo())) {
                this.validerPseudo(modifs.getPseudo());
            }

            // SI l'utilisateur a changé son email, il faut vérifier sa disponibilité
            if (!modifs.getEmail().equals(currentUser.getEmail())) {
                this.validerEmail(modifs.getEmail());
            }

            // check the old password

            if (!modifs.getMdp().equals(currentUser.getMdp())) {
                bizEx.addError(CodesErreurDAL.ECHEC_VALIDATION_PWD);
                throw bizEx;
            }

            // On donne au nouveau userBean l'identifiant de l'ancien
            modifs.setUserNb(currentUser.getUserNb());
            modifs.setConnecte(true);
            modifs.setCredit(currentUser.getCredit());

            // On fait l'update
            user.updateUserInfo(modifs);

        } else {
            throw bizEx;
        }

        return modifs;
    }

    public void supprimerUtilisateur(userBean user) throws BusinessException {
        this.user.deleteUser(user.getUserNb());
    }
}
// TODO : add format checks for email , cpo
