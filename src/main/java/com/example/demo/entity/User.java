package com.example.demo.entity;

import com.example.demo.enums.Role;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User {
    @Column(name = "id", nullable = false, columnDefinition = "INT UNSIGNED")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "active", nullable = false, columnDefinition = "BIT")
    private Boolean active = true;

    @ElementCollection(targetClass = Role.class)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(
                    name = "user_id",
                    nullable = false,
                    columnDefinition = "INT UNSIGNED",
                    referencedColumnName = "id"
            )
    )
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Set<Role> roles = new HashSet<>();

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @OneToOne(targetEntity = Upload.class)
    @JoinColumn(name = "avatar_id", referencedColumnName = "id")
    private Upload avatar;

    @Transient
    private MultipartFile avatarFile;

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public Set<String> getStringRoles() {
        return roles.stream()
                .map(Enum::toString)
                .collect(Collectors.toSet());
    }

    public void setStringRoles(Set<String> stringRoles) {
        roles = stringRoles.stream()
                .map(Role::valueOf)
                .collect(Collectors.toSet());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Upload getAvatar() {
        return avatar;
    }

    public void setAvatar(Upload avatar) {
        this.avatar = avatar;
    }

    public MultipartFile getAvatarFile() {
        return avatarFile;
    }

    public void setAvatarFile(MultipartFile avatarFile) {
        this.avatarFile = avatarFile;
    }
}
