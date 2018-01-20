package com.ykhuo.im.client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.SocketAddress;

import com.ykhuo.im.bean.TranObject;
import com.ykhuo.im.bean.TranObjectType;
import com.ykhuo.im.client.ClientActivity;

/**
 * 服务器对客户端的监听监听
 * @author zhouwen
 *
 */
public class ClientListenThread implements Runnable {
	private ClientActivity client;
	private ObjectInputStream read;
	private boolean isRunning;
	
	public ClientListenThread(ObjectInputStream read, ClientActivity client) {
		this.read = read;
		this.client = client;
		isRunning = true;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (isRunning) {
			readMsg();
		}
	}
	
	
	private void readMsg() {
		SocketAddress s = client.getmClient().getRemoteSocketAddress();

		try {
			TranObject tran = (TranObject) read.readObject();
			TranObjectType type = tran.getTranType();
			System.out.println(type);
			switch (type) {
			case REGISTER_ACCOUNT:
				String account = (String) tran.getObject();
				System.out.println(account);
				client.checkAccount(account);
				break;
			case REGISTER:
				client.regist(tran);
				break;
			case LOGIN:
				client.login(tran);
				break;
			case SEARCH_FRIEND:
				client.searchFriend(tran);
				break;
			case FRIEND_REQUEST:
				client.friendRequset(tran);
				break;
			case MESSAGE:
				client.sendMessage(tran);
				break;
			case USER_CHANGE:
				client.ChangeUser(tran);
			default:
				break;
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (EOFException e) {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void close() {
		isRunning = false;
	}

}
