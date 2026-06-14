#!/bin/sh
mkdir -p bin
javac -d bin -cp "lib/sqlite-jdbc.jar" src/**/*.java src/*.java
if [ $? -ne 0 ]; then
  echo "Erros de compilacao."
else
  echo "Compilacao concluida."
fi
