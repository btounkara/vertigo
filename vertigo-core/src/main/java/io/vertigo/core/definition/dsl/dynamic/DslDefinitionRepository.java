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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import io.vertigo.core.definition.dsl.entity.DslGrammar;
import io.vertigo.core.spaces.definiton.Definition;
import io.vertigo.core.spaces.definiton.DefinitionSpace;
import io.vertigo.lang.Assertion;

/**
 * Espace de nommage.
 * Les éléments de la grammaire à savoir les définitions sont ajoutées à la volée.
 * Les définitions doivent respecter une métagrammaire définie préalablement dans DynamicNameSpace.
 *
 * @author  pchretien
 */
public final class DslDefinitionRepository {

	/***
	 * On retient les définitions dans l'ordre pour
	 * créer les fichiers toujours de la même façon.
	 */
	private final Map<String, DslDefinition> definitions = new LinkedHashMap<>();
	private final List<DslDefinition> partials = new ArrayList<>();

	private final DynamicRegistry registry;
	private final DslGrammar grammar;

	/**
	 * Constructeur.
	 * @param registry DynamicDefinitionHandler
	 */
	public DslDefinitionRepository(final DynamicRegistry registry) {
		Assertion.checkNotNull(registry);
		//-----
		this.registry = registry;
		grammar = registry.getGrammar();
	}

	/**
	 * @return Grammar
	 */
	public DslGrammar getGrammar() {
		return grammar;
	}

	/**
	 * Returns true if a definition to which the specified name is mapped.
	 * @param definitionName name of the definitionClé de la définition
	 * @return Si la définition a déjà été enregistrée
	 */
	public boolean containsDefinitionName(final String definitionName) {
		return definitions.containsKey(definitionName);
	}

	/**
	 * Récupération d'une définition par sa clé
	 *  -Soit la clé n'existe pas
	 *  -Soit la clé existe mais sans aucune définition
	 *  -Soit la clé raméne une définition.
	 *
	 * @param definitionName Name of the definition
	 * @return DynamicDefinition Définition correspondante ou null.
	 */
	public DslDefinition getDefinition(final String definitionName) {
		Assertion.checkArgument(definitions.containsKey(definitionName), "Aucune clé enregistrée pour :{0} parmi {1}", definitionName, definitions.keySet());
		//-----
		final DslDefinition definition = definitions.get(definitionName);
		//-----
		Assertion.checkNotNull(definition, "Clé trouvée mais pas de définition enregistrée trouvée pour {0}", definitionName);
		return definition;
	}

	/**
	 * Résolution des références de définitions.
	 * @param definitionSpace Space where all the definitions are stored
	 */
	public void solve(final DefinitionSpace definitionSpace) {
		Assertion.checkNotNull(definitionSpace);
		//-----
		mergePartials();

		final List<DslDefinition> sortedDynamicDefinitions = DslSolver.solve(definitionSpace, this);
		registerAllDefinitions(definitionSpace, sortedDynamicDefinitions);
	}

	private void mergePartials() {
		//parts of definitions are merged
		for (final DslDefinition partial : partials) {
			final DslDefinition merged = new DslDefinitionBuilder(partial.getName(), partial.getEntity())
					.merge(getDefinition(partial.getName()))
					.merge(partial).build();
			definitions.put(partial.getName(), merged);
		}
	}

	private void registerAllDefinitions(final DefinitionSpace definitionSpace, final List<DslDefinition> sortedDynamicDefinitions) {
		for (final DslDefinition xdefinition : sortedDynamicDefinitions) {
			DsValidator.check(xdefinition);
			if (!xdefinition.getEntity().isProvided()) {
				//The definition identified as root are not registered.
				final Definition definition = registry.createDefinition(definitionSpace, xdefinition);
				definitionSpace.put(definition);
			}
		}
	}

	/**
	 * Add a definition.
	 * @param definition DynamicDefinition
	 */
	public void addDefinition(final DslDefinition definition) {
		Assertion.checkNotNull(definition);
		//-----
		//On enregistre la définition qu'elle soit renseignée ou null.
		final DslDefinition previousDefinition = definitions.put(definition.getName(), definition);
		//On vérifie que l'on n'essaie pas d'écraser la définition déjà présente.
		Assertion.checkState(previousDefinition == null, "la définition {0} est déjà enregistrée", definition.getName());
		//-----
		registry.onNewDefinition(definition, this);
	}

	/**
	 * adds a partial definition.
	 * @param partial the part of a definition
	 */
	public void addPartialDefinition(final DslDefinition partial) {
		Assertion.checkNotNull(partial);
		//-----
		partials.add(partial);
	}

	/**
	 *  @return Liste des clés orphelines.
	 */
	Set<String> getOrphanDefinitionKeys() {
		return definitions.entrySet()
				.stream()
				.filter(entry -> entry.getValue() == null) //select orphans
				.map(Entry::getKey)
				.collect(Collectors.toSet());
	}

	/**
	 * @return Liste des définitions complètes
	 */
	Collection<DslDefinition> getDefinitions() {
		return Collections.unmodifiableCollection(definitions.values());
	}
}
