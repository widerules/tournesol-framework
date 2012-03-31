package com.tournesol.game.shape;

import java.io.Serializable;

import com.tournesol.game.unit.Unit;

public class ShapeCircle extends ShapeEllipse implements Serializable {

	private static final long serialVersionUID = 313351269015517412L;
	
	public ShapeCircle(Unit unit) {
		super(unit);
	}
}
