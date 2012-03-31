package com.tournesol.game.listener;

import com.tournesol.game.listener.IUnitTickListener;
import com.tournesol.game.unit.Unit;

public class WidthStrokeUnitTickListener implements IUnitTickListener{ 

	private static final long serialVersionUID = -5162434222382707429L;
	
	public float stroke_width_min = 0.5f;
	public float stroke_width_max = 3;
	
	private float current_stroke_width = 0.15f;	
		
	@Override
	public void tick(Unit unit) {
		unit.stroke_width += current_stroke_width;
		
		if(unit.stroke_width > stroke_width_max || unit.stroke_width < stroke_width_min)
			current_stroke_width *= -1;

	}
}
