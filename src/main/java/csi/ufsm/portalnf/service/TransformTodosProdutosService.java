package csi.ufsm.portalnf.service;

import org.springframework.stereotype.Service;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.StringWriter;
import java.util.List;
@Service
public class TransformTodosProdutosService {

    // Extrai produtos de várias NF-e e gera um arquivo XML único com todos os produtos
    public void extrairTodosProdutosXML(List<String> xmlPaths, String outputPath) {
        try {
            // Cria um novo documento XML contendo os produtos
            Document newDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element root = newDocument.createElement("Produtos");
            newDocument.appendChild(root);

            for (String xmlPath : xmlPaths) {
                Document document = carregarXML(xmlPath);
                NodeList productList = document.getElementsByTagName("prod"); // Identifica os produtos

                for (int i = 0; i < productList.getLength(); i++) {
                    Node product = productList.item(i);
                    Node importedProduct = newDocument.importNode(product, true);
                    root.appendChild(importedProduct);
                }
            }

            salvarXML(newDocument, outputPath);
            System.out.println("Todos os produtos extraídos e salvos em: " + outputPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Funções auxiliares para carregar e salvar documentos XML

    private Document carregarXML(String xmlPath) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        return builder.parse(new File(xmlPath));
    }

    private void salvarXML(Document document, String filePath) throws Exception {
        TransformerFactory.newInstance().newTransformer()
                .transform(new DOMSource(document), new StreamResult(new File(filePath)));
    }
}
