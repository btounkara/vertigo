/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.studio.plugins.mda.domain;

import io.vertigo.dynamo.domain.metamodel.DtDefinition;
import io.vertigo.lang.Assertion;
import io.vertigo.studio.mda.ResultBuilder;
import io.vertigo.studio.plugins.mda.AbstractGeneratorPlugin;
import io.vertigo.util.MapBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Génération des objets relatifs au module Domain.
 *
 * @author pchretien
 */
public final class JSGeneratorPlugin extends AbstractGeneratorPlugin<DomainConfiguration> {
	private final boolean generateDtResourcesJS;
	private final boolean generateJsDtDefinitions;

	/**
	 * Constructeur.
	 *
	 * @param generateDtResourcesJS Si on génère les fichiers i18n pour les labels des champs en JS
	 * @param generateJsDtDefinitions Si on génère les classes JS.
	 */
	@Inject
	public JSGeneratorPlugin(
			@Named("generateDtResourcesJS") final boolean generateDtResourcesJS,
			@Named("generateJsDtDefinitions") final boolean generateJsDtDefinitions) {
		// ---------------------------------------------------------------------
		this.generateDtResourcesJS = generateDtResourcesJS;
		this.generateJsDtDefinitions = generateJsDtDefinitions;
	}

	/** {@inheritDoc} */
	@Override
	public DomainConfiguration createConfiguration(final Properties properties) {
		return new DomainConfiguration(properties);
	}

	/** {@inheritDoc} */
	@Override
	public void generate(final DomainConfiguration domainConfiguration, final ResultBuilder resultBuilder) {
		Assertion.checkNotNull(domainConfiguration);
		Assertion.checkNotNull(resultBuilder);
		// ---------------------------------------------------------------------
		/* Génération des ressources afférentes au DT mais pour la partie JS.*/
		if (generateDtResourcesJS) {
			generateDtResourcesJS(domainConfiguration, resultBuilder);
		}
		/* Génération des fichiers javascripts référençant toutes les définitions. */
		if (generateJsDtDefinitions) {
			generateJsDtDefinitions(domainConfiguration, resultBuilder);
		}
	}

	private static void generateJsDtDefinitions(final DomainConfiguration domainConfiguration, final ResultBuilder resultBuilder) {

		final List<TemplateDtDefinition> dtDefinitions = new ArrayList<>();
		for (final DtDefinition dtDefinition : DomainUtil.getDtDefinitions()) {
			dtDefinitions.add(new TemplateDtDefinition(dtDefinition));
		}

		final Map<String, Object> mapRoot = new MapBuilder<String, Object>()
				.put("packageName", domainConfiguration.getDomainPackage())
				.put("classSimpleName", domainConfiguration.getDomainDictionaryClassName())
				.put("dtDefinitions", dtDefinitions)
				.build();

		createFileGenerator(domainConfiguration, mapRoot, domainConfiguration.getDomainDictionaryClassName(), domainConfiguration.getDomainPackage(), ".js", "js.ftl")
				.generateFile(resultBuilder);

	}

	/**
	 * Génère les ressources JS pour les traductions.
	 * @param domainConfiguration Configuration du domaine.
	 * @param resultBuilder Builder
	 */
	private static void generateDtResourcesJS(final DomainConfiguration domainConfiguration, final ResultBuilder resultBuilder) {
		/**
		 * Génération des ressources afférentes au DT.
		 */
		for (final Entry<String, Collection<DtDefinition>> entry : DomainUtil.getDtDefinitionCollectionMap().entrySet()) {
			String simpleClassName = entry.getClass().getName() + ".generated";

			final List<TemplateDtDefinition> dtDefinitions = new ArrayList<>();
			for (final DtDefinition dtDefinition : DomainUtil.getDtDefinitions()) {
				dtDefinitions.add(new TemplateDtDefinition(dtDefinition));
				simpleClassName = (new TemplateDtDefinition(dtDefinition)).getClassSimpleNameCamelCase();
			}

			final Collection<DtDefinition> dtDefinitionCollection = entry.getValue();
			Assertion.checkNotNull(dtDefinitionCollection);
			final String packageName = entry.getKey();

			final Map<String, Object> mapRoot = new MapBuilder<String, Object>()
					.put("packageName", packageName)
					.put("simpleClassName", simpleClassName)
					.put("dtDefinitions", dtDefinitions)
					.build();

			createFileGenerator(domainConfiguration, mapRoot, simpleClassName, packageName, ".js", "propertiesJS.ftl")
					.generateFile(resultBuilder);
		}
	}
}
