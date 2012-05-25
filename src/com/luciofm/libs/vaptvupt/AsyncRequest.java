package com.luciofm.libs.vaptvupt;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.security.InvalidParameterException;

import android.content.Context;
import android.os.AsyncTask;

public class AsyncRequest<T> extends AsyncTask<Object, Void, AsyncResponse<T>> {

	private Context context = null;
	private AsyncRequestListener<T> listener;
	private Object data = null;
	private Type type;

	public AsyncRequest(Context context) {
		this(context, null);
	}

	public AsyncRequest(Context context,
			AsyncRequestListener<T> listener) {
		this(context, listener, null);
	}

	public AsyncRequest(Context context,
			AsyncRequestListener<T> listener, Object data) {
		this.context = context;
		this.listener = listener;
		this.data = data;
		this.type = getSuperclassTypeParameter(getClass());
	}

	static Type getSuperclassTypeParameter(Class<?> subclass) {
		Type superclass = subclass.getGenericSuperclass();
		if (superclass instanceof Class) {
			throw new RuntimeException("Missing type parameter.");
		}
		ParameterizedType parameterized = (ParameterizedType) superclass;
		return parameterized.getActualTypeArguments()[0];
	}

	@Override
	protected AsyncResponse<T> doInBackground(Object... params) {
		if (params.length == 0)
			return new AsyncResponse<T>(new IndexOutOfBoundsException());
		if (!(params[0] instanceof String))
			return new AsyncResponse<T>(new InvalidParameterException("First parameter must be a String"));
		String url = (String) params[0];

		return null;
	}

	public interface AsyncRequestListener<T> {
		public void onError(Throwable exception, Object data);
		public void onSuccess(T response, Object data);
	}
}
