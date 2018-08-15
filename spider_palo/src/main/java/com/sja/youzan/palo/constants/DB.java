package com.sja.youzan.palo.constants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
	private Connection con;
	private Connection con1;
	//获取连接
    public Connection getConnection() {
    	try {
    		Class.forName(Constants.PALO_DRIVER);
    		con = DriverManager.getConnection(Constants.PALO_URL, Constants.PALO_USER, Constants.PALO_PASSED);	
    	}catch(ClassNotFoundException e) {
    		e.printStackTrace();
    	}catch(SQLException e) {
    		e.printStackTrace();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return con; 	
    }
    
    //关闭连接
    public void close() throws Exception{
    	if(con != null) {
    		con.close();
    	}
    }
    
    public Connection getConnectionDB() {
    	try {
    		Class.forName(Constants.mysql_driver);
    		con1 = DriverManager.getConnection(Constants.mysql_url, Constants.mysql_username, Constants.mysql_password);	
    	}catch(ClassNotFoundException e) {
    		e.printStackTrace();
    	}catch(SQLException e) {
    		e.printStackTrace();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	return con1; 	
    }
    
 
}
