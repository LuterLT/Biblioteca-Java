# 📚 Sistema de Controle de Biblioteca

Sistema desktop para gerenciamento de uma biblioteca, desenvolvido em Java com interface gráfica Swing e banco de dados SQLite. Projeto acadêmico desenvolvido para a disciplina de Programação Orientada a Objetos.

---

## 💡 Sobre o projeto

O sistema permite que funcionários de uma biblioteca controlem o acervo de obras, gerenciem leitores e registrem empréstimos e reservas — tudo por meio de uma interface gráfica simples e intuitiva.

O banco de dados é criado automaticamente na primeira execução, sem necessidade de configuração manual.

---

## ✨ Funcionalidades

- **Leitores** — cadastro, edição e exclusão de leitores, com controle de bloqueio
- **Funcionários** — cadastro e gerenciamento dos funcionários da biblioteca
- **Livros** — cadastro do acervo com título, autor e ISBN
- **Cópias** — controle dos exemplares físicos de cada livro e sua disponibilidade
- **Empréstimos** — registro de empréstimos e devoluções, com cálculo automático de multa por atraso
- **Reservas** — criação e cancelamento de reservas de livros

---

## 🗂️ Estrutura do projeto

```
Biblioteca-Java/
 src/
   modelo/       → Classes que representam os dados (Leitor, Livro, Copia, Emprestimo...)
   dao/          → Camada de acesso ao banco de dados (um DAO por entidade)
   ui/           → Telas da interface gráfica em Swing
   exceptions/   → Exceções customizadas do sistema
   Main.java     → Ponto de entrada da aplicação
 lib/            → Driver do SQLite (baixado automaticamente pelo script)
 bin/            → Arquivos compilados (.class)
```

---

## 🧱 Conceitos de OO aplicados

| Conceito | Onde é aplicado |
|---|---|
| **Herança** | `Leitor` e `Funcionario` herdam de `Pessoa` |
| **Abstração** | `Pessoa` é uma classe abstrata — nunca instanciada diretamente |
| **Polimorfismo** | `validar()` e `exibirDados()` são sobrescritos em cada subclasse |
| **Encapsulamento** | Atributos privados acessados via getters/setters |
| **Interface** | `Cadastravel` (modelo) e `DAO<T>` (persistência) |
| **Agregação** | Um `Livro` possui várias `Copia` |
| **Associação** | `Emprestimo` referencia `Leitor`, `Copia` e `Funcionario` |
| **Exceções customizadas** | `LivroIndisponivelException`, `LeitorBloqueadoException`, `DadosInvalidosException` |

---

## 🗃️ Banco de dados

O sistema usa **SQLite** — um banco de dados em arquivo, sem necessidade de instalar servidor.

O arquivo `biblioteca.db` é criado automaticamente na raiz do projeto na primeira vez que o sistema roda. As tabelas criadas são:

- `leitor`
- `funcionario`
- `livro`
- `copia`
- `emprestimo`
- `reserva`

---

## 🖥️ Telas do sistema

Todas as telas seguem o mesmo padrão:

- **Tabela** com os registros cadastrados
- **Formulário** lateral para preenchimento dos dados
- **Botões:** Salvar, Atualizar, Excluir e Limpar

Nas telas de **Empréstimos** e **Reservas** há botões adicionais para registrar devolução e cancelar reserva.

---

## ⚠️ Validações

- **CPF** — deve conter exatamente 11 dígitos numéricos
- **E-mail** — deve conter o caractere `@`
- **Matrícula** — formato `MATxxx` (ex: MAT001), preenchido com máscara automática
- **ISBN** — formato `###-##-#####-##-#`, preenchido com máscara automática
- Campos obrigatórios são verificados antes de salvar

Erros são exibidos em caixas de diálogo sem travar o sistema.

---

## 👥 Desenvolvedores

Projeto desenvolvido em dupla como trabalho avaliativo da disciplina de POO.