package com.ykhuo.im.bean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;

import com.ykhuo.im.R;
import com.ykhuo.im.activity.ChatActivity;
import com.ykhuo.im.activity.InfoActivity;
import com.ykhuo.im.activity.MainActivity;
import com.ykhuo.im.databse.ImDB;
import com.ykhuo.im.global.Result;
import com.ykhuo.im.util.PhotoUtils;
import com.ykhuo.im.util.ToolsUtils;

public class ApplicationData{

	private static ApplicationData mInitData;

	private User mUser;
	private boolean mIsReceived;
	private List<User> mFriendList;
	private TranObject mReceivedMessage;
	private Map<Integer, Bitmap> mFriendPhotoMap;
	private Handler messageHandler;
	private Handler chatMessageHandler;
	private Handler friendListHandler;
	private Handler InfoHandler;

	private Context mContext;
	private List<User> mFriendSearched;
	private Bitmap mUserPhoto;
	private List<MessageTabEntity> mMessageEntities;// messageFragment显示的列表
	private Map<Integer, List<ChatEntity>> mChatMessagesMap;
	
	private NotificationManager mNManager;
	private Notification notify1;

	public Map<Integer, List<ChatEntity>> getChatMessagesMap() {
		return mChatMessagesMap;
	}

	public void setChatMessagesMap(
			Map<Integer, List<ChatEntity>> mChatMessagesMap) {
		this.mChatMessagesMap = mChatMessagesMap;
	}

	public static ApplicationData getInstance() {
		if (mInitData == null) {
			mInitData = new ApplicationData();
		}
		return mInitData;
	}

	private ApplicationData() {

	}

	public void start() {
		while (!(mIsReceived))
			;
	}
	
	public void ChangeArrived(Object tranObject) {
		mReceivedMessage = (TranObject) tranObject;
		Result loginResult = mReceivedMessage.getResult();
		if(loginResult==Result.CHANGE_USER_INFO_SUCCESS){
			System.out.println("修改成功");
			if (InfoHandler != null) {
				Message message = new Message();
				message.what = 1;
				InfoHandler.sendMessage(message);
			}
		}else{
			System.out.println("修改失败");
			if (InfoHandler != null) {
				Message message = new Message();
				message.what = 2;
				InfoHandler.sendMessage(message);
			}
		}
	}

	public void loginMessageArrived(Object tranObject) {

		mReceivedMessage = (TranObject) tranObject;
		Result loginResult = mReceivedMessage.getResult();
		if (loginResult == Result.LOGIN_SUCCESS) {
			mUser = (User) mReceivedMessage.getObject();
			mFriendList = mUser.getFriendList();// 根据从服务器得到的信息，设置好友是否在线
			mUserPhoto = PhotoUtils.getBitmap(mUser.getPhoto());
			List<User> friendListLocal = ImDB.getInstance(mContext)
					.getAllFriend();
			mFriendPhotoMap = new HashMap<Integer, Bitmap>();
//			for (int i = 0; i < friendListLocal.size(); i++) {
//				User friend = friendListLocal.get(i);
//				Bitmap photo = PhotoUtils.getBitmap(friend.getPhoto());
//				mFriendPhotoMap.put(friend.getAccount(), photo);
//			}
			for (int i = 0; i < mFriendList.size(); i++) {
				User friend = mFriendList.get(i);
				Bitmap photo = PhotoUtils.getBitmap(friend.getPhoto());
				mFriendPhotoMap.put(friend.getAccount(), photo);
			}
			mMessageEntities = ImDB.getInstance(mContext).getAllMessage();
		} else {

			mUser = null;
			mFriendList = null;
		}
		mChatMessagesMap = new HashMap<Integer, List<ChatEntity>>();
		mIsReceived = true;
	}

	public Map<Integer, Bitmap> getFriendPhotoMap() {
		return mFriendPhotoMap;
	}

	public void setFriendPhotoList(Map<Integer, Bitmap> mFriendPhotoMap) {
		this.mFriendPhotoMap = mFriendPhotoMap;
	}

	public User getUserInfo() {
		return mUser;
	}

	public List<User> getFriendList() {
		return mFriendList;
	}

	public void initData(Context comtext) {
		System.out.println("initdata");
		mContext = comtext;
		mIsReceived = false;
		mFriendList = null;
		mUser = null;
		mReceivedMessage = null;
	}

	public TranObject getReceivedMessage() {
		return mReceivedMessage;
	}

	public void setReceivedMessage(TranObject mReceivedMessage) {
		this.mReceivedMessage = mReceivedMessage;
	}

	public List<User> getFriendSearched() {
		return mFriendSearched;
	}

	public void setFriendSearched(List<User> mFriendSearched) {
		this.mFriendSearched = mFriendSearched;
	}

