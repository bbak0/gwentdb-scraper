package scraper;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.imageio.ImageIO;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;	

public class Scraper implements Runnable{
	
	ConcurrentLinkedQueue<Card> cardList;
	
	public Scraper(ConcurrentLinkedQueue<Card> list){
		this.cardList = list;
	}
	
	ArrayList<String> cardPages = new ArrayList<String>();
	ArrayList<LinkPower> cardPagesWithPower = new ArrayList<LinkPower>();
	//LinkedList<Card> cardList = new LinkedList<Card>();
	
	final String startSite = "http://www.gwentdb.com/cards?filter-display=1";
	final String baseURL = "http://www.gwentdb.com/cards?filter-display=1&page=";
	final String baseCardURL = "http://www.gwentdb.com/cards";
	final boolean getHighResImages = false;

	
	@SuppressWarnings("unused")
	void getCardsURLs() throws IOException{
		
			// connect to the first page of the cards collection
			Document doc = Jsoup.connect(startSite).get();
			Document doc2 = Jsoup.connect("http://www.gwentdb.com/cards/50037-adrenaline-rush").get();
			// get the list of all subpages with cards
			Elements sub = doc.select("a.b-pagination-item");
			Elements pngs = doc2.select("img[src$=.png]");

			// get the number of the highest page that currently exists
			// this number is later used to determine what links to visit
			Element lastLink = sub.last();
			int num = Integer.valueOf(lastLink.html());
			
			// go through all the subpages and save individual links
			for (int i = 1; i <= num; i++){
				
				//append page number to the base url and get the elements with card-row class
				String currentURL = baseURL + i;
				Document currentPage = Jsoup.connect(currentURL).get();
				Elements links = currentPage.select(".card-row");
				
				// first element is table header therefore it is removed
				//links.remove(0);
				
				// there will be around 20 elements per page, each represents a card
				// below the link for each page is extracted
				for (Element x : links){
					String innerHTML = x.html();
					
					// gets the page link
					int startIndex = innerHTML.indexOf("a href=\"/cards") + "a href=\"/cards".length();
					int endIndex = innerHTML.indexOf("\"", startIndex);
					String adress = innerHTML.substring(startIndex, endIndex);
					
					//gets card's power
					startIndex = innerHTML.indexOf("<td class=\"col-power\">") + "<td class=\"col-power\">".length();
					endIndex =  innerHTML.indexOf("<", startIndex);
					int power = Integer.valueOf(innerHTML.substring(startIndex, endIndex));
					
					//gets card's loyalty
					Boolean loyalty = null;
					if (innerHTML.contains("is-disloyal tip")){
						loyalty = false;
					} else if (innerHTML.contains("is-loyal tip")){
						loyalty = true;
					}
					
					cardPagesWithPower.add(new LinkPower(adress,power,loyalty));
				}
				
			}
			System.out.println("done");
	}
	
	@SuppressWarnings("unused")
	void getAllCardDefinitions() throws IOException{
		for (LinkPower link : cardPagesWithPower){
			
			// Generate proper link to the card page and download its contents
			String fullLink = baseCardURL + link.getLink();
			Document currentWebsite = Jsoup.connect(fullLink).get();
			
			// Get values for card
			String name = currentWebsite.select("div.card-name h1").html();
			String type = currentWebsite.select("div.card-type a").html();
			String faction = currentWebsite.select(".faction-link").html();
			Boolean loyalty = link.getLoyal();
			int power = link.getPower();
			String rarity = currentWebsite.select(".card-rarity span").html();
			
			//logic used to determine lane
			String laneInfo = currentWebsite.select(".card-row").html();
			boolean isMelee = laneInfo.contains("Melee");
			boolean isRanged = laneInfo.contains("Ranged");
			boolean isSiege = laneInfo.contains("Siege");
			boolean isEvent = laneInfo.contains("Event");
			
			BufferedImage picture = getPicture(currentWebsite);
			String cardText = currentWebsite.select(".card-abilities").text();
			String flavorText = currentWebsite.select(".sw-card-flavor-text").text();
			
			// Create and fill empty card definition, then add it to queue
			cardList.add(new Card(name, type, faction, loyalty, power, rarity, isMelee, isRanged,
						isSiege, isEvent,picture,cardText,flavorText));
			
		}
		// Inform DB manager no more cards are being added
		DatabaseManager.endProcess();
		
	}
	
	//returns card picture from current website
	BufferedImage getPicture(Document website){
		
		Elements pngs = website.select("img[src$=.png]");
		Element picture;
		
		//chooses appropriate picture according to the setting
		if (getHighResImages){
			picture = pngs.get(1);
		} else {
			picture = pngs.get(0);
		}
		
		//get image's url
		URL imageLink = null;
		BufferedImage out = null;
		
		try {
			imageLink = new URL(picture.attr("src"));
			out = ImageIO.read(imageLink);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return out;
	}
	
	void createNewDatabase(){
		
	}
	
	public void run(){
		try {
			getCardsURLs();
			getAllCardDefinitions();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
