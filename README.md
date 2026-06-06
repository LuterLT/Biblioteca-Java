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
| `+` | `getId()` | `int` | — | — |
| `+` | `getNome()` | `String` | — | — |
| `+` | `toString()` *(abstract)* | `String` | — | Obriga `Leitor` e `Funcionario` a sobrescrever |
| `+` | `validar()` *(abstract)* | `boolean` | — | Obriga `Leitor` e `Funcionario` a sobrescrever |

> **Descrição:** Superclasse abstrata que centraliza os dados pessoais comuns a qualquer pessoa do sistema. Nunca é instanciada diretamente. `toString()` e `validar()` são abstratos — cada subclasse define sua própria implementação, garantindo **polimorfismo de sobrescrita**. Em Java, uma classe abstrata é declarada com `abstract class Pessoa {}`.

---

## Classes Concretas

### `Leitor`

| Visibilidade | Atributo / Método | Tipo / Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `-` | `matricula` | `String` | — | — |
| `-` | `limiteEmprestimos` | `int` | — | — |
| `+` | `getMatricula()` | `String` | — | — |
| `+` | `validar()` | `boolean` | — | Sobrescreve `Pessoa.validar()` |
| `+` | `toString()` | `String` | — | Sobrescreve `Pessoa.toString()` |
| `+` | `getEmprestimosAtivos()` | `List<Emprestimo>` | **Associação:** `Leitor` → `Emprestimo` *(1..*)* | — |
| `+` | `getReservasAtivas()` | `List<Reserva>` | **Associação:** `Leitor` → `Reserva` *(1..*)* | — |

> **Descrição:**
> - **Herança de `Pessoa`:** declarada com `class Leitor extends Pessoa {}`. Herda `id`, `nome`, `cpf`, `email`, `telefone` e seus getters.
> - `validar()` — verifica se o número de empréstimos ativos não ultrapassou `limiteEmprestimos`. Retorna `false` se o leitor estiver bloqueado.
> - `getEmprestimosAtivos()` — retorna a lista de empréstimos ainda não finalizados. Usado pelo controller antes de autorizar um novo empréstimo.
> - `getReservasAtivas()` — retorna reservas com `isAtiva() == true`. Usado para checar conflitos antes de nova reserva.

---

### `Funcionario`

| Visibilidade | Atributo / Método | Tipo / Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `-` | `matricula` | `String` | — | — |
| `-` | `cargo` | `String` | — | — |
| `+` | `getMatricula()` | `String` | — | — |
| `+` | `getCargo()` | `String` | — | — |
| `+` | `validar()` | `boolean` | — | Sobrescreve `Pessoa.validar()` |
| `+` | `toString()` | `String` | — | Sobrescreve `Pessoa.toString()` |
| `+` | `registrarEmprestimo()` | `void` | **Associação:** `Funcionario` → `Emprestimo` *(1..*)* | — |
| `+` | `registrarDevolucao()` | `void` | **Associação:** `Funcionario` → `Emprestimo` *(1..*)* | — |

> **Descrição:**
> - **Herança de `Pessoa`:** declarada com `class Funcionario extends Pessoa {}`. Herda todos os dados pessoais.
> - `validar()` — verifica se `cargo` não está vazio e se `matricula` é única no sistema.
> - `registrarEmprestimo()` — aciona o controller para criar um `Emprestimo`, vinculando o funcionário como operador para fins de rastreabilidade.
> - `registrarDevolucao()` — aciona o controller para finalizar um `Emprestimo` e atualizar o `StatusCopia` para `DISPONIVEL`.

---

### `Obra`

