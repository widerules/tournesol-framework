package com.tournesol.game.listener;

import java.io.Serializable;

import com.tournesol.game.unit.Unit;

public interface IUnitTickListener extends Serializable {
	public void tick(Unit unit);

}
