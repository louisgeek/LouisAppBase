package com.louisgeek.louisappbase.util;

import android.content.Context;
import android.telephony.TelephonyManager;

public class DeviceUtil {
	/**
	 * 获取应用程序的IMEI号
	 */
	public static String getIMEI(Context context) {
		TelephonyManager telecomManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = telecomManager.getDeviceId();
		return imei;
	}

	/**
	 * 获取设备的系统版本号
	 */
	public static int getDeviceSDK() {
		int sdk = android.os.Build.VERSION.SDK_INT;
		return sdk;
	}

	/**
	 * 获取设备的型号
	 */
	public static String getDeviceName() {
		String model = android.os.Build.MODEL;
		return model;
	}
}
