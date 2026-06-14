#!/bin/sh
if [ ! -d bin ]; then
  echo "Pasta bin nao encontrada. Compile primeiro."
  exit 1
fi
echo "Executando aplicacao..."
java -cp "bin:lib/*" Main
