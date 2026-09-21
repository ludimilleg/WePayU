public class Comissionado extends Assalariado {
    double comissao;

    public Comissionado (int id, String nome, String endereço, boolean sindicalizado, double salarioMensal, double comissao){
        super(id, nome, endereço, sindicalizado, salarioMensal);
        this.comissao = comissao;
    }    

    @Override 
    public String getTipo(){
        return "comissionado";
    }

    @Override
    public double getComissao() {
        return comissao;
    }

}
