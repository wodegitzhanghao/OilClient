package com.oil.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.example.oilclient.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.oil.activity.NetworkDialog;
import com.oil.activity.RequestFailDialog;
import com.oil.bean.AsyncHttpClientUtil;
import com.oil.bean.Constants;
import com.oil.bean.OilUser;
import com.oil.event.FinishDialog;
import com.oil.inter.OnReturnListener;

import de.greenrobot.event.EventBus;

public class HttpTool {

	public static void netRequest(final Context context,
			com.loopj.android.http.RequestParams params,
			final OnReturnListener listener, final String url,
			final boolean showDia) {

		AsyncHttpClient client = AsyncHttpClientUtil.getInstance(context);
		AsyncHttpResponseHandler hd = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				if (showDia) {
					CommonUtil.createLoadingDialog(
							context,
							context.getResources().getString(
									R.string.loadingtext)).show();
				}

				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				Log.d("value", "success==" + content);
				CommonUtil.cancleDialog();

				if (null != content) {

					if (new JsonValidator().validate(content)) {
						if (null != listener) {
							listener.onReturn(content);
						}
						try {
							JSONObject obj = new JSONObject(content);
							if (obj.has("login")
									&& obj.getString("login").equals("0")) {
								// 登录失效
								OilUser.logOut(context);

							} else if (obj.has("data")) {
								if (obj.getJSONObject("data")
										.has("accessToken")) {

									Editor editor = context
											.getSharedPreferences(
													Constants.USER_INFO_SHARED,
													Activity.MODE_PRIVATE)
											.edit();
									editor.putString(
											Constants.ACCESS_TOKEN,
											new JSONObject(content)
													.getJSONObject("data")
													.getString("accessToken"))
											.commit();

								}
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} else {
						if (!Constants.isRequestFailDialogExist) {
							Intent intent = new Intent(context,
									RequestFailDialog.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(intent);
						}
					}
				}

				super.onSuccess(statusCode, content);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				Log.d("value", "fail==" + content);
				// CommonUtil.cancleDialog();
				// if (!Constants.isRequestFailDialogExist) {
				// Intent intent = new Intent(context, RequestFailDialog.class);
				// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// context.startActivity(intent);
				// }
				super.onFailure(error, content);
			}
		};

		if (CommonUtil.isNetAvailable(context)) {
			client.get(url, params, hd);

			if (Constants.isNetWorkDialogExist) {
				EventBus.getDefault().post(new FinishDialog());
			}
		} else if (!Constants.isNetWorkDialogExist) {
			Intent intent = new Intent(context, NetworkDialog.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}

	}

	/**
	 * 无验证网络请求
	 * 
	 * @param context
	 * @param params
	 * @param listener
	 * @param url
	 * @param showDia
	 */
	public static void netRequestNoCheck(final Context context,
			com.loopj.android.http.RequestParams params,
			final OnReturnListener listener, final String url,
			final boolean showDia) {

		AsyncHttpClient client = AsyncHttpClientUtil.getInstance(context);
		AsyncHttpResponseHandler hd = new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				if (showDia) {
					CommonUtil.createLoadingDialog(
							context,
							context.getResources().getString(
									R.string.loadingtext)).show();
				}

				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				Log.d("value", "success==" + content);
				CommonUtil.cancleDialog();

				if (null != content) {
					if (new JsonValidator().validate(content)) {
						if (null != listener) {
							listener.onReturn(content);
						}

					} else {
						if (!Constants.isRequestFailDialogExist) {
							Intent intent = new Intent(context,
									RequestFailDialog.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(intent);
						}
					}
				}

				super.onSuccess(statusCode, content);
			}

			@Override
			public void onFailure(Throwable error, String content) {
				Log.d("value", "fail==" + content);
				CommonUtil.cancleDialog();
				// if (!Constants.isRequestFailDialogExist) {
				// Intent intent = new Intent(context, RequestFailDialog.class);
				// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				// context.startActivity(intent);
				// }
				super.onFailure(error, content);
			}
		};

		if (CommonUtil.isNetAvailable(context)) {
			client.get(url, params, hd);

			if (Constants.isNetWorkDialogExist) {
				EventBus.getDefault().post(new FinishDialog());
			}
		} else if (!Constants.isNetWorkDialogExist) {
			Intent intent = new Intent(context, NetworkDialog.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		}

	}

}
