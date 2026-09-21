package br.ufal.ic.p2.wepayu.Exception;
public class EnderecoInvalidoException extends Exception {

    public EnderecoInvalidoException() {
        super("Endereco nao pode ser nulo.");
    }
}