	public void friendRequestArrived(TranObject mReceivedRequest) {
		MessageTabEntity messageEntity = new MessageTabEntity();
		if (mReceivedRequest.getResult() == Result.MAKE_FRIEND_REQUEST) {
			messageEntity.setMessageType(MessageTabEntity.MAKE_FRIEND_REQUEST);
			messageEntity.setContent("希望加你为好友");
		} else if (mReceivedRequest.getResult() == Result.FRIEND_REQUEST_RESPONSE_ACCEPT) {
			messageEntity
					.setMessageType(MessageTabEntity.MAKE_FRIEND_RESPONSE_ACCEPT);
			messageEntity.setContent("接受了你的好友请求");
			User newFriend = (User) mReceivedRequest.getObject();
			if (!mFriendList.contains(newFriend)) {

				mFriendList.add(newFriend);
			}
			
			mFriendPhotoMap.put(newFriend.getAccount(),
					PhotoUtils.getBitmap(newFriend.getPhoto()));
			if (friendListHandler != null) {
				Message message = new Message();
				message.what = 1;
				friendListHandler.sendMessage(message);
			}
			ImDB.getInstance(mContext).saveFriend(newFriend);
		} else {
			messageEntity
					.setMessageType(MessageTabEntity.MAKE_FRIEND_RESPONSE_REJECT);
			messageEntity.setContent("拒绝了你的好友请求");
		}
		messageEntity.setName(mReceivedRequest.getSendName());
		messageEntity.setSendTime(mReceivedRequest.getSendTime());
		messageEntity.setSenderId(mReceivedRequest.getSendId());
		messageEntity.setUnReadCount(1);
		ImDB.getInstance(mContext).saveMessage(messageEntity);
		mMessageEntities.add(messageEntity);
		if (messageHandler != null) {
			Message message = new Message();
			message.what = 1;
			messageHandler.sendMessage(message);
		}
	}

	public void messageArrived(TranObject tran) {
		ChatEntity chat = (ChatEntity) tran.getObject();
		int senderId = chat.getSenderId();
		System.out.println("senderId" + senderId);
		boolean hasMessageTab = false;
		for (int i = 0; i < mMessageEntities.size(); i++) {
			MessageTabEntity messageTab = mMessageEntities.get(i);
			if (messageTab.getSenderId() == senderId
					&& messageTab.getMessageType() == MessageTabEntity.FRIEND_MESSAGE) {
				messageTab.setUnReadCount(messageTab.getUnReadCount() + 1); //消息数量+1
				messageTab.setContent(chat.getContent());
				messageTab.setSendTime(chat.getSendTime());
				
				switch(chat.getMessageType()){
					case ChatEntity.SEND_VOICE:
						byte[] byteVoice=(byte[]) chat.getObject();
						String filepath=ToolsUtils.generateFileName();
						ToolsUtils.getFile(byteVoice,ToolsUtils.getFileUrl(),filepath);
						chat.setFilePath(ToolsUtils.getFileUrl()+"/"+filepath);
						messageTab.setFilepath(ToolsUtils.getFileUrl()+"/"+filepath);
						
						messageTab.setContent("[语音]");
						break;
					case ChatEntity.SEND_PHOTO:
						byte[] bytePhoto=(byte[]) chat.getObject();
						String[] token=chat.getFilePath().split("\\.");
						
						String photoPath=ToolsUtils.generateFileName(token[1]);
						ToolsUtils.getFile(bytePhoto,ToolsUtils.getFileUrl("images"),photoPath);
						chat.setFilePath(ToolsUtils.getFileUrl("images")+"/"+photoPath);
						messageTab.setFilepath(ToolsUtils.getFileUrl("images")+"/"+photoPath);
						
						messageTab.setContent("[图片]");
						break;
					case ChatEntity.SEND_FILE:
						byte[] byteFile=(byte[]) chat.getObject();
						String[] token1=chat.getFilePath().split("\\.");
						
						String FilePath=ToolsUtils.generateFileName(token1[1]);
						ToolsUtils.getFile(byteFile,ToolsUtils.getFileUrl("files"),FilePath);
						chat.setFilePath(ToolsUtils.getFileUrl()+"/"+FilePath);
						messageTab.setFilepath(ToolsUtils.getFileUrl()+"/"+FilePath);
						
						messageTab.setContent("[文件]");
						break;
				}
				showNotification(messageTab.getSenderId(),messageTab.getName(),messageTab.getContent());
				ImDB.getInstance(mContext).updateMessages(messageTab);
				hasMessageTab = true;
			}
		}
		if (!hasMessageTab) { //如果消息列表没有该好友的消息则创建
			MessageTabEntity messageTab = new MessageTabEntity();
			messageTab.setContent(chat.getContent());
			messageTab.setMessageType(MessageTabEntity.FRIEND_MESSAGE);
			messageTab.setName(tran.getSendName());
			messageTab.setSenderId(senderId);

			switch(chat.getMessageType()){
				case ChatEntity.SEND_VOICE:
					messageTab.setContent("[语音]");
					byte[] byteVoice=(byte[]) chat.getObject();
					String filepath=ToolsUtils.generateFileName();
					ToolsUtils.getFile(byteVoice,ToolsUtils.getFileUrl(),filepath);
					chat.setFilePath(ToolsUtils.getFileUrl()+"/"+filepath);
					messageTab.setFilepath(ToolsUtils.getFileUrl()+"/"+filepath);
					break;
					
				case ChatEntity.SEND_PHOTO:
					messageTab.setContent("[图片]");
					String[] token=chat.getFilePath().split("\\.");
					
					byte[] bytePhoto=(byte[]) chat.getObject();
					String photoPath=ToolsUtils.generateFileName(token[1]);
					ToolsUtils.getFile(bytePhoto,ToolsUtils.getFileUrl("images"),photoPath);
					chat.setFilePath(ToolsUtils.getFileUrl("images")+"/"+photoPath);
					messageTab.setFilepath(ToolsUtils.getFileUrl("images")+"/"+photoPath);
					break;
					
				case ChatEntity.SEND_FILE:
					messageTab.setContent("[文件]");
					String[] token1=chat.getFilePath().split("\\.");
					
					byte[] byteFile=(byte[]) chat.getObject();
					String FilePath=ToolsUtils.generateFileName(token1[1]);
					ToolsUtils.getFile(byteFile,ToolsUtils.getFileUrl(),FilePath);
					chat.setFilePath(ToolsUtils.getFileUrl()+"/"+FilePath);
					messageTab.setFilepath(ToolsUtils.getFileUrl()+"/"+FilePath);
					break;
				
			}
			showNotification(messageTab.getSenderId(),messageTab.getName(),messageTab.getContent());
			messageTab.setSendTime(chat.getSendTime());
			messageTab.setUnReadCount(1);
			mMessageEntities.add(messageTab);
			ImDB.getInstance(mContext).saveMessage(messageTab);
		}
		//========================================
		
		switch(chat.getMessageType()){
			case ChatEntity.SEND_VOICE:
				chat.setMessageType(ChatEntity.RECEIVE_VOICE);
				break;
			case ChatEntity.SEND_PHOTO:
				chat.setMessageType(ChatEntity.RECEIVE_PHOTO);
				break;
			case ChatEntity.SEND_FILE:
				chat.setMessageType(ChatEntity.RECEIVE_FILE);
				break;
			default:
				chat.setMessageType(ChatEntity.RECEIVE);
				break;
		}
		
		
		
		List<ChatEntity> chatList = mChatMessagesMap.get(chat.getSenderId());
		if (chatList == null) {
			chatList = ImDB.getInstance(mContext).getChatMessage(
					chat.getSenderId());
			getChatMessagesMap().put(chat.getSenderId(), chatList);
		}
		chatList.add(chat);
		ImDB.getInstance(mContext).saveChatMessage(chat);
		if (messageHandler != null) {
			Message message = new Message();
			message.what = 1;
			messageHandler.sendMessage(message);
		}
		if (chatMessageHandler != null) {
			Message message = new Message();
			message.what = 1;
			chatMessageHandler.sendMessage(message);
		}
	}
	
