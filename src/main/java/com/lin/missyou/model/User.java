package com.lin.missyou.model;

import com.fasterxml.jackson.databind.ser.Serializers;
import lombok.*;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Builder
@Where(clause = "delete_time is null")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy=IDENTITY)
    private Long id;
    private String openid;
    private String nickname;
    private Integer unifyUid;
    private String email;
    private String password;
    private String mobile;
//    private Long unifyUid;
//    private Object wxProfile;


}
