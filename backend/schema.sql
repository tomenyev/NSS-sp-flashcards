create table "user"
(
  id serial not null
    constraint user_pkey
      primary key,
  username varchar(255) not null,
  email varchar(255) not null
    constraint user_email_key
      unique,
  password varchar(255) not null,
  createdat timestamp not null,
  updatedat timestamp not null
);


create unique index user_username_uindex
  on "user" (username);

create table topic
(
  id serial not null
    constraint topic_pkey
      primary key,
  name varchar(255) not null,
  title varchar(255),
  description text,
  tags varchar(255),
  author varchar(255),
  createdat timestamp not null,
  updatedat timestamp not null
);


create table deck
(
  id serial not null
    constraint deck_pkey
      primary key,
  name varchar(255) not null,
  author varchar(255) not null,
  createdat timestamp not null,
  updatedat timestamp not null
);



create table review
(
  id serial not null
    constraint review_pkey
      primary key,
  review text,
  rate boolean not null,
  author varchar(255) not null,
  createdat timestamp not null,
  topic integer not null
    constraint review_topic_fkey
      references topic
      on update cascade on delete cascade,
  "user" integer not null
    constraint review_user_fkey
      references "user"
      on update cascade on delete cascade,
  updatedat timestamp not null
);



create table card
(
  id serial not null
    constraint card_pkey
      primary key,
  question text not null,
  answer text not null,
  createdat timestamp not null,
  updatedat timestamp not null
);


create table role
(
  id serial not null
    constraint role_pk
      primary key,
  name varchar(255) not null,
  createdat timestamp not null,
  updatedat timestamp not null
);



create table users_roles
(
  userid integer not null
    constraint users_roles_user_id_fk
      references "user",
  roleid integer not null
    constraint users_roles_role_id_fk
      references role,
  constraint users_roles_pk
    primary key (userid, roleid)
);



create table topic_users
(
  topic_id integer not null
    constraint topic_users_topic_id_fk
      references topic,
  users_id integer not null
    constraint topic_users_user_id_fk
      references "user",
  constraint topic_users_pk
    primary key (topic_id, users_id)
);



create table deck_topics
(
  topics_id integer not null
    constraint topic_decks_topic_id_fk
      references topic,
  deck_id integer not null
    constraint topic_decks_deck_id_fk
      references deck,
  constraint topic_decks_pk
    primary key (topics_id, deck_id)
);



create table card_decks
(
  card_id integer not null
    constraint card_decks_card_id_fk
      references card,
  decks_id integer not null
    constraint card_decks_deck_id_fk
      references deck,
  constraint card_decks_pk
    primary key (card_id, decks_id)
);



