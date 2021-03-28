
# customer-service

Responsável pelo domínio de cliente, efetuando a gestão dos dados do cliente e sua lista de produtos
favoritos.

## Stack:
- Docker Compose 3.7
- Java 11
- Maven  
- Spring Boot 2.3.8
- Postgres
- Flyway
- Wiremock
- Swagger


## Estrutura de pacotes:

|            Pacote                           |                 Descrição                         |
|---------------------------------------------|:-------------------------------------------------:|
|  com.contaazul.api                          |  Apis expostas pelo projeto                       |
|  com.contaazul.infrastructure.eai           |  Integração http, nunca pode ter transação        |
|  com.contaazul.infrastructure.configuration |  Configuração do projeto aplicadas por java       |
|  com.contaazul.interactions                 |  Caso de uso atendido pelo projeto                |
|  com.contaazul.entity                       |  Entidades mapeadas no projeto                    |
|  com.contaazul.repository                   |  Repositórios relacionados as entidades           |

## Como configurar o ambiente de desenvolvimento

1. Configurar as tecnologias:
   - JDK 11;
   - Maven;
   - Docker e compose;

2. Inicializar as dependências do projeto, na pasta raiz do projeto executar o comando `make start-environment:`
   >Por padrão, o acesso ao banco `customer` é feito com a senha `postgres` e usuário `postgres`_

| Serviço    |    Endpoint    |
|------------|:--------------:|
| Postgres   | localhost:5432 |
| swagger-ui | localhost:8082 |

3. Inicialização do projeto :
   - Por CLI, na pasta do projeto rodar o comando `make start-project`
   - por IDE, só executar a classe `Application`:</br>
   
   
## Integração HTTP

####  Configuração de endpoint:

| Ambiente           |                Endpoint               |  Timeouts(C/R)  |
|--------------------|:-------------------------------------:|----------------:|
| Api de produtos    | http://challenge-api.luizalabs.com    |    500/1500     |

#### Api utilizadas:

|      Api           |            URI              |      Fallback     | 
|--------------------|:---------------------------:|------------------:|
|  Busca de produto  |  /api/product/{productId}/  |      NOT_FOUND    |


## Documentações adicionais

- [Requesitos funcionais](./docs/functional_requirement.md)
- [Documentação de API](http://localhost:8082/)
- [Teste de carga ](./docs/load_test.md)

## Pendências
- [ ] Aplicar segurançã na api;
- [ ] Adicionar cache na consulta de produto;
- [ ] Criar teste de carga.


