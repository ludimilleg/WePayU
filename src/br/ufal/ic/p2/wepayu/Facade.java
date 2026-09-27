package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.models.Empregado;

import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import br.ufal.ic.p2.wepayu.models.Comissionado;
import br.ufal.ic.p2.wepayu.Exception.IdentificacaoEmpregadoNulaException;
import br.ufal.ic.p2.wepayu.Exception.AtributoNaoExisteException;
import br.ufal.ic.p2.wepayu.Exception.ComissaoNaoNumericaException;
import br.ufal.ic.p2.wepayu.Exception.ComissaoNegativaException;
import br.ufal.ic.p2.wepayu.Exception.ComissaoNulaException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoExisteException;
import br.ufal.ic.p2.wepayu.Exception.EnderecoInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.NomeInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.SalarioInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.SalarioNaoNumericoException;
import br.ufal.ic.p2.wepayu.Exception.SalarioNuloException;
import br.ufal.ic.p2.wepayu.Exception.TipoInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.TipoNaoAplicavelException;
import br.ufal.ic.p2.wepayu.Exception.DataInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.HorasPositivasException;
import br.ufal.ic.p2.wepayu.Exception.IdentificacaoSindicatoJaExisteException;
import br.ufal.ic.p2.wepayu.Exception.IdentificacaoEmpregadoNulaException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhHoristaException;
import br.ufal.ic.p2.wepayu.Exception.DataInicialInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.DataFinalInvalidaException;
import br.ufal.ic.p2.wepayu.Exception.DataInicialPosteriorDataFinalException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhHoristaException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhComissionadoException;
import br.ufal.ic.p2.wepayu.Exception.ValorPositivoException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhComissionadoException;
import br.ufal.ic.p2.wepayu.Exception.IdentificacaoSindicatoJaExisteException;
import br.ufal.ic.p2.wepayu.Exception.IdentificacaoMembroNulaException;
import br.ufal.ic.p2.wepayu.Exception.MembroNaoExisteException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhSindicalizadoException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoRecebeEmBancoException;
import br.ufal.ic.p2.wepayu.Exception.EmpregadoNaoEhComissionadoException;
import br.ufal.ic.p2.wepayu.Exception.MetodoPagamentoInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.BancoNuloException;
import br.ufal.ic.p2.wepayu.Exception.AgenciaNulaException;
import br.ufal.ic.p2.wepayu.Exception.ContaCorrenteNulaException;
import br.ufal.ic.p2.wepayu.Exception.ValorNaoBooleanoException;
import br.ufal.ic.p2.wepayu.Exception.IdentificacaoSindicatoNulaException;
import br.ufal.ic.p2.wepayu.Exception.TaxaSindicalNulaException;
import br.ufal.ic.p2.wepayu.Exception.TaxaSindicalNaoNumericaException;
import br.ufal.ic.p2.wepayu.Exception.TaxaSindicalNegativaException; 
import br.ufal.ic.p2.wepayu.Exception.SistemaEncerradoException;

public class Facade {

    private Sistema sistema;

    public Facade() {
        sistema = new Sistema();
    }

    public void zerarSistema()  {
        sistema.zerarSistema();
    }

    public int criarEmpregado(String nome, String endereco, String tipo, String salario) throws NomeInvalidoException,
         EnderecoInvalidoException,
         TipoInvalidoException,
         SalarioNuloException,
         SalarioNaoNumericoException,
         SalarioInvalidoException,
         TipoNaoAplicavelException {

    if (salario.equals("")) {
        throw new SalarioNuloException();
    }

    double salarioConvertido;

    try {
        salarioConvertido = Double.parseDouble(salario.replace(",", "."));
    } catch (NumberFormatException e) {
        throw new SalarioNaoNumericoException();
    }

    return sistema.criarEmpregado(
        nome,
        endereco,
        tipo,
        salarioConvertido,
        null
        );
    }

    public Object getAtributoEmpregado(String emp, String atributo)

            throws EmpregadoNaoExisteException,
                IdentificacaoEmpregadoNulaException,
                AtributoNaoExisteException,
                TipoNaoAplicavelException,
                EmpregadoNaoRecebeEmBancoException,
                EmpregadoNaoEhSindicalizadoException,
                EmpregadoNaoEhComissionadoException {

        if (emp.equals("")) {

            throw new IdentificacaoEmpregadoNulaException();

        }

        int id;

        try {

            id = Integer.parseInt(emp);

        } catch (NumberFormatException e) {

            throw new EmpregadoNaoExisteException();
        }

        return sistema.getAtributoEmpregado(id, atributo);
    }   

