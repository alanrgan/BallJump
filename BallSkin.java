package application;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.shape.Circle;

public class BallSkin implements Skin<Ball> {

	Ball control;
	Group rootNode = new Group();
	Circle circle = new Circle();
	static double defaultRadius = 5.0f;
	
	public BallSkin(Ball control, double rad)
	{
		this.control = control;
		circle.setRadius(rad);
		draw();
	}
	
	public BallSkin(Ball control)
	{
		this(control, defaultRadius);
	}
	
	public void draw()
	{
		rootNode.getChildren().setAll(circle);
	}
	
	public void setRadius(double rad)
	{
		circle.setRadius(rad);
		draw();
	}
	
	public double getRadius()
	{
		return circle.getRadius();
	}
	
	public Circle getCircle()
	{
		return circle;
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Node getNode() {
		return rootNode;
	}

	@Override
	public Ball getSkinnable() {
		// TODO Auto-generated method stub
		return null;
	}

}
