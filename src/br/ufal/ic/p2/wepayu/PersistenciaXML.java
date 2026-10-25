package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.exception.PersistenciaException;
import br.ufal.ic.p2.wepayu.exception.TipoInvalidoException;
import br.ufal.ic.p2.wepayu.exception.TipoNaoAplicavelException;
import br.ufal.ic.p2.wepayu.exception.ColecaoPersistenciaDesconhecidaException;
import br.ufal.ic.p2.wepayu.exception.AgendaPagamentoInvalidaException;
import br.ufal.ic.p2.wepayu.exception.DataInvalidaException;
import br.ufal.ic.p2.wepayu.exception.EmpregadoNaoEhComissionadoException;
import br.ufal.ic.p2.wepayu.exception.EmpregadoNaoEhHoristaException;
import br.ufal.ic.p2.wepayu.exception.HorasPositivasException;
import br.ufal.ic.p2.wepayu.exception.TaxaServicoInvalidaException;
import br.ufal.ic.p2.wepayu.exception.ValorPositivoException;
import br.ufal.ic.p2.wepayu.exception.AgenciaModeloInvalidaException;
import br.ufal.ic.p2.wepayu.exception.BancoModeloInvalidoException;
import br.ufal.ic.p2.wepayu.exception.ComissaoModeloInvalidaException;
import br.ufal.ic.p2.wepayu.exception.ContaCorrenteModeloInvalidaException;
import br.ufal.ic.p2.wepayu.exception.EnderecoEmpregadoInvalidoException;
import br.ufal.ic.p2.wepayu.exception.HorasCartaoInvalidasException;
import br.ufal.ic.p2.wepayu.exception.IdentificacaoSindicalInvalidaException;
import br.ufal.ic.p2.wepayu.exception.MetodoPagamentoModeloInvalidoException;
import br.ufal.ic.p2.wepayu.exception.NomeEmpregadoInvalidoException;
import br.ufal.ic.p2.wepayu.exception.SalarioModeloInvalidoException;
import br.ufal.ic.p2.wepayu.exception.TaxaSindicalInvalidaException;
import br.ufal.ic.p2.wepayu.exception.ValorVendaInvalidoException;

import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.EstadoSistema;
import br.ufal.ic.p2.wepayu.models.AgendaPagamento;
import br.ufal.ic.p2.wepayu.models.FabricaEmpregados;

import java.io.File;
import java.io.IOException;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;

import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

public class PersistenciaXML {

