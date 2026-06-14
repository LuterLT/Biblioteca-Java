#!/bin/sh
mkdir -p lib
echo "Baixando sqlite-jdbc para lib/sqlite-jdbc.jar ..."
if command -v curl >/dev/null 2>&1; then
  curl -L -o lib/sqlite-jdbc.jar https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.45.3.0/sqlite-jdbc-3.45.3.0.jar
elif command -v wget >/dev/null 2>&1; then
  wget -O lib/sqlite-jdbc.jar https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.45.3.0/sqlite-jdbc-3.45.3.0.jar
else
  echo "curl ou wget não encontrado. Baixe manualmente o JAR e coloque em lib/sqlite-jdbc.jar"
fi
echo "Baixando SLF4J (api + simple) para lib ..."
if command -v curl >/dev/null 2>&1; then
  curl -L -o lib/slf4j-api-1.7.36.jar https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar
  curl -L -o lib/slf4j-simple-1.7.36.jar https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/1.7.36/slf4j-simple-1.7.36.jar
elif command -v wget >/dev/null 2>&1; then
  wget -O lib/slf4j-api-1.7.36.jar https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar
  wget -O lib/slf4j-simple-1.7.36.jar https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/1.7.36/slf4j-simple-1.7.36.jar
else
  echo "curl ou wget não encontrado. Baixe manualmente os jars do SLF4J para lib/"
fi
