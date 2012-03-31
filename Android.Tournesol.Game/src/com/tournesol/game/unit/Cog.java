package com.tournesol.game.unit;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.PointF;
import android.graphics.Region.Op;

import com.tournesol.game.GameMath;
import com.tournesol.game.edge.EdgeUnit;
import com.tournesol.game.edge.EdgeVertex;
import com.tournesol.game.edge.PolygoneUnit;
import com.tournesol.game.shape.ShapeEllipse;
import com.tournesol.game.utility.RecycleBin;

public class Cog extends PolygoneUnit {

	private static final long serialVersionUID = -6672876046828298602L;

	//Nombre de coins de la roue
	public int corner_count = 8;
	public float corner_size_ratio = 0.3f; 
	public float inner_circle_ratio = 0.25f;
	
	public void init(float x, float y, float width, float height){
		super.init(x, y, width, height);
		shapes.clear();
		
		ShapeEllipse ellipse = new ShapeEllipse(this);
		ellipse.width = width;
		ellipse.height = width;
		ellipse.doesDraw = false;
		shapes.add(ellipse);
		
		float degrees_increment = 180f / corner_count; 
		float degrees = 360 - degrees_increment / 2;
		float radius = width / 2;
		float corner_size = radius * corner_size_ratio;
		
		for(int corner = 0; corner < corner_count; corner++){
			
			float contact_top_x = radius * GameMath.cos(degrees);
			float contact_top_y = radius * GameMath.sin(degrees);
			float corner_top_x = contact_top_x + corner_size * GameMath.cos(degrees + degrees_increment / 2);
			float corner_top_y = contact_top_y + corner_size * GameMath.sin(degrees + degrees_increment / 2);
			degrees += degrees_increment;
			float contact_bottom_x = radius * GameMath.cos(degrees);
			float contact_bottom_y = radius * GameMath.sin(degrees);
			float corner_bottom_x = contact_bottom_x + corner_size * GameMath.cos(degrees - degrees_increment / 2);
			float corner_bottom_y = contact_bottom_y + corner_size * GameMath.sin(degrees - degrees_increment / 2);
			
			EdgeVertex contact_top = addVertex(contact_top_x, contact_top_y);
			EdgeVertex contact_bottom = addVertex(contact_bottom_x, contact_bottom_y);
			EdgeVertex corner_top = addVertex(corner_top_x, corner_top_y);
			EdgeVertex corner_bottom = addVertex(corner_bottom_x, corner_bottom_y);
			
			addEdge(contact_top, corner_top);
			addEdge(corner_top, corner_bottom);
			addEdge(corner_bottom, contact_bottom);
			degrees += degrees_increment;
		}
		
		this.rotate = 1;
	}
	
	
	public void draw(Canvas c){
		
		PointF focus = getFocusPosition();
		Paint paint = getPaint();
		
		float radius = width / 2;	
		
		if(style == Style.STROKE)
			c.drawCircle(focus.x, focus.y, radius * inner_circle_ratio, paint);
		
		c.save();
		
		Path path = RecycleBin.drawPath;
		path.reset();
		path.addCircle(focus.x, focus.y, radius * inner_circle_ratio, Direction.CW);
		try { c.clipPath(path, Op.DIFFERENCE); } catch(Exception e){}
		super.draw(c);
		
		c.restore();
	}
}
