package csi.ufsm.portalnf.controller;

import csi.ufsm.portalnf.service.ConsultasXmlService;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/consultas")
public class ConsultasXmlController {

    private final ConsultasXmlService nfexmlService;
    private static final String NFE_DIRETORIO = "src/main/resources/nfexml"; // Caminho do diretório das NF-e

    public ConsultasXmlController(ConsultasXmlService nfexmlService) {
        this.nfexmlService = nfexmlService;
    }

    @GetMapping("/total-nfs")
    public int getTotalNFs() {
        List<Document> docs = carregarDocumentosDiretorio();
        return nfexmlService.getTotalNFs(docs);
    }

    @GetMapping("/total-produtos")
    public int getTotalProdutos() {
        List<Document> docs = carregarDocumentosDiretorio();
        return nfexmlService.getTotalProdutos(docs);
    }

    @GetMapping("/total-valor-nfs")
    public double getTotalValorNFs() {
        List<Document> docs = carregarDocumentosDiretorio();
        return nfexmlService.getTotalValorNFs(docs);
    }

    @GetMapping("/total-impostos")
    public double getTotalImpostos() {
        List<Document> docs = carregarDocumentosDiretorio();
        return nfexmlService.getTotalImpostos(docs);
    }

    @GetMapping("/emitente")
    public List<String> getEmitentes() {
        List<Document> docs = carregarDocumentosDiretorio();
        List<String> emitentes = new ArrayList<>();
        for (Document doc : docs) {
            emitentes.add(nfexmlService.getEmitente(doc));
        }
        return emitentes;
    }

    @GetMapping("/destinatario")
    public List<String> getDestinatarios() {
        List<Document> docs = carregarDocumentosDiretorio();
        List<String> destinatarios = new ArrayList<>();
        for (Document doc : docs) {
            destinatarios.add(nfexmlService.getDestinatario(doc));
        }
        return destinatarios;
    }

    @GetMapping("/produtos")
    public List<List<String>> getProdutos() {
        List<Document> docs = carregarDocumentosDiretorio();
        List<List<String>> produtosList = new ArrayList<>();
        for (Document doc : docs) {
            produtosList.add(nfexmlService.getProdutos(doc));
        }
        return produtosList;
    }

    @GetMapping("/transportadora")
    public List<String> getTransportadoras() {
        List<Document> docs = carregarDocumentosDiretorio();
        List<String> transportadoras = new ArrayList<>();
        for (Document doc : docs) {
            transportadoras.add(nfexmlService.getTransportadora(doc));
        }
        return transportadoras;
    }

    @GetMapping("/impostos")
    public List<Map<String, String>> getImpostos() {
        List<Document> docs = carregarDocumentosDiretorio();
        List<Map<String, String>> impostosList = new ArrayList<>();
        for (Document doc : docs) {
            impostosList.add(nfexmlService.getImpostos(doc));
        }
        return impostosList;
    }

    @GetMapping("/fornecedores-transportadoras")
    public Map<String, List<String>> getFornecedoresTransportadorasOrdenados() {
        List<Document> docs = carregarDocumentosDiretorio();
        return nfexmlService.getFornecedoresTransportadorasOrdenados(docs);
    }

    private List<Document> carregarDocumentosDiretorio() {
        List<Document> docs = new ArrayList<>();
        File directory = new File(NFE_DIRETORIO);
        if (directory.exists() && directory.isDirectory()) {
            File[] xmlFiles = directory.listFiles((dir, name) -> name.endsWith(".xml"));
            if (xmlFiles != null) {
                for (File xmlFile : xmlFiles) {
                    Document doc = nfexmlService.carregarXML(xmlFile.getAbsolutePath());
                    if (doc != null) {
                        docs.add(doc);
                    }
                }
            }
        }
        return docs;
    }
}
