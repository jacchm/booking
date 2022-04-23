package com.jacchm.project.controller.dto;

import lombok.Data;

@Data
public class UserDTO {

  private String email;
  private String username;
  private String name;
  private String surname;
  private String authorities;

}
