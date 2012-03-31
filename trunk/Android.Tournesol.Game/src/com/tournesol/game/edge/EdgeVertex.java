package com.tournesol.game.edge;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import com.tournesol.game.shape.Vertex;

public class EdgeVertex extends Vertex{

	private static final long serialVersionUID = 815342787263527020L;

	public int id;
	public transient ArrayList<Edge> edges = new ArrayList<Edge>();
	
	private void writeObject(java.io.ObjectOutputStream stream) throws IOException{
		stream.defaultWriteObject();
		this.writeVertex(stream);
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		this.readVertex(stream);
		edges = new ArrayList<Edge>();
	}
}
