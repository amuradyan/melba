create database melba;
use melba;
create table if not exists users(
	uid varchar(36) unique not null, 
    email varchar(50) unique not null, 
    pwd varchar(32) not null, 
    createdAt long not null,
    updatedAt long not null,
    primary key (uid)
);
create table if not exists notes(
	nid varchar(36) unique not null,
    uid varchar(36) not null,
    title varchar(50) not null,
    note varchar(1000) not null,
    createdAt long not null,
    updatedAt long not null,
    primary key (nid),
    foreign key (uid) references users(uid)
);