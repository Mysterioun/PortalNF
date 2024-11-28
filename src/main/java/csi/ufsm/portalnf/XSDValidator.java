package csi.ufsm.portalnf;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

public class XSDValidator {

    public static List<String> validateWithXSD(List<String> xmlPaths, String xsdPath) {
        List<String> validationResults = new ArrayList<>();

        try {
            // Cria um SchemaFactory para o XSD
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            // Carrega o XSD
            Schema schema = schemaFactory.newSchema(new File(xsdPath));
            // Cria um Validator a partir do Schema
            Validator validator = schema.newValidator();

            // Configura o ErrorHandler para capturar erros de validação
            validator.setErrorHandler(new DefaultHandler() {
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

            // Valida cada arquivo XML na lista
            for (String xmlPath : xmlPaths) {
                try {
                    // Verifica a estrutura do XML
                    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    factory.setNamespaceAware(true);
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    builder.parse(new File(xmlPath)); // Carrega o XML para garantir que ele está bem formado

                    // Valida o XML
                    Source source = new StreamSource(new File(xmlPath));
                    validator.validate(source);
                    validationResults.add("[-] Validação da nota fiscal " + xmlPath + " foi bem-sucedida!");
                } catch (ParserConfigurationException | SAXException | IOException e) {
                    validationResults.add("[-] Erro na nota fiscal " + xmlPath + ": " + e.getMessage());
                }
            }
        } catch (SAXException e) {
            System.out.println("Erro ao carregar o schema XSD: " + e.getMessage());
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

        String xsdPath = "src/main/resources/nfexml/xsd/procNFe_v4.00.xsd"; // Caminho do XSD

        // Valida todas as notas fiscais e imprime os resultados
        List<String> results = validateWithXSD(xmlPaths, xsdPath);
        for (String result : results) {
            System.out.println(result);
        }
    }
}
