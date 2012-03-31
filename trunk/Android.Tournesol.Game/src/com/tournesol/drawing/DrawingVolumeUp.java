package com.tournesol.drawing;

import com.tournesol.game.utility.RecycleBin;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class DrawingVolumeUp extends Drawing{

	private static final long serialVersionUID = -3390375418832693819L;

	@Override
	public void draw(Canvas c) {
		
		Paint paint = this.getPaint();
		
		Path path = RecycleBin.drawPath;
	    path.reset();
	    path.setFillType(Path.FillType.EVEN_ODD);
	    path.moveTo(0, height / 4);
	    path.lineTo(width / 3, height / 4);
	    path.lineTo(width / 2, 0);
	    path.lineTo(width / 2, height);
	    path.lineTo(width / 3, height * 3 / 4);
	    path.lineTo(0, height * 3 / 4);
	    path.close();
	    
	    c.drawPath(path, paint);
	    
	    float center_x = this.width / 2;
	    float center_y = this.height / 2;
	    
	    RecycleBin.drawRectF.left = center_x - this.width / 4;
	    RecycleBin.drawRectF.top = center_y - this.width / 4;
	    RecycleBin.drawRectF.bottom = center_x + this.width / 4;
	    RecycleBin.drawRectF.right = center_y + this.width / 4;
	    c.drawArc(RecycleBin.drawRectF, 315, 90, false, paint);
		
	    RecycleBin.drawRectF.left = center_x - this.width / 2;
	    RecycleBin.drawRectF.top = center_y - this.width / 2;
	    RecycleBin.drawRectF.bottom = center_x + this.width / 2;
	    RecycleBin.drawRectF.right = center_y + this.width / 2;
	    c.drawArc(RecycleBin.drawRectF, 315, 90, false, paint);
	    
	    RecycleBin.drawRectF.left = center_x - this.width / 1.25f;
	    RecycleBin.drawRectF.top = center_y - this.width / 1.25f;
	    RecycleBin.drawRectF.bottom = center_x + this.width / 1.25f;
	    RecycleBin.drawRectF.right = center_y + this.width / 1.25f;
	    c.drawArc(RecycleBin.drawRectF, 315, 90, false, paint);
	}

}
