package com.n1nt3nd0.realtimechatservice;


import com.n1nt3nd0.realtimechatservice.entity.UserEntity;

public class DbBoiii {
  private DbBoiii() {}

  public static String getConvId(UserEntity user1, UserEntity user2) {
    String id1 = user1.getId().toString();
    String id2 = user2.getId().toString();

    return id1.compareTo(id2) > 0 ? id2 + "_" + id1 : id1 + "_" + id2;
  }
}
