package csi.ufsm.portalnf.controller;

import csi.ufsm.portalnf.service.OrdenarProdutosPrecoService;
import csi.ufsm.portalnf.service.OrdenarProdutosNomeService;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/ordenacao")
public class OrdenarProdutosController {

    private static final String XML_DIRETORIO = "src/main/resources/nfexml"; // Diretório onde estão as NF-es

    @PostMapping("/ordenarPorNome")
    public String ordenarProdutosPorNome() {
        try {
            List<String> xmlPaths = carregarXmlsDoDiretorio();
            OrdenarProdutosNomeService.ordenarProdutosPorNome(xmlPaths);
            return "Produtos ordenados por nome e salvos!!!" ;
        } catch (Exception e) {
            return "Erro ao ordenar produtos por nome: " + e.getMessage();
        }
    }

    @PostMapping("/ordenarPorPreco")
    public String ordenarProdutosPorPreco() {
        try {
            List<String> xmlPaths = carregarXmlsDoDiretorio();
            OrdenarProdutosPrecoService.ordenarTodosProdutosPorPreco(xmlPaths);
            return "Produtos ordenados por preço e salvos!!!" ;
        } catch (Exception e) {
            return "Erro ao ordenar produtos por preço: " + e.getMessage();
        }    }

    private List<String> carregarXmlsDoDiretorio() {
        List<String> xmlPaths = new ArrayList<>();
        File folder = new File(XML_DIRETORIO);
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));

        if (files != null) {
            for (File file : files) {
                xmlPaths.add(file.getAbsolutePath());
            }
        }
        return xmlPaths;
    }
}
