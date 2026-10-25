package br.ufal.ic.p2.wepayu.exception;
public class EnderecoInvalidoException extends Exception {

    public EnderecoInvalidoException() {
        super("Endereco nao pode ser nulo.");
    }
}