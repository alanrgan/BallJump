package application;

import javafx.geometry.Point2D;
import javafx.scene.control.Control;

public class Platform extends Control {
	public static final int WIDTH = 40;
	public static final int HEIGHT = 5;
	private int pos_x;
	private int pos_y;
	
	PlatformSkin skin;

	public Platform(int pos_x, int pos_y)
	{
		this.setSkin(new PlatformSkin(this));
		skin = (PlatformSkin)this.getSkin();
		setPosition(pos_x, pos_y);
	}
	
	public Point2D getPosition()
	{
		return new Point2D(pos_x, pos_y);
	}
	
	private void setPosition(int x, int y)
	{
		pos_x = x;
		pos_y = y;
		setLayoutX(x);
		setLayoutY(y);
	}
}