    public int criarEmpregado(
        String nome,
        String endereco,
        String tipo,
        String salario,
        String comissao
    ) throws NomeInvalidoException,
            EnderecoInvalidoException,
            TipoInvalidoException,
            SalarioNuloException,
            SalarioNaoNumericoException,
            SalarioInvalidoException,
            ComissaoNulaException,
            ComissaoNaoNumericaException,
            ComissaoNegativaException,
            TipoNaoAplicavelException {

        if (salario.equals("")) {
            throw new SalarioNuloException();
        }

        double salarioConvertido;

        try {
            salarioConvertido = Double.parseDouble(
                salario.replace(",", ".")
            );
        } catch (NumberFormatException e) {
            throw new SalarioNaoNumericoException();
        }

        if (comissao.equals("")) {
            throw new ComissaoNulaException();
        }

        double comissaoConvertida;

        try {
            comissaoConvertida = Double.parseDouble(
                comissao.replace(",", ".")
            );
        } catch (NumberFormatException e) {
            throw new ComissaoNaoNumericaException();
        }

        if (comissaoConvertida < 0) {
            throw new ComissaoNegativaException();
        }

        return sistema.criarEmpregado(
            nome,
            endereco,
            tipo,
            salarioConvertido,
            comissaoConvertida
        );
    }

    public void removerEmpregado(String emp)
        throws IdentificacaoEmpregadoNulaException,
            EmpregadoNaoExisteException {

        if (emp.equals("")) {
            throw new IdentificacaoEmpregadoNulaException();
        }

        int id;

        try {
            id = Integer.parseInt(emp);
        } catch (NumberFormatException e) {
            throw new EmpregadoNaoExisteException();
        }

        sistema.removerEmpregado(id);
    }

    public void lancaCartao(
            String emp,
            String data,
            String horas)
            throws IdentificacaoEmpregadoNulaException,
                EmpregadoNaoExisteException,
                TipoNaoAplicavelException,
                DataInvalidaException,
                HorasPositivasException,
                EmpregadoNaoEhHoristaException {

        if (emp.equals("")) {
            throw new IdentificacaoEmpregadoNulaException();
        }

        int id;

        try {
            id = Integer.parseInt(emp);
        } catch (NumberFormatException e) {
            throw new EmpregadoNaoExisteException();
        }

        double horasConvertidas;

        try {
            horasConvertidas = Double.parseDouble(
                horas.replace(",", ".")
            );
        } catch (NumberFormatException e) {
            throw new HorasPositivasException();
        }

        sistema.lancaCartao(
            id,
            data,
            horasConvertidas
        );
    }

    public String getHorasNormaisTrabalhadas(
            String emp,
            String dataInicial,
            String dataFinal)
            throws IdentificacaoEmpregadoNulaException,
                EmpregadoNaoExisteException,
                EmpregadoNaoEhHoristaException,
                DataInicialInvalidaException,
                DataFinalInvalidaException,
                DataInicialPosteriorDataFinalException {

        if (emp.equals("")) {
            throw new IdentificacaoEmpregadoNulaException();
        }

        int id;

        try {
            id = Integer.parseInt(emp);
        } catch (NumberFormatException e) {
            throw new EmpregadoNaoExisteException();
        }

        double total = sistema.getHorasNormaisTrabalhadas(
            id,
            dataInicial,
            dataFinal
        );

    return String.valueOf(total).replace(".", ",").replace(",0", "");

    }

    public String getHorasExtrasTrabalhadas(
            String emp,
            String dataInicial,
            String dataFinal)
            throws IdentificacaoEmpregadoNulaException,
                EmpregadoNaoExisteException,
                EmpregadoNaoEhHoristaException,
                DataInicialInvalidaException,
                DataFinalInvalidaException,
                DataInicialPosteriorDataFinalException {

        if (emp.equals("")) {
            throw new IdentificacaoEmpregadoNulaException();
        }

        int id;

        try {
            id = Integer.parseInt(emp);
        } catch (NumberFormatException e) {
            throw new EmpregadoNaoExisteException();
        }

        double total = sistema.getHorasExtrasTrabalhadas(
            id,
            dataInicial,
            dataFinal
        );

    
        return String.valueOf(total) .replace(".", ",") .replace(",0", "");
    }

