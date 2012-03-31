package com.tournesol.game.edge;

import java.io.IOException;
import java.io.ObjectInputStream;

import com.tournesol.game.shape.ShapeLine;
import com.tournesol.game.unit.Unit;

/**
 * Comme une ligne, sauf que l'on doit absolument lui associer des Vertex après la création.
 * @author Heliante
 *
 */
public class Edge extends ShapeLine {

	private static final long serialVersionUID = -3933132741959356786L;

	public int start_id;
	public int end_id;
	
	public Edge(Unit unit) { 
		super(unit);
	}
	
	@Override
	public void initPoints(){

	}
	
	@Override
	public void changeSizeRatio(float width, float height) {

	}
	
	private void writeObject(java.io.ObjectOutputStream stream) throws IOException{
		stream.write(start_id);
		stream.write(end_id);
	}
	
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		start_id = stream.read();
		end_id = stream.read();
	}

}
