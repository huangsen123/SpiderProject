package com.sja.youzan.palo.controller;

import java.sql.SQLException;

import com.sja.youzan.palo.service.ExcuteTimerTask;
import com.sja.youzan.palo.service.PaloService;

public class Main {
	public static void main(String[] args) throws SQLException {
		
        PaloService ps = new PaloService();
//        ExcuteTimerTask excute = new ExcuteTimerTask();
	    ps.initTable();
	    ps.loadTable();
//	    excute.excuteLoadTask();
	       
	}

	// 将数据倒入palo

	protected void loadToPalo() {
		try {
			long time1 = System.currentTimeMillis();
			long time2 = System.currentTimeMillis();
			System.out.println("load CSV 文件");
			PaloService paloService = new PaloService();
			paloService.initTable();
			long time3 = System.currentTimeMillis();
		    paloService.loadTable();
			long time4= System.currentTimeMillis();
			paloService.release();
			System.out.println("创建表耗时:"+(time3-time2));
			System.out.println("数据加载palo耗时:"+(time4-time3));
		}catch (SQLException e) {
            e.printStackTrace();
        }
	}

	

}
