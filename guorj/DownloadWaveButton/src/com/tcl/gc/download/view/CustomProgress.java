package com.tcl.gc.download.view;

import com.tcl.gc.download.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
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
	int defaultImageSize=48;//默认图片大小 dp
	
	int textColor;
	float textSize;
	float imageHeight;
	float imageWidth;
	float waveViewHeight;
	float waveViewWidth;

	public CustomProgress(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		setWillNotDraw(false);

		/***** 获取属性值 **********/

		View view = LayoutInflater.from(context).inflate(R.layout.custom_download, this);
		mImageView = (ImageView) view.findViewById(R.id.image);
		mTextView = (TextView) view.findViewById(R.id.text);
		mDownloadWaveView = (DownloadWaveView) view.findViewById(R.id.downloadWaveView);

		int defautlSize = DensityUtil.dip2px(context, defaultImageSize);

		// TypedArray是一个用来存放由context.obtainStyledAttributes获得的属性的数组
		// 在使用完成后，一定要调用recycle方法
		// 属性的名称是styleable中的名称+“_”+属性名称
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar);
		textColor = array.getColor(R.styleable.CustomProgressBar_android_textColor, 0XFF000000); // 默认黑色
		textSize = array.getDimension(R.styleable.CustomProgressBar_android_textSize,  14);//文字大小，默认14dp
		imageWidth = array.getDimension(R.styleable.CustomProgressBar_image_width, defautlSize);
		imageHeight = array.getDimension(R.styleable.CustomProgressBar_image_height, defautlSize);
		array.recycle(); // 一定要调用，否则这次的设定会对下次的使用造成影响
		
		
		//重新设置ImageView的布局大小属性
		RelativeLayout.LayoutParams imageParams=new RelativeLayout.LayoutParams((int)imageWidth,(int)imageHeight);
		imageParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		imageParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		mImageView.setLayoutParams(imageParams);
		mTextView.setTextSize(DensityUtil.px2dip(context, textSize));
		mTextView.setTextColor(textColor);
		
		mDownloadWaveView.setLayoutParams(imageParams);

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
	protected synchronized void setMainProgress(int progress) {
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
