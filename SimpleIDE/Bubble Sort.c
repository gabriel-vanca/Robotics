
#include "simpletools.h"                      
#include "abdrive.h"

#include <stdio.h>
#include <stdlib.h>

typedef struct _node
{
	int val;
   struct _node *next;
} node;

node *List;
int N;

void Add(int val)
{
	node *p = (node*)malloc(sizeof(node));
	p->next = List;
	p->val = val;
	List = p;
}

void Swap(node *p, node *r)
{
	int aux;
	aux = p->val;
	p->val = r->val;
	r->val = aux;
}

void Read()
{
	printf("Enter the number of elements\n");
	scanf("%d", &N);
	printf("Enter %d integers\n", N);

	List = (node*) malloc(sizeof(node));
	int x;
	scanf("%d", &x);
	List->val = x;
	List->next = 0;

	for (int i = 2; i <= N; i++)
	{
		scanf("%d", &x);
		Add(x);
	}
}

void Sort()
{
	node *p, *r;
	for (p = List; p->next; p = p->next)
		for (r = p->next;r; r = r->next)
			if (r->val < p->val)
				Swap(p, r);
}

void Print()
{
	printf("\n\nSorted list:\n");
	for (node *p = List; p; p = p->next)
		printf("%d ", p->val);
}

int main()                                    // Main function
{
	Read();
	Sort();
	Print();
	return 0;
}
