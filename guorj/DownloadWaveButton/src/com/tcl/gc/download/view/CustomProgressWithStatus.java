package com.tcl.gc.download.view;

import com.tcl.gc.download.R;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 自定义带进度,以及状态 下载按钮控件
 * <P>
 * 圆圈进度显示、自定义文字、更换背景图等
 * <P>
 * 可以设置几种不同的状态
 * 
 * 
 */
public class CustomProgressWithStatus extends CustomProgress {

	public CustomProgressWithStatus(Context context, AttributeSet attrs) {
		super(context, attrs);
		setStatus(CustomDownloadStatus.DOWNLOAD);
	}

	/** 当前所处状态 */
	private CustomDownloadStatus mIconStatus = CustomDownloadStatus.DOWNLOAD;

	/**
	 * 设置状态
	 * 
	 * @param status
	 */
	public void setStatus(CustomDownloadStatus status) {
		this.mIconStatus = status;
		// 通用的状态设置
		setImage(status.mImageSrcId);
		setText(mContext.getString(status.mNameId));
		endDownload();
		// 单独设置各种状态
		switch (status) {
		case PAUSE:
			pauseDownload();
			break;
		case DOWNLOADING:
			resumeDownload();// 恢复
			break;
		default:
			break;
		}

	}

	/**
	 * 设置进度，同时也是设置了当前状态为下载中
	 * <P>
	 * 需要在UI线程中执行
	 */
	@Override
	public synchronized void setMainProgress(int progress) {
		// TODO Auto-generated method stub
		super.setMainProgress(progress);
		setStatus(CustomDownloadStatus.DOWNLOADING);
	}

	/** 返回当前状态 */
	public CustomDownloadStatus getIconStatus() {
		return this.mIconStatus;
	}

	/**
	 * app状态
	 * 
	 * 
	 */
	public static enum CustomDownloadStatus {
		DOWNLOAD(R.string.downloadbtn_download, R.drawable.downloadbtn_download), 
		OPEN(R.string.downloadbtn_open,	R.drawable.downloadbtn_open), 
		UPDATE(R.string.downloadbtn_update, R.drawable.downloadbtn_update),
		PAUSE(R.string.downloadbtn_pause,R.drawable.downloadbtn_pause2),
		DOWNLOADING(R.string.downloadbtn_downloading,R.drawable.downloadbtn_download), 
		WAITING(R.string.downloadbtn_waiting,R.drawable.downloadbtn_waiting),
		INSTALL(R.string.downloadbtn_install,R.drawable.downloadbtn_install),
		CHECK(R.string.downloadbtn_check,R.drawable.downloadbtn_check), 
		ERROR(R.string.downloadbtn_download,R.drawable.downloadbtn_uninstall);

		CustomDownloadStatus(int nameId, int imageSrcId) {
			this.mNameId = nameId;
			this.mImageSrcId = imageSrcId;
		}

		private int mNameId;
		private int mImageSrcId;

	}

}
