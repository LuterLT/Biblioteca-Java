package modelo;

import exceptions.DadosInvalidosException;

public interface Cadastravel {
    void validar() throws DadosInvalidosException;
    String exibirDados();
}
