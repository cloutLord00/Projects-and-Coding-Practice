//Emanuelle (Manny) Crespi
//University of Maryland College Park
//Computer Engineering - Class of 2016

/* 
 * Accepts two arrays, A[] and B[], each with up to 10 integers. 
 * A[] is sorted in the ascending order and B[] is sorted in the descending order. 
 * A space is used after each integer input and “Enter” key will be used at the end 
 * of each array. Numbers are printed in ascending order. 
 * For example, if A[] = {1 3 5 7 9} and B[] = {8 6 4 2}, 
 * output will be 1 2 3 4 5 6 7 8 9
 */

#include <stdio.h>
#include <string.h>

#define MAX 10

int main(){
	int C[2*MAX];
	int a_len = 0, b_len = 0, c_len, i, j, val;
	char c = '\0';

	//build A onto C
	for(i = 0; ( c != '\n' ); i++ ){
		scanf("%d%c",&C[i],&c);
	}
	a_len = i;
	c = '\0';

	//build B onto C
	for(j = i; ( c != '\n' ); j++ ){
		scanf("%d%c",&C[j],&c);
	}

	//selection sort on C
	c_len = j;
	for(i = 0; i < c_len; i++){
		for(j = i+1; j < c_len; j++){
			if(C[i] > C[j]){
				val = C[i];
				C[i] = C[j];
				C[j] = val;
			}
		}
	}

	for(j = 0; j < c_len; j++ ){
		printf("%d ", C[j]);	
	}
	printf("\n");
	return 0;
}
