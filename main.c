#include <stdio.h>

int sumar(int, int);

int main(){
    printf("El resultado de la suma: %d\n", sumar(2,2));
}

int sumar(int a, int b){
    return a+b;
}