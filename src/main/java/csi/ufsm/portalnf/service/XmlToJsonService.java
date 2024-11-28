package csi.ufsm.portalnf.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class XmlToJsonService {

    // Método que converte um XML de NF-e para JSON
    public void converterXmlparaJson(String xmlPath, String jsonPath) {
        try {
            // Instancia o XmlMapper para ler o XML
            XmlMapper xmlMapper = new XmlMapper();
            // Lê o XML e converte para um objeto Java
            Object nfe = xmlMapper.readValue(new File(xmlPath), Object.class);

            // Instancia o ObjectMapper para escrever o JSON
            ObjectMapper jsonMapper = new ObjectMapper();
            // Escreve o objeto convertido em formato JSON
            jsonMapper.writerWithDefaultPrettyPrinter().writeValue(new File(jsonPath), nfe);

            System.out.println("Conversão concluída: " + jsonPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
