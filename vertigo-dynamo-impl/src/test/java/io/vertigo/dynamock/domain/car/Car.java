package io.vertigo.dynamock.domain.car;

import io.vertigo.dynamo.domain.metamodel.annotation.DtDefinition;
import io.vertigo.dynamo.domain.metamodel.annotation.Field;
import io.vertigo.dynamo.domain.model.DtObject;
import io.vertigo.dynamo.domain.util.DtObjectUtil;

/**
 * Attention cette classe est générée automatiquement !
 * Objet de données Car
 */
@javax.persistence.Entity
@javax.persistence.Table(name = "CAR")
@DtDefinition
public final class Car implements DtObject {
	/** SerialVersionUID. */
	private static final long serialVersionUID = 1L;

	private Long id;
	private String make;
	private String model;
	private String description;
	private Integer year;
	private Integer kilo;
	private Integer price;
	private String motorType;
	private Long famId;

	/**
	 * Champ : PRIMARY_KEY.
	 * récupère la valeur de la propriété 'identifiant de la voiture'. 
	 * @return Long id <b>Obligatoire</b>
	 */
	@javax.persistence.Id
	@javax.persistence.SequenceGenerator(name = "sequence", sequenceName = "SEQ_CAR")
	@javax.persistence.GeneratedValue(strategy = javax.persistence.GenerationType.SEQUENCE, generator = "sequence")
	@javax.persistence.Column(name = "ID")
	@Field(domain = "DO_IDENTIFIANT", type = "PRIMARY_KEY", notNull = true, label = "identifiant de la voiture")
	public final Long getId() {
		return id;
	}

	/**
	 * Champ : PRIMARY_KEY.
	 * Définit la valeur de la propriété 'identifiant de la voiture'.
	 * @param id Long <b>Obligatoire</b>
	 */
	public final void setId(final Long id) {
		this.id = id;
	}

	/**
	 * Champ : DATA.
	 * récupère la valeur de la propriété 'Constructeur'. 
	 * @return String make <b>Obligatoire</b>
	 */
	@javax.persistence.Column(name = "MAKE")
	@Field(domain = "DO_KEYWORD", notNull = true, label = "Constructeur")
	public final String getMake() {
		return make;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Constructeur'.
	 * @param make String <b>Obligatoire</b>
	 */
	public final void setMake(final String make) {
		this.make = make;
	}

	/**
	 * Champ : DATA.
	 * récupère la valeur de la propriété 'Modéle'. 
	 * @return String model <b>Obligatoire</b>
	 */
	@javax.persistence.Column(name = "MODEL")
	@Field(domain = "DO_FULL_TEXT", notNull = true, label = "Modéle")
	public final String getModel() {
		return model;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Modéle'.
	 * @param model String <b>Obligatoire</b>
	 */
	public final void setModel(final String model) {
		this.model = model;
	}

	/**
	 * Champ : DATA.
	 * récupère la valeur de la propriété 'Descriptif'. 
	 * @return String description <b>Obligatoire</b>
	 */
	@javax.persistence.Column(name = "DESCRIPTION")
	@Field(domain = "DO_FULL_TEXT", notNull = true, label = "Descriptif")
	public final String getDescription() {
		return description;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Descriptif'.
	 * @param description String <b>Obligatoire</b>
	 */
	public final void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * Champ : DATA.
	 * récupère la valeur de la propriété 'Année'. 
	 * @return Integer year <b>Obligatoire</b>
	 */
	@javax.persistence.Column(name = "YEAR")
	@Field(domain = "DO_INTEGER", notNull = true, label = "Année")
	public final Integer getYear() {
		return year;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Année'.
	 * @param year Integer <b>Obligatoire</b>
	 */
	public final void setYear(final Integer year) {
		this.year = year;
	}

	/**
	 * Champ : DATA.
	 * récupère la valeur de la propriété 'Kilométrage'. 
	 * @return Integer kilo <b>Obligatoire</b>
	 */
	@javax.persistence.Column(name = "KILO")
	@Field(domain = "DO_INTEGER", notNull = true, label = "Kilométrage")
	public final Integer getKilo() {
		return kilo;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Kilométrage'.
	 * @param kilo Integer <b>Obligatoire</b>
	 */
	public final void setKilo(final Integer kilo) {
		this.kilo = kilo;
	}

	/**
	 * Champ : DATA.
	 * récupère la valeur de la propriété 'Prix'. 
	 * @return Integer price <b>Obligatoire</b>
	 */
	@javax.persistence.Column(name = "PRICE")
	@Field(domain = "DO_INTEGER", notNull = true, label = "Prix")
	public final Integer getPrice() {
		return price;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Prix'.
	 * @param price Integer <b>Obligatoire</b>
	 */
	public final void setPrice(final Integer price) {
		this.price = price;
	}

	/**
	 * Champ : DATA.
	 * récupère la valeur de la propriété 'Type de moteur'. 
	 * @return String motorType <b>Obligatoire</b>
	 */
	@javax.persistence.Column(name = "MOTOR_TYPE")
	@Field(domain = "DO_KEYWORD", notNull = true, label = "Type de moteur")
	public final String getMotorType() {
		return motorType;
	}

	/**
	 * Champ : DATA.
	 * Définit la valeur de la propriété 'Type de moteur'.
	 * @param motorType String <b>Obligatoire</b>
	 */
	public final void setMotorType(final String motorType) {
		this.motorType = motorType;
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * récupère la valeur de la propriété 'Famille'. 
	 * @return Long famId <b>Obligatoire</b>
	 */
	@javax.persistence.Column(name = "FAM_ID")
	@Field(domain = "DO_IDENTIFIANT", type = "FOREIGN_KEY", notNull = true, label = "Famille")
	public final Long getFamId() {
		return famId;
	}

	/**
	 * Champ : FOREIGN_KEY.
	 * Définit la valeur de la propriété 'Famille'.
	 * @param famId Long <b>Obligatoire</b>
	 */
	public final void setFamId(final Long famId) {
		this.famId = famId;
	}

	// Association : Famille non navigable

	// Association : Famille non navigable

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return DtObjectUtil.toString(this);
	}
}
