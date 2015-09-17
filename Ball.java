package application;

import javafx.geometry.Point2D;
import javafx.scene.control.Control;

public class Ball extends Control {
	
	BallSkin skin;
	
	public Ball()
	{
		this.setSkin(new BallSkin(this));
		skin = (BallSkin)this.getSkin();
	}
	
	public Ball(double rad)
	{
		this.setSkin(new BallSkin(this, rad));
		skin = (BallSkin)this.getSkin();
	}
	
	public void setRadius(double rad)
	{
		skin.setRadius(rad);
	}
	
	public double getRadius()
	{
		return skin.getRadius();
	}
	
	public Point2D getPosition()
	{
		return new Point2D(getLayoutX(), getLayoutY());
	}
}
