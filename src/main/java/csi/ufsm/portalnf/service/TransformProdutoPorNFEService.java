package csi.ufsm.portalnf.service;

import org.springframework.stereotype.Service;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.List;

@Service
public class TransformProdutoPorNFEService {

    // Extrai produtos de cada NF-e e gera arquivos XML individuais contendo esses produtos
    public void extrairProdutosPorNFE(List<String> xmlPaths) {
        for (String xmlPath : xmlPaths) {
            String outputPath = getCaminhoOutput(xmlPath);
            extrairProdutosXML(xmlPath, outputPath);
        }
    }

    // Extrai produtos de uma única NF-e e gera um arquivo XML contendo esses produtos
    private void extrairProdutosXML(String xmlPath, String outputPath) {
        try {
            Document document = carregarXML(xmlPath);
            NodeList productList = document.getElementsByTagName("prod"); // Identifica os produtos

            // Cria um novo documento XML contendo os produtos
            Document newDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = newDocument.createElement("Produtos");
            newDocument.appendChild(root);

            for (int i = 0; i < productList.getLength(); i++) {
                Node product = productList.item(i);
                Node importedProduct = newDocument.importNode(product, true);
                root.appendChild(importedProduct);
            }

            salvarXML(newDocument, outputPath);
            System.out.println("Produtos extraídos de " + xmlPath + " e salvos em: " + outputPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Gera o caminho do arquivo de saída baseado no caminho da NF-e
    private String getCaminhoOutput(String xmlPath) {
        String basePath = xmlPath.substring(0, xmlPath.lastIndexOf('.'));
        return basePath + "_produtos.xml";
    }

    private Document carregarXML(String xmlPath) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        return builder.parse(new File(xmlPath));
    }

    private void salvarXML(Document document, String filePath) throws Exception {
        TransformerFactory.newInstance().newTransformer()
                .transform(new DOMSource(document), new StreamResult(new File(filePath)));
    }
}

