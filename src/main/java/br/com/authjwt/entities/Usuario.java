
package br.com.authjwt.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@Table(name = "usuario") // Define o nome da tabela no banco de dados associada a esta entidade.
@Entity // Indica que esta classe é uma entidade JPA que será mapeada para uma tabela no banco de dados.
public class Usuario implements UserDetails { // Define a classe Usuario que implementa a interface UserDetails.

    @Id // Indica que este campo é a chave primária da entidade.
    @GeneratedValue(strategy = GenerationType.AUTO) // Define a estratégia de geração automática para o ID.
    @Column(nullable = false) // Define que este campo não pode ser nulo.
    private Integer id; // Campo para armazenar o ID do usuário.

    @Column(nullable = false) // Define que este campo não pode ser nulo.
    private String nomeCompleto; // Campo para armazenar o nome completo do usuário.

    @Column(unique = true, length = 100, nullable = false) // Define que este campo deve ser único, com um comprimento máximo de 100 caracteres, e não pode ser nulo.
    private String email; // Campo para armazenar o e-mail do usuário.

    @Column(nullable = false) // Define que este campo não pode ser nulo.
    private String password; // Campo para armazenar a senha do usuário.

    @CreationTimestamp // Gera automaticamente um timestamp no momento da criação do registro.
    @Column(updatable = false, name = "criado_em") // Define que este campo não pode ser atualizado após a criação e define o nome da coluna no banco de dados.
    private Date criadoEm; // Campo para armazenar a data de criação do registro.

    @UpdateTimestamp // Gera automaticamente um timestamp toda vez que o registro é atualizado.
    @Column(name = "atualizado_em") // Define o nome da coluna no banco de dados.
    private Date atualizadoEm; // Campo para armazenar a data de atualização do registro.

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(); // Retorna uma lista vazia de autoridades (nenhum papel ou permissão específico associado).
    }

    public String getPassword() {
        return password; // Retorna a senha do usuário.
    }

    @Override
    public String getUsername() {
        return email; // Retorna o e-mail como nome de usuário.
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Indica que a conta do usuário não expirou.
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Indica que a conta do usuário não está bloqueada.
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Indica que as credenciais (senha) do usuário não expiraram.
    }

    @Override
    public boolean isEnabled() {
        return true; // Indica que a conta do usuário está ativa.
    }

    public Integer getId() {
        return id; // Retorna o ID do usuário.
    }

    public Usuario setId(Integer id) {
        this.id = id; // Define o ID do usuário e retorna a instância atual da classe.
        return this;
    }

    public String getnomeCompleto() {
        return nomeCompleto; // Retorna o nome completo do usuário.
    }

    public Usuario setnomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto; // Define o nome completo do usuário e retorna a instância atual da classe.
        return this;
    }

    public String getEmail() {
        return email; // Retorna o e-mail do usuário.
    }

    public Usuario setEmail(String email) {
        this.email = email; // Define o e-mail do usuário e retorna a instância atual da classe.
        return this;
    }

    public Usuario setPassword(String password) {
        this.password = password; // Define a senha do usuário e retorna a instância atual da classe.
        return this;
    }

    public Date getcriadoEm() {
        return criadoEm; // Retorna a data de criação do registro.
    }

    public Usuario setcriadoEm(Date criadoEm) {
        this.criadoEm = criadoEm; // Define a data de criação do registro e retorna a instância atual da classe.
        return this;
    }

    public Date getatualizadoEm() {
        return atualizadoEm; // Retorna a data de atualização do registro.
    }

    public Usuario setatualizadoEm(Date atualizadoEm) {
        this.atualizadoEm = atualizadoEm; // Define a data de atualização do registro e retorna a instância atual da classe.
        return this;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nomeCompleto='" + nomeCompleto + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", criadoEm=" + criadoEm +
                ", atualizadoEm=" + atualizadoEm +
                '}';
    } // Retorna uma representação em string da instância do usuário, útil para depuração.
}