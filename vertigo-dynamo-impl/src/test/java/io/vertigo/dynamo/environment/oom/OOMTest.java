package io.vertigo.dynamo.environment.oom;

import io.vertigo.dynamo.TestUtil;
import io.vertigo.dynamo.plugins.environment.loaders.poweramc.core.AssociationOOM;
import io.vertigo.dynamo.plugins.environment.loaders.poweramc.core.LoaderOOM;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test de lecture d'un OOM.
 *
 * @author pchretien
 * @version $Id: OOMTest.java,v 1.1 2013/07/10 15:45:32 npiedeloup Exp $
 */
public class OOMTest {
	private static final Logger LOGGER = Logger.getLogger(OOMTest.class);
	private Map<String, AssociationOOM> map;

	@Before
	public void setUp() throws Exception {
		final File oomFile = TestUtil.getFile("data/Associations.oom", getClass());

		final URL oomURL = oomFile.toURL();
		final LoaderOOM loader = new LoaderOOM(oomURL);
		map = new HashMap<>();
		for (final AssociationOOM associationOOM : loader.getAssociationOOMList()) {
			map.put(associationOOM.getCode(), associationOOM);
			LOGGER.trace("> code = " + associationOOM.getCode());
		}
		LOGGER.trace(">> nb ass.=" + loader.getAssociationOOMList().size());

	}

	@After
	public void tearDown() {
		map = null;
	}

	/*
	 * Conventions de nommage utilisées pour les tests ci dessous.
	 * - Relation de A vers B
	 * - Cardinalité notée 	1 ou n
	 * - Navigabilité notée v 
	 */

	/**
	 * Test d'une relation A1 - Bnv.
	 */
	@Test
	public void testAssoctationA1Bnv() {
		final AssociationOOM associationOOM = map.get("CHA_CHI_1");
		Assert.assertEquals("0..1", associationOOM.getMultiplicityA());
		Assert.assertEquals("0..*", associationOOM.getMultiplicityB());

		Assert.assertEquals("R1A", associationOOM.getRoleLabelA());
		Assert.assertEquals("R1B", associationOOM.getRoleLabelB());

		Assert.assertEquals(false, associationOOM.isNavigableA());
		Assert.assertEquals(true, associationOOM.isNavigableB());
	}

	/**
	 * Test d'une relation A1v - Bnv.
	 */
	@Test
	public void testAssoctationA1vBnv() {
		final AssociationOOM associationOOM = map.get("CHA_CHI_2");
		Assert.assertEquals("0..1", associationOOM.getMultiplicityA());
		Assert.assertEquals("0..*", associationOOM.getMultiplicityB());

		Assert.assertEquals("R2A", associationOOM.getRoleLabelA());
		Assert.assertEquals("R2B", associationOOM.getRoleLabelB());

		Assert.assertEquals(true, associationOOM.isNavigableA());
		Assert.assertEquals(true, associationOOM.isNavigableB());
	}

	/**
	 * Test d'une relation A1v - Bn.
	 */
	@Test
	public void testAssoctationA1vBn() {
		final AssociationOOM associationOOM = map.get("CHA_CHI_3");
		Assert.assertEquals("0..1", associationOOM.getMultiplicityA());
		Assert.assertEquals("0..*", associationOOM.getMultiplicityB());

		Assert.assertEquals("R3A", associationOOM.getRoleLabelA());
		Assert.assertEquals("R3B", associationOOM.getRoleLabelB());

		Assert.assertEquals(true, associationOOM.isNavigableA());
		Assert.assertEquals(false, associationOOM.isNavigableB());
	}

	/**
	 * Test d'une relation An - B1v.
	 */
	@Test
	public void testAssoctationAnB1v() {
		final AssociationOOM associationOOM = map.get("CHA_CHI_4");
		Assert.assertEquals("0..*", associationOOM.getMultiplicityA());
		Assert.assertEquals("0..1", associationOOM.getMultiplicityB());

		Assert.assertEquals("R4A", associationOOM.getRoleLabelA());
		Assert.assertEquals("R4B", associationOOM.getRoleLabelB());

		Assert.assertEquals(false, associationOOM.isNavigableA());
		Assert.assertEquals(true, associationOOM.isNavigableB());
	}

