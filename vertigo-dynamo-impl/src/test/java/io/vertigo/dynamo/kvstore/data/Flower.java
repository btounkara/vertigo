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
package io.vertigo.dynamo.kvstore.data;

import java.io.Serializable;

public final class Flower implements Serializable {
	private static final long serialVersionUID = 732495669497615898L;
	private String name;
	private Double price;

	public Double getPrice() {
		return price;
	}

	public String getName() {
		return name;
	}

	public Flower setPrice(final Double price) {
		this.price = price;
		return this;
	}

	public Flower setName(final String name) {
		this.name = name;
		return this;
	}

}
