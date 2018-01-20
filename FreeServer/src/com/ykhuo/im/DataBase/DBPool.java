package com.ykhuo.im.DataBase;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.commons.dbcp2.BasicDataSourceFactory;

import javax.sql.DataSource;

/**
 * 使用数据库连接池加大响应速度
 *
 */
public class DBPool {
	private static DataSource ds;

	private DBPool() {
	}

	static {
		try {
			InputStream in = DBPool.class.getClassLoader().getResourceAsStream(
					"config.properties");
			//读取Java的配置文件
			Properties pro = new Properties();
			//从输入流中读取属性列表
			pro.load(in);
			//使用apache的数据库连接池
			ds = BasicDataSourceFactory.createDataSource(pro);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		Connection con = null;
		try {
			con = ds.getConnection();
		} catch (SQLException e) {
			System.out.println("获取数据库连接失败....");
			e.printStackTrace();
		}
		return con;
	}

	public static void close(Connection con) {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
