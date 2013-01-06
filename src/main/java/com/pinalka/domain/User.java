package com.pinalka.domain;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.validator.constraints.Email;

/**
 * Represent user in the system
 *
 * @author gman
 */
@Entity
public class User {
    
    public static final int MIN_NAME_LENGTH = 4;
    public static final int MAX_NAME_LENGTH = 20;
    public static final int MIN_PASSWORD_LENGTH = 6;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH, message = "profile.error.name.length")
    private String name;
    
    private String pass;

    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    @Email(message = "profile.error.email.format")
    private String email;

    private VisibilityPolicy visibilityPolicy;

    private Locale locale;
    
    private Date registrationDate;
    
    private Date lastActionDate;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "users")
    @LazyCollection(LazyCollectionOption.TRUE)
    private final List<PublicSubject> publicSubjects = new ArrayList<PublicSubject>();

    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private final List<Subject> subjects = new ArrayList<Subject>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public VisibilityPolicy getVisibilityPolicy() {
        return visibilityPolicy;
    }

    public void setVisibilityPolicy(VisibilityPolicy visibilityPolicy) {
        this.visibilityPolicy = visibilityPolicy;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Date getRegistrationDate() {
        if (registrationDate != null) {
            return new Date(registrationDate.getTime());
        } else {
            return null;
        }
    }

    public void setRegistrationDate(Date registrationDate) {
        if (registrationDate != null) {
            this.registrationDate = new Date(registrationDate.getTime());
        } else {
            this.registrationDate = null;
        }
    }

    public Date getLastActionDate() {
        if (lastActionDate != null) {
            return new Date(lastActionDate.getTime());
        } else {
            return null;
        }
    }

    public void setLastActionDate(Date lastActionDate) {
        if (lastActionDate != null) {
            this.lastActionDate = new Date(lastActionDate.getTime());
        } else {
            this.lastActionDate = null;
        }
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final User user = (User) o;

        if (id != null) {
            return id.equals(user.id);
        } else {
            return name != null && name.equals(user.name);
        }
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    public List<PublicSubject> getPublicSubjects() {
        return publicSubjects;
    }
}
