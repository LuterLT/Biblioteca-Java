# Sistema de Biblioteca — Documentação de Classes

---

## Classes Abstratas

### `Pessoa`
- **Atributos:** `id: int`, `nome: String`, `cpf: String`, `email: String`, `telefone: String`
- **Métodos:** `getNome(): String`, `toString(): String` *(abstract)*, `validar(): boolean` *(abstract)*
- **Relacionamentos:**
  - **Herança:** `Leitor` e `Funcionario` estendem `Pessoa`, herdando todos os atributos e sendo obrigados a implementar `toString()` e `validar()` — isso garante polimorfismo de sobrescrita, pois o sistema pode tratar ambos como `Pessoa` e chamar `validar()` sem conhecer o tipo concreto.

---

## Classes Concretas

### `Leitor`
- **Atributos:** `matricula: String`, `limiteEmprestimos: int`
- **Métodos:** `validar(): boolean`, `toString(): String`, `getEmprestimosAtivos(): List<Emprestimo>`, `getReservasAtivas(): List<Reserva>`
- **Relacionamentos:**
  - **Herança:** Estende `Pessoa`, herdando dados pessoais e implementando os métodos abstratos.
  - **Associação com `Emprestimo`:** Um leitor pode ter múltiplos empréstimos ativos *(1 → \*)*. Ao realizar um empréstimo, o status da `Copia` muda para `EMPRESTADO` e o registro fica vinculado ao leitor.
  - **Associação com `Reserva`:** Um leitor pode ter múltiplas reservas ativas *(1 → \*)*. Ao reservar, o status da `Copia` passa para `RESERVADO`.

---

### `Funcionario`
- **Atributos:** `matricula: String`, `cargo: String`
- **Métodos:** `validar(): boolean`, `toString(): String`, `registrarEmprestimo(): void`, `registrarDevolucao(): void`
- **Relacionamentos:**
  - **Herança:** Estende `Pessoa`.
  - **Associação com `Emprestimo`:** Todo empréstimo registrado fica vinculado ao funcionário que o operou *(1 → \*)*. Isso permite rastreabilidade das operações por atendente.

---

### `Obra`
- **Atributos:** `id: int`, `titulo: String`, `categoria: String`, `anoPublicacao: int`
- **Métodos:** `getTitulo(): String`, `getCopias(): List<Copia>`, `isDisponivel(): boolean`
- **Relacionamentos:**
  - **Herança:** Superclasse de `Livro` e `Periodico`. Centraliza atributos comuns e permite tratar qualquer tipo de obra de forma polimórfica.
  - **Composição com `Copia`:** Uma obra agrega suas cópias físicas *(1 → \*)*. A relação é de **composição** — se a obra for removida do sistema, suas cópias também são, pois não existem de forma independente.

---

### `Livro`
- **Atributos:** `isbn: String`, `autor: String`
- **Métodos:** `getIsbn(): String`, `validar(): boolean`, `toString(): String`
- **Relacionamentos:**
  - **Herança:** Estende `Obra`, especializando-a com atributos bibliográficos. Sobrescreve `validar()` com regra específica de formato ISBN.

---

### `Periodico`
- **Atributos:** `issn: String`, `edicao: int`
- **Métodos:** `getIssn(): String`, `validar(): boolean`, `toString(): String`
- **Relacionamentos:**
  - **Herança:** Estende `Obra`, especializando-a para revistas e jornais. Sobrescreve `validar()` verificando formato ISSN e número de edição.

---

