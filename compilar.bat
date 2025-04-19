@echo off
echo Compilando arquivos Java...

if not exist out mkdir out

javac -d out src\Consts\*.java src\BuscarNomes.java

echo Executando...
cd out
java BuscarNomes
cd ..
