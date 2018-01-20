package com.ykhuo.im.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ykhuo.im.bean.ApplicationData;
import com.ykhuo.im.bean.ChatEntity;
import com.ykhuo.im.bean.TranObject;
import com.ykhuo.im.bean.TranObjectType;
import com.ykhuo.im.bean.User;
import com.ykhuo.im.databse.ImDB;
import com.ykhuo.im.global.Result;
import com.ykhuo.im.network.NetService;

public class UserAction {
	private static NetService mNetService = NetService.getInstance();

	public static void accountVerify(String account) throws IOException {

		TranObject t = new TranObject(account, TranObjectType.REGISTER_ACCOUNT);
		mNetService.send(t);

	}

	public static void register(User user) throws IOException {

		TranObject t = new TranObject(user, TranObjectType.REGISTER);
		mNetService.send(t);

	}

	public static void loginVerify(User user) throws IOException {
		TranObject t = new TranObject(user, TranObjectType.LOGIN);
		mNetService.send(t);
	}
	
	public static void UserChange(User user) throws IOException {
		TranObject t = new TranObject(user, TranObjectType.USER_CHANGE);
		mNetService.send(t);
	}

	public static void searchFriend(String message) throws IOException {
		TranObject t = new TranObject(message, TranObjectType.SEARCH_FRIEND);
		mNetService.send(t);

	}

	public static void sendFriendRequest(Result result, Integer id) {
		TranObject t = new TranObject();
		t.setReceiveId(id);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd hh:mm:ss");
		String sendTime = sdf.format(date);
		t.setSendTime(sendTime);
		User user = ApplicationData.getInstance().getUserInfo();
		t.setResult(result);
		t.setSendId(user.getAccount());
		t.setTranType(TranObjectType.FRIEND_REQUEST);
		t.setSendName(user.getNickname());
		try {
			mNetService.send(t);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void sendMessage(ChatEntity message) {

		TranObject t = new TranObject();
		t.setTranType(TranObjectType.MESSAGE);
		t.setReceiveId(message.getReceiverId());
		t.setSendId(message.getSenderId());
		t.setSendName(ApplicationData.getInstance().getUserInfo().getNickname());
		t.setObject(message);
		try {
			mNetService.send(t);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
