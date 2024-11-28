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
public class OrdenarProdutosNomeService {

    private static String outputDirectory = "src/main/resources/nfexml/ordenado";

    public static void ordenarProdutosPorNome(List<String> xmlPaths) {
        try {
            // Configurar o DocumentBuilderFactory e Transformer uma vez
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Transformer transformer = createTransformer();

            for (String xmlPath : xmlPaths) {
                // Carregar cada NF-e
                Document doc = builder.parse(new File(xmlPath));

                // Remover espaços em branco
                removerNosVazios(doc);

                // Obter a lista de produtos da NF-e
                NodeList produtos = doc.getElementsByTagName("prod");

                // Verificar se há mais de um produto
                if (produtos.getLength() <= 1) {
                    System.out.println("A NF-e em " + xmlPath + " contém um único produto. Nenhuma ordenação necessária.");
                    continue;
                }

                // Armazenar os produtos em uma lista para ordenação
                List<Element> listaProdutos = new ArrayList<>();
                for (int i = 0; i < produtos.getLength(); i++) {
                    listaProdutos.add((Element) produtos.item(i));
                }

                // Ordenar os produtos alfabeticamente pelo nome
                listaProdutos.sort(Comparator.comparing(prod -> prod.getElementsByTagName("xProd").item(0).getTextContent()));

                // Remover os produtos originais do XML
                Node parent = produtos.item(0).getParentNode();
                while (parent.hasChildNodes()) {
                    parent.removeChild(parent.getFirstChild());
                }

                // Adicionar os produtos ordenados ao XML
                for (Element produto : listaProdutos) {
                    Node produtoImportado = doc.importNode(produto, true);
                    parent.appendChild(produtoImportado);
                }

                // Determinar o nome de arquivo de saída e salvar o XML ordenado
                String outputPath = outputDirectory + "/nfe_ordenado_" + new File(xmlPath).getName();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(outputPath));
                transformer.transform(source, result);

                System.out.println("Produtos ordenados e salvos em: " + outputPath);
            }
        } catch (Exception e) {
            System.out.println("Erro ao ordenar produtos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static Transformer createTransformer() throws TransformerConfigurationException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        return transformer;
    }

    // Método para remover nós de texto vazios
    private static void removerNosVazios(Node node) {
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);
            if (childNode.getNodeType() == Node.TEXT_NODE && childNode.getTextContent().trim().isEmpty()) {
                node.removeChild(childNode);
                i--; // Ajuste o índice após a remoção
            } else if (childNode.hasChildNodes()) {
                removerNosVazios(childNode); // Recursão para remover nós de texto vazios nos filhos
            }
        }
    }
}
