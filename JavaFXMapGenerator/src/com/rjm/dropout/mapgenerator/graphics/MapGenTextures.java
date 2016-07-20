package com.rjm.dropout.mapgenerator.graphics;

import javafx.scene.image.Image;

public class MapGenTextures {
	
	// Terrain
	public static Image COASTTEXTURE = null;
	public static Image DESERTTEXTURE = null;
	public static Image GRASSLANDTEXTURE = null;
	public static Image FORESTTEXTURE = null;
	public static Image JUNGLETEXTURE = null;
	public static Image MARSHTEXTURE = null;
	public static Image OCEANTEXTURE = null;
	public static Image SAVANNAHTEXTURE = null;
	public static Image SNOWTEXTURE = null;
	public static Image TAIGATEXTURE = null;
	public static Image TUNDRATEXTURE = null;

	// Resources - Animals
	public static Image BEAR = null;
	public static Image CAMEL = null;
	public static Image CHICKEN = null;
	public static Image COD = null;
	public static Image DOLPHIN = null;
	public static Image ELEPHANTjungle = null;
	public static Image ELEPHANTsavannah = null;
	public static Image FERRET = null;
	public static Image FOX = null;
	public static Image GOAT = null;
	public static Image HORSE = null;
	public static Image LEOPARDSEAL = null;
	public static Image LIONFISH = null;
	public static Image LOBSTER = null;
	public static Image MANTA = null;
	public static Image NESSY = null;
	public static Image PIG = null;
	public static Image RABBITforest = null;
	public static Image RABBITgrassland = null;
	public static Image SHARK = null;
	public static Image SHEEP = null;
	public static Image SHELLFISH = null;
	public static Image SQUID = null;
	public static Image TIGER = null;
	public static Image TUNA = null;
	public static Image WHALE = null;

	// Resources - Fruits
	public static Image BLUEAPRICOTgrassland = null;
	public static Image BLUEAPRICOTjungle = null;
	public static Image CITRONforest = null;
	public static Image CITRONgrassland = null;
	public static Image GAFFERdesert = null;
	public static Image GAFFERsavannah = null;
	public static Image GOREMELONgrassland = null;
	public static Image GOREMELONmarsh = null;
	public static Image MANDARINforest = null;
	public static Image MANDARINjungle = null;
	public static Image POMELOforest = null;
	public static Image POMELOmarsh = null;
	public static Image PORUMforest = null;
	public static Image PORUMjungle = null;
	public static Image UVATgrassland = null;
	public static Image UVATsavannah = null;
	
	public static void loadTextures(){
		try {
			// Terrain
			COASTTEXTURE = new Image("images/terrain/coast.bmp");
			DESERTTEXTURE = new Image("images/terrain/desert.jpg");
			GRASSLANDTEXTURE = new Image("images/terrain/grassland.jpg");
			FORESTTEXTURE = new Image("images/terrain/forest.jpg");
			JUNGLETEXTURE = new Image("images/terrain/jungle.jpg");
			MARSHTEXTURE = new Image("images/terrain/marsh2.jpg");
			OCEANTEXTURE = new Image("images/terrain/ocean.bmp");
			SAVANNAHTEXTURE = new Image("images/terrain/savannah.jpg");
			SNOWTEXTURE = new Image("images/terrain/snow.jpg");
			TAIGATEXTURE = new Image("images/terrain/taiga.jpg");
			TUNDRATEXTURE = new Image("images/terrain/tundra3.jpg");

			// Resources - Animals
			BEAR = new Image("images/resources/animals/bear.png");
			CAMEL = new Image("images/resources/animals/camel.png");
			CHICKEN = new Image("images/resources/animals/chicken.png");
			COD = new Image("images/resources/animals/cod.png");
			ELEPHANTjungle = new Image("images/resources/animals/elephant-j.png");
			ELEPHANTsavannah = new Image("images/resources/animals/elephant-s.png");
			DOLPHIN = new Image("images/resources/animals/dolphin.png");
			FERRET = new Image("images/resources/animals/ferret.png");
			FOX = new Image("images/resources/animals/fox.png");
			GOAT = new Image("images/resources/animals/goat.png");
			HORSE = new Image("images/resources/animals/horse.png");
			LEOPARDSEAL = new Image("images/resources/animals/leopardseal.png");
			LIONFISH = new Image("images/resources/animals/lionfish.png");
			LOBSTER = new Image("images/resources/animals/lobster.png");
			MANTA = new Image("images/resources/animals/manta.png");
			NESSY = new Image("images/resources/animals/nessy.png");
			PIG = new Image("images/resources/animals/pig.png");
			RABBITforest = new Image("images/resources/animals/rabbit-fx16.png");
			RABBITgrassland = new Image("images/resources/animals/rabbit-gx16.png");
			SHARK = new Image("images/resources/animals/shark.png");
			SHEEP = new Image("images/resources/animals/sheep.png");
			SHELLFISH = new Image("images/resources/animals/shellfish.png");
			SQUID = new Image("images/resources/animals/squid.png");
			TIGER = new Image("images/resources/animals/tiger.png");
			TUNA = new Image("images/resources/animals/tuna.png");
			WHALE = new Image("images/resources/animals/whale.png");
			
			// Resources - Fruits
			BLUEAPRICOTgrassland = new Image("images/resources/fruits/blueapricot-g.png");
			BLUEAPRICOTjungle = new Image("images/resources/fruits/blueapricot-j.png");
			CITRONforest = new Image("images/resources/fruits/citron-f.png");
			CITRONgrassland = new Image("images/resources/fruits/citron-g.png");
//			GAFFERdesert = new Image("images/resources/fruits/gaffer-d.png");
//			GAFFERsavannah = new Image("images/resources/fruits/gaffer-s.png");
//			GOREMELONgrassland = new Image("images/resources/fruits/goremelon-g.png");
//			GOREMELONmarsh = new Image("images/resources/fruits/goremelon-m.png");
			MANDARINforest = new Image("images/resources/fruits/mandarin-f.png");
			MANDARINjungle = new Image("images/resources/fruits/mandarin-j.png");
			POMELOforest = new Image("images/resources/fruits/pomelo-f.png");
			POMELOmarsh = new Image("images/resources/fruits/pomelo-m.png");
			PORUMforest = new Image("images/resources/fruits/porum-f.png");
			PORUMjungle = new Image("images/resources/fruits/porum-j.png");
			UVATgrassland = new Image("images/resources/fruits/uvat-g.png");
			UVATsavannah = new Image("images/resources/fruits/uvat-s.png");
			
		} catch (Exception e){
			e.printStackTrace();
		}
	}
}
