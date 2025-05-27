# Guia de Implementação de Observabilidade

Este documento descreve como implementar e utilizar a stack completa de observabilidade (Prometheus + Loki + Grafana) no projeto `microservices-delivery-platform`.

## Pré-requisitos

Para executar a stack de observabilidade, você precisa ter instalado:

1. **Docker** (versão 20.10.0 ou superior)


## Arquitetura da Stack de Observabilidade (PLG)

A stack **P**rometheus + **L**oki + **G**rafana implementada neste projeto funciona da seguinte forma:

1. **Microsserviços (Orders, Drivers, Tracking, Notifications)**:
   - Expõem métricas no formato Prometheus via endpoint `/actuator/prometheus`
   - Geram logs em formato JSON para facilitar a análise

2. **Prometheus**:
   - Coleta métricas periodicamente dos endpoints dos microsserviços
   - Armazena as métricas em seu banco de dados de séries temporais

3. **Loki**:
   - Sistema de agregação de logs leve e eficiente
   - Indexa metadados dos logs para busca rápida

4. **Promtail**:
   - Agente que coleta logs dos contêineres Docker
   - Envia os logs para o Loki com labels apropriados

5. **Grafana**:
   - Interface de visualização para métricas e logs
   - Configurado com dashboards para monitoramento dos microsserviços

## O Que Foi Implementado

### 1. Configuração dos Microsserviços

Todos os microsserviços foram configurados com:

- **Dependências para métricas**:
  ```xml
  <dependency>
      <groupId>io.micrometer</groupId>
      <artifactId>micrometer-registry-prometheus</artifactId>
  </dependency>
  ```

- **Dependências para logs estruturados**:
  ```xml
  <dependency>
      <groupId>net.logstash.logback</groupId>
      <artifactId>logstash-logback-encoder</artifactId>
      <version>7.4</version>
  </dependency>
  ```

- **Configuração de exposição de métricas** em `application.properties`:
  ```properties
  management.endpoints.web.exposure.include=health,info,metrics,prometheus
  management.endpoint.health.show-details=when_authorized
  ```

- **Configuração de logs JSON** em `logback-spring.xml`:
  ```xml
  <appender name="STDOUT_JSON" class="ch.qos.logback.core.ConsoleAppender">
      <encoder class="net.logstash.logback.encoder.LogstashEncoder"/>
  </appender>
  ```

### 2. Configuração da Stack de Observabilidade

O arquivo `docker-compose.yml` foi atualizado para incluir:

- **Prometheus**: Coleta e armazena métricas (porta 9090)
- **Loki**: Sistema de agregação de logs (porta 3100)
- **Promtail**: Agente de coleta de logs
- **Grafana**: Visualização de métricas e logs (porta 3000)

Arquivos de configuração criados:

- `config/observability/prometheus.yml`: Configuração do Prometheus
- `config/observability/loki-config.yml`: Configuração do Loki
- `config/observability/promtail-config.yml`: Configuração do Promtail
- `config/observability/grafana/provisioning/datasources/datasources.yml`: Configuração automática das fontes de dados no Grafana

## Como Executar a Stack de Observabilidade

1. **Inicie todos os serviços**:
   ```bash
   cd microservices-delivery-platform
   docker-compose up -d
   ```

2. **Verifique se todos os contêineres estão rodando**:
   ```bash
   docker-compose ps
   ```

3. **Acesse as interfaces de monitoramento**:
   - **Grafana**: http://localhost:3000 (usuário: admin, senha: admin)
   - **Prometheus**: http://localhost:9090
   - **RabbitMQ Management**: http://localhost:15672 (usuário: guest, senha: guest)

## Utilizando o Grafana

Após acessar o Grafana (http://localhost:3000):

1. **Login**: Use as credenciais padrão (admin/admin) e defina uma nova senha quando solicitado.

2. **Fontes de Dados**: As fontes de dados Prometheus e Loki já estão configuradas automaticamente.

3. **Explorando Métricas**:
   - Clique em "Explore" no menu lateral
   - Selecione "Prometheus" como fonte de dados
   - Experimente consultas como:
     - `jvm_memory_used_bytes`: Uso de memória JVM
     - `http_server_requests_seconds_count`: Contagem de requisições HTTP
     - `process_cpu_usage`: Uso de CPU

4. **Explorando Logs**:
   - Clique em "Explore" no menu lateral
   - Selecione "Loki" como fonte de dados
   - Experimente consultas como:
     - `{service="service-orders"}`: Logs do serviço de pedidos
     - `{service="service-tracking"} |= "error"`: Logs de erro do serviço de rastreamento

5. **Criando Dashboards**:
   - Clique em "+ Create" > "Dashboard"
   - Adicione painéis com métricas e logs relevantes
   - Salve o dashboard para uso futuro

## Métricas e Logs Importantes

### Métricas Principais

- **JVM**:
  - `jvm_memory_used_bytes`: Uso de memória
  - `jvm_threads_states_threads`: Estado das threads

- **HTTP**:
  - `http_server_requests_seconds_count`: Volume de requisições
  - `http_server_requests_seconds_sum`: Tempo total de resposta
  - `http_server_requests_seconds_max`: Tempo máximo de resposta

- **Sistema**:
  - `process_cpu_usage`: Uso de CPU
  - `system_load_average_1m`: Carga média do sistema

### Padrões de Logs

Os logs são estruturados em JSON e incluem:

- `timestamp`: Momento do log
- `level`: Nível (INFO, WARN, ERROR, etc.)
- `logger`: Nome do logger
- `thread`: Thread que gerou o log
- `message`: Mensagem do log
- `service`: Nome do serviço

## Solução de Problemas

### Prometheus não mostra métricas

1. Verifique se os endpoints `/actuator/prometheus` estão acessíveis:
   ```bash
   curl http://localhost:8081/actuator/prometheus
   ```

2. Verifique os targets no Prometheus (http://localhost:9090/targets)

### Logs não aparecem no Grafana/Loki

1. Verifique se o Promtail está coletando logs:
   ```bash
   docker-compose logs promtail
   ```

2. Verifique se os contêineres têm a label `logging=promtail`

3. Teste a conexão do Promtail com o Loki

## Próximos Passos

Para expandir a observabilidade:

1. **Adicionar Dashboards Personalizados**: Crie dashboards específicos para cada microsserviço

2. **Configurar Alertas**: Use o Grafana para configurar alertas baseados em métricas

3. **Implementar Tracing Distribuído**: Adicione Zipkin ou Jaeger para rastreamento de requisições entre serviços

4. **Monitoramento de Negócios**: Adicione métricas específicas de negócios (pedidos por hora, tempo médio de entrega, etc.)
