
package com.tcl.gc.download.view;

import com.tcl.gc.download.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Region.Op;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * 水波浪球形进度View
 * 
 *
 */
public class DownloadWaveView extends FrameLayout {

	ImageView mBackgroundImage;
	private float mPercent;

	private Paint mPaint = new Paint();

	private Bitmap mBitmap;
	private Bitmap mBitmapStop;
	private Bitmap mScaledBitmap;
	private Bitmap mScaledBitmapStop;
	private float mLeft;

	private int mSpeed = 15;

	private int mRepeatCount = 0;

	private Status mFlag = Status.PAUSE;

	public DownloadWaveView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

	}
	/**设置百分比*/
	public void setPercent(float percent) {
		mPercent = percent;
		postInvalidate();
	}
	
	public void resumeWave(){
		mFlag=Status.RUNNING;
		postInvalidate();
	}
	
	/**暂停*/
	public void pauseWave() {
		mFlag = Status.PAUSE;
		postInvalidate();
	}

	/**移除状态*/
	public void stopWave(){
		mFlag=Status.NONE;
		postInvalidate();
	}
	
	public void clear() {
		mFlag = Status.PAUSE;
		if (mScaledBitmap != null) {
			mScaledBitmap.recycle();
			mScaledBitmap = null;
		}

		if (mBitmap != null) {
			mBitmap.recycle();
			mBitmap = null;
		}
		if (mScaledBitmapStop != null) {
			mScaledBitmapStop.recycle();
			mScaledBitmapStop = null;
		}

		if (mBitmapStop != null) {
			mBitmapStop.recycle();
			mBitmapStop = null;
		}
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		int width = getWidth();
		int height = getHeight();
	

		// 裁剪成圆区域
		Path path = new Path();
		canvas.save();
		path.reset();
		canvas.clipPath(path);
		path.addCircle(width / 2, height / 2, width / 2-15, Direction.CCW);//比外面的view半径小15
		canvas.clipPath(path, Op.REPLACE);

		if (mFlag == Status.RUNNING) {
			if (mScaledBitmap == null) {
				mBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.downloadbtn_wave);
				mScaledBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.getWidth(), getHeight(), false);
				mBitmap.recycle();
				mBitmap = null;
				mRepeatCount = (int) Math.ceil(getWidth() / mScaledBitmap.getWidth() + 0.5) + 1;
			}
			for (int idx = 0; idx < mRepeatCount; idx++) {
				canvas.drawBitmap(mScaledBitmap, mLeft + (idx - 1) * mScaledBitmap.getWidth(),
						(1 - mPercent) * getHeight(), null);
			}

			mLeft += mSpeed;
			if (mLeft >= mScaledBitmap.getWidth())
				mLeft = 0;
			// 绘制外圆环
			// mPaint.setStyle(Paint.Style.STROKE);
			// mPaint.setStrokeWidth(4);
			// mPaint.setAntiAlias(true);
			// mPaint.setColor(Color.rgb(33, 211, 39));
			// canvas.drawCircle(width / 2, height / 2, width / 2 - 2, mPaint);

			postInvalidateDelayed(100);
		} else if(mFlag == Status.PAUSE){

			if (mScaledBitmapStop == null) {
				mBitmapStop = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.downloadbtn_stopwave);
				mScaledBitmapStop = Bitmap.createScaledBitmap(mBitmapStop, mBitmapStop.getWidth(), getHeight(), false);
				mBitmapStop.recycle();
				mBitmapStop = null;
			}

			canvas.drawBitmap(mScaledBitmapStop, 0, (1 - mPercent) * getHeight(), null);
		}
		canvas.restore();

	}

	public enum Status {
		RUNNING, PAUSE,NONE
	}

}
