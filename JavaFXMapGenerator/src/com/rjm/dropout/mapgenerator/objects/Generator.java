package com.rjm.dropout.mapgenerator.objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.rjm.dropout.mapgenerator.enums.ContinentSize;
import com.rjm.dropout.mapgenerator.enums.Elevation;
import com.rjm.dropout.mapgenerator.enums.ResourceAnimals;
import com.rjm.dropout.mapgenerator.enums.ResourceFruits;
import com.rjm.dropout.mapgenerator.enums.Terrain;
import com.rjm.dropout.mapgenerator.enums.WorldAge;
import com.rjm.dropout.mapgenerator.graphics.MapGenTextures;
import com.rjm.dropout.utilities.FXMLUtils;
import com.rjm.dropout.utilities.MapGeneratorMain;
import com.rjm.dropout.utilities.MapGeneratorUtility;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;

public class Generator {

	MapGeneratorMain mapGeneratorMain;
	MapGeneratorUtility mapGeneratorUtility;
	ContinentSize continentSize;
	WorldAge worldAge;
	String[][] board;

	int numContinents = 0;
	int coastTotal = 0, desertTotal = 0, forestTotal = 0, freshwaterTotal = 0, grasslandTotal = 0, jungleTotal = 0, marshTotal = 0, oceanTotal = 0, savannahTotal = 0, snowTotal = 0, taigaTotal = 0, tundraTotal = 0, allTerrainTotal = 0;

	public Generator(MapGeneratorUtility mapGeneratorUtility, ContinentSize size, WorldAge age, String[][] board){	
		this.mapGeneratorUtility = mapGeneratorUtility;
		this.mapGeneratorMain = MapGeneratorMain.getInstance();
		this.board = board;
		this.continentSize = size;
		this.worldAge = age;
		switch(size){
		case HUGE:
			numContinents = 8;
			break;
		case LARGE:
			numContinents = 12;
			break;
		case MEDIUM:
			numContinents = 16;
			break;
		case SMALL:
			numContinents = 24;
			break;
		}
	}

	public List<HexTile> setup(){

		List<HexTile> hexTiles = new ArrayList<HexTile>();

		for (int i = 0; i < MapGeneratorUtility.BHEIGHT; i++) {
			for (int j = 0; j < MapGeneratorUtility.BWIDTH; j++) {
				HexTile hexTile = createHexTile(Terrain.OCEAN, Elevation.flat, j, i);
				hexTiles.add(hexTile);

				Point point = new Point(j, i);

				MapGeneratorUtility.oceanMap.put(point, hexTile);
				MapGeneratorUtility.hexMap.put(point, hexTile);
			}
		}

		while(numContinents > 0){
			numContinents = generateContinent(this.continentSize, this.worldAge, numContinents);
		}
		
//		placeResources(this.board);
//		
//		for (int i=0;i<MapGeneratorUtility.BHEIGHT;i++) {
//			for (int j=0;j<MapGeneratorUtility.BWIDTH;j++) {
//				showResources(j,i,this.board[i][j]);
//			}
//		}

		return hexTiles;
	}

	public HexTile createHexTile(Terrain terrain, Elevation elevation, int x, int y){

		boolean xIsEven = (x%2 == 0) ? true : false;

		// Setup City Generator Utility Application

		HexTile hex = null;

		try {
			FXMLLoader fxmlLoader = new FXMLLoader(
					getClass().getClassLoader().getResource(FXMLUtils.FXML_HEX_TILE));

			fxmlLoader.load(getClass().getClassLoader().getResourceAsStream(FXMLUtils.FXML_HEX_TILE));

			hex = fxmlLoader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}

		hex.setup(terrain, elevation);

		double newX = 0.0;
		double newY = 0.0;

		newX = 90.0 * x;

		if(xIsEven){
			newY = 100.0 * y;
		} else if (!xIsEven){
			newY = (100.0 * y)+50;
		}

		hex.setLocation(newX, newY);

		return hex;
	}

