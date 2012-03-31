package com.tournesol.game.unit;

import com.tournesol.game.GameMath;
import com.tournesol.game.shape.ShapeArc;
import com.tournesol.game.shape.ShapeEllipse;
import com.tournesol.game.utility.PaintManager;
import com.tournesol.game.utility.RecycleBin;

import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.RectF;

public class Circle extends ConstructUnit {

	public ShapeArc arc;
	
	public Circle(){
		this.arc = new ShapeArc(this);
		this.shapes.add(arc);
		this.init(0, 0, 0, 0);
	}
	
	@Override
	public void init(float x, float y, float width, float height){

		super.init(x, y, width, width);
		
		arc.x = 0;
		arc.y = 0;
		arc.width = this.width;
		arc.height = this.width;
		arc.startAngle = 0;
		arc.sweepAngle = 360;
	}

	@Override
	public boolean canCollide(Unit unit) {
		return true;
	}
}
