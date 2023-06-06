//Código

#include <stdio.h>
void myfunc(void);
int mysum(int a, int b);
int main(int argc, char** argv)
{
myfunc();
int result = mysum(100,32);
printf("%i + %i = %i\n\n", 100, 5, result);
return 0;
}
void myfunc(void)
{
printf("My func called\n\n");
}
int mysum(int a, int b)
{
return a + b;
}
