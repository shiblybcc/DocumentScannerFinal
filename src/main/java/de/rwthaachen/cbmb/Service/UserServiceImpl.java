package de.rwthaachen.cbmb.Service;

import com.google.common.collect.Lists;
import de.rwthaachen.cbmb.Domain.User;
import de.rwthaachen.cbmb.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("userService")
//@Repository
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return Lists.newArrayList(userRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Integer id) {
        return userRepository.findOne(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Override
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

//    @Override
//    @Transactional
//    public User update(User user) {
//        return userRepository.update(user);
//    }

    @Override
    public void destroy(User user) {
        userRepository.delete(user);
    }
}
