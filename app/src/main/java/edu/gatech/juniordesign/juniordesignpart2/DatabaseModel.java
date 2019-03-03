package edu.gatech.juniordesign.juniordesignpart2;

import android.support.annotation.Nullable;
import android.util.Log;

import java.security.SecureRandom;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

final class DatabaseModel {
    private DatabaseConnector db;
    private boolean dbInitialized;
    @Nullable
    private User currentUser;
    private static DatabaseModel model;
    private static final int SALT_SIZE = 32;
    private PasswordHasher hasher;
    private static String selectedCategory;
    private static ArrayList<BusinessListItem> businessList;
    private static int selectedBusiness;
    private BusinessObject selectedBusinessObject;
    private boolean toggle;

    private DatabaseModel() {
        try {
            db = new DatabaseConnector(
            );
            dbInitialized = true;
            currentUser = null;
            hasher = new PasswordHasher();
        } catch (SQLException e) {
            Log.e("DatabaseModel", e.getMessage());
            dbInitialized = false;
        }
    }

    static void checkInitialization() {
        if ((model == null) || !model.dbInitialized) {
            DatabaseModel.initialize();
        }
    }

    /**
     * This method initializes the model and tries to create a database connection.
     */
    static void initialize() {
        model = new DatabaseModel();
    }

    /**
     * This method gets the current DatabaseModel.
     * @return DatabaseModel currently being maintained.
     */
    static DatabaseModel getInstance() {return model;}

    /**
     * This method returns whether the Database connection has been initialized.
     * @return Boolean whether the Database connection has been initialized
     */
    boolean isDbInitialized() {return dbInitialized;}

    /**
     * This method gets the DatabaseConnector for the model.
     * @return The DatabaseConnector
     */
    DatabaseConnector getConnector() {return db;}

    /**
     * This method gets the current user's username.
     * @return String of current user's username
     */
    @Nullable
    User getCurrentUser() {return currentUser;}

    /**
     * This method clears the current user
     */
    void clearCurrentUser() {
        currentUser = null;
    }

    /**
     * Sets the current user to the one passed in.
     * @param newCurrentUser The new current user
     */
    void setCurrentUser(@Nullable User newCurrentUser) {
        currentUser = newCurrentUser;
    }

    /**
     * This method logs the user in with the passed in credentials if correct. It also sets the
     * current user if the login is successful.
     * @param email The email to login with
     * @param password The password (in plaintext) to login with
     * @return boolean of the result of the login
     */
    boolean login(String email, String password) {
        DatabaseModel.checkInitialization();
        String getUsersText = "SELECT * FROM tb_entity WHERE email=? AND is_inactive is NULL";
        ResultSet results;
        boolean loginStatus;
        try {
            PreparedStatement statement = db.getStatement(getUsersText);
            statement.setString(1, email);
            results = db.query(statement);
            if (!results.next()) {
                // No entries in DB for passed in username
                results.close();
                loginStatus = false;
            } else {
                String dbPass = results.getString("password");
                int salt = results.getInt("salt");
                String hashPass = hasher.getSecurePassword(Integer.toString(salt),
                        password);
                if (dbPass.equals(hashPass)) {
                    Log.i("login", "auth success");
                    loginStatus = true;
                    String entity = results.getString("entity");
                    String first_name = results.getString("first_name");
                    String last_name = results.getString("last_name");
                    boolean isAdmin = results.getBoolean("is_admin");
                    currentUser = new User(email, first_name, last_name, isAdmin, entity);
                } else {
                    Log.i("login", "auth failed");
                    loginStatus = false;
                }
                results.close();
            }
            return loginStatus;
        } catch (SQLException e) {
            Log.e("login", e.getMessage());
            return false;
        }
    }

    public void setSelectedCategory(String category)
    {
        this.selectedCategory = category;
    }

    public String getSelectedCategory()
    {
        return this.selectedCategory;
    }

    public static ArrayList<BusinessListItem> getBusinessList() { return businessList; }

    public void setSelectedBusiness(int businessPK)
    {
        this.selectedBusiness = businessPK;
    }

    public int getSelectedBusiness()
    {
        return this.selectedBusiness;
    }

    public BusinessObject getSelectedBusinessObject() {
        return this.selectedBusinessObject;
    }

    public void setSelectedBusinessObject(BusinessObject b) {
        this.selectedBusinessObject = b;
    }

    public void setToggle(boolean toggle){ this.toggle = toggle; }

