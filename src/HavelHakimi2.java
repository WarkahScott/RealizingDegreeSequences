import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class HavelHakimi2
{
	private String input;
	private String output;
	private String sequence;
	private List<Vertex2> vertices;
	private ArrayList<Group> maxHavelHakimi;
	private ArrayList<Group> minHavelHakimi;
	private ArrayList<Group> uniHavelHakimi;
	private ArrayList<Group> proHavelHakimi;
	private ArrayList<Group> parHavelHakimi;
	private boolean graphic;
	
	public HavelHakimi2(String input)
	{
		this.input = input;
		
		if(!isValidInput(input))
			output = "Invalid input: " + input;
		
		else
		{
			output = "Valid input: " + input;
			maxHavelHakimi = new ArrayList<Group>();
			minHavelHakimi = new ArrayList<Group>();
			uniHavelHakimi = new ArrayList<Group>();
			proHavelHakimi = new ArrayList<Group>();
			parHavelHakimi = new ArrayList<Group>();
			
			maxHH();
			minHH();
			uniformRandomHH();
			probabilisticRandomHH();
		}
	}
	public HavelHakimi2(String input, int parameter)
	{
		this(input);
		this.parameterizedRandomHH(parameter);
	}
	
	public String getOutput() { return output; }
	public String getSequence() { return sequence; }
	public boolean isGraphic() { return graphic; }
	
	public ArrayList<Group> getMaxHH() { return maxHavelHakimi; }
	public ArrayList<Group> getMinHH() { return minHavelHakimi; }
	public ArrayList<Group> getUniHH() { return uniHavelHakimi; }
	public ArrayList<Group> getProHH() { return proHavelHakimi; }
	public ArrayList<Group> getParHH() { return parHavelHakimi; }
	
	private void maxHH()
	{
		boolean graphic = true;
		vertices = initVertices(input);
		
		while(graphic && vertices.get(0).getBucket() > 0)
		{
			Vertex2 v = vertices.remove(0);
			
			while(graphic && v.getDegree() > 0)
			{
				Vertex2 v2 = vertices.remove(0);
				graphic = v.connect(v2);
				v2.setBucket(v2.getBucket() - 1);
				vertices.add(v2);
			}
			
			v.setBucket(0);
			vertices.add(v);
			
			vertices.sort(null);
		}
		
		output = "The degree sequence " + sequence;
		output += (graphic) ? "is graphic." : "is not graphic.";
		
		if(graphic)
			maxHavelHakimi.add(draw(vertices, "Max"));
		
		this.graphic = graphic;
	}
	public void minHH()
	{
		boolean graphic = true;
		vertices = initVertices(input);
		int prev = vertices.size() - 1;
		while(graphic && vertices.get(0).getBucket() > 0)
		{
			while(vertices.get(prev).getDegree() == 0)
				prev--;
			Vertex2 v = vertices.remove(prev);
			
			while(graphic && v.getDegree() > 0)
			{
				Vertex2 v2 = vertices.remove(0);
				graphic = v.connect(v2);
				v2.setBucket(v2.getBucket() - 1);
				vertices.add(v2);
			}
			
			v.setBucket(0);
			vertices.add(v);
			
			vertices.sort(null);
		}

		output = "The degree sequence " + sequence;
		output += (graphic) ? "is graphic." : "is not graphic.";
		
		if(graphic)
			minHavelHakimi.add(draw(vertices, "Min"));
		
		this.graphic = graphic;
	}
	public void uniformRandomHH()
	{
		boolean graphic = true;
		vertices = initVertices(input);
		List<Vertex2> tmp = new LinkedList<Vertex2>();
		while(graphic && vertices.get(0).getBucket() > 0)
		{
			int index = (int) (Math.random() * vertices.size());
			Vertex2 v = vertices.remove(index);
			
			while(graphic && v.getDegree() > 0)
			{	
				if(vertices.isEmpty())
				{
					graphic = false;
					break;
				}
				Vertex2 v2 = vertices.remove(0);
				graphic = v.connect(v2);
				v2.setBucket(v2.getBucket() - 1);
				vertices.add(v2);
			}
			
			v.setBucket(0);
			tmp.add(v);
			
			vertices.sort(null);
		}
		while(!vertices.isEmpty())
			tmp.add(vertices.remove(0));
		vertices = tmp;
		vertices.sort(null);
		
		output = "The degree sequence " + sequence;
		output += (graphic) ? "is graphic." : "is not graphic.";
		
		if(graphic)
			uniHavelHakimi.add(draw(vertices, "Uniform Prob."));
		
		this.graphic = graphic;
	}
	public void probabilisticRandomHH()
	{
		boolean graphic = true;
		vertices = initVertices(input);
		List<Vertex2> tmp = new LinkedList<Vertex2>();
		while(graphic && vertices.get(0).getBucket() > 0)
		{
			Vertex2 v = selectPivot();
			
			while(graphic && v.getDegree() > 0)
			{
				Vertex2 v2 = vertices.remove(0);
				graphic = v.connect(v2);
				v2.setBucket(v2.getBucket() - 1);
				vertices.add(v2);
			}
			
			v.setBucket(0);
			tmp.add(v);
			
			vertices.sort(null);
		}
		
		while(!vertices.isEmpty())
			tmp.add(vertices.remove(0));
		vertices = tmp;
		vertices.sort(null);
		
		output = "The degree sequence " + sequence;
		output += (graphic) ? "is graphic." : "is not graphic.";
		
		if(graphic)
			proHavelHakimi.add(draw(vertices, "Random Prob."));
		
		this.graphic = graphic;
	}
	public void parameterizedRandomHH(int parameter)
	{
		boolean graphic = true;
		vertices = initVertices(input);
		List<Vertex2> tmp = new LinkedList<Vertex2>();
		while(graphic && vertices.get(0).getBucket() > 0)
		{
			Vertex2 v = selectParametricPivot(parameter);
			
			while(graphic && v.getDegree() > 0)
			{
				Vertex2 v2 = vertices.remove(0);
				graphic = v.connect(v2);
				v2.setBucket(v2.getBucket() - 1);
				vertices.add(v2);
			}
			
			v.setBucket(0);
			tmp.add(v);
			
			vertices.sort(null);
		}
		
		while(!vertices.isEmpty())
			tmp.add(vertices.remove(0));
		vertices = tmp;
		vertices.sort(null);
		
		output = "The degree sequence " + sequence;
		output += (graphic) ? "is graphic." : "is not graphic.";
		
		if(graphic)
			parHavelHakimi.add(draw(vertices, "Parameter : " + parameter));
		
		this.graphic = graphic;
	}
	
	private Vertex2 selectPivot()
	{
		int total = 0;
		List<Integer> set = new ArrayList<Integer>();
		Vertex2 val = null;
		
		for(Vertex2 vtx : vertices)
		{
			total += vtx.getDegree();
			for(int i = 0; i < vtx.getDegree(); i++)
				set.add(vtx.getId());
		}
		
		Collections.shuffle(set);
		
		int index = (int) (Math.random() * total);
		for(int i = 0; i < vertices.size(); i++)
		{
			if(vertices.get(i).getId() == set.get(index))
			{
				val = vertices.remove(i);
				break;
			}	
		}
		
		return val;
	}
	private Vertex2 selectParametricPivot(int parameter)
	{
		int total = 0;
		List<Integer> set = new ArrayList<Integer>();
		Vertex2 val = null;
		
		for(Vertex2 vtx : vertices)
		{
			int tmp = (int) Math.pow(vtx.getDegree(), parameter);
			total += tmp;
			for(int i = 0; i < tmp; i++)
				set.add(vtx.getId());
		}
		
		Collections.shuffle(set);
		
		int index = (int) (Math.random() * total);
		for(int i = 0; i < vertices.size(); i++)
		{
			if(vertices.get(i).getId() == set.get(index))
			{
				val = vertices.remove(i);
				break;
			}	
		}
		
		return val;
	}
	
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
	
	private List<Vertex2> initVertices(String input)
	{
		Vertex2.reset();
		Scanner initializer = new Scanner(input);
		int length = initializer.nextInt();
		int tmp = length;
		String seq = "";
		double circleHelper = (Math.PI * 2) / length;
		List<Vertex2> vertices = new LinkedList<Vertex2>();
		
		while(initializer.hasNext())
		{
			int current = initializer.nextInt();
			seq = seq + current + " ";
			Vertex2 v = new Vertex2();
			
			int tmpX = (int) (200 + 180 * Math.cos(circleHelper * (length - tmp)));
			int tmpY = (int) (200 + 180 * Math.sin(circleHelper * (length - tmp)));
			
			v.setX(tmpX);
			v.setY(tmpY);
			v.setDegree(current);
			v.setBucket(current);
			vertices.add(v);
			
			tmp -= 1;
		}
		
		initializer.close();
		sequence = seq;
		return vertices;
	}
	
	private Group draw(List<Vertex2> vertices, String graph)
	{
		Group page = new Group();
		
		for(Vertex2 v : vertices)
		{
			String lbl = "" + (char) ('@' + v.getId()) + ":" + v.getConnections().size();
			Circle vertex = new Circle(v.getX(), v.getY(), 12);
			Text label = new Text(v.getX() - 9, v.getY() + 4, lbl);
			label.setFill(Color.WHITE);
			label.setFont(Font.font("arial", FontWeight.BOLD, null, 10));
			
			for(Vertex2 v2 : v.getConnections())
			{
				Line connect = new Line(v.getX(), v.getY(), v2.getX(), v2.getY());
				connect.setStroke(Color.RED);
				connect.setStrokeWidth(1.5);
				connect.setViewOrder(1);
				page.getChildren().add(connect);
			}
			
			page.getChildren().add(vertex);
			page.getChildren().add(label);
		}
		
		Text graphLabel = new Text(20, 30, graph);
		graphLabel.setFont(Font.font("arial", FontWeight.BOLD, null, 16));
		page.getChildren().add(graphLabel);
		
		return page;
	}
}