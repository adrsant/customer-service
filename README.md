
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
- Cache Caffeine
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

## Como utilizar as api's

1. Após a inicialização do projeto, devemoos obter o 
   token de acesso usando a api `/api/autheticate` passando o payload abaixo:
   
- Acceso completo:
   
```json
{
    "username": "user-api",
    "password": "password"
}
```

- Acceso restrito a apis de leitura:

```json
{
    "username": "user-api",
    "password": "password"
}
```

2. O processo de autenticação utiliza token JWT, sendo enviado por `Bearer Token` sendo assim, 
   fica obrigatório o envio em todas chamdas de apis o header `Authorization: Bearer <TOKEN-JWT>`.
   

##  Dependências:

|     Projeto        |                Endpoint               |  Timeouts(C/R)  |              Cache                     |
|--------------------|:-------------------------------------:|----------------:|---------------------------------------:|
| Api de produtos    | http://challenge-api.luizalabs.com    |    500/1500     | Cache de resposta(OK) com TTL de 5min  |


#### Api utilizadas:

|      Api           |            URI              |      Fallback     | 
|--------------------|:---------------------------:|------------------:|
|  Busca de produto  |  /api/product/{productId}/  |      NOT_FOUND    |


## Documentações adicionais
- [Requesitos funcionais](./docs/functional_requirement.md)
- [Documentação de API](http://localhost:8082/)
- [Teste de carga ](./docs/load_test.md)

## Pendências
- [ ] Criar teste de carga.

## Observações
-  O códio de segurança foi implementado com base no [exemplo](https://www.javainuse.com/spring/boot-jwt) 
- O Cache está em memória por ser tratar de um projeto com uma JVM,
  caso contrário deve ser considerado o uso de cache distribuído. 
