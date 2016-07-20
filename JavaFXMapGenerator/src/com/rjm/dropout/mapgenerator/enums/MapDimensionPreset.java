package com.rjm.dropout.mapgenerator.enums;

public enum MapDimensionPreset {
	
	TABLETOP(39, 16) {
		public String toString(){
			return "Tabletop: 39x16";
		}
	},
	
	DUEL(40, 25) {
		public String toString(){
			return "Duel: 40x25";
		}
	},
	
	TINY(56, 36) {
		public String toString(){
			return "Tiny: 56x36";
		}
	},
	
	SMALL(66, 42) {
		public String toString(){
			return "Small: 66x42";
		}
	},
	
	STANDARD(80, 52) {
		public String toString(){
			return "Standard: 80x52";
		}
	},
	
	LARGE(104, 64) {
		public String toString(){
			return "Large: 104x64";
		}
	},
	
	HUGE(128, 80) {
		public String toString(){
			return "Huge: 128x80";
		}
	},
	
	EPIC(205, 84) {
		public String toString(){
			return "Epic: 205x84";
		}
	};
	
	int x = 0;
	int y = 0;
	
	MapDimensionPreset(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
}
