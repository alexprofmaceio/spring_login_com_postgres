# spring_login_com_postgres
Neste exemplo temos uma forma de gerar uma tela de login usando o Spring e o banco de dados Postgres

Os pacotes básicos usados neste projeto:
- Spring Boot DevTools
- Spring Data JPA
- Spring Security
- Spring Web
- Spring Mail
- Spring Validation
- Thymeleaf
- PostgreSQL Driver

# Descrição do Projeto
O objetivo deste projeto é criar um modelo de login com recursos de "esqueci a senha", "novo usuário" e perfil de grupos de usuários usando o Spring boot e o banco de dados PostgreSQL para autenticação e validação do usuário.

# Principais Módulos
- UsuarioController
- LoginUsuarioService
- NovoUsuario
- SecurityConfiguration
- UsuarioNotFoundException
- UsuarioService

# Templates
- index
- login
- registro
- esqueceuSenha

# Recursos Externos
- Mailtrap -> usado para envio de e-mail de teste para vários módulos do sistema.
- 
