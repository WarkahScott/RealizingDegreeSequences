import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainGUI extends Application
{
	@Override
	public void start(Stage mainStage) throws Exception
	{
		mainStage.setTitle("Realizing Degree Sequences");
		mainStage.getIcons().add(new Image(MainGUI.class.getResourceAsStream("Abstergo.png")));
		
		//Centers the window
		Rectangle2D screen = Screen.getPrimary().getBounds();
		mainStage.setX((screen.getMaxX() - 525) / 2);
		mainStage.setY((screen.getMaxY() - 200) / 2);
		
		mainStage.setResizable(false);
		
		Scene input = ManualInputScene(mainStage);

		mainStage.setScene(input);
		mainStage.show();
	}
	
	private static Scene ManualInputScene(Stage window)
	{
		Label prompt = new Label("Enter your input below:");
		TextField input = new TextField();
		input.setMaxWidth(350);
		input.setOnMouseClicked(e -> input.selectAll());
		
		RadioButton r1 = new RadioButton("Single-Line");
		RadioButton r2 = new RadioButton("File");
		
		ToggleGroup choice = new ToggleGroup();
		
		r1.setToggleGroup(choice);
		r2.setToggleGroup(choice);
		
		r1.setSelected(true);
		choice.selectedToggleProperty().addListener((obs, oldValue, newValue) -> {	window.hide();
																					window.setScene(FileInputScene(window));
																					window.show();
																				 });
		
		CheckBox max = new CheckBox("Max");
			max.setAllowIndeterminate(false);
		CheckBox min = new CheckBox("Min");
			min.setAllowIndeterminate(false);
		CheckBox uni = new CheckBox("Uniform");
			uni.setAllowIndeterminate(false);
		CheckBox pro = new CheckBox("Probabilistic");
			pro.setAllowIndeterminate(false);
		CheckBox par = new CheckBox("Paramaterized");
			par.setAllowIndeterminate(false);
			
		TextField parameter = new TextField();
		parameter.setPrefSize(50, 15);
		
		HBox checkboxes = new HBox(20, max, min, uni, pro, par);
			checkboxes.setAlignment(Pos.CENTER);
		HBox selection = new HBox(5, checkboxes, parameter);
			selection.setAlignment(Pos.CENTER);
			
		Button generate = new Button("Generate Graph");
		generate.setOnAction(e -> 	{
										Set<String> chosen = new HashSet<String>();
										for(Node c : checkboxes.getChildren())
											if(((CheckBox) c).isSelected())
												chosen.add(((CheckBox) c).getText());
										try
										{
											int num = Integer.parseInt(parameter.getText());
											manualEntryGraphs(window, input.getText(), chosen, num);
										}
										catch(NumberFormatException x)
										{
											manualEntryGraphs(window, input.getText(), chosen, null);
										}
								  	});
		
		HBox h1 = new HBox(20, r1, r2);
		h1.setAlignment(Pos.CENTER);
		
		VBox v1 = new VBox(20, prompt, input, h1, selection, generate);
		v1.setAlignment(Pos.CENTER);
		
		return new Scene(v1, 525, 200);
	}
	
	private static void manualEntryGraphs(Stage owner, String input, Set<String> chosen, Integer parameter)
	{
		HavelHakimi2 network;
		if(parameter == null)
			network = new HavelHakimi2(input);
		else
			network = new HavelHakimi2(input, parameter.intValue());
		ArrayList<Group> max = network.getMaxHH();
		ArrayList<Group> min = network.getMinHH();
		ArrayList<Group> uni = network.getUniHH();
		ArrayList<Group> pro = network.getProHH();
		ArrayList<Group> par = network.getParHH();
		
		if(network.isGraphic() && !chosen.isEmpty())
		{
			Stage view = new Stage();
			view.setTitle("Sequence: " + network.getSequence());
			view.setResizable(true);
			view.setMinWidth(420);
			view.setMaxWidth(1200);
			view.setMinHeight(420);
			view.setMaxHeight(840);
			view.initModality(Modality.WINDOW_MODAL);
			view.initOwner(owner);
			
			FlowPane graphs = new FlowPane();
			graphs.setPadding(new Insets(5));
			graphs.setHgap(5);
			graphs.setVgap(5);
			graphs.setPrefWrapLength(1165);
			graphs.setPrefHeight(400);
			
			if(!max.isEmpty() && chosen.contains("Max"))
				graphs.getChildren().add(max.get(max.size() - 1));
			if(!min.isEmpty() && chosen.contains("Min"))
				graphs.getChildren().add(min.get(min.size() - 1));
			if(!uni.isEmpty() && chosen.contains("Uniform"))
				graphs.getChildren().add(uni.get(uni.size() - 1));
			if(!pro.isEmpty() && chosen.contains("Probabilistic"))
				graphs.getChildren().add(pro.get(pro.size() - 1));
			if(par != null && !par.isEmpty() && chosen.contains("Paramaterized"))
				graphs.getChildren().add(par.get(par.size() - 1));
			
			if(graphs.getChildren().size() < 3)
				graphs.setMaxWidth(graphs.getChildren().size() * 400);
			
			Scene display = new Scene(graphs);
			view.setScene(display);
			view.sizeToScene();
			view.show();
		}
	}
	
	private static Scene FileInputScene(Stage window)
	{
		Label prompt = new Label("Select a file:");
		TextField input = new TextField();
		input.setPrefWidth(300);
		input.setEditable(false);
		
		Button generate = new Button("Generate Graph");
		generate.setDisable(true);
		
		FileChooser chooser = new FileChooser();
		chooser.getExtensionFilters().add(new ExtensionFilter("Text File", "*.txt"));
		
		ImageView folder = new ImageView(new Image(MainGUI.class.getResourceAsStream("Folder.png"), 20, 20, true, false));
		Button select = new Button("", folder);
		select.setPrefWidth(25);
		select.setOnAction(e -> {
									File chosen = chooser.showOpenDialog(window);
									if(chosen != null)
									{
										input.setText(chosen.getPath());
										generate.setDisable(false);
									}
									else
									{
										input.setText("");
										generate.setDisable(true);
									}
								});
		
		
		
		RadioButton r1 = new RadioButton("Single-Line");
		RadioButton r2 = new RadioButton("File");
		
		ToggleGroup choice = new ToggleGroup();
		
		r1.setToggleGroup(choice);
		r2.setToggleGroup(choice);
		
		r2.setSelected(true);
		choice.selectedToggleProperty().addListener((obs, oldValue, newValue) -> { 	window.hide();
																					window.setScene(ManualInputScene(window));
																					window.show();
																				 });
		CheckBox max = new CheckBox("Max");                     
			max.setAllowIndeterminate(false);                   
		CheckBox min = new CheckBox("Min");                     
			min.setAllowIndeterminate(false);                   
		CheckBox uni = new CheckBox("Uniform");                 
			uni.setAllowIndeterminate(false);                   
		CheckBox pro = new CheckBox("Probabilistic");           
			pro.setAllowIndeterminate(false);                   
		CheckBox par = new CheckBox("Paramaterized");           
			par.setAllowIndeterminate(false);                   
			                                                    
		TextField parameter = new TextField();                  
		parameter.setPrefSize(50, 15);                          
		                                                     
		HBox checkboxes = new HBox(20, max, min, uni, pro, par);
			checkboxes.setAlignment(Pos.CENTER);                
		HBox selection = new HBox(5, checkboxes, parameter);    
		selection.setAlignment(Pos.CENTER); 
		
		generate.setOnAction(e -> 	{
										Set<String> chosen = new HashSet<String>();
										for(Node c : checkboxes.getChildren())
											if(((CheckBox) c).isSelected())
												chosen.add(((CheckBox) c).getText());
										try
										{
											int num = Integer.parseInt(parameter.getText());
											fileEntryGraphs(window, input, chosen, num);
										}
										catch(NumberFormatException x)
										{
											fileEntryGraphs(window, input, chosen, null);
										}
								  	});
		
		HBox h1 = new HBox(input, select);
		h1.setAlignment(Pos.CENTER);
		HBox h2 = new HBox(20, r1, r2);
		h2.setAlignment(Pos.CENTER);
		
		VBox v1 = new VBox(15, prompt, h1, h2, selection, generate);
		v1.setAlignment(Pos.CENTER);
		
		return new Scene(v1, 525, 200);
	}
	
	private static void fileEntryGraphs(Stage owner, TextField input, Set<String> chosen, Integer parameter)
	{
		ObservableList<String> inputs = FXCollections.observableArrayList();
		try 
		{
			Scanner in = new Scanner(new File(input.getText()));
			while(in.hasNextLine())
				inputs.add(in.nextLine());
			in.close();
		} catch (FileNotFoundException e) {}
		
		ListView<String> list = new ListView<String>(inputs);
		final Stage view = new Stage();
		view.setTitle("Input List");
		view.setResizable(true);
		view.setMinWidth(420);
		view.setMaxWidth(1200);
		view.setMinHeight(420);
		view.setMaxHeight(840);
		view.initModality(Modality.WINDOW_MODAL);
		view.initOwner(owner);
		VBox v1 = new VBox(list);
			
		Scene display = new Scene(v1);
		view.setScene(display);
		view.sizeToScene();
		view.show();
		
		list.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> 	
		{ 	
			manualEntryGraphs(view, newValue, chosen, parameter);
		});
	}
	
	public static void main(String[] args)
	{
		Application.launch();
	}
}
