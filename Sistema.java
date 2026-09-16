import java.util.HashMap;

public class Sistema {
    HashMap<Integer, Empregados> empregados;
    int idGerado;

    public Sistema(){
        idGerado = 0;
        empregados = new HashMap<>();
    }

    public int gerarId(){
        idGerado++;
        return idGerado;
    }

    public void zerarSistema(){

    }

    public int criarEmpregado(String nome, String endereco, String tipo, double salario, double comissao){
        
    }

    public Object getAtributoEmpregado(int id, String atributo){

    }

    public static void main(String[] args){
        Sistema sistema = new Sistema();
    }
}
