package com.cf.cabinescaperoom.service;

import com.cf.cabinescaperoom.models.Provider;
import com.cf.cabinescaperoom.models.Role;
import com.cf.cabinescaperoom.models.User;
import com.cf.cabinescaperoom.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository repo;

    public void processOAuthPostLogin(String username, String provider) {
        Boolean existUser = repo.existsByUsername(username);
        if (!existUser) {
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setProvider(Provider.valueOf(provider.toUpperCase()));
            newUser.setEnabled(true);

            repo.save(newUser);
            System.out.println("Created new user: " + username);
        }

    }

    public void updateAuthenticationType(String username, String oauth2ClientName) {
        Provider provider = Provider.valueOf(oauth2ClientName.toUpperCase());
        repo.updateProvider(username, provider);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User loaded = repo.getUserByUsername(username);

        if( loaded != null && loaded.isEnabled()) {
            Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

            for(Role role : loaded.getRoles()) {
                grantedAuthorities.add(new SimpleGrantedAuthority(role.getName()));
            }
            org.springframework.security.core.userdetails.User user =
                    new org.springframework.security.core.userdetails.User(
                            loaded.getUsername()
                            , loaded.getPassword()
                            , grantedAuthorities);
            return user;
        } else {
            throw new UsernameNotFoundException("could not find username: " + username);
        }
    }

    public void updateResetPasswordToken(String token, String email) throws UsernameNotFoundException {
        User user = repo.findByEmail(email);
        if (user != null) {
            user.setResetPasswordToken(token);
            repo.save(user);
        } else {
            throw new UsernameNotFoundException("Could not find any customer with the email " + email);
        }
    }

    public User getByResetPasswordToken(String token) {
        return repo.findByResetPasswordToken(token);
    }

    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        user.setResetPasswordToken(null);
        repo.save(user);
    }

    public List<User> getAllUsers() {
        return (List<User>) repo.findAll();
    }

    public User createUser(User toAdd) {
        return repo.save(toAdd);
    }

    public boolean existsByUsername(String username){
        return repo.existsByUsername(username);
    }

    public User getUserByUsername(String username) { return repo.getUserByUsername(username); }

    public User getUserById(long id){
        return repo.findById(id).orElse(null);
    }

    public void updateUser(User newData) {
        repo.save(newData);
    }

    public void deleteUser(long id) {
        //Soft Delete
        User user = repo.findById(id).orElse(null);
        user.setEnabled(false);
        repo.save(user);
        //        template.update("UPDATE user SET enabled = 0 WHERE id = ?", id);
    }

}
