package mediaRentalManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;


public class MediaRentalManager implements MediaRentalManagerInt {
	
	//instance variables
	private ArrayList<Customer> customers;
	private ArrayList<Media> media;
	
	//constructor
	public MediaRentalManager() {
		this.customers = new ArrayList<Customer>();
		this.media = new ArrayList<Media>();
	}

	@Override
	public void addCustomer(String name, String address, String plan) {
		customers.add(new Customer(name, address, plan));
	}

	@Override
	public void addMovie(String title, int copiesAvailable, String rating) {
		media.add(new Movie(title, copiesAvailable, rating));
	}

	@Override
	public void addAlbum(String title, int copiesAvailable, String artist, String songs) {	
		media.add(new Album(title, artist, songs, copiesAvailable));
	}

	@Override
	public void setLimitedPlanLimit(int value) {
		for (Customer c : this.customers){
			if (c.getPlan().equals("LIMITED")){
				c.setPlan(value);
			}
		}
	}

	@Override
	public String getAllCustomersInfo() {
		String allCustomers = "***** Customers' Information *****\n" ;
		Collections.sort(customers);
		for (Customer C : customers){
			allCustomers += C.toString();
		}
		return allCustomers;
	}

	@Override
	public String getAllMediaInfo() {
		String allMedia = "***** Media Information *****\n";
		Collections.sort(media);
		for (Media M : media){
			allMedia += M.toString();
		}
		return allMedia;
	}

	@Override
	public boolean addToQueue(String customerName, String mediaTitle) {
		Customer current = null;
		
		if (customers.contains(new Customer (customerName, "",""))){
			for (Customer c : customers){
				if (c.getName().equals(customerName)){
					current = c;
					Queue<String> queue = current.getInterested();
					
					if (!queue.contains(mediaTitle)){
						current.addToInterested(mediaTitle);
						return true;
					}
				}
			}
			
		}
		return false;
	}

	@Override
	public boolean removeFromQueue(String customerName, String mediaTitle) {
		Customer current = null;
		Queue<String> queue;
		if (customers.contains(new Customer(customerName,"",""))){
			for (Customer c : customers){
				if (c.getName().equals(customerName)){
				 current = c;
				}
			}
			queue = current.getInterested();
			if (queue.contains(mediaTitle)){
				current.deleteInterested(mediaTitle);
				return true;
			}
		}
		return false;
	}

	@Override
	public String processRequests() {
		
		String processed = "";
		Collections.sort(customers);
		for (Customer cust : customers){
			//System.out.println(cust);
			Queue<String> queue = cust.getInterested();
			int count = 0;
			int length = queue.size();
			while( count++ < length && !queue.isEmpty() && !cust.isFull() ){
				String media = queue.peek();
				
				for (Media m : this.media){
					if (m.getTitle().equals(media) && m.getCopies() > 0){
						if(cust.addToRented(media)){
							processed += "Sending " + media + " to " + cust.getName() + "\n";
							m.removeCopy();
							this.addToQueue(cust.getName(), media);
							//System.out.println("Sending " + media + " to " + cust.getName() + "\n");
						}
						break;
					}
				}
				
			}
		}
		return processed;
	}

	@Override
	public boolean returnMedia(String customerName, String mediaTitle) {
		Customer current = null;
		Queue<String> queue;
		if (customers.contains(customerName)){
			for (Customer c : customers){
				if (c.getName().equals(customerName)){
					current = c;
					queue = current.getRented();
					if (queue.contains(mediaTitle)){
						current.deleteRented(mediaTitle);

						for (Media m : this.media){
							if (m.getTitle().equals(media)){
								m.addCopy();
								return true;
							}
						}
						
					}
					return false;
				}
			}
			
		}
		return false;
	}

	@Override
	public ArrayList<String> searchMedia(String title, String rating, String artist, String songs) {
		// we will build this list
		ArrayList<String> list = new ArrayList<String>();
		
		// adding all the titles in media if title, rating, artist, and songs is null
		if (title == null && rating == null && artist == null && songs == null ){
			for (Media obj : media ){
				list.add(obj.title);
			}
			return list;
		}

		// all arguments are not null so we build our list depending on the non-null arguments
		// keep in mind that the list is currently empty
		// adding titles with a rating that matches in the media list
		if( title !=null){
			// search the media list for the Media title object with the same title
			for (Media obj : media ){
				// we will add the title that matches 
				if ( obj.title.equals(title)){
					list.add( obj.title );
					break;			// exits the loop
				}
			}	
		}
		// adding titles with a rating that matches in the media list
		// we know that media with a rating must be a Movie
		if( rating !=null){
			// search the media list for all Media title objects with the same rating
			for (Media obj : media){
				//go to net iteration if it is not a Movie
				if ( !(obj instanceof Movie) ) continue;
				
				// casting object to a Movie so we can check the rating attribute
				Movie m = (Movie) obj; 
				// we will add titles with matching ratings only when the title is not already in the list
				if ( m.getRating().equals(rating) && list.contains( m.title ) == false ){
					list.add( m.title);
				}
			}
		}
		// adding titles with an artist or songs that match in the media list
		// we know that media with artist or songs must be an Album
		if(  artist != null || songs !=null){
			for ( Media obj : media){
				//go to next iteration if it is not an Album
				if ( !(obj instanceof Album) ) continue;
				
				// casting obj to a Album so we can check the artist or songs attribute
				Album a = (Album) obj; 
				
				if( artist !=null){
					// we will add titles with matching artists only when the title is not already in the list
					if ( a.getArtist().equals(artist ) && list.contains( a.title ) == false ){
						list.add( a.title );
					}
				}
				if( songs!=null ){
					// we will add titles with matching songs only when the title is not already in the list		
					if ( a.getSongs().contains(songs) && list.contains( a.title ) == false ){
						list.add( a.title);
					}
				}
			}
		}
		// at this point we have added all we can sort the list and return it 
		Collections.sort(list);
		return list;
	}
	
	public String toString(){
		return "Customers: " + this.customers  + "\n Media Avail: " + this.media; 
	}
	

}
