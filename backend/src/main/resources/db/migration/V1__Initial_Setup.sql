CREATE SEQUENCE Customer_id_sequence;

CREATE TABLE customer (
  id INTEGER NOT NULL PRIMARY KEY DEFAULT nextval('Customer_id_sequence'),
  name TEXT NOT NULL,
  email TEXT NOT NULL,
  age INT NOT NULL
);
