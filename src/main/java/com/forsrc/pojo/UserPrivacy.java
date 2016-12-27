package com.forsrc.pojo;

import java.text.MessageFormat;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 * The type UserPrivacy.
 */
//@Entity
//@Table(name = "user_privacy")
public class UserPrivacy implements java.io.Serializable {

    // Fields
    //@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long userId;
    //@Temporal(javax.persistence.TemporalType.DATE)
    private Date createOn;
    //@Temporal(javax.persistence.TemporalType.DATE)
    private Date updateOn;
    private String password;
    private int version;

    // Constructors
    /**
     * default constructor
     */
    public UserPrivacy() {
    }

    /**
     * Instantiates a new User.
     *
     * @param id the id
     */
    public UserPrivacy(Long id) {
        this.id = id;
    }

    // Property accessors
    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return this.id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets update on.
     *
     * @return the update on
     */
    public Date getUpdateOn() {
        return updateOn;
    }

    /**
     * Sets update on.
     *
     * @param updateOn the update on
     */
    public void setUpdateOn(Date updateOn) {
        this.updateOn = updateOn;
    }

    /**
     * Gets create on.
     *
     * @return the create on
     */
    public Date getCreateOn() {
        return createOn;
    }

    /**
     * Sets create on.
     *
     * @param createOn the create on
     */
    public void setCreateOn(Date createOn) {
        this.createOn = createOn;
    }

    /**
     * Gets version.
     *
     * @return the version
     */
    public int getVersion() {
        return version;
    }

    /**
     * Sets version.
     *
     * @param version the version
     */
    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return MessageFormat.format("'{'\"id\" : \"{0}\", \"password\" : \"{1}\"'}'", id, password);
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}