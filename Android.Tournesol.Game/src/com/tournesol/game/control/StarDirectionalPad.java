package com.tournesol.game.control;

import com.tournesol.drawing.DrawingTriangle;
import com.tournesol.game.GameMath;
import com.tournesol.game.utility.RecycleBin;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Region.Op;

public class StarDirectionalPad extends DirectionalPad{

	private static final long serialVersionUID = -6540231352119844952L;

	public StarDirectionalPad(){
		super();

	}
	
	@Override
	public void drawDirectionalPad(Canvas c){
		
		Paint paint = getPaint();
		PointF focus = getFocusPosition();

		Path path = RecycleBin.drawPath;
		
		float buttonRatio = 0.5f;
		
		c.save();
		
		RecycleBin.drawRectF.left = focus.x - width / 4;
		RecycleBin.drawRectF.top = focus.y - height / 4;
		RecycleBin.drawRectF.right = focus.x + width / 4;
		RecycleBin.drawRectF.bottom = focus.y + height / 4;
		c.clipRect(RecycleBin.drawRectF, Op.DIFFERENCE);
		
		RecycleBin.drawRectF.left = focus.x - width / 6;
		RecycleBin.drawRectF.top = focus.y - height / 2;
		RecycleBin.drawRectF.right = focus.x + width / 6;
		RecycleBin.drawRectF.bottom = focus.y + height / 2;
		c.drawRoundRect(RecycleBin.drawRectF, 10, 10, paint);
		
		RecycleBin.drawRectF.left = focus.x - width / 2;
		RecycleBin.drawRectF.top = focus.y - height / 6;
		RecycleBin.drawRectF.right = focus.x + width / 2;
		RecycleBin.drawRectF.bottom = focus.y + height / 6;
		c.drawRoundRect(RecycleBin.drawRectF, 10, 10, paint);
		c.restore();
		
		float arcDistance = GameMath.distance(width * buttonRatio / 2, height / 6);
		float arcStartDegrees = GameMath.degrees(width * buttonRatio / 2, height / 6);
		float arcSweepDegrees = 90 - arcStartDegrees * 2;
		
		RecycleBin.drawRectF.left = focus.x - arcDistance;
		RecycleBin.drawRectF.top = focus.y - arcDistance;
		RecycleBin.drawRectF.right = focus.x + arcDistance;
		RecycleBin.drawRectF.bottom = focus.y + arcDistance;

		c.drawArc(RecycleBin.drawRectF, arcStartDegrees, arcSweepDegrees, true, paint);
		c.drawArc(RecycleBin.drawRectF, arcStartDegrees + 90, arcSweepDegrees, true, paint);
		c.drawArc(RecycleBin.drawRectF, arcStartDegrees + 180, arcSweepDegrees, true, paint);
		c.drawArc(RecycleBin.drawRectF, arcStartDegrees + 270, arcSweepDegrees, true, paint);
		
		DrawingTriangle.singleton.copy(this);
		DrawingTriangle.singleton.style = Style.FILL;
		DrawingTriangle.singleton.init(width * 0.15f, width * 0.15f);
		DrawingTriangle.singleton.manage_center = false;
		DrawingTriangle.singleton.draw(c, focus.x, focus.y - height / 3, 0, 0, 0);
		DrawingTriangle.singleton.draw(c, focus.x, focus.y + height / 3, 180, 0, 0);
		DrawingTriangle.singleton.draw(c, focus.x - width / 3, focus.y, 270, 0, 0);
		DrawingTriangle.singleton.draw(c, focus.x + height / 3, focus.y, 90, 0, 0);
		
		//Dessiner la partie touché
		paint.setStyle(Style.FILL);
		paint.setAlpha(125);
		
		if(direction == DIRECTION_UP){
			path.reset();
			path.moveTo(focus.x - width / 6, focus.y - height * buttonRatio / 2 );
			path.lineTo(focus.x - width / 6, focus.y - height / 2);
			path.lineTo(focus.x + width / 6, focus.y - height / 2);
			path.lineTo(focus.x + width / 6, focus.y - height * buttonRatio / 2);
			path.lineTo(focus.x, focus.y);
			path.close();
			c.drawPath(path, paint);
		}
		
		if(direction == DIRECTION_DOWN){
			path.reset();
			path.moveTo(focus.x - width / 6, focus.y + height * buttonRatio / 2);
			path.lineTo(focus.x - width / 6, focus.y + height / 2);
			path.lineTo(focus.x + width / 6, focus.y + height / 2);
			path.lineTo(focus.x + width / 6, focus.y + height * buttonRatio / 2);
			path.lineTo(focus.x, focus.y);
			path.close();
			c.drawPath(path, paint);
		}
		
		if(direction == DIRECTION_LEFT){
			path.reset();
			path.moveTo(focus.x - width * buttonRatio / 2, focus.y - height / 6);
			path.lineTo(focus.x - width / 2, focus.y - height / 6);
			path.lineTo(focus.x - width / 2, focus.y + height / 6);
			path.lineTo(focus.x - width * buttonRatio / 2, focus.y + height / 6);
			path.lineTo(focus.x, focus.y);
			path.close();
			c.drawPath(path, paint);
		}
		
		if(direction == DIRECTION_RIGHT){
			path.reset();
			path.moveTo(focus.x + width * buttonRatio / 2, focus.y - height / 6);
			path.lineTo(focus.x + width / 2, focus.y - height / 6);
			path.lineTo(focus.x + width / 2, focus.y + height / 6);
			path.lineTo(focus.x + width * buttonRatio / 2, focus.y + height / 6);
			path.lineTo(focus.x, focus.y);
			path.close();
			c.drawPath(path, paint);
		}
		
		if(direction == DIRECTION_DOWN_RIGHT){
			c.drawArc(RecycleBin.drawRectF, arcStartDegrees, arcSweepDegrees, true, paint);
		}
		
		if(direction == DIRECTION_DOWN_LEFT){
			c.drawArc(RecycleBin.drawRectF, arcStartDegrees + 90, arcSweepDegrees, true, paint);
		}
		
		if(direction == DIRECTION_UP_LEFT){
			c.drawArc(RecycleBin.drawRectF, arcStartDegrees + 180, arcSweepDegrees, true, paint);
		}
		
		if(direction == DIRECTION_UP_RIGHT){
			c.drawArc(RecycleBin.drawRectF, arcStartDegrees + 270, arcSweepDegrees, true, paint);
		}
		
	}
	
}