| Visibilidade | Atributo / Método | Tipo / Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `-` | `id` | `int` | — | — |
| `-` | `titulo` | `String` | — | — |
| `-` | `autor` | `String` | — | — |
| `-` | `categoria` | `String` | — | — |
| `-` | `anoPublicacao` | `int` | — | — |
| `-` | `tipo` | `TipoObra` | **Dependência:** usa enum `TipoObra` | — |
| `-` | `isbn` | `String` | — | — |
| `-` | `issn` | `String` | — | — |
| `+` | `getTitulo()` | `String` | — | — |
| `+` | `getTipo()` | `TipoObra` | **Dependência:** retorna enum `TipoObra` | — |
| `+` | `getCopias()` | `List<Copia>` | **Composição:** `Obra` ◆→ `Copia` *(1..*)* | — |
| `+` | `isDisponivel()` | `boolean` | **Composição:** consulta status das `Copia` | — |
| `+` | `validar()` | `boolean` | — | — |
| `+` | `toString()` | `String` | — | — |

> **Descrição:**
> - `tipo` — campo do enum `TipoObra` (`LIVRO` ou `PERIODICO`) que define a natureza da obra. Substitui a necessidade de subclasses `Livro` e `Periodico`.
> - `isbn` — preenchido apenas quando `tipo == LIVRO`. Pode ser `null` para periódicos.
> - `issn` — preenchido apenas quando `tipo == PERIODICO`. Pode ser `null` para livros.
> - `validar()` — verifica se `titulo` não está vazio e, dependendo do `tipo`, valida o formato do `isbn` ou `issn`.
> - `getCopias()` — retorna os exemplares físicos vinculados. Relação de **composição**: cópias não existem sem a obra.
> - `isDisponivel()` — itera sobre `getCopias()` e retorna `true` se ao menos uma tiver `status == DISPONIVEL`.

---

### `Copia`

| Visibilidade | Atributo / Método | Tipo / Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `-` | `id` | `int` | — | — |
| `-` | `numeroCopia` | `int` | — | — |
| `-` | `status` | `StatusCopia` | **Dependência:** usa enum `StatusCopia` | — |
| `-` | `conservacao` | `String` | — | — |
| `-` | `obra` | `Obra` | **Composição:** `Copia` → `Obra` *(..1)* | — |
| `+` | `getStatus()` | `StatusCopia` | **Dependência:** retorna enum `StatusCopia` | — |
| `+` | `getObra()` | `Obra` | **Composição:** retorna a `Obra` vinculada | — |
| `+` | `emprestar(leitor: Leitor)` | `void` | **Realização:** implementa `Emprestavel.emprestar()` | — |
| `+` | `devolver()` | `void` | **Realização:** implementa `Emprestavel.devolver()` | — |
| `+` | `reservar(leitor: Leitor)` | `void` | **Realização:** implementa `Emprestavel.reservar()` | — |

> **Descrição:**
> - **Realização de `Emprestavel`:** declarada com `class Copia implements Emprestavel {}`. É o único objeto que pode ser emprestado, devolvido ou reservado. O controller opera sobre o tipo `Emprestavel` — **polimorfismo por interface**.
> - `emprestar(leitor)` — valida se `status == DISPONIVEL`, cria um `Emprestimo` e muda `status` para `EMPRESTADO`.
> - `devolver()` — finaliza o `Emprestimo` ativo desta cópia, muda `status` para `DISPONIVEL`. Se houver `Reserva` pendente, muda para `RESERVADO` automaticamente.
> - `reservar(leitor)` — valida se `status == DISPONIVEL`, cria uma `Reserva` e muda `status` para `RESERVADO`.

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
> - `isAtrasado()` — compara `dataDevolucaoPrevista` com a data atual. Retorna `true` se `dataDevolucaoReal` for nula e o prazo já tiver passado.
> - `finalizar()` — preenche `dataDevolucaoReal` com a data atual e delega para `copia.devolver()` a atualização do status.
> - `calcularMulta()` — se `isAtrasado()`, calcula `(dias de atraso) × valorDiárioDaMulta`. Retorna `0.0` se não atrasado.
> - Todas as três associações (`Copia`, `Leitor`, `Funcionario`) são obrigatórias — um empréstimo sem qualquer um deles é inválido e deve lançar exceção.

---

### `Reserva`

