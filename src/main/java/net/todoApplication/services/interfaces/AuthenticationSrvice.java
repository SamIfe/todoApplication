package net.todoApplication.services.interfaces;

import net.todoApplication.data.models.User;

public interface AuthenticationSrvice {

    String generateToken(User user);
    boolean verifyToken(String token);
    String getUserIdFromToken(String token);
    String hashPassword(String password);
    boolean comparePassword(String password, String hash);
}
