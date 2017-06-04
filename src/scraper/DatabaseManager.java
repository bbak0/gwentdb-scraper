package scraper;

import java.util.concurrent.ConcurrentLinkedQueue;

public class DatabaseManager implements Runnable {
	
	ConcurrentLinkedQueue<Card> cardList;
	String dbName;
	String dbPath;
	
	public DatabaseManager(ConcurrentLinkedQueue<Card> list, String name, String path){
		this.cardList = list;
		this.dbName = name;
		this.dbPath = path;
	}
	
	
	public void run(){
		System.out.println("threading test");
	}
}
