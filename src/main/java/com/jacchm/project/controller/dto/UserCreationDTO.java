package com.jacchm.project.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserCreationDTO {

  @NotBlank(message = "Email address cannot be null neither empty.")
  private String email;
  @NotBlank(message = "Username cannot be null neither empty.")
  private String username;
  @NotBlank(message = "Password cannot be null neither empty.")
  private String password;
  @NotBlank(message = "Role cannot be null neither empty.")
  private String roles;
  private String name;
  private String surname;

}
