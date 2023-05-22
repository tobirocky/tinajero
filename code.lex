/*analisis lexico*/
%{

#include<stdio.h>
    
%}

digit       [0-9]
number         {digit}+
letter      [a-zA-Z]
indentifier {letter}+{number}+
newline             \newline
whitespace  [\t]+
simbolos    [@|#|&]+
conditional [if|then|else|for]

%option noyywrap

%%

{digit}     {printf("Encontrado un digito: %s\n",yytext);}
{simbolos}  {printf("An special character found: %s\n",yytext);}
{number}    {printf("Encontrado un numero: %s\n",yytext);}
{indentifier}   {printf("Encontrado un indentificador: %s\n",yytext);}
{conditional} {printf("palabra reservada : %s\n",yytext);} 
.           {}

%%

int main(void)
{
    yylex();
    return 0;
}