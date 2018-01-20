package com.ykhuo.im.DataBase;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.PresentationDirection;

import com.ykhuo.im.bean.User;

/**
 * 数据库操作
 * 
 */
public class UserDao {

	private UserDao() {
	}

	/**
	 * 查询账号是否存在
	 * 
	 */
	public static boolean selectAccount(String account) {
		String sql0 = "use myim";
		String sql1 = "select * from im_user where username=?";
		Connection con = DBPool.getConnection();
		try {
			con.setAutoCommit(false);
			PreparedStatement ps;
			ps = con.prepareStatement(sql0);
			ps.execute();

			ps = con.prepareStatement(sql1);
			ps.setString(1, account);
			ResultSet rs = ps.executeQuery();
			return rs.first() ? true : false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DBPool.close(con);
		return false;
	}

	/**
	 * 向数据库中添加账户
	 * 
	 */
	public static int insertInfo(User user) {
		String sql0 = "use myim";
		String sql1 = "insert into im_user (username,password,photo,nickname,sex,birthday)"
				+ " values(?,?,?,?,?,?)";
		Connection con = DBPool.getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			ps.setBytes(3, user.getPhoto());
			ps.setString(4, user.getNickname());
			System.out.println(user.getPhoto().length);
			ps.setInt(5, user.getSex());
			ps.setDate(6, new java.sql.Date(user.getBirthday().getTime()));
			ps.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			try {
				System.out.println("插入数据库异常，正在进行回滚..");
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return getLastID(con);
	}
	
	/**
	 * 修改账户信息
	 * 
	 */
	public static int updateInfo(User user) {
		int record=0;//更新记录数
		String sql0 = "use myim";
		String sql1 = "update im_user set photo=?,nickname=?,sex=?,birthday=? where account=?";
		System.out.println("更新记录");
		Connection con = DBPool.getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e2) {
			e2.printStackTrace();
		}
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setBytes(1, user.getPhoto());
			ps.setString(2, user.getNickname());
			ps.setInt(3, user.getSex());
			ps.setDate(4, new java.sql.Date(user.getBirthday().getTime()));
			ps.setInt(5, user.getAccount());

			record=ps.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			try {
				System.out.println("修改数据库异常，正在进行回滚..");
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return record;
	}

	/**
	 * 得到最后一次插入的值
	 */
	public static int getLastID(Connection con) {
		String sql0 = "use myim";
		String sql1 = "select MAX(account) as ID from im_user";// 注意:使用MAX(account) 必须加上 as
																// id 翻译
		PreparedStatement ps;
		ResultSet rs;
		int id = -1;
		try {
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			rs = ps.executeQuery();
			if (rs.first())
				id = rs.getInt("id");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DBPool.close(con);
		return id;

	}

	/**
	 * 进行登录的验证
	 */
	public static boolean login(User user) {

		boolean isExisted = false;
		String sql0 = "use myim";
		String sql1 = "select * from im_user where username=? and password=?";
		Connection con=null;
		con= DBPool.getConnection();

		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setString(1, user.getUsername());
			ps.setString(2, user.getPassword());
			rs = ps.executeQuery();
			if (rs.first()) {
				isExisted = true;
				System.out.println(rs.getInt("account"));
				// 为用户添加自己的id
				user.setAccount(rs.getInt("account"));
				user.setUsername(rs.getString("username"));
				user.setPassword(rs.getString("password"));
				user.setPhoto(rs.getBytes("photo"));
				user.setBirthday(rs.getDate("birthday"));
				user.setSex(rs.getInt("sex"));
				user.setNickname(rs.getString("nickname"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			rs.close();
			ps.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBPool.close(con);
		return isExisted;
	}

	/**
	 * 更新在线状态
	 */
	public static void updateIsOnline(int id, int Online) {
		String sql0 = "use myim";
		String sql1 = "update im_user set online=? where account=?";
		Connection con = DBPool.getConnection();
		try {
			con.setAutoCommit(false);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, Online);
			ps.setInt(2, id);
			ps.executeUpdate();
			con.commit();
		} catch (SQLException e) {
			try {
				System.out.println("数据库正在回滚....");
				con.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		DBPool.close(con);
	}

	public static ArrayList<User> selectFriendByAccountOrID(Object condition) {
		ArrayList<User> list = new ArrayList<User>();
		String sql0 = "use myim";
		String sql1 = "";
		int conFlag = 0;// 默认是0 表示使用id查找 1为使用id
		if (condition instanceof String) {
			sql1 = "select * from im_user where username=?";
			conFlag = 1;
		} else if (condition instanceof Integer)
			sql1 = "select * from im_user where account=?";
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			if (conFlag == 1)
				ps.setString(1, (String) condition);
			else if (conFlag == 0)
				ps.setInt(1, (Integer) condition);
			rs = ps.executeQuery();
			while (rs.next()) {
				User friend = new User();				
				friend.setAccount(rs.getInt("account"));
				friend.setUsername(rs.getString("username"));
				friend.setBirthday(rs.getDate("birthday"));
				friend.setSex(rs.getInt("sex"));
				friend.setNickname(rs.getString("nickname"));
				
				if (rs.getInt("online") == 1)
					friend.setOnline(true);
				else
					friend.setOnline(false);
				friend.setPhoto(rs.getBytes("photo"));
				//friend.setLocation(rs.getString("location"));
				list.add(friend);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DBPool.close(con);
		return list;
	}

	public static ArrayList<User> selectFriendByMix(String[] mix) {
		ArrayList<User> list = new ArrayList<User>();
		String sql0 = "use myim";
		String sql1 = "select * "
				+ "from im_user "
				+ "where ((YEAR(CURDATE())-YEAR(birthday))-(RIGHT(CURDATE(),5)<RIGHT(birthday,5))) "
				+ "between ? and ? ";
		Connection con = DBPool.getConnection();
		PreparedStatement ps;
		ResultSet rs;
		try {
			if (mix[3].equals("3"))
				sql1 += "and sex=1 or sex=0";
			else if (mix[3].equals("1"))
				sql1 += "and sex=1";
			else if (mix[3].equals("0"))
				sql1 += "and sex=0";
			ps = con.prepareStatement(sql0);
			ps.execute();
			ps = con.prepareStatement(sql1);
			ps.setInt(1, Integer.parseInt(mix[1]));
			ps.setInt(2, Integer.parseInt(mix[2]));
			rs = ps.executeQuery();
			while (rs.next()) {
				User friend = new User();
				friend.setAccount(rs.getInt("account"));
				friend.setUsername(rs.getString("username"));
				friend.setBirthday(rs.getDate("birthday"));
				friend.setSex(rs.getInt("sex"));
				friend.setNickname(rs.getString("nickname"));
				
				if (rs.getInt("online") == 1)
					friend.setOnline(true);
				else
					friend.setOnline(false);
				friend.setPhoto(rs.getBytes("photo"));
				//friend.setLocation(rs.getString("location"));
				list.add(friend);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		DBPool.close(con);
		return list;
	}



}
