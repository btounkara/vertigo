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
package io.vertigo.rest.impl.rest.handler;

import io.vertigo.rest.rest.exception.SessionException;
import io.vertigo.rest.rest.exception.VSecurityException;
import spark.Request;
import spark.Response;

/**
 * Atomic handler of http request. 
 * @author npiedeloup
 */
public interface RouteHandler {

	/**
	 * Do handle of this route.
	 * 
	 * @param request spark.Request
	 * @param response spark.Response
	 * @param routeContext Context of this request
	 * @param chain current HandlerChain.
	 * @return Response body
	 * @throws SessionException Session expired exception
	 * @throws VSecurityException Security exception
	 */
	Object handle(final Request request, final Response response, final RouteContext routeContext, final HandlerChain chain) throws SessionException, VSecurityException;
}