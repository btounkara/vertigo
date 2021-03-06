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
package io.vertigo.account.impl.account;

import java.util.List;

import io.vertigo.core.definition.Definition;
import io.vertigo.core.definition.DefinitionSpace;
import io.vertigo.core.definition.SimpleDefinitionProvider;
import io.vertigo.dynamo.domain.metamodel.DataType;
import io.vertigo.dynamo.domain.metamodel.Domain;
import io.vertigo.dynamo.domain.metamodel.DtDefinition;
import io.vertigo.util.ListBuilder;

/**
 * Provides all the definitions used in the 'account' module.
 * @author pchretien
 */
public final class AccountDefinitionProvider implements SimpleDefinitionProvider {

	private static final String EMAIL = "EMAIL";
	private static final String ID = "ID";
	private static final String DISPLAY_NAME = "DISPLAY_NAME";

	/** {@inheritDoc} */
	@Override
	public List<Definition> provideDefinitions(final DefinitionSpace definitionSpace) {
		final Domain domainAccountId = Domain.builder("DO_X_ACCOUNT_ID", DataType.String).build();
		final Domain domainAccountName = Domain.builder("DO_X_ACCOUNT_NAME", DataType.String).build();
		final Domain domainAccountEmail = Domain.builder("DO_X_ACCOUNT_EMAIL", DataType.String).build();

		final DtDefinition accountDtDefinition = DtDefinition.builder("DT_ACCOUNT")
				.addIdField(ID, "id", domainAccountId)
				.addDataField(DISPLAY_NAME, "displayName", domainAccountName, false, true)
				.addDataField(EMAIL, "email", domainAccountEmail, false, true)
				.withSortField(DISPLAY_NAME)
				.withDisplayField(DISPLAY_NAME)
				.build();

		final DtDefinition accountGroupDtDefinition = DtDefinition.builder("DT_ACCOUNT_GROUP")
				.addIdField(ID, "id", domainAccountId)
				.addDataField(DISPLAY_NAME, "displayName", domainAccountName, false, true)
				.withSortField(DISPLAY_NAME)
				.withDisplayField(DISPLAY_NAME)
				.build();

		return new ListBuilder<Definition>()
				.add(domainAccountId)
				.add(domainAccountName)
				.add(domainAccountEmail)
				.add(accountDtDefinition)
				.add(accountGroupDtDefinition)
				.build();
	}

}
