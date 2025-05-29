# Microservices Delivery Platform

## Autor

Este projeto foi desenvolvido por **Leandro Barreto de Brito** como um exemplo prático de arquitetura de microsserviços e observabilidade.

## Objetivo do Projeto

Este projeto simula uma plataforma de entrega de produtos, demonstrando a implementação de uma arquitetura de microsserviços moderna e robusta. O foco principal é educacional e servir como portfólio, mostrando a aplicação de tecnologias e padrões de mercado, incluindo:

*   Desenvolvimento de microsserviços independentes.
*   Comunicação síncrona (REST) e assíncrona (Event-Driven com RabbitMQ).
*   Persistência poliglota (MySQL e MongoDB).
*   Containerização com Docker e Docker Compose.
*   Implementação de uma stack completa de observabilidade (Prometheus, Loki, Grafana).

## Arquitetura

O sistema é composto pelos seguintes componentes:

1.  **Microsserviços Principais** (Rodando em contêineres Docker):
    *   `service-orders`: Gerencia pedidos (CRUD) e publica eventos de criação. Usa MySQL.
    *   `service-drivers`: Gerencia motoristas (CRUD). Usa MySQL.
    *   `service-tracking`: Rastreia o status dos pedidos consumindo eventos. Usa MongoDB.
    *   `service-notifications`: Simula o envio de notificações consumindo eventos.
2.  **Infraestrutura** (Rodando em contêineres Docker):
    *   `mysql`: Banco de dados relacional para Orders e Drivers.
    *   `mongodb`: Banco de dados NoSQL para Tracking.
    *   `rabbitmq`: Broker de mensageria para comunicação assíncrona.
3.  **Stack de Observabilidade (PLG)** (Rodando em contêineres Docker):
    *   `prometheus`: Coleta métricas dos microsserviços.
    *   `loki`: Agrega logs dos microsserviços.
    *   `promtail`: Agente que coleta logs dos contêineres e envia para o Loki.
    *   `grafana`: Interface de visualização para métricas e logs.

*(Consulte o arquivo `PROJECT_FLOW.md` e o fluxograma `project_flowchart_v3.png` para uma representação visual detalhada)*

## Tecnologias Utilizadas

*   **Linguagem:** Java 21
*   **Framework:** Spring Boot 3.2.5
*   **Módulos Spring:** Spring Web, Spring Data JPA, Spring Data MongoDB, Spring AMQP (RabbitMQ), Spring Boot Actuator
*   **Banco de Dados:** MySQL 8.0, MongoDB 6.0
*   **Mensageria:** RabbitMQ 3.11 (com Management UI)
*   **Observabilidade:**
    *   Métricas: Micrometer, Prometheus
    *   Logs: Logback, Logstash Logback Encoder, Loki, Promtail
    *   Visualização: Grafana
*   **Documentação API:** Springdoc OpenAPI (Swagger UI)
*   **Build:** Maven
*   **Containerização:** Docker, Docker Compose
*   **Outros:** Lombok

## Pré-requisitos

Para executar este projeto localmente, você precisará ter instalado:

1.  **Docker:** Versão 20.10.0 ou superior.
2.  **Docker Compose:** Versão 2.0.0 ou superior (geralmente instalado como plugin do Docker).
3.  **Git:** Para clonar o repositório (opcional, se baixar o ZIP).
4.  **(Opcional) Ferramenta de API:** Como Postman ou Insomnia para interagir com as APIs REST.

*(Consulte o `OBSERVABILITY_IMPLEMENTATION_GUIDE.md` para comandos de instalação do Docker e Docker Compose )*

## Como Executar o Projeto

1.  **Clone ou Baixe o Repositório:**
    ```bash
      git clone https://github.com/LeandroBryto/Microservices-Delivery-Platform.git
      cd Microservices-Delivery-Platform
    ```
    Ou baixe e descompacte o arquivo ZIP do projeto.

2.  **Inicie todos os serviços com Docker Compose:**
    Navegue até o diretório raiz do projeto (onde está o arquivo `docker-compose.yml`) e execute:
    ```bash
    docker-compose up -d
    ```
    Este comando fará o build das imagens dos microsserviços (se ainda não existirem) e iniciará todos os contêineres (microsserviços, bancos de dados, RabbitMQ e a stack de observabilidade) em segundo plano (`-d`).

3.  **Aguarde a Inicialização:** Pode levar alguns minutos para todos os serviços estarem totalmente iniciados e saudáveis, especialmente na primeira vez.

4.  **Verifique o Status:** Você pode verificar se todos os contêineres estão rodando com:
    ```bash
    docker-compose ps
    ```

## Acessando os Serviços e Ferramentas

Após a inicialização completa, você pode acessar as seguintes interfaces no seu navegador:

*   **Microsserviços (Swagger UI):**
    *   Service-Orders: http://localhost:8081/swagger-ui.html
    *   Service-Drivers: http://localhost:8082/swagger-ui.html
    *   Service-Tracking: http://localhost:8083/swagger-ui.html
    *   Service-Notifications: http://localhost:8084/swagger-ui.html
*   **RabbitMQ Management UI:**
    *   URL: http://localhost:15672
    *   Login: `guest` / `guest`
*   **Prometheus UI:**
    *   URL: http://localhost:9090
*   **Grafana UI:**
    *   URL: http://localhost:3000
    *   Login: `admin` / `admin` (será solicitado para trocar a senha no primeiro acesso)

## Testando o Fluxo Principal

Consulte o arquivo `TESTING_GUIDE.md` para um passo a passo detalhado de como testar a criação de um pedido e verificar a comunicação via RabbitMQ e a atualização no serviço de tracking usando o Postman ou o Swagger UI.

## Utilizando a Observabilidade (Grafana)

Consulte o `OBSERVABILITY_IMPLEMENTATION_GUIDE.md` para detalhes sobre como usar o Grafana para:

*   Explorar métricas do Prometheus.
*   Explorar logs do Loki.
*   Criar dashboards personalizados.

## Configuração de Banco de Dados Local (Opcional)

Por padrão, o projeto usa os bancos de dados MySQL e MongoDB rodando em contêineres Docker. Se você preferir usar uma instância MySQL local (instalada na sua máquina), consulte o `USAGE_GUIDE.md` para instruções sobre como ativar o perfil `localdb`.

## Estrutura do Projeto

O projeto segue uma estrutura organizada por microsserviços. Cada microsserviço (ex: `service-orders`) contém:

*   `src/main/java`: Código-fonte Java (seguindo arquitetura hexagonal básica).
*   `src/main/resources`: Arquivos de configuração (`application.properties`, `logback-spring.xml`).
*   `src/test/java`: Código de testes unitários.
*   `pom.xml`: Dependências e build do Maven.
*   `Dockerfile`: Instruções para construir a imagem Docker do serviço.

Arquivos e pastas na raiz:

*   `docker-compose.yml`: Orquestração de todos os contêineres.
*   `config/`: Arquivos de configuração para a stack de observabilidade.
*   Documentação (`.md`): Guias detalhados.

## Contribuição e Autoria

Este projeto foi desenvolvido por **Leandro Barreto de Brito**. Sinta-se à vontade para explorar o código, aprender com a implementação e utilizá-lo como referência para seus próprios estudos e projetos.

