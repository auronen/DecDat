package de.lego.gottfried.util;

public class Pair<T1, T2> {
	public T1 left;
	public T2 right;
	
	public Pair(T1 l, T2 r) {
		left = l;
		right = r;
	}

	public static Pair<Integer, String> getIS(Integer i, String s) {
		return new Pair<Integer, String>(i, s);
	}
}
