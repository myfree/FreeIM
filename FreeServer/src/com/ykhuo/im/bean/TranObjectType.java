package com.ykhuo.im.bean;

/**
 * �����������
 * 
 * @author way
 * 
 */
public enum TranObjectType {
	REGISTER, // ע��
	REGISTER_ACCOUNT,//ע����˺���֤
	LOGIN, // �û���¼
	LOGOUT, // �û��˳���¼
	FRIENDLOGIN, // ��������
	FRIENDLOGOUT, // ��������
	MESSAGE, // �û�������Ϣ
	UNCONNECTED, // �޷�����
	FILE, // �����ļ�
	REFRESH, // ˢ��
	SEARCH_FRIEND,//������
	FRIEND_REQUEST,//��������
	USER_CHANGE; //�޸��û���Ϣ
}
