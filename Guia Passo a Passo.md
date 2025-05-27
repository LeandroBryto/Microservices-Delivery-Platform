# Guia Passo a Passo: Testando a Plataforma de Entrega

Este guia detalhado mostrará como iniciar, usar e testar o fluxo principal da aplicação `microservices-delivery-platform` na versão simplificada.

**Objetivo do Teste:** Criar um pedido, verificar se ele é processado pelo serviço de tracking através do RabbitMQ e consultar os dados.

**Ferramentas que Usaremos:**

*   **Docker Compose:** Para iniciar/parar todos os serviços e a infraestrutura.
*   **Navegador Web:** Para acessar as interfaces Swagger UI e RabbitMQ Management.
*   **Swagger UI:** Interface gráfica para interagir com as APIs REST dos microsserviços.
*   **RabbitMQ Management:** Interface para visualizar filas e mensagens no RabbitMQ.

## Passo 1: Iniciar a Aplicação

1.  **Abra um Terminal ou Prompt de Comando:** Navegue até a pasta raiz do projeto (onde está o arquivo `docker-compose.yml`).
2.  **Execute o Docker Compose:**
    ```bash
    docker-compose up -d
    ```
    *   Aguarde um pouco (1-2 minutos) para que todos os contêineres iniciem completamente.
3.  **Verifique se Tudo Está Rodando:**
    ```bash
    docker-compose ps
    ```
    *   Você deve ver os serviços `service-orders`, `service-drivers`, `service-tracking`, `service-notifications`, `mysql`, `mongodb`, e `rabbitmq` com status `Up`.

## Passo 2: Criar um Novo Pedido (Usando Swagger)

1.  **Acesse a API do Service-Orders:** Abra seu navegador e vá para [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html).
2.  **Encontre o Endpoint de Criação:** Na interface Swagger, procure pela seção `Order Controller` e expanda o endpoint `POST /api/orders` (Criar novo pedido).
3.  **Clique em "Try it out":** Isso habilitará a edição do corpo da requisição (Request body).
4.  **Preencha os Dados do Pedido:** Modifique o JSON de exemplo no campo "Request body". Você pode usar algo simples como:
    ```json
    {
      "customerName": "Cliente Teste",
      "deliveryAddress": "Rua Exemplo, 123",
      "items": [
        {
          "productName": "Produto A",
          "quantity": 2
        },
        {
          "productName": "Produto B",
          "quantity": 1
        }
      ]
    }
    ```
    *   **Importante:** Não inclua os campos `id`, `createdAt`, `updatedAt` ou `status` no JSON, pois eles são definidos automaticamente pelo serviço.
5.  **Execute a Requisição:** Clique no botão azul "Execute".
6.  **Verifique a Resposta:** Abaixo do botão "Execute", você verá a resposta do servidor (Server response).
    *   Se tudo deu certo, o código da resposta será `201` (Created).
    *   O corpo da resposta (Response body) mostrará o pedido criado, agora com um `id`, `status: CREATED` e `createdAt`.
    *   **Anote o `id` do pedido criado.** Você precisará dele nos próximos passos (Ex: `1`).

## Passo 3: Verificar a Mensagem no RabbitMQ (Opcional, mas instrutivo)

Quando um pedido é criado, o `service-orders` deve publicar uma mensagem no RabbitMQ.

1.  **Acesse o RabbitMQ Management:** Abra seu navegador e vá para [http://localhost:15672](http://localhost:15672).
2.  **Faça Login:** Use o usuário `guest` e a senha `guest`.
3.  **Vá para a Aba "Queues":** Clique na aba "Queues" (Filas) no topo.
4.  **Encontre a Fila de Pedidos:** Procure por uma fila relacionada a pedidos (o nome exato depende da configuração, mas pode ser algo como `orders.tracking.queue` ou similar).
5.  **Verifique as Mensagens:** Você deve ver que há mensagens na fila (coluna "Ready" ou "Total"). Se você clicar no nome da fila, poderá encontrar uma seção "Get messages" para inspecionar o conteúdo da mensagem (geralmente em formato JSON, contendo os dados do pedido criado).
    *   **Observação:** O `service-tracking` consome essas mensagens rapidamente. Se você não vir a mensagem, significa que o `service-tracking` já a processou (o que é bom!). Você pode tentar criar outro pedido e verificar o RabbitMQ imediatamente.

## Passo 4: Verificar o Rastreamento (Tracking)

O `service-tracking` deve ter consumido a mensagem do RabbitMQ e criado um registro de evento no MongoDB.

1.  **Acesse a API do Service-Tracking:** Abra seu navegador e vá para [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html).
2.  **Encontre o Endpoint de Consulta por Pedido:** Na interface Swagger, procure pela seção `Tracking Controller` e expanda o endpoint `GET /api/tracking/order/{orderId}` (Buscar eventos por pedido).
3.  **Clique em "Try it out".**
4.  **Informe o ID do Pedido:** No campo `orderId`, digite o `id` do pedido que você anotou no Passo 2 (Ex: `1`).
5.  **Execute a Requisição:** Clique em "Execute".
6.  **Verifique a Resposta:**
    *   O código da resposta deve ser `200` (OK).
    *   O corpo da resposta (Response body) deve mostrar uma lista contendo pelo menos um evento de rastreamento para o seu pedido, provavelmente com um status inicial como `ORDER_RECEIVED` ou similar, e o `orderId` correspondente.

## Passo 5: Consultar Outros Endpoints (Exploração)

Agora que o fluxo principal funcionou, você pode explorar outros endpoints nas interfaces Swagger:

*   **Service-Orders (`localhost:8081`):**
    *   `GET /api/orders`: Listar todos os pedidos.
    *   `GET /api/orders/{id}`: Buscar o pedido específico que você criou.
*   **Service-Drivers (`localhost:8082`):**
    *   `POST /api/drivers`: Cadastrar um novo motorista (similar ao Passo 2).
    *   `GET /api/drivers`: Listar motoristas.
*   **Service-Tracking (`localhost:8083`):**
    *   `GET /api/tracking`: Listar todos os eventos de rastreamento.
*   **Service-Notifications (`localhost:8084`):**
    *   Este serviço geralmente não tem muitos endpoints de consulta, pois ele reage a eventos. Verifique os logs do contêiner `service-notifications` para ver se ele registrou o recebimento de algum evento:
        ```bash
        docker-compose logs service-notifications
        ```

## Passo 6: Parar a Aplicação

Quando terminar os testes:

1.  **Volte ao seu Terminal ou Prompt de Comando.**
2.  **Execute:**
    ```bash
    docker-compose down
    ```

---

Seguindo estes passos, você terá testado o fluxo principal da aplicação: criação de pedido, publicação de evento no RabbitMQ, consumo do evento pelo serviço de tracking e persistência do rastreamento. Use as interfaces Swagger para explorar todas as funcionalidades disponíveis em cada microsserviço!
