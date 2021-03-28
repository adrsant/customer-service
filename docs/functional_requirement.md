
####  Requesitos funcionais:

| ID     |                Descrição                                                        |     API     |   Status   |  Teste   |
|--------|:-------------------------------------------------------------------------------:|------------:|----------:|---------:|
| RF-01  | Criar cliente com (id,email,nome)                                               |             |  Atendido  | [CustomerITest.shouldCreateCustomer()](https://github.com/adrsant/customer-service/blob/0547c5dcf673b962c8edfec928b9f6cc96fb0274/src/test/java/com/luizalabs/itest/CustomerITest.java#L37)  |
| RF-02  | Um cliente não pode se registrar duas vezes com o mesmo e-mail                  |             |  Atendido  | [CustomerITest.shouldDoNotCreateCustomerBecauseMailDuplicated()](https://github.com/adrsant/customer-service/blob/0547c5dcf673b962c8edfec928b9f6cc96fb0274/src/test/java/com/luizalabs/itest/CustomerITest.java#L56) |
| RF-02  | Um cliente não pode se registrar duas vezes com o mesmo e-mail                  |             |  Atendido  | [CustomerITest.shouldDoNotCreateCustomerBecauseMailDuplicated()](https://github.com/adrsant/customer-service/blob/0547c5dcf673b962c8edfec928b9f6cc96fb0274/src/test/java/com/luizalabs/itest/CustomerITest.java#L56) |
| RF-03  | Cada cliente só deverá ter uma única lista de produtos favoritos                |             |  Atendido  |    -    |
| RF-04  | Adicionar um produto na lista de favoritos  do cliente                          |             |  Atendido  | [ProductFavoriteITest.shouldAddProductFavorite()](https://github.com/adrsant/customer-service/blob/0547c5dcf673b962c8edfec928b9f6cc96fb0274/src/test/java/com/luizalabs/itest/ProductFavoriteITest.java#L47) |
| RF-05  | Remover um produto na lista de favoritos  do cliente                            |             |  Atendido  | [ProductFavoriteITest.shouldRemoveProductFavorite()](https://github.com/adrsant/customer-service/blob/e1236bd6282033b0d38cea066c82af59dc9ce49c/src/test/java/com/luizalabs/itest/ProductFavoriteITest.java#L125) |
| RF-06  | Obter a lista de favoritos  do cliente (paginada)                               |             |  Atendido  | [ProductFavoriteITest.shouldListFavorites()](https://github.com/adrsant/customer-service/blob/0547c5dcf673b962c8edfec928b9f6cc96fb0274/src/test/java/com/luizalabs/itest/ProductFavoriteITest.java#L34) |
| RF-07  | Um produto não pode ser adicionado em uma lista caso ele não exista             |             |  Atendido  | [ProductFavoriteITest.shouldDoNotAddProductFavoriteBecauseNotExistsProduct()](https://github.com/adrsant/customer-service/blob/0547c5dcf673b962c8edfec928b9f6cc96fb0274/src/test/java/com/luizalabs/itest/ProductFavoriteITest.java#L71) |
| RF-08  | Um produto não pode ser adicionado duas vezes na lista de favoritos do cliente  |             |  Atendido  | [ProductFavoriteITest.shouldDoNotAddProductFavoriteBecauseIsDuplicated()](https://github.com/adrsant/customer-service/blob/e1236bd6282033b0d38cea066c82af59dc9ce49c/src/test/java/com/luizalabs/itest/ProductFavoriteITest.java#L109) |
| RF-09  | Criar usuário para acesso a api                                                 |             |  -        |          |
| RF-10  | Autenticar usuário para acesso a api                                            |             |  -        |          |
| RF-11  | Autorizar usuário para acesso a api                                             |             |  -        |          |


####  Requesitos não funcionais:

| ID      |                Descrição                                                    |   Status  |
|---------|:---------------------------------------------------------------------------:|----------:|
| RNF-01  |  Atender 10 requests simultâneas                                            |  -        |
| RNF-02  |  O SLA de 100ms para o tempo de resposta, retirando o tempo da api externa  |  -        |
| RNF-03  |  A JVM deve ficar com o consumo de memória média de 512MB                   |  -        |
