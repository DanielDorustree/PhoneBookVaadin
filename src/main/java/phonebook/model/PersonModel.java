/*
 * Copyright (c) 2023 Dorustree private limited. All rights reserved.
 */

package phonebook.model;

import phonebook.componet.UserGroupType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * <h1>Person Model</h1>
 *
 * <p>Model with handling person {@link PersonModel} details</p>
 */
@Entity
@Table(name = "person")
public class PersonModel implements Serializable
{

    //
    // attributes
    //

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date birthDay;

    @NotNull(message = "Name is required")
    @Size(min = 3, max = 50, message = "name must be longer than 3 and less than 40 characters")
    private String name;

    private Boolean colleague;

    private String phoneNumber;

    @NotNull(message = "Email is required")
    @Pattern(regexp = ".+@.+\\.[a-z]+", message = "Must be valid email")
    private String email;

    private UserGroupType category;

    //
    // constructor
    //

    public PersonModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getColleague() {
        return colleague;
    }

    public void setColleague(Boolean colleague) {
        this.colleague = colleague;
    }

    public UserGroupType getCategory() {
        return category;
    }

    public void setCategory(UserGroupType category) {
        this.category = category;
    }
}