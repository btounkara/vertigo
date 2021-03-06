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
package io.vertigo.dynamox.search.dsl.rules;

import java.util.List;

import io.vertigo.commons.peg.AbstractRule;
import io.vertigo.commons.peg.PegRule;
import io.vertigo.commons.peg.PegRules;
import io.vertigo.dynamox.search.dsl.model.DslMultiExpression;

/**
 * Parsing rule for search expression.
 * (multiExpression)+
 * @author npiedeloup
 */
public final class DslSearchExpressionRule extends AbstractRule<List<DslMultiExpression>, List<DslMultiExpression>> {

	public DslSearchExpressionRule() {
		super(createMainRule(), "searchExpression");
	}

	private static PegRule<List<DslMultiExpression>> createMainRule() {
		return PegRules.oneOrMore(new DslMultiExpressionRule(), true);
	}

	@Override
	protected List<DslMultiExpression> handle(final List<DslMultiExpression> parsing) {
		return parsing;
	}

}
