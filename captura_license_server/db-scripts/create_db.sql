create database license_server;
create user license_server_user with password 'daftPunk';
grant all privileges on database license_server to license_server_user;