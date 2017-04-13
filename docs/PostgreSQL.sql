CREATE SEQUENCE public."CardsInstances_id_seq"
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public."CardsInstances_id_seq"
  OWNER TO admin;

CREATE SEQUENCE public."CardsPlans_id_seq"
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public."CardsPlans_id_seq"
  OWNER TO admin;
  
CREATE SEQUENCE public."CardsReviews_id_seq"
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public."CardsReviews_id_seq"
  OWNER TO admin;
  
CREATE SEQUENCE public."Cards_id_seq"
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public."Cards_id_seq"
  OWNER TO admin;
  
CREATE SEQUENCE public."Decks_id_seq"
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE public."Decks_id_seq"
  OWNER TO admin;

  
CREATE TABLE public."Decks"
(
  id integer NOT NULL DEFAULT nextval('"Decks_id_seq"'::regclass),
  version integer NOT NULL,
  description text NOT NULL,
  notes text NOT NULL,
  CONSTRAINT "Decks_pkey" PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public."Decks"
  OWNER TO admin;


CREATE TABLE public."Cards"
(
  id integer NOT NULL DEFAULT nextval('"Cards_id_seq"'::regclass),
  version integer NOT NULL,
  "deckId" integer NOT NULL,
  "sideA" text NOT NULL,
  "sideB" text NOT NULL,
  notes text NOT NULL,
  tags text NOT NULL,
  CONSTRAINT "Cards_pkey" PRIMARY KEY (id),
  CONSTRAINT "Cards_Decks_fk" FOREIGN KEY ("deckId")
      REFERENCES public."Decks" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public."Cards"
  OWNER TO admin;

CREATE INDEX "fki_Cards_Decks_fk"
  ON public."Cards"
  USING btree
  ("deckId");

  
CREATE TABLE public."CardsInstances"
(
  id integer NOT NULL DEFAULT nextval('"CardsInstances_id_seq"'::regclass),
  "cardId" integer NOT NULL,
  "sideAToB" boolean NOT NULL,
  disabled boolean NOT NULL,
  CONSTRAINT "CardsInstances_pkey" PRIMARY KEY (id),
  CONSTRAINT "CardsInstances_Cards_fk" FOREIGN KEY ("cardId")
      REFERENCES public."Cards" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT "CardInstances_card_side_u" UNIQUE ("cardId", "sideAToB")
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public."CardsInstances"
  OWNER TO admin;

CREATE INDEX "fki_CardsInstances_Cards_fk"
  ON public."CardsInstances"
  USING btree
  ("cardId");


CREATE TABLE public."CardsReviews"
(
  id integer NOT NULL DEFAULT nextval('"CardsReviews_id_seq"'::regclass),
  "cardInstanceId" integer NOT NULL,
  value smallint NOT NULL,
  "dateTime" timestamp with time zone NULL,
  CONSTRAINT "CardsReviews_pkey" PRIMARY KEY (id),
  CONSTRAINT "CardsReviews_CardsInstances_fk" FOREIGN KEY ("cardInstanceId")
      REFERENCES public."CardsInstances" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public."CardsReviews"
  OWNER TO admin;

CREATE INDEX "fki_CardsReviews_CardsInstances_fk"
  ON public."CardsReviews"
  USING btree
  ("cardInstanceId");


CREATE TABLE public."CardsPlans"
(
  id integer NOT NULL DEFAULT nextval('"CardsPlans_id_seq"'::regclass),
  "cardInstanceId" integer NOT NULL,
  "nextDate" timestamp with time zone NULL,
  "nextDays" integer NOT NULL,
  "lastDate" timestamp with time zone NULL,
  "lastDays" integer NOT NULL,
  CONSTRAINT "CardsPlans_pkey" PRIMARY KEY (id),
  CONSTRAINT "CardsPlans_CardsInstances_fk" FOREIGN KEY ("cardInstanceId")
      REFERENCES public."CardsInstances" (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE public."CardsPlans"
  OWNER TO admin;

CREATE INDEX "fki_CardsPlans_CardsInstances_fk"
  ON public."CardsPlans"
  USING btree
  ("cardInstanceId");
  