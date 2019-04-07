package edu.gatech.juniordesign.juniordesignpart2;

public class User
{
    private String email;
    private String firstName;
    private String lastName;
    private boolean isAdmin;
    private String entity;

    public User (String email, String firstName, String lastName, boolean isAdmin, String entity)
    {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isAdmin = isAdmin;
        this.entity = entity;
    }

    /**
     * getter for email variable
     * @return user's email
     */
    public String getEmail() { return email; }

    /**
     * getter for firstName variable
     * @return user's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * getter for lastName variable
     * @return user's last name
     */
    public String getLastName() { return lastName; }

    /**
     * getter for isAdmin variable
     * @return whether or not User is an admin
     */
    public Boolean getAdmin() { return  isAdmin; }

    /**
     * getter for entity variable
     * @return entity
     */
    public String getEntity() { return entity; }
}
