package com.sja.youzan.palo.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtil {

	/**
	 * 保证label的唯一性，并且友显示
	 * 
	 * @return
	 */
	public static String getLabel() {
		Date date = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		String dt = format.format(date);
		return dt;
	}
	
	public static void main(String[] args) {
		for(int i=0;i<10;i++) {
		String dt = getLabel();
		System.out.println(dt);
		}
	}
}
