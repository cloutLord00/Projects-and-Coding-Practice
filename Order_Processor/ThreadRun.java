import java.awt.List;
import java.io.BufferedReader;
import java.util.HashMap;

public class ThreadRun extends Thread{
	private int id;
	private Thread Th;
	private OrderP op;
	private BufferedReader file;

	public ThreadRun(int id,OrderP op, BufferedReader file)
	{
		this.id=id;
		this.op=op;
		this.file=file;
	}

	public void run()
	{
		op.processOrder(this.file,this.id);		
		//System.out.println("Thread "+ id+" exiting");
	}

	public void start()
	{   //System.out.println("starting "+id);
		if(Th==null){			
			Th=new Thread(this,id+"");
			Th.start();
		}
	}
}
