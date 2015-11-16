package com.live106.sort;

import java.util.Random;

public class Utils {

	private static Random random;

	public static int[] dummyArray(int length, int offset) {
		random = new Random();
		int[] data = new int[length];
		for (int i = 0; i < data.length; i++) {
			data[i] = random.nextInt(100) + offset;
		}
		return data;
	}
}
