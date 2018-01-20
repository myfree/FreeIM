/**
 * �ļ�����VerifyUtils.java
 */
package com.ykhuo.im.util;

import java.util.regex.Pattern;

import android.widget.EditText;

/**
 * ������VerifyUtils
 * ˵������֤�˺� �����Ƿ�Ϸ�
 */
public class VerifyUtils {
	public static boolean isNull(EditText editText) {
		String text = editText.getText().toString().trim();
		if (text != null && text.length() > 0) {
			return false;
		}
		return true;
	}

	public static boolean matchAccount(String text) {
		if (Pattern.compile("^[a-z0-9_-]{6,18}$").matcher(text).matches()) {
			return true;
		}
		return false;
	}

	public static boolean matchEmail(String text) {
		if (Pattern.compile("\\w[\\w.-]*@[\\w.]+\\.\\w+").matcher(text)
				.matches()) {
			return true;
		}
		return false;
	}
}
