package com.ykhuo.im.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.ykhuo.im.client.ClientActivity;
import com.ykhuo.im.server.OnMap;

public class ServerListen {
	
	private final int port=8399;
	private ServerSocket server;
	
	public static void main(String[] args) {
		new ServerListen().begin();
	}
	
	public void begin(){
		try {
			server=new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		while(true){
			try {
				Socket client=server.accept();
				System.out.println("有客户端连接了");
				new ClientActivity(this,client);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	/*
	 * 获得在线用户
	 */
    public ClientActivity getClientByID(int id){
    	return OnMap.getInstance().getClientById(id);
    }
    public void closeClientByID(int id){
    	OnMap.getInstance().removeClient(id);
    }
    public void addClient(int id, ClientActivity ca0){
    	OnMap.getInstance().addClient(id, ca0);
    }
    public boolean contatinId(int id){
    	return OnMap.getInstance().isContainId(id);
    }
    public void close(){
    	try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	public int  size() {
		return OnMap.getInstance().size();
	}

}
