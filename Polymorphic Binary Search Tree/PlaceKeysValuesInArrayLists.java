package tree;
import java.util.*;

/**
 * This task places key/values in two arrays in the order
 * in which the key/values are seen during the traversal.  If no keys/values
 * are found the ArrayList will be empty (constructor creates two
 * empty ArrayLists).  
 *
 * @param <K>
 * @param <V>
 */
public class PlaceKeysValuesInArrayLists<K,V> implements TraversalTask<K, V> {
	
	//instance fields
	private ArrayList<K> listOfKeys;
	private ArrayList<V> listOfValues;
	
	/**
	 * Creates two ArrayList objects: one for the keys and one for the values.
	 */
	public PlaceKeysValuesInArrayLists() {
		this.listOfKeys = new ArrayList<K>();
		this.listOfValues = new ArrayList<V>();
	}
	
	/**
	 * Adds key/value to the corresponding ArrayLists.
	 */
	public void performTask(K key, V value) {
		this.listOfKeys.add(key);
		this.listOfValues.add(value);
	}

	/**
	 * Returns reference to ArrayList holding keys.
	 * @return ArrayList
	 */
	public ArrayList<K> getKeys() {
		return this.listOfKeys;
	}
	
	/**
	 * Returns reference to ArrayList holding values.
	 * @return ArrayList
	 */
	public ArrayList<V> getValues() {
		return this.listOfValues;
	}
}
