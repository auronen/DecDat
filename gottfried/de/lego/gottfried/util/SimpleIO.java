package de.lego.gottfried.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Vector;

public abstract class SimpleIO {
	public static final String StartupPath = System.getProperty("user.home");
	
	public static InputStream GetStream(String path) {
		InputStream is;
		if((is = Resource.ByName(path)) == null) {
			File f = new File(path);
			try {
				is = new FileInputStream(f);
			}
			catch(FileNotFoundException e) {
				return null;
			}
		}
		return is;
	}
	
	public static String[] ReadAllLines(String path) {
		InputStream is;
		if((is = GetStream(path)) == null)
			return null;
		
		Scanner s = new Scanner(is);
		
		Vector<String> l = new Vector<String>();
		while(s.hasNextLine())
			l.add(s.nextLine());
		String[] lines = new String[l.size()];
		l.toArray(lines);
		
		try {
			s.close();
			is.close();
		}
		catch(Exception e){}
		return lines;
	}
	
	public static String ReadAllText(String path) {
		InputStream is;
		if((is = GetStream(path)) == null)
			return null;
		
		Scanner s = new Scanner(is);
		
		StringBuilder b = new StringBuilder();
		while(s.hasNextLine()) {
			b.append(s.nextLine()).append('\n');
		}
		String full = b.toString();
		
		try {
			s.close();
			is.close();
		}
		catch(Exception e){}
		return full;
	}
	
	public static byte[] ReadAllBytes(String path) {
		InputStream is;
		if((is = GetStream(path)) == null)
			return null;
		
		ByteArrayOutputStream o = new ByteArrayOutputStream();
		
		byte[] data = new byte[16384];
		int n;
		
		try {
			while((n = is.read(data, 0, 16384)) != -1)
				o.write(data, 0, n);
			o.flush();
		}
		catch(IOException e) {
			return null;
		}
		
		try {
			o.close();
			is.close();
		}
		catch(Exception e) {}
		
		return o.toByteArray();
	}
}







