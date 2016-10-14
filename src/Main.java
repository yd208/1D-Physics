import javax.swing.SwingUtilities;

public class Main {
	
	private static Track track;
	
	public static void createAndShowTrack()
	{
		track = new Track();
		track.setVisible(true);
	}
	
	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable(){
			@Override
			public void run() {
				createAndShowTrack();
			}	
		});
	}
}
