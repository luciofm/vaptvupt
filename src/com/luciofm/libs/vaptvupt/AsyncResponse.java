package com.luciofm.libs.vaptvupt;

public class AsyncResponse<T> {
	private T response;
	private Throwable exception;

	public AsyncResponse(T response) {
		this.setResponse(response);
	}

	public AsyncResponse(Throwable exception) {
		this.setException(exception);
	}

	public T getResponse() {
		return response;
	}

	public void setResponse(T response) {
		this.response = response;
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}
}
