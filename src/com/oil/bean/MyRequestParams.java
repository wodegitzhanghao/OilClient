package com.oil.bean;

import com.loopj.android.http.RequestParams;
import com.oil.workmodel.AppVersionManager;
import com.oilchem.weixin.mp.aes.AesException;
import com.oilchem.weixin.mp.aes.SHA1;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class MyRequestParams extends RequestParams {

	public MyRequestParams(Context context) {
		if (context != null) {
			SharedPreferences sp = context.getSharedPreferences(Constants.USER_INFO_SHARED, Activity.MODE_PRIVATE);
			this.put("appname", AppVersionManager.OilAppTag + "");
			if (sp.getBoolean(Constants.LOGIN_STATE, false)) { // �ѵ�¼

				this.put("cuuid", sp.getString(Constants.CUUID, ""));

				String accessToken = sp.getString(Constants.ACCESS_TOKEN, "");
				String userName = sp.getString(Constants.USER_NAME, "");
				this.put("accesstoken", accessToken);
				String timestamp = String.valueOf(System.currentTimeMillis() + sp.getLong(Constants.TIME_GAP, 0));
				this.put("timestamp", timestamp);
				this.put("username", userName);

				try {
					this.put("signature", SHA1.getSHA1(new String[] { userName, timestamp, accessToken }));
				} catch (AesException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			this.put("appIdentify", "oilclient");
		}

	}

}
