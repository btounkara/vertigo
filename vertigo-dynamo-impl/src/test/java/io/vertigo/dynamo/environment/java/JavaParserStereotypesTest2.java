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
package io.vertigo.dynamo.environment.java;

import org.junit.Assert;
import org.junit.Test;

import io.vertigo.AbstractTestCaseJU4;
import io.vertigo.core.definition.DefinitionSpace;
import io.vertigo.dynamo.domain.metamodel.DtDefinition;
import io.vertigo.dynamo.domain.metamodel.DtStereotype;

/**
 * Test de lecture de class Java.
 *
 * @author npiedeloup
 */
public final class JavaParserStereotypesTest2 extends AbstractTestCaseJU4 {

	/**
	 * Tableau des fichiers managers.xml a prendre en compte.
	 *
	 * @return fichier managers.xml (par defaut managers-test.xml)
	 */
	@Override
	protected String[] getManagersXmlFileName() {
		return new String[] { "./managers-test2.xml", };
	}

	private DtDefinition getDtDefinition(final String urn) {
		final DefinitionSpace definitionSpace = getApp().getDefinitionSpace();
		return definitionSpace.resolve(urn, DtDefinition.class);
	}

	/**
	 * Test du stereotype MasterData
	 */
	@Test
	public void testStereotypeMasterData() {
		final DtDefinition dtDefinitionCity = getDtDefinition("DT_CITY");
		Assert.assertNotNull(dtDefinitionCity);
		Assert.assertEquals(DtStereotype.MasterData, dtDefinitionCity.getStereotype());

		final DtDefinition dtDefinitionCommandType = getDtDefinition("DT_COMMAND_TYPE");
		Assert.assertNotNull(dtDefinitionCommandType);
		Assert.assertEquals(DtStereotype.MasterData, dtDefinitionCommandType.getStereotype());
	}

	/**
	 * Test du stereotype keyConcept
	 */
	@Test
	public void testStereotypeKeyConcept() {
		final DtDefinition dtDefinitionCommand = getDtDefinition("DT_COMMAND");
		Assert.assertNotNull(dtDefinitionCommand);
		Assert.assertEquals(DtStereotype.KeyConcept, dtDefinitionCommand.getStereotype());

	}

	/**
	 * Test du stereotype Data
	 */
	@Test
	public void testStereotypeEntity() {
		final DtDefinition dtDefinitionAttachment = getDtDefinition("DT_ATTACHMENT");
		Assert.assertNotNull(dtDefinitionAttachment);
		Assert.assertEquals(DtStereotype.Entity, dtDefinitionAttachment.getStereotype());

		final DtDefinition dtDefinitionCommandValidation = getDtDefinition("DT_COMMAND_VALIDATION");
		Assert.assertNotNull(dtDefinitionCommandValidation);
		Assert.assertEquals(DtStereotype.Entity, dtDefinitionCommandValidation.getStereotype());
	}

	@Test
	public void testStereotypeData() {
		final DtDefinition dtDefinitionAttachment = getDtDefinition("DT_COMMAND_CRITERIA");
		Assert.assertNotNull(dtDefinitionAttachment);
		Assert.assertEquals(DtStereotype.ValueObject, dtDefinitionAttachment.getStereotype());

	}
}
