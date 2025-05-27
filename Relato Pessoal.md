# Relato Pessoal

Esse projeto nasceu de um grande desafio pessoal e acadêmico. A ideia surgiu observando como funcionam plataformas de delivery, como **iFood**, e também sistemas de logística para motoboys. Foi assim que decidi desenvolver minha própria plataforma de **microsserviços**, de forma simples, aplicando na prática os conhecimentos que venho adquirindo na faculdade e também a experiência profissional que tive na **LK Engenharia**.

Tive o apoio fundamental dos professores **Aldemir** e **David**, do curso de **Engenharia de Software do IESB**, que me orientaram bastante durante todo esse processo. Foi um desenvolvimento que exigiu muita resiliência — foram muitas noites acordado, enfrentando erros, ajustes e desafios constantes.

No meio do desenvolvimento, enfrentei diversos problemas técnicos:

- Conflitos de dependências no **Docker**.
- Erros na configuração das filas do **RabbitMQ**.
- Inconsistências no consumo de eventos entre os microsserviços.
- Problemas com persistência de dados no **MongoDB**, que em alguns momentos não salvava os registros corretamente por pequenos erros que levaram horas para serem identificados.

Também tive bastante dificuldade na configuração do stack de observabilidade, que inclui:

- **Prometheus**
- **Grafana**
- **Loki**
- **Promtail**

Foram muitos erros no YAML, falhas de autenticação entre serviços, problemas de rede e portas, além de desafios para entender como funciona, na prática, a coleta de logs, métricas e como transformar tudo isso em dashboards visuais no Grafana.

Durante o desenvolvimento, cheguei a estudar bastante sobre mensageria e fiquei na dúvida se usaria **RabbitMQ** ou **Kafka**. Optei pelo **RabbitMQ** por ser uma solução mais simples e fácil de implementar para esse tipo de projeto, mas entendi bem as diferenças. Enquanto o **Kafka** é mais robusto, escalável e indicado para aplicações que demandam altíssima performance e grandes volumes de dados em tempo real, o **RabbitMQ** funciona muito bem para filas de mensagens tradicionais, com menor complexidade na configuração e na gestão. Para os objetivos desse projeto, o **RabbitMQ** atendeu perfeitamente.

Cada microsserviço foi desenvolvido para ser independente, se comunicando de forma:

- **Assíncrona** via **RabbitMQ**.
- **Síncrona** através de **APIs REST**.

Apliquei diversos conceitos que aprendi na faculdade, como:

- Arquitetura orientada a microsserviços.
- Comunicação baseada em eventos.
- Mensageria.
- Versionamento de código.
- Conteinerização.
- Boas práticas de desenvolvimento.

Todo o ambiente foi montado utilizando:

- **Docker** e **Docker Compose**.
- Bancos de dados:
    - **Relacional:** MySQL
    - **Não relacional:** MongoDB
- Stack de observabilidade:
    - **Prometheus**
    - **Grafana**
    - **Loki**
    - **Promtail**
- Ferramentas de apoio:
    - **RabbitMQ**
    - **ChatGPT** — utilizado como apoio na pesquisa, revisão de código, geração de soluções, otimização de textos técnicos e auxílio na resolução de problemas durante o desenvolvimento.

Depois de muitos erros, acertos, ajustes e recomeços, consegui finalizar o projeto funcionando de ponta a ponta. Foi uma experiência extremamente enriquecedora, onde pude aplicar na prática os conhecimentos adquiridos nas aulas, nas pesquisas e na minha vivência profissional.

Sem dúvidas, é um dos projetos mais desafiadores e, ao mesmo tempo, mais gratificantes que já desenvolvi até hoje.
