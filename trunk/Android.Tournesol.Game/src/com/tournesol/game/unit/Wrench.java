package com.tournesol.game.unit;

import com.tournesol.game.GameMath;
import com.tournesol.game.edge.EdgeUnit;
import com.tournesol.game.edge.EdgeVertex;
import com.tournesol.game.shape.ShapeArc;

public class Wrench extends EdgeUnit {

	private static final long serialVersionUID = -176424055845969109L;

	public Wrench(){
		
		this.width = height * 2;
		
		float radius = height / 2f;
		float radius_screw = height / 5;
		
		float screw_degrees = 20;
		float hand_degrees = 20;
		
		ShapeArc arcTop = new ShapeArc(this);
		arcTop.startAngle = screw_degrees + 180;
		arcTop.sweepAngle = 360 - arcTop.startAngle - hand_degrees;
		arcTop.width = radius * 2;
		arcTop.height = radius * 2;
		shapes.add(arcTop);
		
		ShapeArc arcBottom= new ShapeArc(this);
		arcBottom.startAngle = hand_degrees;
		arcBottom.sweepAngle = 180 - arcBottom.startAngle - screw_degrees;
		arcBottom.width = radius * 2;
		arcBottom.height = radius * 2;
		shapes.add(arcBottom);
		
		EdgeVertex screwTopLeft = this.addVertex(radius * GameMath.cos(screw_degrees + 180), 
												 radius * GameMath.sin(screw_degrees + 180));
		
		EdgeVertex screwBottomLeft = this.addVertex(radius * GameMath.cos(180 - screw_degrees), 
													radius * GameMath.sin(180 - screw_degrees));
		
		EdgeVertex screwTopRight = this.addVertex(radius_screw * GameMath.cos(300), 
												  radius_screw * GameMath.sin(300));
		
		EdgeVertex screwBottomRight = this.addVertex(radius_screw * GameMath.cos(60), 
				  									 radius_screw * GameMath.sin(60));
		
		EdgeVertex screwMiddleRight = this.addVertex(radius_screw, 0);
		
		this.addEdges(screwTopLeft, screwTopRight, screwMiddleRight, screwBottomRight, screwBottomLeft);
		
		EdgeVertex handTopLeft = this.addVertex(radius * GameMath.cos(360 - hand_degrees), 
				 							    radius * GameMath.sin(360 - hand_degrees));
		
		EdgeVertex handBottomLeft = this.addVertex(radius * GameMath.cos(hand_degrees), 
				    							   radius * GameMath.sin(hand_degrees));
		
		float radius_hand = handBottomLeft.y - handTopLeft.y;
		float hand_width = width - radius * 2 + radius_hand;
		
		EdgeVertex handTopRight = this.addVertex(handTopLeft.x + hand_width, 
												 handTopLeft.y);
		
		EdgeVertex handBottomRight = this.addVertex(handTopLeft.x + hand_width, 
													handBottomLeft.y);
		
		this.addEdge(handTopLeft, handTopRight);
		this.addEdge(handBottomRight, handBottomLeft);

		ShapeArc arcHand = new ShapeArc(this);
		arcHand.x = handTopRight.x;
		arcHand.y = 0;
		arcHand.startAngle = 270;
		arcHand.sweepAngle = 180;
		arcHand.width = radius_hand;
		arcHand.height = radius_hand;
		shapes.add(arcHand);
	}
}
