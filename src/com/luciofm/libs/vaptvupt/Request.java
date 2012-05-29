package com.luciofm.libs.vaptvupt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;

import android.text.TextUtils;

public class Request {

	public static final int GET = 0;
	public static final int POST = 1;
	public static final int DELETE = 2;
	public static final int MAX_METHOD = DELETE;

	private int mMethod;
	private String mUrl;
	private String mParams;
	private Map<String, String> mHeaders;
	private boolean mJson;
	public String mResponseField;

	private Request(int method) {
		mMethod = method;
	}

	public int getMethod() {
		return mMethod;
	}

	public String getUrl() {
		return mUrl;
	}

	public String getParams() {
		return mParams;
	}

	public Map<String, String> getHeaders() {
		return mHeaders;
	}

	public String getResponseField() {
		return mResponseField;
	}

	public boolean isJson() {
		return mJson;
	}

	public static class Builder {
		private int mMethod;
		private String mUrl;
		private Map<String, String> mHeaders = new HashMap<String, String>();
		private List<NameValuePair> mUrlParams = new ArrayList<NameValuePair>();
		private List<NameValuePair> mParams = new ArrayList<NameValuePair>();
		private String mResponseField;
		private boolean mJson = true;

		public Builder(int method) {
			mMethod = method;
		}

		public Builder setUrl(String url) {
			mUrl = url;
			return this;
		}

		public Builder withUrlParam(String param, String value) {
			mUrlParams.add(new BasicNameValuePair(param, value));
			return this;
		}

		public Builder withParam(String param, String value) {
			mParams.add(new BasicNameValuePair(param, value));
			return this;
		}

		public Builder withHeader(String header, String value) {
			mHeaders.put(header, value);
			return this;
		}

		public Builder withResponseField(String responseField) {
			mResponseField = responseField;
			return this;
		}

		public Builder isJson(boolean json) {
			mJson = json;
			return this;
		}

		public Request build() {
			Request req = new Request(mMethod);
			req.mUrl = mUrl;
			if (mUrlParams.size() > 0)
				req.mUrl += "?" + URLEncodedUtils.format(mUrlParams, "utf-8");

			if (mParams.size() > 0)
				req.mParams = URLEncodedUtils.format(mParams, "utf-8");

			if (mHeaders.size() > 0)
				req.mHeaders = mHeaders;

			if (!TextUtils.isEmpty(mResponseField))
				req.mResponseField = mResponseField;

			req.mJson = mJson;

			return req;
		}
	}
}
