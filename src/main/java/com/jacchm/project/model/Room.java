package com.jacchm.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Room {

  private String roomNo;
  private int noPeople;
  private String description;
  private String randomField;

}
