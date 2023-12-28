package com.project.firstTry.service.impl;

import com.project.firstTry.dto.UpdatePassword;
import com.project.firstTry.model.Users;
import com.project.firstTry.repository.ReservationRepository;
import com.project.firstTry.repository.UsersRepository;
import com.project.firstTry.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UsersRepository usersRepository;
    private final ReservationRepository reservationRepository;




    @Override
    public Users createUser(Users user) {
        return usersRepository.save(user);
    }

    @Override
    public Users getUserById(Long userId) {
        return usersRepository.findById(userId).orElse(null);
    }

    @Override
    public List<Users> getAllUsers() {
        return usersRepository.findAll();
    }

    public Users updateUser(Long userId, Users updatedUser) {
        Optional<Users> optionalUser = usersRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Users existingUser = optionalUser.get();
            // Update user fields based on your requirements
            existingUser.setFirst_name(updatedUser.getFirst_name());
            existingUser.setLast_name(updatedUser.getLast_name());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPhone(updatedUser.getPhone());
            System.out.println("updating  User");

            return usersRepository.save(existingUser);
        }
        return null;
    }


    @Override
    @Transactional
    public void deleteUser(Long userId) {
        reservationRepository.deleteByUserId(userId);

        // Delete the chamber
        usersRepository.deleteById(userId);

    }

    @Override
    public UserDetailsService usersDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return usersRepository.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("User Not Found"));
            }
        };
    }
}
