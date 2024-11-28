# Projeto Notas Fiscais Eletrônicas (PORTALNF) 📄
Este projeto realiza a análise e manipulação de Notas Fiscais Eletrônicas (NF-e) no formato XML. O sistema foi desenvolvido em Java, utilizando o framework Spring Boot, e permite a validação, consulta e transformação de dados das NF-e por meio de endpoints REST.

## Funcionalidades Implementadas 🛠️:
- <b>Validação de XML com DTD e XSD</b>: Validação do XML das NF-e utilizando DTD e XSD fornecido pela Receita Federal.
- <b>Consultas ao XML com XPath</b>: Consultas detalhadas aos dados das NF-e, incluindo:
    - Numero total de NF-e registradas;
    - Numero total de produtos registrados em todas as NF-e;
    - Soma do valor de todos os produtos;
    - Total de impostos de todas as NF-e;
    - Emitente de cada compra;
    - Destinatário de cada compra;
    - Todos os produtos;
    - Todas as transportadoras;
    - Impostos referentes a cada NF-e;
    - Fornecedores referentes a cada transportadora;
  
- <b>Transformações de XML</b>: Conversão de XML para JSON, ordenação dos produtos de cada NF-e por nome e ordenação dos produtos de todas NF-e pelo seu preço.
- <b>Endpoints REST</b>: Acesso a todas as funcionalidades por meio de endpoints REST.

## Tecnologias Utilizadas 🧰:
 - <b>Linguagem de programação</b>: Java 17 (JDK 17)
 - <b>Framework</b>: Spring Boot para construção do backend
 - <b>IDE/Editor de código</b>: IntelliJ IDEA para desenvolvimento do projeto
 - <B>Testar API</B>: Postman para testes de endpoints

## Dependências/Bibliotecas utilizadas  📂:
- ```javax.xml.bind``` para manipulação e conversão de XML
- ```com.fasterxml.jackson.databind``` para conversão de XML para JSON
- ```org.springframework.boot``` para criação dos endpoints e gestão das dependências

## Estrutura do Projeto  🗂️:
- ```/controllers```: Contém os controllers que expõem os endpoints REST.
- ```/services```: Contém a lógica de negócios e manipulação dos arquivos XML.
- ```/resources```: Contém os arquivos de configuração, DTD, XSD e as NF-e utilizadas no formato XML.

# Pré-requisitos recomendados para executar o projeto  📝:
- <a href="https://www.oracle.com/br/java/technologies/downloads/#java17-windows">Java JDK 17</a><img align="center" alt="JDK" height="20" width="30" src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/java/java-plain.svg"/>
- <a href="https://www.jetbrains.com/pt-br/idea/download/?section=windows">IntelliJ IDEA </a><img align="center" alt="Intellij" height="20" width="30" src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/intellij/intellij-original.svg" />
- <a href="https://www.postman.com/downloads/">Postman </a><img align="center" alt="Postman" height="20" width="30" src="https://cdn.jsdelivr.net/gh/devicons/devicon@latest/icons/postman/postman-original.svg" />

## Clonagem e Execução do Projeto 🚀:
### 1 - Clone este repositório com o comando:

```Bash
#Comando para clonar o projeto utilizando o Git

git clone https://github.com/Mysterioun/PortalNF

```

### 2 - Abra o projeto no IntelliJ IDEA.
Certifique-se de que o JDK 17 está selecionado como SDK do projeto em IntelliJ:

 - Vá para ```File > Project Structure > Project```, e selecione o SDK 17.

### 3 - Instale as dependências do projeto:

 - No IntelliJ, o <b>Maven</b> já deve baixar as dependências automaticamente.

 - Verifique se as dependências <b>javax</b>, <b>jackson</b> e <b>spring-boot</b> estão listadas no arquivo ```pom.xml```.

### 4 - Executando o Projeto

Para rodar o projeto, abra o arquivo principal "<b>PortalNfApplication</b>" (classe com a anotação ```@SpringBootApplication```) e execute-o.

### 5 - Testando Endpoints com Postman
- Abra o <b>Postman</b> e configure uma nova requisição.
- Insira a URL do endpoint, por exemplo:

``` bash
#Endpoint responsavel por trazer todos os produtos de todas as NFEs

http://localhost:8080/api/consultas/produtos

```
 - Clique em "Send" para enviar a requisição e visualizar a resposta.


