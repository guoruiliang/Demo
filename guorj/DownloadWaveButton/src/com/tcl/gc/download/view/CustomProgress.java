package com.tcl.gc.download.view;

import com.tcl.gc.download.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 自定义带进度下载按钮控件
 * <P>
 * 圆圈进度显示、自定义文字、更换背景图等
 * 
 * 
 */
public class CustomProgress extends RelativeLayout {

	private ImageView mImageView;// 圆形进度
	protected Context mContext;
	private TextView mTextView;
	private DownloadWaveView mDownloadWaveView;

	public CustomProgress(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setWillNotDraw(false);

		/***** 获取属性值 **********/

		View view = LayoutInflater.from(context).inflate(R.layout.custom_download, this);
		mImageView = (ImageView) view.findViewById(R.id.image);
		mTextView = (TextView) view.findViewById(R.id.text);
		mDownloadWaveView = (DownloadWaveView) view.findViewById(R.id.downloadWaveView);

	}

	/** 设置背景图 */
	public void setImage(int imageSrcId) {
		mImageView.setImageResource(imageSrcId);
	}

	/** 设置文字显示内容 */
	public void setText(String value) {
		mTextView.setText(value);
	}

	/** 设置主进度值 */
	public synchronized void setMainProgress(int progress) {
		mDownloadWaveView.setPercent(progress / 100f);
	}

	public synchronized void resumeDownload() {
		mDownloadWaveView.resumeWave();
	}
	public synchronized void pauseDownload() {
		mDownloadWaveView.pauseWave();
	}

	public synchronized void endDownload() {
		mDownloadWaveView.stopWave();
	}

}
