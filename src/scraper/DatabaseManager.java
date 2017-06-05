package scraper;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.io.IOException;
import java.sql.*;

public class DatabaseManager implements Runnable {
	
	ConcurrentLinkedQueue<Card> cardList;
	String dbName;
	String dbPath;
	static boolean finished = false;
	
	public DatabaseManager(ConcurrentLinkedQueue<Card> list, String name, String path){
		this.cardList = list;
		this.dbName = name;
		this.dbPath = path;
	}
	
	// used to tell that there are no more cards entering the queue
	static void endProcess(){
		finished = true;
	}
	
	private Connection connect(){
		
		String url = "jdbc:sqlite:" + dbPath + dbName;
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
		} catch (SQLException e){
			System.out.println(e.getMessage());
		}
		return conn;
	}
	
	
	void removeExistingContent(){
		String sql = "DROP TABLE IF EXISTS Cards;";
		
		 try (Connection conn = this.connect();
	                PreparedStatement pstmt = conn.prepareStatement(sql)) {
			 	pstmt.execute();
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        }
	}
	
	void createTable(){
		String sql = "CREATE TABLE IF NOT EXISTS Cards (\n"
                + "	CardID integer PRIMARY KEY,\n"
                + "	Name text,\n"
                + "	Faction text,\n"
                + "	Loyalty integer,\n"
                + "	Power integer,\n"
                + "	Rarity text,\n"
                + "	isMelee integer,\n"
                + "	isRanged integer,\n"
                + "	isSiege integer,\n"
                + "	isEvent integer,\n"
                + "	CardText text,\n"
                + "	FlavorText text,\n"
                + "	Picture blob\n"
                + ");";
		
		try (Connection conn = this.connect();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
		 	pstmt.execute();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	
	void insertCard(Card card){
		
		String sql = "INSERT INTO Cards(Name, Faction, Loyalty, Power, Rarity, "
				+ "isMelee, isRanged, isSiege, isEvent, CardText, FlavorText, Picture) "
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		
		 try (Connection conn = this.connect();
	                PreparedStatement pstmt = conn.prepareStatement(sql)) {
			 	pstmt.setString(1, card.getName());
			 	pstmt.setString(2, card.getFaction());
			 	pstmt.setInt(3, card.getLoyaltyInt());
			 	pstmt.setInt(4, card.getPower());
			 	pstmt.setString(5, card.getRarity());
			 	pstmt.setInt(6,card.isMelee()? 1 : 0);
			 	pstmt.setInt(7, card.isRanged()? 1 : 0);
			 	pstmt.setInt(8, card.isSiege()? 1 : 0);
			 	pstmt.setInt(9, card.isEvent()? 1 : 0);
			 	pstmt.setString(10, card.getCardText());
			 	pstmt.setString(11, card.getFlavorText());
			 	pstmt.setBytes(12, card.getPictureByteArray());
			 	pstmt.execute();			 	
			 	
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	        } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	
	public void run(){
		// create db and table
		connect();
		removeExistingContent();
		createTable();
		int i = 1;

		while (!finished || !cardList.isEmpty()){
			
			if (!cardList.isEmpty()){
				insertCard(cardList.poll());
				System.out.println("saved card "+i);
				i++;
			} else {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
}
