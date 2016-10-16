import java.awt.Color;
import java.awt.Rectangle;

public abstract class Entity implements DrawableEntity{

	protected Rectangle rect;
	protected double xcor;
	protected double ycor;
	protected Color color;
	protected double mass;
	protected double velocity;
	protected double momentum;
	protected double accel;
	protected double elasticity;
	protected Vector direction;
	protected int id;
	
	//Getters for Car properties
	public int getID(){return id;}
	public Color getColor(){return color;}
	public Rectangle getShape(){return rect;}
	public double getXcor(){return xcor;}
	public double getMass(){return mass;}
	public double getVelocity(){return velocity;}
	public double getMomentum(){return momentum;}
	public double getAcceleration(){return accel;}
	public double getElasticity(){return elasticity;}
	public Vector getDirection(){return direction;}
		
	//Setters for Car properties
	public void setID(int i){id = i;}
	public void setColor(Color c){color = c;}
	public void setXcor(double x){xcor = x;}
	public void setMass(double m){mass = m;}
	public void setVelocity(double v){velocity = v;}
	public void setMomentum(double m){momentum = m;}
	public void setAcceleration(double a){accel = a;}
	public void setElasticity(double e){elasticity = e;}
	public void setDirection(Vector v){direction = v;}
	
}
