package io.vertigo.dynamo.impl.work;

import io.vertigo.dynamo.work.WorkEngine;
import io.vertigo.kernel.exception.VRuntimeException;
import io.vertigo.kernel.lang.Assertion;

import java.util.concurrent.Callable;

/**
 * Gestion des taches asynchrones définies par un Callable 
 * @author pchretien, npiedeloup
 * @version $Id: AsyncEngine.java,v 1.4 2014/01/20 11:34:32 npiedeloup Exp $
 */
final class AsyncEngine<WR> implements WorkEngine<WR, Object> {
	private final Callable<WR> callable;

	AsyncEngine(final Callable<WR> callable) {
		Assertion.checkNotNull(callable);
		//-----------------------------------------------------------------
		this.callable = callable;
	}

	public WR process(final Object dummy) {
		try {
			return callable.call();
		} catch (final Exception e) {
			throw new VRuntimeException(e);
		}
	}
}