| Visibilidade | Atributo / Método | Tipo / Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `-` | `id` | `int` | — | — |
| `-` | `dataReserva` | `Date` | — | — |
| `-` | `dataExpiracao` | `Date` | — | — |
| `-` | `copia` | `Copia` | **Associação:** `Reserva` → `Copia` *(..1)* | — |
| `-` | `leitor` | `Leitor` | **Associação:** `Reserva` → `Leitor` *(..1)* | — |
| `+` | `confirmar()` | `void` | Converte `Reserva` em `Emprestimo` | — |
| `+` | `cancelar()` | `void` | Libera a `Copia` para `DISPONIVEL` | — |
| `+` | `expirar()` | `void` | Libera a `Copia` quando prazo vence | — |
| `+` | `isAtiva()` | `boolean` | — | — |

> **Descrição:**
> - `confirmar()` — converte a reserva em `Emprestimo` ativo, delegando para `copia.emprestar(leitor)`.
> - `cancelar()` — muda o status da cópia para `DISPONIVEL` e marca a reserva como inativa.
> - `expirar()` — equivalente a `cancelar()`, porém acionado por `ReservaDAO.expirarAntigas()` quando `dataExpiracao` é ultrapassada sem retirada.
> - `isAtiva()` — retorna `true` se a reserva não foi cancelada, expirada ou confirmada.

---

### `BibliotecaController`

| Visibilidade | Atributo / Método | Tipo / Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `-` | `obraDAO` | `ObraDAO` | **Agregação:** `Controller` ◇→ `ObraDAO` | — |
| `-` | `copiaDAO` | `CopiaDAO` | **Agregação:** `Controller` ◇→ `CopiaDAO` | — |
| `-` | `leitorDAO` | `LeitorDAO` | **Agregação:** `Controller` ◇→ `LeitorDAO` | — |
| `-` | `funcionarioDAO` | `FuncionarioDAO` | **Agregação:** `Controller` ◇→ `FuncionarioDAO` | — |
| `-` | `emprestimoDAO` | `EmprestimoDAO` | **Agregação:** `Controller` ◇→ `EmprestimoDAO` | — |
| `-` | `reservaDAO` | `ReservaDAO` | **Agregação:** `Controller` ◇→ `ReservaDAO` | — |
| `+` | `realizarEmprestimo(leitorId, copiaId, funcId)` | `void` | Orquestra `Leitor`, `Copia`, `Funcionario`, `Emprestimo` | — |
| `+` | `realizarDevolucao(emprestimoId)` | `void` | Orquestra `Emprestimo`, `Copia`, `EmprestimoDAO` | — |
| `+` | `realizarReserva(leitorId, copiaId)` | `void` | Orquestra `Leitor`, `Copia`, `Reserva`, `ReservaDAO` | — |
| `+` | `cancelarReserva(reservaId)` | `void` | Orquestra `Reserva`, `ReservaDAO` | — |
| `+` | `cadastrarLeitor(leitor)` | `void` | Usa `LeitorDAO.inserir()` | — |
| `+` | `cadastrarObra(obra)` | `void` | Usa `ObraDAO.inserir()` | — |
| `+` | `buscarObra(id)` | `Obra` | Usa `ObraDAO.buscarPorId()` | — |
| `+` | `buscarLeitor(matricula)` | `Leitor` | Usa `LeitorDAO.buscarPorMatricula()` | — |

> **Descrição:**
> - `realizarEmprestimo()` — busca `Leitor`, `Copia` e `Funcionario` pelos IDs, chama `leitor.validar()`, chama `copia.emprestar(leitor)` e persiste via `EmprestimoDAO.inserir()`. Exceções são lançadas para cima e tratadas na View.
> - `realizarDevolucao()` — busca o `Emprestimo`, chama `emprestimo.finalizar()` e persiste via `EmprestimoDAO.atualizar()`.
> - `realizarReserva()` — valida leitor e cópia, chama `copia.reservar(leitor)` e persiste via `ReservaDAO.inserir()`.
> - **Agregação com DAOs:** os DAOs são criados dentro do controller mas poderiam existir de forma independente — por isso é agregação, não composição.
> - **Controle de exceções:** o controller deixa as exceções (`SQLException`, `IllegalStateException`) subirem até a View, que as captura e exibe para o usuário — conforme exigido pelo enunciado.

