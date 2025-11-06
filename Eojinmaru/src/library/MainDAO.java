package library;

import java.sql.SQLException;

public interface MainDAO {
	
	public boolean signUpUser(MainDTO user) throws SQLException;
}
