package scraper;

import java.util.concurrent.ConcurrentLinkedQueue;

public class MainClass {

	static String dbPath = "C:/sqlite/db/";
	static String dbName = "test.sqlite";
	public static ConcurrentLinkedQueue<Card> cardList = new ConcurrentLinkedQueue<Card>();
	
	public static void main(String[] args) {
		// Thread that collects data from the website and adds it to the queue
		new Thread(new Scraper(cardList)).start();
		// Thread that takes cards from the queue and adds them to database
		new Thread(new DatabaseManager(cardList, dbName, dbPath)).start();
	}

}
