package com.tournesol.game.listener;

import java.io.Serializable;

import com.tournesol.game.World;

public interface IWorldFocusListener extends Serializable{
	public void focusChanged(World world, float x, float y);
}
