import java.awt.Color;
import java.awt.Graphics;

//This class allows for cars that visually display the mass of lighter cars
//by creating holes in the center of the car
public class HollowCar extends Entity {

	public HollowCar(){}
	
	public HollowCar(double x, double y)
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
		velocity = 5;
		momentum = 0;
		accel = 1.5;
		xcor = x;
		ycor = y;
	}
	
	@Override
	public void drawEntity(Graphics g) {
		
	}
	
	@Override
	public void drawCursor(Graphics g, double x, double y)
	{
		
	}

}
