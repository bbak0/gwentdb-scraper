package scraper;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

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
	BufferedImage picture;
	String cardText;
	String flavorText;
	
	// Automatically generated constructor
	Card(String name, String type, String faction, Boolean loyalty, int power, String rarity, boolean isMelee,
			boolean isRanged, boolean isSiege, boolean isEvent, BufferedImage picture, String cardText, String flavorText) {
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

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public String getFaction() {
		return faction;
	}

	public Boolean getLoyalty() {
		return loyalty;
	}
	
	public int getLoyaltyInt(){
		if (loyalty == null){
			return -1;
		} else if (loyalty){
			return 1;
		} else {
			return 0;
		}

	}

	public int getPower() {
		return power;
	}

	public String getRarity() {
		return rarity;
	}

	public boolean isMelee() {
		return isMelee;
	}

	public boolean isRanged() {
		return isRanged;
	}

	public boolean isSiege() {
		return isSiege;
	}

	public boolean isEvent() {
		return isEvent;
	}

	public BufferedImage getPicture() {
		return picture;
	}
	
	public byte[] getPictureByteArray() throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(picture, "png", baos);
		baos.flush();
		byte[] byteImage = baos.toByteArray();
		baos.close();
		return byteImage;
	}

	public String getCardText() {
		return cardText;
	}

	public String getFlavorText() {
		return flavorText;
	}	
	
	
	
	
}
