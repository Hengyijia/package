package com.hengyijia.friend.pojo;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "tb_friend")
@IdClass(Friend.class)
public class Friend implements Serializable {

    @Id
    String userid;

    @Id
    String friendid;

    String islike;
}
