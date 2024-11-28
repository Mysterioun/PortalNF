package csi.ufsm.portalnf;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
import java.io.IOException;

public class DTDValidator {

    public static List<String> validateWithDTD(List<String> xmlPaths) {
        List<String> validationResults = new ArrayList<>();

        for (String xmlPath : xmlPaths) {
            try {
                // Configura a fábrica do parser para validação com DTD
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setValidating(true); // Habilita a validação
                factory.setNamespaceAware(true); // Para trabalhar com namespaces

                // Cria o DocumentBuilder e configura o ErrorHandler para capturar erros de validação
                DocumentBuilder builder = factory.newDocumentBuilder();
                builder.setErrorHandler(new DefaultHandler() {
                    @Override
                    public void error(SAXParseException e) throws SAXException {
                        throw new SAXException("Erro de validação: " + e.getMessage());
                    }

                    @Override
                    public void fatalError(SAXParseException e) throws SAXException {
                        throw new SAXException("Erro fatal de validação: " + e.getMessage());
                    }

                    @Override
                    public void warning(SAXParseException e) throws SAXException {
                        System.out.println("Aviso: " + e.getMessage());
                    }
                });

                // Carrega e valida o XML
                builder.parse(new File(xmlPath));
                validationResults.add("[-] Validação da nota fiscal " + xmlPath + " foi bem-sucedida!");

            } catch (ParserConfigurationException | SAXException | IOException e) {
                validationResults.add("[-] Erro na nota fiscal " + xmlPath + ": " + e.getMessage());
            }
        }

        return validationResults;
    }

    public static void main(String[] args) {
        // Lista de caminhos dos arquivos XML das notas fiscais
        List<String> xmlPaths = new ArrayList<>();
        xmlPaths.add("src/main/resources/nfexml/nfe1.xml");
        xmlPaths.add("src/main/resources/nfexml/nfe2.xml");
        xmlPaths.add("src/main/resources/nfexml/nfe3.xml");
        xmlPaths.add("src/main/resources/nfexml/nfe4.xml");
        xmlPaths.add("src/main/resources/nfexml/nfe5.xml");
        xmlPaths.add("src/main/resources/nfexml/nfe6.xml");

        // Valida todas as notas fiscais e imprime os resultados
        List<String> results = validateWithDTD(xmlPaths);
        for (String result : results) {
            System.out.println(result);
        }
    }
}
