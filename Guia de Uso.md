# Guia de Uso - Microservices Delivery Platform

Este guia detalha como configurar e executar a Plataforma de Entrega baseada em Microsserviços no seu ambiente local, cobrindo duas opções principais para o banco de dados MySQL: usando o MySQL dentro do Docker (recomendado e padrão) ou conectando a uma instância MySQL local.

## Pré-requisitos Gerais

Antes de começar, certifique-se de ter as seguintes ferramentas instaladas:

*   **Git**: Para clonar o repositório.
*   **Docker e Docker Compose**: Essencial para a execução padrão e gerenciamento dos contêineres.
*   **Java 21 JDK**: Necessário se você planeja compilar ou executar os serviços fora do Docker.
*   **Maven**: Necessário se você planeja compilar os serviços fora do Docker.
*   **(Opcional) Cliente MySQL**: Como MySQL Workbench, DBeaver ou `mysql` CLI, útil para inspecionar o banco de dados local.
*   **(Opcional) Instância MySQL Local**: Necessária apenas se você escolher a opção de usar um banco de dados local em vez do contêiner Docker.

## Opção 1: Executando Tudo com Docker (Recomendado)

Esta é a maneira mais simples de rodar o projeto, pois o Docker Compose gerencia todos os serviços e dependências (MySQL, MongoDB, RabbitMQ).

**Passos:**

1.  **Clone o Repositório:**
    ```bash
    git clone <URL_DO_REPOSITORIO> # Substitua pela URL correta
    cd microservices-delivery-platform
    ```

2.  **Inicie os Contêineres:**
    Navegue até a raiz do projeto (onde está o `docker-compose.yml`) e execute:
    ```bash
    docker-compose up -d
    ```
    *   Este comando fará o build das imagens dos microsserviços (se ainda não existirem) e iniciará todos os contêineres definidos no `docker-compose.yml` (serviços, MySQL, MongoDB, RabbitMQ, e futuramente Eureka, Zipkin, etc.) em segundo plano (`-d`).
    *   Aguarde alguns instantes para que todos os serviços iniciem completamente e se registrem (quando o Eureka estiver ativo).

3.  **Verifique o Status:**
    ```bash
    docker-compose ps
    ```
    *   Todos os contêineres devem estar com o status `Up` ou `Running`.

