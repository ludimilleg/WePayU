public class SalarioInvalidoException extends Exception {

    public SalarioInvalidoException() {
        super("Salario deve ser nao-negativo.");
    }
}