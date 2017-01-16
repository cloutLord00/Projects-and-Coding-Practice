package mediaRentalManager;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Customer implements Comparable<Customer> {
	
	//instance variables
	private String name;
	private String address;
	private String plan;
	private Queue<String> interested;
	private Queue<String> rented;
	private int value;
	
	public Customer(String name, String address, String plan) {
		this.name = name;
		this.address = address;
		this.plan = plan;
		
		this.interested = new LinkedList<String>();
		this.rented = new LinkedList<String>();
		
		if (this.plan.equals("LIMITED")){
			this.value = 2;
		}
		else {
			this.value = Integer.MAX_VALUE;
		}
	}
	
	//sets the value for that customer
	public void setPlan(int value){
		this.value = value;
	}

	public String getName(){
		return name;
	}
	
	public String getAddress(){
		return address;
	}
	
	public String getPlan(){
		return plan;
	}
	
	public boolean isFull(){
		return this.rented.size() == value;
	}
	
	public void addToInterested(String title){
		interested.add(title);
	}
	
	public boolean addToRented(String title){
		if (this.rented.size()<value) {
			return rented.add(title);
		}
		return false;
	}
	
	public void deleteInterested(String title){
		interested.remove(title);
	}
	
	public void deleteRented(String title){
		rented.remove(title);
	}
	
	public Queue<String> getInterested(){
		return interested;
	}

	public Queue<String> getRented(){
		return rented;
	}

	public boolean equals (Object o){
		if (o==null) {return false;}
		if (!(o instanceof Customer)) {return false;}

		Customer aCustomer = (Customer) o;
		if (this.name.equals(aCustomer.name)){
			return true;
		}return false;
	}
	
	public String toString(){
		return "Name: " + getName() + ", Address: " + getAddress() + ", Plan:" + this.plan + 
				"\nRented: " + getRented()  + "\nQueue: " + getInterested() + "\n";
	}

	@Override
	public int compareTo(Customer o) {
		return this.name.compareTo(o.name);
	}
	
}