---

## Interfaces

### `«interface» Emprestavel`

| Visibilidade | Método | Retorno | Implementado por | Observação |
|---|---|---|---|---|
| `+` | `emprestar(leitor: Leitor)` | `void` | `Copia` | Transição para `EMPRESTADO` |
| `+` | `devolver()` | `void` | `Copia` | Transição para `DISPONIVEL` |
| `+` | `reservar(leitor: Leitor)` | `void` | `Copia` | Transição para `RESERVADO` |

> **Descrição:** Define o contrato de comportamento para qualquer objeto emprestável. Em Java: `interface Emprestavel {}`. O `BibliotecaController` opera sobre este tipo (`Emprestavel e = copia`), sem depender da classe concreta `Copia` diretamente — **polimorfismo por interface**. No futuro, um `Equipamento` poderia implementar a mesma interface sem alterar o controller.

---

### `«interface» IGenericDAO<T>`

| Visibilidade | Método | Retorno | Implementado por | Observação |
|---|---|---|---|---|
| `+` | `inserir(obj: T)` | `void` | Todos os DAOs | Executa `INSERT` no banco |
| `+` | `atualizar(obj: T)` | `void` | Todos os DAOs | Executa `UPDATE` no banco |
| `+` | `deletar(id: int)` | `void` | Todos os DAOs | Executa `DELETE` no banco |
| `+` | `buscarPorId(id: int)` | `T` | Todos os DAOs | Executa `SELECT WHERE id = ?` |
| `+` | `listarTodos()` | `List<T>` | Todos os DAOs | Executa `SELECT *` |

> **Descrição:** Interface genérica que padroniza as operações CRUD. Em Java: `interface IGenericDAO<T> {}`. Cada DAO concreto declara `class ObraDAO implements IGenericDAO<Obra> {}`. O uso de generics `<T>` evita duplicação de código e permite que o controller dependa da abstração — **inversão de dependência**.

---

## Enumerações

### `«enum» TipoObra`

| Valor | Significado | Campo relevante |
|---|---|---|
| `LIVRO` | A obra é um livro | `isbn` deve ser preenchido |
| `PERIODICO` | A obra é uma revista ou jornal | `issn` deve ser preenchido |

> **Descrição:** Substitui a necessidade de subclasses `Livro` e `Periodico`. Usado dentro de `Obra.validar()` para decidir qual campo de identificação validar. Em Java: `enum TipoObra { LIVRO, PERIODICO }`.

---

### `«enum» StatusCopia`

| Valor | Significado | Transição permitida |
|---|---|---|
| `DISPONIVEL` | Cópia livre para empréstimo ou reserva | → `EMPRESTADO` ou `RESERVADO` |
| `EMPRESTADO` | Cópia fora da biblioteca | → `DISPONIVEL` na devolução |
| `RESERVADO` | Cópia reservada para um leitor | → `EMPRESTADO` na retirada ou `DISPONIVEL` no cancelamento |
| `DANIFICADO` | Cópia indisponível para circulação | Nenhuma transição automática |

> **Descrição:** Controla o ciclo de vida de cada exemplar físico. Usado por `Copia.status` e consultado em toda lógica de negócio do controller e das views. Em Java: `enum StatusCopia { DISPONIVEL, EMPRESTADO, RESERVADO, DANIFICADO }`.

---

## Classes DAO

### `ConnectionFactory`

| Visibilidade | Método | Retorno | Relacionamento | Observação |
|---|---|---|---|---|
| `+` | `getConnection()` *(static)* | `Connection` | **Dependência:** usada por todos os DAOs | Lê configurações JDBC |
| `+` | `closeConnection(conn)` *(static)* | `void` | **Dependência:** usada por todos os DAOs | Fecha recursos JDBC |

> **Descrição:** Ponto único de configuração do banco de dados (URL, driver, usuário, senha). Centralizar aqui significa que uma troca de banco exige mudança em um único lugar. Todos os DAOs chamam `ConnectionFactory.getConnection()` no início de cada operação e `closeConnection()` no bloco `finally`.