    public boolean queryBusinessDetails()
    {
        DatabaseModel.checkInitialization();
        Log.i("BusinessDetails", "here");
        try {
            PreparedStatement checkStatement = db.getStatement(
                    "SELECT b.business," +
                            " b.name," +
                            " b.avg_rating," +
                            " c.description " +
                            " FROM tb_business b " +
                    "LEFT JOIN tb_business_category bc ON b.business = bc.business " +
                    "LEFT JOIN tb_category c ON bc.category = c.category " +
                    "WHERE b.business = CAST(? AS int)");
            checkStatement.setString(1, String.valueOf(selectedBusiness));
            PreparedStatement favoritesStatement = db.getStatement(
                    "SELECT * from tb_entity_favorites " +
                            "WHERE entity = CAST(? AS int) " +
                            "AND business = CAST(? AS int) ");
            favoritesStatement.setString(1, getCurrentUser().getEntity());
            favoritesStatement.setString(2, String.valueOf(selectedBusiness));
            Log.i("BusinessDetails", checkStatement.toString() + ", " + favoritesStatement.toString());
            ResultSet checkResults = db.query(checkStatement);
            ResultSet favoritesResults = db.query(favoritesStatement);
            while ( checkResults.next() ) {
                //TODO : fix to get the remaining arguments
                BusinessObject b_o = new BusinessObject(checkResults.getInt(1),
                        checkResults.getString(2), checkResults.getString(4),
                        checkResults.getString(3), null, null,
                        null);
                setSelectedBusinessObject(b_o);
                Log.i("BusinessDetails", checkResults.getInt(1) + ": "
                        + checkResults.getString(2) + ":" + checkResults.getString(4)
                        + ": " + checkResults.getString(3));
            }
            while ( favoritesResults.next() ) {
                selectedBusinessObject.setIsFavorited(true);
            }
        } catch (SQLException e) {
            Log.e("BusinessDetails", e.getMessage());
        }
        return true;
    }

    public boolean toggleFavorited(){
        DatabaseModel.checkInitialization();
        Log.i("toggleFavorited", "here");
        if (toggle) {
            try {
                PreparedStatement checkStatement = db.getStatement("INSERT INTO tb_entity_favorites(entity, business) VALUES ( CAST(? AS int), CAST(? AS int) ) ");
                checkStatement.setString(1, getCurrentUser().getEntity());
                checkStatement.setString(2, String.valueOf(selectedBusiness));
                Log.i("toggleFavorited", checkStatement.toString());
                db.query(checkStatement);
            } catch (SQLException e) {
                Log.e("toggleFavorited", e.getMessage());
            }
        } else {
            try {
                PreparedStatement deleteStatement = db.getStatement("DELETE FROM tb_entity_favorites WHERE entity = CAST(? AS int) AND business = CAST(? AS int) ");
                deleteStatement.setString(1, getCurrentUser().getEntity());
                deleteStatement.setString(2, String.valueOf(selectedBusiness));
                Log.i("toggleFavorited", deleteStatement.toString());
                db.query(deleteStatement);
            } catch (SQLException e) {
                Log.e("toggleFavorited", e.getMessage());
            }
        }
        return true;
    }




    /**
     * This method registers a user in the database. After registration they will instantly be able
     * to log in to the system.
     *
     * NOTE: this method does not do any input validation. You should check
     * to make sure usernames, passwords, profile names, and home locations match your specific
     * criteria before calling this method.
     * @param email The username under which to register the user
     * @param password The password to use for the user's account
     * @param first_name The first name to display for the user
     * @param last_name The last name to display for the user
     * @param isAdmin A boolean for whether to make the user an administrator.
     * @return An integer code indicating the success of the registration. 0 if registration
     *         succeeds, 1 if the registration fails because the username is already taken, and
     *         2 if a SQLException occurs during registration;
     */
    public int registerUser(String email, String password, String first_name,
                     String last_name, boolean isAdmin) {
        DatabaseModel.checkInitialization();
        SecureRandom saltShaker = new SecureRandom();
        try {
            PreparedStatement checkStatement = db.getStatement(
                    "SELECT * FROM tb_entity WHERE email = ?");
            checkStatement.setString(1, email);
            ResultSet checkResults = db.query(checkStatement);
            if (checkResults.next()) { //Check for username already in use
                return 1;
            } else {
                int salt = saltShaker.nextInt(SALT_SIZE);
                String hashedPass = hasher.getSecurePassword(Integer.toString(salt),
                        password);
                String registrationText = "INSERT INTO tb_entity(email, password, first_name, "
                        + "last_name, salt, is_admin) VALUES(?, ?, ?, ?, ?, ?)";
                PreparedStatement registerStatement = db.getStatement(registrationText);
                registerStatement.setString(1, email);
                registerStatement.setString(2, hashedPass);
                registerStatement.setString(3, first_name);
                registerStatement.setString(4, last_name);
                registerStatement.setInt(5, salt);
                registerStatement.setBoolean(6, isAdmin);
                db.update(registerStatement);
                checkResults = db.query(checkStatement);
                String entity = checkResults.getString("entity");
                setCurrentUser(new User(email, first_name, last_name, isAdmin, entity));
                Log.d("Register User", "Success for email = " + email);
                return 0;
            }
        } catch (SQLException e) {
            Log.e("Register User", e.getMessage());
            return 2;

        }
    }

