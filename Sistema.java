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
        Sistema sistema = new Sistema();
    }

    public int criarEmpregado(String nome, String endereco, String tipo, double salario, Double comissao){
    
        if (nome == null || nome.equals("")) {
            throw new IllegalArgumentException("Nome nao pode ser nulo."); 
        }

        if (endereco == null || endereco.equals("")) {
            throw new IllegalArgumentException("Endereco nao pode ser nulo.");
        }

        if (tipo == null || !tipo.equals("horista") && !tipo.equals("assalariado") && !tipo.equals("comissionado")) {
            throw new IllegalArgumentException("Tipo invalido.");
        }

        if (salario < 0) {
            throw new IllegalArgumentException("Salario deve ser nao-negativo.");
        }
        
        if (tipo.equals("comissionado") && comissao == null) {
            throw new IllegalArgumentException("Comissao nao pode ser nula.");
        }

        if (!tipo.equals("comissionado") && comissao != null) {
            throw new IllegalArgumentException("Tipo nao aplicavel.");
        }

        if (comissao != null && comissao < 0) {
            throw new IllegalArgumentException("Comissao deve ser nao-negativa.");
        }

        int id = gerarId();
        Empregados empregado;

        if (tipo.equals("horista")){
            empregado = new Horista(id, nome, endereco, false, salario);
        }
        else if (tipo.equals("assalariado")) {
            empregado = new Assalariado(id, nome, endereco, false, salario);
        } else {
            empregado = new Comissionado(id, nome, endereco, false, salario, comissao);
        }

        empregados.put(id, empregado);

        return id;
    }

    public Object getAtributoEmpregado(int id, String atributo){

    }

    public static void main(String[] args){
        Sistema sistema = new Sistema();
    }
}
