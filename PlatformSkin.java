package application;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.shape.Rectangle;

public class PlatformSkin implements Skin<Platform> {

	private static final double defWidth = 50;
	private static final double defHeight = 4;
	Group rootNode = new Group();
	Platform control;
	Rectangle rect;
	
	public PlatformSkin(Platform control, double w, double h)
	{
		this.control = control;
		rect = new Rectangle(w, h);
		draw();
	}
	
	public PlatformSkin(Platform control)
	{
		this(control, defWidth, defHeight);
	}
	
	public void draw()
	{
		rootNode.getChildren().add(rect);
	}
	
	@Override
	public void dispose() {
		rootNode = null;
		rect = null;
		control = null;
	}

	@Override
	public Node getNode() {
		return rootNode;
	}

	@Override
	public Platform getSkinnable() {
		// TODO Auto-generated method stub
		return null;
	}

}

