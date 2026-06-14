@echo off
if not exist bin (
  echo Pasta bin nao encontrada. Compile primeiro.
  exit /b 1
)
echo Executando aplicação...
java -cp "bin;lib/*" Main
