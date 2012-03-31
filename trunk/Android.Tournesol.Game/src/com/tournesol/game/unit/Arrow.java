package com.tournesol.game.unit;

import com.tournesol.game.edge.PolygoneUnit;

public class Arrow extends PolygoneUnit {

	private static final long serialVersionUID = 659130505013038966L;

	public Arrow(){
		
		this.addPolygone(true, new float[]{
				width * -0.5f, height * 0.0f,
				width * 0.0f, height * -0.5f,
				width * 0.5f, height * 0.0f,
				width * 0.3f, height * 0.0f,
				width * 0.3f, height * 0.5f,
				width * -0.3f, height * 0.5f,
				width * -0.3f, height * 0.0f,
		});
	}
}
