package com.tournesol.game.unit.button;

import java.io.Serializable;

public interface ILeverListener extends Serializable {
	
	float getValuePct();
	void move(float distance_pct, float direction_x, float direction_y);
}
