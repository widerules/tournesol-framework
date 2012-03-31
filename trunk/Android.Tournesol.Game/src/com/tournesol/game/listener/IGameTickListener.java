package com.tournesol.game.listener;

import java.io.Serializable;

import com.tournesol.game.Game;

public interface IGameTickListener extends Serializable{
	public void tick(Game game);
}
