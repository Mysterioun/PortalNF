package csi.ufsm.portalnf.service;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import javax.xml.namespace.NamespaceContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.*;

@Service
public class ConsultasXmlService {

    // Define a manipulação de namespaces, se houver no XML
    static class NFENamespaceContext implements NamespaceContext {
        @Override
        public String getNamespaceURI(String prefix) {
            if ("nfe".equals(prefix)) {
                return "http://www.portalfiscal.inf.br/nfe"; // Namespace do XML da NF-e
            }
            return null;
        }

        @Override
        public String getPrefix(String namespaceURI) {
            return null;
        }

        @Override
        public Iterator<String> getPrefixes(String namespaceURI) {
            return null;
        }
    }

    // Carrega um arquivo XML e retorna o documento
    public Document carregarXML(String xmlPath) {
        try {
            File xmlFile = new File(xmlPath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true); // Habilita suporte a namespaces
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(xmlFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Consulta o número total de NFs
    public int getTotalNFs(List<Document> docs) {
        int totalNFs = 0;
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(new NFENamespaceContext()); // Configura namespaces
            XPathExpression expr = xpath.compile("count(//nfe:nfeProc)");
            for (Document doc : docs) {
                totalNFs += ((Double) expr.evaluate(doc, XPathConstants.NUMBER)).intValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalNFs;
    }

    // Consulta o número total de produtos
    public int getTotalProdutos(List<Document> docs) {
        int totalProdutos = 0;
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(new NFENamespaceContext()); // Configura namespaces
            XPathExpression expr = xpath.compile("count(//nfe:det/nfe:prod)");
            for (Document doc : docs) {
                totalProdutos += ((Double) expr.evaluate(doc, XPathConstants.NUMBER)).intValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalProdutos;
    }

    // Consulta o valor total das NFs
    public double getTotalValorNFs(List<Document> docs) {
        double totalValor = 0;
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(new NFENamespaceContext()); // Configura namespaces
            XPathExpression expr = xpath.compile("sum(//nfe:ICMSTot/nfe:vNF)");
            for (Document doc : docs) {
                totalValor += ((Double) expr.evaluate(doc, XPathConstants.NUMBER));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalValor;
    }

    // Consulta o valor total de impostos
    public double getTotalImpostos(List<Document> docs) {
        double totalImpostos = 0;
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(new NFENamespaceContext()); // Configura namespaces
            XPathExpression expr = xpath.compile("sum(//nfe:ICMSTot/nfe:vTotTrib)");
            for (Document doc : docs) {
                totalImpostos += ((Double) expr.evaluate(doc, XPathConstants.NUMBER));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalImpostos;
    }

    // Consulta detalhada por NF - Emitente
    public String getEmitente(Document document) {
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(new NFENamespaceContext());
            XPathExpression expr = xpath.compile("//nfe:emit/nfe:xNome");
            return (String) expr.evaluate(document, XPathConstants.STRING);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Consulta detalhada por NF - Destinatário
    public String getDestinatario(Document document) {
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(new NFENamespaceContext());
            XPathExpression expr = xpath.compile("//nfe:dest/nfe:xNome");
            return (String) expr.evaluate(document, XPathConstants.STRING);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Consulta detalhada por NF - Produtos
    public List<String> getProdutos(Document document) {
        List<String> produtos = new ArrayList<>();
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(new NFENamespaceContext());
            XPathExpression expr = xpath.compile("//nfe:det/nfe:prod/nfe:xProd");
            NodeList nodeList = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); i++) {
                produtos.add(nodeList.item(i).getTextContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return produtos;
    }

    // Consulta detalhada por NF - Transportadora
    public String getTransportadora(Document document) {
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(new NFENamespaceContext());
            XPathExpression expr = xpath.compile("//nfe:transporta/nfe:xNome");
            return (String) expr.evaluate(document, XPathConstants.STRING);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Consulta detalhada por NF - Impostos (ICMS, IPI, PIS, COFINS)
    public Map<String, String> getImpostos(Document document) {
        Map<String, String> impostos = new HashMap<>();
        try {
            XPath xpath = XPathFactory.newInstance().newXPath();
            xpath.setNamespaceContext(new NFENamespaceContext());

            // Consulta os valores de cada imposto nos nós aninhados
            XPathExpression icmsExpr = xpath.compile("//nfe:ICMS/nfe:ICMS00/nfe:vICMS");
            XPathExpression ipiExpr = xpath.compile("//nfe:IPI/nfe:vIPI");
            XPathExpression pisExpr = xpath.compile("//nfe:PIS/nfe:PISAliq/nfe:vPIS");
            XPathExpression cofinsExpr = xpath.compile("//nfe:COFINS/nfe:COFINSAliq/nfe:vCOFINS");

            String icmsValor = (String) icmsExpr.evaluate(document, XPathConstants.STRING);
            String ipiValor = (String) ipiExpr.evaluate(document, XPathConstants.STRING);
            String pisValor = (String) pisExpr.evaluate(document, XPathConstants.STRING);
            String cofinsValor = (String) cofinsExpr.evaluate(document, XPathConstants.STRING);

            // Formata a saída incluindo o nome do imposto e o valor pago
            impostos.put("ICMS", "Valor pago em ICMS: R$ " + (icmsValor.isEmpty() ? "0.00" : icmsValor));
            impostos.put("IPI", "Valor pago em IPI: R$ " + (ipiValor.isEmpty() ? "0.00" : ipiValor));
            impostos.put("PIS", "Valor pago em PIS: R$ " + (pisValor.isEmpty() ? "0.00" : pisValor));
            impostos.put("COFINS", "Valor pago em COFINS: R$ " + (cofinsValor.isEmpty() ? "0.00" : cofinsValor));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return impostos;
    }



    // Consulta de fornecedores e transportadoras, ordenando por nome
    public Map<String, List<String>> getFornecedoresTransportadorasOrdenados(List<Document> docs) {
        Map<String, List<String>> fornecedoresTransportadoras = new TreeMap<>();
        try {
            for (Document document : docs) {
                String fornecedor = getEmitente(document);
                String transportadora = getTransportadora(document);
                fornecedoresTransportadoras.computeIfAbsent(fornecedor, k -> new ArrayList<>()).add(transportadora);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fornecedoresTransportadoras;
    }
}
