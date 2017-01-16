package mediaRentalManager;

public class Album extends Media{
	
	//instance Variables
	private String artist;
	private String songs;
	
	public Album(String title, String artist, String songs, int copiesAvail) {
		super (title, copiesAvail);
		this.artist = artist;
		this.songs = songs;
	}

	public String getArtist(){
		return artist;
	}
	
	public String getSongs(){
		return songs;
	}
	
	public String toString(){
		return super.toString() + ", Artist: " + getArtist()+ 
				", Songs: " + getSongs() + "\n";
	}

}
