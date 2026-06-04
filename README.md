# Sistema de Biblioteca — Documentação de Classes

---

## Classes Abstratas

### `Pessoa`

| Visibilidade | Atributo / Método | Tipo / Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `-` | `id` | `int` | — | — |
| `-` | `nome` | `String` | — | — |
| `-` | `cpf` | `String` | — | — |
| `-` | `email` | `String` | — | — |
| `-` | `telefone` | `String` | — | — |
| `+` | `getNome()` | `String` | — | — |
| `+` | `toString()` *(abstract)* | `String` | — | Obriga `Leitor` e `Funcionario` a sobrescrever |
| `+` | `validar()` *(abstract)* | `boolean` | — | Obriga `Leitor` e `Funcionario` a sobrescrever |

> **Descrição:** Superclasse abstrata que centraliza os dados pessoais de qualquer pessoa do sistema. Nunca é instanciada diretamente. `toString()` e `validar()` são abstratos — cada subclasse define sua própria implementação, garantindo **polimorfismo de sobrescrita**. `validar()` em `Leitor` verifica limite de empréstimos; em `Funcionario` verifica cargo ativo.

---

## Classes Concretas

### `Leitor`

| Visibilidade | Atributo / Método | Tipo / Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `-` | `matricula` | `String` | — | — |
| `-` | `limiteEmprestimos` | `int` | — | — |
| `+` | `validar()` | `boolean` | — | Sobrescreve `Pessoa.validar()` |
| `+` | `toString()` | `String` | — | Sobrescreve `Pessoa.toString()` |
| `+` | `getEmprestimosAtivos()` | `List<Emprestimo>` | **Associação:** `Leitor` → `Emprestimo` *(1..*)* | — |
| `+` | `getReservasAtivas()` | `List<Reserva>` | **Associação:** `Leitor` → `Reserva` *(1..*)* | — |

> **Descrição:**
> - `validar()` — verifica se o número de empréstimos ativos não ultrapassou `limiteEmprestimos`. Retorna `false` se bloqueado.
> - `getEmprestimosAtivos()` — retorna todos os empréstimos com `status != FINALIZADO`. Usado pela tela de empréstimo para exibir histórico.
> - `getReservasAtivas()` — retorna reservas com `isAtiva() == true`. Usado para checar conflitos antes de nova reserva.
> - **Herança de `Pessoa`:** herda `id`, `nome`, `cpf`, `email`, `telefone` e os getters correspondentes.

---

### `Funcionario`

| Visibilidade | Atributo / Método | Tipo / Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `-` | `matricula` | `String` | — | — |
| `-` | `cargo` | `String` | — | — |
| `+` | `validar()` | `boolean` | — | Sobrescreve `Pessoa.validar()` |
| `+` | `toString()` | `String` | — | Sobrescreve `Pessoa.toString()` |
| `+` | `registrarEmprestimo()` | `void` | **Associação:** `Funcionario` → `Emprestimo` *(1..*)* | — |
| `+` | `registrarDevolucao()` | `void` | **Associação:** `Funcionario` → `Emprestimo` *(1..*)* | — |

> **Descrição:**
> - `validar()` — verifica se o cargo está preenchido e se a matrícula é única no sistema.
> - `registrarEmprestimo()` — aciona o controller para criar um `Emprestimo`, vinculando o funcionário como operador do registro para fins de rastreabilidade.
> - `registrarDevolucao()` — aciona o controller para finalizar um `Emprestimo` e atualizar o `StatusCopia` para `DISPONIVEL`.
> - **Herança de `Pessoa`:** herda todos os atributos e getters de dados pessoais.

---

### `Obra`

| Visibilidade | Atributo / Método | Tipo / Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `-` | `id` | `int` | — | — |
| `-` | `titulo` | `String` | — | — |
| `-` | `categoria` | `String` | — | — |
| `-` | `anoPublicacao` | `int` | — | — |
| `+` | `getTitulo()` | `String` | — | — |
| `+` | `getCopias()` | `List<Copia>` | **Composição:** `Obra` ◆→ `Copia` *(1..*)* | — |
| `+` | `isDisponivel()` | `boolean` | **Composição:** consulta status das `Copia` | — |

