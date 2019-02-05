import java.util.ArrayList;

public class Vertex2 implements Comparable<Vertex2>
{
	private ArrayList<Vertex2> connected;
	private static int index = 0;
	private int id;
	private int x, y;
	private int degree;
	private int bucket;
	
	public Vertex2()
	{
		connected = new ArrayList<Vertex2>();
		index++;
		id = index;
	}
	
	public Vertex2(Vertex2 v)
	{
		this.connected = v.connected;
		this.id = v.id;
		this.x = v.x;
		this.y = v.y;
		this.degree = v.degree;
	}
	
	public boolean connect(Vertex2 v)
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
	public int compareTo(Vertex2 v) 
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
	
	public int getBucket()
	{
		return bucket;
	}
	public void setBucket(int bucket)
	{
		this.bucket = bucket;
	}
	
	public int getId()
	{
		return id;
	}
	
	public ArrayList<Vertex2> getConnections()
	{
		return connected;
	}
	
	public static void reset()
	{
		index = 0;
	}
	
	
}
