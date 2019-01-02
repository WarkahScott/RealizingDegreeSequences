import java.util.*;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;
import java.io.*;

public class Main
{
	public static PriorityQueue<Vertex> vertices = null;
	public static int[] degrees = null;
	public static ArrayList<ArrayList<Vertex>> graphs = null;
	
	public static void main(String[] args)
	{
		Scanner input = new Scanner(System.in);
		
		inputSelect(input);
		
		for(ArrayList<Vertex> g : graphs)
			drawGraph(g);
	}
	
	private static void manualInput(Scanner input)
	{	
		while(vertices == null)
		{
			System.out.println("Enter input: ");
			int length;
			
			if(input.hasNextInt())
				length = input.nextInt();
			else
			{
				System.out.println("Invalid input.");
				input.nextLine();
				continue;
			}
			String[] degreeInput = input.nextLine().trim().split(" ");
			vertices = inputValidator(degreeInput, length);
		}
		
		if(isGraphic(degrees))
		{
			HavelHakimi graph = new HavelHakimi(vertices, degrees);
			graph.maxHH();
			
			if(graph.isGraphic())
				graphs.add(graph.getVertices());
		}
		
		input.close();
		return;
	}
	
	private static void lineInput(Scanner input)
	{
		while(vertices == null)
		{
			int length;
			
			if(input.hasNextInt())
				length = input.nextInt();
			else
				return;
			
			String[] degreeInput = input.nextLine().trim().split(" ");
			vertices = inputValidator(degreeInput, length);
		}
		
		if(isGraphic(degrees))
		{
			HavelHakimi graph = new HavelHakimi(vertices, degrees);
			graph.maxHH();
			
			if(graph.isGraphic())
				graphs.add(graph.getVertices());
		}
		
		input.close();
		return;
	}
	
	private static void fileInput(Scanner input)
	{
		Scanner file = openFile();
		
		while(file.hasNextLine())
		{
			String next = file.nextLine();
			lineInput(new Scanner(next));
			vertices = null;
			degrees = null;
			Vertex.reset();
		}

		input.close();
		file.close();
		return;
	}
	
	private static PriorityQueue<Vertex> inputValidator(String[] input, int length)
	{	
		if(length != input.length)
		{
			System.out.println("Invalid input.");
			return null;
		}
		
		int prev = length - 1;
		for(String s : input)
		{
			int i;
			
			try 
			{
				i = Integer.parseInt(s);
			}
			catch(NumberFormatException e)
			{
				System.out.println("Invalid input.");
				return null;
			}
			
			if(i <= prev)
			{
				prev = i;
				continue;
			}
			else
			{
				System.out.println("Invalid input.");
				return null;
			}
		}
				
		PriorityQueue<Vertex> temp = new PriorityQueue<Vertex>();
		degrees = new int[length];
		
		double tmp = (Math.PI * 2) / length;
		for(int i = 0; i < length; i++)
		{
			int degree = Integer.parseInt(input[i]);
			degrees[i] = degree;
			Vertex v = new Vertex();
			
			int tmpX = (int) (200 + 180 * Math.cos(tmp * i));
			int tmpY = (int) (200 + 180 * Math.sin(tmp * i));
			
			v.setX(tmpX);
			v.setY(tmpY);
			v.setDegree(degree);
			
			temp.add(v);
		}
		
		return temp;
	}
	
	private static void inputSelect(Scanner in)
	{
		int temp = 0;
		
		System.out.println("Enter one of the following to continue:");
		System.out.println("1 - Manual entry");
		System.out.println("2 - Select a file");
		
		while(temp != 1 && temp != 2)
		{
			if(in.hasNextInt())
				temp = in.nextInt();
			else
				in.nextLine();
		}
		
		graphs = new ArrayList<ArrayList<Vertex>>();
		if(temp == 1)
			manualInput(in);
		else
			fileInput(in);
	}
	
	private static boolean isGraphic(int[] degrees)
	{
		int sum = 0;
		for(int i : degrees)
			sum += i;
		if(sum % 2 == 0)
		{
			return true;
		}
		else
		{
			System.out.print("The sequence ");
			for(int i : degrees)
				System.out.print(i + " ");
			System.out.println("is not graphic.");
			return false;
		}
	}
	
	private static Scanner openFile()
	{
		Scanner file = null;
		int result = Integer.MIN_VALUE;
		Window w = new Window(400, 400);
		JFileChooser selected = new JFileChooser();
		FileFilter filter = new FileNameExtensionFilter("Text File", "txt");
		selected.setFileFilter(filter);
		w.add(selected);
		w.pack();
		w.setLocationRelativeTo(null);
		while(result != JFileChooser.APPROVE_OPTION)
			result = selected.showOpenDialog(w);
		w.dispose();
		try 
		{
			file = new Scanner(selected.getSelectedFile());
		} 
		catch (FileNotFoundException e) {}
		
		return file;
	}
	
	private static void drawGraph(ArrayList<Vertex> vertices)
	{
		Window w = new Window(400, 400);
		w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		w.add(new Canvas(vertices));
		w.pack();
		w.setLocationRelativeTo(null);
		w.setVisible(true);
	}
}