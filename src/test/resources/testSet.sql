CREATE TABLE "person" (
  "id"             SERIAL      NOT NULL,
  "name"           VARCHAR(30) NOT NULL,
  "age"            INTEGER     NOT NULL,
  "isStudent"      BOOLEAN     NOT NULL,
  "score"          FLOAT       NULL,
  "submissionDate" TIMESTAMP   NULL,
  CONSTRAINT pk_person PRIMARY KEY ("id")
);

INSERT INTO "person" ("id", "name", "age", "isStudent", "score", "submissionDate") VALUES
  (0, 'Harry Potter', 11, TRUE, 6.5, '2001-08-01' :: TIMESTAMP),
  (1, 'Hermione Granger', 10, TRUE, 9.5, '2001-08-02' :: TIMESTAMP),
  (2, 'Ron Weasley', 12, TRUE, 5.6, '2001-08-03' :: TIMESTAMP),
  (3, 'Severus Snape', 28, FALSE, NULL, NULL);