package com.tournesol.game.shape;

import java.io.Serializable;

import android.graphics.PointF;
import android.graphics.RectF;

import com.tournesol.game.GameMath;
import com.tournesol.game.unit.Unit;
import com.tournesol.game.utility.RecycleBin;

public class ShapeArc extends Shape implements Serializable {

	public ShapeArc(Unit unit) {
		super(unit);
	}

	private static final long serialVersionUID = 7667266584762051634L;
	
	public float x;
    public float y;
    public float width;
    public float height;
    public float startAngle;
    public float sweepAngle;
    
	public PointF getAbsolutePoint()
	{
		RecycleBin.absolutePointF.x = x + unit.x;
		RecycleBin.absolutePointF.y = y + unit.y;
		return RecycleBin.absolutePointF; 
	}
	
	public RectF getAbsoluteRectF(){
		
		RecycleBin.absoluteRectF.left = x + unit.x - width / 2f;
		RecycleBin.absoluteRectF.top = y + unit.y - height / 2f;
		RecycleBin.absoluteRectF.right = x + unit.x + width / 2f;
		RecycleBin.absoluteRectF.bottom =  y + unit.y + height / 2f;
		return RecycleBin.absoluteRectF;
	}
	
	public boolean isEmpty() {
        return width <= 0.0 || height <= 0.0;
    }
	
	@Override
	public boolean contains(PointF p){
		return getAbsoluteRectF().contains(p.x, p.y);
	}
	
	private static float a;
	private static float b;
	public boolean isBorder(float px, float py) {
		
        if (isEmpty()) {
            return false;
        }

        a = (px - x) / width - 0.5f;
        b = (py - y) / height - 0.5f;
        return a * a + b * b < 0.25;
    }
	
	public boolean containsCollisionAngle(float x, float y){
		 
		//Vérifier que le point de collision est dans l'arc
		float degrees = GameMath.degrees(x - this.x, y - this.y);
		float degrees_min = startAngle;
		
		if(degrees_min < 0)
			degrees_min = 360 + degrees_min;
		
		float degrees_max = degrees_min + sweepAngle;
		

		if(degrees_max > 360)
			return degrees >= degrees_min || degrees <= (degrees_max % 360);
		else
			return degrees >= degrees_min && degrees <= degrees_max;
	}

	@Override
	public void rotate(float degrees, float x, float y) {
		startAngle += degrees;
		PointF p = GameMath.rotate(degrees, this.x, this.y);
		this.x = p.x;
		this.y = p.y;
	}
	
	@Override
	public void changeSizeRatio(float width, float height) {
		this.x *= width;
		this.y *= height;
		this.width *= width;
		this.height *= height;
	}


}
