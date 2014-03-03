package io.vertigo.dynamo.domain.metamodel;

import io.vertigo.kernel.lang.Assertion;

/**
 * Propriété (meta-data, aspect, attribute) transverse gérée par l'application.
 * Certaines propriétés sont nativement gérées par Dynamo,
 * elles sont listées sous formes de constantes.
 * <br><br>
 * <u>Exemple :</u> le caractère obligatoire d'un champ est déclaré au niveau du modèle,
 * grâce à la propriété NOT_NULL. Cette information est utilisée pour
 * <ul>
 * <li>automatiser les tests métier unitaires coté serveur,</li>
 * <li>automatiser les tests de surface coté client (En utilisant par exemple le javascript),</li>
 * <li>modifier l'affichage de façon à renseigner l'utilisateur sur le caractère
 *      obligatoire du champ. (Exemple : libellé en gras ou astérisque à coté du champ</li>

 * Cette information ou propriété peut être directement portée par le champ voire
 * plus efficace portée par un domaine métier.
 * <br>
 * La finalité du domaine métier étant de dépasser les simples types techniques
 * afin de les enrichir avec une forte sémantique (le caractère obligatoire par exemple).
 * <br>
 * Cette riche sémantique étant utilisée de manière automatique et transparente
 * dans le framework Dynamo ou le framework commun de l'application.
 *
 * @author  pchretien
 * @version $Id: Property.java,v 1.3 2013/10/22 12:25:18 pchretien Exp $
 */
public final class Property<T> {
	/**
	 * Classe java représentant le type de la propriété.
	 */
	private final Class<T> clazz;
	private final String name;

	/**
	 * Constructeur à partir du nom évocateur de la propriété.
	 * @param clazz Classe java représentant le type de la propriété.
	 */
	public Property(final String name, final Class<T> clazz) {
		Assertion.checkArgNotEmpty(name);
		Assertion.checkNotNull(clazz);
		//----------------------------------------------------------------------
		this.clazz = clazz;
		this.name = name;
	}

	/**
	 * @return Nom de la propriété
	 */
	public String getName() {
		return name;
	}

	/**
	* @return Classe java représentant le type de la propriété.
	*/
	public Class<T> getType() {
		return clazz;
	}
}
