package com.rjm.dropout.mapgenerator.objects;

import com.rjm.dropout.mapgenerator.enums.ResourceAnimals;

public class ResourceAnimal {
	ResourceAnimals type;
		
	public ResourceAnimal(Point point, ResourceAnimals type, String[][] board){
		this.type = type;
		board[point.y][point.x] = type.toString();
		
		switch(type){
		case bear:
			System.out.println("Placed 'Bears' at: " + point.x + "," + point.y);
			break;
		case bluewhale:
			System.out.println("Placed 'Blue Whales' at: " + point.x + "," + point.y);
			break;
		case camel:
			System.out.println("Placed 'Camels' at: " + point.x + "," + point.y);
			break;
		case chicken:
			System.out.println("Placed 'Chickens' at: " + point.x + "," + point.y);
			break;
		case cod:
			System.out.println("Placed 'Cod' at: " + point.x + "," + point.y);
			break;
		case dolphin:
			System.out.println("Placed 'Dolphins' at: " + point.x + "," + point.y);
			break;
		case elephantj:
			System.out.println("Placed 'Elephants' at: " + point.x + "," + point.y);
			break;
		case elephants:
			System.out.println("Placed 'Elephants' at: " + point.x + "," + point.y);
			break;
		case ferret:
			System.out.println("Placed 'Ferrets' at: " + point.x + "," + point.y);
			break;
		case fox:
			System.out.println("Placed 'Foxes' at: " + point.x + "," + point.y);
			break;
		case giantsquid:
			System.out.println("Placed 'Giant Squids' at: " + point.x + "," + point.y);
			break;
		case goat:
			System.out.println("Placed 'Goats' at: " + point.x + "," + point.y);
			break;
		case horse:
			System.out.println("Placed 'Horses' at: " + point.x + "," + point.y);
			break;
		case leopardseal:
			System.out.println("Placed 'Leopard Seals' at: " + point.x + "," + point.y);
			break;
		case lionfish:
			System.out.println("Placed 'Lionfish' at: " + point.x + "," + point.y);
			break;
		case lobster:
			System.out.println("Placed 'Lobsters' at: " + point.x + "," + point.y);
			break;
		case manta:
			System.out.println("Placed 'Mantas' at: " + point.x + "," + point.y);
			break;
		case nessy:
			System.out.println("Placed 'Nessies' at: " + point.x + "," + point.y);
			break;
		case pig:
			System.out.println("Placed 'Pigs' at: " + point.x + "," + point.y);
			break;
		case rabbitf:
			System.out.println("Placed 'Rabbits' at: " + point.x + "," + point.y);
			break;
		case rabbitg:
			System.out.println("Placed 'Rabbits' at: " + point.x + "," + point.y);
			break;
		case shark:
			System.out.println("Placed 'Sharks' at: " + point.x + "," + point.y);
			break;
		case sheep:
			System.out.println("Placed 'Sheep' at: " + point.x + "," + point.y);
			break;
		case shellfish:
			System.out.println("Placed 'Shellfish' at: " + point.x + "," + point.y);
			break;
		case tiger:
			System.out.println("Placed 'Tigers' at: " + point.x + "," + point.y);
			break;
		case tuna:
			System.out.println("Placed 'Tuna' at: " + point.x + "," + point.y);
			break;
		default:
			break;
		}
	}
}
