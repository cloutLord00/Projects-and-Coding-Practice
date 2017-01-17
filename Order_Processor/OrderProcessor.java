import java.util.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;

public class OrderProcessor {
	
	public static void main(String []args) throws FileNotFoundException
	{
		Scanner input= new Scanner(System.in);
		System.out.print("Enter item's data file name: ");
		String filename=input.nextLine();
		
		//prompt for 'y'
		System.out.print("Enter 'y' for multiple threads, any other character otherwise: ");
		char y =input.next().charAt(0);
		//number of orders
		System.out.print("Enter number of orders to process: ");
		int numoforder=input.nextInt();
		
		//some prep work for multi-thread/single - process
		//open data file
		BufferedReader file= new BufferedReader(new FileReader(filename));
		OrderP obj = new OrderP(file, numoforder);
		//sorts the keys for future processing 
		List<String>temp=new ArrayList<String>(obj.prices.keySet());
		Collections.sort(temp);
		
		//base filename
		System.out.print("Enter order's base filename: ");
		String base=input.next();
		//result filename
		//input.nextLine();
		System.out.print("Enter result's filename: ");
		String filen=input.next();
		input.close();

		long startTime = System.currentTimeMillis();
		/* TASK YOU WANT TO TIME */
		if(y=='y')
		{
			ArrayList<ThreadRun>test=new ArrayList<ThreadRun>();

			//prep threads
			for(int x = 1; x<=numoforder; x++){
				try
				{
					String name = base+x;
					file = new BufferedReader( new FileReader(name+".txt") );
					String data;

					data = file.readLine();
					int id=Integer.parseInt((data.split(" ")[1])); 

					ThreadRun tx = new ThreadRun(id,obj,file);
					test.add(tx);

				}catch (IOException e)
				{
					System.out.println("please be right???");
					e.printStackTrace();
				}
			}

			//start threads
			for(int x = 1; x<=numoforder; x++){
				//join thread
				ThreadRun tx = test.get(x-1);
				tx.start();				
			}
			
			for(int x = 1; x<=numoforder; x++){
				//join thread
				ThreadRun tx = test.get(x-1);
				try {
					tx.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
			
		}
		else{	
			//single thread - process
			for(int x = 1; x<=numoforder; x++){
				file=new BufferedReader(new FileReader(base+x+".txt"));
				try
				{
					String data=file.readLine();
					int id=Integer.parseInt((data.split(" ")[1])); 
					obj.processOrder(file,id);
				}catch (IOException e)
				{
					System.out.println("please ???");
					e.printStackTrace();
				}
			}

		}
		
		
		//wait for all threads to finish before processing summary
		//while( !obj.doneProcessing() ); //wait until all orders have been processed
		
		
		//Process Summary
		String results = "";
		
		//Sorted orders
		List<Integer>temp2=new ArrayList<Integer>(obj.orders.keySet());
		Collections.sort(temp2);
		for(Integer someId: temp2)
		{
			//add summarry to content
			results += obj.orders.get(someId);
		}
		
		//Sorted Summary
		results +="***** Summary of all orders *****"+"\n";
		double total1=0;
		NumberFormat formatter = NumberFormat.getCurrencyInstance();
		for(String key2:temp)
		{
			//add summary to content
			results +="Summary - Item's name: "+key2+","+" Cost per item: "+formatter.format(obj.prices.get(key2))+","+
					" Number sold: "+obj.summarymap.get(key2)+","+"Item's Total: "+formatter.format((obj.prices.get(key2))*(obj.summarymap.get(key2)))+"\n";

			total1 +=obj.prices.get(key2)*obj.summarymap.get(key2);
		}

		results+="Summary Grand Total: "+formatter.format(total1) +"\n";

		//open and write results to filen
		try{
			File files = new File(filen);
			if (!files.exists()) {
				files.createNewFile();
			}

			FileWriter fw = new FileWriter(files.getAbsoluteFile());;
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(results);
			bw.close();
		}
		catch (IOException e){
			e.printStackTrace();
		}

		long endTime = System.currentTimeMillis();
		System.out.println("Processing time (msec): " + (endTime - startTime));
		System.out.println("Results can be found in the file: "+filen);

	}
}
