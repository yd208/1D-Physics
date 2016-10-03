import java.awt.Color;
import java.awt.Graphics;

public class Car {

	private double xcor;
	private double ycor;
	private Color color;
	private double mass;
	private double velocity;
	private double momentum;
	private double accel;
	private Vector direction = new Vector(0, Track.getGravity().getAngle());
	
	public Car(){}
	
	public Car(double x, double y)
	{
		if((Math.random() * 10) > 5)
			color = Color.BLUE;
		else
			color = Color.RED;
		mass = 10;
		velocity = 5;
		momentum = 0;
		accel = 1.5;
		xcor = x;
		ycor = y;
	}
	
	public void drawCar(Graphics g)
	{
		//g.setColor(Color.WHITE);
		//g.drawRect((int)xcor, (int)ycor, 30, 80);
		g.setColor(color);
		g.fillRect((int)xcor, (int)ycor, 30, 80);
	}
	
	//Getters for Car properties
	public Color getColor(){return color;}
	public double getXcor(){return xcor;}
	public double getMass(){return mass;}
	public double getVelocity(){return velocity;}
	public double getMomentum(){return momentum;}
	public double getAcceleration(){return accel;}
	public Vector getDirection(){return direction;}
	
	//Setters for Car properties
	public void setColor(Color c){color = c;}
	public void setXcor(double x){xcor = x;}
	public void setMass(double m){mass = m;}
	public void setVelocity(double v){velocity = v;}
	public void setMomentum(double m){momentum = m;}
	public void setAcceleration(double a){accel = a;}
	public void setDirection(Vector v){direction = v;}
	
}
