package Modelos;
import java_cup.runtime.*;
%%

%cupsym Simbolo
%class Scanner
%cup
%public
%unicode
%line
%char
%ignorecase

D = [0-9]+
C = ([a-zA-Z]|[0-9])*
K = \"[a-z A-Z]*\"
R = \"(\/[a-z A-Z ]*)+.[a-zA-Z]+\"
 LineTerminator = \r|\n|\r\n
 InputCharacter = [^\r\n]
 WhiteSpace     = {LineTerminator} | [ \t\f]

%%

"<page>" {return new Symbol(Simbolo.MAIN, yychar,yyline);}
"</page>" {return new Symbol(Simbolo.MAIN_FIN, yychar,yyline);}
"h1" {return new Symbol(Simbolo.SUB, yychar,yyline);}
"h2" {return new Symbol(Simbolo.SUBT, yychar,yyline);}
"p" {return new Symbol(Simbolo.PARRAFO, yychar,yyline);}
"check" {return new Symbol(Simbolo.OK, yychar,yyline);}
"list" {return new Symbol(Simbolo.LISTA, yychar,yyline);}
"onload" {return new Symbol(Simbolo.REFRESH, yychar,yyline);}
"primary key" {return new Symbol(Simbolo.PKEY, yychar,yyline);} 
"bit" {return new Symbol(Simbolo.BOOL, yychar,yyline);}
"values" {return new Symbol(Simbolo.VALORES, yychar,yyline);}
";" {return new Symbol(Simbolo.ENDLINE, yychar,yyline);}
"=" {return new Symbol(Simbolo.ASIGNACION, yychar,yyline);}
"(" {return new Symbol(Simbolo.INICIO_ATRIBUTOS, yychar,yyline);}
")" {return new Symbol(Simbolo.FIN_ATRIBUTOS, yychar,yyline);}
"<head>" {return new Symbol(Simbolo.ENCABEZADO, yychar,yyline);}
"</head>" {return new Symbol(Simbolo.ENCABEZADO_FIN, yychar,yyline);}
"title" {return new Symbol(Simbolo.TITULO, yychar,yyline);}
"include" {return new Symbol(Simbolo.LIBRERIAS, yychar,yyline);}
"width" {return new Symbol(Simbolo.ANCHO, yychar,yyline);}
"height" {return new Symbol(Simbolo.ALTO, yychar,yyline);}
"text" {return new Symbol(Simbolo.TEXTO, yychar,yyline);}
"<body>" {return new Symbol(Simbolo.CUERPO, yychar,yyline);}
"</body>" {return new Symbol(Simbolo.CUERPO_FIN, yychar,yyline);}
"create" {return new Symbol(Simbolo.CREAR, yychar,yyline);}
"read" {return new Symbol(Simbolo.LEER, yychar,yyline);}
"update" {return new Symbol(Simbolo.ACTUALIZAR, yychar,yyline);}
"delete" {return new Symbol(Simbolo.ELIMINAR, yychar,yyline);}
"style" {return new Symbol(Simbolo.ESTILOS, yychar,yyline);}
"<div>" {return new Symbol(Simbolo.SECCION, yychar,yyline);}
"</div>" {return new Symbol(Simbolo.SECCION_FIN, yychar,yyline);}
"button" {return new Symbol(Simbolo.BOTON, yychar,yyline);}
"onClick" {return new Symbol(Simbolo.PRESS, yychar,yyline);}
"focus" {return new Symbol(Simbolo.PASS, yychar,yyline);}
"input" {return new Symbol(Simbolo.ENTRADA, yychar,yyline);}
"image" {return new Symbol(Simbolo.IMAGEN, yychar,yyline);}
"class" {return new Symbol(Simbolo.CLASE, yychar,yyline);}
"{" {return new Symbol(Simbolo.INICIO_SECCION, yychar,yyline);}
"}" {return new Symbol(Simbolo.FIN_SECCION, yychar,yyline);}
"user" {return new Symbol(Simbolo.USUARIO, yychar,yyline);}
"id" {return new Symbol(Simbolo.IDE, yychar,yyline);}
"userid" {return new Symbol(Simbolo.USID, yychar,yyline);}
"password" {return new Symbol(Simbolo.CONTRASENA, yychar,yyline);}
"href" {return new Symbol(Simbolo.HREF, yychar,yyline);}
"varchar2" {return new Symbol(Simbolo.VAR, yychar,yyline);}
"char" {return new Symbol(Simbolo.CARACTER, yychar,yyline);}
"number" {return new Symbol(Simbolo.NUMERO, yychar,yyline);}
{D} { return new Symbol(Simbolo.INT, yychar,yyline, new String(yytext()));}
{R} { return new Symbol(Simbolo.RUTA, yychar,yyline, new String(yytext()));}
{K} { return new Symbol(Simbolo.KADENA, yychar,yyline, new String(yytext()));}
{C} { return new Symbol(Simbolo.ALFANUMERICO, yychar,yyline, new String(yytext()));}

{WhiteSpace}                 { /* ignore */ }
. { System.out.println("Error lexico: "+yytext()); }
