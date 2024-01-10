package com.project.firstTry;

import com.project.firstTry.model.Employee;
import com.project.firstTry.model.Roles;
import com.project.firstTry.model.Users;
import com.project.firstTry.repository.EmployeeRepository;
import com.project.firstTry.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@SpringBootApplication
public class FirstTryApplication implements CommandLineRunner{

	@Autowired
	private UsersRepository usersRepository;

	public static void main(String[] args) {
		SpringApplication.run(FirstTryApplication.class, args);

	}

	@Override
	public void run(String... args) throws Exception {
		Users adminAccount= usersRepository.findByRole(Roles.ADMIN);
		if(null==adminAccount){
			Users user =new Users();
			user.setEmail("admin@gmail.com");
			user.setFirst_name("admin");
			user.setLast_name("admin");
			user.setPhone("25252525");
			user.setRole(Roles.ADMIN);
			user.setPassword(new BCryptPasswordEncoder().encode("admin"));
			usersRepository.save(user);
		}
	}



}
