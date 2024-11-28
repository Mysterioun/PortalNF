package csi.ufsm.portalnf.controller;


import csi.ufsm.portalnf.service.XmlToJsonService;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/converterJSON")
public class XmlToJsonController {

    private final XmlToJsonService nfeJsonService;
    private static final String NFE_DIRETORIO = "src/main/resources/nfexml";
    private static final String JSON_DIRETORIO = "src/main/resources/nfexml/json";

    public XmlToJsonController(XmlToJsonService nfeJsonService) {
        this.nfeJsonService = nfeJsonService;
    }

    // Converter todas as NF-e no diretório de XML para JSON
    @PostMapping("/converter")
    public List<String> convertAllXmlToJson() {
        List<String> jsonFiles = new ArrayList<>();
        File directory = new File(NFE_DIRETORIO);
        if (directory.exists() && directory.isDirectory()) {
            File[] xmlFiles = directory.listFiles((dir, name) -> name.endsWith(".xml"));
            if (xmlFiles != null) {
                for (File xmlFile : xmlFiles) {
                    String jsonPath = JSON_DIRETORIO + "/" + xmlFile.getName().replace(".xml", ".json");
                    nfeJsonService.converterXmlparaJson(xmlFile.getAbsolutePath(), jsonPath);
                    jsonFiles.add(jsonPath);
                }
            }
        }
        return jsonFiles;
    }
}