> **Descrição:**
> - `getCopias()` — retorna a lista de exemplares físicos vinculados. A relação é **composição**: cópias não existem sem a obra — se a obra for deletada, as cópias são deletadas em cascata.
> - `isDisponivel()` — itera sobre `getCopias()` e retorna `true` se ao menos uma cópia tiver `status == DISPONIVEL`.
> - **Herança para `Livro` e `Periodico`:** fornece os atributos comuns. Subclasses adicionam seus identificadores específicos (ISBN / ISSN).

---

### `Livro`

| Visibilidade | Atributo / Método | Tipo / Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `-` | `isbn` | `String` | — | — |
| `-` | `autor` | `String` | — | — |
| `+` | `getIsbn()` | `String` | — | — |
| `+` | `validar()` | `boolean` | — | Sobrescreve comportamento de `Obra` |
| `+` | `toString()` | `String` | — | Sobrescreve comportamento de `Obra` |

> **Descrição:**
> - `validar()` — verifica formato do ISBN-13 (13 dígitos, dígito verificador correto) e se `titulo` não está vazio.
> - `toString()` — retorna string formatada: `"[Livro] titulo — autor (ISBN: isbn)"`.
> - **Herança de `Obra`:** herda `id`, `titulo`, `categoria`, `anoPublicacao` e todos os métodos da obra.

---

### `Periodico`

| Visibilidade | Atributo / Método | Tipo / Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `-` | `issn` | `String` | — | — |
| `-` | `edicao` | `int` | — | — |
| `+` | `getIssn()` | `String` | — | — |
| `+` | `validar()` | `boolean` | — | Sobrescreve comportamento de `Obra` |
| `+` | `toString()` | `String` | — | Sobrescreve comportamento de `Obra` |

> **Descrição:**
> - `validar()` — verifica formato ISSN (8 dígitos com hífen: XXXX-XXXX) e se `edicao > 0`.
> - `toString()` — retorna: `"[Periódico] titulo — Ed. edicao (ISSN: issn)"`.
> - **Herança de `Obra`:** herda todos os atributos e comportamentos da obra.

---

### `Copia`

| Visibilidade | Atributo / Método | Tipo / Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `-` | `numeroCopia` | `int` | — | — |
| `-` | `status` | `StatusCopia` | **Dependência:** usa `StatusCopia` (enum) | — |
| `-` | `conservacao` | `String` | — | — |
| `+` | `emprestar(leitor)` | `void` | **Realização:** implementa `Emprestavel.emprestar()` → cria `Emprestimo` | — |
| `+` | `devolver()` | `void` | **Realização:** implementa `Emprestavel.devolver()` → finaliza `Emprestimo` | — |
| `+` | `reservar(leitor)` | `void` | **Realização:** implementa `Emprestavel.reservar()` → cria `Reserva` | — |
| `+` | `getStatus()` | `StatusCopia` | **Dependência:** retorna valor do enum `StatusCopia` | — |

> **Descrição:**
> - `emprestar(leitor)` — valida se `status == DISPONIVEL`, cria um `Emprestimo` com data atual e prazo padrão, e muda `status` para `EMPRESTADO`.
> - `devolver()` — localiza o `Emprestimo` ativo desta cópia, chama `finalizar()` nele e muda `status` para `DISPONIVEL`. Verifica se há `Reserva` pendente — se houver, muda para `RESERVADO` automaticamente.
> - `reservar(leitor)` — valida se `status == DISPONIVEL`, cria uma `Reserva` e muda `status` para `RESERVADO`.
> - `getStatus()` — retorna o `StatusCopia` atual. Usado pela tela e pelo controller para tomada de decisão.
> - **Realização de `Emprestavel`:** permite que o controller opere sobre o tipo da interface, sem conhecer `Copia` diretamente — **polimorfismo por interface**.

---

### `Emprestimo`

