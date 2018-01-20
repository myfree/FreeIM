package com.ykhuo.im.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View.OnClickListener;

import com.ykhuo.im.R;
import com.ykhuo.im.view.HandyTextView;


public class TextUtils {
	/**
	 * ����»���
	 * 
	 * @param context
	 *            ������
	 * @param textView
	 *            ����»��ߵ�TextView
	 * @param start
	 *            ����»��߿�ʼ��λ��
	 * @param end
	 *            ����»��߽�����λ��
	 */
	public static void addUnderlineText(final Context context,
			final HandyTextView textView, final int start, final int end) {
		textView.setFocusable(true);
		textView.setClickable(true);
		SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(
				textView.getText().toString().trim());
		spannableStringBuilder.setSpan(new UnderlineSpan(), start, end,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.setText(spannableStringBuilder);
	}


	/**
	 * ��ӳ�����
	 * 
	 * @param textView
	 *            �����ӵ�TextView
	 * @param start
	 *            �����ӿ�ʼ��λ��
	 * @param end
	 *            �����ӽ�����λ��
	 * @param listener
	 *            �����ӵĵ��������¼�
	 */
	public static void addHyperlinks(final HandyTextView textView,
			final int start, final int end, final OnClickListener listener) {

		String text = textView.getText().toString().trim();
		SpannableString sp = new SpannableString(text);
		sp.setSpan(new IntentSpan(listener), start, end,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new ForegroundColorSpan(textView.getContext().getResources()
				.getColor(R.color.black)), start, end,
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.setText(sp);
		textView.setMovementMethod(LinkMovementMethod.getInstance());

	}

	/**
	 * �������ջ�ȡ����
	 * 
	 * @param month
	 *            ��
	 * @param day
	 *            ��
	 * @return
	 */
	public static String getConstellation(int month, int day) {
		month++;
		int temp = month *100 + day;
		System.out.println(month + " " + day);
		if (temp >=120&&temp <= 218) {
			return "ˮƿ��";
		} else if (temp >= 219 && temp <= 320) {
			return "˫����";
		} else if (temp >= 321 && temp <= 419) {
			return "������";
		} else if (temp >= 420&& temp <= 520) {
			return "��ţ��";
		} else if (temp >= 521&& temp <= 621) {
			return "˫����";
		} else if (temp >= 622&& temp <= 722) {
			return "��з��";
		} else if (temp >= 723&& temp <= 822) {
			return "ʨ����";
		} else if (temp >= 823&& temp <= 922) {
			return "��Ů��";
		} else if (temp >= 923&& temp <= 1023) {
			return "�����";
		} else if (temp >= 1024&& temp <= 1122) {
			return "��Ы��";
		} else if (temp >= 1123&& temp <= 1221) {
			return "������";
		} else 
			return "ħЫ��";
		
	}

	/**
	 * ���������ջ�ȡ����
	 * 
	 * @param year
	 *            ��
	 * @param month
	 *            ��
	 * @param day
	 *            ��
	 * @return
	 */
	public static int getAge(int year, int month, int day) {
		int age = 0;
		month++;
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(Calendar.YEAR) == year) {
			if (calendar.get(Calendar.MONTH) == month) {
				if (calendar.get(Calendar.DAY_OF_MONTH) >= day) {
					age = calendar.get(Calendar.YEAR) - year + 1;
				} else {
					age = calendar.get(Calendar.YEAR) - year;
				}
			} else if (calendar.get(Calendar.MONTH) > month) {
				age = calendar.get(Calendar.YEAR) - year + 1;
			} else {
				age = calendar.get(Calendar.YEAR) - year;
			}
		} else {
			age = calendar.get(Calendar.YEAR) - year;
		}
		if (age < 0) {
			return 0;
		}
		return age;
	}

	/**
	 * ��ȡAssets�е�json�ı�
	 * 
	 * @param context
	 *            ������
	 * @param name
	 *            �ı�����
	 * @return
	 */
	public static String getJson(Context context, String name) {
		if (name != null) {
			String path = "json/" + name;
			InputStream is = null;
			try {
				is = context.getAssets().open(path);
				return readTextFile(is);
			} catch (IOException e) {
				return null;
			} finally {
				try {
					if (is != null) {
						is.close();
						is = null;
					}
				} catch (IOException e) {

				}
			}
		}
		return null;
	}

	/**
	 * ���������л�ȡ�ı�
	 * 
	 * @param inputStream
	 *            �ı�������
	 * @return
	 */
	public static String readTextFile(InputStream inputStream) {
		String readedStr = "";
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			String tmp;
			while ((tmp = br.readLine()) != null) {
				readedStr += tmp;
			}
			br.close();
			inputStream.close();
		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (IOException e) {
			return null;
		}

		return readedStr;
	}

}
