package de.lego.gottfried.util;

import java.io.InputStream;

public class Resource {
	public static InputStream ByName(String path) {
		try {
			return Thread.currentThread().getContextClassLoader().getResource(path).openStream();
		}
		catch(Exception e) {
			return null;
		}
	}
}
