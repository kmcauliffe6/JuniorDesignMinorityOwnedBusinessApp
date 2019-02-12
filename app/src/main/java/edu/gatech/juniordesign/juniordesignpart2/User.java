package edu.gatech.juniordesign.juniordesignpart2;

public class User
{
    private String email;
    private String firstName;
    private String lastName;
    private boolean isAdmin;

    public User (String email, String firstName, String lastName, boolean isAdmin)
    {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isAdmin = isAdmin;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
