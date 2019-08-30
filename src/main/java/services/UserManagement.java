package services;

import entities.UserEntity;
import models.User;
import specs.UserSpec;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class UserManagement {
    private static final Logger logger = Logger.getLogger(UserManagement.class.getName());

    private UserManagement() {}

    public static UserEntity createUser(UserSpec userSpec) throws SQLIntegrityConstraintViolationException {
        UserEntity user = null;
        try {
            Connection conn = DataSource.getConnection();

            String createUserQuery = "insert into melba.users values (?, ?, ?, ?, ?);";
            String uid = UUID.randomUUID().toString();
            PreparedStatement createUser = conn.prepareStatement(createUserQuery);
            createUser.setString(1, uid);
            createUser.setString(2, userSpec.getEmail());
            createUser.setString(3, userSpec.getPassword());
            createUser.setLong(4, userSpec.getCreatedAt());
            createUser.setLong(5, userSpec.getUpdatedAt());
            int res = createUser.executeUpdate();

            if(res > 0) {
                user = new UserEntity(uid, userSpec.getEmail(), userSpec.getPassword(), userSpec.getCreatedAt(), userSpec.getUpdatedAt());
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
