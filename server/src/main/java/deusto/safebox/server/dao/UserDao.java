package deusto.safebox.server.dao;

import deusto.safebox.server.User;
import java.util.Optional;
import java.util.UUID;

public interface UserDao extends Dao<User, UUID> {

    Optional<User> getByEmail(String email) throws DaoException;
}
