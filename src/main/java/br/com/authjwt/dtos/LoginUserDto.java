package br.com.authjwt.dtos;


/*
 * Este código define um Data Transfer Object (DTO) chamado LoginUserDto, 
 * que é usado para encapsular os dados de login (e-mail e senha) ao transferir
 *  informações entre diferentes camadas da aplicação, como entre o cliente e o 
 *  servidor. A classe inclui métodos getter e setter para acessar e modificar os 
 *  valores dos campos email e password, bem como um método 
 *  toString para facilitar a visualização dos dados encapsulados.
 * 
 * 
 * */


public class LoginUserDto { // Define uma classe pública chamada LoginUserDto, usada como um Data Transfer Object (DTO) para transferir dados de login entre diferentes camadas de uma aplicação.

    private String email; // Declara um campo privado para armazenar o e-mail do usuário.
    private String password; // Declara um campo privado para armazenar a senha do usuário.

    public String getEmail() {
        return email; // Retorna o valor do campo `email`.
    }

    public LoginUserDto setEmail(String email) {
        this.email = email; // Define o valor do campo `email` e retorna a instância atual da classe.
        return this; // Permite o encadeamento de métodos (method chaining).
    }

    public String getPassword() {
        return password; // Retorna o valor do campo `password`.
    }

    public LoginUserDto setPassword(String password) {
        this.password = password; // Define o valor do campo `password` e retorna a instância atual da classe.
        return this; // Permite o encadeamento de métodos (method chaining).
    }

    @Override
    public String toString() {
        return "LoginUserDto{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