    public void salvar(String caminho, EstadoSistema estado) throws PersistenciaException {
        Document doc = novoDocumento();
        Element root = doc.createElement("sistema");
        doc.appendChild(root);
        criarFilho(doc, root, "idGerado", String.valueOf(estado.getIdGerado()));

        Element empregadosElem = doc.createElement("empregados");
        root.appendChild(empregadosElem);

        Element agendasElem = doc.createElement("agendasPagamento");
        root.appendChild(agendasElem);
        for (String agenda : estado.getAgendasPagamento()) {
            criarFilho(doc, agendasElem, "agenda", agenda);
        }

        for (Empregado empregado : estado.getEmpregados().values()) {
            Element empregadoElem = doc.createElement("empregado");
            empregadoElem.setAttribute("id", String.valueOf(empregado.getId()));
            empregadoElem.setAttribute("tipo", empregado.getTipo());
            empregadosElem.appendChild(empregadoElem);

            criarFilho(doc, empregadoElem, "nome", empregado.getNome());
            criarFilho(doc, empregadoElem, "endereco", empregado.getEndereco());
            criarFilho(doc, empregadoElem, "sindicalizado",
                    String.valueOf(empregado.isSindicalizado()));
            criarSeNulo(doc, empregadoElem, "idSindicato", empregado.getIdSindicato());
            criarFilho(doc, empregadoElem, "taxaSindical",
                    String.valueOf(empregado.getTaxaSindical()));
            criarFilho(doc, empregadoElem, "metodoPagamento",
                    empregado.getMetodoPagamento());
            criarFilho(doc, empregadoElem, "agendaPagamento",
                    empregado.getAgendaPagamento());
            criarSeNulo(doc, empregadoElem, "banco", empregado.getBanco());
            criarSeNulo(doc, empregadoElem, "agencia", empregado.getAgencia());
            criarSeNulo(doc, empregadoElem, "contaCorrente", empregado.getContaCorrente());
            criarFilho(doc, empregadoElem, "salario",
                    String.valueOf(empregado.getSalario()));

            empregadoElem.appendChild(doc.createElement("taxasServico"));
            escreverRegistros(doc, empregadoElem, "taxasServico", "taxa",
                    empregado.getTaxasServico());
            if ("comissionado".equals(empregado.getTipo())) {
                try {
                    criarFilho(doc, empregadoElem, "comissao",
                            String.valueOf(empregado.getComissao()));
                } catch (TipoNaoAplicavelException e) {
                    throw new PersistenciaException(
                            "Comissao ausente para empregado comissionado.");
                }
            }
            escreverRegistros(doc, empregadoElem, "cartoes", "cartao",
                    empregado.getCartoes());
            escreverRegistros(doc, empregadoElem, "vendas", "venda",
                    empregado.getVendas());
        }

        Path arquivoTemporario = null;
        PersistenciaException falha = null;
        try {
            Path destino = Paths.get(caminho).toAbsolutePath();
            if (Files.isDirectory(destino)) {
                throw new IOException("O destino do XML e um diretorio.");
            }
            arquivoTemporario = Files.createTempFile(destino.getParent(), "wepayu-", ".tmp");
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(doc),
                    new StreamResult(arquivoTemporario.toFile()));
            try {
                Files.move(arquivoTemporario, destino, StandardCopyOption.ATOMIC_MOVE,
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(arquivoTemporario, destino, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException | SecurityException | TransformerException e) {
            falha = new PersistenciaException("Erro ao escrever o arquivo XML.", e);
        } finally {
            if (arquivoTemporario != null) {
                try {
                    Files.deleteIfExists(arquivoTemporario);
                } catch (IOException | SecurityException e) {
                    if (falha == null) {
                        falha = new PersistenciaException(
                                "Erro ao remover o arquivo XML temporario.", e);
                    } else {
                        falha.addSuppressed(e);
                    }
                }
            }
        }
        if (falha != null) {
            throw falha;
        }
    }

    public EstadoSistema carregar(String caminho, EstadoSistema estadoAtual)
            throws PersistenciaException {
        File arquivo = new File(caminho);
        if (!arquivo.exists()) {
            return estadoAtual;
        }

        Document doc;
        try {
            doc = novoBuilder().parse(arquivo);
        } catch (SAXException e) {
            throw new PersistenciaException("Arquivo XML mal formado.", e);
        } catch (IOException e) {
            throw new PersistenciaException("Erro ao ler o arquivo XML.", e);
        }

        try {
            return carregarEstado(doc);
        } catch (NumberFormatException | AgendaPagamentoInvalidaException
                 | AgenciaModeloInvalidaException | BancoModeloInvalidoException
                 | ComissaoModeloInvalidaException | ContaCorrenteModeloInvalidaException
                 | EnderecoEmpregadoInvalidoException | HorasCartaoInvalidasException
                 | IdentificacaoSindicalInvalidaException
                 | MetodoPagamentoModeloInvalidoException | NomeEmpregadoInvalidoException
                 | SalarioModeloInvalidoException | TaxaServicoInvalidaException
                 | TaxaSindicalInvalidaException | ValorVendaInvalidoException e) {
            throw new PersistenciaException("Dados invalidos no arquivo XML.", e);
        }
    }

    private EstadoSistema carregarEstado(Document doc) throws PersistenciaException {
        Element root = doc.getDocumentElement();
        if (root == null || !"sistema".equals(root.getTagName())) {
            throw new PersistenciaException("Estrutura invalida no arquivo XML.");
        }
        int idGerado = Integer.parseInt(textoObrigatorio(root, "idGerado"));
        if (idGerado < 0) {
            throw new PersistenciaException("Identificador gerado invalido no XML.");
        }
        Map<Integer, Empregado> empregados = new HashMap<>();
        Set<String> agendasPagamento =
                new LinkedHashSet<>(AgendaPagamento.agendasPadrao());
        NodeList agendas = root.getElementsByTagName("agendasPagamento");
        if (agendas.getLength() > 0) {
            agendasPagamento.clear();
            NodeList itens = ((Element) agendas.item(0)).getElementsByTagName("agenda");
            for (int i = 0; i < itens.getLength(); i++) {
                agendasPagamento.add(
                        AgendaPagamento.normalizar(itens.item(i).getTextContent()));
            }
        }
        NodeList nodes = root.getElementsByTagName("empregado");

        for (int i = 0; i < nodes.getLength(); i++) {
            Element empregadoElem = (Element) nodes.item(i);
            int id = Integer.parseInt(atributoObrigatorio(empregadoElem, "id"));
            if (id <= 0 || id > idGerado) {
                throw new PersistenciaException("Identificador de empregado invalido no XML.");
            }
            if (empregados.containsKey(id)) {
                throw new PersistenciaException("Identificador de empregado duplicado no XML.");
            }
            String tipo = empregadoElem.getAttribute("tipo");
            String comissaoTexto = textoFilho(empregadoElem, "comissao");
            Double comissao = comissaoTexto == null ? null : Double.valueOf(comissaoTexto);
            boolean sindicalizado =
                    parseBooleano(textoObrigatorio(empregadoElem, "sindicalizado"));
            Empregado empregado;
            try {
                empregado = FabricaEmpregados.criar(tipo, id,
                        textoObrigatorio(empregadoElem, "nome"),
                        textoObrigatorio(empregadoElem, "endereco"),
                        Double.parseDouble(textoObrigatorio(empregadoElem, "salario")),
                        comissao);
            } catch (TipoInvalidoException | TipoNaoAplicavelException e) {
                throw new PersistenciaException("Tipo de empregado inválido no XML.");
            }

            String idSindicato = textoFilho(empregadoElem, "idSindicato");
            double taxaSindical =
                    Double.parseDouble(textoObrigatorio(empregadoElem, "taxaSindical"));
            empregado.alterarSindicalizacao(sindicalizado, idSindicato, taxaSindical);
            String banco = textoFilho(empregadoElem, "banco");
            String agencia = textoFilho(empregadoElem, "agencia");
            String contaCorrente = textoFilho(empregadoElem, "contaCorrente");
            empregado.alterarMetodoPagamento(
                    textoObrigatorio(empregadoElem, "metodoPagamento"),
                    banco, agencia, contaCorrente);
            String agendaPagamento = textoFilho(empregadoElem, "agendaPagamento");
            if (agendaPagamento != null) {
                String agendaNormalizada;
                try {
                    agendaNormalizada = AgendaPagamento.normalizar(agendaPagamento);
                } catch (AgendaPagamentoInvalidaException e) {
                    throw new PersistenciaException(
                            "Agenda de pagamento invalida no XML.");
                }
                if (!agendasPagamento.contains(agendaNormalizada)) {
                    throw new PersistenciaException(
                            "Agenda de pagamento do empregado nao esta disponivel.");
                }
                empregado.setAgendaPagamento(agendaNormalizada);
            }
            importarRegistros(empregado, empregadoElem, "taxasServico", "taxa");
            importarRegistros(empregado, empregadoElem, "cartoes", "cartao");
            importarRegistros(empregado, empregadoElem, "vendas", "venda");
            empregados.put(id, empregado);
        }

        return new EstadoSistema(idGerado, empregados, agendasPagamento);
    }

    private Document novoDocumento() throws PersistenciaException {
        return novoBuilder().newDocument();
    }

    private DocumentBuilder novoBuilder() throws PersistenciaException {
        try {
            return DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new PersistenciaException("Erro ao configurar o parser XML.");
        }
    }

    private void importarRegistros(Empregado empregado, Element origem,
                                   String colecao, String registro)
            throws PersistenciaException {
        NodeList colecoes = origem.getElementsByTagName(colecao);
        if (colecoes.getLength() == 0) {
            return;
        }
        NodeList registros = ((Element) colecoes.item(0)).getElementsByTagName(registro);
        for (int i = 0; i < registros.getLength(); i++) {
            Element item = (Element) registros.item(i);
            String data = item.getAttribute("data");
            double valor = Double.parseDouble(item.getTextContent());
            try {
                switch (colecao) {
                    case "taxasServico":
                        empregado.registrarTaxaServico(data, valor);
                        break;
                    case "cartoes":
                        empregado.lancarCartao(data, valor);
                        break;
                    case "vendas":
                        empregado.lancarVenda(data, valor);
                        break;
                    default:
                        throw new ColecaoPersistenciaDesconhecidaException(colecao);
                }
            } catch (DataInvalidaException | EmpregadoNaoEhHoristaException
                     | EmpregadoNaoEhComissionadoException | HorasPositivasException
                     | ValorPositivoException | TaxaServicoInvalidaException e) {
                throw new PersistenciaException("Registro invalido no XML.");
            }
        }
    }

    private void escreverRegistros(Document doc, Element empregado, String colecao,
                                   String registro, Map<String, Double> valores) {
        if (valores.isEmpty()) {
            return;
        }
        Element container = primeiroFilho(empregado, colecao);
        if (container == null) {
            container = doc.createElement(colecao);
            empregado.appendChild(container);
        }
        for (Map.Entry<String, Double> valor : valores.entrySet()) {
            Element item = doc.createElement(registro);
            item.setAttribute("data", valor.getKey());
            item.setTextContent(String.valueOf(valor.getValue()));
            container.appendChild(item);
        }
    }

    private void criarSeNulo(Document doc, Element pai, String nome, String valor) {
        if (valor != null) {
            criarFilho(doc, pai, nome, valor);
        }
    }

    private void criarFilho(Document doc, Element pai, String nome, String valor) {
        Element filho = doc.createElement(nome);
        filho.setTextContent(valor);
        pai.appendChild(filho);
    }

    private String textoFilho(Element pai, String nome) {
        NodeList nodes = pai.getElementsByTagName(nome);
        return nodes.getLength() == 0 ? null : nodes.item(0).getTextContent();
    }

    private String textoObrigatorio(Element pai, String nome) throws PersistenciaException {
        String valor = textoFilho(pai, nome);
        if (valor == null || valor.isEmpty()) {
            throw new PersistenciaException("Campo obrigatorio ausente no XML: " + nome);
        }
        return valor;
    }

    private String atributoObrigatorio(Element elemento, String nome)
            throws PersistenciaException {
        String valor = elemento.getAttribute(nome);
        if (valor.isEmpty()) {
            throw new PersistenciaException("Atributo obrigatorio ausente no XML: " + nome);
        }
        return valor;
    }

    private boolean parseBooleano(String valor) throws PersistenciaException {
        if (!"true".equalsIgnoreCase(valor) && !"false".equalsIgnoreCase(valor)) {
            throw new PersistenciaException("Valor booleano invalido no arquivo XML.");
        }
        return Boolean.parseBoolean(valor);
    }

    private Element primeiroFilho(Element pai, String nome) {
        NodeList nodes = pai.getElementsByTagName(nome);
        return nodes.getLength() == 0 ? null : (Element) nodes.item(0);
    }
}