	/**
	 * Test d'une relation Anv - B1.
	 */
	@Test
	public void testAssoctationAnvB1() {
		final AssociationOOM associationOOM = map.get("CHA_CHI_5");
		Assert.assertEquals("0..*", associationOOM.getMultiplicityA());
		Assert.assertEquals("0..1", associationOOM.getMultiplicityB());

		Assert.assertEquals("R5A", associationOOM.getRoleLabelA());
		Assert.assertEquals("R5B", associationOOM.getRoleLabelB());

		Assert.assertEquals(true, associationOOM.isNavigableA());
		Assert.assertEquals(false, associationOOM.isNavigableB());
	}

	/**
	 * Test d'une relation Anv - B1v.
	 */
	@Test
	public void testAssoctationAnvB1v() {
		final AssociationOOM associationOOM = map.get("CHA_CHI_6");
		Assert.assertEquals("0..*", associationOOM.getMultiplicityA());
		Assert.assertEquals("0..1", associationOOM.getMultiplicityB());

		Assert.assertEquals("R6A", associationOOM.getRoleLabelA());
		Assert.assertEquals("R6B", associationOOM.getRoleLabelB());

		Assert.assertEquals(true, associationOOM.isNavigableA());
		Assert.assertEquals(true, associationOOM.isNavigableB());
	}

	/**
	 * Test d'une relation An - Bnv.
	 */
	@Test
	public void testAssoctationAnBnv() {
		final AssociationOOM associationOOM = map.get("CHA_CHI_7");
		Assert.assertEquals("0..*", associationOOM.getMultiplicityA());
		Assert.assertEquals("0..*", associationOOM.getMultiplicityB());

		Assert.assertEquals("R7A", associationOOM.getRoleLabelA());
		Assert.assertEquals("R7B", associationOOM.getRoleLabelB());

		Assert.assertEquals(false, associationOOM.isNavigableA());
		Assert.assertEquals(true, associationOOM.isNavigableB());
	}

	/**
	 * Test d'une relation Anv - Bnv.
	 */
	@Test
	public void testAssoctationAnvBnv() {
		final AssociationOOM associationOOM = map.get("CHA_CHI_8");
		Assert.assertEquals("0..*", associationOOM.getMultiplicityA());
		Assert.assertEquals("0..*", associationOOM.getMultiplicityB());

		Assert.assertEquals("R8A", associationOOM.getRoleLabelA());
		Assert.assertEquals("R8B", associationOOM.getRoleLabelB());

		Assert.assertEquals(true, associationOOM.isNavigableA());
		Assert.assertEquals(true, associationOOM.isNavigableB());
	}

	/**
	 * Test d'une relation An - Bn.
	 */
	@Test
	public void testAssoctationAnBn() {
		final AssociationOOM associationOOM = map.get("CHA_CHI_9");
		Assert.assertEquals("0..*", associationOOM.getMultiplicityA());
		Assert.assertEquals("0..*", associationOOM.getMultiplicityB());

		Assert.assertEquals("R9A", associationOOM.getRoleLabelA());
		Assert.assertEquals("R9B", associationOOM.getRoleLabelB());

		Assert.assertEquals(false, associationOOM.isNavigableA());
		Assert.assertEquals(false, associationOOM.isNavigableB());
	}

	/**
	 * Test d'une relation Anv - Bn.
	 */
	@Test
	public void testAssoctationAnvBn() {
		final AssociationOOM associationOOM = map.get("CHA_CHI_10");
		Assert.assertEquals("0..*", associationOOM.getMultiplicityA());
		Assert.assertEquals("0..*", associationOOM.getMultiplicityB());

		Assert.assertEquals("R10A", associationOOM.getRoleLabelA());
		Assert.assertEquals("R10B", associationOOM.getRoleLabelB());

		Assert.assertEquals(true, associationOOM.isNavigableA());
		Assert.assertEquals(false, associationOOM.isNavigableB());
	}
}
