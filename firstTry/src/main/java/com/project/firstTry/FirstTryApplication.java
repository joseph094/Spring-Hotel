package com.project.firstTry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.project.firstTry.model.Roles;
import com.project.firstTry.model.Users;
import com.project.firstTry.repository.UsersRepository;

@SpringBootApplication
public class FirstTryApplication implements CommandLineRunner {

    // Autowired UsersRepository for interacting with user data
    @Autowired
    private UsersRepository usersRepository;

    // Application entry point
    public static void main(String[] args) {
        SpringApplication.run(FirstTryApplication.class, args);
    }

    // CommandLineRunner method to be executed on application startup
    @Override
    public void run(String... args) throws Exception {
        // Check if an admin account already exists
        Users adminAccount = usersRepository.findByRole(Roles.ADMIN);
        
        // If admin account doesn't exist, create a default one
        if (null == adminAccount) {
            Users user = new Users();
            user.setEmail("admin@gmail.com");
            user.setFirst_name("admin");
            user.setLast_name("admin");
            user.setPhone("25252525");
            user.setRole(Roles.ADMIN);
            
            // Encode the password using BCryptPasswordEncoder
            user.setPassword(new BCryptPasswordEncoder().encode("admin"));
            
            // Save the admin user to the repository
            usersRepository.save(user);
        }
    }
}
