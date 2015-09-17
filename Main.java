package application;
	
import java.util.ArrayList;
import java.util.List;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Main extends Application {
	
	private static final int WINDOW_WIDTH = 400;
	private static final int WINDOW_HEIGHT = 400;
	private static final int RIGHT = 1;
	private static final int LEFT = 2;
	private static final int DOWN = 4;
	private static final double BOUNCE = -4;
	private static final double AIR_RESISTANCE = 0.001;
	private static final double gravity = 9.8f;
	private static final int speed = 1;
	private static double initSpeed;
	
	private static List<Platform> platforms = new ArrayList<Platform>();
	private static Ball ball;
	private static Pane master;
	private static Timeline t;
	private static int gameState;
	private static double xyInertialVelocity;
	private static int score;
	
	@Override
	public void start(Stage primaryStage) {
		
		BorderPane root = new BorderPane();
		Scene scene = new Scene(root,400,400);
		
		setup(root);
		
		scene.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>(){
			@Override
			public void handle(KeyEvent e)
			{	
				List<Integer> collision = checkCollision();
				double amt = 2;
				
				if(e.isControlDown() && e.getCode() == KeyCode.R)
				{
					for(Node n : master.getChildren())
						n.setVisible(false);
					master.getChildren().clear();
					platforms.clear();
					setup(root);
					if(t.getStatus() == Animation.Status.STOPPED)
						t.play();
				}
				if(e.getCode() == KeyCode.RIGHT && collision.get(1) != RIGHT)
				{
					if(collision.get(1) == 0)
					{
						amt = 1;
						xyInertialVelocity += 0.2;
					}
					setNodePosition(ball, ball.getPosition().getX() + amt, ball.getPosition().getY());
				}
				if(e.getCode() == KeyCode.UP && gameState == 0)
					setNodePosition(ball, ball.getPosition().getX(), ball.getPosition().getY() - 5);
				if(e.getCode() == KeyCode.LEFT && collision.get(1) != LEFT)
				{
					if(collision.get(1) == 0)
					{
						amt = 1;
						xyInertialVelocity += -0.2;
					}
					setNodePosition(ball, ball.getPosition().getX() - amt, ball.getPosition().getY());
				}
			}
		});
		
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
		
		final Duration oneFrameAmt = Duration.millis(1000/60);
		final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
		   new EventHandler<ActionEvent>() {
		 
		   @Override
		   public void handle(ActionEvent event) 
		   {
			   updateBall();
			   updateScreen();
		   }
		}); // oneFrame
		 
		// sets the game world's game loop (Timeline)
		t = TimelineBuilder.create()
		   .cycleCount(Animation.INDEFINITE)
		   .keyFrames(oneFrame)
		   .build();
		t.play();
		
	}
	
	private static void updateBall()
	{
		double xPos = ball.getPosition().getX();
		double yPos = ball.getPosition().getY();
		boolean didFall = checkIfFall();
		List<Integer> collision = checkCollision();
		
		if(!didFall)
		{
			if(collision.get(1) == DOWN)
				initSpeed = BOUNCE;
			setNodePosition(ball, xPos + xyInertialVelocity, yPos + initSpeed);
			initSpeed += 0.01 * gravity;
			
			double inertialXYSpeedFactor = 0.005 * xyInertialVelocity;
			if(xyInertialVelocity <= -AIR_RESISTANCE + inertialXYSpeedFactor)
				xyInertialVelocity += AIR_RESISTANCE - inertialXYSpeedFactor;
			else if(xyInertialVelocity >= AIR_RESISTANCE + inertialXYSpeedFactor)
				xyInertialVelocity -= (AIR_RESISTANCE + inertialXYSpeedFactor);
		}
		else if(collision.get(0) != 1)
			t.stop();
		else
		{
			initSpeed = speed;
			xyInertialVelocity = 0;
		}
		
		if(xPos > WINDOW_WIDTH)
			setNodePosition(ball, 0, yPos);
		else if(xPos < 0)
			setNodePosition(ball, WINDOW_WIDTH, yPos);
	}
	
	private static void updateScreen()
	{
		double yPos = ball.getLayoutY();
		if(yPos <= WINDOW_HEIGHT / 3)
		{
			for(Platform platform : platforms)
			{
				double pPos = platform.getLayoutY();
				platform.setLayoutY(pPos + 0.5);
				if(platform.getLayoutY() >= WINDOW_HEIGHT)
				{
					int randX = (int)(Math.random() * (WINDOW_WIDTH-Platform.WIDTH));
					setNodePosition(platform, randX, 0);
					score += 5;
				}
			}
		}
	}
	
	private static boolean checkIfFall()
	{
		double yPos = ball.getPosition().getY() + ball.getRadius() * 2;
		if(yPos <= 400)
			return false;
		gameState = 0;
		System.out.println(score);
		return true;
	}
	
	private static ArrayList<Integer> checkCollision()
	{	
		ArrayList<Integer> ret = new ArrayList<Integer>();
		ret.add(0);
		ret.add(0);
		for(Platform platform : platforms)
		{
			if(ball.getBoundsInParent().intersects(platform.getBoundsInParent()))
			{
				ret.set(0, 1);
				ret.set(1, getCollisionDir(platform));
				return ret;
			}
		}
		return ret;
	}
	
	private static int getCollisionDir(Node n)
	{
		if(ball.getBoundsInParent().getMaxX() - 1 <= n.getBoundsInParent().getMinX())
			return RIGHT;
		if(ball.getBoundsInParent().getMinX() >= n.getBoundsInParent().getMaxX())
			return LEFT;
		return DOWN;
	}
	
	private static void setup(Pane p)
	{
		master = p;
		master.getChildren().clear();
		platforms.clear();
		
		gameState = 1;
		score = 0;
		
		ball = new Ball(10);
		setNodePosition(ball, 200, 375);
		
		initSpeed = speed;
		xyInertialVelocity = 0;
		
		platforms.add(new Platform(200, 390));
		
		createPlatforms();
		draw();
	}
	
	private static void draw()
	{
		master.getChildren().add(ball);
		int doesInt = 0;
		if(checkCollision().get(0) != 0)
			doesInt++;
		if(doesInt == 0)
			master.getChildren().addAll(platforms);
		else
			setup(master);
	}
	
	private static void createPlatforms()
	{
		platforms.add(new Platform(60, 100));
		platforms.add(new Platform(200, 200));
		platforms.add(new Platform(150, 300));
		platforms.add(new Platform(130, 270));
		platforms.add(new Platform(250, 130));
		platforms.add(new Platform(220, 80));
		platforms.add(new Platform(340, 220));
		platforms.add(new Platform(30, 40));
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
    private static void setNodePosition(Node b, double x, double y)
    {
    	b.setLayoutX(x);
    	b.setLayoutY(y);
    }
}
