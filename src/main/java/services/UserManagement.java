package services;

import entities.UserEntity;
import specs.UserSpec;

import java.sql.*;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class UserManagement {
    private static final Logger logger = Logger.getLogger(UserManagement.class.getName());

    private UserManagement() {}

    public static UserEntity getUser(String email, String password) {
        UserEntity res = null;

        try (Connection conn = DataSource.getConnection()){
            PreparedStatement ps = conn.prepareStatement("select * from melba.users where email=? and pwd=?");
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet users = ps.executeQuery();

            if(users.next())
                res = new UserEntity(users.getString("uid"),
                        users.getString("email"),
                        users.getString("pwd"),
                        users.getLong("createdAt"),
                        users.getLong("updatedAt"));
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return res;
    }

    public static UserEntity createUser(UserSpec userSpec) throws SQLIntegrityConstraintViolationException {
        UserEntity user = null;
        try (Connection conn = DataSource.getConnection()) {
            Long createdAt = System.currentTimeMillis();
            Long updatedAt = createdAt;

            String createUserQuery = "insert into melba.users values (?, ?, ?, ?, ?);";
            String uid = UUID.randomUUID().toString();
            PreparedStatement createUser = conn.prepareStatement(createUserQuery);
            createUser.setString(1, uid);
            createUser.setString(2, userSpec.getEmail());
            createUser.setString(3, userSpec.getPassword());
            createUser.setLong(4, createdAt); // createdAt
            createUser.setLong(5, updatedAt);
            int res = createUser.executeUpdate();

            if(res > 0) {
                user = new UserEntity(uid, userSpec.getEmail(), userSpec.getPassword(), createdAt, updatedAt);
            }
        } catch (SQLIntegrityConstraintViolationException e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
            throw e;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }

        return user;
    }
}
