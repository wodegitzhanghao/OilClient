package com.oil.datamodel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.R.integer;

import com.oil.bean.Constants;
import com.oil.utils.FileUtils;
import com.oil.utils.ObjectConvertUtils;
import com.oil.utils.StringUtils;
import com.oil.workmodel.AppInit;

/**
 * ��Ʒ�ṹmodel
 * 
 * @author Administrator
 *
 */
public class OilProductStrucModel {
	public List<Map<String, Object>> productWangList;
	public List<Map<String, Object>> productList;
	public List<Map<String, Object>> productChainList;

	public void OilProductStrucMode() {
		init();
	}

	private void init() {
		// TODO Auto-generated method stub

		try {
			String jsonContent = StringUtils.convertStreamToString(FileUtils
					.openFile(new File(Constants.PathAppInit,
							AppInit.fileName_proStruct).getAbsolutePath()));

			ObjectConvertUtils<List<Map<String, Object>>> ocu = new ObjectConvertUtils<List<Map<String, Object>>>();
			JSONObject jo = new JSONObject(jsonContent);
			setProductWangList(ocu.convert(jo.getString("productWangList")));
			setProductList(ocu.convert(jo.getString("productList")));
			setProductChainList(ocu.convert(jo.getString("productChainList")));
			// productChainLis = ocu.convert(jo.getString("productChainList"));

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public List<Map<String, Object>> getProductWangList() {
		return productWangList;
	}

	public void setProductWangList(List<Map<String, Object>> productWangList) {
		this.productWangList = productWangList;
	}

	public List<Map<String, Object>> getProductList() {
		return productList;
	}

	public void setProductList(List<Map<String, Object>> productList) {
		this.productList = productList;
	}

	public List<Map<String, Object>> getProductChainList() {
		return productChainList;
	}

	public void setProductChainList(List<Map<String, Object>> productChainList) {
		this.productChainList = productChainList;
	}

	public Map<String, Object> getItemFromWangList(int wang_id) {
		for (int i = 0; i < productWangList.size(); i++) {
			if (productWangList.get(i).get("wang_id").toString()
					.equals(wang_id + "")) {
				return productWangList.get(i);

			}
		}
		return null;
	}

	public Map<String, Object> getItemFromChainList(int chan_id) {
		for (int i = 0; i < productChainList.size(); i++) {
			if (productChainList.get(i).get("chan_id").toString()
					.equals(chan_id + "")) {
				return productChainList.get(i);
			}
		}
		return null;
	}

	public Map<String, Object> getItemFromProductList(int pro_id) {

		for (int i = 0; i < productList.size(); i++) {
			if (productList.get(i).get("pro_id").toString().equals(pro_id + "")) {
				return productList.get(i);
			}
		}

		return null;

	}

	public List<Map<String, Object>> getChainListByWangId(int wang_id) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < productChainList.size(); i++) {
			if (productChainList.get(i).get("wang_id").toString()
					.equals(wang_id + "")) {
				mapList.add(productChainList.get(i));
			}
		}
		return mapList;
	}

	public List<Map<String, Object>> getProductListByChainId(int chain_id) {
		List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < productList.size(); i++) {
			if (productList.get(i).get("chan_id").toString()
					.equals(chain_id + "")) {
				mapList.add(productList.get(i));
			}
		}
		return mapList;
	}
}
