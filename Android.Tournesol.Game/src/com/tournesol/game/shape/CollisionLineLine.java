package com.tournesol.game.shape;

import android.graphics.PointF;
import android.graphics.RectF;

import com.tournesol.game.utility.RecycleBin;

public class CollisionLineLine {

	private static float x1;
	private static float y1;
	private static float x2;
	private static float y2;
	private static float x3;
	private static float y3;
	private static float x4;
	private static float y4;
	private static float d;
	private static float x;
	private static float y;
	
	public static PointF collide(ShapeLine line1, ShapeLine line2){
		
		x1 = line1.start.x + line1.unit.x;
		y1 = line1.start.y + line1.unit.y;
		x2 = line1.end.x + line1.unit.x;
		y2 = line1.end.y + line1.unit.y;
		x3 = line2.start.x + line2.unit.x;
		y3 = line2.start.y + line2.unit.y;
		x4 = line2.end.x + line2.unit.x;
		y4 = line2.end.y + line2.unit.y;

		d = (x1-x2)*(y3-y4) - (y1-y2)*(x3-x4);
        if (d == 0) 
        	return null;
            
        x = ((x3-x4)*(x1*y2-y1*x2)-(x1-x2)*(x3*y4-y3*x4))/d;
        y = ((y3-y4)*(x1*y2-y1*x2)-(y1-y2)*(x3*y4-y3*x4))/d;
        
        if(((x + 1 >= x1 && x - 1 <= x2) || (x + 1 >= x2 && x - 1 <= x1)) &&
           ((x + 1 >= x3 && x - 1 <= x4) || (x + 1 >= x4 && x - 1 <= x3)) &&
           ((y + 1 >= y1 && y - 1 <= y2) || (y + 1 >= y2 && y - 1 <= y1)) &&
           ((y + 1 >= y3 && y - 1 <= y4) || (y + 1 >= y4 && y - 1 <= y3))){
        	RecycleBin.collisionPointF.x = x;
        	RecycleBin.collisionPointF.y = y;
        	return RecycleBin.collisionPointF;
        }
        else
        	return null;

	}
}
