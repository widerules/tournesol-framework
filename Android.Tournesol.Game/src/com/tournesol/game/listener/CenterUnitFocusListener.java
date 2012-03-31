package com.tournesol.game.listener;

import java.io.IOException;
import java.io.ObjectInputStream;

import com.tournesol.game.Game;
import com.tournesol.game.World;
import com.tournesol.game.listener.IWorldFocusListener;
import com.tournesol.game.unit.Unit;

/**
 * Événement pour centrer une unité au centre du focus lors d'un changement de focus.
 * @author Heliante
 */
public class CenterUnitFocusListener implements IWorldFocusListener{

	private static final long serialVersionUID = 8921232034801936863L;
	
	//ID auquel on doit centrer au milieu du focus
	public long unit_id;
	
	//Unité chargé à partir de l'ID
	private transient Unit unit;

	@Override
	public void focusChanged(World world, float x, float y) {
		
		if(unit == null)
			unit = world.game.getUnit(unit_id);
		
		unit.x = x;
		unit.y = y;
	}
}
