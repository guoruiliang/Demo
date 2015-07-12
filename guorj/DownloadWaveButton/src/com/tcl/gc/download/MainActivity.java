package com.tcl.gc.download;

import java.util.Random;

import com.tcl.gc.download.view.CustomProgressWithStatus;
import com.tcl.gc.download.view.CustomProgressWithStatus.CustomDownloadStatus;
import com.tcl.gc.download.view.DownloadWaveView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/**
 * 
 */
public class MainActivity extends Activity implements OnClickListener {
CustomProgressWithStatus mCustomProgress;
int percent;
Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.mContext=MainActivity.this;
		
		
		mCustomProgress=(CustomProgressWithStatus)findViewById(R.id.customProgress);
		mCustomProgress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				CustomDownloadStatus status =mCustomProgress.getIconStatus();
				
				switch (status) {
				case DOWNLOAD:
					Toast.makeText(mContext, "控件处于普通状态，即可下载状态，点击后触发下载事件", Toast.LENGTH_LONG).show();
					break;
				case UPDATE:
					Toast.makeText(mContext, "控件处于可升级状态，点击后触发下载升级事件", Toast.LENGTH_LONG).show();
					break;
				case OPEN:
					Toast.makeText(mContext, "控件处于可打开状态，点击后打开APP", Toast.LENGTH_LONG).show();
					break;
				//如果是系统安装，就不会有这个状态了
				case INSTALL:
					Toast.makeText(mContext, "处于下载完毕，未安装状态，点击调用安装功能", Toast.LENGTH_LONG).show();
					break;
				case PAUSE:
					Toast.makeText(mContext, "控件处于暂停状态，点击后恢复下载", Toast.LENGTH_LONG).show();
					//暂停事件后，可以直接设置这个状态为下载中。
					mCustomProgress.setStatus(CustomDownloadStatus.DOWNLOADING);
					break;
				case DOWNLOADING:
					Toast.makeText(mContext, "处于下载中状态，点击会暂停", Toast.LENGTH_LONG).show();
					mCustomProgress.setStatus(CustomDownloadStatus.PAUSE);
					break;
				case CHECK:
					Toast.makeText(mContext, "详情", Toast.LENGTH_LONG).show();
					break;
					
				default:
					break;
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.download:
			mCustomProgress.setStatus(CustomDownloadStatus.DOWNLOAD);
			break;
		case R.id.open:
			mCustomProgress.setStatus(CustomDownloadStatus.OPEN);
			break;
		case R.id.install:
			mCustomProgress.setStatus(CustomDownloadStatus.INSTALL);
			break;
		case R.id.waiting:
			mCustomProgress.setStatus(CustomDownloadStatus.WAITING);
			break;
		case R.id.pause:
			mCustomProgress.setStatus(CustomDownloadStatus.PAUSE);
			break;
		case R.id.update:
			mCustomProgress.setStatus(CustomDownloadStatus.UPDATE);
			break;
		case R.id.downloading:
			mCustomProgress.setStatus(CustomDownloadStatus.DOWNLOADING);
			break;
		case R.id.check:
			mCustomProgress.setStatus(CustomDownloadStatus.CHECK);
			break;
		case R.id.progress:
			percent=new Random().nextInt(100);
			mCustomProgress.setMainProgress(percent);
			break;
		case R.id.progressAuto:
			startPercentTest();
			break;
		default:
			break;
		}

	}

	private void startPercentTest() {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {

                percent = 0;
                while (percent <= 100) {
                	
                	
                	setMainProgress(percent);
                    percent += 1;
                    try {
                        Thread.sleep(40);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                percent = 56;
                setMainProgress(percent);
            }
        });
        thread.start();
    }
	
	
	void setMainProgress(int p){
		this.percent=p;
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				 mCustomProgress.setMainProgress(percent);
			}
		});
	}
}
