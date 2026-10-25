package br.ufal.ic.p2.wepayu;

public final class AlteracaoEmpregado {
    private final String atributo;
    private final String valor;
    private String idSindicato;
    private String taxaSindical;
    private String valorAuxiliar;
    private String comissao;
    private String salario;
    private String banco;
    private String agencia;
    private String contaCorrente;

    private AlteracaoEmpregado(String atributo, String valor) {
        this.atributo = atributo;
        this.valor = valor;
    }

    public static AlteracaoEmpregado para(String atributo, String valor) {
        return new AlteracaoEmpregado(atributo, valor);
    }

    public AlteracaoEmpregado comDadosSindicais(String idSindicato, String taxaSindical) {
        this.idSindicato = idSindicato;
        this.taxaSindical = taxaSindical;
        return this;
    }

    public AlteracaoEmpregado comValorAuxiliar(String valorAuxiliar) {
        this.valorAuxiliar = valorAuxiliar;
        return this;
    }

    public AlteracaoEmpregado comComissao(String comissao) {
        this.comissao = comissao;
        return this;
    }

    public AlteracaoEmpregado comSalario(String salario) {
        this.salario = salario;
        return this;
    }

    public AlteracaoEmpregado comDadosBancarios(
            String banco, String agencia, String contaCorrente) {
        this.banco = banco;
        this.agencia = agencia;
        this.contaCorrente = contaCorrente;
        return this;
    }

    String getAtributo() {
        return atributo;
    }

    String getValor() {
        return valor;
    }

    String getIdSindicato() {
        return idSindicato;
    }

    String getTaxaSindical() {
        return taxaSindical;
    }

    String getValorAuxiliar() {
        return valorAuxiliar;
    }

    String getComissao() {
        return comissao;
    }

    String getSalario() {
        return salario;
    }

    String getBanco() {
        return banco;
    }

    String getAgencia() {
        return agencia;
    }

    String getContaCorrente() {
        return contaCorrente;
    }
}
