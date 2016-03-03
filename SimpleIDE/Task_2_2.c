#include "simpletools.h"                      
#include "abdrive.h"

#include <stdio.h>
#include <stdlib.h>

int N;
int *List;

void Swap(int i, int j)
{
	int aux;
	aux = List[i];
	List[i] = List[j];
	List[j] = aux;
}

void Read()
{
	print("Enter the number of elements\n");
	scanf("%d", &N);
	List = (int*)malloc(N * sizeof(int));

	print("Enter %d integers\n", N);
	for (int i = 0, x; i < N; i++)
	{
		scanf("%d", &x);
		List[i] = x;
	}
}

void Print(int Step)
{
	if (Step)
		print("\nThe list at step %d:\n", Step);
	else
		print("\n\nSorted list in ascending order:\n");

	for (int i = 0; i<N; i++)
		print("%d ", List[i]);
}

void BubbleSort()
{
	int Step = 0;   
   for (int i = 0; i < N; i++)
		for (int j = 0; j < N - 1; j++)
			if (List[j] > List[j+1])
       {
				Swap(j, j+1);
    			Step++;
				Print(Step);
			}
}

int main()
{
	Read();
	BubbleSort();
	Print(0);
	return 0;
}
