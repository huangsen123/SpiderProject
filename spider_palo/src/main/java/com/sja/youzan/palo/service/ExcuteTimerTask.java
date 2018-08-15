package com.sja.youzan.palo.service;

import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class ExcuteTimerTask {
	public void excuteLoadTask() {
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				try {
					PaloService paloService = new PaloService();
					paloService.loadTable();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				
			}
			
			
		}, 0,1000*60*60*2);
		
		
	}
	

}
