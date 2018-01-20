package com.ykhuo.im.network;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.ykhuo.im.bean.TranObject;

public class ClientSendThread {
	private Socket mSocket = null;
	private ObjectOutputStream oos = null;
	public ClientSendThread(Socket socket) {
		this.mSocket = socket;
		try {
			oos = new ObjectOutputStream(mSocket.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void sendMessage(TranObject t) throws IOException{
		System.out.println(t.getObject());
		oos.writeObject(t);
		oos.flush();
	}
}