    public void lancaVenda(
            String emp,
            String data,
            String valor)
            throws IdentificacaoEmpregadoNulaException,
                EmpregadoNaoExisteException,
                EmpregadoNaoEhComissionadoException,
                DataInvalidaException,
                ValorPositivoException {

        if (emp.equals("")) {
            throw new IdentificacaoEmpregadoNulaException();
        }

        int id;

        try {
            id = Integer.parseInt(emp);
        } catch (NumberFormatException e) {
            throw new EmpregadoNaoExisteException();
        }

        double valorConvertido;

        try {
            valorConvertido = Double.parseDouble(
                valor.replace(",", ".")
            );
        } catch (NumberFormatException e) {
            throw new ValorPositivoException();
        }

        sistema.lancaVenda(
            id,
            data,
            valorConvertido
        );
    }

    public String getVendasRealizadas(
            String emp,
            String dataInicial,
            String dataFinal)
            throws IdentificacaoEmpregadoNulaException,
                EmpregadoNaoExisteException,
                EmpregadoNaoEhComissionadoException,
                DataInicialInvalidaException,
                DataFinalInvalidaException,
                DataInicialPosteriorDataFinalException {

        if (emp.equals("")) {
            throw new IdentificacaoEmpregadoNulaException();
        }

        int id;

        try {
            id = Integer.parseInt(emp);
        } catch (NumberFormatException e) {
            throw new EmpregadoNaoExisteException();
        }

        double total = sistema.getVendasRealizadas(
            id,
            dataInicial,
            dataFinal
        );

        return String.format(
            Locale.US,
            "%.2f",
            total
        ).replace(".", ",");
    }

    public void alteraEmpregado(
        String emp,
        String atributo,
        String valor,
        String idSindicato,
        String taxaSindical,
        String valor1,
        String comissao,
        String salario,
        String banco,
        String agencia,
        String contaCorrente)
        throws EmpregadoNaoExisteException,
       IdentificacaoSindicatoJaExisteException,
       NomeInvalidoException,
       EnderecoInvalidoException,
       SalarioNuloException,
       SalarioNaoNumericoException,
       SalarioInvalidoException,
       TipoInvalidoException,
       ComissaoNulaException,
       ComissaoNaoNumericaException,
       ComissaoNegativaException,
       EmpregadoNaoEhComissionadoException,
       AtributoNaoExisteException, IdentificacaoEmpregadoNulaException,
        MetodoPagamentoInvalidoException,BancoNuloException,
        AgenciaNulaException,
        ContaCorrenteNulaException,
        ValorNaoBooleanoException,
        IdentificacaoSindicatoNulaException,
        TaxaSindicalNulaException,
        TaxaSindicalNaoNumericaException,
        TaxaSindicalNegativaException {

        if (emp.equals("")) {
            throw new IdentificacaoEmpregadoNulaException();
        }

        int id;

        try {
            id = Integer.parseInt(emp);
        } catch (NumberFormatException e) {
            throw new EmpregadoNaoExisteException();
        }

            sistema.alteraEmpregado(
                id,
                atributo,
                valor,
                idSindicato,
                taxaSindical,
                valor1,
                comissao,
                salario,
                banco,
                agencia,
                contaCorrente
            );
    }

    public void alteraEmpregado(
        String emp,
        String atributo,
        String valor,
        String comissao)
        throws EmpregadoNaoExisteException,
       IdentificacaoSindicatoJaExisteException,
       NomeInvalidoException,
       EnderecoInvalidoException,
       SalarioNuloException,
       SalarioNaoNumericoException,
       SalarioInvalidoException,
       TipoInvalidoException,
       ComissaoNulaException,
       ComissaoNaoNumericaException,
       ComissaoNegativaException,
       EmpregadoNaoEhComissionadoException,
       AtributoNaoExisteException, IdentificacaoEmpregadoNulaException,
        MetodoPagamentoInvalidoException,BancoNuloException,
        AgenciaNulaException,
        ContaCorrenteNulaException,
        ValorNaoBooleanoException,
        IdentificacaoSindicatoNulaException,
        TaxaSindicalNulaException,
        TaxaSindicalNaoNumericaException,
        TaxaSindicalNegativaException {
        alteraEmpregado(
        emp,
        atributo,
        valor,
        null,
        null,
        null,
        comissao,
        null,
        null,
        null,
        null
    );
}

