package com.jacchm.project.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthRequestDTO implements Serializable {

  @Serial
  private static final long serialVersionUID = -6986746375915710855L;

  @NotBlank(message = "Username cannot be null.")
  private String username;

  @NotBlank(message = "Password cannot be null.")
  private String password;

}