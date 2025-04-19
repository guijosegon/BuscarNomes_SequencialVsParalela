#!/bin/bash
echo "Compilando arquivos Java..."

mkdir -p out

javac -d out src/Consts/*.java src/BuscarNomes.java

echo "Executando..."
cd out
java BuscarNomes
cd ..
