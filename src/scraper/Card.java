package scraper;

import java.awt.Image;

public class Card {
	
	String name;
	String type;
	String faction;
	Boolean loyalty;
	int power;
	String rarity;
	boolean isMelee;
	boolean isRanged;
	boolean isSiege;
	boolean isEvent;
	Image picture;
	String cardText;
	String flavorText;
	
	// Automatically generated constructor
	Card(String name, String type, String faction, Boolean loyalty, int power, String rarity, boolean isMelee,
			boolean isRanged, boolean isSiege, boolean isEvent, Image picture, String cardText, String flavorText) {
		this.name = name;
		this.type = type;
		this.faction = faction;
		this.loyalty = loyalty;
		this.power = power;
		this.rarity = rarity;
		this.isMelee = isMelee;
		this.isRanged = isRanged;
		this.isSiege = isSiege;
		this.isEvent = isEvent;
		this.picture = picture;
		this.cardText = cardText;
		this.flavorText = flavorText;
	}	
	
	
	
}
