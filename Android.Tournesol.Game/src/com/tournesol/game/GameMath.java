package com.tournesol.game;

import com.tournesol.game.utility.RecycleBin;

import android.graphics.PointF;
import android.util.FloatMath;

/**
 * Classe permettant de faire des opérations mathématiques optimisées
 */
public class GameMath {

	
	public static float distance(float x, float y){
		return FloatMath.sqrt(x*x+y*y);
	}
	
	public static PointF direction(float x, float y){
		
		float distance = distance(x, y);
		if(distance == 0){
			RecycleBin.mathDirectionPointF.x = 0;
			RecycleBin.mathDirectionPointF.y = 0;
		}
		else{
			RecycleBin.mathDirectionPointF.x = x / distance;
			RecycleBin.mathDirectionPointF.y = y / distance;
		}
		
		return RecycleBin.mathDirectionPointF;
	}
	
	public static PointF direction(float degrees){
		RecycleBin.mathDirectionPointF.y = (float) Math.sin(degrees / 360f * Math.PI * 2);
		RecycleBin.mathDirectionPointF.x = (float) Math.cos(degrees / 360f * Math.PI * 2);
		return RecycleBin.mathDirectionPointF;
	}
	
	public static float degrees(float x, float y){
		
		float degrees = 0;
		float distance = GameMath.distance(x, y);
		if(distance != 0)
			degrees = arcCos(x / distance);//  (float)((Math.acos(x / distance) / Math.PI) * 180);
		
		if(y < 0)
			degrees = 360 - degrees;
		
		return degrees % 360;
	}
	
	public static PointF rotate(float degrees, float x, float y){
		
		float distance = GameMath.distance(x, y);
		float old_degrees = GameMath.degrees(x, y);
		RecycleBin.mathRotatePointF.x = distance * GameMath.cos(degrees + old_degrees);
		RecycleBin.mathRotatePointF.y = distance * GameMath.sin(degrees + old_degrees);
		return RecycleBin.mathRotatePointF;
	}
	
	public static float sin(float degrees){
		return (float)Math.sin(Math.PI * degrees / 180);
	}
	
	public static float cos(float degrees){
		return (float)Math.cos(Math.PI * degrees / 180);
	}
	
	public static float arcSin(float value){
		return (float)(Math.asin(value) * 180 / Math.PI);
	}
	
	public static float arcCos(float value){
		return (float)(Math.acos(value) * 180 / Math.PI);
	}
	
	/**
	 * Distance entre une ligne et un point.
	 */
	public static float distance(PointF line_start, PointF line_end, float x, float y){
		return distance(line_start.x, line_start.y, line_end.x, line_end.y, x, y);
	}
	
	public static float distance(float line_start_x, float line_start_y, float line_end_x, float line_end_y, float point_x, float point_y){
		float cross = cross(line_start_x, line_start_y, line_end_x, line_end_y, line_start_x, line_start_y, point_x, point_y);
		return Math.abs(cross / distance(line_end_x - line_start_x, line_end_y - line_start_y));
	}
	
	/**
	 * Produit croisé entre deux vecteurs.
	 */
	public static float cross(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4){
		
		float AB_x = x2 - x1;
		float AB_y = y2 - y1;
		
		float AC_x = x4 - x3;
		float AC_y = y4 - y3;
		
	    return AB_x * AC_y - AB_y * AC_x;
	}
	
	 public static int factorial(int n)
    {
        int ret = 1;
        for (int i = 1; i <= n; ++i) ret += i;
        return ret;
    }
}
