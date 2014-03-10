package io.vertigo.dynamo.impl.collections.functions.filter;

import io.vertigo.dynamo.domain.metamodel.DtDefinition;
import io.vertigo.dynamo.domain.metamodel.DtField;
import io.vertigo.dynamo.domain.metamodel.KDataType;
import io.vertigo.dynamo.domain.model.DtObject;
import io.vertigo.kernel.exception.VRuntimeException;
import io.vertigo.kernel.lang.Option;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parser des filtres utilisant une syntaxe définie.
 * @version $Id: DtListPatternFilterUtil.java,v 1.4 2014/01/20 17:45:43 pchretien Exp $
 */
final class DtListPatternFilterUtil {

	public static enum FilterPattern {
		/** range. */
		Range("([A-Z_0-9]+):([\\[\\]])(.*) TO (.*)([\\[\\]])"),
		/** term. */
		Term("([A-Z_0-9]+):\"(.*)\"");

		private final Pattern pattern;

		FilterPattern(final String patternString) {
			pattern = Pattern.compile(patternString);
		}

		Pattern getPattern() {
			return pattern;
		}
	}

	/**
	 * Constructeur privé.
	 */
	private DtListPatternFilterUtil() {
		//rien
	}

	static <D extends DtObject> DtListFilter<D> createDtListFilterForPattern(final FilterPattern filterPattern, final String[] parsedFilter, final DtDefinition dtDefinition) {
		//Si on trouve un pattern, on passe sur du code spécifique
		final String fieldName = parsedFilter[1]; //attention parsedFilter[0] = filtre entier
		final DtField dtField = dtDefinition.getField(fieldName);
		final KDataType dataType = dtField.getDomain().getDataType();

		switch (filterPattern) {
			case Range:
				return createDtListRangeFilter(parsedFilter, fieldName, dataType);
			case Term:
				return createDtListTermFilter(parsedFilter, fieldName, dataType);
			default:
				throw new VRuntimeException("La chaine de filtrage: {0} , ne respecte pas la syntaxe {1}.", null, parsedFilter[0], filterPattern.getPattern().pattern());
		}
	}

	/**
	 * Retourne les éléments parsés du filtre.
	 * index 0 : filtre d'origine.
	 * index 1 : nom du champs (par convention)
	 * ensuite dépend du pattern
	 **/
	static Option<String[]> parseFilter(final String filterString, final Pattern parsingPattern) {
		final String[] groups;
		int nbGroup = 0;
		final Matcher matcher = parsingPattern.matcher(filterString);
		if (!matcher.matches()) {
			return Option.none();
		}

		nbGroup = matcher.groupCount() + 1;
		groups = new String[nbGroup];
		for (int i = 0; i < nbGroup; i++) {
			groups[i] = matcher.group(i);
		}
		return Option.some(groups);
	}

	private static <D extends DtObject> DtListFilter<D> createDtListTermFilter(final String[] parsedFilter, final String fieldName, final KDataType dataType) {
		final Option<Comparable> filterValue = convertToComparable(parsedFilter[2], dataType, false);
		return new DtListValueFilter<>(fieldName, (Serializable) filterValue.get());
	}

	private static <D extends DtObject> DtListFilter<D> createDtListRangeFilter(final String[] parsedFilter, final String fieldName, final KDataType dataType) {
		final boolean isMinInclude = "[".equals(parsedFilter[2]);
		final Option<Comparable> minValue = convertToComparable(parsedFilter[3], dataType, true);
		final Option<Comparable> maxValue = convertToComparable(parsedFilter[4], dataType, true);
		final boolean isMaxInclude = "]".equals(parsedFilter[5]);
		return new DtListRangeFilter<>(fieldName, minValue, maxValue, isMinInclude, isMaxInclude);
	}

	private static Option<Comparable> convertToComparable(final String valueToConvert, final KDataType dataType, final boolean acceptJoker) {
		final String stringValue = valueToConvert.trim();
		if (acceptJoker && "*".equals(stringValue)) {
			return Option.none();//pas de test
		}
		final Comparable result;
		switch (dataType) {
			case Integer:
				result = Integer.valueOf(stringValue);
				break;
			case Long:
				result = Long.valueOf(stringValue);
				break;
			case BigDecimal:
				result = new BigDecimal(stringValue);
				break;
			case Double:
				result = Double.valueOf(stringValue);
				break;
			case Date:
				result = new Date();
				//TODO syntaxe complexe à parser
				break;
			case String:
				result = stringValue;
				break;
			case Boolean:
			case DataStream:
			case DtObject:
			case DtList:
			default:
				throw new VRuntimeException("Type de données non comparable : " + dataType.name());
		}
		return Option.some(result);
	}
}