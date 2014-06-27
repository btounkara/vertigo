package io.vertigo.publisher.metamodel;

import io.vertigo.kernel.lang.Assertion;
import io.vertigo.kernel.lang.Option;

import java.util.regex.Pattern;


/**
 * D�finition de la structure d'un champ d'un noeud du mod�le d'�dition.
 * Tous les champs sont nomm�s et typ�s.
 * @author npiedeloup
 * @version $Id: PublisherField.java,v 1.4 2013/10/22 10:50:52 pchretien Exp $
 */
public final class PublisherField {
	/**
	 * Expression r�guli�re v�rifi�e par les noms des champs. 
	 */
	private static final Pattern REGEX_FIELD_NAME = Pattern.compile("[A-Z][A-Z0-9_]{2,59}");

	private final String name;
	private final PublisherFieldType fieldType;

	private final Option<PublisherNodeDefinition> nodeDefinition;

	/**
	 * Constructeur pour les champs composites (noeud de l'arbre de d�finition).
	 * @param name Nom du champ
	 * @param fieldType Type du champ
	 * @param publisherDataNodeDefinition D�finition du noeud sous-jacent
	 */
	PublisherField(final String name, final PublisherFieldType fieldType, final PublisherNodeDefinition publisherDataNodeDefinition) {
		Assertion.checkArgNotEmpty(name);
		Assertion.checkNotNull(fieldType);
		Assertion.checkArgument(REGEX_FIELD_NAME.matcher(name).matches(), "Le nom du champ {0} doit matcher le pattern {1}", name, REGEX_FIELD_NAME);
		if (publisherDataNodeDefinition != null) {
			Assertion.checkArgument(fieldType == PublisherFieldType.Node || fieldType == PublisherFieldType.List, "Le champ {0} n''est pas du bon type ({1}). Les champs de type Data ou List ont besoin d''une nodeDefinition", name, fieldType);
		} else {
			Assertion.checkArgument(fieldType != PublisherFieldType.Node && fieldType != PublisherFieldType.List, "Le champ {0} n''est pas du bon type ({1}). Seul les champs de type Data ou List ont besoin d''une nodeDefinition", name, fieldType);
		}
		//---------------------------------------------------------------------
		this.name = name;
		this.fieldType = fieldType;
		nodeDefinition = Option.option(publisherDataNodeDefinition);
	}

	/**
	 * Retourne le nom du champ.
	 * @return Nom du champ
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return Type du champ.
	 */
	public PublisherFieldType getFieldType() {
		return fieldType;
	}

	/**
	 * 
	 * Si et seulement si le champ est une liste ou un objet
	 */
	public Option<PublisherNodeDefinition> getNodeDefinition() {
		return nodeDefinition;
	}
}
