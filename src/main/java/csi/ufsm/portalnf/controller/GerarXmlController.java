package csi.ufsm.portalnf.controller;

import csi.ufsm.portalnf.service.TransformProdutoPorNFEService;
import csi.ufsm.portalnf.service.TransformTodosProdutosService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/gerarXML")
public class GerarXmlController {
    private final TransformTodosProdutosService todosProdutosService = new TransformTodosProdutosService();
    private final TransformProdutoPorNFEService produtoPorNFEService = new TransformProdutoPorNFEService();
    private static final String NFE_DIRETORIO = "src/main/resources/nfexml"; // Caminho do diretório das NF-e

    // Endpoint para gerar um XML com todos os produtos de todas as NF-es
    @PostMapping("/produtos")
    public String gerarProdutosXML() {
        List<String> xmlPaths = carregarDocumentosDiretorio();
        String outputPath = "src/main/resources/nfexml/todos_produtos.xml";
        todosProdutosService.extrairTodosProdutosXML(xmlPaths, outputPath);
        return "XML com todos os produtos gerado com sucesso!";
    }

    // Endpoint para gerar um XML com os produtos de cada NF-e
    @PostMapping("/produtos-por-nota")
    public String gerarProdutosPorNotaXML() {
        List<String> xmlPaths = carregarDocumentosDiretorio();
        produtoPorNFEService.extrairProdutosPorNFE(xmlPaths);
        return "XMLs com produtos de cada NF-e gerados com sucesso!";
    }

    private List<String> carregarDocumentosDiretorio() {
        List<String> paths = new ArrayList<>();
        File directory = new File(NFE_DIRETORIO);
        if (directory.exists() && directory.isDirectory()) {
            File[] xmlFiles = directory.listFiles((dir, name) -> name.endsWith(".xml"));
            if (xmlFiles != null) {
                for (File xmlFile : xmlFiles) {
                    paths.add(xmlFile.getAbsolutePath());
                }
            }
        }
        return paths;
    }
}
