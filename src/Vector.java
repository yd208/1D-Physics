public class Vector {

	private double magnitude;
	private double angle;
	
	public Vector(double m, double a)
	{
		magnitude = m;
		angle = a;
	}
	
	public Vector(Vector v1, Vector v2)
	{
		//Angle is either 0 or PI 
		System.out.println("--------------------New Vector--------------------------");
		System.out.println("angle 1: " + Math.cos(v1.getAngle()) + ", angle 2: " + Math.cos(v2.getAngle()));
		magnitude = Math.cos(v1.angle) * v1.magnitude + Math.cos(v2.angle) * v2.magnitude;
		if(magnitude >= 0)
			angle = 0;
		else
			angle = Math.PI;
	}
	
	public double getMagnitude()
	{
		return magnitude;
	}
	
	public double getAngle()
	{
		return angle;
	}
	
	public void setMagnitude(double m)
	{
		magnitude = m;
	}
	
	public void setAngle(double a)
	{
		angle = a;
	}
	
}
