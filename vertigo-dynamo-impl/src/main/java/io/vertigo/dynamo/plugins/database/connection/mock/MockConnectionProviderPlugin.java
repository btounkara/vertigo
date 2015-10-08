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
package io.vertigo.dynamo.plugins.database.connection.mock;

import io.vertigo.dynamo.database.connection.SqlConnection;
import io.vertigo.dynamo.database.vendor.SqlDataBase;
import io.vertigo.dynamo.impl.database.SqlDataBaseManagerImpl;
import io.vertigo.dynamo.plugins.database.connection.AbstractSqlConnectionProviderPlugin;
import io.vertigo.lang.Assertion;
import io.vertigo.lang.Option;
import io.vertigo.util.ClassUtil;

import java.sql.DriverManager;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Implémentation d'un pseudo Pool.
 *
 * @see io.vertigo.dynamo.plugins.database.connection.datasource.DataSourceConnectionProviderPlugin Utiliser une DataSource
 * @deprecated NE DOIT PAS ETRE UTILISE EN PRODUCTION.
 */
@Deprecated
public final class MockConnectionProviderPlugin extends AbstractSqlConnectionProviderPlugin {
	/** Url Jdbc. */
	private final String jdbcUrl;

	/**
	 * Constructeur (deprecated).
	 * @param name ConnectionProvider's name
	 * @param dataBaseClass Type de base de données
	 * @param jdbcDriver Classe du driver jdbc
	 * @param jdbcUrl URL de configuration jdbc
	 */
	@Inject
	public MockConnectionProviderPlugin(@Named("name") final Option<String> name, @Named("dataBaseClass") final String dataBaseClass, @Named("jdbcDriver") final String jdbcDriver, @Named("jdbcUrl") final String jdbcUrl) {
		super(name.getOrElse(SqlDataBaseManagerImpl.MAIN_CONNECTION_PROVIDER_NAME), ClassUtil.newInstance(dataBaseClass, SqlDataBase.class));

		Assertion.checkNotNull(jdbcUrl);
		Assertion.checkNotNull(jdbcDriver);
		//-----
		ClassUtil.classForName(jdbcDriver); //Initialisation du driver

		this.jdbcUrl = jdbcUrl;
	}

	/** {@inheritDoc} */
	@Override
	public SqlConnection obtainConnection() throws SQLException {
		//Dans le pseudo pool on crée systématiquement une connexion
		return new SqlConnection(DriverManager.getConnection(jdbcUrl), getDataBase(), true);
	}
}
