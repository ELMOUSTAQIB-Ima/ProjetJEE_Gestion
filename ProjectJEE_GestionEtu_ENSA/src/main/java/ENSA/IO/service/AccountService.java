package ENSA.IO.service;


import ENSA.IO.Entities.AppRole;
import ENSA.IO.Entities.AppUser;

public interface AccountService {

    public AppUser saveUser(String username, String password, String confirmedPassword);
    public AppRole save(AppRole role);
    public AppUser loadUserByUsername(String username);
    public void addRoleToUser(String username,String rolename);
}