### `Copia`
- **Atributos:** `numeroCopia: int`, `status: StatusCopia`, `conservacao: String`
- **Métodos:** `emprestar(leitor: Leitor): void`, `devolver(): void`, `reservar(leitor: Leitor): void`, `getStatus(): StatusCopia`
- **Relacionamentos:**
  - **Realização de `Emprestavel`:** Implementa a interface, sendo o único objeto que pode ser emprestado, devolvido ou reservado. O controller opera sobre o tipo `Emprestavel`, nunca sobre `Copia` diretamente — isso é polimorfismo por interface.
  - **Composição com `Obra`:** Toda cópia pertence obrigatoriamente a uma obra *(muitos → 1)*.
  - **Associação com `Emprestimo`:** Ao ser emprestada, a cópia origina um registro de `Emprestimo` e muda seu `status` para `EMPRESTADO`.
  - **Associação com `Reserva`:** Ao ser reservada, origina um registro de `Reserva` e muda seu `status` para `RESERVADO`.
  - **Dependência de `StatusCopia`:** Usa o enum para controlar transições de estado de forma segura.

---

### `Emprestimo`
- **Atributos:** `id: int`, `dataEmprestimo: Date`, `dataDevolucaoPrevista: Date`, `dataDevolucaoReal: Date`
- **Métodos:** `getDevolucaoPrevista(): Date`, `isAtrasado(): boolean`, `finalizar(): void`, `calcularMulta(): double`
- **Relacionamentos:**
  - **Associação com `Copia`:** Cada empréstimo referencia exatamente uma cópia *(1 → 1)*. Ao finalizar, a cópia retorna ao status `DISPONIVEL`.
  - **Associação com `Leitor`:** Registra qual leitor realizou o empréstimo *(muitos → 1)*.
  - **Associação com `Funcionario`:** Registra qual funcionário operou o empréstimo *(muitos → 1)*.

---

### `Reserva`
- **Atributos:** `id: int`, `dataReserva: Date`, `dataExpiracao: Date`
- **Métodos:** `confirmar(): void`, `cancelar(): void`, `expirar(): void`, `isAtiva(): boolean`
- **Relacionamentos:**
  - **Associação com `Copia`:** Cada reserva vincula uma cópia específica *(muitos → 1)*. Quando confirmada, a reserva pode ser convertida em `Emprestimo`.
  - **Associação com `Leitor`:** Registra o leitor que fez a reserva *(muitos → 1)*.

---

### `BibliotecaController`
- **Atributos:** `livroDAO: LivroDAO`, `leitorDAO: LeitorDAO`, `emprestimoDAO: EmprestimoDAO`, `reservaDAO: ReservaDAO`
- **Métodos:** `realizarEmprestimo(leitorId: int, copiaId: int): void`, `realizarDevolucao(emprestimoId: int): void`, `realizarReserva(leitorId: int, copiaId: int): void`, `cancelarReserva(reservaId: int): void`, `cadastrarLeitor(leitor: Leitor): void`, `buscarLivro(isbn: String): Livro`
- **Relacionamentos:**
  - **Agregação com os DAOs:** Mantém referências a todos os DAOs. Os DAOs podem existir independentemente — a relação é de **agregação**, não composição.
  - **Dependência com as Views:** As telas Swing chamam métodos do controller, mas o controller não conhece as views — mantendo separação de responsabilidades (MVC).

---

## Interfaces

### `«interface» Emprestavel`
- **Métodos:** `emprestar(leitor: Leitor): void`, `devolver(): void`, `reservar(leitor: Leitor): void`
- **Relacionamentos:**
  - **Realização por `Copia`:** `Copia` é a única implementação concreta. O `BibliotecaController` depende deste tipo para realizar operações, garantindo que no futuro qualquer outro objeto emprestável (ex: equipamento) possa ser introduzido sem alterar o controller.

---

### `«interface» IGenericDAO<T>`
- **Métodos:** `inserir(obj: T): void`, `atualizar(obj: T): void`, `deletar(id: int): void`, `buscarPorId(id: int): T`, `listarTodos(): List<T>`
- **Relacionamentos:**
  - **Realização por todos os DAOs concretos:** `LivroDAO`, `LeitorDAO`, `EmprestimoDAO` e `ReservaDAO` implementam esta interface. Isso permite que o controller dependa da abstração `IGenericDAO`, não das implementações — inversão de dependência.

---

## Enumerações

