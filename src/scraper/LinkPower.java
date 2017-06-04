package scraper;

// Because gwentdb doesnt show power on individual card pages it needs to be
// saved while looking for individual links. This small class will hold pairs
// of link to card and its power.

//added Loyal stat

public class LinkPower {
	
	String link;
	int power;
	Boolean loyal; //nullable boolean because its not applicable to some cards
	
	public LinkPower(String link, int power, Boolean loyal){
		this.link = link;
		this.power = power;
		this.loyal = loyal;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public Boolean getLoyal() {
		return loyal;
	}

	public void setLoyal(Boolean loyal) {
		this.loyal = loyal;
	}
	
	

}
