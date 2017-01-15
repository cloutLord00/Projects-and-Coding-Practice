//Emanuelle (Manny) Crespi
//University of Maryland College Park
//Computer Engineering - Class of 2016

/* 
 * Opens an input file, reverses each of the word and writes to an output file. 
 * A word is a string (with less than 20 characters) that ends with anything other 
 * than 0-9, a-z, and A-Z. 
 *
 * For example:
 * Input file: This is ENEE459B. Homework 1 is due on Friday. Good$$?Luck! 
 * Output file: sihT si B954EENE. krowemoH 1 si eud no yadirF. dooG$$?kcuL! 
 *
 * The input and output file names will be provided in the command line.
 */
 
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <stdbool.h>

#define MAX 20

//returns true if char c is end of word
bool endOfWord(char c){
	bool cond1 = (c >= '0' && c <= '9');
	bool cond2 = (c >= 'a' && c <= 'z');
	bool cond3 = (c >= 'A' && c <= 'Z');

	return !(cond1 || cond2 || cond3);
}

//prints the reverse a word
void print_reverse(char *word, int len){
	int i;
	for( i = len - 1; i >= 0; i-- ){
		printf("%c", word[i]);
	}
}

int main( int argc, char *argv[] ) {
	int input, output, i = 0;
	char c, word[MAX];
	
	input = open( argv[1], O_RDONLY );
	output = open( argv[2], O_CREAT|O_RDWR, 0640 );

	//input redirection on input file
	dup2(input, STDIN_FILENO);
	//output redirection on output file
	dup2(output, STDOUT_FILENO);

	while( scanf( "%c", &c ) != EOF ){
		if( endOfWord(c) ){
			print_reverse( word, i );
			printf( "%c", c );
			i = 0;
		}else if( i == 20 ){
			print_reverse( word, i );
			i = 0;
		}else{
			word[i++] = c;
		}
	}

	close(input);
	close(output);
}
