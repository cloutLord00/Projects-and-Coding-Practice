/**
* Word frequency counter
 */
package frequency;
import java.util.Iterator;
/**
 *
 * @author UMD CS
 */
public class Frequency<E extends Comparable<E>> implements Iterable<E>{
    private Node first;
    private int N;
    Frequency(){
        N = 0;
        first = null;
    }

    @Override
    public Iterator<E> iterator() {
        return new ListIterator();
    }
    /**
     * 
     * List iterator
     *
     */
    private class ListIterator implements Iterator<E>{
        private Node current;
        private int index ;
        ListIterator(){
            current = first;
            index = 0;
        }
                
        @Override
        public boolean hasNext() {
            return current != null;
        }

        
        public E next() {
            if(!hasNext()){
                return null;
            }
            E word = current.key;
            int count = current.count;
            String r = "("+word + "," + Integer.toString(count)+")";
            current = current.next;
            return (E)r;
        }

        @Override
        public void remove() {
            
        } 
    }
    /**
     * 
     * Node class
     *
     */
    private class Node implements Comparable<Node>{
        private E key;
        private int count;
        private Node next;
        Node(E item){
            key = item;
            count = 1;
            next = null;
        }
        @Override 
        public String toString(){
        	return "("+key +","+count+")";
        }
		
		@Override
		//prioritize frequency for sorting Nodes
		public int compareTo(Frequency<E>.Node o) {
			if(this.count > o.count){
				return 1;
			}
			else if( this.count < o.count ){
				return -1;
			}
			else{
				//when the frequencies are the same
				int comp = this.key.compareTo(o.key);
				
				//fixes the natural ordering for this list
				if( comp > 0 ){
					return -1;
				}else if ( comp < 0 ){
					return 1;
				}else{
					return 0;
				}
			}
		}
    }
    
    //Necessary for removal of a linked list Node from the list
    //Searches for a word in the list
    //returns Node with the word after removing it from the list
    //n should never be null
    private Node remove(Node n){
    	if( first == null ){
    		return null;
    	}
    	
    	Node prev = null, curr = first;
    	while( curr != null && !curr.key.equals(n.key) ){
    		prev = curr;
    		curr = curr.next;
    	}
    	
    	//node was found at the head
    	if( prev == null && curr == first ){
    		first = first.next;  //set new head
    		curr.next = null;	 //remove the node
    		return curr;		 //return the node
    	}
    	//re-establish linked list links for removal
    	else if( prev != null && curr != null ){
    		prev.next = curr.next;
    		curr.next = null;
    		return curr;
    	}
    	//node was never found
    	else{
    		return null;
    	}
    }
    
    
    /*
     * Inserts a word into the linked list. If the word exists, increment the 
     * count by q. 
     */
    public void insert(E word){
        if( word == null || word.equals("") ){
            return;
        }
        
        Node newNode = new Node(word);
        Node temp = remove( newNode );
        //System.out.println("Removed " + temp);
        //increment frequency if the Node is found/removed
        if( temp != null ){
        	newNode = temp;
        	newNode.count++;
        }
        
        //perform insert
        Node prev = null;
        temp = first;
        
        //walk the list
        while(temp != null && temp.compareTo(newNode) >= 0 ){
        	prev = temp;
        	temp = temp.next;
        }
        
        //insert a new head
        if( prev == null ){
        	newNode.next = first;
        	first = newNode;
        }
        //perform normal insert
        else{
        	newNode.next = temp;
        	prev.next = newNode;
        }
        
        //System.out.println("Inserted " + newNode);
    }
    
    /**
     * 
     * @param str input string
     * This method splits a string into words and pass the words to insert method
     * 
     */
    public void insertWords(String str){
        String delims = "[ .,?!'\"()}{;/<>&=#-:\\ _]+";
        String[] words = str.split(delims);
        for(String s: words){
            s = s.toLowerCase();
            insert((E)s);
        }
    }
    /**
     *  prints the word frequency list
     */
    
    public void print(){
        Node c = first;
        while(c != null){
            System.out.print("("+c.key + "," + c.count+")");
            c = c.next;
        }
        System.out.print("\n");
    }
}
    