| Visibilidade | Atributo / Método | Tipo / Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `-` | `id` | `int` | — | — |
| `-` | `dataEmprestimo` | `Date` | — | — |
| `-` | `dataDevolucaoPrevista` | `Date` | — | — |
| `-` | `dataDevolucaoReal` | `Date` | — | — |
| `-` | `copia` | `Copia` | **Associação:** `Emprestimo` → `Copia` *(..1)* | — |
| `-` | `leitor` | `Leitor` | **Associação:** `Emprestimo` → `Leitor` *(..1)* | — |
| `-` | `funcionario` | `Funcionario` | **Associação:** `Emprestimo` → `Funcionario` *(..1)* | — |
| `+` | `getDevolucaoPrevista()` | `Date` | — | — |
| `+` | `isAtrasado()` | `boolean` | — | — |
| `+` | `finalizar()` | `void` | Atualiza `StatusCopia` via `copia.devolver()` | — |
| `+` | `calcularMulta()` | `double` | — | — |

> **Descrição:**
> - `isAtrasado()` — compara `dataDevolucaoPrevista` com a data atual. Retorna `true` se `dataDevolucaoReal` for nula e a data prevista já passou.
> - `finalizar()` — preenche `dataDevolucaoReal` com a data atual e delega para `copia.devolver()` a atualização do status.
> - `calcularMulta()` — se `isAtrasado()`, calcula `(dias de atraso) × valorDiárioDaMulta`. Retorna `0.0` se não atrasado.
> - **Associações:** todas as três (`Copia`, `Leitor`, `Funcionario`) são obrigatórias — um empréstimo sem qualquer um deles é inválido.

---

### `Reserva`

| Visibilidade | Atributo / Método | Tipo / Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `-` | `id` | `int` | — | — |
| `-` | `dataReserva` | `Date` | — | — |
| `-` | `dataExpiracao` | `Date` | — | — |
| `-` | `copia` | `Copia` | **Associação:** `Reserva` → `Copia` *(..1)* | — |
| `-` | `leitor` | `Leitor` | **Associação:** `Reserva` → `Leitor` *(..1)* | — |
| `+` | `confirmar()` | `void` | Converte reserva em `Emprestimo` | — |
| `+` | `cancelar()` | `void` | Libera a `Copia` para `DISPONIVEL` | — |
| `+` | `expirar()` | `void` | Chamado automaticamente após `dataExpiracao` | — |
| `+` | `isAtiva()` | `boolean` | — | — |

> **Descrição:**
> - `confirmar()` — converte a reserva em um `Emprestimo` ativo, delegando para `copia.emprestar(leitor)`.
> - `cancelar()` — muda o status da cópia para `DISPONIVEL` e marca a reserva como inativa.
> - `expirar()` — equivalente a `cancelar()`, porém acionado automaticamente por `ReservaDAO.expirarAntigas()` quando a `dataExpiracao` é ultrapassada sem retirada.
> - `isAtiva()` — retorna `true` se a reserva não foi cancelada, expirada ou confirmada.

---

### `BibliotecaController`

| Visibilidade | Atributo / Método | Tipo / Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `-` | `livroDAO` | `LivroDAO` | **Agregação:** `Controller` ◇→ `LivroDAO` | — |
| `-` | `leitorDAO` | `LeitorDAO` | **Agregação:** `Controller` ◇→ `LeitorDAO` | — |
| `-` | `emprestimoDAO` | `EmprestimoDAO` | **Agregação:** `Controller` ◇→ `EmprestimoDAO` | — |
| `-` | `reservaDAO` | `ReservaDAO` | **Agregação:** `Controller` ◇→ `ReservaDAO` | — |
| `+` | `realizarEmprestimo(leitorId, copiaId)` | `void` | Orquestra `Leitor`, `Copia`, `Emprestimo`, `EmprestimoDAO` | — |
| `+` | `realizarDevolucao(emprestimoId)` | `void` | Orquestra `Emprestimo`, `Copia`, `EmprestimoDAO` | — |
| `+` | `realizarReserva(leitorId, copiaId)` | `void` | Orquestra `Leitor`, `Copia`, `Reserva`, `ReservaDAO` | — |
| `+` | `cancelarReserva(reservaId)` | `void` | Orquestra `Reserva`, `ReservaDAO` | — |
| `+` | `cadastrarLeitor(leitor)` | `void` | Usa `LeitorDAO.inserir()` | — |
| `+` | `buscarLivro(isbn)` | `Livro` | Usa `LivroDAO.buscarPorIsbn()` | — |

