package com.tournesol.drawing;

import android.graphics.Canvas;
import android.graphics.Paint;

public class DrawingBrightnessUp extends Drawing {

	@Override
	public void draw(Canvas c) {
		
		Paint paint = this.getPaint();
		
		float center_x =  width / 2;
		float center_y = width / 2;
		c.drawCircle(center_x, center_y, width / 4, paint);
		
		float inner = width / 2;
		float outer = width * 3 / 4;
		for(int i = 0; i < 8; i++){
			float cos = (float)Math.cos(Math.PI * i * 45 / 180);
			float sin = (float)Math.sin(Math.PI * i * 45 / 180);
			c.drawLine(center_x + inner * cos, center_y + inner * sin, 
					   center_x + outer * cos, center_y + outer * sin, paint);
		}
		
	}

}
