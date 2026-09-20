public class NomeInvalidoException extends Exception {

    public NomeInvalidoException() {
        super("Nome nao pode ser nulo.");
    }
}