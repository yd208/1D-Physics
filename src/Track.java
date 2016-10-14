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
//	private double mouseX = 0;
	@SuppressWarnings("unused")
	private boolean reverse_dir = false;
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
		
		friction = .96;
		loadCars(2);
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
			addCar((i + 5) * 40, 60);
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
		        if(source.getValue() < 0)
		        {
		        	gravity = new Vector(-(source.getValue() - .05), Math.PI);
		        }
		        else if(source.getValue() > 0)
		        {
		        	gravity = new Vector(source.getValue() + .05, 0);
		        }
		        else
		        {
		        	gravity = new Vector(source.getValue(), 0);
		        }
		        
		        System.out.println("Mag: " + gravity.getMagnitude() + ", Angle: " + gravity.getAngle());
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
			//System.out.println("case 2| " + "accel: " + car.getAcceleration() + ", final_vel: " + final_vel.getMagnitude());
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
			reverse_dir = true;
		}
		else if(car.getXcor() < 0)
		{
			avg_vel.setMagnitude(avg_vel.getMagnitude() * car.getElasticity());
			avg_vel.setAngle(0);
			final_vel.setMagnitude(final_vel.getMagnitude() * car.getElasticity());
			final_vel.setAngle(0);
			car.setXcor(0);
			reverse_dir = false;
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
		for(Entity c: cars)
		{
			if((c.getXcor() + 34) >= car.getXcor())
			{
				//Vector direction1 = new Vector(c.getDirection(), car.getDirection());
				//Vector direction2 = new Vector();
				//Comment
			}
			else if(c.getXcor() <= (car.getXcor() + 34))
			{
				c.getDirection().getMagnitude();
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
		     //drawGuiControls(g);
		     for(Entity c: cars)
		     {
		    	calcVelocity(c);
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
	public void addCar(int x, int y)
	{
		Car car = new Car(x, y);
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
