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
package io.vertigo.dynamo.plugins.work.redis;

import io.vertigo.dynamo.impl.node.NodePlugin;
import io.vertigo.dynamo.impl.work.worker.Worker;
import io.vertigo.dynamo.impl.work.worker.local.LocalWorker;
import io.vertigo.dynamo.node.Node;
import io.vertigo.kernel.lang.Activeable;
import io.vertigo.kernel.lang.Assertion;
import io.vertigo.kernel.lang.Option;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * NodePlugin 
 * Ce plugin permet d'exécuter des travaux en mode distribué.
 * REDIS est utilisé comme plateforme d'échanges.
 * 
 * @author pchretien
 * $Id: RedisNodePlugin.java,v 1.9 2014/06/26 12:30:08 npiedeloup Exp $
 */
public final class RedisNodePlugin implements NodePlugin, Activeable {
	private final JedisPool jedisPool;
	private final LocalWorker localWorker = new LocalWorker(/*workersCount*/5);
	private final Thread dispatcherThread;
	private final String nodeId;

	@Inject
	public RedisNodePlugin(final @Named("nodeId") String nodeId, final @Named("host") String redisHost, final @Named("port") int redisPort, final @Named("password") Option<String> password) {
		Assertion.checkArgNotEmpty(nodeId);
		Assertion.checkArgNotEmpty(redisHost);
		//---------------------------------------------------------------------
		this.nodeId = nodeId;
		jedisPool = RedisUtil.createJedisPool(redisHost, redisPort, password);
		dispatcherThread = new RedisDispatcherThread(nodeId, jedisPool, localWorker);
		//System.out.println("RedisNodePlugin");
	}

	/** {@inheritDoc} */
	public void start() {
		//System.out.println("start node");

		//On enregistre le node
		register(new Node(nodeId, true));

		dispatcherThread.start();
	}

	/** {@inheritDoc} */
	public void stop() {
		dispatcherThread.interrupt();
		try {
			dispatcherThread.join();
		} catch (final InterruptedException e) {
			//On ne fait rien
		}
		localWorker.close();
		register(new Node(nodeId, false));
	}

	//------------------------------------
	//------------------------------------
	//------------------------------------
	//------------------------------------

	private final Gson gson = createGson();

	private static Gson createGson() {
		return new GsonBuilder()//
				//.setPrettyPrinting()//
				.create();
	}

	/** {@inheritDoc} */
	public List<Node> getNodes() {
		try (Jedis jedis = jedisPool.getResource()) {
			final List<Node> nodes = new ArrayList<>();

			final List<String> nodeKeys = jedis.lrange("nodes", -1, -1);
			for (final String nodeKey : nodeKeys) {
				final String json = jedis.hget(nodeKey, "json");
				nodes.add(toNode(json));
			}
			return nodes;
		}
	}

	private Node toNode(final String json) {
		return gson.fromJson(json, Node.class);
	}

	private String toJson(final Node node) {
		return gson.toJson(node);
	}

	private void register(final Node node) {
		Assertion.checkNotNull(node);
		//---------------------------------------------------------------------
		try (Jedis jedis = jedisPool.getResource()) {
			jedis.lpush("nodes", node.getUID());
			jedis.hset("node:" + node.getUID(), "json", toJson(node));
		}
	}
}