---

### `ObraDAO`

| Visibilidade | Método | Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `+` | `inserir(obra: Obra)` | `void` | Persiste `Obra` no banco | Implementa `IGenericDAO<Obra>` |
| `+` | `atualizar(obra: Obra)` | `void` | Atualiza `Obra` no banco | Implementa `IGenericDAO<Obra>` |
| `+` | `deletar(id: int)` | `void` | Remove `Obra` do banco | Implementa `IGenericDAO<Obra>` |
| `+` | `buscarPorId(id: int)` | `Obra` | `SELECT WHERE id = ?` | Implementa `IGenericDAO<Obra>` |
| `+` | `listarTodos()` | `List<Obra>` | `SELECT *` | Implementa `IGenericDAO<Obra>` |
| `+` | `buscarPorTitulo(titulo: String)` | `List<Obra>` | `SELECT WHERE titulo LIKE ?` | Método adicional |
| `+` | `buscarPorTipo(tipo: TipoObra)` | `List<Obra>` | `SELECT WHERE tipo = ?` | Método adicional |

> **Descrição:** Traduz objetos `Obra` para SQL e mapeia `ResultSet` de volta para objetos. O campo `tipo` é salvo como `String` no banco (`"LIVRO"` ou `"PERIODICO"`) e convertido de volta com `TipoObra.valueOf()` na leitura. `isbn` e `issn` são salvos normalmente — o banco aceita `null` para o campo que não se aplica.

---

### `CopiaDAO`

| Visibilidade | Método | Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `+` | `inserir(copia: Copia)` | `void` | Persiste `Copia` no banco | Implementa `IGenericDAO<Copia>` |
| `+` | `atualizar(copia: Copia)` | `void` | Atualiza `Copia` no banco | Implementa `IGenericDAO<Copia>` |
| `+` | `deletar(id: int)` | `void` | Remove `Copia` do banco | Implementa `IGenericDAO<Copia>` |
| `+` | `buscarPorId(id: int)` | `Copia` | `SELECT WHERE id = ?` | Implementa `IGenericDAO<Copia>` |
| `+` | `listarTodos()` | `List<Copia>` | `SELECT *` | Implementa `IGenericDAO<Copia>` |
| `+` | `buscarPorObra(obraId: int)` | `List<Copia>` | `SELECT WHERE obra_id = ?` | Método adicional |
| `+` | `buscarDisponiveis()` | `List<Copia>` | `SELECT WHERE status = 'DISPONIVEL'` | Método adicional |

> **Descrição:** `buscarPorObra()` é usado pela tela de empréstimo para listar os exemplares disponíveis de uma obra pesquisada. `buscarDisponiveis()` alimenta a tela de reservas. O `status` é salvo como `String` no banco e convertido com `StatusCopia.valueOf()` na leitura.

---

### `LeitorDAO`

| Visibilidade | Método | Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `+` | `inserir(leitor: Leitor)` | `void` | Persiste `Leitor` no banco | Implementa `IGenericDAO<Leitor>` |
| `+` | `atualizar(leitor: Leitor)` | `void` | Atualiza `Leitor` no banco | Implementa `IGenericDAO<Leitor>` |
| `+` | `deletar(id: int)` | `void` | Remove `Leitor` do banco | Implementa `IGenericDAO<Leitor>` |
| `+` | `buscarPorId(id: int)` | `Leitor` | `SELECT WHERE id = ?` | Implementa `IGenericDAO<Leitor>` |
| `+` | `listarTodos()` | `List<Leitor>` | `SELECT *` | Implementa `IGenericDAO<Leitor>` |
| `+` | `buscarPorMatricula(mat: String)` | `Leitor` | `SELECT WHERE matricula = ?` | Método adicional |
| `+` | `listarAtivos()` | `List<Leitor>` | Leitores sem empréstimos em atraso | Método adicional |

