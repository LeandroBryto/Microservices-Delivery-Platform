# Lista de Tarefas - Microservices Delivery Platform

## Estrutura Geral do Projeto
- [x] Criar diretório principal do projeto
- [x] Inicializar README.md básico
- [x] Criar diretório de documentação
- [x] Definir estrutura de diretórios para cada microsserviço
- [x] Criar esqueleto para cada serviço (Orders, Drivers, Tracking, Notifications)

## Implementação dos Microsserviços
- [ ] Implementar Service-Orders (CRUD de pedidos)
- [ ] Implementar Service-Drivers (Cadastro e consulta de motoristas)
- [ ] Implementar Service-Tracking (Consumo de eventos e atualização de status)
- [ ] Implementar Service-Notifications (Escuta de eventos e envio de notificações)

## Configuração de Comunicação
- [x] Configurar comunicação REST para cadastros e consultas
- [x] Configurar mensageria (RabbitMQ/Kafka) para eventos
- [x] Implementar padrões de comunicação assíncrona

## Bancos de Dados
- [x] Configurar MySQL para Service-Orders
- [x] Configurar MySQL para Service-Drivers
- [x] Configurar MongoDB para Service-Tracking

## Documentação e Testes
- [x] Adicionar Swagger para documentação das APIs
- [x] Implementar testes unitários com JUnit 5 e Mockito
- [ ] Documentar padrões de arquitetura utilizados

## Docker e Orquestração
- [x] Criar Dockerfile para cada serviço
- [x] Criar arquivo Docker Compose completo
- [x] Configurar rede para comunicação entre contêineres

## Validação e Entrega
- [x] Testar fluxo completo de eventos
- [x] Validar funcionamento integrado
- [x] Finalizar README com documentação completa
- [x] Entregar projeto ao usuário
