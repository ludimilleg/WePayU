package br.ufal.ic.p2.wepayu;

import br.ufal.ic.p2.wepayu.Exception.PersistenciaException;
import br.ufal.ic.p2.wepayu.Exception.TipoInvalidoException;
import br.ufal.ic.p2.wepayu.Exception.TipoNaoAplicavelException;
import br.ufal.ic.p2.wepayu.models.Empregado;
import br.ufal.ic.p2.wepayu.models.EstadoSistema;
import br.ufal.ic.p2.wepayu.models.Exportador;
import br.ufal.ic.p2.wepayu.models.FabricaEmpregados;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
import org.w3c.dom.Node;
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
            empregado.exportarDadosExtras(new ExportadorXML(doc, empregadoElem));
        }

        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(new DOMSource(doc), new StreamResult(new File(caminho)));
        } catch (TransformerException e) {
            throw new PersistenciaException("Erro ao escrever o arquivo XML.");
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
            throw new PersistenciaException("Arquivo XML mal formado.");
        } catch (IOException e) {
            throw new PersistenciaException("Erro ao ler o arquivo XML.");
        }

        Element root = doc.getDocumentElement();
        int idGerado = Integer.parseInt(textoFilho(root, "idGerado"));
        Map<Integer, Empregado> empregados = new HashMap<>();
        NodeList nodes = root.getElementsByTagName("empregado");

        for (int i = 0; i < nodes.getLength(); i++) {
            Element empregadoElem = (Element) nodes.item(i);
            int id = Integer.parseInt(empregadoElem.getAttribute("id"));
            String tipo = empregadoElem.getAttribute("tipo");
            String comissaoTexto = textoFilho(empregadoElem, "comissao");
            Double comissao = comissaoTexto == null ? null : Double.valueOf(comissaoTexto);
            Empregado empregado;
            try {
                empregado = FabricaEmpregados.criar(tipo, id,
                        textoFilho(empregadoElem, "nome"),
                        textoFilho(empregadoElem, "endereco"),
                        Boolean.parseBoolean(textoFilho(empregadoElem, "sindicalizado")),
                        Double.parseDouble(textoFilho(empregadoElem, "salario")),
                        comissao);
            } catch (TipoInvalidoException | TipoNaoAplicavelException e) {
                throw new PersistenciaException("Tipo de empregado inválido no XML.");
            }

            atribuirSePresente(empregado, "idSindicato",
                    textoFilho(empregadoElem, "idSindicato"));
            empregado.setTaxaSindical(
                    Double.parseDouble(textoFilho(empregadoElem, "taxaSindical")));
            empregado.setMetodoPagamento(textoFilho(empregadoElem, "metodoPagamento"));
            String agendaPagamento = textoFilho(empregadoElem, "agendaPagamento");
            if (agendaPagamento != null) {
                empregado.setAgendaPagamento(agendaPagamento);
            }
            atribuirSePresente(empregado, "banco", textoFilho(empregadoElem, "banco"));
            atribuirSePresente(empregado, "agencia", textoFilho(empregadoElem, "agencia"));
            atribuirSePresente(empregado, "contaCorrente",
                    textoFilho(empregadoElem, "contaCorrente"));
            importarRegistros(empregado, empregadoElem, "taxasServico", "taxa",
                    "taxaServico");
            importarRegistros(empregado, empregadoElem, "cartoes", "cartao", "cartao");
            importarRegistros(empregado, empregadoElem, "vendas", "venda", "venda");
            empregados.put(id, empregado);
        }

        return new EstadoSistema(idGerado, empregados);
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
                                   String colecao, String registro, String nomeImportacao) {
        NodeList colecoes = origem.getElementsByTagName(colecao);
        if (colecoes.getLength() == 0) {
            return;
        }
        NodeList registros = ((Element) colecoes.item(0)).getElementsByTagName(registro);
        for (int i = 0; i < registros.getLength(); i++) {
            Element item = (Element) registros.item(i);
            empregado.importarRegistro(nomeImportacao, item.getAttribute("data"),
                    Double.parseDouble(item.getTextContent()));
        }
    }

    private void atribuirSePresente(Empregado empregado, String campo, String valor) {
        if (valor == null) {
            return;
        }
        switch (campo) {
            case "idSindicato":
                empregado.setIdSindicato(valor);
                break;
            case "banco":
                empregado.setBanco(valor);
                break;
            case "agencia":
                empregado.setAgencia(valor);
                break;
            case "contaCorrente":
                empregado.setContaCorrente(valor);
                break;
            default:
                throw new IllegalArgumentException("Campo não suportado: " + campo);
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

    private static class ExportadorXML implements Exportador {
        private final Document doc;
        private final Element empregado;

        ExportadorXML(Document doc, Element empregado) {
            this.doc = doc;
            this.empregado = empregado;
        }

        @Override
        public void campo(String nome, String valor) {
            Element campo = doc.createElement(nome);
            campo.setTextContent(valor);
            empregado.appendChild(campo);
        }

        @Override
        public void registro(String colecao, String data, double valor) {
            String containerNome;
            String registroNome;
            switch (colecao) {
                case "taxaServico":
                    containerNome = "taxasServico";
                    registroNome = "taxa";
                    break;
                case "cartao":
                    containerNome = "cartoes";
                    registroNome = "cartao";
                    break;
                case "venda":
                    containerNome = "vendas";
                    registroNome = "venda";
                    break;
                default:
                    throw new IllegalArgumentException("Coleção não suportada: " + colecao);
            }

            Element container = primeiroFilho(empregado, containerNome);
            if (container == null) {
                container = doc.createElement(containerNome);
                empregado.appendChild(container);
            }
            Element registro = doc.createElement(registroNome);
            registro.setAttribute("data", data);
            registro.setTextContent(String.valueOf(valor));
            container.appendChild(registro);
        }

        private Element primeiroFilho(Element pai, String nome) {
            NodeList nodes = pai.getElementsByTagName(nome);
            return nodes.getLength() == 0 ? null : (Element) nodes.item(0);
        }
    }
}
