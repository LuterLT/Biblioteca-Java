import dao.*;
import modelo.*;
import exceptions.*;

import java.time.LocalDate;

public class TesteManual {
    public static void main(String[] args) {
        try {
            // Inicializa o banco e cria tabelas
            ConexaoBD.init();

            LeitorDAO leitorDAO = new LeitorDAO();
            FuncionarioDAO funcDAO = new FuncionarioDAO();
            LivroDAO livroDAO = new LivroDAO();
            CopiaDAO copiaDAO = new CopiaDAO();
            EmprestimoDAO emprestimoDAO = new EmprestimoDAO();
            ReservaDAO reservaDAO = new ReservaDAO();

            // Cria dados de exemplo: Leitor, Funcionario, Livro, Copia
            System.out.println("--- Criando Leitor ---");
            Leitor l = new Leitor(0, "Maria Silva", "12345678900", "MAT001");
            l.validar();
            leitorDAO.inserir(l);
            System.out.println("Leitor criado: " + l.exibirDados());

            System.out.println("--- Criando Funcionário ---");
            Funcionario f = new Funcionario(0, "João Admin", "98765432100", "Bibliotecario");
            f.validar();
            funcDAO.inserir(f);
            System.out.println("Funcionario criado: " + f.exibirDados());

            System.out.println("--- Criando Livro e Cópia ---");
            Livro livro = new Livro(0, "Java Básico", "Autor Exemplo", "ISBN-001");
            livroDAO.inserir(livro);
            Copia c = new Copia(0, livro, true, "Prateleira A1");
            copiaDAO.inserir(c);
            System.out.println("Livro e cópia criados: " + livro + " / " + c);

            System.out.println("--- Realizando empréstimo ---");
            Emprestimo emp = new Emprestimo(0, c, l, f, LocalDate.now());
            emprestimoDAO.inserir(emp);
            System.out.println("Emprestimo criado: " + emp);

            System.out.println("--- Realizando reserva ---");
            Reserva r = new Reserva(0, l, livro, LocalDate.now(), "ATIVA");
            reservaDAO.inserir(r);
            System.out.println("Reserva criada: " + r);

            System.out.println("--- Devolução (simulada após 16 dias para multa) ---");
            emprestimoDAO.devolver(emp.getId(), LocalDate.now().plusDays(16));
            Emprestimo empAtual = emprestimoDAO.buscarPorId(emp.getId());
            System.out.println("Emprestimo após devolução: multa=" + empAtual.getMulta());

            System.out.println("--- Testando exceções ---");
            try {
                Leitor l2 = new Leitor(0, "", "", "");
                l2.validar();
            } catch (DadosInvalidosException ex) {
                System.out.println("Capturado DadosInvalidosException conforme esperado: " + ex.getMessage());
            }

        } catch (Exception ex) {
            System.err.println("Erro durante testes: ");
            ex.printStackTrace();
        }
    }
}
