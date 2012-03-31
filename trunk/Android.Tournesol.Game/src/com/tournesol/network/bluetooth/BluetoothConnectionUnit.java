package com.tournesol.network.bluetooth;

import com.tournesol.game.GameMath;
import com.tournesol.game.utility.RecycleBin;
import com.tournesol.network.ConnectionUnit;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;

public class BluetoothConnectionUnit extends ConnectionUnit {

	private static final long serialVersionUID = -6378154495379809127L;

	@Override
	public void draw(Canvas c){
		
		PointF focus = getFocusPosition();
		Paint paint = getPaint();
		paint.setColor(stroke_color);
		paint.setStyle(Style.STROKE);
		
		float radius = height / 2;
		
		//Côté gauche
		float contact_top_degrees = 190;
		float contact_bottom_degrees = 170;
		float contact_top_x = focus.x - distance + radius * GameMath.cos(contact_top_degrees);
		float contact_top_y = focus.y + radius * GameMath.sin(contact_top_degrees);
		float contact_bottom_x = focus.x - distance + radius * GameMath.cos(contact_bottom_degrees);
		float contact_bottom_y = focus.y + radius * GameMath.sin(contact_bottom_degrees);
		
		Path path = RecycleBin.drawPath;
		path.reset();
		path.moveTo(contact_top_x, contact_top_y);
		path.lineTo(focus.x + -width / 2, contact_top_y);
		path.lineTo(focus.x + -width / 2, contact_bottom_y);
		path.lineTo(contact_bottom_x, contact_bottom_y);
		c.drawPath(path, paint);
		
		RecycleBin.drawRectF.left = focus.x - distance - radius;
		RecycleBin.drawRectF.top = focus.y - radius;
		RecycleBin.drawRectF.right = focus.x - distance + radius;
		RecycleBin.drawRectF.bottom = focus.y + radius;
		c.drawArc(RecycleBin.drawRectF , contact_top_degrees, 270 - contact_top_degrees, false, paint);
		c.drawArc(RecycleBin.drawRectF , 90, contact_bottom_degrees - 90, false, paint);
		
		path.reset();
		path.moveTo(focus.x - distance, focus.y + height * -0.5f);
		path.lineTo(focus.x - distance + height * 0.25f, focus.y + height * -0.25f);
		path.lineTo(focus.x - distance, focus.y + height * 0);
		path.lineTo(focus.x - distance + height * 0.25f, focus.y + height * 0.25f);
		path.lineTo(focus.x - distance, focus.y + height * 0.5f);
		c.drawPath(path, paint);
		
		//Côté droit
		contact_top_degrees = 350;
		contact_bottom_degrees = 10;
		contact_top_x = focus.x + distance + radius * GameMath.cos(contact_top_degrees);
		contact_top_y = focus.y + radius * GameMath.sin(contact_top_degrees);
		contact_bottom_x = focus.x + distance + radius * GameMath.cos(contact_bottom_degrees);
		contact_bottom_y = focus.y + radius * GameMath.sin(contact_bottom_degrees);
		
		path.reset();
		path.moveTo(contact_top_x, contact_top_y);
		path.lineTo(focus.x + width / 2, contact_top_y);
		path.lineTo(focus.x + width / 2, contact_bottom_y);
		path.lineTo(contact_bottom_x, contact_bottom_y);
		c.drawPath(path, paint);
		
		RecycleBin.drawRectF.left = focus.x + distance - radius;
		RecycleBin.drawRectF.top = focus.y - radius;
		RecycleBin.drawRectF.right = focus.x + distance + radius;
		RecycleBin.drawRectF.bottom = focus.y + radius;
		c.drawArc(RecycleBin.drawRectF , 270, contact_top_degrees - 270, false, paint);
		c.drawArc(RecycleBin.drawRectF , contact_bottom_degrees, 90 - contact_bottom_degrees, false, paint);
		
		path.reset();
		path.moveTo(focus.x + distance, focus.y + height * -0.5f);
		path.lineTo(focus.x + distance + height * 0.25f, focus.y + height * -0.25f);
		path.lineTo(focus.x + distance, focus.y + height * 0);
		path.lineTo(focus.x + distance + height * 0.25f, focus.y + height * 0.25f);
		path.lineTo(focus.x + distance, focus.y + height * 0.5f);
		c.drawPath(path, paint);
		
		//Bluetooth
		if(distance == 0){
			path.reset();
			path.moveTo(focus.x, focus.y + height * -0.5f);
			path.lineTo(focus.x, focus.y + height * 0.5f);
			path.moveTo(focus.x - height * 0.25f, focus.y + height * -0.25f);
			path.lineTo(focus.x, focus.y);
			path.lineTo(focus.x - height * 0.25f, focus.y + height * 0.25f);
			
			if(tick_wait > tick_wait_initial / 2)
				paint.setAlpha((int)((tick_wait_initial / 2 - tick_wait) / (tick_wait_initial / 2) * 255f));
			else
				paint.setAlpha((int)((tick_wait) / (tick_wait_initial / 2) * 255f));
			
			c.drawPath(path, paint);
		}
		
	}
}
