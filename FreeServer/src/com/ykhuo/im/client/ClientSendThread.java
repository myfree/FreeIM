package com.ykhuo.im.client;

import java.util.concurrent.TimeUnit;

import com.ykhuo.im.bean.TranObject;
import com.ykhuo.im.client.ClientActivity;

/**
 * 发送线程
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
		System.out.println("监听线程运行中");
		while (isRunning) {
			if (mClient.sizeOfQueue() == 0)
				try {
					// 若没有数据则阻塞
					TimeUnit.SECONDS.sleep(3);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			else {
				TranObject tran = mClient.removeQueueEle(0);
				System.out.println("发送中");
				mClient.send(tran);

			}
		}

	}
	
	public void close() {
		isRunning = false;
	}

}
