import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Track extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private Thread thread;
	private Dimension size;
	private EventHandler myHandler;
	private Entity selectedObject = null;
	//Standard Gravity is 1.05
	private static Vector gravity = new Vector(0, 0);
	//Standard Friction is .96
	private double friction;
	private double mouseX;
	private double mouseY;
	private boolean swapped = false;
	private ArrayList<Entity> cars = new ArrayList<Entity>();
	
	public Track()
	{
		size = new Dimension(800, 200);
		DrawingSurface myCanvas = new DrawingSurface();

		add(createGUIControls());
		myCanvas.addMouseListener(myHandler);
		myCanvas.addMouseMotionListener(myHandler);
		setTitle("Track");
		setPreferredSize(size);
		setResizable(false);
		setBackground(Color.BLACK);
		add(myCanvas);
		pack();
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		friction = 1;
		loadCars(3);
	}
	
	public JPanel createGUIControls()
	{
		myHandler = new EventHandler();
		
		JPanel panel = new JPanel();
		panel.setBounds(new Rectangle((int) size.getWidth(), 35));
		panel.setSize(new Dimension((int) size.getWidth(), 35));
		
		JButton button  = new JButton("Vel = 0");
	    button.setSize(new Dimension(30, 18));
	    button.setMaximumSize(getSize());
		button.setSize(new Dimension(30, 20));
		button.addActionListener(myHandler);
		button.setActionCommand("Vel = 0");
		panel.add(button);
		
		JSlider grav_slider = new JSlider(JSlider.HORIZONTAL, -2, 2, 0);
		grav_slider.setPaintTicks(true);
		grav_slider.setMajorTickSpacing(1);
		grav_slider.addChangeListener(myHandler);
		panel.add(grav_slider);
		
		return panel;
	}
	
	public void loadCars(int num_cars)
	{
		for(int i = 0; i < num_cars; i++)
		{
			addCar((i + 5) * 40, 60, i);
			if(i % 2 == 0)
			{
				cars.get(i).setColor(Color.RED);
				cars.get(i).setDirection(new Vector(0, 0));
				cars.get(i).setVelocity(5);
			}
			else
			{
				cars.get(i).setColor(Color.BLUE);
				cars.get(i).setDirection(new Vector(0, Math.PI));
			}
			//cars.get(i).setVelocity(Math.random() * 10);
		}	
	}
	
	//**************************************************Event Listener*******************************************************
	
	private class EventHandler extends MouseAdapter implements ActionListener, ChangeListener, MouseMotionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getActionCommand() == "Vel = 0")
			{
				for(Entity c: cars)
				{
					c.setVelocity(0);
				}
				gravity = new Vector(0, 0);
			}
		}
		
		@Override
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider)e.getSource();
		     if (source.getValueIsAdjusting()) {
		        if(source.getValue() == -1)
		        {
		        	gravity = new Vector(1.05, Math.PI);
		        }
		        else if(source.getValue() == 1)
		        {
		        	gravity = new Vector(1.05, 0);
		        }
		        else if(source.getValue() == -2)
		        {
		        	gravity = new Vector(1.1, Math.PI);
		        }
		        else if(source.getValue() == 2)
		        {
		        	gravity = new Vector(1.1, 0);
		        }
		        else
		        {
		        	gravity = new Vector(0, 0);
		        }
		        
		      //  System.out.println("Mag: " + gravity.getMagnitude() + ", Angle: " + gravity.getAngle());
		     }
			
		}
		
		@Override
	    public void mousePressed(MouseEvent e)
	    {
	    	System.out.println("position: " + e.getX());
	    	selectedObject = findObjects(e.getX());
	    	if(selectedObject != null)
	    	{
	    		mouseX = e.getX();
	    		mouseY = e.getY();
	    	}
	    	e.consume();
	    }
	    
	    @Override
	    public void mouseDragged(MouseEvent e)
	    {
	    	if(selectedObject != null)
	    	{
	    		mouseX = e.getX();
	    		mouseY = e.getY();
	    		
	    		double angle = (mouseX > selectedObject.getXcor()) ? 0 : Math.PI;
	    		double dx = angle == 0 ? mouseX - selectedObject.getXcor() : selectedObject.getXcor() - mouseX;
	    		Vector force = new Vector(dx * .5, angle);
	    		selectedObject.setDirection(new Vector(force, selectedObject.getDirection()));
	    	}
	    	e.consume();
	    }
	    
	    @Override
	    public void mouseReleased(MouseEvent e)
	    {
	    	selectedObject = null;
	    	e.consume();
	    }
	    
	    public Entity findObjects(double x)
	    {
	    	System.out.println("Here");
	    	for(Entity c: cars)
	    	{
	    		if(x >= c.getXcor() && x <= c.getXcor() + 30)
	    		{
	    			return c;
	    		}
	    	}
			return null;
	    }
	}
	
	//**************************************************Entity Interactions and Physics***************************************
	
	//Calculates the velocity and direction of the cars
	public void calcVelocity(Entity car)
	{
		Vector final_vel = new Vector(0, 0);
		Vector avg_vel = new Vector(0, 0);
		Vector direction = car.getDirection();
		
		Vector car_forces = new Vector(new Vector(car.getMass(), gravity.getAngle()), gravity);
		        
		//Calculate car acceleration
		car.setAcceleration(car_forces.getMagnitude() / car.getMass());
		        
		//Calculate car final velocity
		if(direction.getAngle() > 0 && gravity.getAngle() == 0)
		{
			final_vel = new Vector((car.getVelocity() * (1/car.getAcceleration())) * friction, direction.getAngle());
		}
		else if(direction.getAngle() > 0 && gravity.getAngle() > 0)
		{
			final_vel = new Vector((car.getVelocity() * car.getAcceleration()) * friction, direction.getAngle());
		}
		else if(direction.getAngle() == 0 && gravity.getAngle() == 0)
		{
			final_vel = new Vector((car.getVelocity() * car.getAcceleration()) * friction, direction.getAngle());
		}
		else
		{
			final_vel = new Vector((car.getVelocity() * (1/car.getAcceleration())) * friction, direction.getAngle());
		}
	
		//Calculate average velocity
		avg_vel = new Vector((car.getVelocity() + Math.abs(final_vel.getMagnitude()))/2.0, direction.getAngle());
		
		if(Math.abs(avg_vel.getMagnitude()) < .001 )
			avg_vel.setMagnitude(0);
		if(Math.abs(final_vel.getMagnitude()) < .001)
		    final_vel.setMagnitude(0);    
			
		if((car.getXcor()+ 36) > size.getWidth())
		{
			avg_vel.setMagnitude(avg_vel.getMagnitude() * car.getElasticity());
			avg_vel.setAngle(Math.PI);
			final_vel.setMagnitude(final_vel.getMagnitude() * car.getElasticity());
			final_vel.setAngle(Math.PI);
			car.setXcor(size.getWidth() - 36);
		}
		else if(car.getXcor() < 0)
		{
			avg_vel.setMagnitude(avg_vel.getMagnitude() * car.getElasticity());
			avg_vel.setAngle(0);
			final_vel.setMagnitude(final_vel.getMagnitude() * car.getElasticity());
			final_vel.setAngle(0);
			car.setXcor(0);
		}
	/*	System.out.println("final_vel mag: " + final_vel.getMagnitude() + ", final_vel angle: " + final_vel.getAngle());
		System.out.println("avg_vel mag: " + avg_vel.getMagnitude() + ", avg_vel angle: " + avg_vel.getAngle());
		System.out.println("Xcor: " + car.getXcor());*/
		
		//Calculate new car position using new velocity
		direction = new Vector(avg_vel, gravity);
		car.setDirection(direction);
		
		//Add new force to appropriate direction
		car.setXcor(car.getXcor() + direction.getMagnitude());
		
		//Set car velocity to final velocity
		car.setVelocity(Math.abs(direction.getMagnitude()));
		
		//System.out.println("Direction mag: " + direction.getMagnitude() + ", Direction angle: " + direction.getAngle() + ", Second Xcor: " + car.getXcor());
	}
	
	//Calculates collisions between cars
	public void calcCollisions(Entity car)
	{
		double angle1 = 0, angle2 = 0;
		double v1, v2, m1, m2;
		double v1_after, v2_after;
		double xcor, xcor2;
		double diff;
		
		for(Entity c: cars)
		{
			xcor = car.getXcor();
			xcor2 = c.getXcor();
			if(car.equals(c))
			{
				continue;
			}
			
			if(car.getID() > c.getID())
			{
				Entity temp = car;
				car = c;
				c = temp;
				swapped = true;
			}

			if(car.getXcor() + 30 > c.getXcor())
			{
				//System.out.println("X1: " + xcor + ", X2: " + xcor2);
				
				diff = (car.getXcor() + 15) - (c.getXcor() + 15);
				diff = Math.abs(diff);
				System.out.println("*************************New Collision******************************");
				System.out.println("X1: " + xcor + ", X1_C: " + (xcor + 15) + ", X2: " + xcor2 + ", X2_C: " + (xcor2 + 15) + ", diff: " + diff);
				if(diff < 30)
				{
					car.setXcor(car.getXcor() - (30 - diff));
					c.setXcor(c.getXcor() + (30 - diff));
					System.out.println("car_aft_x: " + car.getXcor() + ", c_aft_x: " + c.getXcor());
					if(car.getXcor() < 0)
					{
						car.setXcor(0);
						System.out.println("case 1: " + car.getXcor() + ", " + c.getXcor());
					}
					else if(c.getShape().getMaxX() + 36 > size.getWidth())
					{
						c.setXcor(size.getWidth() - 36);
						System.out.println("case 4: " + car.getXcor() + ", " + c.getXcor());
					}
				}
				
				v1 = car.getVelocity();
				v2 = c.getVelocity();
				if(car.getDirection().getAngle() != c.getDirection().getAngle())
				{
					if(car.getDirection().getAngle() == 0)
						v2 = -1 * v2;
					else
						v1 = -1 * v1;
				}
				
				m1 = car.getMass();
				m2 = c.getMass();
				v1_after = ((m1 - m2)/(m1 + m2)) * v1 + ((2 * m2)/(m1 + m2)) * v2;
				v2_after = ((2 * m1)/(m1 + m2)) * v1 + ((m2 - m1)/(m1 + m2)) * v2;
				v1_after = v1_after * .95;
				v2_after = v2_after * .95;
				
				if(v1_after < 0)
				{
					angle1 = car.getDirection().getAngle() == 0 ? Math.PI : 0;
					angle2 = (v2_after < 0) ? Math.PI : 0;
				}
				else if(v2_after < 0)
				{
					angle2 = c.getDirection().getAngle() == 0 ? Math.PI : 0;
					angle1 = (v1_after < 0) ? Math.PI : 0;
				}
				else
				{
					angle1 = car.getDirection().getAngle();
					angle2 = c.getDirection().getAngle();
				}
				
				System.out.println("v1_b: " + v1 + ", v2_b: " + v2 + ", ang1: " + angle1 + ", v1_a: " + v1_after + ", v2_a: " + v2_after + ", ang2: " + angle2);
				
				if(Math.abs(v1_after) < 1.26)
				{
					System.out.println("Vel = 0 case");
					v1_after = 0;
				}
				if(Math.abs(v2_after) < 1.26)
				{
					System.out.println("Vel = 0 case 2");
					v2_after = 0;
				}
				
				car.setVelocity(Math.abs(v1_after));
				c.setVelocity(Math.abs(v2_after));
		
				car.setDirection(new Vector(v1_after, angle1));
				calcVelocity(car);
				c.setDirection(new Vector(v2_after, angle2));
				calcVelocity(c);
			}
			if(swapped)
			{
				Entity temp = car;
				car = c;
				c = temp;
				swapped = false;
			}
		}
	}
	
	//************************************************Drawing Graphics*******************************************
	
	private class DrawingSurface extends Canvas implements Runnable
	{

		private static final long serialVersionUID = 1L;

		public DrawingSurface()
		{
			thread = new Thread(this, "Display");
			thread.start();
		}
		
		//Draw cars and background
		public void paint(Graphics g)
		{
			 g.clearRect(0, 0, size.width, size.height);
		     background(g);
		     for(Entity c: cars)
		     {
		    	calcVelocity(c);
		    	calcCollisions(c);
		    	c.drawEntity(g);
		    	if(selectedObject != null && c.equals(selectedObject))
		    	{
		    		c.drawCursor(g, mouseX, mouseY);
		    	}
		     }
		}
		
		//Updates the screen using an image buffer
		public void update(Graphics g)
		{
			Graphics offScreenGraphics;
			BufferedImage offScreen = null;
			
			offScreen = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
			offScreenGraphics = offScreen.getGraphics();
			offScreenGraphics.setColor(this.getBackground());
			offScreenGraphics.fillRect(0, 0, size.width, size.height);
			offScreenGraphics.setColor(this.getForeground());
			paint(offScreenGraphics);
			
			g.drawImage(offScreen, 0, 30, this);
		}
		
		//Draws black background of track
		public void background(Graphics g)
		{
			g.setColor(Color.BLACK);
			g.fillRect(0, 30, size.width, size.height);
		}
		
		@Override
		public void run() {
			while(true)
			{
				try {
					Thread.currentThread();
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				repaint();
			}
		}
	}
	
	//Add a car to the track
	public void addCar(int x, int y, int id)
	{
		Car car = new Car(x, y, id);
		cars.add(car);
	}
	
	
	//**********************************Track Getters and Setters********************************************
	
	//Set track friction
	public void setFriction(double f){friction = f;}
	
	//Set track gravity
	public void setGravity(Vector g){gravity = g;}
	
	//Get track gravity
	public static Vector getGravity(){return gravity;}
	
	//Get track friction
	public double getFriction(){return friction;}
	
	
}
