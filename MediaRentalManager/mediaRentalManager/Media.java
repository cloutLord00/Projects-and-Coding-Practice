package mediaRentalManager;

public class Media  implements Comparable <Media>{
	protected String title;
	protected int copiesAvailable;
	
	public Media(String title, int copiesAvailable) {
		this.title = title;
		this.copiesAvailable = copiesAvailable;
	}
	
	public String getTitle(){
		return title;
	}
	
	public int getCopies(){
		return copiesAvailable;
	}
	
	public void addCopy(){
		copiesAvailable++;
	}
	
	public void removeCopy(){
		if (copiesAvailable>0) copiesAvailable--;
	}
	
	public int compareTo(Media object){
		String title1 = this.title;
		String title2 = object.title;
		
		//basically using to find if its 0
		//which means that the two titles are the same
		return title1.compareTo(title2);
	}
	
	
	public String toString(){
		return "Title: " + getTitle() + ", Copies Available: " + getCopies();
	}
	

}
