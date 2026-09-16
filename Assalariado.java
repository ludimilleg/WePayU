public class Assalariado extends Empregados {
    double salarioMensal;

    public Assalariado(int id, String nome, String endereço, boolean sindicalizado, double salarioMensal){
        super(id, nome, endereço, sindicalizado);
        this.salarioMensal = salarioMensal;
    }
    
}