> **Descrição:** `buscarPorMatricula()` é o meio mais rápido de localizar um leitor no atendimento — usado pela `TelaEmprestimo`. `listarAtivos()` faz um `JOIN` com a tabela de empréstimos filtrando apenas leitores sem atraso, usado pelo controller antes de autorizar novo empréstimo.

---

### `FuncionarioDAO`

| Visibilidade | Método | Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `+` | `inserir(func: Funcionario)` | `void` | Persiste `Funcionario` no banco | Implementa `IGenericDAO<Funcionario>` |
| `+` | `atualizar(func: Funcionario)` | `void` | Atualiza `Funcionario` no banco | Implementa `IGenericDAO<Funcionario>` |
| `+` | `deletar(id: int)` | `void` | Remove `Funcionario` do banco | Implementa `IGenericDAO<Funcionario>` |
| `+` | `buscarPorId(id: int)` | `Funcionario` | `SELECT WHERE id = ?` | Implementa `IGenericDAO<Funcionario>` |
| `+` | `listarTodos()` | `List<Funcionario>` | `SELECT *` | Implementa `IGenericDAO<Funcionario>` |
| `+` | `buscarPorMatricula(mat: String)` | `Funcionario` | `SELECT WHERE matricula = ?` | Método adicional |

> **Descrição:** Persiste e recupera funcionários. `buscarPorMatricula()` é usado na tela de login ou ao vincular um funcionário a um empréstimo. Segue o mesmo padrão dos demais DAOs com `ConnectionFactory` e `PreparedStatement`.

---

### `EmprestimoDAO`

| Visibilidade | Método | Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `+` | `inserir(e: Emprestimo)` | `void` | Persiste `Emprestimo` no banco | Implementa `IGenericDAO<Emprestimo>` |
| `+` | `atualizar(e: Emprestimo)` | `void` | Atualiza `Emprestimo` no banco | Implementa `IGenericDAO<Emprestimo>` |
| `+` | `deletar(id: int)` | `void` | Remove registro do banco | Implementa `IGenericDAO<Emprestimo>` |
| `+` | `buscarPorId(id: int)` | `Emprestimo` | `SELECT WHERE id = ?` | Implementa `IGenericDAO<Emprestimo>` |
| `+` | `listarTodos()` | `List<Emprestimo>` | `SELECT *` | Implementa `IGenericDAO<Emprestimo>` |
| `+` | `buscarAtivos()` | `List<Emprestimo>` | `WHERE data_devolucao_real IS NULL` | Método adicional |
| `+` | `buscarAtrasados()` | `List<Emprestimo>` | `WHERE prazo < NOW() AND real IS NULL` | Método adicional |
| `+` | `buscarPorLeitor(leitorId: int)` | `List<Emprestimo>` | `WHERE leitor_id = ?` | Método adicional |

> **Descrição:** `buscarAtrasados()` é a consulta mais crítica — alimenta alertas na `TelaPrincipal`. `atualizar()` é chamado na devolução para gravar `dataDevolucaoReal`. Na leitura do `ResultSet`, os IDs de `leitor`, `copia` e `funcionario` são usados para buscar os objetos completos nos respectivos DAOs.

---

### `ReservaDAO`

| Visibilidade | Método | Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `+` | `inserir(r: Reserva)` | `void` | Persiste `Reserva` no banco | Implementa `IGenericDAO<Reserva>` |
| `+` | `atualizar(r: Reserva)` | `void` | Atualiza `Reserva` no banco | Implementa `IGenericDAO<Reserva>` |
| `+` | `deletar(id: int)` | `void` | Remove registro do banco | Implementa `IGenericDAO<Reserva>` |
| `+` | `buscarPorId(id: int)` | `Reserva` | `SELECT WHERE id = ?` | Implementa `IGenericDAO<Reserva>` |
| `+` | `listarTodos()` | `List<Reserva>` | `SELECT *` | Implementa `IGenericDAO<Reserva>` |
| `+` | `buscarAtivasPorLeitor(leitorId: int)` | `List<Reserva>` | `WHERE leitor_id = ? AND ativa = true` | Método adicional |
| `+` | `buscarPorCopia(copiaId: int)` | `List<Reserva>` | `WHERE copia_id = ?` | Método adicional |
| `+` | `expirarAntigas()` | `void` | `UPDATE WHERE expiracao < NOW()` | Método adicional |

