/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2018, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.dynamo.plugins.search.elasticsearch.embedded;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.reindex.ReindexPlugin;
import org.elasticsearch.node.InternalSettingsPreparer;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeValidationException;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.transport.Netty4Plugin;

import io.vertigo.commons.codec.CodecManager;
import io.vertigo.core.resource.ResourceManager;
import io.vertigo.dynamo.plugins.search.elasticsearch.AbstractESSearchServicesPlugin;
import io.vertigo.lang.Assertion;
import io.vertigo.lang.WrappedException;

//Vérifier
/**
 * Gestion de la connexion au serveur ElasticSearch en mode embarqué.
 *
 * @author pchretien, npiedeloup
 */
public final class ESEmbeddedSearchServicesPlugin extends AbstractESSearchServicesPlugin {
	/** url du serveur elasticSearch.  */
	private final URL elasticSearchHomeURL;
	private Node node;

	/**
	 * Constructor.
	 * @param elasticSearchHome URL du serveur SOLR
	 * @param envIndex Nom de l'index de l'environment
	 * @param envIndexIsPrefix Si Nom de l'index de l'environment est un prefix
	 * @param rowsPerQuery Nombre d'élément retourné par query
	 * @param codecManager Manager des codecs
	 * @param resourceManager Manager d'accès aux ressources
	 * @param configFile Fichier de configuration des indexs
	 */
	@Inject
	public ESEmbeddedSearchServicesPlugin(
			@Named("home") final String elasticSearchHome,
			@Named("envIndex") final String envIndex,
			@Named("envIndexIsPrefix") final Optional<Boolean> envIndexIsPrefix,
			@Named("rowsPerQuery") final int rowsPerQuery,
			@Named("config.file") final String configFile,
			final CodecManager codecManager,
			final ResourceManager resourceManager) {
		super(envIndex, envIndexIsPrefix.orElse(false), rowsPerQuery, configFile, codecManager, resourceManager);
		Assertion.checkArgNotEmpty(elasticSearchHome);
		//-----
		elasticSearchHomeURL = resourceManager.resolve(elasticSearchHome);
	}

	/** {@inheritDoc} */
	@Override
	protected Client createClient() {
		node = createNode(elasticSearchHomeURL);
		try {
			node.start();
		} catch (final NodeValidationException e) {
			throw WrappedException.wrap(e, "Error at ElasticSearch node start");
		}
		return node.client();
	}

	/** {@inheritDoc} */
	@Override
	protected void closeClient() {
		try {
			node.close();
		} catch (final IOException e) {
			throw WrappedException.wrap(e, "Error at ElasticSearch node stop");
		}
	}

	private static Node createNode(final URL esHomeURL) {
		Assertion.checkNotNull(esHomeURL);
		//-----
		final File home;
		try {
			home = new File(URLDecoder.decode(esHomeURL.getFile(), StandardCharsets.UTF_8.name()));
		} catch (final UnsupportedEncodingException e) {
			throw WrappedException.wrap(e, "Error de parametrage du ElasticSearchHome " + esHomeURL);
		}
		Assertion.checkArgument(home.exists() && home.isDirectory(), "Le ElasticSearchHome : {0} n''existe pas, ou n''est pas un répertoire.", home.getAbsolutePath());
		Assertion.checkArgument(home.canWrite(), "L''application n''a pas les droits d''écriture sur le ElasticSearchHome : {0}", home.getAbsolutePath());
		return new MyNode(buildNodeSettings(home.getAbsolutePath()), Arrays.asList(Netty4Plugin.class, ReindexPlugin.class));
	}

	private static class MyNode extends Node {
		public MyNode(final Settings preparedSettings, final Collection<Class<? extends Plugin>> classpathPlugins) {
			super(InternalSettingsPreparer.prepareEnvironment(preparedSettings, null), classpathPlugins);
		}
	}

	private static Settings buildNodeSettings(final String homePath) {
		//Build settings
		return Settings.builder()
				.put("node.name", "es-embedded-node-" + System.currentTimeMillis())
				.put("transport.type", "netty4")
				.put("http.type", "netty4")
				.put("http.enabled", "true")
				.put("path.home", homePath)
				.build();
	}
}
