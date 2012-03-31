package com.tournesol.game.unit;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;

import com.tournesol.game.GameMath;
import com.tournesol.game.edge.Edge;
import com.tournesol.game.edge.EdgeUnit;
import com.tournesol.game.edge.EdgeVertex;
import com.tournesol.game.edge.PolygoneUnit;
import com.tournesol.game.utility.RecycleBin;

public class Star extends PolygoneUnit{

	private static final long serialVersionUID = 2016118783781696734L;

	public float inner_ratio = 0.5f;
	
	public Star(){
		
		float outer_distance = width;
		float current_degrees = 270;
		EdgeVertex outer_top = this.addVertex(outer_distance * GameMath.cos(current_degrees), outer_distance * GameMath.sin(current_degrees));
		
		current_degrees += 72;
		EdgeVertex outer_top_right = this.addVertex(outer_distance * GameMath.cos(current_degrees), outer_distance * GameMath.sin(current_degrees));
		
		current_degrees += 72;
		EdgeVertex outer_bottom_right = this.addVertex(outer_distance * GameMath.cos(current_degrees), outer_distance * GameMath.sin(current_degrees));
		
		current_degrees += 72;
		EdgeVertex outer_bottom_left = this.addVertex(outer_distance * GameMath.cos(current_degrees), outer_distance * GameMath.sin(current_degrees));
		
		current_degrees += 72;
		EdgeVertex outer_top_left = this.addVertex(outer_distance * GameMath.cos(current_degrees), outer_distance * GameMath.sin(current_degrees));
		
		
		float inner_distance = outer_distance * inner_ratio;
		
		current_degrees = 90;
		EdgeVertex inner_bottom = this.addVertex(inner_distance * GameMath.cos(current_degrees), inner_distance * GameMath.sin(current_degrees));
		
		current_degrees += 72;
		EdgeVertex inner_bottom_left = this.addVertex(inner_distance * GameMath.cos(current_degrees), inner_distance * GameMath.sin(current_degrees));
		
		current_degrees += 72;
		EdgeVertex inner_top_left = this.addVertex(inner_distance * GameMath.cos(current_degrees), inner_distance * GameMath.sin(current_degrees));
		
		current_degrees += 72;
		EdgeVertex inner_top_right = this.addVertex(inner_distance * GameMath.cos(current_degrees), inner_distance * GameMath.sin(current_degrees));
		
		current_degrees += 72;
		EdgeVertex inner_bottom_right = this.addVertex(inner_distance * GameMath.cos(current_degrees), inner_distance * GameMath.sin(current_degrees));
		
		this.addPolygone(outer_top, inner_top_right, outer_top_right, inner_bottom_right, outer_bottom_right, inner_bottom, outer_bottom_left, inner_bottom_left, outer_top_left, inner_top_left);
	}	
}
