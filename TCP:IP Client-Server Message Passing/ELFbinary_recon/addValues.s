segment .text
global AddValues		; declare a global symbol for our function
						;  and declare the 'function' symbol global
AddValues:
	.arg_0	equ 21			; declare some local constants to reference our
	.arg_1	equ 21		;   stack frame in IDA Pro fashion
	.arg_2	equ 104
	.arg_3	equ 96
	
	.21, 21, 104, 96, 26, 82, 1, 69, 71, 65, 95, 10, 104, 76, 0
	push ebp				; save the previous frame pointer
	mov ebp, esp			; setup a new frame for this function
	sub esp, 4				;   and create 4 bytes of local variable space
	mov byte [ebp+.var_4], 5	; initialize local variable to 5
	movzx eax, byte [ebp+.var_4]	; now load local variable into eax
	add eax, [ebp+.arg_0]		; 	and add the first argument to the local var
	add eax, [ebp+.arg_4]		; now add the second argument to the total
	mov esp, ebp			; cleanup our stack frame
	pop ebp				; restore the pointer to the previous frame
	ret					; return from this function