    public void alteraEmpregado(
        String emp,
        String atributo,
        String valor)
        
        throws EmpregadoNaoExisteException,
       IdentificacaoSindicatoJaExisteException,
       NomeInvalidoException,
       EnderecoInvalidoException,
       SalarioNuloException,
       SalarioNaoNumericoException,
       SalarioInvalidoException,
       TipoInvalidoException,
       ComissaoNulaException,
       ComissaoNaoNumericaException,
       ComissaoNegativaException,
       EmpregadoNaoEhComissionadoException,
       AtributoNaoExisteException, IdentificacaoEmpregadoNulaException,
        MetodoPagamentoInvalidoException,BancoNuloException,
        AgenciaNulaException,
        ContaCorrenteNulaException,
        ValorNaoBooleanoException,
        IdentificacaoSindicatoNulaException,
        TaxaSindicalNulaException,
        TaxaSindicalNaoNumericaException,
        TaxaSindicalNegativaException {
        alteraEmpregado(
            emp,
            atributo,
            valor,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
            );
    }

    public void alteraEmpregado(
            String emp,
            String atributo,
            String valor,
            String idSindicato,
            String taxaSindical)
            
        throws EmpregadoNaoExisteException,
       IdentificacaoSindicatoJaExisteException,
       NomeInvalidoException,
       EnderecoInvalidoException,
       SalarioNuloException,
       SalarioNaoNumericoException,
       SalarioInvalidoException,
       TipoInvalidoException,
       ComissaoNulaException,
       ComissaoNaoNumericaException,
       ComissaoNegativaException,
       EmpregadoNaoEhComissionadoException,
       AtributoNaoExisteException, IdentificacaoEmpregadoNulaException,
        MetodoPagamentoInvalidoException,BancoNuloException,
        AgenciaNulaException,
        ContaCorrenteNulaException,
        ValorNaoBooleanoException,
        IdentificacaoSindicatoNulaException,
        TaxaSindicalNulaException,
        TaxaSindicalNaoNumericaException,
        TaxaSindicalNegativaException {
        alteraEmpregado(
            emp,
            atributo,
            valor,
            idSindicato,
            taxaSindical,
            null,
            null,
            null,
            null,
            null,
            null
        );
    }


    public void alteraEmpregado(
            String emp,
            String atributo,
            String valor1,
            String banco,
            String agencia,
            String contaCorrente)
        throws EmpregadoNaoExisteException,
       IdentificacaoSindicatoJaExisteException,
       NomeInvalidoException,
       EnderecoInvalidoException,
       SalarioNuloException,
       SalarioNaoNumericoException,
       SalarioInvalidoException,
       TipoInvalidoException,
       ComissaoNulaException,
       ComissaoNaoNumericaException,
       ComissaoNegativaException,
       EmpregadoNaoEhComissionadoException,
       AtributoNaoExisteException, IdentificacaoEmpregadoNulaException,
        MetodoPagamentoInvalidoException,BancoNuloException,
        AgenciaNulaException,
        ContaCorrenteNulaException,
        ValorNaoBooleanoException,
        IdentificacaoSindicatoNulaException,
        TaxaSindicalNulaException,
        TaxaSindicalNaoNumericaException,
        TaxaSindicalNegativaException {
        alteraEmpregado(
            emp,
            atributo,
            null,
            null,
            null,
            valor1,
            null,
            null,
            banco,
            agencia,
            contaCorrente
        );
    }


    public void lancaTaxaServico(
            String membro,
            String data,
            String valor)
            throws IdentificacaoMembroNulaException,
                MembroNaoExisteException,
                DataInvalidaException,
                ValorPositivoException {

        double valorDouble = Double.parseDouble(
            valor.replace(",", ".")
        );

        sistema.lancaTaxaServico(
            membro,
            data,
            valorDouble
        );
    }

    public String getTaxasServico(
            String emp,
            String dataInicial,
            String dataFinal)
            throws IdentificacaoEmpregadoNulaException,
                EmpregadoNaoExisteException,
                EmpregadoNaoEhSindicalizadoException,
                DataInicialInvalidaException,
                DataFinalInvalidaException,
                DataInicialPosteriorDataFinalException {

        if (emp.equals("")) {
            throw new IdentificacaoEmpregadoNulaException();
        }

        int id;

        try {
            id = Integer.parseInt(emp);
        } catch (NumberFormatException e) {
            throw new EmpregadoNaoExisteException();
        }

        double total = sistema.getTaxasServico(
            id,
            dataInicial,
            dataFinal
        );

        return String.format(
            Locale.US,
            "%.2f",
            total
        ).replace(".", ",");
    }

