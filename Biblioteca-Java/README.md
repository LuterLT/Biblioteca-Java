# Biblioteca-Java

Projeto simples de sistema de controle de biblioteca em Java (sem Maven/Gradle).

Requisitos:
- JDK 11+

Passos rápidos:

1. Rodar o script de download (uma vez):

Windows:

```
baixar-dependencias.bat
```

Linux/Mac:

```
./baixar-dependencias.sh
```

2. Compilar:

Windows:

```
compilar.bat
```

Linux/Mac:

```
./compilar.sh
```

3. Executar:

Windows:

```
executar.bat
```

Linux/Mac:

```
./executar.sh
```

Observações:
- O banco `biblioteca.db` será criado automaticamente na primeira execução.
- Para testar via linha de comando há a classe `TesteManual` em `src`.
- Se usar VS Code, a configuração que referencia o JAR do SQLite está em `.vscode/settings.json`.
