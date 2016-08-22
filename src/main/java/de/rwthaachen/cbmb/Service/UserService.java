package de.rwthaachen.cbmb.Service;


import de.rwthaachen.cbmb.Domain.User;

import java.util.List;

public interface UserService {

    public List<User> findAll();

    public User findById(Integer id);
    public User findByUsername(String username);

    public User save(User user);
    public void destroy(User user);
}