4.  **Acesse as Interfaces:**
    *   **Swagger UI (APIs):**
        *   Pedidos: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)
        *   Motoristas: [http://localhost:8082/swagger-ui.html](http://localhost:8082/swagger-ui.html)
        *   Tracking: [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html)
        *   Notificações: [http://localhost:8084/swagger-ui.html](http://localhost:8084/swagger-ui.html)
    *   **RabbitMQ Management:** [http://localhost:15672](http://localhost:15672) (Login: `guest` / `guest`)
    *   *(Outras interfaces como Eureka, Zipkin, Grafana estarão disponíveis aqui quando implementadas)*

5.  **Parando os Serviços:**
    ```bash
    docker-compose down
    ```
    *   Este comando para e remove os contêineres, redes e volumes criados pelo `up` (a menos que os volumes sejam externos).

## Opção 2: Executando com MySQL Local

Esta opção permite que os serviços `service-orders` e `service-drivers` (que usam MySQL) se conectem a uma instância MySQL rodando diretamente na sua máquina, fora do Docker. Os outros serviços (Tracking, Notifications, RabbitMQ, MongoDB) ainda podem rodar via Docker Compose.

**Pré-requisitos Adicionais:**

*   Ter uma instância MySQL (versão 8+) instalada e rodando localmente.
*   Saber o endereço, porta, usuário e senha do seu MySQL local.

**Passos:**

1.  **Prepare seu MySQL Local:**
    *   Conecte-se à sua instância MySQL local.
    *   Crie os bancos de dados necessários (se ainda não existirem):
        ```sql
        CREATE DATABASE IF NOT EXISTS orders_db;
        CREATE DATABASE IF NOT EXISTS drivers_db;
        ```
    *   Certifique-se de que o usuário que os serviços usarão tem permissão para acessar e modificar esses bancos.

2.  **Configure os Serviços para Usar o MySQL Local:**
    Para cada serviço que usa MySQL (`service-orders` e `service-drivers`), você precisará informar a URL de conexão local. Há duas abordagens principais:

    *   **Abordagem A: Modificando `application.properties` (Simples, mas requer cuidado com Git):**
        *   Edite o arquivo `src/main/resources/application.properties` em `service-orders` e `service-drivers`.
        *   Altere as propriedades `spring.datasource.url`, `spring.datasource.username`, e `spring.datasource.password` para apontar para seu MySQL local. Exemplo:
            ```properties
            # Exemplo para service-orders/src/main/resources/application.properties
            # ... outras propriedades ...
            spring.datasource.url=jdbc:mysql://localhost:3306/orders_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true # Ajuste localhost, porta e parâmetros conforme necessário
            spring.datasource.username=seu_usuario_local
            spring.datasource.password=sua_senha_local
            # ... outras propriedades ...
            ```
        *   **Atenção:** Tenha cuidado para não commitar suas credenciais locais no Git.

    *   **Abordagem B: Usando Perfis Spring (Recomendado):**
        *   Crie um novo arquivo de configuração para o perfil local em cada serviço: `src/main/resources/application-localdb.properties`.
        *   Dentro de `application-localdb.properties`, defina as propriedades do datasource local:
            ```properties
            # Exemplo para service-orders/src/main/resources/application-localdb.properties
            spring.datasource.url=jdbc:mysql://localhost:3306/orders_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
            spring.datasource.username=seu_usuario_local
            spring.datasource.password=sua_senha_local
            ```
        *   Repita o processo para `service-drivers`.

3.  **Execute os Serviços:**
    Agora você precisa iniciar os serviços ativando o perfil `localdb` (se usou a Abordagem B).

    *   **Executando via IDE ou Maven:**
        *   Ao rodar a aplicação (ex: `mvn spring-boot:run` ou pela sua IDE), ative o perfil Spring: `-Dspring.profiles.active=localdb`.
        *   Você precisará iniciar cada serviço (`orders`, `drivers`, `tracking`, `notifications`, `discovery`) individualmente.
        *   Você também precisará iniciar as dependências que não estão locais (MongoDB, RabbitMQ) manualmente ou usando uma versão modificada do `docker-compose.yml` que não inclua MySQL, Orders e Drivers.

    *   **Executando via Docker Compose (Modificado):**
        *   Você pode modificar o `docker-compose.yml` para:
            *   Remover o serviço `mysql`.
            *   Adicionar uma variável de ambiente `SPRING_PROFILES_ACTIVE=localdb` aos serviços `service-orders` e `service-drivers`.
            *   **Importante:** Para que os contêineres Docker (`service-orders`, `service-drivers`) acessem o `localhost` da sua máquina host, a URL do MySQL no `application-localdb.properties` precisa usar um endereço especial:
                *   **Docker Desktop (Windows/Mac):** `jdbc:mysql://host.docker.internal:3306/orders_db...`
                *   **Linux:** Você pode precisar configurar a rede do Docker ou usar o IP da interface `docker0`. Uma alternativa é rodar os serviços `orders` e `drivers` fora do Docker (via IDE/Maven) e apenas o restante (Mongo, Rabbit, Tracking, Notifications) via Docker Compose.
        *   Execute `docker-compose up -d` com o arquivo modificado.

4.  **Verificação e Acesso:**
    *   Verifique os logs dos serviços `service-orders` e `service-drivers` para confirmar que eles se conectaram com sucesso ao seu MySQL local.
    *   Acesse as interfaces Swagger como na Opção 1.

## Considerações

*   A **Opção 1 (Tudo com Docker)** é geralmente mais fácil para desenvolvimento e garante um ambiente consistente.
*   A **Opção 2 (MySQL Local)** pode ser útil se você já possui um MySQL local configurado ou prefere gerenciar o banco de dados separadamente.
*   Lembre-se que a configuração de rede entre contêineres Docker e o host local pode exigir ajustes específicos dependendo do seu sistema operacional (Windows, Mac, Linux).

Se encontrar problemas, verifique os logs dos serviços (`docker-compose logs <nome_do_servico>`) para mensagens de erro detalhadas.
