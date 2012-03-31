package com.tournesol.drawing;

import com.tournesol.game.GameMath;
import com.tournesol.game.utility.RecycleBin;

import android.graphics.Canvas;
import android.graphics.Path;
import android.util.FloatMath;

public class DrawingTriangle extends Drawing {

	private static final long serialVersionUID = 622626441993954670L;
	
	public static final DrawingTriangle singleton = new DrawingTriangle();
	
	@Override
	public void draw(Canvas c) {
		
	    Path path = RecycleBin.drawPath;
	    path.reset();
	    
	    path.moveTo(width * GameMath.cos(30) / 2, width * GameMath.sin(30) / 2);
	    path.lineTo(width * GameMath.cos(150) / 2, width * GameMath.sin(150) / 2);
	    path.lineTo(width * GameMath.cos(270) / 2, width * GameMath.sin(270) / 2);
	    path.close();

	    c.drawPath(path, this.getPaint());
	}

}
