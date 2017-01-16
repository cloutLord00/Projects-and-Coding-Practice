package mediaRentalManager;

public class Movie extends Media {

	//instance variables
	private String rating;
	
	public Movie(String title, int copiesAvail, String rating) {
		super(title, copiesAvail);
		this.rating = rating;
	}
	
	public String getRating(){
		return rating;
	}
	
	public boolean equals (Object o){
		if (o==null) {return false;}
		if (!(o instanceof Movie)) {return false;}
		
		Movie aMovie = (Movie) o;
		if (this.title.equals(aMovie.title) && 
			this.copiesAvailable == aMovie.copiesAvailable &&
			this.rating.equals(aMovie.rating)){
			return true;
		}
		return false;
	}
	
	public String toString(){
		return super.toString() + ", Rating: " + getRating() + "\n";
	}
	
}
