package com.tournesol.game.edge;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import android.graphics.PointF;

import com.tournesol.game.GameMath;
import com.tournesol.game.shape.Vertex;
import com.tournesol.game.unit.ConstructUnit;
import com.tournesol.game.unit.Wall;

public class EdgeUnit extends ConstructUnit {
	
	private static final long serialVersionUID = -3416803835729003198L;
	
	//Liste des sommets
	public HashMap<Integer, EdgeVertex> cache = new HashMap<Integer, EdgeVertex>();
	public transient ArrayList<EdgeVertex> vertices = new ArrayList<EdgeVertex>();
	
	//Séquence pour les id des sommets
	public AtomicInteger sequence_vertex_id = new AtomicInteger();
	
	public EdgeVertex addVertex(float x, float y){
		EdgeVertex vertex = new EdgeVertex();
		vertex.id = sequence_vertex_id.incrementAndGet();
		vertex.x = x;
		vertex.y = y;
		this.cache.put(vertex.id, vertex);
		this.vertices.add(vertex);
		return vertex;
	}
	
	public Edge addEdge(EdgeVertex start, EdgeVertex end){
		Edge edge = new Edge(this);
		
		edge.start = start;
		edge.start_id = start.id;
		
		if(!start.edges.contains(edge))
			start.edges.add(edge);
		
		edge.end = end;
		edge.end_id = end.id;
		
		if(!end.edges.contains(edge))
			end.edges.add(edge);
		
		this.shapes.add(edge);
		return edge;
	}
	
	public void addEdges(EdgeVertex... edgeVertices){
		
		if(edgeVertices.length < 2)
			return;
		
		EdgeVertex lastVertex = edgeVertices[0];
		for(int i = 1; i < edgeVertices.length; i++){
			this.addEdge(lastVertex, edgeVertices[i]);
			lastVertex = edgeVertices[i];
		}
	}
	
	@Override
	public void init(float x, float y, float width, float height){
		
		float width_ratio = width / this.width;
		float height_ratio = height / this.height;
		super.init(x, y, width, height);
		
		int count = vertices.size();
		for(int i = 0; i < count; i++){
			Vertex vertex = vertices.get(i);
			vertex.x *= width_ratio;
			vertex.y *= height_ratio;
		}
		
		count = shapes.size();
		for(int i = 0; i < count; i++)
			shapes.get(i).changeSizeRatio(width_ratio, height_ratio);

	}
	
	@Override
	public boolean contains(PointF point){
		return containsEdge(point);
	}
	
	/**
	 * Algorithme trouvé sur
	 * http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
	 */
	protected boolean containsEdge(PointF point){
		
		int count = vertices.size();
		boolean contains = false;
		
		for (int i = 0, j = count-1; i < count; j = i++) {
			float vertice_i_x = vertices.get(i).x + x;
			float vertice_i_y = vertices.get(i).y + y;
			float vertice_j_x = vertices.get(j).x + x;
			float vertice_j_y = vertices.get(j).y + y;
		    if (((vertice_i_y > point.y) != (vertice_j_y > point.y)) &&
			     (point.x < ((vertice_j_x-vertice_i_x) * (point.y-vertice_i_y) / (vertice_j_y-vertice_i_y) + vertice_i_x))) 
		    	contains = !contains;
		  }
		  
		  return contains;
	}
	
	/**
	 * Centrer la position x et y au milieu de l'unité selon la largeur et la hauteur des sommets.
	 */
	@Override
	public void centerFocus(){
		
		float min_x = Float.MAX_VALUE;
		float max_x = -Float.MAX_VALUE;
		
		float min_y = Float.MAX_VALUE;
		float max_y = -Float.MAX_VALUE;
		
		int count = vertices.size();
		for(int i = 0; i < count; i++){
			Vertex vertex = vertices.get(i);
			if(vertex.x < min_x)
				min_x = vertex.x;
			
			if(vertex.y < min_y)
				min_y = vertex.y;
			
			if(vertex.x > max_x)
				max_x = vertex.x;
			
			if(vertex.y > max_y)
				max_y = vertex.y;
		}
		
		float translate_x = 0;
		float translate_y = 0;
		
		if(min_x != Float.MAX_VALUE && max_x != -Float.MAX_VALUE){
			translate_x = (min_x + max_x)/2;
			width = max_x - min_x;
		}
		
		if(min_y != Float.MAX_VALUE && max_y != -Float.MAX_VALUE){
			translate_y = (min_y + max_y)/2;
			height = max_y - min_y;
		}
		
		for(int i = 0; i < count; i++){
			Vertex vertex = vertices.get(i);
			vertex.x -= translate_x;
			vertex.y -= translate_y;
		}
		
		x += translate_x;
		y += translate_y;
	}
	
	@Override
	public void rotate(float degrees, float x, float y){
		
		super.rotate(degrees, x, y);
		int count = vertices.size();
		for(int i = 0; i < count; i++){
			Vertex vertex = vertices.get(i);
			PointF p = GameMath.rotate(degrees, vertex.x, vertex.y);
			vertex.x = p.x; 
			vertex.y = p.y;
		}
		/*
		count = shapes.size();
		for(int i = 0; i < count; i++){
			if(!(shapes.get(i) instanceof Edge)){
				shapes.get(i).rotate(degrees, x, y);
			}
		}
		*/
	} 
	
	protected float getTop(){
		
		int count = vertices.size();
		if(count == 0)
			return 0;
		
		float top = vertices.get(0).y;
		for(int i = 1; i < count; i++)
			if(vertices.get(i).y < top)
				top = vertices.get(i).y;
		
		return top;
	}
	
	protected float getBottom(){
		
		int count = vertices.size();
		if(count == 0)
			return 0;
		
		float bottom = vertices.get(0).y;
		for(int i = 1; i < count; i++)
			if(vertices.get(i).y > bottom)
				bottom = vertices.get(i).y;
		
		return bottom;
	}
	
	protected float getLeft(){
		
		int count = vertices.size();
		if(count == 0)
			return 0;
		
		float left = vertices.get(0).x;
		for(int i = 1; i < count; i++)
			if(vertices.get(i).x < left)
				left = vertices.get(i).x;
		
		return left;
	}
	
	protected float getRight(){
		
		int count = vertices.size();
		if(count == 0)
			return 0;
		
		float right = vertices.get(0).x;
		for(int i = 1; i < count; i++)
			if(vertices.get(i).x > right)
				right = vertices.get(i).x;
		
		return right;
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		
		//Remplir les sommets à partir de la cache
		vertices.addAll(cache.values());
		
		//Recréer les mappings
		int count = shapes.size();
		for(int i = 0; i < count; i++)
			if(shapes.get(i) instanceof Edge){
				Edge edge = (Edge)shapes.get(i);
				EdgeVertex start = this.cache.get(edge.start_id);
				start.edges.add(edge);
				edge.start = start;
				
				EdgeVertex end = this.cache.get(edge.end_id);
				end.edges.add(edge);
				edge.end = end;
			}
	}
}
