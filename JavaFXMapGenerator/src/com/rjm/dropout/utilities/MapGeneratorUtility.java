package com.rjm.dropout.utilities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import com.rjm.dropout.mapgenerator.enums.Elevation;
import com.rjm.dropout.mapgenerator.enums.Terrain;
import com.rjm.dropout.mapgenerator.graphics.MapGenColors;
import com.rjm.dropout.mapgenerator.graphics.MapGenTextures;
import com.rjm.dropout.mapgenerator.objects.Generator;
import com.rjm.dropout.mapgenerator.objects.HexTile;
import com.rjm.dropout.mapgenerator.objects.Point;
import com.rjm.dropout.mapgenerator.enums.ContinentSize;
import com.rjm.dropout.mapgenerator.enums.WorldAge;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SnapshotParameters;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.control.TreeView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class MapGeneratorUtility {	

	MapGeneratorMain mapGeneratorMain;

	public Generator generator;

	public Camera camera;

	// Map sizes (ratio 2.44 h to w)

	public static int BWIDTH = 0;
	public static int BHEIGHT = 0;

	//Map Generation Heuristics
	public static int BIOMEWEIGHT = BWIDTH - BHEIGHT;
	public static int MOUNTAINCHANCE = 50;

	public static HashMap<Point, HexTile> hexMap;

	public static HashMap<Point, HexTile> coastMap;
	public static HashMap<Point, HexTile> desertMap;
	public static HashMap<Point, HexTile> forestMap;
	public static HashMap<Point, HexTile> freshwaterMap;
	public static HashMap<Point, HexTile> grasslandMap;
	public static HashMap<Point, HexTile> jungleMap;
	public static HashMap<Point, HexTile> marshMap;
	public static HashMap<Point, HexTile> oceanMap;
	public static HashMap<Point, HexTile> savannahMap;
	public static HashMap<Point, HexTile> snowMap;
	public static HashMap<Point, HexTile> taigaMap;
	public static HashMap<Point, HexTile> tundraMap;

	static int allTerrainTotal = 0;
	static int coastTotal = 0;
	static int desertTotal = 0;
	static int forestTotal = 0;
	static int freshwaterTotal = 0;
	static int grasslandTotal = 0;
	static int jungleTotal = 0;
	static int marshTotal = 0;
	static int oceanTotal = 0;
	static int savannahTotal = 0;
	static int snowTotal = 0;
	static int taigaTotal = 0;
	static int tundraTotal = 0;

	public static HashMap<Point, HexTile> desertSpawnMap;
	public static HashMap<Point, HexTile> grasslandSpawnMap;
	public static HashMap<Point, HexTile> forestSpawnMap;
	public static HashMap<Point, HexTile> jungleSpawnMap;
	public static HashMap<Point, HexTile> marshSpawnMap;
	public static HashMap<Point, HexTile> savannahSpawnMap;
	public static HashMap<Point, HexTile> snowSpawnMap;
	public static HashMap<Point, HexTile> taigaSpawnMap;
	public static HashMap<Point, HexTile> tundraSpawnMap;

	static boolean isGridOn = true;	

	// 3D Scene

	@FXML // fx:id="mainVBox"
	private VBox mainVBox; // Value injected by FXMLLoader

	@FXML // fx:id="menuBar"
	private MenuBar menuBar; // Value injected by FXMLLoader
	@FXML // fx:id="saveAsPNGMenuItem"
	private MenuItem saveAsPNGMenuItem; // Value injected by FXMLLoader	

	@FXML // fx:id="showGridCheckMenuItem"
	private CheckMenuItem showGridCheckMenuItem; // Value injected by FXMLLoader

	@FXML // fx:id="desertCityButton"
	private Button desertCityButton; // Value injected by FXMLLoader

	@FXML // fx:id="forestCityButton"
	private Button forestCityButton; // Value injected by FXMLLoader

	@FXML // fx:id="grasslandCityButton"
	private Button grasslandCityButton; // Value injected by FXMLLoader

	@FXML // fx:id="jungleCityButton"
	private Button jungleCityButton; // Value injected by FXMLLoader

	@FXML // fx:id="marshCityButton"
	private Button marshCityButton; // Value injected by FXMLLoader

	@FXML // fx:id="savannahCityButton"
	private Button savannahCityButton; // Value injected by FXMLLoader

	@FXML // fx:id="snowCityButton"
	private Button snowCityButton; // Value injected by FXMLLoader

	@FXML // fx:id="taigaCityButton"
	private Button taigaCityButton; // Value injected by FXMLLoader

	@FXML // fx:id="tundraCityButton"
	private Button tundraCityButton; // Value injected by FXMLLoader

	@FXML // fx:id="canvasScrollPane"
	private ScrollPane canvasScrollPane; // Value injected by FXMLLoader

	@FXML // fx:id="canvasBackground"
	private StackPane canvasBackground; // Value injected by FXMLLoader

	@FXML // fx:id="groupThreeD"
	private Group groupThreeD; // Value injected by FXMLLoader

	@FXML // fx:id="treeView"
	private TreeView<?> treeView; // Value injected by FXMLLoader

	@FXML // fx:id="pagination"
	private Pagination pagination; // Value injected by FXMLLoader

	private SubScene msaa;

	Rectangle ground;

	double startX = 0.0;
	double startY = 0.0;

	private Rotate rotateX = new Rotate(0, 1080, 540, 0, Rotate.X_AXIS);
	private Rotate rotateY = new Rotate(0, 1080, 540, 0, Rotate.Y_AXIS);
	private Rotate rotateZ = new Rotate(0, 1080, 540, 0, Rotate.Z_AXIS);

	private Group guiGroup = new Group();
//	private Group resourceGroup = new Group();
	private Group tilegroup = new Group();

	public List<Node> nodesToAddToScene = new ArrayList<>();

	private double mousePosX, mousePosY = 0;
	private void handleMouseEvents() {
		msaa.setOnMousePressed((MouseEvent me) -> {
			mousePosX = me.getSceneX();
			mousePosY = me.getSceneY();
		});

		msaa.setOnMouseDragged((MouseEvent me) -> {
			double dx = (mousePosX - me.getSceneX());
			double dy = (mousePosY - me.getSceneY());
			if (me.isMiddleButtonDown()) {
				double rotation = (rotateX.getAngle() - (dy / ground.getHeight() * 360) * (12*Math.PI / 180));

				if(rotation > 0){
					rotateX.setAngle(Math.min(rotation, -5));
				} else {
					rotateX.setAngle(Math.max(rotation, -45));
				}
				rotateZ.setAngle(rotateZ.getAngle() - (dx / ground.getWidth() * -360) * (24*Math.PI / 180));
			}
			mousePosX = me.getSceneX();
			mousePosY = me.getSceneY();
		});
	}

	@FXML
	void scrollPaneClicked(MouseEvent event) {
		System.out.println(event.getButton().toString() + " Mouse Button Pressed");

		if(!event.getButton().toString().equals("MIDDLE")){
			startX = event.getSceneX();
			startY = event.getSceneY();
		}

		if(!event.getButton().toString().equals("SECONDARY")){
			canvasScrollPane.setPannable(false);
		} else {
			canvasScrollPane.setPannable(true);
		}
	}

	@FXML
	void cameraRotate(MouseEvent event) {
		System.out.println(event.getButton().toString() + " Mouse Button Dragged");
	}

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {

		MapGenTextures.loadTextures();

		canvasScrollPane.setPannable(true);
		canvasScrollPane.setFitToHeight(true);
		canvasScrollPane.setFitToWidth(true);
		canvasScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
		canvasScrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);

		canvasScrollPane.addEventFilter(ScrollEvent.SCROLL,new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				System.out.println(event.getSource());

				if(event.getSource().equals(canvasScrollPane)){
					Camera camera = msaa.getCamera();
					if(event.getDeltaY() > 0 && camera.getTranslateZ() < 5000){
						camera.setTranslateZ(camera.getTranslateZ() + 250);
					} else if(event.getDeltaY() < 0 && camera.getTranslateZ() > -15000){
						camera.setTranslateZ(camera.getTranslateZ() - 250);
					}
					System.out.println("Camera Z: " + camera.getTranslateZ());
					event.consume();
				} else {
					event.consume();
				}
			}
		});
		
		ground = new Rectangle(2160, 1080);
		
		getTilegroup().getTransforms().addAll(rotateX, rotateY, rotateZ);
		nodesToAddToScene.add(getTilegroup());
		
