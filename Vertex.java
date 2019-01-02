import java.util.*;

public class Vertex implements Comparable<Vertex>
{
	private ArrayList<Vertex> connected;
	private static int index = 0;
	private int id;
	private int x, y;
	private int degree;
	
	public Vertex()
	{
		connected = new ArrayList<Vertex>();
		index++;
		id = index;
	}
	
	public Vertex(Vertex v)
	{
		this.connected = v.connected;
		this.id = v.id;
		this.x = v.x;
		this.y = v.y;
		this.degree = v.degree;
	}
	
	public boolean connect(Vertex v)
	{
		if(this == v)
			return false;
		if(degree == 0 || v.degree == 0)
			return false;
		if(connected.contains(v))
			return false;
		
		this.connected.add(v);
		this.degree--;
		
		v.connected.add(this);
		v.degree--;
		
		return true;
	}

	@Override
	public int compareTo(Vertex v) 
	{
		if(this.degree == v.degree)
			return this.id - v.id;
		else
			return v.degree - this.degree;
	}

	public int getX() 
	{ 
		return x; 
	}
	public void setX(int x) 
	{ 
		this.x = x; 
	}
	
	public int getY() 
	{	
		return y; 
	}
	public void setY(int y) 
	{ 
		this.y = y; 
	}
	
	public int getDegree()
	{
		return degree;
	}
	public void setDegree(int degree)
	{
		this.degree = degree;
	}
	
	public int getId()
	{
		return id;
	}
	
	public ArrayList<Vertex> getConnections()
	{
		return connected;
	}
	
	public static void reset()
	{
		index = 0;
	}
}
