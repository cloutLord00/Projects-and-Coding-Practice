//Emanuelle (Manny) Crespi
//University of Maryland College Park
//Computer Engineering - Class of 2016

/* 
 * Prints out the calendar of January. 2016 in the same format as “cal 1 2016”.
 */

#include <stdio.h>
#include <string.h>

int main(){
	int i;
	printf("   January 2016\n");
	printf(" S  M Tu  W Th  F  S\n");
	printf("               ");
	
	for( i = 1; i < 10; i++ ){
		
		if( i == 2 || i == 9 ){
			printf(" %d\n", i);
		}else{
			printf(" %d ",i);
		}
	}

	for( i = 10; i < 31; i++ ){
		
		if( i == 16 || i == 23 || i == 30 ){
			printf("%d\n", i);
		}else{
			printf("%d ", i);
		}
	}

	printf("%d \n",i);
	return 0;
}
