package com.tournesol.game.listener;

import java.io.Serializable;

import android.graphics.PointF;

import com.tournesol.game.unit.Unit;

public interface ICollisionListener extends Serializable {
	public void collision(Unit unit1, Unit unit2, PointF collision);
}
