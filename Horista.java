public class Horista extends Empregado {
    double salarioHora;

    public Horista(int id, String nome, String endereço, boolean sindicalizado, double salarioHora){
        super(id, nome, endereço, sindicalizado);
        this.salarioHora = salarioHora;
    }
}
