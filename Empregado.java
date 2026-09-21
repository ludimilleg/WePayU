
public abstract class Empregado{
    int id;
    String nome;
    String endereco;
    boolean sindicalizado;

    public Empregado(int id, String nome, String endereco, boolean sindicalizado){
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.sindicalizado = sindicalizado;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public boolean isSindicalizado() {
        return sindicalizado;
    }

    public abstract String getTipo();

     public abstract double getSalario();

     public double getComissao() throws ComissaoNaoAplicavelException {
        throw new ComissaoNaoAplicavelException();
    }
}