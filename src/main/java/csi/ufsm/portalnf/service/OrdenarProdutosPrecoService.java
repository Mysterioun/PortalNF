package csi.ufsm.portalnf.service;

import org.springframework.stereotype.Service;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class OrdenarProdutosPrecoService {

    private static String outputPath = "src/main/resources/nfexml/ordenado/produtos_ordenados_por_preco.xml";

    public static void ordenarTodosProdutosPorPreco(List<String> xmlPaths) {
        try {
            // Configurar o DocumentBuilderFactory e Transformer uma vez
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            // Configurações do Transformer para evitar espaços em branco
            transformer.setOutputProperty(OutputKeys.INDENT, "no"); // Não permitir indentação
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes"); // Não incluir declaração XML
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "0"); // Especificar quantidade de indentação

            // Lista para armazenar todos os produtos de todas as NF-e
            List<Element> listaProdutos = new ArrayList<>();

            // Carregar e extrair produtos de cada NF-e
            for (String xmlPath : xmlPaths) {
                Document doc = builder.parse(new File(xmlPath));
                NodeList produtos = doc.getElementsByTagName("prod");

                for (int i = 0; i < produtos.getLength(); i++) {
                    listaProdutos.add((Element) produtos.item(i));
                }
            }

            // Ordenar a lista de produtos por preço (tag <vProd>)
            listaProdutos.sort(Comparator.comparing(prod -> Double.parseDouble(prod.getElementsByTagName("vProd").item(0).getTextContent())));

            // Criar um novo documento XML para armazenar o XML consolidado
            Document docConsolidado = builder.newDocument();
            Element rootElement = docConsolidado.createElement("ProdutosOrdenados");
            docConsolidado.appendChild(rootElement);

            // Adicionar os produtos ordenados ao documento consolidado
            for (Element produto : listaProdutos) {
                Node produtoImportado = docConsolidado.importNode(produto, true);
                rootElement.appendChild(produtoImportado);
            }

            // Salvar o XML consolidado
            DOMSource source = new DOMSource(docConsolidado);
            StreamResult result = new StreamResult(new File(outputPath));
            transformer.transform(source, result);

            System.out.println("Produtos de todas as NF-e ordenados por preço e salvos em: " + outputPath);

        } catch (Exception e) {
            System.out.println("Erro ao ordenar produtos por preço: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
