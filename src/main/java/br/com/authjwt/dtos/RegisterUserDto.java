package br.com.authjwt.dtos;

public class RegisterUserDto {
    private String email;
    private String password;
    private String nomeCompleto;

    public String getEmail() {
        return email;
    }

    public RegisterUserDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public RegisterUserDto setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getnomeCompleto() {
        return nomeCompleto;
    }

    public RegisterUserDto setnomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
        return this;
    }

    @Override
    public String toString() {
        return "RegisterUserDto{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", nomeCompleto='" + nomeCompleto + '\'' +
                '}';
    }
}
