package com.n1nt3nd0.realtimechatservice.model;

import lombok.Data;

@Data
public class LoginRequest {
  private String username;
  private String password;
}
