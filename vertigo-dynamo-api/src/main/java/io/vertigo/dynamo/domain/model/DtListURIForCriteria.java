package io.vertigo.dynamo.domain.model;

import io.vertigo.dynamo.domain.metamodel.DtDefinition;
import io.vertigo.dynamo.domain.metamodel.DtField;
import io.vertigo.dynamo.domain.util.DtObjectUtil;
import io.vertigo.dynamo.persistence.Criteria;
import io.vertigo.dynamo.persistence.FilterCriteriaBuilder;
import io.vertigo.kernel.lang.Assertion;

/**
 * Implementation d'une liste filtré par un Criteria.
 * @author dchallas
 * @version $Id: DtListURIForCriteria.java,v 1.2 2013/11/15 17:13:52 npiedeloup Exp $
 * @param <D> Type de DtObject
 */
public final class DtListURIForCriteria<D extends DtObject> extends DtListURI {
	private static final long serialVersionUID = 7926630153187124165L;
	private final int maxRows;
	private final Criteria<D> criteria;

	/**
	 * Constructeur.
	 *  @param dtDefinition Id de la Définition de DT
	 * @param criteria critere
	 * @param maxRows Nombre de ligne max
	 */
	public DtListURIForCriteria(final DtDefinition dtDefinition, final Criteria<D> criteria, final int maxRows) {
		super(dtDefinition);
		this.criteria = criteria;
		this.maxRows = maxRows;
	}

	/**
	 * Pour Récupération une liste filtrée par le champ saisie dans le dtoCritère.
	 * @param dtDefinition Id de la Définition de DT
	 * @param dtoCriteria critere
	 * @param maxRows Nombre de ligne max
	 * @deprecated Utiliser DtListURIForCriteria(final DtDefinition dtDefinition, final Criteria<D> criteria, final int maxRows)
	 */
	@Deprecated
	public DtListURIForCriteria(final DtDefinition dtDefinition, final DtObject dtoCriteria, final int maxRows) {
		this(dtDefinition, DtListURIForCriteria.<D> createCriteria(dtoCriteria), maxRows);
	}

	/**
	 * @return Criteres de la liste
	 */
	public Criteria<D> getCriteria() {
		return criteria;
	}

	/**
	 * @return Nombre de ligne max
	 */
	public int getMaxRows() {
		return maxRows;
	}

	/**
	* Construit automatiquement un Criteria à partir d'un DtObject de critère.
	* Les noms des champs dans l'objet de critère doivent correspondre à ceux de l'objet métier.
	* @param dtoCriteria Objet de critère
	* @return Criteria resultant
	*/
	private static <D extends DtObject> Criteria<D> createCriteria(final DtObject dtoCriteria) {
		Assertion.checkNotNull(dtoCriteria);
		//---------------------------------------------------------------------
		final FilterCriteriaBuilder<D> filterCriteriaBuilder = new FilterCriteriaBuilder<>();
		final DtDefinition dtDefinition = DtObjectUtil.findDtDefinition(dtoCriteria);

		for (final DtField field : dtDefinition.getFields()) {
			if (field.getType() != DtField.FieldType.COMPUTED) {
				final Object value = field.getDataAccessor().getValue(dtoCriteria);
				if (value instanceof String && field.getType() != DtField.FieldType.FOREIGN_KEY) {
					//si String et pas une FK : on met en préfix
					filterCriteriaBuilder.withPrefix(field.getName(), (String) value);
				} else if (value != null) {
					filterCriteriaBuilder.withFilter(field.getName(), value);
				}
			}
			//si null, alors on ne filtre pas
		}
		return filterCriteriaBuilder.build();
	}
}