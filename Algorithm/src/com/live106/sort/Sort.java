/**
 * 
 */
package com.live106.sort;

import java.util.Arrays;

/**
 * 排序
 * @author live106 @creation Oct 22, 2015
 *
 */
public class Sort {

	private static final int[] inArray = { 2, 3, 9, 3, 5, 10, 1, 52, 45, 26 };

	public static void main(String[] args) {
		int[] in = new int[inArray.length];
		System.arraycopy(inArray, 0, in, 0, inArray.length);

		insertionSort(in);
		System.err.println(Arrays.toString(in));
		int[] out = mergeSort(in);
		System.err.println(Arrays.toString(out));
	}

	/**
	 * <h1>Insertion Sort</h1>
	 * <p>
	 * O(n²)
	 * </p>
	 * <ul>
	 * <li>对有序数组进行同序插入时速度较快</li>
	 * </ul>
	 * 
	 * @param in
	 */
	private static void insertionSort(int[] in) {
		int key = in[0];
		int i = 0;
		for (int j = 1; j < in.length; j++) {
			key = in[j];
			i = j - 1;
			while (i >= 0 && in[i] > key) {
				in[i + 1] = in[i];
				i--;
			}
			in[i + 1] = key;
		}
	}

	/**
	 * <h1>Merge Sort</h1>
	 * <p>
	 * O(nlgn)
	 * </p>
	 * 
	 * @param in
	 */
	private static int[] mergeSort(int[] in) {
		int length = in.length;
		if (length == 1) {
			return in;
		}
		int half = length / 2;
		int[] left = new int[half];
		int[] right = new int[length - half];
		System.arraycopy(in, 0, left, 0, left.length);
		System.arraycopy(in, half, right, 0, right.length);
		
		left = mergeSort(left);
		right = mergeSort(right);
		
		return mergeTwoArray(left, right);
	}

	private static int[] mergeTwoArray(int[] left, int[] right) {
		int[] result = new int[left.length + right.length];
		int i = 0, l = 0, r = 0;
		while (i < result.length) {
			if (left[l] < right[r]) {
				result[i++] = left[l++];
				if (l >= left.length) {
					break;
				}
			} else {
				result[i++] = right[r++];
				if (r >= right.length) {
					break;
				}
			}
		}
		
		while (l < left.length) {
			result[i++] = left[l++];
		}
		
		while (r < right.length) {
			result[i++] = right[r++];
		}
		
		return result;
	}
}
