package com.h2rd.refactoring.management.user;

import com.fasterxml.jackson.annotation.JsonView;
import com.h2rd.refactoring.management.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;
import java.util.UUID;

@Builder(toBuilder = true)
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "JR_USER", uniqueConstraints = {@UniqueConstraint(columnNames={"USER_EMAIL"})})
@DynamicInsert
public class User {
    @Id
    @GenericGenerator(name = "uuid-gen", strategy = "uuid2")
    @GeneratedValue(generator = "uuid-gen")
    @Column(name = "USER_ID", nullable = false)
    @JsonView({UserView.ExistingUser.class})
    @Null(groups = User.New.class)
    @NotNull(groups = User.Existing.class)
    private UUID id;

    @Column(name = "USER_FIRST_NAME", nullable = false)
    @JsonView({UserView.ExistingUser.class, UserView.NewUser.class})
    @NotNull
    private String firstName;

    @Column(name = "USER_LAST_NAME", nullable = false)
    @JsonView({UserView.ExistingUser.class, UserView.NewUser.class})
    @NotNull
    private String lastName;

    @Column(name = "USER_EMAIL", unique = true, nullable = false)
    @JsonView({UserView.ExistingUser.class, UserView.NewUser.class})
    @NotNull
    private String email;

    @ElementCollection(targetClass = Role.class)
    @Enumerated(EnumType.STRING)
    @JsonView({UserView.ExistingUser.class, UserView.NewUser.class})
    @NotNull
    private List<Role> roles;

    public interface New{}

    public interface Existing{}
}
