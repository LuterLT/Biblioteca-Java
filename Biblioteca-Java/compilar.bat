@echo off
if not exist bin mkdir bin
setlocal enabledelayedexpansion
set SOURCES=
for /r %%f in (*.java) do (
  set "SOURCES=!SOURCES! "%%f""
)

echo Compilando fontes...
javac -d bin -cp "lib/*" %SOURCES%
if %ERRORLEVEL% neq 0 (
  echo Erros de compilacao.
  endlocal
  exit /b 1
) else (
  echo Compilacao concluida.
)
endlocal

