package io.vertigo.commons.script;

import io.vertigo.AbstractTestCaseJU4;
import io.vertigo.commons.script.parser.ScriptParserHandler;
import io.vertigo.commons.script.parser.ScriptSeparator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author pchretien
 * $Id: ScriptManagerTest.java,v 1.2 2013/10/22 10:59:02 pchretien Exp $
 */
public final class ScriptManagerTest extends AbstractTestCaseJU4 {
	@Inject
	private ScriptManager scriptManager;
	private final ScriptSeparator comment = new ScriptSeparator("<!--", "-->");

	//	private ExpressionEvaluatorPlugin bsExpressionEvaluatorPlugin;
	//	private ExpressionEvaluatorPlugin mvelExpressionEvaluatorPlugin;

	private static List<ExpressionParameter> createParameters() {
		final List<ExpressionParameter> parameters = new ArrayList<ExpressionParameter>();
		parameters.add(new ExpressionParameter("nom", String.class, "Duraton"));
		parameters.add(new ExpressionParameter("prenom", String.class, "jean paul"));
		parameters.add(new ExpressionParameter("age", Integer.class, 54));
		return parameters;
	}

	@Test
	public void testStringReplace() {
		final String script = "ce matin M.<%=nom%> est all� chercher son pain";
		final String result = scriptManager.evaluateScript(script, SeparatorType.CLASSIC, createParameters());
		Assert.assertEquals("ce matin M.Duraton est all� chercher son pain", result);
	}

	@Test
	public void testIntegerReplace() {
		final String script = "M.Duraton a <%=age%> ans";
		final String result = scriptManager.evaluateScript(script, SeparatorType.CLASSIC, createParameters());
		Assert.assertEquals("M.Duraton a 54 ans", result);
	}

	@Test
	public void testCount() {
		final String script = "Le prénom de M.<%=nom%> est composé de <%=prenom.length()%> lettres";
		final String result = scriptManager.evaluateScript(script, SeparatorType.CLASSIC, createParameters());
		Assert.assertEquals("Le prénom de M.Duraton est composé de 9 lettres", result);
	}

	@Test
	public void testIf() {
		final String script = "<%if (nom.startsWith(\"Dur\")) {%>Il s'agit bien de M.Duraton<%}%>";
		final String result = scriptManager.evaluateScript(script, SeparatorType.CLASSIC, createParameters());
		Assert.assertEquals("Il s'agit bien de M.Duraton", result);
	}

	@Test(expected = Exception.class)
	public void testSyntaxError() {
		//On g�n�re une erreur java
		final String script = "<%if (nom.sttart(\"Dur\")) {%>Il s'agit bien de M.Duraton<%}%>";
		final String result = scriptManager.evaluateScript(script, SeparatorType.CLASSIC, createParameters());
		nop(result);
	}

	@Test
	public void testNonDynamic() {
		//On fait une �valuation d'un texte qui s'av�re non dynamique. (Absence de <%)
		final String script = "Il s'agit bien de M.Duraton";
		final String result = scriptManager.evaluateScript(script, SeparatorType.CLASSIC, createParameters());
		Assert.assertEquals("Il s'agit bien de M.Duraton", result);
	}

	@Test
	public void testComment() {
		final List<ScriptSeparator> separators = new ArrayList<ScriptSeparator>();
		separators.add(comment);

		final String script = "bla <!--commentaires-->bla";

		final MyScriptParserHandler scriptHandler = new MyScriptParserHandler();
		scriptManager.parse(script, scriptHandler, separators);
		Assert.assertEquals("bla bla", scriptHandler.result.toString());
	}

	@Test(expected = Exception.class)
	public void testOubliParametre() {
		final List<ScriptSeparator> separators = new ArrayList<ScriptSeparator>();
		separators.add(comment);

		final String script = "bla <!---->bla";

		final MyScriptParserHandler scriptHandler = new MyScriptParserHandler();

		scriptManager.parse(script, scriptHandler, separators);
	}

	@Test
	public void testEchappement() {
		//Si le s�parateur est un car.
		//il suffit de double le s�parateur pour l'�chapper.
		final List<ScriptSeparator> separators = new ArrayList<ScriptSeparator>();
		separators.add(new ScriptSeparator('$'));
		final String script = "le prix du barril est de $price$ $$";
		final MyScriptParserHandler scriptHandler = new MyScriptParserHandler();
		scriptManager.parse(script, scriptHandler, separators);
		Assert.assertEquals("le prix du barril est de 100 $", scriptHandler.result.toString());
	}

	@Test(expected = Exception.class)
	public void testOubliCaractereDeFin() {
		final List<ScriptSeparator> separators = new ArrayList<ScriptSeparator>();
		separators.add(new ScriptSeparator('$'));
		final String script = "le prix du barril est de $price";
		final MyScriptParserHandler scriptHandler = new MyScriptParserHandler();

		scriptManager.parse(script, scriptHandler, separators);
	}

	@Test
	public void testExpressionString() {
		final String test = scriptManager.evaluateExpression("\"darwin\"", createParameters(), String.class);
		Assert.assertEquals("darwin", test);
	}

	@Test
	public void testExpressionVarString() {
		final String test = scriptManager.evaluateExpression("nom", createParameters(), String.class);
		Assert.assertEquals("Duraton", test);
	}

	@Test
	public void testExpressionVarInteger() {
		final Integer test = scriptManager.evaluateExpression("age", createParameters(), Integer.class);
		Assert.assertEquals(54, test.intValue());
	}

	@Test
	public void testExpressionVarBoolean() {
		final Boolean test = scriptManager.evaluateExpression("age>20", createParameters(), Boolean.class);
		Assert.assertEquals(true, test.booleanValue());
	}

	//
	//	@Test
	//	public void testBSExpressionVarBoolean() {
	//		final Boolean test = bsExpressionEvaluatorPlugin.evaluate("age>20", createParameters(), Boolean.class);
	//		Assert.assertEquals(true, test.booleanValue());
	//	}
	//
	//	@Test
	//	public void testMVELExpressionVarBoolean() {
	//		final Boolean test = mvelExpressionEvaluatorPlugin.evaluate("nom == 'Duraton'", createParameters(), Boolean.class);
	//		Assert.assertEquals(true, test.booleanValue());
	//	}

	private class MyScriptParserHandler implements ScriptParserHandler {
		final StringBuilder result = new StringBuilder();

		public void onExpression(final String expression, final ScriptSeparator separator) {
			if ("price".equals(expression)) {
				result.append("100");
			} else if (!separator.equals(comment)) {
				result.append(expression);
			}
		}

		public void onText(final String text) {
			result.append(text);
		}
	}
}