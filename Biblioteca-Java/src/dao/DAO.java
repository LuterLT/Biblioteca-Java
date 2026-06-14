package dao;

import java.util.List;

public interface DAO<T> {
    T inserir(T obj) throws Exception;
    void atualizar(T obj) throws Exception;
    void remover(int id) throws Exception;
    T buscarPorId(int id) throws Exception;
    List<T> listarTodos() throws Exception;
}
