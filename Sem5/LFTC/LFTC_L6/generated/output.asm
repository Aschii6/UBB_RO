bits 32

global start

extern exit
extern scanf
extern printf

section .data
	a dd 0
	b dd 0
	input_format db "%d", 0
	output_format db "%d", 10, 0

section .text

start:
	mov dword [a], 2

	push dword b
	push dword input_format
	call scanf
	add esp, 4 * 2
	
	mov eax, [b]
	imul eax, 3
	mov ebx, eax

	mov eax, [a]
	add eax, ebx
	mov ebx, eax

	mov [a], eax

	push dword [a]
	push dword output_format
	call printf
	add esp, 4 * 2
	
	push dword 0
	call exit