> **Descrição:**
> - `realizarEmprestimo()` — busca o `Leitor` e a `Copia` pelos IDs, chama `leitor.validar()` (verifica limite), chama `copia.emprestar(leitor)` e persiste via `EmprestimoDAO.inserir()`.
> - `realizarDevolucao()` — busca o `Emprestimo`, chama `emprestimo.finalizar()` (que atualiza a cópia) e persiste a atualização via `EmprestimoDAO.atualizar()`.
> - `realizarReserva()` — valida leitor e cópia, chama `copia.reservar(leitor)` e persiste via `ReservaDAO.inserir()`.
> - **Agregação com DAOs:** os DAOs são injetados no controller — eles existem independentemente dele, portanto é agregação e não composição.

---

## Interfaces

### `«interface» Emprestavel`

| Visibilidade | Método | Retorno | Implementado por | Observação |
|---|---|---|---|---|
| `+` | `emprestar(leitor: Leitor)` | `void` | `Copia` | Transição para `EMPRESTADO` |
| `+` | `devolver()` | `void` | `Copia` | Transição para `DISPONIVEL` |
| `+` | `reservar(leitor: Leitor)` | `void` | `Copia` | Transição para `RESERVADO` |

> **Descrição:** Define o contrato de comportamento para qualquer objeto emprestável. O `BibliotecaController` opera sobre este tipo (`Emprestavel emprestavel = copia`), sem depender da classe concreta `Copia` diretamente. Isso garante que, no futuro, um novo tipo (ex: `Equipamento`) possa ser emprestado sem alteração no controller — **princípio aberto/fechado (OCP)**.

---

### `«interface» IGenericDAO<T>`

| Visibilidade | Método | Retorno | Implementado por | Observação |
|---|---|---|---|---|
| `+` | `inserir(obj: T)` | `void` | Todos os DAOs | Executa `INSERT` no banco |
| `+` | `atualizar(obj: T)` | `void` | Todos os DAOs | Executa `UPDATE` no banco |
| `+` | `deletar(id: int)` | `void` | Todos os DAOs | Executa `DELETE` no banco |
| `+` | `buscarPorId(id: int)` | `T` | Todos os DAOs | Executa `SELECT WHERE id` |
| `+` | `listarTodos()` | `List<T>` | Todos os DAOs | Executa `SELECT *` |

> **Descrição:** Interface genérica que padroniza as operações CRUD. Usando generics `<T>`, cada DAO concreto define o tipo que manipula. O controller pode depender desta abstração em vez das implementações concretas — **inversão de dependência (DIP)**.

---

## Enumeração

### `«enum» StatusCopia`

| Valor | Significado | Transição permitida |
|---|---|---|
| `DISPONIVEL` | Cópia livre para empréstimo ou reserva | → `EMPRESTADO` ou `RESERVADO` |
| `EMPRESTADO` | Cópia fora da biblioteca | → `DISPONIVEL` (na devolução) |
| `RESERVADO` | Cópia reservada para um leitor | → `EMPRESTADO` (na retirada) ou `DISPONIVEL` (no cancelamento) |
| `DANIFICADO` | Cópia indisponível para circulação | Nenhuma transição automática |

> **Descrição:** Substitui strings literais por constantes tipadas, tornando os estados verificáveis em tempo de compilação. Usado por `Copia.status` e consultado em toda lógica de negócio do controller e das views.

---

## Classes DAO

### `ConnectionFactory`

| Visibilidade | Método | Retorno | Relacionamento | Observação |
|---|---|---|---|---|
| `+` | `getConnection()` *(static)* | `Connection` | **Dependência:** usada por todos os DAOs | Lê `db.properties` |
| `+` | `closeConnection(conn)` *(static)* | `void` | **Dependência:** usada por todos os DAOs | Fecha recursos JDBC |

> **Descrição:** Ponto único de configuração do banco de dados (URL, driver, usuário, senha). Centralizar aqui significa que uma troca de banco (ex: MySQL → PostgreSQL) exige mudança em um único lugar. Todos os DAOs a utilizam via chamada estática.

---

### `LivroDAO`

