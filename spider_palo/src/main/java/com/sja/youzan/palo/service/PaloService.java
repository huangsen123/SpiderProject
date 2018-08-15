package com.sja.youzan.palo.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.hadoop.fs.FileStatus;

import com.sja.youzan.palo.constants.Constants;
import com.sja.youzan.palo.constants.DB;
import com.sja.youzan.palo.constants.HdfsOperation;

public class PaloService {

	private Connection con;
	private Statement sta;
	private DB db;

	public PaloService() throws SQLException {
		db = new DB();
		con = db.getConnection();
		sta = con.createStatement();
	}
	

	public void initTable() {
		initShop();
//		rollupShopDay();
//		rollupMonitor();
//		rollupShopMonth();
//		rollupMonitor();
//		rollupShopYear();
//		rollupMonitor();
//		rollupProductDay();
//		rollupMonitor();
//		rollupProductMonth();
//		rollupMonitor();
//		rollupProductYear();
//		rollupMonitor();
	}

	public void loadTable() {
		String path = "";
		HdfsOperation hdfsOperation = new HdfsOperation();
		Map<String, FileStatus> modifyMap = hdfsOperation.getModifyFile();
		Set<String> keySet = modifyMap.keySet();
//		for (Iterator<String> it = keySet.iterator(); it.hasNext();) {
//			String key = it.next();
//			FileStatus value = modifyMap.get(key);
//			path = value.getPath().toString();
//			loadShop(path);
//		}
		 loadShop();

	}
	

	private void initShop() {
		String sql = "create table IF NOT EXISTS " + Constants.TB_ORDER + "(" 
	            + "plantform_name varchar(32)," // 属性，电商id
				+ "channel_name varchar(32)," // 渠道id
				+ "shop_kdt_id varchar(32)," // 店铺id
				+ "shop_name varchar(1000)," //店铺名称
				+ "alias varchar(32)," // 商品别名
				+ "product_name varchar(1000)," //商品名称
				+ "order_year INT," // 原始订单创建时间，年
				+ "order_month varchar(32)," // 原始订单创建时间，月
				+ "order_day varchar(32)," // 原始订单创建时间，日
				+ "nickname varchar(32)," // 昵称，复购率
				+ "product_create_time DATE REPLACE," //商品上架时间
				+ "product_price decimal REPLACE,"//商品单价
				+ "update_time DATETIME REPLACE," //最新订单更新时间
				+ "product_total_stock bigint REPLACE," //商品库存
				+ "item_num bigint SUM DEFAULT '0'," // 单订单购买商品数
				+ "order_price decimal SUM DEFAULT '0'," // 订单总价格
				+ "order_num bigint SUM DEFAULT '0'" // 订单数
				+ ")"
				+ " AGGREGATE KEY(plantform_name,channel_name,shop_kdt_id,shop_name,alias,product_name,order_year,order_month,order_day,nickname)"
				+ "PARTITION BY RANGE (order_year) "
				+ "("
				+ " PARTITION p1 VALUES LESS THAN ('2017'),"
				+ " PARTITION p2 VALUES LESS THAN ('2018'),"
				+ " PARTITION p3 VALUES LESS THAN ('2019')"
				+ ")"
				+ " DISTRIBUTED BY HASH (shop_kdt_id,alias) BUCKETS 32" + " properties(\"replication_num\" = \"1\")";
		try {
			sta.execute(sql);
		} catch (SQLException e) {

			e.printStackTrace();
		}
		

	}
	
	public void delay(int second) {
		try {
			Thread.currentThread();
			Thread.sleep(1000*second);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void rollupMonitor() {
		List<String> list = new ArrayList<>();
		String sql = "SHOW ALTER TABLE ROLLUP";
		System.out.println(sql);
		try {
			while (true) {
				ResultSet rs = sta.executeQuery(sql);
				while (rs.next()) {
					list.add(rs.getString("State"));
				}
				if (list.get(list.size() - 1).equals("FINISHED") || list.get(list.size() - 1).equals("CANCELLED")) {
					break;

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

    public void rollupShopDay(){
    	String sql = "ALTER TABLE " + Constants.TB_ORDER + " ADD ROLLUP rollup_shop_day(plantform_name,channel_name,shop_kdt_id,"
                     + "order_year,order_month,order_day,item_num,order_price,order_num)";
    	System.out.println(sql);
    	try {
			sta.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    }
    public void rollupShopMonth(){
    	String sql = "ALTER TABLE " + Constants.TB_ORDER + " ADD ROLLUP rollup_shop_month(plantform_name,channel_name,shop_kdt_id,"
                     + "order_year,order_month,item_num,order_price,order_num)";
    	System.out.println(sql);
    	try {
			sta.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    }
    public void rollupShopYear(){
    	String sql = "ALTER TABLE " + Constants.TB_ORDER + " ADD ROLLUP rollup_shop_year(plantform_name,channel_name,shop_kdt_id,"
                     + "order_year,item_num,order_price,order_num)";
    	System.out.println(sql);
    	try {
			sta.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    }
    
    public void rollupProductDay(){
    	String sql = "ALTER TABLE " + Constants.TB_ORDER + " ADD ROLLUP rollup_product_day(plantform_name,channel_name,alias,"
                     + "order_year,order_month,order_day,item_num,order_price,order_num)";
    	System.out.println(sql);
    	try {
			sta.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    }
    public void rollupProductMonth(){
    	String sql = "ALTER TABLE " + Constants.TB_ORDER + " ADD ROLLUP rollup_product_month(plantform_name,channel_name,alias,"
                     + "order_year,order_month,item_num,order_price,order_num)";
    	System.out.println(sql);
    	try {
			sta.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    }
    public void rollupProductYear(){
    	String sql = "ALTER TABLE " + Constants.TB_ORDER + " ADD ROLLUP rollup_product_year(plantform_name,channel_name,alias,"
                     + "order_year,item_num,order_price,order_num)";
    	System.out.println(sql);
    	try {
			sta.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	
    }
	private void loadShop() {
		String sql = "LOAD LABEL " + Constants.TB_ORDER + "_" + UUID.randomUUID().toString().replace("-", "_") + "(" + " DATA INFILE(\""
				+  Constants.HDFS_HOME+ "\")" + " INTO TABLE " + Constants.TB_ORDER + " COLUMNS TERMINATED BY \""
				+ Constants.FILE_COLUMNS_TERMINATED + "\"" + ")" + " WITH  BROKER " + Constants.BROKER_NAME
				+ " PROPERTIES" + "('timeout'='1800', 'max_filter_ratio'='0.1')";
		System.out.println(UUID.randomUUID().toString().replace("-", "_"));
		
		try {
			sta.execute(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void release() {
		try {
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws SQLException {
		PaloService ps = new PaloService();
		ps.loadTable();
		
	}
	

			

}
