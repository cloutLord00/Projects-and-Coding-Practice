import java.io.BufferedReader;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class OrderP 
{
	public HashMap<Integer, String> orders;
	public HashMap<String,Integer> summarymap;
	public HashMap<String,Double>prices;
	
	private BufferedReader file;
	private int totalOrders;
	
	//will keep track of processed orders
	private int orderCount;
	
	//initialize with file containing keys and a number of orders that need to be processed
	public OrderP(BufferedReader fileName, int ordersToProcess){
		file = fileName;
		orders = new HashMap<Integer,String>();
		summarymap = new HashMap<String,Integer>();
		prices=new HashMap<String,Double>();
		totalOrders = ordersToProcess;
		orderCount = 0;
		
		//read the data file
		try{
			String data;
			//init prices.. init summary map
			String [] info;
			while((data=file.readLine())!=null)
			{
				info = data.split(" ");
				String name=info[0];
				String prices1=info[1];
				double prices3=Double.parseDouble(prices1);
				prices.put(name,prices3);
				summarymap.put(name,0);
				//System.out.println(data);
			}
			file.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public void processOrder(BufferedReader file, int id)
	{
		System.out.println("Reading order for client with id: "+id);
		String content = "";
		NumberFormat formatter = NumberFormat.getCurrencyInstance();

		//read the data file
		try{
			String data;

			HashMap<String, Integer>count = new HashMap<String,Integer>();
			Set<String> keys = prices.keySet();
			for(String n: keys){
				count.put(n, 0);
			}
			
			
			//output processing for client id 

			//clients purchases - data = "name date" - (ignores date)
			while((data=file.readLine())!=null)
			{
				String name=data.split(" ")[0];
				count.put(name,count.get(name)+1);
				
				//critical section counting total purchases
				synchronized(this){
					summarymap.put(name,summarymap.get(name)+1);
				}
			}
			file.close();

			content += "----- Order details for client with Id: "+ id + " -----\n";	

			List<String>temp=new ArrayList<String>(prices.keySet());
			Collections.sort(temp);
			double total = 0;
			for(String key:temp)
			{
				if(count.get(key)!=0)
				{
					content += "Item's name: " + key +", Cost per item: "+formatter.format(prices.get(key)) +","+" Quantity: "+ count.get(key)+","+
							" Cost: "+formatter.format((prices.get(key))*count.get(key))+"\n";
					total+=((prices.get(key))*count.get(key));
				}
			}
			content+="Order Total: "+formatter.format(total)+"\n";
			
			//critical section keeping track of order for a client
			//also signals that an order has finished processing
			synchronized(this){	
				orders.put(id,content);
				orderCount += 1;
			}
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	
	public boolean doneProcessing(){
		return orderCount == totalOrders;
	}
}
