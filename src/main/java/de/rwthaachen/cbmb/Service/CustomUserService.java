package de.rwthaachen.cbmb.Service;


import de.rwthaachen.cbmb.Domain.User;

public interface CustomUserService {

    User findByUsername(String username);
}
