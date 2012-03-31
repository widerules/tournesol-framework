package com.tournesol.game.shape;

import java.io.Serializable;

import com.tournesol.game.unit.Unit;
import com.tournesol.game.utility.RecycleBin;

import android.graphics.PointF;
import android.graphics.RectF;

public class ShapeRectangle extends Shape implements Serializable{

	private static final long serialVersionUID = -4581108055312743176L;
	
	private ShapeLine line1; 
	private ShapeLine line2;
	private ShapeLine line3;
	private ShapeLine line4;
	
    public ShapeRectangle(Unit unit) {
		super(unit);
		line1 = new ShapeLine(unit);
		line2 = new ShapeLine(unit);
		line3 = new ShapeLine(unit);
		line4 = new ShapeLine(unit);
	}
    

	public float x;
    public float y;
    public float width;
    public float height;
    
    public RectF getAbsoluteRectF(){
    	
		RecycleBin.absoluteRectF.left = x + unit.x - width / 2f;
		RecycleBin.absoluteRectF.top = y + unit.y - width / 2f;
		RecycleBin.absoluteRectF.right = x + unit.x + width / 2f;
		RecycleBin.absoluteRectF.bottom =  y + unit.y + width / 2f;
		return RecycleBin.absoluteRectF;
    }
	
	@Override
	public boolean contains(PointF p) {
		return getAbsoluteRectF().contains(p.x, p.y);
	}
	
	private static final ShapeLine[] lines = new ShapeLine[4];
	public ShapeLine[] getLines(){
		
		line1.start.x = x;
		line1.start.y = y;
		line1.end.x = x;
		line1.end.y = y + height;
		
		line2.start.x = x;
		line2.start.y = y;
		line2.end.x = x + width;
		line2.end.y = y;
		
		line3.start.x = x + width;
		line3.start.y = y;
		line3.end.x = x + width;
		line3.end.y = y + height;
		
		line4.start.x = x;
		line4.start.y = y + height;
		line4.end.x = x + width;
		line4.end.y = y + height;
		
		lines[0] = line1;
		lines[1] = line2;
		lines[2] = line3;
		lines[3] = line4;
		return lines;
	}

	@Override
	public void changeSizeRatio(float width, float height) {
		line1.changeSizeRatio(width, height);
		line2.changeSizeRatio(width, height);
		line3.changeSizeRatio(width, height);
		line4.changeSizeRatio(width, height);
	}

	@Override
	public void rotate(float x, float y, float degrees) {
		// TODO Auto-generated method stub
		
	}
}
