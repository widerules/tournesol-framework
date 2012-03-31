package com.tournesol.game.listener;

import java.io.Serializable;

import com.tournesol.game.World;

public interface IWorldOrientationListener extends Serializable{

	void orientationChanged(World world, int new_orientation, int old_orientation);
}
