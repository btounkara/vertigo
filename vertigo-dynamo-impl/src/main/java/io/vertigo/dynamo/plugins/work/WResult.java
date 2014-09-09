package io.vertigo.dynamo.plugins.work;

import io.vertigo.core.lang.Assertion;

/**
 * @author pchretien
 */
public final class WResult<WR> {
	public final String workId;
	public final Throwable error;
	public final WR result;

	public WResult(final String workId, final WR result, final Throwable error) {
		Assertion.checkArgNotEmpty(workId);
		Assertion.checkArgument(result == null ^ error == null, "result xor error is null");
		//---------------------------------------------------------------------
		this.workId = workId;
		this.error = error;
		this.result = result;
	}
}