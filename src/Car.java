import java.awt.Color;
import java.awt.Graphics;

//This class is for cars that are solid colored and do
//not display mass visually
public class Car extends Entity {
	
	public Car(){}
	
	public Car(double x, double y)
	{
		//Car direction
		direction = new Vector(0, Track.getGravity().getAngle());
		
		//Car color
		if((Math.random() * 10) > 5)
			color = Color.BLUE;
		else
			color = Color.RED;
		
		//Car attributes
		mass = 10;
		velocity = 10;
		momentum = 0;
		accel = 1.5;
		//Standard Elasticity is .7
		elasticity = 1;
		xcor = x;
		ycor = y;
	}
	
	@Override
	public void drawEntity(Graphics g)
	{
		//g.setColor(Color.WHITE);
		//g.drawRect((int)xcor, (int)ycor, 30, 80);
		g.setColor(color);
		g.fillRect((int)xcor, (int)ycor, 30, 80);
	}
	
}
