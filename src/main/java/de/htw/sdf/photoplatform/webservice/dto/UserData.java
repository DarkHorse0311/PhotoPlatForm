/**
 *
 */
package de.htw.sdf.photoplatform.webservice.dto;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * Data transfer object to get and update user data.
 * Represents the domain object User.
 *
 * @author Sergej Meister
 */
public class UserData extends UserCredential implements Serializable {

    @NotEmpty
    protected Long id;

    protected String email;

    protected Integer index;

    protected Boolean banned;

    protected Boolean enabled;

    protected Boolean admin;

    /**
     * Returns user id.
     *
     * @return user id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets user id.
     *
     * @param id uder id.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns index.
     *
     * @return index.
     */
    public Integer getIndex() {
        return index;
    }

    /**
     * Sets index.
     *
     * @param index index.
     */
    public void setIndex(Integer index) {
        this.index = index;
    }

    /**
     * Is user banned.
     *
     * @return true if banned.
     */
    public Boolean isBanned() {
        return banned;
    }

    /**
     * Sets value user banned.
     *
     * @param banned is banned.
     */
    public void setBanned(Boolean banned) {
        this.banned = banned;
    }


    /**
     * Is admin.
     *
     * @return true if user has role admin.
     */
    public Boolean isAdmin() {
        return admin;
    }

    /**
     * Set admin value.
     *
     * @param admin isAdmin.
     */
    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    /**
     * Returns is user enabled.
     *
     * @return true if enabled.
     */
    public Boolean isEnabled() {
        return enabled;
    }

    /**
     * Sets value to enabled.
     * 
     * @param enabled true if enabled.
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Returns user email.
     *
     * @return email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets user email.
     *
     * @param email email.
     */
    public void setEmail(String email) {
        this.email = email;
    }
}
