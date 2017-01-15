//Emanuelle (Manny) Crespi
//University of Maryland College Park
//Computer Engineering - Class of 2016

/* 
 * Read in a positive integer (using %d) and print it out digit by
 * digit from the 1’s digit, 10’s digit, and so on. 
 * 
 * For example: 
 * Input: 384205
 * Output: 5 0 2 4 8 3
 */

#include <stdio.h>

int main(){
	unsigned int num, temp;

	scanf("%d",&num);
        
        if( num == 0 ){
          printf("0 \n");
          return 0;
        }

	while( num >= 1 ){
		temp = num % 10;

		printf("%d ", temp);

		num /= 10;
	}
	printf("\n");
	return 0;
}