   public ArrayList<String> getCatagories() {
       DatabaseModel.checkInitialization();
       ArrayList<String> catagories = new ArrayList<String>();
       try {
           ResultSet checkResults = db.query("SELECT description FROM tb_catagory");
           while (checkResults.next()) {
               catagories.add(checkResults.getString(1));
           }
           Log.i("getCatagories", "catagories retrieved: " + catagories);
           return catagories;
       } catch (SQLException e) {
           Log.e("getCatagories", e.getMessage());
           return null;
       }
   }

   public boolean removeUser(String email) {
        DatabaseModel.checkInitialization();
        try {
            PreparedStatement checkStatement = db.getStatement(
                    "UPDATE tb_entity SET is_inactive = now() where email = ?");
            Log.i("removeUser", "'UPDATE tb_entity SET is_inactive = now() where email = "+email+"'");
            checkStatement.setString(1, email);
            db.update(checkStatement);
            return true;
        } catch (SQLException e) {
            Log.e("removeUser", e.getMessage());
            return false;
        }
    }

    boolean queryBusinessList()
    {
        businessList = new ArrayList<BusinessListItem>();
        DatabaseModel.checkInitialization();
        Log.i("BusinessList", "here");
        try {
            PreparedStatement checkStatement = db.getStatement("SELECT b.business, b.name, avg_rating, array_agg(s.name)" +
                    "FROM tb_business b " +
                    "LEFT JOIN tb_business_subcategory bs " +
                    "ON b.business = bs.business " +
                    "LEFT JOIN tb_subcategory s " +
                    "ON bs.subcategory = s.subcategory " +
                    "LEFT JOIN tb_business_category bc " +
                    "ON b.business = bc.business " +
                    "WHERE bc.category = " +
                    "( SELECT category FROM tb_category WHERE description LIKE ? )" +
                    "GROUP BY ( b.business, b.name, avg_rating ) ");
            checkStatement.setString(1, selectedCategory);
            ResultSet checkResults = db.query(checkStatement);
            while ( checkResults.next() )
            {
                businessList.add( new BusinessListItem(checkResults.getInt(1), checkResults.getString(2), checkResults.getString(3), (String[])checkResults.getArray(4).getArray() ) );
                Log.i("BusinessList", checkResults.getInt(1)+ ": " + checkResults.getString(2) + ", " + checkResults.getArray(4));
            }
        } catch (SQLException e) {
            Log.e("BusinessList", e.getMessage());
        }
        return true;
    }

    /**
     * This method will change the user's password if they pass in the correct homeLocation for
     * that particular user.
     * @param userName Username of the user to reset the password.
     * @param first_name first name for authentication.
     * @param last_name first name for authentication.
     * @param newPassword Password to change the password to.
     * @return An integer code indicating the success of the update. 0 if update
     *         succeeds, 1 if the update fails because they passed in the incorrect homeLocation,
     *         and 2 if a SQLException occurs during the update.
     *
    int changePassword( String userName, String first_name, String last_name, String newPassword ) {
        try {
            //Get values from passed in User
            String qText = "SELECT salt, homeLocation FROM users WHERE userName = ?";
            PreparedStatement stmt = db.getStatement(qText);
            stmt.setString(1, userName);
            ResultSet results = db.query(stmt);
            results.next();
            String salt = results.getString("salt");
            String homeLoc = results.getString("homeLocation");
            results.close();
            //Here is where we "Authenticate" the user. If they correctly answer the homeLocation,
            //they can change the password
            if (homeLocation.equals(homeLoc)) {
                //Update the password
                String newPass = hasher.getSecurePassword(salt, newPassword);
                String qText2 = "UPDATE users SET password = ? WHERE userName = ?";
                PreparedStatement stmt2 = db.getStatement(qText2);
                stmt2.setString(1, newPass);
                stmt2.setString(2, userName);
                Log.d("Change Password", "Attempting...");
                db.update(stmt2);
                Log.d("Change Password", "Success!");
                return 0;
            } else {
                Log.d("Change Password", "Failed attempt");
                return 1;
            }

        } catch (SQLException e) {
            Log.e("Change Password", e.getMessage(), e);
            return 2;
        }
    }*/

}

