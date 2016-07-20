package com.rjm.dropout.mapgenerator.objects;

import com.rjm.dropout.mapgenerator.enums.Elevation;
import com.rjm.dropout.mapgenerator.enums.Terrain;
import com.rjm.dropout.mapgenerator.graphics.MapGenColors;
import com.rjm.dropout.mapgenerator.graphics.MapGenTextures;

import javafx.fxml.FXML;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;

public class HexTile {
	
	double x, y = 0.0;
	
	private Color color = null;
	private Color cityColor = null;
	private Terrain terrain = null;
	private Color grid = null;
	private Elevation elevation = null;
	private Color mountainColor = null;
	private Image texture = null;
	
	boolean explored = false;

    @FXML // fx:id="hexagon"
    private Polygon hexagon; // Value injected by FXMLLoader

    @FXML
    void mouseClicked(MouseEvent event) {
    	System.out.println("[" + this.x + "," + this.y + "]");
    }

    @FXML
    void mouseEntered(MouseEvent event) {
    	hexagon.setEffect(new Glow());
    }

    @FXML
    void mouseExited(MouseEvent event) {
    	hexagon.setEffect(null);
    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        
    }

	public void setup(Terrain terrain, Elevation elevation) {
		this.setTerrain(terrain);
		this.setElevation(elevation);
		setColor();
	}
	
	public void setLocation(double x, double y){
		this.x = x;
		this.y = y;
		this.getHexagon().relocate(x, y);
	}

	public Color getFillColor() {
		return color;
	}
	
	public Color getGridColor() {
		return grid;
	}

	public void setColor() {
		switch (this.terrain) {
		case COAST:
			this.color = MapGenColors.COAST;
			this.cityColor = MapGenColors.OCEANCITY;
			this.setMountainColor(Color.GRAY);
			this.setTexture(MapGenTextures.COASTTEXTURE);
			break;
			
		case OCEAN:
			this.color = MapGenColors.OCEAN;
			this.cityColor = MapGenColors.OCEANCITY;
			this.setMountainColor(Color.GRAY);
			this.setTexture(MapGenTextures.OCEANTEXTURE);
			break;
		
		case FRESHWATER:
			this.color = MapGenColors.FRESHWATER;
			this.cityColor = MapGenColors.FRESHWATERCITY;
			this.setMountainColor(Color.GRAY);
			this.setTexture(null);
			break;

		case GRASSLAND:
			this.color = MapGenColors.GRASSLAND;
			this.cityColor = MapGenColors.GRASSLANDCITY;
			this.setMountainColor(MapGenColors.GRASSLANDMTN);
			this.setTexture(MapGenTextures.GRASSLANDTEXTURE);
			break;

		case FOREST:
			this.color = MapGenColors.FOREST;
			this.cityColor = MapGenColors.FORESTCITY;
			this.setMountainColor(MapGenColors.FORESTMTN);
			this.setTexture(MapGenTextures.FORESTTEXTURE);
			break;

		case TAIGA:
			this.color = MapGenColors.TAIGA;
			this.cityColor = MapGenColors.TAIGACITY;
			this.setMountainColor(MapGenColors.TAIGAMTN);
			this.setTexture(MapGenTextures.TAIGATEXTURE);
			break;

		case JUNGLE:
			this.color = MapGenColors.JUNGLE;
			this.cityColor = MapGenColors.JUNGLECITY;
			this.setMountainColor(MapGenColors.JUNGLEMTN);
			this.setTexture(MapGenTextures.JUNGLETEXTURE);
			break;
			
		case SAVANNAH:
			this.color = MapGenColors.SAVANNAH;
			this.cityColor = MapGenColors.SAVANNAHCITY;
			this.setMountainColor(MapGenColors.SAVANNAHMTN);
			this.setTexture(MapGenTextures.SAVANNAHTEXTURE);
			break;

		case DESERT:
			this.color = MapGenColors.DESERT;
			this.cityColor = MapGenColors.DESERTCITY;
			this.setMountainColor(MapGenColors.DESERTMTN);
			this.setTexture(MapGenTextures.DESERTTEXTURE);
			break;

		case TUNDRA:
			this.color = MapGenColors.TUNDRA;
			this.cityColor = MapGenColors.TUNDRACITY;
			this.setMountainColor(Color.GRAY);
			this.setTexture(MapGenTextures.TUNDRATEXTURE);
			break;
		case SNOW:
			this.color = MapGenColors.SNOW;
			this.cityColor = MapGenColors.SNOWCITY;
			this.setMountainColor(Color.GRAY);
			this.setTexture(MapGenTextures.SNOWTEXTURE);
			break;
		case MARSH:
			this.color = MapGenColors.MARSH;
			this.cityColor = MapGenColors.MARSHCITY;
			this.setMountainColor(Color.GRAY);
			this.setTexture(MapGenTextures.MARSHTEXTURE);
			break;
		default:
			this.color = MapGenColors.OCEAN;
			this.cityColor = MapGenColors.OCEANCITY;
			this.setMountainColor(Color.GRAY);
			this.setTexture(null);
			break;
		}
		
		this.grid = MapGenColors.OCEANCITY;
		this.getHexagon().setFill(new ImagePattern(this.getTexture()));
	}

	public void setFillColor(Color color){
		this.color = color;
	}
	
	public void setGridColor(Color color){
		this.grid = color;
	}

	public Terrain getTerrain() {
		return terrain;
	}

	public void setTerrain(Terrain terrain) {
		this.terrain = terrain;
		setColor();
	}

	public Color getCityColor() {
		return cityColor;
	}

	public void setCityColor(Color cityColor) {
		this.cityColor = cityColor;
	}

	public Elevation getElevation() {
		return elevation;
	}

	public void setElevation(Elevation elevation) {
		this.elevation = elevation;
	}

	public Color getMountainColor() {
		return mountainColor;
	}

	public void setMountainColor(Color mountainColor) {
		this.mountainColor = mountainColor;
	}

	public Image getTexture() {
		return texture;
	}
	
	private void setTexture(Image GRASSLANDTEXTURE) {
		this.texture = GRASSLANDTEXTURE;
	}

	public Polygon getHexagon() {
		return hexagon;
	}

	public void setHexagon(Polygon hexagon) {
		this.hexagon = hexagon;
	}
}
