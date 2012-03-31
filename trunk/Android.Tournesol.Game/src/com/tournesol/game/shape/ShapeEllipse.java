package com.tournesol.game.shape;

import com.tournesol.game.unit.Unit;

public class ShapeEllipse extends ShapeArc{

	private static final long serialVersionUID = 1479237718747881337L;

	public ShapeEllipse(Unit unit) {
		super(unit);
		startAngle = 0;
		sweepAngle = 360;
	}
	


}
