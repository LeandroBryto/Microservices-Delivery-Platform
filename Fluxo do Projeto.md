# Fluxo do Projeto: Microservices Delivery Platform

Este documento descreve o fluxo principal de operações e a interação entre os componentes da plataforma de entrega baseada em microsserviços.

## Componentes Principais

1.  **Cliente/Usuário**: Interage com o sistema através de APIs REST.
2.  **Service-Orders**: Gerencia pedidos (CRUD). Utiliza MySQL.
3.  **Service-Drivers**: Gerencia motoristas (CRUD). Utiliza MySQL.
4.  **Service-Tracking**: Rastreia o status dos pedidos. Utiliza MongoDB e consome eventos.
5.  **Service-Notifications**: Envia notificações (simuladas via log). Consome eventos.
6.  **MySQL**: Banco de dados relacional para Orders e Drivers.
7.  **MongoDB**: Banco de dados NoSQL para Tracking.
8.  **RabbitMQ**: Broker de mensageria para comunicação assíncrona baseada em eventos.
9.  **Stack de Observabilidade (PLG)**:
    *   **Prometheus**: Coleta métricas dos serviços.
    *   **Loki**: Agrega logs dos serviços.
    *   **Promtail**: Coleta e envia logs para o Loki.
    *   **Grafana**: Visualiza métricas (Prometheus) e logs (Loki).

## Fluxo Principal: Criação de um Pedido

1.  **Requisição do Cliente**: O cliente (ou um sistema externo) envia uma requisição `POST` para a API REST do `service-orders` (ex: `POST /api/orders`) com os detalhes do novo pedido.

2.  **Processamento no Service-Orders**:
    *   O `OrderController` recebe a requisição.
    *   Valida os dados do pedido.
    *   Salva o novo pedido no banco de dados **MySQL** através do `OrderRepository`.
    *   Após salvar com sucesso, o `OrderEventPublisher` publica uma mensagem `order.created` na exchange do **RabbitMQ**. A mensagem contém informações básicas sobre o pedido criado (como o ID).
    *   Retorna uma resposta `201 Created` para o cliente com os detalhes do pedido salvo.

3.  **Consumo de Evento pelo Service-Tracking**:
    *   O `service-tracking` está configurado para escutar a fila vinculada à exchange onde o evento `order.created` foi publicado no **RabbitMQ**.
    *   O `OrderEventConsumer` no `service-tracking` recebe a mensagem.
    *   Processa a mensagem, extrai o ID do pedido.
    *   Cria um novo registro de evento de rastreamento (ex: status "Pedido Recebido") no banco de dados **MongoDB** através do `TrackingEventRepository`.

4.  **(Opcional) Consumo de Evento pelo Service-Notifications**:
    *   Se o `service-notifications` estivesse configurado para escutar o evento `order.created` (ou eventos futuros de status do `service-tracking`), ele receberia a mensagem via **RabbitMQ**.
    *   Processaria a mensagem e simularia o envio de uma notificação (neste projeto, ele apenas registraria um log).

## Outras Interações

*   **Consulta de Motoristas**: O cliente pode fazer requisições `GET` para o `service-drivers` (ex: `GET /api/drivers`) para listar ou buscar motoristas. Esta é uma comunicação síncrona via API REST.
*   **Atualização de Status (Tracking)**: Poderia haver um endpoint no `service-tracking` para atualizar o status de um pedido (ex: `PUT /api/tracking/{orderId}/status`). Ao atualizar, o `service-tracking` poderia publicar um novo evento (ex: `tracking.updated`) no RabbitMQ, que seria consumido pelo `service-notifications` para alertar o cliente.

## Monitoramento e Observabilidade

*   **Coleta de Métricas**: O **Prometheus** coleta métricas continuamente de todos os microsserviços (Orders, Drivers, Tracking, Notifications) através dos endpoints `/actuator/prometheus`.
*   **Coleta de Logs**: O **Promtail** lê os logs (em formato JSON) gerados por todos os contêineres dos microsserviços e os envia para o **Loki**.
*   **Visualização**: O **Grafana** se conecta ao Prometheus e ao Loki, permitindo visualizar dashboards com métricas de desempenho (CPU, memória, requisições HTTP) e pesquisar/visualizar os logs de todos os serviços de forma centralizada.

Este fluxo demonstra a separação de responsabilidades, a comunicação síncrona (REST) e assíncrona (eventos via RabbitMQ), e a importância da observabilidade em uma arquitetura de microsserviços.
