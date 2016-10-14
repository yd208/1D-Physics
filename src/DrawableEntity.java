import java.awt.Graphics;

public interface DrawableEntity {
	
	public void drawEntity(Graphics g);
	public void drawCursor(Graphics g, double x, double y);
}
