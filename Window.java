import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

@SuppressWarnings("serial")
public class Window extends JFrame 
{

	public Window(int width, int height)
	{
		super("Realizing Degree Sequences");
		this.setSize(width, height);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
}

@SuppressWarnings("serial")
class Canvas extends JPanel 
{
	private ArrayList<Vertex> vertices;
	
    public Canvas(ArrayList<Vertex> vertices) 
    {
    	super();
        setBorder(BorderFactory.createLineBorder(Color.black));
        this.vertices = vertices;
    }

    public Dimension getPreferredSize() 
    {
        return new Dimension(410,410);
    }

    public void paintComponent(Graphics g) 
    {
        super.paintComponent(g);       
        
        String sequence = "Sequence: ";
        for(Vertex v : vertices)
        	sequence += v.getDegree() + " ";
        
        g.setColor(Color.black);
        for(Vertex v : vertices)
        	for(Vertex v2 : v.getConnections())
        		g.drawLine(v.getX(), v.getY(), v2.getX(), v2.getY());
        
        g.drawString(sequence,10,20);
        
        g.setColor(Color.red);
        for(Vertex v : vertices)
        	g.fillOval(v.getX() - 10, v.getY() - 10, 25, 25);
        
        char c = 'A';
        g.setColor(Color.white);
        g.setFont(new Font("Calibri", Font.BOLD, 14));
        for(Vertex v : vertices)
        {
        	g.drawString("" + c, v.getX(), v.getY());
        	c++;
        }
    }  
}