| Visibilidade | Método | Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `+` | `inserir(livro: Livro)` | `void` | Persiste `Livro` no banco | Implementa `IGenericDAO<Livro>` |
| `+` | `atualizar(livro: Livro)` | `void` | Atualiza `Livro` no banco | Implementa `IGenericDAO<Livro>` |
| `+` | `deletar(id: int)` | `void` | Remove `Livro` do banco | Implementa `IGenericDAO<Livro>` |
| `+` | `buscarPorId(id: int)` | `Livro` | Busca `Livro` por PK | Implementa `IGenericDAO<Livro>` |
| `+` | `listarTodos()` | `List<Livro>` | Retorna todos os livros | Implementa `IGenericDAO<Livro>` |
| `+` | `buscarPorIsbn(isbn: String)` | `Livro` | `SELECT WHERE isbn = ?` | Método adicional |
| `+` | `buscarPorTitulo(titulo: String)` | `List<Livro>` | `SELECT WHERE titulo LIKE ?` | Método adicional |

> **Descrição:** Cada método monta um `PreparedStatement`, obtém conexão via `ConnectionFactory.getConnection()`, executa o SQL e mapeia o `ResultSet` para um objeto `Livro`. O `buscarPorIsbn()` é usado pela tela de empréstimo para localizar a obra rapidamente.

---

### `LeitorDAO`

| Visibilidade | Método | Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `+` | `inserir(leitor: Leitor)` | `void` | Persiste `Leitor` no banco | Implementa `IGenericDAO<Leitor>` |
| `+` | `atualizar(leitor: Leitor)` | `void` | Atualiza `Leitor` no banco | Implementa `IGenericDAO<Leitor>` |
| `+` | `deletar(id: int)` | `void` | Remove `Leitor` do banco | Implementa `IGenericDAO<Leitor>` |
| `+` | `buscarPorId(id: int)` | `Leitor` | Busca `Leitor` por PK | Implementa `IGenericDAO<Leitor>` |
| `+` | `listarTodos()` | `List<Leitor>` | Retorna todos os leitores | Implementa `IGenericDAO<Leitor>` |
| `+` | `buscarPorMatricula(mat: String)` | `Leitor` | `SELECT WHERE matricula = ?` | Método adicional |
| `+` | `listarAtivos()` | `List<Leitor>` | Leitores sem pendências em atraso | Método adicional |

> **Descrição:** `listarAtivos()` faz um `JOIN` com a tabela de empréstimos e filtra apenas leitores sem atraso — usado pelo controller antes de liberar um novo empréstimo. `buscarPorMatricula()` é o meio mais rápido de localizar um leitor no atendimento.

---

### `EmprestimoDAO`

| Visibilidade | Método | Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `+` | `inserir(e: Emprestimo)` | `void` | Persiste `Emprestimo` no banco | Implementa `IGenericDAO<Emprestimo>` |
| `+` | `atualizar(e: Emprestimo)` | `void` | Atualiza `Emprestimo` no banco | Implementa `IGenericDAO<Emprestimo>` |
| `+` | `deletar(id: int)` | `void` | Remove registro do banco | Implementa `IGenericDAO<Emprestimo>` |
| `+` | `buscarPorId(id: int)` | `Emprestimo` | Busca por PK | Implementa `IGenericDAO<Emprestimo>` |
| `+` | `listarTodos()` | `List<Emprestimo>` | Todos os registros | Implementa `IGenericDAO<Emprestimo>` |
| `+` | `buscarAtivos()` | `List<Emprestimo>` | `WHERE data_devolucao_real IS NULL` | Método adicional |
| `+` | `buscarAtrasados()` | `List<Emprestimo>` | `WHERE prazo < NOW() AND real IS NULL` | Método adicional |
| `+` | `buscarPorLeitor(leitorId: int)` | `List<Emprestimo>` | `WHERE leitor_id = ?` | Método adicional |

> **Descrição:** `buscarAtrasados()` é a consulta mais crítica do sistema — alimenta relatórios de cobrança e alertas na `TelaPrincipal`. `buscarAtivos()` é usado para exibir empréstimos em andamento. `atualizar()` é chamado na devolução para gravar `dataDevolucaoReal`.

---

### `ReservaDAO`

