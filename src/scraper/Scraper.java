package scraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Scraper {
	
	List<String> cardPages = new ArrayList<String>();
	
	ArrayList<Card> cardList = new ArrayList<Card>();
	
	final String startSite = "http://www.gwentdb.com/cards?filter-display=1";
	final String baseURL = "http://www.gwentdb.com/cards?filter-display=1&page=";
	final String baseCardURL = "http://www.gwentdb.com/cards/";
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
				Elements links = currentPage.select(".col-name");
				
				// first element is table header therefore it is removed
				links.remove(0);
				
				//there will be around 20 elements per page, each represents a card
				//below the link for each page is extracted
				for (Element x : links){
					String innerHTML = x.html();
					int startIndex = innerHTML.indexOf("a href=\"/cards") + "a href=\"/cards".length();
					int endIndex = innerHTML.indexOf("\"", startIndex);
					String adress = innerHTML.substring(startIndex, endIndex);
					cardPages.add(adress);
				}
				
			}
			System.out.println("done");
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
