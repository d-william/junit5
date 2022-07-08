/*
 * Copyright 2015-2022 the original author or authors.
 *
 * All rights reserved. This program and the accompanying materials are
 * made available under the terms of the Eclipse Public License v2.0 which
 * accompanies this distribution and is available at
 *
 * https://www.eclipse.org/legal/epl-v20.html
 */

package org.junit.jupiter.api;

import static org.junit.jupiter.api.AssertionUtils.buildPrefix;
import static org.junit.jupiter.api.AssertionUtils.formatValues;
import static org.junit.jupiter.api.AssertionUtils.nullSafeGet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.opentest4j.AssertionFailedError;

public class AssertionBuilder {

	private Object message;
	private Throwable cause;
	private final List<Throwable> suppressed = new ArrayList<>();
	private boolean mismatch;
	private Object expected;
	private Object actual;
	private String reason;

	public static AssertionBuilder assertion() {
		return new AssertionBuilder();
	}

	private AssertionBuilder() {
	}

	public AssertionBuilder message(Object message) {
		this.message = message;
		return this;
	}

	public AssertionBuilder reason(String reason) {
		this.reason = reason;
		return this;
	}

	public AssertionBuilder cause(Throwable cause) {
		this.cause = cause;
		return this;
	}

	public AssertionBuilder suppressed(Throwable suppressed) {
		this.suppressed.add(suppressed);
		return this;
	}

	public AssertionBuilder suppressed(Throwable... suppressed) {
		this.suppressed.addAll(Arrays.asList(suppressed));
		return this;
	}

	public AssertionBuilder expected(Object expected) {
		this.mismatch = true;
		this.expected = expected;
		return this;
	}

	public AssertionBuilder actual(Object actual) {
		this.mismatch = true;
		this.actual = actual;
		return this;
	}

	public void fail() throws AssertionFailedError {
		throw build();
	}

	public AssertionFailedError build() {
		String reason = nullSafeGet(this.reason);
		if (mismatch) {
			reason = (reason == null ? "" : reason + ", ") + formatValues(expected, actual);
		}
		String message = nullSafeGet(this.message);
		if (reason != null) {
			message = buildPrefix(message) + reason;
		}
		AssertionFailedError assertionFailedError = mismatch //
				? new AssertionFailedError(message, expected, actual, cause) //
				: new AssertionFailedError(message, cause);
		suppressed.forEach(assertionFailedError::addSuppressed);
		return assertionFailedError;
	}

}
