delete
from customer;
delete
from favorite_product;

INSERT INTO customer (id, name, mail)
VALUES ('6fc1307c-5100-4b6b-b3a0-d4c6cfb35c11', 'Test', 'test@mail.com');
INSERT INTO customer (id, name, mail)
VALUES ('1bf0f365-fbdd-4e21-9786-da459d78dd1f', 'Test 2', 'test_2@mail.com');

INSERT INTO favorite_product (product_id, customer_id)
VALUES ('1bf0f365-fbdd-4e21-9786-da459d78dd1f', '1bf0f365-fbdd-4e21-9786-da459d78dd1f');

INSERT INTO favorite_product (product_id, customer_id)
VALUES ('1bf0f365-fbdd-4e21-9786-da459d78dd1f', '6fc1307c-5100-4b6b-b3a0-d4c6cfb35c11');
