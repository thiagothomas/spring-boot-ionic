package com.thiagothomas.cursomc.dto;

import com.thiagothomas.cursomc.services.validation.ClienteInsert;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@NoArgsConstructor
@Data
@ClienteInsert
public class ClienteNewDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "Preenchimento Obrigatório")
    @Length(min =5, max=120, message="O tamanho deve ser entre 5 e 120 caracteres")
    private String nome;

    @NotEmpty(message = "Preenchimento Obrigatório")
    @Email(message = "Email Inválido")
    private String email;

    @NotEmpty(message = "Preenchimento Obrigatório")
    private String cpfOuCnpj;

    private Integer tipo;

    @NotEmpty(message = "Preenchimento Obrigatório")
    private String logradouro;

    @NotEmpty(message = "Preenchimento Obrigatório")
    private String numero;
    private String complemento;
    private String bairro;

    @NotEmpty(message = "Preenchimento Obrigatório")
    private String cep;

    @NotEmpty(message = "Preenchimento Obrigatório")
    private String telefone1;

    private String telefone2;
    private String telefone3;

    private Integer cidadeId;

}
