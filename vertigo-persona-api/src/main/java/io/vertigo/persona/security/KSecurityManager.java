package io.vertigo.persona.security;

import io.vertigo.kernel.component.Manager;
import io.vertigo.kernel.lang.Option;
import io.vertigo.persona.security.model.Role;

import java.util.Set;

/**
 * Gestion centralis�e des droits d'acc�s.
 *
 * @author npiedeloup
 * @version $Id: KSecurityManager.java,v 1.3 2013/10/23 11:53:43 pchretien Exp $
 */
public interface KSecurityManager extends Manager {
	//-------------------------------------------------------------------------
	//--------------Gestion de l'utilisateur (porteur des droits) -------------
	//-------------------------------------------------------------------------
	/**
	 * Accroche l'utilisateur au thread courant.
	 * S'effectue dans la servlet. Ne pas utiliser directement.
	 * @param userSession Session utilisateur (not null)
	 */
	void startCurrentUserSession(final UserSession userSession);

	/**
	 * R�initialise la session courante.
	 */
	void stopCurrentUserSession();

	/**
	 * R�cup�ration de la session utilisateur courante.
	 * @param <U> Session utilisateur
	 * @return Session utilisateur courante.
	 */
	<U extends UserSession> Option<U> getCurrentUserSession();

	/**
	 * Cr�ation de nouveaux utilisateurs. 
	 * @param <U> Type de l'utilisateur 
	 * @return Nouvel utilisateur
	 */
	<U extends UserSession> U createUserSession();

	/**
	 * Contr�le d'acc�s bas� sur les r�les.
	 * 
	 * L'utilisateur dispose-t-il des droits n�cessaires.
	 * <br/>
	 * <ul>
	 * <li>Si la liste des r�les autoris�s est vide, on consid�re que l'objet n'est pas soumis � autorisation et donc l'acc�s est accord�.</li>
	 * <li>Si la liste contient au moins un �l�ment alors l'objet est s�curis� et il est n�cessaire que
	 * l'utilisateur dispose d'au moins un des r�les autoris�s pour que l'acc�s soit accord�.</li>
	 * </ul>
	 *
	 * La fonction d'acc�s autorise la session utilisateur <code>null</code> : il faut alors que la liste des droits soit vide.
	 *
	 * @param userSession Session utilisateur. (non null)
	 * @param authorizedRoleSet Set des roles autoris�s. (non null)
	 *
	 * @return Si les droits de l'utilisateur lui permettent un acc�s.
	 */
	boolean hasRole(UserSession userSession, Set<Role> authorizedRoleSet);

	/**
	 * Contr�le d'acc�s bas� sur les permissions.
	 * 
	 * Indique si l'utilisateur courant a la permission d'effectuer l'op�ration
	 * donn�e sur la ressource donn�e.
	 * 
	 * @param resource la ressource
	 * @param operation l'op�ration
	 * @return true si l'utilisateur courant a la permission d'effectuer l'op�ration
	 * donn�e sur la ressource donn�e
	 */
	boolean isAuthorized(String resource, String operation);

	/**
	 * Contr�le d'acc�s bas� sur les permissions.
	 * 
	 * Indique si l'utilisateur courant a la permission d'effectuer l'op�ration
	 * donn�e sur la ressource donn�e.
	 * @param resourceType Type de la resource
	 * @param resource la ressource
	 * @param operation l'op�ration
	 * @return true si l'utilisateur courant a la permission d'effectuer l'op�ration
	 * donn�e sur la ressource donn�e
	 */
	boolean isAuthorized(String resourceType, Object resource, String operation);

	/**
	 * Enregistre une ResourceNameFactory sp�cifique pour un type donn�e.
	 * @param resourceType Type de la resource
	 * @param resourceNameFactory ResourceNameFactory sp�cifique
	 */
	void registerResourceNameFactory(final String resourceType, final ResourceNameFactory resourceNameFactory);
}