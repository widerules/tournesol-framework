package com.tournesol.game.utility;

import java.util.ArrayList;
import java.util.HashSet;

import com.tournesol.game.shape.CollisionUnit;
import com.tournesol.game.unit.Unit;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Classe permettant de réutiliser des objets au lieu d'en générer des nouveaux.
 */
public class RecycleBin {

	//Draw
	public final static RectF drawRectF = new RectF();
	public final static Rect drawRect = new Rect();
	public final static Path drawPath = new Path();
	
	//World
	public final static PointF gravityPointF = new PointF();
	public final static ArrayList<Unit> nearestUnit= new ArrayList<Unit>();
	public final static ArrayList<int[]> cells = new ArrayList<int[]>();
	public final static ArrayList<int[]> shapeCells = new ArrayList<int[]>();

	//Math
	public final static PointF mathDirectionPointF = new PointF();
	public final static PointF mathRotatePointF = new PointF();
	
	//Game
	public final static PointF gameScalePointF = new PointF();
	public final static PointF gameNoScalePointF = new PointF();
	public final static ArrayList<Unit> removedUnits = new ArrayList<Unit>();
	public final static ArrayList<Unit> touchedUnits = new ArrayList<Unit>();
	public final static ArrayList<PointF> collisionPointFs = new ArrayList<PointF>();
	
	//GameView
	public final static PointF transformPointF = new PointF();
	
	//Unit
	public final static PointF focusPointF = new PointF();
	public final static RectF collideRangeRectF = new RectF();
	
	//Shape
	public final static RectF absoluteRectF = new RectF();
	public final static RectF absoluteRectF1 = new RectF();
	public final static RectF absoluteRectF2 = new RectF();
	public final static PointF absolutePointF = new PointF();
	public final static PointF collisionPointF = new PointF();
	public final static CollisionUnit collisionUnit1 = new CollisionUnit();
	public final static CollisionUnit collisionUnit2 = new CollisionUnit();
}