	public int generateContinent(ContinentSize size, WorldAge age, int numContinents){
		/* Map Generation Heuristics
		 *  - Jungle & Desert must be between tropics
		 *  - Savannah must start between tropics, but can continue until taiga
		 *  - Grassland & Marsh can start anywhere from equator to taiga, but can't continue into taiga
		 *  - Temperate Forest must start between taigas, but can continue until tundra
		 */

		// Default land/water ratio => 30/70 percent

		double equator = MapGeneratorUtility.BHEIGHT/2;

		//		int arcticN = (int)Math.ceil(equator - (((0.64*80)/100)*equator)); //       /---------------\
		//		int tundraN = (int)Math.ceil(equator - (((0.58*80)/100)*equator)); //      /-----------------\
		//		int taigaN = (int)Math.ceil(equator - (((0.44*80)/100)*equator));  //	  /-------------------\
		//		int tropicN = (int)Math.ceil(equator - (((0.24*80)/100)*equator)); //	 /---------------------\
		//		// equator											 			   //	|=======================|
		//		int tropicS = (int)Math.ceil(equator - (((-0.24*80)/100)*equator));//	 \---------------------/
		//		int taigaS = (int)Math.ceil(equator - (((-0.56*80)/100)*equator)); //	  \-------------------/
		//		int tundraS = (int)Math.ceil(equator - (((-0.64*80)/100)*equator));//      \-----------------/
		//		int arcticS = (int)Math.ceil(equator - (((-0.72*80)/100)*equator));//       \---------------/

		int arcticN = (int)Math.ceil(equator - (((0.79*80)/100)*equator)); //       /---------------\
		int tundraN = (int)Math.ceil(equator - (((0.64*80)/100)*equator)); //      /-----------------\
		int taigaN = (int)Math.ceil(equator - (((0.56*80)/100)*equator));  //	  /-------------------\
		int tropicN = (int)Math.ceil(equator - (((0.24*80)/100)*equator)); //	 /---------------------\
		// equator											 			   //	|=======================|
		int tropicS = (int)Math.ceil(equator - (((-0.24*80)/100)*equator));//	 \---------------------/
		int taigaS = (int)Math.ceil(equator - (((-0.56*80)/100)*equator)); //	  \-------------------/
		int tundraS = (int)Math.ceil(equator - (((-0.64*80)/100)*equator));//      \-----------------/
		int arcticS = (int)Math.ceil(equator - (((-0.79*80)/100)*equator));//       \---------------/

		System.out.println("Arctic North: " + arcticN);
		System.out.println("Tundra North: " + tundraN);
		System.out.println("Taiga North: " + taigaN);
		System.out.println("Tropic North: " + tropicN);
		System.out.println("Equator: " + equator);
		System.out.println("Tropic South: " + tropicS);
		System.out.println("Taiga South: " + taigaS);
		System.out.println("Tundra South: " + tundraS);
		System.out.println("Arctic South: " + arcticS);
		System.out.println();

		switch(age){
		case FiveBillion:
			MapGeneratorUtility.MOUNTAINCHANCE = 25;
			break;
		case FourBillion:
			MapGeneratorUtility.MOUNTAINCHANCE = 50;
			break;
		case ThreeBillion:
			MapGeneratorUtility.MOUNTAINCHANCE = 75;
			break;
		default:
			MapGeneratorUtility.MOUNTAINCHANCE = 50;
			break;
		}

		int cycles = numContinents;
		while(cycles > 0){
			Random rand = new Random();

			int landMin = 0;
			int landMax = 100;

			switch(age){
			case ThreeBillion:
				landMin = 40;
				landMax = 50;
				break;
			case FourBillion:
				landMin = 30;
				landMax = 40;
				break;
			case FiveBillion:
				landMin = 20;
				landMax = 30;
				break;
			}

			int landAreaPercent = rand.nextInt(landMax-landMin) + landMin;
			double landArea = (double)landAreaPercent / 100;
			int totalTiles = MapGeneratorUtility.BWIDTH * MapGeneratorUtility.BHEIGHT; System.out.println("Total Map Tiles: " + totalTiles);
			int totalLandTiles = (int)Math.ceil(totalTiles * landArea); System.out.println("Total Land Tiles: " + totalLandTiles);
			int i = rand.nextInt(MapGeneratorUtility.BWIDTH);
			int j = rand.nextInt(MapGeneratorUtility.BHEIGHT);
			Point point = new Point(i,j);
			HexTile hexTileAtPoint = MapGeneratorUtility.hexMap.get(point);

			/*while(!hexTileAtPoint.getColor().equals(OCEAN)){
			i = rand.nextInt(MapGeneratorUtility.BWIDTH);
			j = rand.nextInt(MapGeneratorUtility.BHEIGHT);
		}*/

			Point continentOrigin = point;
			Point previousPoint = point;
			Point currentPoint = point;

			/* Size Heurisitics
			 *  - SMALL: 5 - 11 percent of total land area
			 *  - MEDIUM: 12 - 24 percent of total land area
			 *  - LARGE: 25 - 35 percent of total land area
			 *  - HUGE: 36 - 50 percent of total land area
			 */
			int continentSize = totalLandTiles;
			Random sizeRand = new Random();
			int sizeRandInt = 0;
			double sizePercent = 0.0;
			switch(size){
			case HUGE:
				sizeRandInt = sizeRand.nextInt(15) + 36;
				sizePercent = (double)sizeRandInt / 100;
				continentSize = (int)Math.ceil(totalLandTiles * sizePercent);
				System.out.println("Continent Size (in tiles): " + continentSize);
				System.out.println();
				break;
			case LARGE:
				sizeRandInt = sizeRand.nextInt(11) + 25;
				sizePercent = (double)sizeRandInt / 100;
				continentSize = (int)Math.ceil(totalLandTiles * sizePercent);
				System.out.println("Continent Size (in tiles): " + continentSize);
				System.out.println();
				break;
			case MEDIUM:
				sizeRandInt = sizeRand.nextInt(13) + 12;
				sizePercent = (double)sizeRandInt / 100;
				continentSize = (int)Math.ceil(totalLandTiles * sizePercent);
				System.out.println("Continent Size (in tiles): " + continentSize);
				System.out.println();
				break;
			case SMALL:
				sizeRandInt = sizeRand.nextInt(7) + 5;
				sizePercent = (double)sizeRandInt / 100;
				continentSize = (int)Math.ceil(totalLandTiles * sizePercent);
				System.out.println("Continent Size (in tiles): " + continentSize);
				System.out.println();
				break;
			}
			int biomeWeight = MapGeneratorUtility.BIOMEWEIGHT;
			boolean firstTile = true;
			while(continentSize > 0){
				HexTile hexTileAtPreviousPoint = hexTileAtPoint;
				hexTileAtPoint = MapGeneratorUtility.hexMap.get(currentPoint);

				Random sameBiome = new Random();
				double biomeChance = sameBiome.nextInt(10);

				if(biomeChance < biomeWeight && !firstTile){

					// Remove the tile being replaced from its previous Map
					Terrain terrainToReplace = hexTileAtPoint.getTerrain();
					switch(terrainToReplace){
					case DESERT:
						desertTotal--;
						MapGeneratorUtility.desertMap.remove(currentPoint);
						break;
					case FOREST:
						forestTotal--;
						MapGeneratorUtility.forestMap.remove(currentPoint);
						break;
					case FRESHWATER:
						freshwaterTotal--;
						MapGeneratorUtility.freshwaterMap.remove(currentPoint);
						break;
					case GRASSLAND:
						grasslandTotal--;
						MapGeneratorUtility.grasslandMap.remove(currentPoint);
						break;
					case JUNGLE:
						jungleTotal--;
						MapGeneratorUtility.jungleMap.remove(currentPoint);
						break;
					case MARSH:
						marshTotal--;
						MapGeneratorUtility.marshMap.remove(currentPoint);
						break;
					case OCEAN:
						oceanTotal--;
						MapGeneratorUtility.oceanMap.remove(currentPoint);
						break;
					case SAVANNAH:
						savannahTotal--;
						MapGeneratorUtility.savannahMap.remove(currentPoint);
						break;
					case SNOW:
						snowTotal--;
						MapGeneratorUtility.snowMap.remove(currentPoint);
						break;
					case TAIGA:
						taigaTotal--;
						MapGeneratorUtility.taigaMap.remove(currentPoint);
						break;
					case TUNDRA:
						tundraTotal--;
						MapGeneratorUtility.tundraMap.remove(currentPoint);
						break;
					case COAST:
						coastTotal--;
						MapGeneratorUtility.coastMap.remove(currentPoint);
						break;
					default:
						break;					
					}

					// Add tile to new map
					Terrain terrain = hexTileAtPreviousPoint.getTerrain();
					switch(terrain){
					case DESERT:
						if(!MapGeneratorUtility.desertMap.containsKey(currentPoint)){
							desertTotal++;
							MapGeneratorUtility.desertMap.put(currentPoint, hexTileAtPoint);
						}
						break;
					case FOREST:
						if(!MapGeneratorUtility.forestMap.containsKey(currentPoint)){
							forestTotal++;
							MapGeneratorUtility.forestMap.put(currentPoint, hexTileAtPoint);
						}
						break;
					case FRESHWATER:
						if(!MapGeneratorUtility.freshwaterMap.containsKey(currentPoint)){
							freshwaterTotal++;
							MapGeneratorUtility.freshwaterMap.put(currentPoint, hexTileAtPoint);
						}
						break;
					case GRASSLAND:
						if(!MapGeneratorUtility.grasslandMap.containsKey(currentPoint)){
							grasslandTotal++;
							MapGeneratorUtility.grasslandMap.put(currentPoint, hexTileAtPoint);
						}
						break;
					case JUNGLE:
						if(!MapGeneratorUtility.jungleMap.containsKey(currentPoint)){
							jungleTotal++;
							MapGeneratorUtility.jungleMap.put(currentPoint, hexTileAtPoint);
						}
						break;
					case MARSH:
						if(!MapGeneratorUtility.marshMap.containsKey(currentPoint)){
							marshTotal++;
							MapGeneratorUtility.marshMap.put(currentPoint, hexTileAtPoint);
						}
						break;
					case OCEAN:
						if(!MapGeneratorUtility.oceanMap.containsKey(currentPoint)){
							oceanTotal++;
							MapGeneratorUtility.oceanMap.put(currentPoint, hexTileAtPoint);
						}
						break;
					case SAVANNAH:
						if(!MapGeneratorUtility.savannahMap.containsKey(currentPoint)){
							savannahTotal++;
							MapGeneratorUtility.savannahMap.put(currentPoint, hexTileAtPoint);
						}
						break;
					case SNOW:
						if(!MapGeneratorUtility.snowMap.containsKey(currentPoint)){
							snowTotal++;
							MapGeneratorUtility.snowMap.put(currentPoint, hexTileAtPoint);
						}
						break;
					case TAIGA:
						if(!MapGeneratorUtility.taigaMap.containsKey(currentPoint)){
							taigaTotal++;
							MapGeneratorUtility.taigaMap.put(currentPoint, hexTileAtPoint);
						}
						break;
					case TUNDRA:
						if(!MapGeneratorUtility.tundraMap.containsKey(currentPoint)){
							tundraTotal++;
							MapGeneratorUtility.tundraMap.put(currentPoint, hexTileAtPoint);
						}
						break;
					case COAST:
						if(!MapGeneratorUtility.coastMap.containsKey(currentPoint)){
							coastTotal++;
							MapGeneratorUtility.coastMap.put(currentPoint, hexTileAtPoint);
						}
						break;
					default:
						break;					
					}

					hexTileAtPoint.setTerrain(terrain);
					biomeWeight--;
				} else {
					biomeWeight = MapGeneratorUtility.BIOMEWEIGHT;

					// Remove the tile being replaced from its previous Map
					Terrain terrainToReplace = hexTileAtPoint.getTerrain();
					switch(terrainToReplace){
					case DESERT:
						desertTotal--;
						MapGeneratorUtility.desertMap.remove(currentPoint);
						break;
					case FOREST:
						forestTotal--;
						MapGeneratorUtility.forestMap.remove(currentPoint);
						break;
					case FRESHWATER:
						freshwaterTotal--;
						MapGeneratorUtility.freshwaterMap.remove(currentPoint);
						break;
					case GRASSLAND:
						grasslandTotal--;
						MapGeneratorUtility.grasslandMap.remove(currentPoint);
						break;
					case JUNGLE:
						jungleTotal--;
						MapGeneratorUtility.jungleMap.remove(currentPoint);
						break;
					case MARSH:
						marshTotal--;
						MapGeneratorUtility.marshMap.remove(currentPoint);
						break;
					case OCEAN:
						oceanTotal--;
						MapGeneratorUtility.oceanMap.remove(currentPoint);
						break;
					case SAVANNAH:
						savannahTotal--;
						MapGeneratorUtility.savannahMap.remove(currentPoint);
						break;
					case SNOW:
						snowTotal--;
						MapGeneratorUtility.snowMap.remove(currentPoint);
						break;
					case TAIGA:
						taigaTotal--;
						MapGeneratorUtility.taigaMap.remove(currentPoint);
						break;
					case TUNDRA:
						tundraTotal--;
						MapGeneratorUtility.tundraMap.remove(currentPoint);
						break;
					case COAST:
						coastTotal--;
						MapGeneratorUtility.coastMap.remove(currentPoint);
						break;
					default:
						break;					
					}

					if (tropicN < currentPoint.y && currentPoint.y < tropicS) {
						// If between tropic and equator
						int terrainChoice = rand.nextInt(6);
						//Grassland, Forest, Jungle, Savannah, Desert, Marsh
						switch (terrainChoice) {
						case 0:
							hexTileAtPoint.setTerrain(Terrain.GRASSLAND);
							if(!MapGeneratorUtility.grasslandMap.containsKey(currentPoint)){
								grasslandTotal++;
								MapGeneratorUtility.grasslandMap.put(currentPoint, hexTileAtPoint);
							}
							break;
						case 1:
							hexTileAtPoint.setTerrain(Terrain.FOREST);
							if(!MapGeneratorUtility.forestMap.containsKey(currentPoint)){
								forestTotal++;
								MapGeneratorUtility.forestMap.put(currentPoint, hexTileAtPoint);
							}
							break;
						case 2:
							hexTileAtPoint.setTerrain(Terrain.JUNGLE);
							if(!MapGeneratorUtility.jungleMap.containsKey(currentPoint)){
								jungleTotal++;
								MapGeneratorUtility.jungleMap.put(currentPoint, hexTileAtPoint);
							}
							break;
						case 3:
							hexTileAtPoint.setTerrain(Terrain.SAVANNAH);
							if(!MapGeneratorUtility.savannahMap.containsKey(currentPoint)){
								savannahTotal++;
								MapGeneratorUtility.savannahMap.put(currentPoint, hexTileAtPoint);
							}
							break;
						case 4:
							hexTileAtPoint.setTerrain(Terrain.DESERT);
							if(!MapGeneratorUtility.desertMap.containsKey(currentPoint)){
								desertTotal++;
								MapGeneratorUtility.desertMap.put(currentPoint, hexTileAtPoint);
							}
							break;
						case 5:
							hexTileAtPoint.setTerrain(Terrain.MARSH);
							if(!MapGeneratorUtility.marshMap.containsKey(currentPoint)){
								marshTotal++;
								MapGeneratorUtility.marshMap.put(currentPoint, hexTileAtPoint);
							}
							break;
						}
					} else if ((currentPoint.y <= tropicN && currentPoint.y >= taigaN)
							|| (currentPoint.y >= tropicS && currentPoint.y <= taigaS)) {
						// If between taiga and tropic
						int terrainChoice = rand.nextInt(3);
						//Grassland, Forest, Savannah
						switch (terrainChoice) {
						case 0:
							hexTileAtPoint.setTerrain(Terrain.GRASSLAND);
							if(!MapGeneratorUtility.grasslandMap.containsKey(currentPoint)){
								grasslandTotal++;
								MapGeneratorUtility.grasslandMap.put(currentPoint, hexTileAtPoint);
							}
							break;
						case 1:
							hexTileAtPoint.setTerrain(Terrain.FOREST);
							if(!MapGeneratorUtility.forestMap.containsKey(currentPoint)){
								forestTotal++;
								MapGeneratorUtility.forestMap.put(currentPoint, hexTileAtPoint);
							}
							break;
						case 2:
							hexTileAtPoint.setTerrain(Terrain.SAVANNAH);
							if(!MapGeneratorUtility.savannahMap.containsKey(currentPoint)){
								savannahTotal++;
								MapGeneratorUtility.savannahMap.put(currentPoint, hexTileAtPoint);
							}
							break;
						}
					} else if ((currentPoint.y <= taigaN && currentPoint.y >= tundraN)
							|| (currentPoint.y >= taigaS && currentPoint.y <= tundraS)) {
						// If between tundra and taiga
						hexTileAtPoint.setTerrain(Terrain.TAIGA);
						if(!MapGeneratorUtility.taigaMap.containsKey(currentPoint)){
							taigaTotal++;
							MapGeneratorUtility.taigaMap.put(currentPoint, hexTileAtPoint);
						}
					} else if ((currentPoint.y <= tundraN && currentPoint.y >= arcticN)
							|| (currentPoint.y >= tundraS && currentPoint.y <= arcticS)) {
						// If between arctic and tundra
						hexTileAtPoint.setTerrain(Terrain.TUNDRA);
						if(!MapGeneratorUtility.tundraMap.containsKey(currentPoint)){
							tundraTotal++;
							MapGeneratorUtility.tundraMap.put(currentPoint, hexTileAtPoint);
						}
					} else if ((currentPoint.y <= arcticN && currentPoint.y >= 0)
							|| (currentPoint.y >= arcticS && currentPoint.y <= MapGeneratorUtility.BHEIGHT)) {
						// if between pole and arctic
						hexTileAtPoint.setTerrain(Terrain.SNOW);
						if(!MapGeneratorUtility.snowMap.containsKey(currentPoint)){
							snowTotal++;
							MapGeneratorUtility.snowMap.put(currentPoint, hexTileAtPoint);
						}
					} 

					firstTile = false;
				}

				//System.out.println("Changed tile at coordinate: " + currentPoint.x + "," + currentPoint.y + " to " + hexTileAtPoint.getTerrain().toString());

				previousPoint = currentPoint;

				int newX = currentPoint.x;
				int newY = currentPoint.y;
				double x0or1;
				double y0or1;
				double negOrPosX;
				double negOrPosY;

				x0or1 = Math.random();
				y0or1 = Math.random();
				negOrPosX = Math.random();
				negOrPosY = Math.random();
				if (negOrPosX >= 0.5) {
					if (x0or1 >= 0.5) {
						newX = currentPoint.x + 1;
					} else {
						newX = currentPoint.x + 0;
					}
				} else {
					if (x0or1 >= 0.5) {
						newX = currentPoint.x - 1;
					} else {
						newX = currentPoint.x - 0;
					}
				}
				if (negOrPosY >= 0.5) {
					if (y0or1 >= 0.5) {
						newY = currentPoint.y + 1;
					} else {
						newY = currentPoint.y + 0;
					}
				} else {
					if (y0or1 >= 0.5) {
						newY = currentPoint.y - 1;
					} else {
						newY = currentPoint.y - 0;
					}
				} 

				if(newX < 0){
					newX = MapGeneratorUtility.BWIDTH -1;
				} else if(newX >= MapGeneratorUtility.BWIDTH){
					newX = 0;
				}

				if(newY < 0){
					newY = 0;
				} else if(newY >= MapGeneratorUtility.BHEIGHT){
					newY = MapGeneratorUtility.BHEIGHT -1;
				}

				Point newPoint = new Point(newX, newY);
				currentPoint = newPoint;

				continentSize--;

			}

			cycles--;
		}


		//		MapGeneratorUtility.findCoastlines();
		//		MapGeneratorUtility.widenCoastlines();
		//		MapGeneratorUtility.findMountains();

		return cycles;
	}

