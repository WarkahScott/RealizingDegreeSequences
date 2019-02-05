import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class HavelHakimi2
{
	private String input;
	private String output;
	private Queue<Vertex2> vertices;
	private ArrayList<Vertex2> maxHavelHakimi;
	private ArrayList<Vertex2> minHavelHakimi;
	private ArrayList<Vertex2> uniHavelHakimi;
	private ArrayList<Vertex2> proHavelHakimi;
	//private ArrayList<Vertex2> parHavelHakimi;
	
	public HavelHakimi2(String input)
	{
		this.input = input;
		
		if(!isValidInput(input))
			output = "Invalid input: " + input;
		
		else
		{
			output = "Valid input: " + input;
			maxHH();
		}
	}
	
	public String getOutput()
	{
		return output;
	}
	
	private void maxHH()
	{
		vertices = initVertices(input);
	}
	
	public void minHH(){}
	public void uniformRandomHH(){}
	public void probabilisticRandomHH(){}
	public void parameterizedRandomHH(){}
	
	private static boolean isValidInput(String input)
	{
		Scanner validator = new Scanner(input);
		
		if(validator.hasNextInt())
		{
			int count = validator.nextInt();
			int prev = count - 1;
			while(validator.hasNext())
			{
				try
				{
					int tmp = Integer.parseInt(validator.next());
					
					if(tmp > prev)
						return false;
					else
					{
						prev = tmp;
						count -= 1;
					}
				}
				catch(Exception e) { validator.close(); return false; }
				
			}
			
			validator.close();
			if(count != 0)
				return false;
			else 
				return true;
		}
		
		validator.close();
		return false;
	}
	
	private static Queue<Vertex2> initVertices(String input)
	{
		Vertex2.reset();
		Scanner initializer = new Scanner(input);
		int length = initializer.nextInt();
		int tmp = length;
		double circleHelper = (Math.PI * 2) / length;
		Queue<Vertex2> vertices = new LinkedList<Vertex2>();
		
		while(initializer.hasNext())
		{
			int current = initializer.nextInt();

			Vertex2 v = new Vertex2();
			
			int tmpX = (int) (200 + 180 * Math.cos(circleHelper * (length - tmp)));
			int tmpY = (int) (200 + 180 * Math.sin(circleHelper * (length - tmp)));
			
			v.setX(tmpX);
			v.setY(tmpY);
			v.setDegree(current);
			v.setBucket(current);
			vertices.add(v);
			
			char label = (char) ('@' + v.getId());
			System.out.println(label + ": (" + v.getX() + " , " + v.getY() + ")");
			
			tmp -= 1;
		}
		
		initializer.close();
		return vertices;
	}
}