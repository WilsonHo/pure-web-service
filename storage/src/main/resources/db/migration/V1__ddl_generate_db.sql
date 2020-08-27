CREATE TABLE "product" (
    "id" UUID NOT NULL,
    CONSTRAINT "product_pk" PRIMARY KEY ("id")
);

CREATE TABLE "name" (
    "id" UUID               NOT NULL,
    "product_id" UUID       NOT NULL,
    "lang_code" VARCHAR(2)  NOT NULL,
    "name" TEXT             NOT NULL,
    CONSTRAINT "name_pk" PRIMARY KEY ("id"),
    UNIQUE (product_id, lang_code),
    CONSTRAINT "names_product_id_fk" FOREIGN KEY ("product_id")
        REFERENCES "product" ("id")
        ON DELETE CASCADE ON UPDATE CASCADE
);
