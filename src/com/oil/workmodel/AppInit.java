package com.oil.workmodel;

import java.io.File;
import java.io.UnsupportedEncodingException;

import com.oil.bean.Constants;
import com.oil.inter.OnReturnListener;
import com.oil.utils.FileUtils;
import com.oil.utils.HttpTool;
import com.oil.utils.StringUtils;

import android.content.Context;

/**
 * 应用初始化的相关操作
 * 
 * @author Administrator
 *
 */
public class AppInit {
	public static final String fileName_proStruct = "productStruct.json";
	private Context context;

	public AppInit(Context context) {
		this.context = context;
	}

	/**
	 * 获取产品结构
	 */
	public void initProductStruct() {
		final File file = new File(Constants.PathAppInit, fileName_proStruct);
		if (file.exists()) {
			return;
		} else {
			String url = Constants.URL_GETPROSTRUCTURE;
			HttpTool.netRequestNoCheck(context, null, new OnReturnListener() {

				@Override
				public void onReturn(String jsString) {
					// TODO Auto-generated method stub
					try {
						file.getParentFile().mkdirs();
						new FileUtils().savaData(file.getAbsolutePath(),
								StringUtils.convertStringToIs(jsString));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}, url, false);

		}

	}
}
