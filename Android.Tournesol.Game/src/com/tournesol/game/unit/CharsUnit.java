package com.tournesol.game.unit;

import com.tournesol.drawing.Drawing;
import com.tournesol.drawing.DrawingChars;

public class CharsUnit extends Unit{
	
	private static final long serialVersionUID = 7869230328153732993L;
	
	public final DrawingChars drawingChars = new DrawingChars();
	
	public CharsUnit(){
		this.drawings.add(drawingChars);
	}
	
	@Override
	public void copy(Drawing drawing){
		super.copy(drawing);
		if(!(drawing instanceof CharsUnit))
			return;
		
		CharsUnit copy = (CharsUnit)drawing;
		this.drawingChars.copy(copy.drawingChars);
	}
}
