package com.ykhuo.im.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

import com.ykhuo.im.client.ClientListenThread;
import com.ykhuo.im.client.ClientSendThread;
import com.ykhuo.im.client.ClientActivity;
import com.ykhuo.im.DataBase.FriendDao;
import com.ykhuo.im.DataBase.SaveMsgDao;
import com.ykhuo.im.DataBase.UserDao;
import com.ykhuo.im.bean.TranObjectType;
import com.ykhuo.im.global.Result;
import com.ykhuo.im.bean.TranObject;
import com.ykhuo.im.bean.User;
import com.ykhuo.im.server.ServerListen;

/**
 * �ͻ����߳�
 * @author zhouwen
 *
 */
public class ClientActivity {
	/*  ���Ͷ��У� ��Ϊ�������ж�������ͻ��˵��̣߳����ܶ����һ������������Ϣ��ÿ���������߳�
	   ��ͬʱ���ô�ʵ����socket����send �����������쳣��*/
	private LinkedList<TranObject> sendQueue; //��������
	private ServerListen mServer;
	private Socket mClient;
	private User user;
	private ClientListenThread mClientListen;
	private ClientSendThread mClientSend;
	private ObjectOutputStream mOutput;
	private ObjectInputStream mInput;    
	
	public ClientActivity(ServerListen mServer,Socket mClient){
		user=new User();
		sendQueue=new LinkedList<TranObject>();
		this.mServer=mServer;
		this.mClient=mClient;

		try {
			mOutput=new ObjectOutputStream(mClient.getOutputStream());
			mInput=new ObjectInputStream(mClient.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		mClientListen = new ClientListenThread(mInput, this);
		mClientSend = new ClientSendThread(this);
		Thread listen = new Thread(mClientListen);
		Thread send = new Thread(mClientSend);
		listen.start();
		send.start();
		
		
	}
	
	public Socket getmClient() {
		return mClient;
	}

	public void setmClient(Socket mClient) {
		this.mClient = mClient;
	}

	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user.setAccount(user.getAccount());
		this.user.setUsername(user.getUsername());
		this.user.setPassword(user.getPassword());
		this.user.setNickname(user.getNickname());
		this.user.setPhoto(user.getPhoto());
		this.user.setSex(user.getSex());
		this.user.setBirthday(user.getBirthday());
	}
	
	
	/**
	 * ���ע���˺��Ƿ����
	 */
	public void checkAccount(String username) {
		mServer.addClient(user.getAccount(), this);
		boolean isExisted = UserDao.selectAccount(username);
		TranObject tran = new TranObject("", TranObjectType.REGISTER_ACCOUNT);
		if (isExisted)
			tran.setResult(Result.ACCOUNT_EXISTED);
		else
			tran.setResult(Result.ACCOUNT_CAN_USE);
		send(tran);
	}
	
	
	/**
	 * ����˺ź��û����Ƿ����
	 */
	public void login(TranObject tran) {
		User user = (User) tran.getObject();
		// ��֤������û����Ƿ���ڣ���������Ϊuser����ֵ
		boolean isExisted = UserDao.login(user);
		if (isExisted == true) {
			UserDao.updateIsOnline(user.getAccount(), 1);
			setUser(user);
			System.out.println(user.getAccount() + "������");
			tran.setResult(Result.LOGIN_SUCCESS);
			mServer.addClient(user.getAccount(), this);
			System.out.println("��ǰ����������" + mServer.size());
			// ��ȡ�����б�
			ArrayList<User> friendList = FriendDao.getFriend(user.getAccount());
			user.setFriendList(friendList);

			tran.setObject(user);

		} else
			tran.setResult(Result.LOGIN_FAILED);
		send(tran);
		/*try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		// ��ȡ������Ϣ
		ArrayList<TranObject> offMsg = SaveMsgDao.selectMsg(user.getAccount());
		for (int i = 0; i < offMsg.size(); i++)
			insertQueue(offMsg.get(i));
		SaveMsgDao.deleteSaveMsg(user.getAccount());

	}
	
	
	/**
	 * ���������Ϣ
	 * @param tran
	 */
	public synchronized void send(TranObject tran) {
		try {
			mOutput.writeObject(tran);
			mOutput.flush();
			notify();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * ע��
	 */
	public void regist(TranObject tran) {
		User user = (User) tran.getObject();
		int id = UserDao.insertInfo(user);
		user.setAccount(id);
		if (id == -1)
			tran.setResult(Result.REGISTER_FAILED);
		else
			tran.setResult(Result.REGISTER_SUCCESS);
		System.out.println("����ע����...");
		send(tran);
	}
	
	/**
	 * �޸���Ϣ
	 */
	public void ChangeUser(TranObject tran) {
		User user = (User) tran.getObject();
		int num = UserDao.updateInfo(user);
		if (num>=1)
			tran.setResult(Result.CHANGE_USER_INFO_SUCCESS);
		else
			tran.setResult(Result.CHANGE_USER_INFO_FAILED);
		System.out.println("����ע����...");
		send(tran);
	}

	/**
	 * �ͻ�������
	 */
	public void getOffLine() {
		mServer.closeClientByID(user.getAccount());
		UserDao.updateIsOnline(user.getAccount(), 0);
	}

	/**
	 * �ر���ͻ��˵�����
	 */
	public void close() {
		try {
			mClient.close();// socket�رպ������ڵ���Ҳ���Զ��ر�
			mClientListen.close();
			mClientSend.close();
			if (user.getAccount() != 0)
				getOffLine();
			System.out.println(user.getAccount() + "������...");
		} catch (IOException e) {
			System.out.println("�ر�ʧ��.....");
			e.printStackTrace();
		}
	}

	/**
	 * ��������
	 */
	public void searchFriend(TranObject tran) {
		String values[] = ((String) tran.getObject()).split(" ");
		ArrayList<User> list;
		if (values[0].equals("0"))
			list = UserDao.selectFriendByAccountOrID(values[1]);
		else
			list = UserDao.selectFriendByMix(values);
		System.out.println((String) tran.getObject());
		System.out.println("���Ϳͻ��˲��ҵĺ����б�...");
		for (int i = 0; i < list.size(); i++)
			System.out.println(list.get(i));
		tran.setObject(list);
		send(tran);
	}
	
	
	/**
	 * �����������
	 */
	public void friendRequset(TranObject tran) {
		System.out.println("��Ӻ���");
		Result result = tran.getResult();
		if (result == Result.FRIEND_REQUEST_RESPONSE_ACCEPT) {
			System.out.println("���շ�id" + tran.getReceiveId());
			FriendDao.addFriend(tran.getReceiveId(), tran.getSendId());
			FriendDao.addFriend(tran.getSendId(), tran.getReceiveId());
			System.out.println("��Ӻ��ѳɹ�....");
			// ����ѷ��� �����Լ�����Ϣ
			tran.setObject(user);
			ArrayList<User> friend = UserDao.selectFriendByAccountOrID(tran
					.getSendId());
			tran.setObject(friend.get(0));
			tran.setSendName(user.getNickname());
			// ���Լ���Ӻ���
			friend = UserDao.selectFriendByAccountOrID(tran.getReceiveId());
			TranObject tran2 = new TranObject();
			tran2.setObject(friend.get(0));
			tran2.setResult(tran.getResult());
			tran2.setReceiveId(tran.getSendId());
			tran2.setSendId(tran.getReceiveId());
			tran2.setSendName(friend.get(0).getNickname());
			tran2.setTranType(tran.getTranType());
			tran2.setSendTime(tran2.getSendTime());
			send(tran2);
		}
		sendFriend(tran);
	}

	/**
	 * ת����Ϣ ��ת������Ϣ���͵� ��������ÿͻ������ӵ� ���Ͷ�����
	 */
	public void sendFriend(TranObject tran) {
		ClientActivity friendClient = null;
		System.out.println("����Ҫ���͵��Ǹ�������" + tran.getReceiveId()
				+":"+ mServer.contatinId(tran.getReceiveId()));
		if (mServer.contatinId(tran.getReceiveId())) {
			friendClient = mServer.getClientByID(tran.getReceiveId());
			System.out.println("���������󷢸�����...");
			friendClient.insertQueue(tran);
		} else {
			SaveMsgDao.insertSaveMsg(user.getAccount(), tran);
		}

	}

	public void sendMessage(TranObject tran) {
		// ��ӵ����ѵķ��Ͷ���
		System.out.println("����������Ϣ....");
		sendFriend(tran);
	}

	/******************************** �Է��Ͷ��е��첽���� ***********************************/
	/**
	 * �������� ����ǴӺ������﷢������ ������ӵ����� �������ƣ���Ϊͬ����̫ǿ ����ֱ�ӷ��ͣ� ���ڷ����߳�
	 */
	public synchronized void insertQueue(TranObject tran) {
		sendQueue.add(tran);
	}

	public synchronized int sizeOfQueue() {
		return sendQueue.size();
	}

	public synchronized TranObject removeQueueEle(int i) {
		return sendQueue.remove(i);
	}
	
	
}
