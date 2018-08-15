package com.sja.youzan.palo.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class NumberUtil {
	/**
	 * 格式化成百分数
	 * 
	 * @param val1
	 *            除数
	 * @param val2
	 *            被除数
	 * @param val3
	 *            保留的小数位
	 * @return
	 */
	public static String formatNumber(int val1, int val2, int val3) {
		if (val2 == 0) {
			return "00.00%";
		}
		DecimalFormat format = new DecimalFormat("0%");
		format.setMinimumFractionDigits(val3);
		format.setRoundingMode(RoundingMode.HALF_UP);
		double result = val2 * 1.0 / val1 * 1.0;
		return format.format(result);
	}

}
