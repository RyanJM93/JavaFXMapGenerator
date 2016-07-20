package com.rjm.dropout.utilities;

import java.io.IOException;

import com.rjm.dropout.mapgenerator.dialogs.MapDimensionDialogController;
import com.rjm.dropout.mapgenerator.enums.MapGenScene;

import javafx.application.Application;
import javafx.application.ConditionalFeature;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.Bloom;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MapGeneratorMain extends Application {
	
	private static volatile MapGeneratorMain _instance;
	private final static Object _syncObject = new Object();
	
	private Stage stage;

    @FXML // fx:id="singleplayer"
    private ImageView singleplayer; // Value injected by FXMLLoader

    @FXML // fx:id="multiplayer"
    private ImageView multiplayer; // Value injected by FXMLLoader

    @FXML // fx:id="settings"
    private ImageView settings; // Value injected by FXMLLoader

    @FXML // fx:id="exitToDesktop"
    private ImageView exitToDesktop; // Value injected by FXMLLoader

    @FXML
    void mouseClicked(MouseEvent event) {

    	if(event.getSource().equals(singleplayer)){
    		this.switchScene(MapGenScene.DIMENSIONS);
    	}
    	
    	if(event.getSource().equals(exitToDesktop)){
    		System.exit(500);
    	}
    }

    @FXML
    void mouseEntered(MouseEvent event) {
    	ImageView source = (ImageView) event.getSource();
    	source.setEffect(new Bloom());
    }

    @FXML
    void mouseExited(MouseEvent event) {
		ImageView source = (ImageView) event.getSource();
		source.setEffect(null);
    }
    
    public static MapGeneratorMain getInstance() {
		
		if (_instance == null) {
			synchronized (_syncObject) {
				if (_instance == null) {
					_instance = new MapGeneratorMain();
				}
			}
		}

		return _instance;
	}

	public static void main(String[] args) {		
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		
		stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("images/menu/DropoutIcon.png")));

		MapGeneratorMain.getInstance().setStage(stage);

		//		BWIDTH = 205;
		//		BHEIGHT = 84;
		//		BIOMEWEIGHT = BWIDTH - BHEIGHT;
		//		String[][] board = new String[BHEIGHT][BWIDTH];
		//		launchMapGen(board);

		if (!Platform.isSupported(ConditionalFeature.SCENE3D)) {
			throw new RuntimeException("*** ERROR: common conditional SCENE3D is not supported");
		}

		MapGeneratorMain.getInstance().switchScene(MapGenScene.DIMENSIONS);
		stage.show();

		stage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, new EventHandler<WindowEvent>(){
			@Override
			public void handle(WindowEvent we)
			{
				if (stage != null)
					stage.close();
				System.exit(0);
			}
		});

		// Setup Warning Dialog Scene

		Parent warningRoot = null;
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(
					getClass().getClassLoader().getResource(FXMLUtils.FXML_WARNING));

			warningRoot = fxmlLoader.load(
					getClass().getClassLoader().getResourceAsStream(FXMLUtils.FXML_WARNING));

			GUIUtils.warningDialogController = fxmlLoader.getController();

		} catch (IOException e) {
			e.printStackTrace();
		}

		Scene warningDialogScene = new Scene(warningRoot);

		GUIUtils.setWarningDialogScene(warningDialogScene);
	}

	public void switchScene(MapGenScene scene){
		switch(scene){
		case DIMENSIONS:

			// Setup Map Dimensions Dialog

			Parent mddRoot = null;
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(
						getClass().getClassLoader().getResource(FXMLUtils.FXML_MAP_DIMENSION_DIALOG));

				mddRoot = fxmlLoader.load(
						getClass().getClassLoader().getResourceAsStream(FXMLUtils.FXML_MAP_DIMENSION_DIALOG));

				MapDimensionDialogController mddController = fxmlLoader.getController();
				mddController.setMapGeneratorMain(this);
			} catch (IOException e) {
				e.printStackTrace();
			}

			Scene mddScene = new Scene(mddRoot);
			MapGeneratorMain.getInstance().getStage().setScene(mddScene);
			MapGeneratorMain.getInstance().getStage().setTitle("Map Dimensions");
			MapGeneratorMain.getInstance().getStage().centerOnScreen();

			break;
		case GAME:

			// Setup Map Generator Utility Application
			
			MapGeneratorUtility mapGeneratorUtility = null;

			Parent mguRoot = null;
			try {
				FXMLLoader fxmlLoader = new FXMLLoader(
						getClass().getClassLoader().getResource(FXMLUtils.FXML_MAP_GENERATOR_UTILITY));

				mguRoot = fxmlLoader.load(
						getClass().getClassLoader().getResourceAsStream(FXMLUtils.FXML_MAP_GENERATOR_UTILITY));

				mapGeneratorUtility = fxmlLoader.getController();
				mapGeneratorUtility.setMapGeneratorMain(MapGeneratorMain.getInstance());
				
			} catch (IOException e) {
				e.printStackTrace();
			}

			Scene mguScene = new Scene(mguRoot);
			MapGeneratorMain.getInstance().getStage().setScene(mguScene);
			MapGeneratorMain.getInstance().getStage().setTitle("Map Generator Utility");
			MapGeneratorMain.getInstance().getStage().centerOnScreen();

			if(MapGeneratorUtility.BWIDTH > 0 && MapGeneratorUtility.BHEIGHT > 0){
				MapGeneratorUtility.BIOMEWEIGHT = MapGeneratorUtility.BWIDTH - MapGeneratorUtility.BHEIGHT;
				String[][] board = new String[MapGeneratorUtility.BHEIGHT][MapGeneratorUtility.BWIDTH];
				mapGeneratorUtility.launchMapGen(board);
			}

			break;
		case MAIN:
			break;
		default:
			break;
		}
	}

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

}