//		getResourceGroup().getTransforms().addAll(rotateX, rotateY, rotateZ);
//		nodesToAddToScene.add(getResourceGroup());

		camera = new PerspectiveCamera();
		camera.setTranslateX(0);
		camera.setTranslateY(0);
		camera.setTranslateZ(0);

		msaa = createSubScene(nodesToAddToScene, Color.TRANSPARENT,	camera, true, 8160, 4080);

		canvasBackground.getChildren().add(msaa);

		String oceanHexColor = "2E4172";
		canvasBackground.setStyle("-fx-background-color: #" + oceanHexColor);

		handleMouseEvents();
	}

	public void launchMapGen(String[][] board) {
		hexMap = new HashMap<Point, HexTile>();
		coastMap = new HashMap<Point, HexTile>();
		oceanMap = new HashMap<Point, HexTile>();
		freshwaterMap = new HashMap<Point, HexTile>();
		grasslandMap = new HashMap<Point, HexTile>();
		forestMap = new HashMap<Point, HexTile>();
		taigaMap = new HashMap<Point, HexTile>();
		jungleMap = new HashMap<Point, HexTile>();
		savannahMap = new HashMap<Point, HexTile>();
		desertMap = new HashMap<Point, HexTile>();
		tundraMap = new HashMap<Point, HexTile>();
		snowMap = new HashMap<Point, HexTile>();
		marshMap = new HashMap<Point, HexTile>();

		for (int i=0;i<BHEIGHT;i++) {
			for (int j=0;j<BWIDTH;j++) {
				board[i][j]="";
			}
		}

		generator = new Generator(this, ContinentSize.HUGE, WorldAge.FourBillion, board);

		for(HexTile hexTile : generator.setup()){
			getTilegroup().getChildren().add(hexTile.getHexagon());
		}

		desertSpawnMap = getViableSpawns(Terrain.DESERT);
		if(desertSpawnMap.size() <= 0){
			desertCityButton.setDisable(true);
		} else {
			desertCityButton.setDisable(false);
		}
		forestSpawnMap = getViableSpawns(Terrain.FOREST);
		if(forestSpawnMap.size() <= 0){
			forestCityButton.setDisable(true);
		} else {
			forestCityButton.setDisable(false);
		}
		grasslandSpawnMap = getViableSpawns(Terrain.GRASSLAND);
		if(grasslandSpawnMap.size() <= 0){
			grasslandCityButton.setDisable(true);
		} else {
			grasslandCityButton.setDisable(false);
		}
		jungleSpawnMap = getViableSpawns(Terrain.JUNGLE);
		if(jungleSpawnMap.size() <= 0){
			jungleCityButton.setDisable(true);
		} else {
			jungleCityButton.setDisable(false);
		}
		marshSpawnMap = getViableSpawns(Terrain.MARSH);
		if(marshSpawnMap.size() <= 0){
			marshCityButton.setDisable(true);
		} else {
			marshCityButton.setDisable(false);
		}
		savannahSpawnMap = getViableSpawns(Terrain.SAVANNAH);
		if(savannahSpawnMap.size() <= 0){
			savannahCityButton.setDisable(true);
		} else {
			savannahCityButton.setDisable(false);
		}
		snowSpawnMap = getViableSpawns(Terrain.SNOW);
		if(snowSpawnMap.size() <= 0){
			snowCityButton.setDisable(true);
		} else {
			snowCityButton.setDisable(false);
		}
		taigaSpawnMap = getViableSpawns(Terrain.TAIGA);
		if(taigaSpawnMap.size() <= 0){
			taigaCityButton.setDisable(true);
		} else {
			taigaCityButton.setDisable(false);
		}
		tundraSpawnMap = getViableSpawns(Terrain.TUNDRA);
		if(tundraSpawnMap.size() <= 0){
			tundraCityButton.setDisable(true);
		} else {
			tundraCityButton.setDisable(false);
		}

		ArrayList<Integer> totals = MapGeneratorUtility.getTotals();

		coastTotal = totals.get(0);
		System.out.println("Total Coast Tiles: " + coastTotal);
		desertTotal = totals.get(1);
		System.out.println("Total Desert Tiles: " + desertTotal);
		forestTotal = totals.get(2);
		System.out.println("Total Forest Tiles: " + forestTotal);
		freshwaterTotal = totals.get(3);
		System.out.println("Total Freshwater Tiles: " + freshwaterTotal);
		grasslandTotal = totals.get(4);
		System.out.println("Total Grassland Tiles: " + grasslandTotal);
		jungleTotal = totals.get(5);
		System.out.println("Total Jungle Tiles: " + jungleTotal);
		marshTotal = totals.get(6);
		System.out.println("Total Marsh Tiles: " + marshTotal);
		oceanTotal = totals.get(7);
		System.out.println("Total Ocean Tiles: " + oceanTotal);
		savannahTotal = totals.get(8);
		System.out.println("Total Savannah Tiles: " + savannahTotal);
		snowTotal = totals.get(9);
		System.out.println("Total Snow Tiles: " + snowTotal);
		taigaTotal = totals.get(10);
		System.out.println("Total Taiga Tiles: " + taigaTotal);
		tundraTotal = totals.get(11);
		System.out.println("Total Tundra Tiles: " + tundraTotal);

		allTerrainTotal = coastTotal + desertTotal + forestTotal + freshwaterTotal + grasslandTotal + jungleTotal + marshTotal + oceanTotal + savannahTotal + snowTotal + taigaTotal + tundraTotal;

		System.out.println("Vaiable City Spawns: Desert = " + desertSpawnMap.size() + ", Forest = " + forestSpawnMap.size() + 
				", Grassland = " + grasslandSpawnMap.size() + ", Jungle = " + jungleSpawnMap.size() + ", Marsh = " + marshSpawnMap.size() +
				", Savannah = " + savannahSpawnMap.size() + ", Snow = " + snowSpawnMap.size() + ", Taiga = " + taigaSpawnMap.size() + 
				", Tundra = " + tundraSpawnMap.size());
		
		String infoPrint = new String("Desert: " + desertTotal + ", Forest: " + forestTotal + ", Freshwater: " + freshwaterTotal + ", Grassland: " + grasslandTotal + ", Jungle: " + jungleTotal + ", Marsh: " + marshTotal + ", Ocean: " + oceanTotal + ", Savannah: " + savannahTotal + ", Snow: " + snowTotal + ", Taiga: " + taigaTotal + ", Tundra: " + tundraTotal + " = Total: " + allTerrainTotal);
		mapGeneratorMain.getStage().setTitle("Map Generator Utility - " + infoPrint);
	
		rotateX.setAngle(-5);
	}

	private static SubScene createSubScene(List<Node> nodes, Paint fillPaint, Camera camera, boolean msaa, double width, double height) {
		Group root = new Group();

		PointLight light = new PointLight(Color.WHITE);
		light.setTranslateX(50);
		light.setTranslateY(-300);
		light.setTranslateZ(-400);
		PointLight light2 = new PointLight(Color.LIGHTGOLDENRODYELLOW);
		light2.setTranslateX(400);
		light2.setTranslateY(0);
		light2.setTranslateZ(-400);

		//		AmbientLight ambientLight = new AmbientLight(Color.LIGHTYELLOW);

		//		for(Node node : nodes){
		//			node.setRotationAxis(new Point3D(1080.0, 540.0, 0.0).normalize());
		//		}

		root.getChildren().addAll(/*ambientLight,*/	light, light2);
		root.getChildren().addAll(nodes);

		SubScene subScene = new SubScene(root, width, height, true, msaa ? SceneAntialiasing.BALANCED : SceneAntialiasing.DISABLED);
		subScene.setFill(fillPaint);
		subScene.setCamera(camera);

		return subScene;
	}

	static int distanceFromOrigin(Point origin, Point current){
		int xAway = Math.abs(current.x - origin.x);
		int yAway = Math.abs(current.y - origin.y);

		if(xAway > yAway){
			return xAway;
		} else {
			return yAway;
		}
	}

	static int distanceFromPole(Point current){
		Point pole = new Point(-1,-1);
		pole.x = current.x;
		if(current.x < (BHEIGHT - 1)){
			pole.y = 0;
		} else {
			pole.y = BHEIGHT - 1;			
		}

		int distance = distanceFromOrigin(pole, current);

		return distance;
	}

	static ArrayList<Point> getAdjacentPoints(Point point){
		ArrayList<Point> adjacentPoints = new ArrayList<Point>();
		if(point.x%2 == 0){
			// Top Left
			adjacentPoints.add(new Point(point.x-1, point.y-1));
			// Top Mid
			adjacentPoints.add(new Point(point.x, point.y-1));
			// Top Right
			adjacentPoints.add(new Point(point.x+1, point.y-1));
			// Bottom Right
			adjacentPoints.add(new Point(point.x+1, point.y));
			// Bottom Mid
			adjacentPoints.add(new Point(point.x, point.y+1));
			// Bottom Left
			adjacentPoints.add(new Point(point.x-1, point.y));
		} else {
			// Top Left
			adjacentPoints.add(new Point(point.x-1, point.y));
			// Top Mid
			adjacentPoints.add(new Point(point.x, point.y-1));
			// Top Right
			adjacentPoints.add(new Point(point.x+1, point.y));
			// Bottom Right
			adjacentPoints.add(new Point(point.x+1, point.y+1));
			// Bottom Mid
			adjacentPoints.add(new Point(point.x, point.y+1));
			// Bottom Left
			adjacentPoints.add(new Point(point.x-1, point.y+1));
		}

		/*
		// Prune away points that exist outside the bounds of the map
		for(Point p : adjacentPoints){
			if(p.x < 0 || p.x > BWIDTH-1 || p.y < 0 || p.y > BHEIGHT-1){
				adjacentPoints.remove(p);
			}
		}
		 */

		return new ArrayList<Point>(adjacentPoints);
	}

	static public HashMap<Point, HexTile> getViableSpawns(Terrain terrain){
		HashMap<Point, HexTile> inputMap = new HashMap<Point, HexTile>();
		HashMap<Point, HexTile> returnMap = new HashMap<Point, HexTile>();

		switch(terrain){
		case DESERT:
			inputMap = desertMap;
			break;
		case FOREST:
			inputMap = forestMap;
			break;
		case FRESHWATER:
			break;
		case GRASSLAND:
			inputMap = grasslandMap;
			break;
		case JUNGLE:
			inputMap = jungleMap;
			break;
		case MARSH:
			inputMap = marshMap;
			break;
		case OCEAN:
			break;
		case SAVANNAH:
			inputMap = savannahMap;
			break;
		case SNOW:
			inputMap = snowMap;
			break;
		case TAIGA:
			inputMap = taigaMap;
			break;
		case TUNDRA:
			inputMap = tundraMap;
			break;
		case COAST:
			break;
		default:
			break;					
		}

		List<Point> mapKeysAsArray = new ArrayList<Point>(inputMap.keySet());

		for(Point key : mapKeysAsArray){
			List<Point> surroundingTiles = new ArrayList<Point>(getAdjacentPoints(key));
			int viableSpawn = 3;
			for(Point point : surroundingTiles){
				if(!inputMap.containsKey(point)){
					viableSpawn--;
				}
			}

			if(viableSpawn >= 0){
				returnMap.put(key, inputMap.get(key));
			}
		}

		return returnMap;
	}

	static void spawnCity(Terrain terrain){
		HashMap<Point, HexTile> spawnMap = null;
		HashMap<Point, HexTile> terrainMap = null;
		Color cityColor = null;

		switch(terrain){
		case DESERT:
			spawnMap = desertSpawnMap;
			terrainMap = desertMap;
			cityColor = MapGenColors.DESERTCITY;
			break;
		case FOREST:
			spawnMap = forestSpawnMap;
			terrainMap = forestMap;
			cityColor = MapGenColors.FORESTCITY;
			break;
		case FRESHWATER:
			break;
		case GRASSLAND:
			spawnMap = grasslandSpawnMap;
			terrainMap = grasslandMap;
			cityColor = MapGenColors.GRASSLANDCITY;
			break;
		case JUNGLE:
			spawnMap = jungleSpawnMap;
			terrainMap = jungleMap;
			cityColor = MapGenColors.JUNGLECITY;
			break;
		case MARSH:
			spawnMap = marshSpawnMap;
			terrainMap = marshMap;
			cityColor = MapGenColors.MARSHCITY;
			break;
		case OCEAN:
			break;
		case SAVANNAH:
			spawnMap = savannahSpawnMap;
			terrainMap = savannahMap;
			cityColor = MapGenColors.SAVANNAHCITY;
			break;
		case SNOW:
			spawnMap = snowSpawnMap;
			terrainMap = snowMap;
			cityColor = MapGenColors.SNOWCITY;
			break;
		case TAIGA:
			spawnMap = taigaSpawnMap;
			terrainMap = taigaMap;
			cityColor = MapGenColors.TAIGACITY;
			break;
		case TUNDRA:
			spawnMap = tundraSpawnMap;
			terrainMap = tundraMap;
			cityColor = MapGenColors.TUNDRACITY;
			break;
		}

		List<Point> mapKeysAsArray = new ArrayList<Point>(spawnMap.keySet());
		Random genRandomPoint = new Random();
		Point randomPoint = new Point(-1,-1);
		List<Point> adjacentPoints = new ArrayList<Point>();
		randomPoint = mapKeysAsArray.get(genRandomPoint.nextInt(mapKeysAsArray.size()));

		adjacentPoints = getAdjacentPoints(randomPoint);

		HexTile citySpawn = spawnMap.get(randomPoint);
		citySpawn.setFillColor(Color.BLACK);
		citySpawn.setGridColor(MapGenColors.CITYGRID);
		//board[randomPoint.x][randomPoint.y] = (int)'C';
		spawnMap.remove(randomPoint);
		terrainMap.remove(randomPoint);
		for(Point p : adjacentPoints){
			HexTile tileAtPoint = hexMap.get(p);
			tileAtPoint.setFillColor(tileAtPoint.getCityColor());
			tileAtPoint.setGridColor(MapGenColors.CITYGRID);
			List<Point> secondAdjacentPoints = getAdjacentPoints(p);
			// Get rid of points already processed
			List<Point> markedForDelete = new ArrayList<Point>();
			for(Point p2 : secondAdjacentPoints){
				if(adjacentPoints.contains(p2)){
					markedForDelete.add(p2);
				}
			}
			for(Point MFDp : markedForDelete){
				secondAdjacentPoints.remove(MFDp);
			}
			// Process second layer adjacent points
			for(Point p1 : secondAdjacentPoints){
				if(oceanMap.containsKey(p1)){
					//oceanMap.get(p1).setFillColor(OCEANCITY);
					oceanMap.get(p1).setGridColor(MapGenColors.CITYGRID);
					oceanMap.remove(p1);
				} else if(freshwaterMap.containsKey(p1)){
					//freshwaterMap.get(p1).setFillColor(FRESHWATERCITY);
					freshwaterMap.get(p1).setGridColor(MapGenColors.CITYGRID);
					freshwaterMap.remove(p1);
				} else if(grasslandMap.containsKey(p1)){
					//grasslandMap.get(p1).setFillColor(GRASSLANDCITY);
					grasslandMap.get(p1).setGridColor(MapGenColors.CITYGRID);
					grasslandMap.remove(p1);
				}  else if(forestMap.containsKey(p1)){
					//forestMap.get(p1).setFillColor(FORESTCITY);
					forestMap.get(p1).setGridColor(MapGenColors.CITYGRID);
					forestMap.remove(p1);
				}  else if(taigaMap.containsKey(p1)){
					//taigaMap.get(p1).setFillColor(TAIGACITY);
					taigaMap.get(p1).setGridColor(MapGenColors.CITYGRID);
					taigaMap.remove(p1);
				}  else if(jungleMap.containsKey(p1)){
					//jungleMap.get(p1).setFillColor(JUNGLECITY);
					jungleMap.get(p1).setGridColor(MapGenColors.CITYGRID);
					jungleMap.remove(p1);
				}  else if(savannahMap.containsKey(p1)){
					//savannahMap.get(p1).setFillColor(SAVANNAHCITY);
					savannahMap.get(p1).setGridColor(MapGenColors.CITYGRID);
					savannahMap.remove(p1);
				}  else if(desertMap.containsKey(p1)){
					//desertMap.get(p1).setFillColor(DESERTCITY);
					desertMap.get(p1).setGridColor(MapGenColors.CITYGRID);
					desertMap.remove(p1);
				}  else if(tundraMap.containsKey(p1)){
					//tundraMap.get(p1).setFillColor(TUNDRACITY);
					tundraMap.get(p1).setGridColor(MapGenColors.CITYGRID);
					tundraMap.remove(p1);
				}  else if(snowMap.containsKey(p1)){
					//snowMap.get(p1).setFillColor(SNOWCITY);
					snowMap.get(p1).setGridColor(MapGenColors.CITYGRID);
					snowMap.remove(p1);
				}  else if(marshMap.containsKey(p1)){
					//marshMap.get(p1).setFillColor(MARSHCITY);
					marshMap.get(p1).setGridColor(MapGenColors.CITYGRID);
					marshMap.remove(p1);
				}
			}
			spawnMap.remove(p);
		}

		String terrainName = terrain.toString().toLowerCase().substring(0, 1).toUpperCase() + terrain.toString().toLowerCase().substring(1);
		System.out.println(terrainName + " City Spawned at point: " + randomPoint.x + ", " + randomPoint.y);
		//		panel.repaint();
	}

	public static void findCoastlines(){

		ArrayList<HashMap<Point, HexTile>> terrainMapList = new ArrayList<HashMap<Point, HexTile>>();

		terrainMapList.add(desertMap);
		terrainMapList.add(forestMap);
		terrainMapList.add(grasslandMap);
		terrainMapList.add(jungleMap);
		terrainMapList.add(marshMap);
		terrainMapList.add(savannahMap);
		terrainMapList.add(taigaMap);
		terrainMapList.add(tundraMap);

		for(HashMap<Point, HexTile> map : terrainMapList){
			for(Point point : map.keySet()){
				List<Point> adjacentPoints = getAdjacentPoints(point);
				for(Point p : adjacentPoints){
					if(oceanMap.containsKey(p)){
						HexTile coastTile = oceanMap.get(p);
						coastTile.setTerrain(Terrain.COAST);
						coastMap.put(p, coastTile);
						oceanMap.remove(p);
					}
				}			
			}
		}
		//		panel.repaint();
	}

	public static void widenCoastlines(){

		HashMap<Point, HexTile> newCoastMap = new HashMap<Point, HexTile>();
		newCoastMap.putAll(coastMap);

		for(Point point : coastMap.keySet()){
			Random widenChanceGen = new Random();
			int widenChance = widenChanceGen.nextInt(5);
			if(widenChance <= 1){
				List<Point> adjacentPoints = getAdjacentPoints(point);
				for(Point p : adjacentPoints){
					if(oceanMap.containsKey(p)){
						HexTile coastTile = oceanMap.get(p);
						coastTile.setTerrain(Terrain.COAST);
						newCoastMap.put(p, coastTile);
						oceanMap.remove(p);
					}
				}
			}
		}

		coastMap = newCoastMap;
	}

	public static void findMountains(){

		ArrayList<HashMap<Point, HexTile>> terrainMapList = new ArrayList<HashMap<Point, HexTile>>();

		terrainMapList.add(desertMap);
		terrainMapList.add(forestMap);
		terrainMapList.add(grasslandMap);
		terrainMapList.add(jungleMap);
		terrainMapList.add(savannahMap);
		terrainMapList.add(taigaMap);

		for(HashMap<Point, HexTile> map : terrainMapList){
			for(Point point : map.keySet()){
				List<Point> adjacentPoints = getAdjacentPoints(point);
				for(Point p : adjacentPoints){
					if(desertMap.containsKey(p)){
						Random rand = new Random();
						int mountainChance = rand.nextInt(MOUNTAINCHANCE);
						if(mountainChance <= 1){
							desertMap.get(p).setElevation(Elevation.mountain);
							if(desertSpawnMap.containsKey(p)){
								desertSpawnMap.remove(p);
							}
						}
					} else if(forestMap.containsKey(p)){
						Random rand = new Random();
						int mountainChance = rand.nextInt(MOUNTAINCHANCE);
						if(mountainChance <= 1){
							forestMap.get(p).setElevation(Elevation.mountain);
							if(forestSpawnMap.containsKey(p)){
								forestSpawnMap.remove(p);
							}
						}
					} else if(grasslandMap.containsKey(p)){
						Random rand = new Random();
						int mountainChance = rand.nextInt(MOUNTAINCHANCE);
						if(mountainChance <= 1){
							grasslandMap.get(p).setElevation(Elevation.mountain);
							if(grasslandSpawnMap.containsKey(p)){
								grasslandSpawnMap.remove(p);
							}
						}
					} else if(jungleMap.containsKey(p)){
						Random rand = new Random();
						int mountainChance = rand.nextInt(MOUNTAINCHANCE);
						if(mountainChance <= 1){
							jungleMap.get(p).setElevation(Elevation.mountain);
							if(jungleSpawnMap.containsKey(p)){
								jungleSpawnMap.remove(p);
							}
						}
					} else if(savannahMap.containsKey(p)){
						Random rand = new Random();
						int mountainChance = rand.nextInt(MOUNTAINCHANCE);
						if(mountainChance <= 1){
							savannahMap.get(p).setElevation(Elevation.mountain);
							if(savannahSpawnMap.containsKey(p)){
								savannahSpawnMap.remove(p);
							}
						}
					} else if(taigaMap.containsKey(p)){
						Random rand = new Random();
						int mountainChance = rand.nextInt(MOUNTAINCHANCE);
						if(mountainChance <= 1){
							taigaMap.get(p).setElevation(Elevation.mountain);
							if(taigaSpawnMap.containsKey(p)){
								taigaSpawnMap.remove(p);
							}
						}
					}
				}			
			}
		}
		//		panel.repaint();
	}

	@FXML
	public void saveMapAsPNG() {

		WritableImage image = mainVBox.snapshot(new SnapshotParameters(), null);
		BufferedImage bImage = SwingFXUtils.fromFXImage(image, null);
		long systemTime = System.currentTimeMillis();

		try {
			if (ImageIO.write(bImage, "png", new File("C:/Users/rmelvil9/Documents/Saved Hex Maps/map" + systemTime + ".png")))
			{
				System.out.println("-- saved");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void clearBoard(String[][] board){
		for(int i=0; i<BHEIGHT; i++){
			for(int j=0; j<BWIDTH; j++){
				board[i][j] = "";
			}
		}
	}

	public static ArrayList<Integer> getTotals(){
		ArrayList<Integer> totals = new ArrayList<Integer>();

		totals.add(coastTotal);
		totals.add(desertTotal);
		totals.add(forestTotal);
		totals.add(freshwaterTotal);
		totals.add(grasslandTotal);
		totals.add(jungleTotal);
		totals.add(marshTotal);
		totals.add(oceanTotal);
		totals.add(savannahTotal);
		totals.add(snowTotal);
		totals.add(taigaTotal);
		totals.add(tundraTotal);

		return totals;
	}

	@FXML
	public void toggleGrid(ActionEvent event){

		double strokeWidth = 0.0;

		if(showGridCheckMenuItem.isSelected()){
			strokeWidth = 1.0;
		} else {
			strokeWidth = 0.0;
		}

		for(Node node : getTilegroup().getChildren()){
			Polygon hexagon = (Polygon) node;
			hexagon.setStrokeWidth(strokeWidth);
		}
	}

	public MapGeneratorMain getMapGeneratorMain() {
		return mapGeneratorMain;
	}

	public void setMapGeneratorMain(MapGeneratorMain mapGeneratorMain) {
		this.mapGeneratorMain = mapGeneratorMain;
	}

	public Group getTilegroup() {
		return tilegroup;
	}

	public void setTilegroup(Group tilegroup) {
		this.tilegroup = tilegroup;
	}

	public Group getGuiGroup() {
		return guiGroup;
	}

	public void setGuiGroup(Group guiGroup) {
		this.guiGroup = guiGroup;
	}
}
