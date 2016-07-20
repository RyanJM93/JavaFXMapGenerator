package com.rjm.dropout.mapgenerator.objects;

import com.rjm.dropout.mapgenerator.enums.ResourceFruits;

public class ResourceFruit {
	ResourceFruits type;
		
	public ResourceFruit(Point point, ResourceFruits type, String[][] board){
		this.type = type;
		board[point.y][point.x] = type.toString();
		
		switch(type){
		case porumf:
			System.out.println("Placed 'Porums' at: " + point.x + "," + point.y);
			break;
		case porumj:
			System.out.println("Placed 'Porums' at: " + point.x + "," + point.y);
			break;
		case uvatg:
			System.out.println("Placed 'Uvats' at: " + point.x + "," + point.y);
			break;
		case uvats:
			System.out.println("Placed 'Uvats' at: " + point.x + "," + point.y);
			break;
		default:
			break;
		}
	}
}
