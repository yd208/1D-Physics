import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

//This class is for cars that are solid colored and do
//not display mass visually
public class Car extends Entity {
	
	public Car(){}
	
	public Car(double x, double y, int id)
	{
		//Car direction
		direction = new Vector(0, Track.getGravity().getAngle());

		//Car color
		if((Math.random() * 10) > 5)
			color = Color.BLUE;
		else
			color = Color.RED;
		
		this.id = id;
		//Car attributes
		mass = 10;
		velocity = 10;
		momentum = 0;
		accel = 1.5;
		//Standard Elasticity is .7
		elasticity = .7;
		xcor = x;
		ycor = y;
		rect = new Rectangle(30, 80);
	}
	
	@Override
	public void drawEntity(Graphics g)
	{
		//g.setColor(Color.WHITE);
		//g.drawRect((int)xcor, (int)ycor, 30, 80);
		g.setColor(color);
		g.fillRect((int)xcor, (int)ycor, (int)rect.getWidth(), (int)rect.getHeight());
		rect.setLocation((int)xcor, (int)ycor);
	}
	
	@Override
	public void drawCursor(Graphics g, double x, double y)
	{
		g.setColor(Color.WHITE);
		g.drawLine((int)(xcor + 15), 100, (int)x, (int)y - 30);
		System.out.println("xcor: " +(int)(xcor + 15) + ", ycor: " + 140 + ", x: " + (int)x + ", y: " + (int)y);
	}
	
}
