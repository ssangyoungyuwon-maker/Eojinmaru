package library;

import java.sql.SQLException;

public interface UserDAO {
	
	public boolean signUpUser(UserDTO user) throws SQLException;
}
