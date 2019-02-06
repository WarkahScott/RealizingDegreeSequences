import java.io.File;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.GridPane;
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
		mainStage.setWidth(400);
		mainStage.setHeight(200);
		
		//Centers the window
		Rectangle2D screen = Screen.getPrimary().getBounds();
		mainStage.setX((screen.getMaxX() - 400) / 2);
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
		Button generate = new Button("Generate Graph");
		generate.setOnAction(e -> 	{
										manualEntryGraphs(window, input);
								  	});
		
		HBox h1 = new HBox(20, r1, r2);
		h1.setAlignment(Pos.CENTER);
		
		VBox v1 = new VBox(20, prompt, input, h1, generate);
		v1.setAlignment(Pos.CENTER);
		
		return new Scene(v1, 400, 200);
	}
	
	private static void manualEntryGraphs(Stage owner, TextField input)
	{
		HavelHakimi2 network = new HavelHakimi2(input.getText());
		ArrayList<Group> max = network.getMaxHH();
		ArrayList<Group> min = network.getMinHH();
		ArrayList<Group> uni = network.getUniHH();
		ArrayList<Group> pro = network.getProHH();
		//ArrayList<Group> par = network.getParHH();
		
		Stage view = new Stage();
		view.setResizable(false);
		view.initModality(Modality.WINDOW_MODAL);
		view.initOwner(owner);
		
		GridPane graphs = new GridPane();
		graphs.setHgap(10);
		graphs.setVgap(10);
		
		if(max != null && !max.isEmpty())
			graphs.add(max.get(max.size() - 1), 0, 0);
		if(min != null && !min.isEmpty())
			graphs.add(min.get(min.size() - 1), 1, 0);
		if(uni != null && !uni.isEmpty())
			graphs.add(uni.get(uni.size() - 1), 0, 1);
		if(pro != null && !pro.isEmpty())
			graphs.add(pro.get(pro.size() - 1), 1, 1);
		
		Scene display = new Scene(graphs);
		view.setScene(display);
		view.show();
		input.setText(network.getOutput());
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
		
		HBox h1 = new HBox(input, select);
		h1.setAlignment(Pos.CENTER);
		HBox h2 = new HBox(20, r1, r2);
		h2.setAlignment(Pos.CENTER);
		
		VBox v1 = new VBox(20, prompt, h1, h2, generate);
		v1.setAlignment(Pos.CENTER);
		
		
		return new Scene(v1, 400, 200);
	}
	
	public static void main(String[] args)
	{
		Application.launch();
	}
}
