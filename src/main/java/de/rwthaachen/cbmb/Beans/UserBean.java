package de.rwthaachen.cbmb.Beans;

import de.rwthaachen.cbmb.Domain.User;
import de.rwthaachen.cbmb.Service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

//@Getter
//@Setter
@ManagedBean
@ViewScoped
public class UserBean implements Serializable {

    private static final long serialVersionUID = 1L;

//    @Autowired
    @ManagedProperty("#{userService}")
    UserService userService;

    private User user = new User();

    private List<User> users;

//    @PostConstruct
//    public void loadUsers() {
//        users = userService.findAll();
//    }


    public String register(){
        user.setOriginalPassword(user.getPassword());
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        userService.save(user);
        return "login.xhtml?faces-redirect=true";
    }

        public List<User> findAllUsers() {
            users = userService.findAll();
            return users;
        }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