    public String totalFolha(String data)
        throws DataInvalidaException {

        return String.format(
            Locale.US,
            "%.2f",
            sistema.totalFolha(data)
        ).replace(".", ",");

        }
    public void rodaFolha(String data, String saida)
        throws DataInvalidaException, EmpregadoNaoExisteException,
        TipoNaoAplicavelException, FileNotFoundException {

        LocalDate dataPagamento;

        try {
            dataPagamento = LocalDate.parse(
                data,
                new DateTimeFormatterBuilder()
                    .appendPattern("d/M/uuuu")
                    .toFormatter()
                    .withResolverStyle(ResolverStyle.STRICT)
            );
        } catch (DateTimeParseException e) {
            throw new DataInvalidaException();
        }

        ArrayList<Empregado> lista =
            new ArrayList<>(sistema.getEmpregados().values());

        lista.sort((a, b) -> a.getNome().compareTo(b.getNome()));

        try (PrintWriter arquivo = new PrintWriter(saida)) {

            String cabecalho = "FOLHA DE PAGAMENTO DO DIA "
                + dataPagamento.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            arquivo.println(cabecalho);
            arquivo.println("=".repeat(cabecalho.length()));
            arquivo.println();

            // ===================== HORISTAS =====================

            arquivo.println("=".repeat(127));
            arquivo.println(secaoTitulo("HORISTAS"));
            arquivo.println("=".repeat(127));
            arquivo.println("Nome                                 Horas Extra Salario Bruto Descontos Salario Liquido Metodo");
            arquivo.println("==================================== ===== ===== ============= ========= =============== ======================================");

            double totalHorasNormais = 0;
            double totalHorasExtras = 0;
            double totalBrutoHoristas = 0;
            double totalDescontosHoristas = 0;
            double totalLiquidoHoristas = 0;

            for (Empregado empregado : lista) {

                if (empregado.getTipo().equals("horista")
                        && dataPagamento.getDayOfWeek() == DayOfWeek.FRIDAY) {

                    double horasNormais = empregado.getHorasNormais(dataPagamento);
                    double horasExtras = empregado.getHorasExtras(dataPagamento);
                    double salarioBruto = empregado.calculaPagamento(dataPagamento);
                    double descontos = empregado.getDescontos(dataPagamento);
                    double salarioLiquido = salarioBruto - descontos;

                    totalHorasNormais += horasNormais;
                    totalHorasExtras += horasExtras;
                    totalBrutoHoristas += salarioBruto;
                    totalDescontosHoristas += descontos;
                    totalLiquidoHoristas += salarioLiquido;

                    arquivo.println(String.format(
                        "%-36s %5.0f %5.0f %13s %9s %15s %s",
                        empregado.getNome(),
                        horasNormais,
                        horasExtras,
                        formatMoeda(salarioBruto),
                        formatMoeda(descontos),
                        formatMoeda(salarioLiquido),
                        empregado.getMetodoPagamentoFormatado()
                    ));
                }
            }

            arquivo.println();

            arquivo.println(String.format(
                "%-36s %5.0f %5.0f %13s %9s %15s",
                "TOTAL HORISTAS",
                totalHorasNormais,
                totalHorasExtras,
                formatMoeda(totalBrutoHoristas),
                formatMoeda(totalDescontosHoristas),
                formatMoeda(totalLiquidoHoristas)
            ));

            arquivo.println();

            // ===================== ASSALARIADOS =====================

            arquivo.println("=".repeat(127));
            arquivo.println(secaoTitulo("ASSALARIADOS"));
            arquivo.println("=".repeat(127));
            arquivo.println("Nome                                             Salario Bruto Descontos Salario Liquido Metodo");
            arquivo.println("================================================ ============= ========= =============== ======================================");

            double totalBrutoAssalariados = 0;
            double totalDescontosAssalariados = 0;
            double totalLiquidoAssalariados = 0;

            for (Empregado empregado : lista) {

                if (empregado.getTipo().equals("assalariado")
                        && empregado.calculaPagamento(dataPagamento) > 0) {

                    double salarioBruto = empregado.calculaPagamento(dataPagamento);
                    double descontos = empregado.getDescontos(dataPagamento);
                    double salarioLiquido = salarioBruto - descontos;

                    totalBrutoAssalariados += salarioBruto;
                    totalDescontosAssalariados += descontos;
                    totalLiquidoAssalariados += salarioLiquido;

                    arquivo.println(String.format(
                        "%-48s %13s %9s %15s %s",
                        empregado.getNome(),
                        formatMoeda(salarioBruto),
                        formatMoeda(descontos),
                        formatMoeda(salarioLiquido),
                        empregado.getMetodoPagamentoFormatado()
                    ));
                }
            }

            arquivo.println();

            arquivo.println(String.format(
                "%-48s %13s %9s %15s",
                "TOTAL ASSALARIADOS",
                formatMoeda(totalBrutoAssalariados),
                formatMoeda(totalDescontosAssalariados),
                formatMoeda(totalLiquidoAssalariados)
            ));

            arquivo.println();

            // ===================== COMISSIONADOS =====================

            arquivo.println("=".repeat(127));
            arquivo.println(secaoTitulo("COMISSIONADOS"));
            arquivo.println("=".repeat(127));
            arquivo.println("Nome                  Fixo     Vendas   Comissao Salario Bruto Descontos Salario Liquido Metodo");
            arquivo.println("===================== ======== ======== ======== ============= ========= =============== ======================================");

            double totalFixoComissionados = 0;
            double totalVendasComissionados = 0;
            double totalComissaoComissionados = 0;
            double totalBrutoComissionados = 0;
            double totalDescontosComissionados = 0;
            double totalLiquidoComissionados = 0;

            for (Empregado empregado : lista) {

                if (empregado.getTipo().equals("comissionado")
                        && empregado.calculaPagamento(dataPagamento) > 0) {

                    Comissionado comissionado = (Comissionado) empregado;

                    double fixo = comissionado.getSalarioFixo(dataPagamento);
                    double vendasPeriodo = comissionado.getVendasNoPeriodo(dataPagamento);
                    double comissaoValor = comissionado.getComissaoNoPeriodo(dataPagamento);
                    double salarioBruto = empregado.calculaPagamento(dataPagamento);
                    double descontos = empregado.getDescontos(dataPagamento);
                    double salarioLiquido = salarioBruto - descontos;

                    totalFixoComissionados += fixo;
                    totalVendasComissionados += vendasPeriodo;
                    totalComissaoComissionados += comissaoValor;
                    totalBrutoComissionados += salarioBruto;
                    totalDescontosComissionados += descontos;
                    totalLiquidoComissionados += salarioLiquido;

                    arquivo.println(String.format(
                        "%-21s %8s %8s %8s %13s %9s %15s %s",
                        empregado.getNome(),
                        formatMoeda(fixo),
                        formatMoeda(vendasPeriodo),
                        formatMoeda(comissaoValor),
                        formatMoeda(salarioBruto),
                        formatMoeda(descontos),
                        formatMoeda(salarioLiquido),
                        empregado.getMetodoPagamentoFormatado()
                    ));
                }
            }

            arquivo.println();

            arquivo.println(String.format(
                "%-21s %8s %8s %8s %13s %9s %15s",
                "TOTAL COMISSIONADOS",
                formatMoeda(totalFixoComissionados),
                formatMoeda(totalVendasComissionados),
                formatMoeda(totalComissaoComissionados),
                formatMoeda(totalBrutoComissionados),
                formatMoeda(totalDescontosComissionados),
                formatMoeda(totalLiquidoComissionados)
            ));

            arquivo.println();

            double totalFolha =
                totalBrutoHoristas
                + totalBrutoAssalariados
                + totalBrutoComissionados;

            arquivo.println("TOTAL FOLHA: " + formatMoeda(totalFolha));
        }
    }

    private String secaoTitulo(String label) {
        String esquerda = "=".repeat(21) + " ";
        String direita = " " + "=".repeat(127 - esquerda.length() - label.length() - 1);
        return esquerda + label + direita;
    }

    private String formatMoeda(double valor) {
        return String.format(Locale.US, "%.2f", valor).replace(".", ",");
    }

    private boolean encerrado = false;

    private void verificarSistemaEncerrado() throws SistemaEncerradoException {
        if (encerrado) {
            throw new SistemaEncerradoException();
        }
    }


    public void encerrarSistema() {
        encerrado = true;
    }
}