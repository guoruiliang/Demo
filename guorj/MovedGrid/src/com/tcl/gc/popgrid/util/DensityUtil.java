
package com.tcl.gc.popgrid.util;

import android.content.Context;

/**
 * TODO dp和px之间的转换
 * 
 * @author $weidong1$
 * @version $Revision: 1.3 $
 */
public class DensityUtil {

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;

		return (int) ((dpValue * scale) + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 * 
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) ((pxValue / scale) + 0.5f);
	}

	public static float getDensity(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}

	public static int getScreenWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	public static int getScreenHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}
	
	public static int getScreenWidthDp(Context context){
		int width=context.getResources().getDisplayMetrics().widthPixels;
		return (int) (width/getDensity(context));
	}
	
	public static int getScreenHeightDp(Context context){
		int height=context.getResources().getDisplayMetrics().heightPixels;
		return (int) (height/getDensity(context));
	}
	
	  /** 
     * 将px值转换为sp值，保证文字大小不变 
     *  
     * @param pxValue 
     * @param fontScale 
     *            （DisplayMetrics类中属性scaledDensity） 
     * @return 
     */  
    public static int px2sp(Context context, float pxValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (pxValue / fontScale + 0.5f);  
    }  
  
    /** 
     * 将sp值转换为px值，保证文字大小不变 
     *  
     * @param spValue 
     * @param fontScale 
     *            （DisplayMetrics类中属性scaledDensity） 
     * @return 
     */  
    public static int sp2px(Context context, float spValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (spValue * fontScale + 0.5f);  
    } 
    
    
    public static float dp2sp(Context context, float dpValue) {  
    	final float scale = context.getResources().getDisplayMetrics().density;
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return scale/fontScale*dpValue;
    } 
    
}
