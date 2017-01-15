//Emanuelle (Manny) Crespi
//University of Maryland College Park
//Computer Engineering - Class of 2016

/* 
 * Reads in 3 integers from the keyboard and prints them out in increasing order. A
 * single space is used to separate the numbers in both input and output. For example:
 * Input: 4 -2 9
 * Output: -2 4 9
 */

#include <stdio.h>

int main(){
	int i, j, val, nums[3];
	scanf("%d %d %d", &nums[0], &nums[1], &nums[2]);
	
	for(i = 0; i < 3; i++){
		for(j = i+1; j < 3; j++){
			if(nums[i] > nums[j]){
				val = nums[i];
				nums[i] = nums[j];
				nums[j] = val;
			}
		}
	}
	
	printf("%d %d %d\n",nums[0],nums[1],nums[2]);
	return 0;
}