	public static void placeResources(String [][] board){

		// Resources - Animals
		HashMap<Point, HexTile> bearMap = MapGeneratorUtility.forestMap;
		HashMap<Point, HexTile> camelMap = MapGeneratorUtility.desertMap;
		HashMap<Point, HexTile> chickenMap = MapGeneratorUtility.grasslandMap;
		HashMap<Point, HexTile> elephantMap = new HashMap<Point, HexTile> (MapGeneratorUtility.savannahMap);
		elephantMap.putAll(MapGeneratorUtility.jungleMap);
		HashMap<Point, HexTile> ferretMap = MapGeneratorUtility.forestMap;
		HashMap<Point, HexTile> foxMap = MapGeneratorUtility.forestMap;
		HashMap<Point, HexTile> goatMap = MapGeneratorUtility.grasslandMap;
		HashMap<Point, HexTile> horseMap = MapGeneratorUtility.grasslandMap;
		HashMap<Point, HexTile> pigMap = MapGeneratorUtility.marshMap;
		HashMap<Point, HexTile> rabbitMap = new HashMap<Point, HexTile> (MapGeneratorUtility.grasslandMap);
		rabbitMap.putAll(MapGeneratorUtility.forestMap);
		HashMap<Point, HexTile> sheepMap = MapGeneratorUtility.grasslandMap;
		HashMap<Point, HexTile> tigerMap = MapGeneratorUtility.jungleMap;

		HashMap<Point, HexTile> seaAnimalMap = MapGeneratorUtility.coastMap;

		// Resources - Fruits
		HashMap<Point, HexTile> blueapricotMap =  new HashMap<Point, HexTile> (MapGeneratorUtility.grasslandMap);
		blueapricotMap.putAll(MapGeneratorUtility.jungleMap);
		HashMap<Point, HexTile> citronMap =  new HashMap<Point, HexTile> (MapGeneratorUtility.forestMap);
		citronMap.putAll(MapGeneratorUtility.grasslandMap);
		HashMap<Point, HexTile> gafferMap =  new HashMap<Point, HexTile> (MapGeneratorUtility.desertMap);
		gafferMap.putAll(MapGeneratorUtility.savannahMap);
		HashMap<Point, HexTile> goremelonMap =  new HashMap<Point, HexTile> (MapGeneratorUtility.grasslandMap);
		goremelonMap.putAll(MapGeneratorUtility.marshMap);
		HashMap<Point, HexTile> mandarinMap =  new HashMap<Point, HexTile> (MapGeneratorUtility.forestMap);
		mandarinMap.putAll(MapGeneratorUtility.jungleMap);
		HashMap<Point, HexTile> pomeloMap =  new HashMap<Point, HexTile> (MapGeneratorUtility.forestMap);
		pomeloMap.putAll(MapGeneratorUtility.marshMap);
		HashMap<Point, HexTile> porumMap = new HashMap<Point, HexTile> (MapGeneratorUtility.forestMap);
		porumMap.putAll(MapGeneratorUtility.jungleMap);
		HashMap<Point, HexTile> uvatMap = new HashMap<Point, HexTile> (MapGeneratorUtility.grasslandMap);
		uvatMap.putAll(MapGeneratorUtility.savannahMap);

		/*
		// Sea small/
		HashMap<Point, HexTile> lionfishMap = coastMap;
		HashMap<Point, HexTile> codMap = coastMap;
		HashMap<Point, HexTile> lobsterMap = coastMap;
		HashMap<Point, HexTile> shellfishMap = coastMap;
		// Sea medium
		HashMap<Point, HexTile> leopardsealMap = coastMap;
		HashMap<Point, HexTile> sharkMap = coastMap;
		HashMap<Point, HexTile> tunaMap = coastMap;
		HashMap<Point, HexTile> dolphinMap = coastMap;
		// Sea large
		HashMap<Point, HexTile> bluewhaleMap = coastMap;
		HashMap<Point, HexTile> mantaMap = coastMap;
		HashMap<Point, HexTile> giantsquidMap = coastMap;
		HashMap<Point, HexTile> nessyMap = coastMap;
		 */

		bearMap = pruneMountains(bearMap);
		camelMap = pruneMountains(camelMap);
		chickenMap = pruneMountains(chickenMap);
		elephantMap = pruneMountains(elephantMap);
		ferretMap = pruneMountains(ferretMap);
		foxMap = pruneMountains(foxMap);
		goatMap = pruneMountains(goatMap);
		horseMap = pruneMountains(horseMap);
		pigMap = pruneMountains(pigMap);
		rabbitMap = pruneMountains(rabbitMap);
		sheepMap = pruneMountains(sheepMap);
		tigerMap = pruneMountains(tigerMap);

		blueapricotMap = pruneMountains(blueapricotMap);
		citronMap = pruneMountains(citronMap);
		gafferMap = pruneMountains(gafferMap);
		goremelonMap = pruneMountains(goremelonMap);
		mandarinMap = pruneMountains(mandarinMap);
		pomeloMap = pruneMountains(pomeloMap);
		porumMap = pruneMountains(porumMap);
		uvatMap = pruneMountains(uvatMap);

		int numBears = (int) Math.ceil(bearMap.size()/15); System.out.println("Number of Bears: " + numBears);
		int numCamels = (int) Math.ceil(camelMap.size()/15); System.out.println("Number of Camels: " + numCamels);
		int numChickens = (int) Math.ceil(chickenMap.size()/15); System.out.println("Number of Chickens: " + numChickens);
		int numElephants = (int) Math.ceil(elephantMap.size()/15); System.out.println("Number of Elephants: " + numElephants);
		int numFerrets = (int) Math.ceil(ferretMap.size()/15); System.out.println("Number of Ferrets: " + numFerrets);
		int numFoxes = (int) Math.ceil(foxMap.size()/15); System.out.println("Number of Foxes: " + numFoxes);
		int numGoats = (int) Math.ceil(goatMap.size()/15); System.out.println("Number of Goats: " + numGoats);
		int numHorses = (int) Math.ceil(horseMap.size()/15); System.out.println("Number of Horses: " + numHorses);
		int numPigs = (int) Math.ceil(pigMap.size()/15); System.out.println("Number of Pigs: " + numPigs);
		int numRabbits = (int) Math.ceil(rabbitMap.size()/15); System.out.println("Number of Rabbits: " + numRabbits);
		int numSheep = (int) Math.ceil(sheepMap.size()/15); System.out.println("Number of Sheep: " + numSheep);
		int numTigers = (int) Math.ceil(tigerMap.size()/15); System.out.println("Number of Tigers: " + numTigers);

		int numSeaAnimals = (int) Math.ceil(seaAnimalMap.size()/5);

		int numBlueapricots = (int) Math.ceil(porumMap.size()/15); System.out.println("Number of Blue Apricots: " + numBlueapricots);
		int numCitrons = (int) Math.ceil(porumMap.size()/15); System.out.println("Number of Citrons: " + numCitrons);
		int numGaffers = (int) Math.ceil(porumMap.size()/15); System.out.println("Number of Gaffers: " + numGaffers);
		int numGoremelons = (int) Math.ceil(porumMap.size()/15); System.out.println("Number of Gore Melons: " + numGoremelons);
		int numMandarins = (int) Math.ceil(porumMap.size()/15); System.out.println("Number of Mandarins: " + numMandarins);
		int numPomelos = (int) Math.ceil(porumMap.size()/15); System.out.println("Number of Pomelos: " + numPomelos);
		int numPorums = (int) Math.ceil(porumMap.size()/15); System.out.println("Number of Porums: " + numPorums);
		int numUvats = (int) Math.ceil(uvatMap.size()/15); System.out.println("Number of Uvats: " + numUvats);

		while(numBears > 0){
			List<Point> mapKeysAsArray = new ArrayList<Point>(bearMap.keySet());
			Random genRandomPoint = new Random();
			Point randomPoint = new Point(-1,-1);
			randomPoint = mapKeysAsArray.get(genRandomPoint.nextInt(mapKeysAsArray.size()));

			new ResourceAnimal(randomPoint, ResourceAnimals.bear, board);
			numBears--;
		}
		while(numCamels > 0){
			List<Point> mapKeysAsArray = new ArrayList<Point>(camelMap.keySet());
			Random genRandomPoint = new Random();
			Point randomPoint = new Point(-1,-1);
			randomPoint = mapKeysAsArray.get(genRandomPoint.nextInt(mapKeysAsArray.size()));

			new ResourceAnimal(randomPoint, ResourceAnimals.camel, board);
			numCamels--;
		}
		while(numChickens > 0){
			List<Point> mapKeysAsArray = new ArrayList<Point>(chickenMap.keySet());
			Random genRandomPoint = new Random();
			Point randomPoint = new Point(-1,-1);
			randomPoint = mapKeysAsArray.get(genRandomPoint.nextInt(mapKeysAsArray.size()));

			new ResourceAnimal(randomPoint, ResourceAnimals.chicken, board);
			numChickens--;
		}
		while(numElephants > 0){
			List<Point> mapKeysAsArray = new ArrayList<Point>(elephantMap.keySet());
			Random genRandomPoint = new Random();
			Point randomPoint = new Point(-1,-1);
			randomPoint = mapKeysAsArray.get(genRandomPoint.nextInt(mapKeysAsArray.size()));

			if(elephantMap.get(randomPoint).getTerrain().equals(Terrain.JUNGLE)){
				new ResourceAnimal(randomPoint, ResourceAnimals.elephantj, board);				
			} else if(elephantMap.get(randomPoint).getTerrain().equals(Terrain.SAVANNAH)){
				new ResourceAnimal(randomPoint, ResourceAnimals.elephants, board);
			}
			numElephants--;
		}
		while(numFerrets > 0){
			List<Point> mapKeysAsArray = new ArrayList<Point>(ferretMap.keySet());
			Random genRandomPoint = new Random();
			Point randomPoint = new Point(-1,-1);
			randomPoint = mapKeysAsArray.get(genRandomPoint.nextInt(mapKeysAsArray.size()));

			new ResourceAnimal(randomPoint, ResourceAnimals.ferret, board);
			numFerrets--;
		}
		while(numFoxes > 0){
			List<Point> mapKeysAsArray = new ArrayList<Point>(foxMap.keySet());
			Random genRandomPoint = new Random();
			Point randomPoint = new Point(-1,-1);
			randomPoint = mapKeysAsArray.get(genRandomPoint.nextInt(mapKeysAsArray.size()));

			new ResourceAnimal(randomPoint, ResourceAnimals.fox, board);
			numFoxes--;
		}
		while(numGoats > 0){
			List<Point> mapKeysAsArray = new ArrayList<Point>(goatMap.keySet());
			Random genRandomPoint = new Random();
			Point randomPoint = new Point(-1,-1);
			randomPoint = mapKeysAsArray.get(genRandomPoint.nextInt(mapKeysAsArray.size()));

			new ResourceAnimal(randomPoint, ResourceAnimals.goat, board);
			numGoats--;
		}
		while(numHorses > 0){
			List<Point> mapKeysAsArray = new ArrayList<Point>(horseMap.keySet());
			Random genRandomPoint = new Random();
			Point randomPoint = new Point(-1,-1);
			randomPoint = mapKeysAsArray.get(genRandomPoint.nextInt(mapKeysAsArray.size()));

			new ResourceAnimal(randomPoint, ResourceAnimals.horse, board);
			numHorses--;
		}
		while(numPigs > 0){
			List<Point> mapKeysAsArray = new ArrayList<Point>(pigMap.keySet());
			Random genRandomPoint = new Random();
			Point randomPoint = new Point(-1,-1);
			randomPoint = mapKeysAsArray.get(genRandomPoint.nextInt(mapKeysAsArray.size()));

			new ResourceAnimal(randomPoint, ResourceAnimals.pig, board);
			numPigs--;
		}
		while(numRabbits > 0){
			List<Point> mapKeysAsArray = new ArrayList<Point>(rabbitMap.keySet());
			Random genRandomPoint = new Random();
			Point randomPoint = new Point(-1,-1);
			randomPoint = mapKeysAsArray.get(genRandomPoint.nextInt(mapKeysAsArray.size()));

			if(rabbitMap.get(randomPoint).getTerrain().equals(Terrain.FOREST)){
				new ResourceAnimal(randomPoint, ResourceAnimals.rabbitf, board);				
			} else if(rabbitMap.get(randomPoint).getTerrain().equals(Terrain.GRASSLAND)){
				new ResourceAnimal(randomPoint, ResourceAnimals.rabbitg, board);
			}

			numRabbits--;
		}
		while(numSheep > 0){
			List<Point> mapKeysAsArray = new ArrayList<Point>(sheepMap.keySet());
			Random genRandomPoint = new Random();
			Point randomPoint = new Point(-1,-1);
			randomPoint = mapKeysAsArray.get(genRandomPoint.nextInt(mapKeysAsArray.size()));

			new ResourceAnimal(randomPoint, ResourceAnimals.sheep, board);
			numSheep--;
		}
		while(numTigers > 0){
			List<Point> mapKeysAsArray = new ArrayList<Point>(tigerMap.keySet());
			Random genRandomPoint = new Random();
			Point randomPoint = new Point(-1,-1);
			randomPoint = mapKeysAsArray.get(genRandomPoint.nextInt(mapKeysAsArray.size()));

			new ResourceAnimal(randomPoint, ResourceAnimals.tiger, board);
			numTigers--;
		}

		while(numSeaAnimals > 0){
			List<Point> mapKeysAsArray = new ArrayList<Point>(seaAnimalMap.keySet());
			Random genRandomPoint = new Random();
			Point randomPoint = new Point(-1,-1);
			randomPoint = mapKeysAsArray.get(genRandomPoint.nextInt(mapKeysAsArray.size()));

			Random genRandomSeaAnimal = new Random();
			int randomSeaAnimal = genRandomSeaAnimal.nextInt(12);

			switch(randomSeaAnimal){
			case 0:
				new ResourceAnimal(randomPoint, ResourceAnimals.bluewhale, board);
				break;
			case 1:
				new ResourceAnimal(randomPoint, ResourceAnimals.cod, board);
				break;
			case 2:
				new ResourceAnimal(randomPoint, ResourceAnimals.dolphin, board);
				break;
			case 3:
				new ResourceAnimal(randomPoint, ResourceAnimals.giantsquid, board);
				break;
			case 4:
				new ResourceAnimal(randomPoint, ResourceAnimals.leopardseal, board);
				break;
			case 5:
				new ResourceAnimal(randomPoint, ResourceAnimals.lionfish, board);
				break;
			case 6:
				new ResourceAnimal(randomPoint, ResourceAnimals.lobster, board);
				break;
			case 7:
				new ResourceAnimal(randomPoint, ResourceAnimals.manta, board);
				break;
			case 8:
				new ResourceAnimal(randomPoint, ResourceAnimals.nessy, board);
				break;
			case 9:
				new ResourceAnimal(randomPoint, ResourceAnimals.shark, board);
				break;
			case 10:
				new ResourceAnimal(randomPoint, ResourceAnimals.shellfish, board);
				break;
			case 11:
				new ResourceAnimal(randomPoint, ResourceAnimals.tuna, board);
				break;

			}
			numSeaAnimals--;
		}

		while(numBlueapricots > 0){
			List<Point> mapKeysAsArray = new ArrayList<Point>(blueapricotMap.keySet());
			Random genRandomPoint = new Random();
			Point randomPoint = new Point(-1,-1);
			randomPoint = mapKeysAsArray.get(genRandomPoint.nextInt(mapKeysAsArray.size()));

			if(blueapricotMap.get(randomPoint).getTerrain().equals(Terrain.GRASSLAND)){
				new ResourceFruit(randomPoint, ResourceFruits.blueapricotg, board);				
			} else if(blueapricotMap.get(randomPoint).getTerrain().equals(Terrain.JUNGLE)){
				new ResourceFruit(randomPoint, ResourceFruits.blueapricotj, board);
			}
			numBlueapricots--;
		}
		while(numCitrons > 0){
			List<Point> mapKeysAsArray = new ArrayList<Point>(citronMap.keySet());
			Random genRandomPoint = new Random();
			Point randomPoint = new Point(-1,-1);
			randomPoint = mapKeysAsArray.get(genRandomPoint.nextInt(mapKeysAsArray.size()));

			if(citronMap.get(randomPoint).getTerrain().equals(Terrain.FOREST)){
				new ResourceFruit(randomPoint, ResourceFruits.citronf, board);				
			} else if(citronMap.get(randomPoint).getTerrain().equals(Terrain.GRASSLAND)){
				new ResourceFruit(randomPoint, ResourceFruits.citrong, board);
			}
			numCitrons--;
		}
		while(numGaffers > 0){
			List<Point> mapKeysAsArray = new ArrayList<Point>(gafferMap.keySet());
			Random genRandomPoint = new Random();
			Point randomPoint = new Point(-1,-1);
			randomPoint = mapKeysAsArray.get(genRandomPoint.nextInt(mapKeysAsArray.size()));

			if(gafferMap.get(randomPoint).getTerrain().equals(Terrain.DESERT)){
				new ResourceFruit(randomPoint, ResourceFruits.gafferd, board);				
			} else if(gafferMap.get(randomPoint).getTerrain().equals(Terrain.SAVANNAH)){
				new ResourceFruit(randomPoint, ResourceFruits.gaffers, board);
			}
			numGaffers--;
		}
		while(numGoremelons > 0){
			List<Point> mapKeysAsArray = new ArrayList<Point>(goremelonMap.keySet());
			Random genRandomPoint = new Random();
			Point randomPoint = new Point(-1,-1);
			randomPoint = mapKeysAsArray.get(genRandomPoint.nextInt(mapKeysAsArray.size()));

			if(goremelonMap.get(randomPoint).getTerrain().equals(Terrain.GRASSLAND)){
				new ResourceFruit(randomPoint, ResourceFruits.goremelong, board);				
			} else if(goremelonMap.get(randomPoint).getTerrain().equals(Terrain.MARSH)){
				new ResourceFruit(randomPoint, ResourceFruits.goremelonm, board);
			}
			numGoremelons--;
		}
		while(numMandarins > 0){
			List<Point> mapKeysAsArray = new ArrayList<Point>(mandarinMap.keySet());
			Random genRandomPoint = new Random();
			Point randomPoint = new Point(-1,-1);
			randomPoint = mapKeysAsArray.get(genRandomPoint.nextInt(mapKeysAsArray.size()));

			if(mandarinMap.get(randomPoint).getTerrain().equals(Terrain.FOREST)){
				new ResourceFruit(randomPoint, ResourceFruits.mandarinf, board);				
			} else if(mandarinMap.get(randomPoint).getTerrain().equals(Terrain.JUNGLE)){
				new ResourceFruit(randomPoint, ResourceFruits.mandarinj, board);
			}
			numMandarins--;
		}
		while(numPomelos > 0){
			List<Point> mapKeysAsArray = new ArrayList<Point>(pomeloMap.keySet());
			Random genRandomPoint = new Random();
			Point randomPoint = new Point(-1,-1);
			randomPoint = mapKeysAsArray.get(genRandomPoint.nextInt(mapKeysAsArray.size()));

			if(pomeloMap.get(randomPoint).getTerrain().equals(Terrain.FOREST)){
				new ResourceFruit(randomPoint, ResourceFruits.pomelof, board);				
			} else if(pomeloMap.get(randomPoint).getTerrain().equals(Terrain.MARSH)){
				new ResourceFruit(randomPoint, ResourceFruits.pomelom, board);
			}
			numPomelos--;
		}
		while(numPorums > 0){
			List<Point> mapKeysAsArray = new ArrayList<Point>(porumMap.keySet());
			Random genRandomPoint = new Random();
			Point randomPoint = new Point(-1,-1);
			randomPoint = mapKeysAsArray.get(genRandomPoint.nextInt(mapKeysAsArray.size()));

			if(porumMap.get(randomPoint).getTerrain().equals(Terrain.FOREST)){
				new ResourceFruit(randomPoint, ResourceFruits.porumf, board);				
			} else if(porumMap.get(randomPoint).getTerrain().equals(Terrain.JUNGLE)){
				new ResourceFruit(randomPoint, ResourceFruits.porumj, board);
			}
			numPorums--;
		}
		while(numUvats > 0){
			List<Point> mapKeysAsArray = new ArrayList<Point>(uvatMap.keySet());
			Random genRandomPoint = new Random();
			Point randomPoint = new Point(-1,-1);
			randomPoint = mapKeysAsArray.get(genRandomPoint.nextInt(mapKeysAsArray.size()));

			if(uvatMap.get(randomPoint).getTerrain().equals(Terrain.GRASSLAND)){
				new ResourceFruit(randomPoint, ResourceFruits.uvatg, board);				
			} else if(uvatMap.get(randomPoint).getTerrain().equals(Terrain.SAVANNAH)){
				new ResourceFruit(randomPoint, ResourceFruits.uvats, board);
			}
			numUvats--;
		}
	}

