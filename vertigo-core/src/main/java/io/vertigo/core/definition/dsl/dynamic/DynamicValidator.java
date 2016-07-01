/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2016, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
 * KleeGroup, Centre d'affaire la Boursidiere - BP 159 - 92357 Le Plessis Robinson Cedex - France
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.vertigo.core.definition.dsl.dynamic;

import java.util.HashSet;
import java.util.Set;

import io.vertigo.core.definition.dsl.entity.Entity;
import io.vertigo.lang.Assertion;

/**
 * 
 * Validate a definition considering its own entity.
 * 
 * @author pchretien
 *
 */
final class DynamicValidator {
	private DynamicValidator() {
		//utility Class
	}

	static void check(final DynamicDefinition definition) {
		Assertion.checkNotNull(definition);
		//-----
		final Entity myEntity = definition.getEntity();
		// 1.On vérifie la définition par rapport à la métadéfinition
		// 1.1 on vérifie les propriétés.
		final Set<String> propertyNames = definition.getPropertyNames();
		final Set<String> entityPropertyNames = myEntity.getPropertyNames();
		// 1.1.1 on vérifie que toutes les propriétés sont déclarées sur le
		// métamodèle
		checkProperties(definition, propertyNames, entityPropertyNames);

		// 1.1.2 on vérifie les propriétés obligatoires
		checkMandatoryProperties(definition, myEntity, propertyNames, entityPropertyNames);

		// 1.1.3 on vérifie les types des propriétés déclarées
		for (final String propertyName : propertyNames) {
			myEntity.getPrimitiveType(propertyName).checkValue(definition.getPropertyValue(propertyName));
		}

		// 1.2 on vérifie les définitions composites (sous définitions).
		for (final DynamicDefinition child : definition.getAllChildDefinitions()) {
			check(child);
		}

		// 1.3 on vérifie les définitions références.
		// TODO vérifier les définitions références
	}

	private static void checkProperties(
			final DynamicDefinition definition,
			final Set<String> propertyNames,
			final Set<String> entityPropertyNames) {
		// Vérification que toutes les propriétés sont déclarées sur le
		// métamodèle
		final Set<String> undeclaredPropertyNames = new HashSet<>();
		for (final String propertyName : propertyNames) {
			if (!entityPropertyNames.contains(propertyName)) {
				// Si la propriété n'est pas déclarée alors erreur
				undeclaredPropertyNames.add(propertyName);
			}
		}
		if (!undeclaredPropertyNames.isEmpty()) {
			throw new IllegalStateException("Sur l'objet '" + definition.getName() + "' Il existe des propriétés non déclarées " + undeclaredPropertyNames);
		}
	}

	private static void checkMandatoryProperties(
			final DynamicDefinition dynamicDefinition,
			final Entity myEntity,
			final Set<String> propertyNames,
			final Set<String> entityPropertyNames) {
		// Vérification des propriétés obligatoires
		final Set<String> unusedMandatoryPropertySet = new HashSet<>();
		for (final String propertyName : entityPropertyNames) {
			if (myEntity.isRequired(propertyName) && (!propertyNames.contains(propertyName) || dynamicDefinition.getPropertyValue(propertyName) == null)) {
				// Si la propriété obligatoire n'est pas renseignée alors erreur
				// Ou si la propriété obligatoire est renseignée mais qu'elle
				// est nulle alors erreur !
				unusedMandatoryPropertySet.add(propertyName);
			}
		}
		if (!unusedMandatoryPropertySet.isEmpty()) {
			throw new IllegalStateException(dynamicDefinition.getName() + " Il existe des propriétés obligatoires non renseignées " + unusedMandatoryPropertySet);
		}
	}
}
