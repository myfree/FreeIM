package com.ykhuo.im.server;
import java.util.HashMap;

import com.ykhuo.im.client.*;

/**
 * ���ڱ��������������û���socket����
 * ��ת����Ϣ��ʱ���ú����������������
 */
public class OnMap {
	//��̬��ԱֻҪ������������� �������Ա��һֱ����
	private HashMap<Integer,ClientActivity> clientMap ; 
	private static OnMap instance ;//�˾�̬ʵ���������ɺ��һֱ����
	
	public static OnMap getInstance(){
		if(instance == null)
			instance = new OnMap();
		return instance;
	}
	private OnMap(){
		clientMap = new HashMap<Integer,ClientActivity>();
	}
    public synchronized  ClientActivity getClientById(int id){
    	return clientMap.get(id);
    }
    public synchronized void addClient(int id,ClientActivity ca0){
    	clientMap.put(id, ca0);
    }
    public synchronized void removeClient(int id){
    	clientMap.remove(id);
    }
    public synchronized boolean isContainId(int id){
      	return clientMap.containsKey(id);
    }

	public int size() {
		return clientMap.size();
	}
}
