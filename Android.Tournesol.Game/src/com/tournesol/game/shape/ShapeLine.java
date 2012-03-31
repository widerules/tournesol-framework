package com.tournesol.game.shape;

import java.io.Serializable;

import com.tournesol.game.GameMath;
import com.tournesol.game.unit.Unit;
import com.tournesol.game.utility.RecycleBin;

import android.graphics.PointF;
import android.graphics.RectF;

public class ShapeLine extends Shape implements Serializable {

	private static final long serialVersionUID = -6278495816214325374L;
	
	public Vertex start;
	public Vertex end;
	
	public ShapeLine(Unit unit) {
		super(unit);
		initPoints();
	}
	
	protected void initPoints(){
		start = new Vertex();
		end = new Vertex();
	}
	
	public PointF getAbsoluteStart()
	{
		RecycleBin.absolutePointF.x = start.x + unit.x;
		RecycleBin.absolutePointF.y = start.y + unit.y;
		return RecycleBin.absolutePointF;
	}
	
	public PointF getAbsoluteEnd()
	{
		RecycleBin.absolutePointF.x = end.x + unit.x;
		RecycleBin.absolutePointF.y = end.y + unit.y;
		return RecycleBin.absolutePointF;
	}
	
	public RectF getAbsoluteRectF()
	{
		if(start.x < end.x){
			RecycleBin.absoluteRectF.left = start.x + unit.x;
			RecycleBin.absoluteRectF.right = end.x + unit.x;
		}
		else {
			RecycleBin.absoluteRectF.left = end.x + unit.x;
			RecycleBin.absoluteRectF.right = start.x + unit.x;
		}
		
		if(start.y < end.y){
			RecycleBin.absoluteRectF.top = start.y + unit.y;
			RecycleBin.absoluteRectF.bottom = end.y + unit.y;
		}
		else {
			RecycleBin.absoluteRectF.top = end.y + unit.y;
			RecycleBin.absoluteRectF.bottom = start.y + unit.y;
		}
		
		return RecycleBin.absoluteRectF;
	}

	@Override
	public boolean contains(PointF p) {
		
		RectF r = getAbsoluteRectF();
		r.inset(-5, -5);
		return r.contains(p.x, p.y);
	}

	@Override
	public void rotate(float degrees, float x, float y) {
		PointF p = GameMath.rotate(degrees, start.x, start.y);//GameMath.rotate(degrees, start.x + x, start.y + y);
		start.x = p.x;
		start.y = p.y;
		
		 p = GameMath.rotate(degrees, end.x, end.y);//GameMath.rotate(degrees, end.x + x, end.y + y);
		 end.x = p.x;
		 end.y = p.y;
	}
	
	@Override
	public void changeSizeRatio(float width, float height) {
		start.x *= width; 
		start.y *= height; 
		end.x *= width; 
		end.y *= height; 
	}
}