### `«enum» StatusCopia`
- **Valores:** `DISPONIVEL`, `EMPRESTADO`, `RESERVADO`, `DANIFICADO`
- **Uso:** Utilizada por `Copia` para controlar transições de estado. Evita o uso de strings literais e torna as transições verificáveis em tempo de compilação.

---

## Classes DAO

### `LivroDAO`
- **Implementa:** `IGenericDAO<Livro>`
- **Métodos adicionais:** `buscarPorIsbn(isbn: String): Livro`, `buscarPorTitulo(titulo: String): List<Livro>`, `listarPorCategoria(cat: String): List<Livro>`
- **Responsabilidade:** Traduz objetos `Livro` para SQL (`INSERT`, `UPDATE`, `SELECT`) e mapeia `ResultSet` de volta para objetos. Usa `ConnectionFactory` para obter a conexão.

---

### `LeitorDAO`
- **Implementa:** `IGenericDAO<Leitor>`
- **Métodos adicionais:** `buscarPorMatricula(mat: String): Leitor`, `listarAtivos(): List<Leitor>`
- **Responsabilidade:** Persiste e recupera leitores. O método `listarAtivos()` filtra leitores sem empréstimos em atraso, usado pela tela de empréstimo para validar elegibilidade.

---

### `EmprestimoDAO`
- **Implementa:** `IGenericDAO<Emprestimo>`
- **Métodos adicionais:** `buscarAtivos(): List<Emprestimo>`, `buscarAtrasados(): List<Emprestimo>`, `buscarPorLeitor(leitorId: int): List<Emprestimo>`
- **Responsabilidade:** Persiste registros de empréstimo e oferece consultas de negócio críticas para relatórios e cobrança de multas.

---

### `ReservaDAO`
- **Implementa:** `IGenericDAO<Reserva>`
- **Métodos adicionais:** `buscarAtivasPorLeitor(leitorId: int): List<Reserva>`, `buscarPorCopia(copiaId: int): List<Reserva>`, `expirarAntigas(): void`
- **Responsabilidade:** Gerencia o ciclo de vida das reservas no banco. O método `expirarAntigas()` pode ser chamado por um job agendado para liberar cópias reservadas e não retiradas.

---

### `ConnectionFactory`
- **Métodos:** `getConnection(): Connection` *(static)*, `closeConnection(conn: Connection): void` *(static)*
- **Responsabilidade:** Centraliza a configuração JDBC (URL, usuário, senha, driver). Todos os DAOs a utilizam como dependência. Ponto único de mudança caso o banco de dados seja trocado.

---

## Views Swing

### `BaseFrame`
- **Estende:** `JFrame`
- **Métodos:** `inicializar(): void`, `exibirMensagem(msg: String): void`, `exibirErro(msg: String): void`
- **Responsabilidade:** Superclasse de todas as janelas. Define tamanho padrão, centralização e comportamento de fechamento. As subclasses herdam esses comportamentos sem reescrevê-los.

---

### `TelaPrincipal`
- **Estende:** `BaseFrame`
- **Métodos:** `abrirTelaEmprestimo(): void`, `abrirTelaCadastroLeitor(): void`, `abrirRelatorios(): void`
- **Responsabilidade:** Menu principal do sistema. Ponto de entrada do usuário após login.

---

### `TelaEmprestimo`
- **Estende:** `BaseFrame`
- **Métodos:** `buscarLeitor(): void`, `buscarCopia(): void`, `confirmarEmprestimo(): void`, `registrarDevolucao(): void`
- **Responsabilidade:** Interface para operações de empréstimo e devolução. Chama `BibliotecaController.realizarEmprestimo()` e `realizarDevolucao()`.

---

### `TelaCadastroLeitor`
- **Estende:** `BaseFrame`
- **Métodos:** `salvarLeitor(): void`, `pesquisar(): void`, `limparFormulario(): void`
- **Responsabilidade:** Formulário de criação e edição de leitores. Chama `BibliotecaController.cadastrarLeitor()`.