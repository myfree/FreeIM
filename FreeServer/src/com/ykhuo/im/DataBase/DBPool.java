package com.ykhuo.im.DataBase;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import org.apache.commons.dbcp2.BasicDataSourceFactory;

import javax.sql.DataSource;

/**
 * ʹ�����ݿ����ӳؼӴ���Ӧ�ٶ�
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
			//��ȡJava�������ļ�
			Properties pro = new Properties();
			//���������ж�ȡ�����б�
			pro.load(in);
			//ʹ��apache�����ݿ����ӳ�
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
			System.out.println("��ȡ���ݿ�����ʧ��....");
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
