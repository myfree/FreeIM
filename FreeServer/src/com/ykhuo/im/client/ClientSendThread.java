package com.ykhuo.im.client;

import java.util.concurrent.TimeUnit;

import com.ykhuo.im.bean.TranObject;
import com.ykhuo.im.client.ClientActivity;

/**
 * �����߳�
 * @author zhouwen
 *
 */
public class ClientSendThread implements Runnable {
	private ClientActivity mClient;
	private boolean isRunning;
	public ClientSendThread(ClientActivity mClient) {
		this.mClient = mClient;
		this.isRunning = true;
	}
	
	@Override
	public void run() {
		System.out.println("�����߳�������");
		while (isRunning) {
			if (mClient.sizeOfQueue() == 0)
				try {
					// ��û������������
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			else {
				TranObject tran = mClient.removeQueueEle(0);
				System.out.println("������");
				mClient.send(tran);

			}
		}

	}
	
	public void close() {
		isRunning = false;
	}

}
