package library;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO1 {
	public List<BookInfoDTO1> listBook(String bookname, String author_name);
	public void loan(BookInfoDTO1 dto) throws SQLException;
}
