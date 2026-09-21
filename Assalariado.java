public class Assalariado extends Empregado {
    double salarioMensal;

    public Assalariado(int id, String nome, String endereço, boolean sindicalizado, double salarioMensal){
        super(id, nome, endereço, sindicalizado);
        this.salarioMensal = salarioMensal;
    }

    public String getTipo(){
        return "assalariado";
    }

    public double getSalario() {
        return salarioMensal;
    }
    
}