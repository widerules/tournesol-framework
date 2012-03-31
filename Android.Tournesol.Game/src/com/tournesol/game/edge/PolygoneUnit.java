package com.tournesol.game.edge;

import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Path.Direction;
import android.graphics.Path.FillType;
import android.graphics.PointF;

import com.tournesol.game.shape.Shape;
import com.tournesol.game.shape.ShapeArc;
import com.tournesol.game.shape.ShapeLine;
import com.tournesol.game.utility.RecycleBin;

public class PolygoneUnit extends EdgeUnit{

	private static final long serialVersionUID = 1401293483201285279L;
	
	public boolean close = true;
	
	public void addPolygone(EdgeVertex... edgeVertices){
		
		this.addEdges(edgeVertices);
		
		if(edgeVertices.length > 2)
			this.addEdge(edgeVertices[edgeVertices.length - 1], edgeVertices[0]);
	}
	
	public void addPolygone(boolean close, float[] points){
		
		int count = points.length / 2 - 1;
		int edge_length = count;
		if(close)
			edge_length++;
		
		int index_point = 2;
		EdgeVertex first_vertex = this.addVertex(points[0], points[1]);
		EdgeVertex last_vertex = first_vertex;
		
		for(int index_edge = 0; index_edge < count; index_edge++){
			EdgeVertex vertex = this.addVertex(points[index_point], points[index_point + 1]);
			addEdge(last_vertex, vertex);
			last_vertex = vertex;

			index_point += 2;
		}
		
		if(close)
			addEdge(last_vertex, first_vertex);
	}
	
	protected void drawConstruct(Canvas c) {
		Paint paint = getPaint();
		PointF focus = getFocusPosition();
		Path path  = RecycleBin.drawPath;
		path.reset();

		int count = this.shapes.size();
		
		boolean start = true;
		if(count > 0)
			start = drawShape(this.shapes.get(0), path, focus, start);	
		
		for(int i = 1; i < count; i++)
			start = drawShape(this.shapes.get(i), path, focus, start);	
		
		if(close)
			path.close();
		
		c.drawPath(path, paint);
	}
	
	private boolean drawShape(Shape shape, Path path, PointF focus, boolean start){
		
		if(!shape.doesDraw)
			return start;
		
		if(shape instanceof ShapeLine){
			ShapeLine line = (ShapeLine)shape;
			
			if(start)
				path.moveTo(focus.x + line.start.x, focus.y + line.start.y);
			else
				path.lineTo(focus.x + line.start.x, focus.y + line.start.y);

			path.lineTo(focus.x + line.end.x, focus.y + line.end.y);
		}
		else if(shape instanceof ShapeArc){
			ShapeArc arc = (ShapeArc)shape;
			
			if(arc.sweepAngle < 360 || arc.width != arc.height){
				RecycleBin.drawRectF.left = focus.x - arc.width / 2 + arc.x;
				RecycleBin.drawRectF.top = focus.y - arc.height / 2 + arc.y;
				RecycleBin.drawRectF.right = focus.x + arc.width / 2 + arc.x;
				RecycleBin.drawRectF.bottom = focus.y + arc.height / 2 + arc.y;
				path.addArc(RecycleBin.drawRectF, arc.startAngle, arc.sweepAngle);
			}
			else{
				path.addCircle(focus.x, focus.y, arc.width / 2, Direction.CW);
			}
		}
		
		return false;
	}
}
