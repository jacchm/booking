package com.jacchm.project.controller.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserCreationDTO {

  @NotBlank(message = "Email address cannot be null neither empty.")
  @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$", message = "Invalid email address.")
  private String email;
  @NotBlank(message = "Username cannot be null neither empty.")
  @Pattern(regexp = "[a-zA-Z0-9]{5,}", message = "Username should consist of letters and numbers and have minimum of 5 characters.")
  private String username;
  @NotBlank(message = "Password cannot be null neither empty.")
  private String password;
  @NotBlank(message = "Role cannot be null neither empty.")
  private String roles;
  private String name;
  private String surname;

}
