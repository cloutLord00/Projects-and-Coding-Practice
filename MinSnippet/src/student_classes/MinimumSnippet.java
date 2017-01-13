package student_classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * When you do a web search, the results page shows you a <a
 * href="http://searchengineland.com/anatomy-of-a-google-snippet-38357"
 * >snippet</a> for each result, showing you search terms in context. For
 * purposes of this project, a snippet is a subsequence of a document that
 * contains all the search terms.
 * 
 * For this project, you will write code that, given a document (a sequence of
 * words) and set of search terms, find the minimal length subsequence in the
 * document that contains all of the search terms.
 * 
 * If there are multiple subsequences that have the same minimal length, you may
 * return any one of them.
 * 
 */
public class MinimumSnippet {
	public static List<String> docList = new ArrayList<String>();
	public static List<String> termList = new ArrayList<String>();
	public ArrayList<Integer> positionOfTerms;
	public int start;
	public int end;
	public int length;
	public int startPosition;
	public int endPosition;
	public int lengthTotal;

	/**
	 * Compute minimum snippet.
	 * 
	 * Given a document (represented as an List<String>), and a set of terms
	 * (represented as a List<String>), find the shortest subsequence of the
	 * document that contains all of the terms.
	 * 
	 * This constructor should find the minimum snippet, and store information
	 * about the snippet in fields so that the methods can be called to query
	 * information about the snippet. All significant computation should be done
	 * during construction.
	 * 
	 * @param document
	 *            The Document is an Iterable<String>. Do not change the
	 *            document. It is an Iterable, rather than a List, to allow for
	 *            implementations where we scan very large documents that are
	 *            not read entirely into memory. If you have problems figuring
	 *            out how to solve it with the document represented as an
	 *            Iterable, you may cast it to a List<String>; in all but a very
	 *            small number of test cases, in will in fact be a List<String>.
	 * 
	 * @param terms
	 *            The terms you need to look for. The terms will be unique
	 *            (e.g., no term will be repeated), although you do not need to
	 *            check for that. There should always be at least one term and
	 *            your code should throw an IllegalArgumentException if "terms"
	 *            is empty.
	 * 
	 * 
	 */
	public MinimumSnippet(Iterable<String> document, List<String> terms) {

		/*
		 * Intializing the variables used for this method
		 */

		// makes docList and listOfTerms a new array list of strings
		docList = new ArrayList<String>();
		termList = new ArrayList<String>();
		// creates temporary lists
		List<String> docTemp = new ArrayList<String>();
		List<String> termTemp = new ArrayList<String>();
		// sets the total length equal to the maximum value that the integer can hold
		lengthTotal = Integer.MAX_VALUE;

		/*
		 * Copying all the information from the parameters to 
		 * the instance variables and the temporary lists.
		 */
		
		//copies all the terms to listOfTerms and the temporary terms 
		for (String newTerm : terms) {
			termList.add(newTerm);
			termTemp.add(newTerm);
		}

		//copies all the contents in document to docList and the temporary docList 
		for (String newDoc : document) {
			docList.add(newDoc);
			docTemp.add(newDoc);
		}

		//intializes variables to equal the size of the temporary lists
		int tempTermSize = termTemp.size();

		//if the size is less than 1, throw an exception because the size of the list cannot be lesst than 1
		if (tempTermSize < 1) {
			throw new IllegalArgumentException();
			
		//if there is only one element in the list 
		} else if (tempTermSize == 1) {
			//initalize variables used in the while-loops 
			int count = 0, newCount = 0;
			
			//nested while loops 
			while (count < termTemp.size()) {
				//checking that the iterator is less than the size of a document list 
				while (newCount < docTemp.size()) {
					
					//getting the index and storing in variables 
					String getIdxTerm = termTemp.get(count);
					String getIdxDoc = docTemp.get(newCount);

					//checking if the index of the term is equal to the index of the document s
					if (getIdxTerm.equals(getIdxDoc)) {
						
						//creates a new list of integers for position of Terms
						this.positionOfTerms = new ArrayList<Integer>();
						this.positionOfTerms.add(newCount);
						//set the endPosition equal to the newCount iterator
						this.startPosition = newCount;
						//set the endPosition equal to the newCount iterator
						this.endPosition = newCount;
						//total length is one because tempTermSize is 1
						this.lengthTotal = 1;
						//break out of the loop
						break;
					}
					
					//increases first iterator by 1 
					newCount = newCount + 1;

				}

				//increasing the second iterator by 1 
				count = count + 1;
			}
		
		// if the iterator size of the term list is greater than 1
		} else {
			//initialize loop variable 
			int counter = 0;
			//creates a new integer list 
			ArrayList<Integer> xyz = new ArrayList<Integer>();

			//checks if the document list contains the terms in the parameter 
			while (docTemp.containsAll(terms)) {
				//creates a new integer list for xyz
				xyz = new ArrayList<Integer>();
				
				//for each loop which iterates through each term of the term list 
				for (String newTerm : terms) {
					//adds to the xyz list 
					xyz.add(docTemp.indexOf(newTerm));
				}
				
				//uses collections to sort the list instead of writing a method 
				Collections.sort(xyz);
				
				//stores the calculation of the size minus 1 in an integer 
				int size = xyz.size() - 1;
				
				//gets the last element in the list  
				this.end = xyz.get(size);
				//gets the 0 element in the list 
				this.start = xyz.get(0);
				//significant variable representing 0
				int zero = 0;

				//if the start is equal to 0
				if (this.start == zero) {
					//length is equal to the end value of the list plus 1  
					this.length = this.end + 1;
					
				// if the start is not equal to 0
				} else {
					//length is equal to the end value of list minus the start of the list plus 1 
					this.length = this.end - this.start + 1;
				}
				
				//if the temporary length is less than the final length
				if (this.length < this.lengthTotal) {
					
					this.positionOfTerms = new ArrayList<Integer>();
					this.startPosition = this.start + counter;
					this.endPosition = this.end + counter;
					this.lengthTotal = this.length;

					//iterates through the list of strings 
					for (String newTerm : terms) {
						positionOfTerms.add(docTemp.indexOf(newTerm) + counter);
					}
				}

				//removes the first element of the list because it is not neccessary anymore 
				docTemp.remove(this.start);
				//increment counter plus 1 
				counter = counter + 1;
			}
		}
	}

