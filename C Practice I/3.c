//Emanuelle (Manny) Crespi
//University of Maryland College Park
//Computer Engineering - Class of 2016

/* 
 * Read in a positive integer (maybe very large) from 
 * keyboard and print it out digit by digit in English. For example:
 * Input: 384205
 * Output: three eight four two zero five
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX 10

int main(){
	int i, len;
	char num[MAX +1];
	scanf("%s",num);
        len = strlen(num);
	for( i = 0; i < len; i++ ){
		switch(num[i]){
			case '0':
                printf("zero "); 
                break; 

            case '1':
				printf("one ");
				break;

			case '2':
				printf("two ");
				break;

			case '3':
				printf("three ");
				break;	

			case '4':
				printf("four ");
				break;

			case '5':
				printf("five ");
				break;

			case '6':
				printf("six ");
				break;

			case '7':
				printf("seven ");
				break;

			case '8':
				printf("eight ");
				break;

			case '9':
				printf("nine ");
				break;

			default:
				break;
		}
	}
	printf("\n");
	return 0;
}
