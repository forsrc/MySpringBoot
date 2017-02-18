package com.forsrc.pojo;


import java.text.MessageFormat;
import java.util.Date;


/**
 * The type User.
 */
//@Entity
//@Table(name = "user")
//@Document(coreName="collection1")
public class User implements java.io.Serializable {

    // Fields
    //@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    //@Temporal(javax.persistence.TemporalType.DATE)
    private Date updateOn;
    //@Temporal(javax.persistence.TemporalType.DATE)
    private Date createOn;
    //@Indexed(solrType="text_general")
    private String username;
    private String email;
    private int status; // 0: delete; 1: OK; 2: NG
    private Boolean isAdmin;
    private String image;
    private int version;

    // Constructors

    /**
     * default constructor
     */
    public User() {
    }

    /**
     * Instantiates a new User.
     *
     * @param id the id
     */
    public User(Long id) {
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
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * Is admin boolean.
     *
     * @return the boolean
     */
    public Boolean isAdmin() {
        return isAdmin;
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
     * Sets admin.
     *
     * @param admin the admin
     */
    public void setAdmin(Boolean admin) {
        this.isAdmin = admin;
    }

    /**
     * Gets is admin.
     *
     * @return the is admin
     */
    public Boolean getIsAdmin() {
        return isAdmin;
    }

    /**
     * Sets is admin.
     *
     * @param admin the admin
     */
    public void setIsAdmin(Boolean admin) {
        this.isAdmin = admin;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets image.
     *
     * @return the image
     */
    public String getImage() {
        return image;
    }

    /**
     * Sets image.
     *
     * @param image the image
     */
    public void setImage(String image) {
        this.image = image;
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
        return MessageFormat.format("'{'\"id\" : \"{0}\", \"email\" : \"{1}\"'}'", id, email);
    }


}