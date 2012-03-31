package com.tournesol.game.shape;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

import com.tournesol.game.edge.Edge;

import android.graphics.PointF;

/**
 * Sommet relatif à l'unité
 * @author Heliante
 */
public class Vertex extends PointF implements Serializable {

	private static final long serialVersionUID = 7876627428174638199L;

	protected void writeVertex(java.io.ObjectOutputStream stream) throws IOException{
		stream.writeFloat(x);
		stream.writeFloat(y);
	}
	
	private void writeObject(java.io.ObjectOutputStream stream) throws IOException{
	
		stream.defaultWriteObject();
		writeVertex(stream);
	}
	
	protected void readVertex(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		 x = stream.readFloat();
		 y = stream.readFloat();
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		 stream.defaultReadObject();
		 readVertex(stream);
	}
}