> **Descrição:** `expirarAntigas()` deve ser chamado na inicialização do sistema para liberar cópias cujas reservas venceram sem retirada. `buscarPorCopia()` é usado na devolução para verificar se há reserva pendente para aquela cópia — se houver, o status vai para `RESERVADO` em vez de `DISPONIVEL`.

---

## Views Swing

### `BaseFrame`

| Visibilidade | Atributo / Método | Tipo / Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `#` | `controller` | `BibliotecaController` | **Associação:** todas as views → `Controller` | — |
| `+` | `inicializar()` | `void` | — | — |
| `+` | `exibirMensagem(msg: String)` | `void` | — | — |
| `+` | `exibirErro(msg: String)` | `void` | Captura exceções vindas do controller | — |

> **Descrição:** Superclasse de todas as janelas Swing, declarada como `class BaseFrame extends JFrame {}`. Define tamanho padrão, centralização na tela e tratamento uniforme de erros via `JOptionPane`. O atributo `controller` é `protected` (`#`) para que todas as subclasses o acessem diretamente. **É aqui e nas subclasses que as exceções lançadas pelo controller são capturadas** — conforme exigido pelo enunciado.

---

### `TelaPrincipal`

| Visibilidade | Método | Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `+` | `abrirTelaEmprestimo()` | `void` | Instancia `TelaEmprestimo` | Estende `BaseFrame` |
| `+` | `abrirTelaCadastroLeitor()` | `void` | Instancia `TelaCadastroLeitor` | Estende `BaseFrame` |
| `+` | `exibirAlertasAtraso()` | `void` | Usa `controller` → `EmprestimoDAO.buscarAtrasados()` | Estende `BaseFrame` |

> **Descrição:** Ponto de entrada da aplicação. Exibe alertas de empréstimos atrasados ao abrir. Cada botão do menu instancia e torna visível a tela correspondente. Exceções são capturadas e exibidas via `exibirErro()` herdado de `BaseFrame`.

---

### `TelaEmprestimo`

| Visibilidade | Método | Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `+` | `buscarLeitor()` | `void` | Chama `controller.buscarLeitor()` | Estende `BaseFrame` |
| `+` | `buscarCopia()` | `void` | Chama `controller.buscarObra()` + `CopiaDAO` | Estende `BaseFrame` |
| `+` | `confirmarEmprestimo()` | `void` | Chama `controller.realizarEmprestimo()` | Estende `BaseFrame` |
| `+` | `registrarDevolucao()` | `void` | Chama `controller.realizarDevolucao()` | Estende `BaseFrame` |

> **Descrição:** Tela principal do atendimento diário. Possui campos de busca por matrícula do leitor e número da cópia. Exibe tabela com empréstimos ativos do leitor selecionado. Ao confirmar, o controller valida e persiste — em caso de erro (leitor bloqueado, cópia indisponível) o bloco `catch` chama `exibirErro()`.

---

### `TelaCadastroLeitor`

| Visibilidade | Método | Retorno | Relacionamento | Herança |
|---|---|---|---|---|
| `+` | `salvarLeitor()` | `void` | Chama `controller.cadastrarLeitor()` | Estende `BaseFrame` |
| `+` | `pesquisar()` | `void` | Chama `controller.buscarLeitor()` | Estende `BaseFrame` |
| `+` | `limparFormulario()` | `void` | Limpa os campos da tela | Estende `BaseFrame` |
| `+` | `preencherFormulario(leitor: Leitor)` | `void` | Popula campos com dados do `Leitor` retornado | Estende `BaseFrame` |

> **Descrição:** Formulário de cadastro e edição de leitores. Fluxo típico: pesquisar por matrícula → formulário preenchido automaticamente via `preencherFormulario()` → editar → salvar. Para novo leitor: `limparFormulario()` → preencher → `salvarLeitor()`. Exceções capturadas e exibidas via `exibirErro()`.