package scraper;

import java.io.IOException;
import java.util.ArrayList;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.sql.*;	

public class Scraper {
	
	ArrayList<String> cardPages = new ArrayList<String>();
	ArrayList<Integer> cardPower = new ArrayList<Integer>();
	ArrayList<LinkPower> cardPagesWithPower = new ArrayList<LinkPower>();
	
	ArrayList<Card> cardList = new ArrayList<Card>();
	
	final String startSite = "http://www.gwentdb.com/cards?filter-display=1";
	final String baseURL = "http://www.gwentdb.com/cards?filter-display=1&page=";
	final String baseCardURL = "http://www.gwentdb.com/cards";
	final boolean getHighResImages = false;
	String dbPath;
	String dbName;

	
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
	
	void getAllCardDefinitions() throws IOException{
		
		for (String page : cardPages){
			
			// Generate proper link to the card page and download its contents
			String fullLink = baseCardURL + page;
			Document currentWebsite = Jsoup.connect(fullLink).get();
			
			// Get values for card;
			
			
			// Create and fill empty card definition
			
			
		}
		
	}
	
	void createNewDatabase(){
		
	}
	
	void run(){
		try {
			getCardsURLs();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Scraper().run();
	}

}
