package com.ykhuo.im.databse;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ykhuo.im.bean.ApplicationData;
import com.ykhuo.im.bean.ChatEntity;
import com.ykhuo.im.bean.MessageTabEntity;
import com.ykhuo.im.bean.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ImDB {
	public static final String DB_NAME = "im_local";
	private User user = ApplicationData.getInstance().getUserInfo();
	public static final int VERSION = 1;
	private static ImDB imDB;
	private SQLiteDatabase db;

	private ImDB(Context context) {
		ImOpenHelper imOpenHelper = new ImOpenHelper(context, DB_NAME, null,
				VERSION);
		db = imOpenHelper.getWritableDatabase();
	}

	public synchronized static ImDB getInstance(Context context) {
		if (imDB == null)
			imDB = new ImDB(context);
		return imDB;
	}
	
	public void saveUser(User meUser) {
		ContentValues values = new ContentValues();
		values.put("mid", meUser.getAccount());
		values.put("username", meUser.getUsername());
		values.put("password", meUser.getPassword());
		db.insert("save_user", null, values);

	}
	
	public void updateUser(User meUser) {
		String sql = "update save_user set username = \"" + meUser.getUsername()
				+ "\", password = \"" + meUser.getPassword()+ "\" where mid = 1";
		db.execSQL(sql);
	}
	
	public User getmeUser() {
		User muser = new User();
		int id = muser.getAccount();
		Cursor cursor = db.rawQuery(
				"select * from save_user where mid=1", null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				muser.setNickname(cursor.getString(cursor
						.getColumnIndex("username")));
				muser.setPassword(cursor.getString(cursor
						.getColumnIndex("password")));
			}
		}

		if (cursor != null)
			cursor.close();
		return muser;
	}
	
	//===========================================

	public void saveFriend(User friend) {
		ContentValues values = new ContentValues();
		values.put("userid", user.getAccount());
		values.put("friendid", friend.getAccount());
		values.put("name", friend.getNickname());
		values.put("birthday", friend.getBirthday().toString());
		values.put("photo", friend.getPhoto());
		db.insert("friend", null, values);

	}

	public List<User> getAllFriend() {
		List<User> friends = new ArrayList<User>();
		int id = user.getAccount();
		Cursor cursor = db.rawQuery(
				"select * from friend where userid = " + id, null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				User friend = new User();
				friend.setAccount(cursor.getInt(cursor.getColumnIndex("friendid")));
				friend.setNickname(cursor.getString(cursor
						.getColumnIndex("name")));
				friend.setPhoto(cursor.getBlob(cursor.getColumnIndex("photo")));
				friends.add(friend);
			}
		}

		if (cursor != null)
			cursor.close();
		return friends;
	}

	public void saveMessage(MessageTabEntity message) {
		ContentValues values = new ContentValues();
		values.put("userid", user.getAccount());
		values.put("senderid", message.getSenderId());
		values.put("name", message.getName());
		values.put("content", message.getContent());
		values.put("filepath", message.getFilepath());
		values.put("sendtime", message.getSendTime());
		values.put("unread", message.getUnReadCount());
		values.put("type", message.getMessageType());
		db.insert("message", null, values);
	}

	public List<MessageTabEntity> getAllMessage() {
		List<MessageTabEntity> messages = new ArrayList<MessageTabEntity>();
		Cursor cursor = db.rawQuery("select * from message where userid = "
				+ user.getAccount(), null);
		if (cursor != null) {
			while (cursor.moveToNext()) {
				MessageTabEntity message = new MessageTabEntity();
				message.setSenderId(cursor.getInt(cursor
						.getColumnIndex("senderid")));
				message.setName(cursor.getString(cursor.getColumnIndex("name")));
				String time = cursor.getString(cursor
						.getColumnIndex("sendtime"));
				message.setSendTime(time);
				message.setContent(cursor.getString(cursor
						.getColumnIndex("content")));
				message.setFilepath(cursor.getString(cursor
						.getColumnIndex("filepath")));
				message.setMessageType(cursor.getInt(cursor
						.getColumnIndex("type")));
				message.setUnReadCount(cursor.getInt(cursor
						.getColumnIndex("unread")));
				messages.add(message);
			}
		}
		if (cursor != null)
			cursor.close();
		return messages;

	}

	public void deleteMessage(MessageTabEntity message) {
		String sql = "delete from message where userid = " + user.getAccount()
				+ " and senderid =" + message.getSenderId() + " and type = "
				+ message.getMessageType();
		db.execSQL(sql);
	}

	public void updateMessages(MessageTabEntity message) {
		String sql = "update message set unread = " + message.getUnReadCount()
				+ ", content = \"" + message.getContent()+ "\",sendtime = \""
				+ message.getSendTime() + "\" where userid = " + user.getAccount()
				+ " and senderid = " + message.getSenderId() + " and type = "
				+ message.getMessageType();
		db.execSQL(sql);
	}

	public void saveChatMessage(ChatEntity message) {
		ContentValues values = new ContentValues();
		values.put("userid", user.getAccount());
		if (user.getAccount() == message.getSenderId()) {
			values.put("friendid", message.getReceiverId());
			values.put("type", message.getMessageType());
		} else {
			values.put("friendid", message.getSenderId());
			values.put("type", message.getMessageType());
		}
		if(message.getMessageType()!=ChatEntity.SEND&&message.getMessageType()!=ChatEntity.RECEIVE){
			values.put("filepath", message.getFilePath());
			values.put("content", message.getTime());
		}else{
			values.put("filepath", "");
			values.put("content", message.getContent());
		}
		
		values.put("sendtime", message.getSendTime());
		db.insert("chat_message", null, values);
	}

	public List<ChatEntity> getChatMessage(int friendId) {
		Cursor cursor = db.rawQuery(
				"select * from chat_message where userid = " + user.getAccount()
						+ " and friendid = " + friendId, null);
		List<ChatEntity> allMessages = new ArrayList<ChatEntity>();
		if (cursor != null) {
			while (cursor.moveToNext()) {
				ChatEntity chat = new ChatEntity();
				chat.setSenderId(cursor.getInt(cursor
						.getColumnIndex("friendid")));
				chat.setReceiverId(cursor.getInt(cursor
						.getColumnIndex("userid")));
				chat.setContent(cursor.getString(cursor
						.getColumnIndex("content")));
				chat.setFilePath(cursor.getString(cursor
						.getColumnIndex("filepath")));
				chat.setMessageType(cursor.getInt(cursor.getColumnIndex("type")));
				chat.setSendTime(cursor.getString(cursor
						.getColumnIndex("sendtime")));
				allMessages.add(chat);
			}
		}

		if (cursor != null)
			cursor.close();

		return  allMessages;
	}
}
