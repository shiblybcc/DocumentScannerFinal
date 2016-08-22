package de.rwthaachen.cbmb.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("userDetailsService")
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private CustomUserService customUserService;

    @Transactional(readOnly=true)
    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        de.rwthaachen.cbmb.Domain.User user = customUserService.findByUsername(username);
//        de.rwthaachen.cbmb.Domain.User user = customUserService.findByUsername("asdflk");
        if (user == null) {
            return null;
        }
        List<GrantedAuthority> auth = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String password = user.getPassword();
//        return new org.springframework.security.core.userdetails.User("asdflk", password, true, true, true, true,
        return new org.springframework.security.core.userdetails.User(username, password, true, true, true, true,
                auth);
    }
}
