package com.tournesol.game.listener;

import java.io.Serializable;

import com.tournesol.game.unit.Unit;
import com.tournesol.motion.TouchEvent;

public interface IUnitTouchListener extends Serializable{
	public void touch(TouchEvent e, Unit unit);
}
