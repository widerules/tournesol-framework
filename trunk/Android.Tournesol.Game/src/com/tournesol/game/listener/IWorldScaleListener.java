package com.tournesol.game.listener;

import com.tournesol.game.World;

public interface IWorldScaleListener {
	void scaleChanged(World world, float new_scale, float old_scale);
}
