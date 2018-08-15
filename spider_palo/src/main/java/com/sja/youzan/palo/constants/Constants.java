package com.sja.youzan.palo.constants;

public class Constants {
	//palo
	public static String PALO_DRIVER = "com.mysql.jdbc.Driver";
	public static String PALO_URL    = "jdbc:mysql://192.168.3.202:9030/db_test";
	public static String PALO_USER   = "root";
	public static String PALO_PASSED = "root";
	public static String BROKER_NAME = "hdfs";
	
	//mysql
	public static String mysql_driver="com.mysql.jdbc.Driver";
	public static String mysql_url="jdbc:mysql://47.100.12.219:3306/youzan_products?useUnicode=true&characterEncoding=UTF-8&useSSL=false";
	public static String mysql_username="root";
	public static String mysql_password="Sja_123456";
	//hdfs
	public static String HDFS_HOME = "hdfs://master:8020/user/spider/dianshang/data_etl/*/*.csv";
	
	//file
	public static String FILE_SUFFIX = ".csv";
	public static String FILE_COLUMNS_TERMINATED = ",";
	
	//table
	public static String TB_ORDER = "tb_shop";

}