	static HashMap<Point, HexTile> pruneMountains(HashMap<Point,HexTile> map){
		HashMap<Point, HexTile> newMap = map;
		ArrayList<Point> markedForDelete = new ArrayList<Point>();

		for(Point point : newMap.keySet()){
			if(newMap.get(point).getElevation().equals(Elevation.mountain)){
				markedForDelete.add(point);
			}
		}

		for(Point point : markedForDelete){
			newMap.remove(point);
		}

		return newMap;
	}

	private static int BORDERS=50;	//default number of pixels for the border.
	private static int RESOURCEB=0;

	public void showResources(int x, int y, String text) {

		ImageView resource = new ImageView();

		if(text.equals(ResourceAnimals.bear.toString())){
			resource = new ImageView(MapGenTextures.BEAR);
			//			resource.setLayoutX(x+RESOURCEB);
			//			resource.setLayoutY(y+RESOURCEB);
			//
			//			mapGeneratorUtility.getResourceGroup().getChildren().add(resource);
		} else if(text.equals(ResourceAnimals.bluewhale.toString())){
			resource = new ImageView(MapGenTextures.WHALE);
		} else if(text.equals(ResourceAnimals.camel.toString())){
			resource = new ImageView(MapGenTextures.CAMEL);
		} else if(text.equals(ResourceAnimals.chicken.toString())){
			resource = new ImageView(MapGenTextures.CHICKEN);
		} else if(text.equals(ResourceAnimals.cod.toString())){
			resource = new ImageView(MapGenTextures.COD);
		} else if(text.equals(ResourceAnimals.dolphin.toString())){
			resource = new ImageView(MapGenTextures.DOLPHIN);
		} else if(text.equals(ResourceAnimals.elephantj.toString())){
			resource = new ImageView(MapGenTextures.ELEPHANTjungle);
		} else if(text.equals(ResourceAnimals.elephants.toString())){
			resource = new ImageView(MapGenTextures.ELEPHANTsavannah);
		} else if(text.equals(ResourceAnimals.ferret.toString())){
			resource = new ImageView(MapGenTextures.FERRET);
		} else if(text.equals(ResourceAnimals.fox.toString())){
			resource = new ImageView(MapGenTextures.FOX);
		} else if(text.equals(ResourceAnimals.giantsquid.toString())){
			resource = new ImageView(MapGenTextures.SQUID);
		} else if(text.equals(ResourceAnimals.goat.toString())){
			resource = new ImageView(MapGenTextures.GOAT);
		} else if(text.equals(ResourceAnimals.horse.toString())){
			resource = new ImageView(MapGenTextures.HORSE);
		} else if(text.equals(ResourceAnimals.leopardseal.toString())){
			resource = new ImageView(MapGenTextures.LEOPARDSEAL);
		} else if(text.equals(ResourceAnimals.lionfish.toString())){
			resource = new ImageView(MapGenTextures.LIONFISH);
		} else if(text.equals(ResourceAnimals.lobster.toString())){
			resource = new ImageView(MapGenTextures.LOBSTER);
		} else if(text.equals(ResourceAnimals.manta.toString())){
			resource = new ImageView(MapGenTextures.MANTA);
		} else if(text.equals(ResourceAnimals.nessy.toString())){
			resource = new ImageView(MapGenTextures.NESSY);
		} else if(text.equals(ResourceAnimals.pig.toString())){
			resource = new ImageView(MapGenTextures.PIG);
		} else if(text.equals(ResourceAnimals.rabbitf.toString())){
			resource = new ImageView(MapGenTextures.RABBITforest);
		} else if(text.equals(ResourceAnimals.rabbitg.toString())){
			resource = new ImageView(MapGenTextures.RABBITgrassland);
		} else if(text.equals(ResourceAnimals.shark.toString())){
			resource = new ImageView(MapGenTextures.SHARK);
		} else if(text.equals(ResourceAnimals.sheep.toString())){
			resource = new ImageView(MapGenTextures.SHEEP);
		} else if(text.equals(ResourceAnimals.shellfish.toString())){
			resource = new ImageView(MapGenTextures.SHELLFISH);
		} else if(text.equals(ResourceAnimals.tiger.toString())){
			resource = new ImageView(MapGenTextures.TIGER);
		} else if(text.equals(ResourceAnimals.tuna.toString())){
			resource = new ImageView(MapGenTextures.TUNA);
		}

		else if(text.equals(ResourceFruits.blueapricotg.toString())){
			resource = new ImageView(MapGenTextures.BLUEAPRICOTgrassland);
		} else if(text.equals(ResourceFruits.blueapricotj.toString())){
			resource = new ImageView(MapGenTextures.BLUEAPRICOTjungle);
		} else if(text.equals(ResourceFruits.citronf.toString())){
			resource = new ImageView(MapGenTextures.CITRONforest);
		} else if(text.equals(ResourceFruits.citrong.toString())){
			resource = new ImageView(MapGenTextures.CITRONgrassland);
		} else if(text.equals(ResourceFruits.gafferd.toString())){
			resource = new ImageView(MapGenTextures.GAFFERdesert);
		} else if(text.equals(ResourceFruits.gaffers.toString())){
			resource = new ImageView(MapGenTextures.GAFFERsavannah);
		} else if(text.equals(ResourceFruits.goremelong.toString())){
			resource = new ImageView(MapGenTextures.GOREMELONgrassland);
		} else if(text.equals(ResourceFruits.goremelonm.toString())){
			resource = new ImageView(MapGenTextures.GOREMELONmarsh);
		} else if(text.equals(ResourceFruits.mandarinf.toString())){
			resource = new ImageView(MapGenTextures.MANDARINforest);
		} else if(text.equals(ResourceFruits.mandarinj.toString())){
			resource = new ImageView(MapGenTextures.MANDARINjungle);
		} else if(text.equals(ResourceFruits.pomelof.toString())){
			resource = new ImageView(MapGenTextures.POMELOforest);
		} else if(text.equals(ResourceFruits.pomelom.toString())){
			resource = new ImageView(MapGenTextures.POMELOmarsh);
		} else if(text.equals(ResourceFruits.porumf.toString())){
			resource = new ImageView(MapGenTextures.PORUMforest);
		} else if(text.equals(ResourceFruits.porumj.toString())){
			resource = new ImageView(MapGenTextures.PORUMjungle);
		} else if(text.equals(ResourceFruits.uvatg.toString())){
			resource = new ImageView(MapGenTextures.UVATgrassland);
		} else if(text.equals(ResourceFruits.uvats.toString())){
			resource = new ImageView(MapGenTextures.UVATsavannah);
		}

		resource.setLayoutX(x+RESOURCEB);
		resource.setLayoutY(y+RESOURCEB);

//		mapGeneratorUtility.getResourceGroup().getChildren().add(resource);
	}
}