package library;

import java.util.List;

// 관리자 기능 관련 DB 작업을 위한 인터페이스
 
public interface AdminDAO {

    public MainDTO findUserById(String userId);
    public MainDTO findUserByCode(int userCode);
    public boolean deleteUserByCode(int userCode);
    public List<MainDTO> findUserByName(String name);
    public List<MainDTO> findAllUsers();
    
}