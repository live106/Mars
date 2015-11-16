/**
 * 
 */
package com.live106.sort;

import java.util.Arrays;

/**
 * 最大子数组
 * @author live106 @creation Nov 2, 2015
 *
 */
public class MaxSubArray {
	public static void main(String[] args) {
		int[] data = Utils.dummyArray(10, -50);
		System.err.println(Arrays.toString(data));

		int[] result = getMaxSubArray(data, 0, data.length - 1);
		
		int[] subArray = new int[result[1] - result[0] + 1];
		System.arraycopy(data, result[0], subArray, 0, subArray.length);
		
		System.err.println(Arrays.toString(subArray));
	}

	/**
	 * <h1>分治算法</h1>
	 * <p>
	 * O(nlgn)
	 * </p>
	 * @param data
	 * @param low
	 * @param high
	 * @return
	 */
	private static int[] getMaxSubArray(int[] data, int low, int high) {
		if (low == high) {
			return new int[] { low, high, data[low] };
		} else {
			int mid = (low + high) / 2;
			int[] leftResult = getMaxSubArray(data, low, mid);
			int[] rightResult = getMaxSubArray(data, mid + 1, high);
			int[] crossResult = getMaxCrossSubArray(data, low, mid, high);
			
			if (leftResult[2] >= rightResult[2] && leftResult[2] >= crossResult[2]) {
				return leftResult;
			} else if (rightResult[2] >= leftResult[2] && rightResult[2] >= crossResult[2]) {
				return rightResult;
			} else {
				return crossResult;
			}
		}
	}

	private static int[] getMaxCrossSubArray(int[] data, int low, int mid, int high) {
		int leftSum = Integer.MIN_VALUE;
		int sum = 0;
		int minLeft = mid;
		int maxRight = mid;
		for (int i = mid; i >= low; i--) {
			sum += data[i];
			if (sum > leftSum) {
				leftSum = sum;
				minLeft = i;
			}
		}
		sum = 0;
		int rightSum = Integer.MIN_VALUE;
		for (int i = mid + 1; i <= high; i++) {
			sum += data[i];
			if (sum > rightSum) {
				rightSum = sum;
				maxRight = i;
			}
		}

		return new int[] { minLeft, maxRight, leftSum + rightSum };
	}
}
