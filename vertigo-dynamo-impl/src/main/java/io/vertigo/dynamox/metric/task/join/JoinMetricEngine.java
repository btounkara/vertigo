/**
 * vertigo - simple java starter
 *
 * Copyright (C) 2013-2017, KleeGroup, direction.technique@kleegroup.com (http://www.kleegroup.com)
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
package io.vertigo.dynamox.metric.task.join;

import java.util.Locale;

import io.vertigo.commons.metric.Metric;
import io.vertigo.commons.metric.MetricEngine;
import io.vertigo.dynamo.task.metamodel.TaskDefinition;
import io.vertigo.lang.Assertion;

/**
 * Plugin qui compte le nombre de jointures déclarées dans la requête.
 *
 * @author tchassagnette
 */
public final class JoinMetricEngine implements MetricEngine<TaskDefinition> {
	/** {@inheritDoc} */
	@Override
	public Metric execute(final TaskDefinition taskDefinition) {
		Assertion.checkNotNull(taskDefinition);
		//-----
		final double joinCount = taskDefinition.getRequest().toUpperCase(Locale.ENGLISH).split("JOIN").length - 1d;
		final double fromCount = taskDefinition.getRequest().toUpperCase(Locale.ENGLISH).split("FROM ").length - 1d;
		return Metric.builder()
				.withType("taskJoinCount")
				.withSubject(taskDefinition.getName())
				.withValue(joinCount + fromCount)
				.withSuccess()
				.build();
	}
}