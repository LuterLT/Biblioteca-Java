@echo off
if not exist lib mkdir lib
echo Baixando sqlite-jdbc para lib\sqlite-jdbc.jar ...
powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/xerial/sqlite-jdbc/3.45.3.0/sqlite-jdbc-3.45.3.0.jar' -OutFile 'lib\\sqlite-jdbc.jar'"
if %ERRORLEVEL% neq 0 (
  echo Falha no download. Tente usar curl ou baixe manualmente.
) else (
  echo Download concluido.
)
echo Baixando SLF4J (api + simple) para lib ...
powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-api/1.7.36/slf4j-api-1.7.36.jar' -OutFile 'lib\\slf4j-api-1.7.36.jar'"
powershell -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/org/slf4j/slf4j-simple/1.7.36/slf4j-simple-1.7.36.jar' -OutFile 'lib\\slf4j-simple-1.7.36.jar'"

