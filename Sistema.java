import java.util.HashMap;

public class Sistema {
    HashMap<Integer, Empregado> empregados;
    int idGerado;

    public Sistema(){
        idGerado = 0;
        empregados = new HashMap<>();
    }

    public int gerarId(){
        idGerado++;
        return idGerado;
    }

    public void zerarSistema() {
        idGerado = 0;
        empregados.clear();
    }

    public int criarEmpregado(String nome, String endereco, String tipo, double salario, Double comissao) 
        throws NomeInvalidoException, EnderecoInvalidoException, TipoInvalidoException, SalarioInvalidoException, 
        ComissaoInvalidaException, ComissaoNaoAplicavelException {
    
        if (nome == null || nome.equals("")) {
             throw new NomeInvalidoException(); 
        }
        if (endereco == null || endereco.equals("")) { 
            throw new EnderecoInvalidoException(); 
        } 
        if (tipo == null || (!tipo.equals("horista") 
            && !tipo.equals("assalariado") 
            && !tipo.equals("comissionado"))) { 
                throw new TipoInvalidoException(); 
        }
        if (salario < 0) { 
            throw new SalarioInvalidoException(); 
        } 
        if (tipo.equals("comissionado") && comissao == null) { 
            throw new ComissaoInvalidaException(); 
        } 
        if (!tipo.equals("comissionado") && comissao != null) { 
            throw new ComissaoNaoAplicavelException(); 
        } 
        if (comissao != null && comissao < 0) { 
            throw new ComissaoInvalidaException(); 
        }

        int id = gerarId();
        Empregado empregado;

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
        int x = 0;
        
        return x;
    }

    public static void main(String[] args){
        Sistema sistema = new Sistema();
    }
}