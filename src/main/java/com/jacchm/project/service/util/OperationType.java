package com.jacchm.project.service.util;

import lombok.Getter;

@Getter
public enum OperationType {
  CREATED("created"),
  DELETED("deleted"),
  FETCHED("fetched"),
  UPDATED("updated");

  private final String nameLowercase;

  OperationType(String nameLowercase) {
    this.nameLowercase = nameLowercase;
  }
}