	public void showNotification(int fid,String name,String text){
		//定义一个PendingIntent点击Notification后启动一个Activity
        Intent it = new Intent(mContext, ChatActivity.class);
        it.putExtra("friendId", fid);
        it.putExtra("friendName", name);
        PendingIntent pit = PendingIntent.getActivity(mContext, 0, it, 0);

        //设置图片,通知标题,发送时间,提示方式等属性
        Notification.Builder mBuilder = new Notification.Builder(mContext);
        mBuilder.setContentTitle(name)                        //标题
                .setContentText(text)      //内容
                .setSubText("——来自Free IM")                    //内容下面的一小段文字
                .setWhen(System.currentTimeMillis())           //设置通知时间
                .setSmallIcon(R.drawable.ic_launcher)            //设置小图标
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)    //设置默认的三色灯与振动器
                .setSound(Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.msg))  //设置自定义的提示音
                .setAutoCancel(true)                           //设置点击后取消Notification
                .setContentIntent(pit);                        //设置PendingIntent
        notify1 = mBuilder.build();
        mNManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        mNManager.notify(111, notify1);
		
		
	}

	public Bitmap getUserPhoto() {
		return mUserPhoto;
	}

	public void setUserPhoto(Bitmap mUserPhoto) {
		this.mUserPhoto = mUserPhoto;
	}

	public List<MessageTabEntity> getMessageEntities() {
		return mMessageEntities;
	}

	public void setMessageEntities(List<MessageTabEntity> mMessageEntities) {
		this.mMessageEntities = mMessageEntities;
	}

	public void setMessageHandler(Handler handler) {
		this.messageHandler = handler;
	}

	public void setChatHandler(Handler handler) {
		this.chatMessageHandler = handler;
	}

	public void setfriendListHandler(Handler handler) {
		this.friendListHandler = handler;
	}
	
	public void setInfoHandler(Handler infoHandler) {
		InfoHandler = infoHandler;
	}
}
