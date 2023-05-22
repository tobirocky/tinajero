/*Declaring two counters one for numbers of lines other for numbers of characters*/
%{
int no_of_lines=0;
int no_of_chars=0;
%}

/***rule 1 counts the number of lines, rule 2 counts the numbers of characters and rule 3 specifies when to stop taking input***/
%%
\n       ++no_of_lines;
.        ++no_of_chars;
end      return 0;
%%

/***User code section***/
int yywrap(){}
int main(int argc,char**argv)
{
    yylex();
    printf("number of lines = %d,number of chars = %d\n",no_of_lines,no_of_chars);

    return 0;
}