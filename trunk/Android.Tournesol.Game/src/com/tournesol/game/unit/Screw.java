package com.tournesol.game.unit;

import android.graphics.Canvas;
import android.graphics.PointF;

import com.tournesol.game.GameMath;
import com.tournesol.game.edge.EdgeUnit;
import com.tournesol.game.edge.EdgeVertex;
import com.tournesol.game.edge.PolygoneUnit;

public class Screw extends PolygoneUnit {

	private static final long serialVersionUID = -186034849821423709L;
	
	public Screw(){
		
		float radius = width / 2;
		
		EdgeVertex vertexTopLeft = this.addVertex(radius * GameMath.cos(240), 
												  radius * GameMath.sin(240));
		
		EdgeVertex vertexTopRight = this.addVertex(radius * GameMath.cos(300), 
				  							       radius * GameMath.sin(300));
		
		EdgeVertex vertexMiddleRight = this.addVertex(radius, 0);
		
		EdgeVertex vertexBottomRight = this.addVertex(radius * GameMath.cos(60), 
													  radius * GameMath.sin(60));
		
		EdgeVertex vertexBottomLeft = this.addVertex(radius * GameMath.cos(120), 
				  									 radius * GameMath.sin(120));
		
		EdgeVertex vertexMiddleLeft = this.addVertex(-radius, 0);

		this.addPolygone(vertexTopLeft, vertexTopRight, vertexMiddleRight, vertexBottomRight, vertexBottomLeft, vertexMiddleLeft);
	}
	
	@Override
	public void draw(Canvas c){
		super.draw(c);
		PointF focus = getFocusPosition();
		float radius = width / 3;
		c.drawCircle(focus.x, focus.y , radius, getPaint());
	}

}