	/**
	 * Returns whether or not all terms were found in the document. If all terms
	 * were not found, then none of the other methods should be called.
	 * 
	 * @return whether all terms were found in the document.
	 */
	public boolean foundAllTerms() {
		//creates a variable for the return value 
		boolean returnVal = false;
		
		//gets the size of the term list 
		int termListSize = termList.size();
		
		//nested for loop 
		for (int idx = 0; idx < termList.size(); idx++) {
			for (int otherIdx = 0; otherIdx < docList.size(); otherIdx++) {
				//if the term of a specific index of the term list is  equal 
				//to the the term of a specific index of the document list 
				if (termList.get(idx).equals(docList.get(otherIdx))) {
					//decrements termListSize by one 
					termListSize = termListSize - 1;
					//breaks out of the loop
					break;
				}
			}
		}
		
		//if the size of term list is equal to 0 then return true 
		if (termListSize == 0) {
			returnVal = true;
		//otherwise return false
		} else {
			returnVal = false;
		}

		//returns whatever is in the if-else statement
		return returnVal;
	}

	/**
	 * Return the starting position of the snippet
	 * 
	 * @return the index in the document of the first element of the snippet
	 */
	public int getStartingPos() {
		//Return the starting position of the snippet
		return this.startPosition;
	}

	/**
	 * Return the ending position of the snippet
	 * 
	 * @return the index in the document of the last element of the snippet
	 */
	public int getEndingPos() {
		//Return the ending position of the snippet
		return this.endPosition;
	}

	/**
	 * Return total number of elements contained in the snippet.
	 * 
	 * @return
	 */
	public int getLength() {
		return this.lengthTotal;
	}

	/**
	 * Returns the position of one of the search terms as it appears in the
	 * original document
	 * 
	 * @param index
	 *            index of the term in the original list of terms. For example,
	 *            if index is 0 then the method will return the position (in the
	 *            document) of the first search term. If the index is 1, then
	 *            the method will return the position (in the document) of the
	 *            second search term. Etc.
	 * 
	 * @return position of the term in the document
	 */
	public int getPos(int index) {
		//Returns the position of one of the search terms as it appears in the
		//original document
		return this.positionOfTerms.get(index);
	}

}