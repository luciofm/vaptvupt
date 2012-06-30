package com.luciofm.libs.vaptvupt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.util.Log;

public class IOUtils {

	private static final int EOF = -1;

	public static byte[] inputStreamToByteArray(InputStream is)
			throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[16384];

		while ((nRead = is.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}

		buffer.flush();

		return buffer.toByteArray();
	}

	public static byte[] readFileToByteArray(File file) throws IOException {
		InputStream in = null;
		try {
			in = openInputStream(file);
			return toByteArray(in, file.length());
		} finally {
			closeQuietly(in);
		}
	}

	public static byte[] toByteArray(InputStream input, long size)
			throws IOException {

		if (size > Integer.MAX_VALUE) {
			throw new IllegalArgumentException(
					"Size cannot be greater than Integer max value: " + size);
		}

		return toByteArray(input, (int) size);
	}

	public static byte[] toByteArray(InputStream input, int size)
			throws IOException {

		if (size < 0) {
			throw new IllegalArgumentException(
					"Size must be equal or greater than zero: " + size);
		}

		if (size == 0) {
			return new byte[0];
		}

		byte[] data = new byte[size];
		int offset = 0;
		int readed;

		while (offset < size
				&& (readed = input.read(data, offset, size - offset)) != EOF) {
			offset += readed;
		}

		if (offset != size) {
			throw new IOException("Unexpected readed size. current: " + offset
					+ ", excepted: " + size);
		}

		return data;
	}

	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		copy(input, output);
		return output.toByteArray();
	}

	public static void closeQuietly(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		} catch (IOException ioe) {
			// ignore
		}
	}

	public static FileInputStream openInputStream(File file) throws IOException {
		if (file.exists()) {
			if (file.isDirectory()) {
				throw new IOException("File '" + file
						+ "' exists but is a directory");
			}
			if (file.canRead() == false) {
				throw new IOException("File '" + file + "' cannot be read");
			}
		} else {
			throw new FileNotFoundException("File '" + file
					+ "' does not exist");
		}
		return new FileInputStream(file);
	}

	public static boolean copyFile(InputStream input, String file)
			throws IOException {
		OutputStream output;
		int bytes = 0;
		output = new FileOutputStream(file);
		bytes = copy(input, output);

		Log.d("ZynkFramework", "got " + bytes + " bytes...");

		return (bytes > 0 ? true : false);
	}

	public static boolean moveFile(String in, String out) {
		File f1 = new File(in);
		File f2 = new File(out);
		return f1.renameTo(f2);
	}

	public static final int BUFFER_SIZE = 1024 * 8;

	private static int copy(InputStream input, OutputStream output)
			throws IOException {
		byte[] buffer = new byte[BUFFER_SIZE];

		BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
		BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
		int count = 0, n = 0;
		try {
			while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
				out.write(buffer, 0, n);
				count += n;
			}
			out.flush();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				Log.d("ZynkFramework", "out close: " + e);
			}
			try {
				in.close();
			} catch (IOException e) {
				Log.d("ZynkFramework", "in close: " + e);
			}
		}
		return count;
	}

}
