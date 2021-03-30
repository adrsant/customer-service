
####  Requesitos funcionais:

|   ID    |     Descrição                                  |              API                |                         Status                            |                                           Teste                                           |
|---------|:----------------------------------------------:|--------------------------------:|----------------------------------------------------------:|------------------------------------------------------------------------------------------:|
|  RF-01  | Criar cliente com (id,email,nome)                                               |  POST /api/customer/                                      |  Atendido  | `CustomerITest.shouldCreateCustomer()`                                        |
|  RF-02  | Atualizar cliente                                                               |  PUT  /api/customer/                                      |  Atendido  | `CustomerITest.shouldUpdateCustomer()`                                        |
|  RF-03  | Remover cliente                                                                 |  DELETE  /api/customer//{customerId}/                     |  Atendido  | `CustomerITest.shouldRemovalCustomer()`                                       |
|  RF-04  | Um cliente não pode se registrar duas vezes com o mesmo e-mail                  |                                                           |  Atendido  | `CustomerITest.shouldDoNotCreateCustomerBecauseMailDuplicated()`              |
|  RF-05  | Cada cliente só deverá ter uma única lista de produtos favoritos                |                                                           |  Atendido  |                                                                               |
|  RF-06  | Adicionar um produto na lista de favoritos  do cliente                          |   POST /api/customer/{customerId}/favorite/{productId}/   |  Atendido  | `ProductFavoriteITest.shouldAddProductFavorite()`                             |
|  RF-07  | Remover um produto na lista de favoritos  do cliente                            |   POST /api/customer/{customerId}/favorite/{productId}/   |  Atendido  | `ProductFavoriteITest.shouldRemoveProductFavorite()`                          | 
|  RF-08  | Obter a lista de favoritos  do cliente (paginada)                               |   GET /api/customer/{customerId}/favorite/                |  Atendido  | `ProductFavoriteITest.shouldListFavorites()`                                  |
|  RF-09  | Um produto não pode ser adicionado em uma lista caso ele não exista             |                                                           |  Atendido  | `ProductFavoriteITest.shouldDoNotAddProductFavoriteBecauseNotExistsProduct()` |
|  RF-10  | Um produto não pode ser adicionado duas vezes na lista de favoritos do cliente  |                                                           |  Atendido  | `ProductFavoriteITest.shouldDoNotAddProductFavoriteBecauseIsDuplicated()`     | 
|  RF-11  | Autenticar usuário para acesso a api                                            |                                                           |  Atendido  |                                                                               |
|  RF-12  | Autorizar usuário para acesso a api                                             |                                                           |  Atendido  |                                                                               |


####  Requesitos não funcionais:

| ID      |                Descrição                                                    |   Status  |
|---------|:---------------------------------------------------------------------------:|----------:|
| RNF-01  |  Atender 10 requests simultâneas                                            |  -        |
| RNF-02  |  O SLA de 100ms para o tempo de resposta, retirando o tempo da api externa  |  -        |
| RNF-03  |  A JVM deve ficar com o consumo de memória média de 512MB                   |  -        |