| Visibilidade | Método | Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `+` | `inserir(r: Reserva)` | `void` | Persiste `Reserva` no banco | Implementa `IGenericDAO<Reserva>` |
| `+` | `atualizar(r: Reserva)` | `void` | Atualiza `Reserva` no banco | Implementa `IGenericDAO<Reserva>` |
| `+` | `deletar(id: int)` | `void` | Remove registro do banco | Implementa `IGenericDAO<Reserva>` |
| `+` | `buscarPorId(id: int)` | `Reserva` | Busca por PK | Implementa `IGenericDAO<Reserva>` |
| `+` | `listarTodos()` | `List<Reserva>` | Todos os registros | Implementa `IGenericDAO<Reserva>` |
| `+` | `buscarAtivasPorLeitor(leitorId)` | `List<Reserva>` | `WHERE leitor_id = ? AND ativa = true` | Método adicional |
| `+` | `buscarPorCopia(copiaId)` | `List<Reserva>` | `WHERE copia_id = ?` | Método adicional |
| `+` | `expirarAntigas()` | `void` | `UPDATE WHERE expiracao < NOW()` | Método adicional — job agendado |

> **Descrição:** `expirarAntigas()` deve ser chamado na inicialização do sistema (ou por um `Timer`) para liberar automaticamente cópias cujas reservas venceram sem retirada. `buscarPorCopia()` é usado na devolução para verificar se existe reserva pendente para aquela cópia.

---

## Views Swing

### `BaseFrame`

| Visibilidade | Atributo / Método | Tipo / Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `#` | `controller` | `BibliotecaController` | **Associação:** todas as views → `Controller` | — |
| `+` | `inicializar()` | `void` | — | — |
| `+` | `exibirMensagem(msg: String)` | `void` | — | — |
| `+` | `exibirErro(msg: String)` | `void` | — | — |

> **Descrição:** Superclasse de todas as janelas Swing. Define tamanho padrão, centralização na tela e tratamento uniforme de mensagens. O atributo `controller` é `protected` para que todas as subclasses o acessem diretamente sem getter — decisão prática para janelas de uma aplicação desktop.

---

### `TelaPrincipal`

| Visibilidade | Método | Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `+` | `abrirTelaEmprestimo()` | `void` | Instancia `TelaEmprestimo` | Estende `BaseFrame` |
| `+` | `abrirTelaCadastroLeitor()` | `void` | Instancia `TelaCadastroLeitor` | Estende `BaseFrame` |
| `+` | `exibirAlertasAtraso()` | `void` | Usa `controller` → `EmprestimoDAO.buscarAtrasados()` | Estende `BaseFrame` |

> **Descrição:** Ponto de entrada da aplicação após login. Exibe alertas de atraso ao abrir. Cada botão do menu instancia e exibe a tela correspondente.

---

### `TelaEmprestimo`

| Visibilidade | Método | Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `+` | `buscarLeitor()` | `void` | Usa `controller.buscarLeitor()` | Estende `BaseFrame` |
| `+` | `buscarCopia()` | `void` | Usa `controller.buscarLivro()` | Estende `BaseFrame` |
| `+` | `confirmarEmprestimo()` | `void` | Chama `controller.realizarEmprestimo()` | Estende `BaseFrame` |
| `+` | `registrarDevolucao()` | `void` | Chama `controller.realizarDevolucao()` | Estende `BaseFrame` |

> **Descrição:** Tela principal do atendimento diário. Campos de busca por matrícula do leitor e número de cópia. Exibe tabela de empréstimos ativos do leitor selecionado. Ao confirmar, o controller valida e persiste — em caso de erro (leitor bloqueado, cópia indisponível) o `BaseFrame.exibirErro()` é chamado.

---

### `TelaCadastroLeitor`

| Visibilidade | Método | Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `+` | `salvarLeitor()` | `void` | Chama `controller.cadastrarLeitor()` | Estende `BaseFrame` |
| `+` | `pesquisar()` | `void` | Usa `controller` → `LeitorDAO.buscarPorMatricula()` | Estende `BaseFrame` |
| `+` | `limparFormulario()` | `void` | Limpa campos da tela | Estende `BaseFrame` |
| `+` | `preencherFormulario(leitor)` | `void` | Popula campos com dados do `Leitor` retornado | Estende `BaseFrame` |

> **Descrição:** Formulário de cadastro e edição de leitores. O fluxo típico é: pesquisar por matrícula → formulário é preenchido automaticamente → editar → salvar. Para novo leitor: limpar → preencher → salvar.