package com.tournesol.game.unit.button;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;

import com.tournesol.drawing.Drawing;
import com.tournesol.drawing.DrawingCheck;
import com.tournesol.game.Game;
import com.tournesol.game.listener.ShowHideUnitTickListener;
import com.tournesol.game.unit.Rectangle;
import com.tournesol.game.unit.Unit;
import com.tournesol.game.utility.RecycleBin;

public class CheckBox extends Rectangle{

	private static final long serialVersionUID = 3206753424645383129L;

	public boolean check = false;
	public Unit checkUnit;
	
	public CheckBox(){
		this.doesCollide = false;
		this.shouldCollide = false;
		this.followFocus = false;
		this.scaling = false;
		this.gravity_damper = 0;
		this.manage_rotation = false;
		
		checkUnit = new Unit();
		DrawingCheck drawingCheck = new DrawingCheck();
		checkUnit.drawings.add(drawingCheck);
		
	}
	
	@Override
	public void init(float x, float y, float width, float height){
		super.init(x, y, width, height);
		checkUnit.init(x, y, width * 1.5f, height * 1.5f);
		checkUnit.drawings.get(0).init(width * 1.5f, height * 1.5f);
		checkUnit.drawings.get(0).style = Style.FILL;
	}
	
	@Override 
	public void setGame(Game game){
		super.setGame(game);
		checkUnit.tick_listeners.add(new ShowHideUnitTickListener(checkUnit, game));
	}
	
	@Override
	public void tick(){
		super.tick();
		
		//Constament mettre à jour la position original du check
		((ShowHideUnitTickListener)checkUnit.tick_listeners.get(0)).original_x = x;
		((ShowHideUnitTickListener)checkUnit.tick_listeners.get(0)).original_y = y + height * 0.25f;
		
		checkUnit.tick();
		if(check)
			((ShowHideUnitTickListener)checkUnit.tick_listeners.get(0)).show(checkUnit);
		else if(!((ShowHideUnitTickListener)checkUnit.tick_listeners.get(0)).hide)
			((ShowHideUnitTickListener)checkUnit.tick_listeners.get(0)).hideSmoothly(checkUnit);
	}
	
	
	@Override
	public void drawConstruct(Canvas c){
		
		PointF focus = getFocusPosition();
		Paint paint = getPaint();
		
		RecycleBin.drawRectF.left = focus.x - width / 2;
		RecycleBin.drawRectF.top = focus.y - height / 2;
		RecycleBin.drawRectF.right = focus.x + width / 2;
		RecycleBin.drawRectF.bottom = focus.y + height / 2;
		c.drawRoundRect(RecycleBin.drawRectF, 10, 10, paint);
	
		checkUnit.draw(c, 0, 0, checkUnit.degrees, checkUnit.x, checkUnit.y);
	}
	
	@Override
	public void copy(Drawing drawing){
		super.copy(drawing);
		checkUnit.copy(drawing);
		checkUnit.drawings.get(0).copy(drawing);
		
		if(!(drawing instanceof CheckBox))
			return;
		
		CheckBox copy = (CheckBox)drawing;
		checkUnit.copy(copy.checkUnit);
		checkUnit.drawings.get(0).copy(copy.checkUnit.drawings.get(0));
	}
	
}
