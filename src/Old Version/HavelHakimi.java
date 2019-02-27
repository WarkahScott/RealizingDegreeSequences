import java.util.ArrayList;
import java.util.PriorityQueue;

public class HavelHakimi 
{
	private ArrayList<Vertex> vertices;
	private int[] degrees;
	private boolean graphic;
	
	public HavelHakimi(PriorityQueue<Vertex> vertices, int[] degrees)
	{
		this.vertices = new ArrayList<Vertex>();
		for(Vertex v : vertices)
			this.vertices.add(new Vertex(v));
		
		this.degrees = new int[degrees.length];
		for(int i = 0; i < degrees.length; i++)
			this.degrees[i] = degrees[i];
		
		graphic = true;
	}
	
	public void maxHH()
	{
		int[] temp = new int[degrees.length];
		for(int i = 0; i < degrees.length; i++)
			temp[i] = degrees[i];
		
		while(!this.isEmpty() && isValid(temp))
		{
			vertices.sort(null);
			
			Vertex pivot = vertices.get(0);
			int tmp = pivot.getDegree();
			for(int i = 1; i <= tmp; i++)
				pivot.connect(vertices.get(i));
			
			for(Vertex v : vertices)
				degrees[v.getId() - 1] = v.getDegree();
		}
		
		vertices.sort(null);
		degrees = temp;
		for(int i = 0; i < vertices.size(); i++)
			vertices.get(i).setDegree(degrees[i]);
	}
	
	public void minHH(){}
	public void uniformRandomHH(){}
	public void probabilisticRandomHH(){}
	public void parameterizedRandomHH(){}
	
	private boolean isEmpty()
	{
		boolean empty = true;
		for(int i : degrees)
			if(i > 0)
				empty = false;
		return empty;	
	}
	
	private boolean isValid(int[] original)
	{
		int count = 0;
		for(int i : degrees)
		{
			if(i > 0)
				count++;
		}
		for(int i : degrees)
		{
			if(i >= count)
			{
				System.out.print("The sequence ");
				for(int d : original)
					System.out.print(d + " ");
				System.out.println("is not graphic.");
				graphic = false;
				return false;
			}
		}
		return true;
	}
	
	public ArrayList<Vertex> getVertices()
	{
		return vertices;
	}
	
	public boolean isGraphic()
	{
		return graphic;
	}
}
