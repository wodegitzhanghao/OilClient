package com.oil.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ViewportChangeListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import lecho.lib.hellocharts.view.PreviewLineChartView;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

import com.example.oilclient.R;

/**
 * table activity
 * 
 * @author xu
 *
 */
public class CommonSingleLineChartActivity extends Activity {
	LineChartView lineChar;
	PreviewLineChartView preLinechart;
	String userId;
	String unitId;
	String title;
	List<Map<String, Object>> mapContentList;
	TextView tv_title;
	ImageView iv_more, iv_pageback;

	// Button btn_test;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commonlinechart);
		Intent intent = getIntent();
		if (intent != null) {
			userId = intent.getStringExtra("userId");
			unitId = intent.getStringExtra("unitId");
			title = intent.getStringExtra("title");
			mapContentList = (List<Map<String, Object>>) intent.getSerializableExtra("dataList");
		}
		initView();
		initContentData();
	}

	ChartModel chartModel = new ChartModel();

	private void initContentData() {
		// TODO Auto-generated method stub
		if (mapContentList != null) {
			chartModel.setMapList(mapContentList);
			updateChartData();
		}

	}

	LineChartData lineChartData;

	private void updateChartData() {
		// TODO Auto-generated method stub
		List<AxisValue> axisValues = new ArrayList<AxisValue>();// 横轴标签列
		List<PointValue> values = new ArrayList<PointValue>();// 图表值
		for (int i = 0; i < chartModel.getMapList().size(); i++) {
			PointValue pValue = new PointValue(i,
					Float.valueOf(chartModel.getMapList().get(i).get("unit_value").toString()));
			pValue.setLabel(chartModel.getMapList().get(i).get("unit_value").toString());
			values.add(pValue);
			axisValues.add(new AxisValue(i).setLabel(chartModel.getMapList().get(i).get("data_time").toString()));
		}
		Line line = new Line(values);
		line.setColor(ChartUtils.COLOR_RED).setStrokeWidth(1).setPointRadius(3);
		if (isShowLable) {

			line.setHasLabels(true);
			// line.set
		}

		List<Line> lines = new ArrayList<Line>();
		lines.add(line);
		lineChartData = new LineChartData(lines);
		lineChartData.setAxisXBottom(
				new Axis(axisValues).setHasLines(true).setName("时间").setMaxLabelChars(5).setHasTiltedLabels(true));
		lineChartData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(5).setAutoGenerated(true)
				.setName(chartModel.getChartName()).setInside(true));

		LineChartData predata = new LineChartData(lineChartData);
		predata.getLines().get(0).setColor(ChartUtils.DEFAULT_DARKEN_COLOR).setHasLabels(false);
		predata.setAxisXBottom(new Axis().setName("时间").setHasLines(true).setInside(true));
		predata.setAxisYLeft(new Axis().setName(chartModel.getChartName()).setHasLines(true).setInside(true));
		lineChar.setLineChartData(lineChartData);
		preLinechart.setLineChartData(predata);
		preLinechart.setViewportChangeListener(new ViewportChangeListener() {

			@Override
			public void onViewportChanged(Viewport viewport) {
				// TODO Auto-generated method stub
				lineChar.setCurrentViewport(viewport);
			}
		});
		lineChar.setViewportCalculationEnabled(true);
		Viewport v = new Viewport(0, chartModel.getMaxData(), 6, 0);
		// Viewport v = lineChar.getMaximumViewport();
		// v.set(0, chartModel.getMaxData(), 5, 0);
		lineChar.setCurrentViewportWithAnimation(v, 1000);
		// lineChar.setMaximumViewport(v);
		lineChar.setZoomType(ZoomType.HORIZONTAL);
		previewX(true);
	}

	private void initView() {

		iv_more = (ImageView) findViewById(R.id.iv_more);
		iv_more.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// lineChar.setZoomLevelWithAnimation(6, chartModel.maxData, 2);
				initMenuPop();
			}
		});
		iv_pageback = (ImageView) findViewById(R.id.iv_pageback);
		iv_pageback.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		lineChar = (LineChartView) findViewById(R.id.chart_product_details);
		preLinechart = (PreviewLineChartView) findViewById(R.id.chart_pre_linechart);
		tv_title = (TextView) findViewById(R.id.tv_page_title);
		tv_title.setText(title);
	}

	private void previewX(boolean animate) {
		Viewport temViewPort = new Viewport(lineChar.getCurrentViewport());
		float dx = temViewPort.width() / 8;
		temViewPort.inset(dx, 0);
		if (animate) {
			preLinechart.setCurrentViewportWithAnimation(temViewPort);
		} else {
			preLinechart.setCurrentViewport(temViewPort);
		}
		preLinechart.setZoomType(ZoomType.HORIZONTAL);
	}

	private class ChartModel {
		List<Map<String, Object>> mapList;// 源锟斤拷锟�
		String dataUnit = "";

		public String getDataUnit() {
			return dataUnit;
		}

		public void setDataUnit(String dataUnit) {
			this.dataUnit = dataUnit;
		}

		String chartName;// 图锟斤拷锟斤拷
		JSONArray yList;
		float maxData = -1;// 锟斤拷锟街�

		public List<Map<String, Object>> getMapList() {
			return mapList;
		}

		public void setMapList(List<Map<String, Object>> mapList) {
			this.mapList = mapList;
			Collections.reverse(mapList);
			for (int i = 0; i < mapList.size(); i++) {
				if (i == 0) {
					setChartName(mapList.get(i).get("temp_name").toString());
					setDataUnit(mapList.get(i).get("temp_unit").toString());
				}
				float temData = Float.valueOf(mapList.get(i).get("unit_value").toString());
				if (maxData < temData) {
					maxData = temData;
				}
			}

		}

		public String getChartName() {
			return chartName;
		}

		public void setChartName(String chartName) {
			this.chartName = chartName;
		}

		public float getMaxData() {
			return maxData;
		}

		JSONArray xlable;
	}

	PopupMenu pmenu;
	boolean isShowLable = true;
	boolean isLandScape = false;

	private void initMenuPop() {
		// TODO Auto-generated method stub
		pmenu = new PopupMenu(CommonSingleLineChartActivity.this, iv_more);
		pmenu.getMenuInflater().inflate(R.menu.popu_menu_chart, pmenu.getMenu());
		pmenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				switch (item.getItemId()) {
				case R.id.item_showlable:
					isShowLable = !isShowLable;
					updateChartData();
					break;
				case R.id.item_screentrans:
					// isLandScape = !isLandScape;
					if (isLandScape) {
						setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
						isLandScape = false;
					} else {
						setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
						isLandScape = true;
					}
					break;
				case R.id.item_totalchartshow:
					if (preLinechart.isShown()) {
						preLinechart.setVisibility(View.GONE);
					} else {
						preLinechart.setVisibility(View.VISIBLE);
					}
					break;
				default:
					break;
				}
				return true;
			}
		});
		pmenu.show();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
}
