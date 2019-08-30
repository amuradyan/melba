package views;

import entities.UserEntity;

public class UserView {
    private String id;
    private String email;
    private Long createdAt;
    private Long updatedAt;

    public UserView(String id, String email, Long createdAt, Long updatedAt) {
        this.id = id;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static UserView fromEntity(UserEntity userEntity) {
        return new UserView(userEntity.getId(),
                userEntity.getEmail(),
                userEntity.getCreatedAt(),
                userEntity.getUpdatedAt());
    }
}