package com.example.inventorymanager.init;


import com.example.inventorymanager.entity.Role;
import com.example.inventorymanager.entity.User;
import com.example.inventorymanager.repository.RoleRepo;
import com.example.inventorymanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    UserService userService;

    @Override
    public void run(String... args) throws Exception {

      /* Role role = roleRepo.getOne(1L);
        List<Role> roleList1 = new ArrayList<>();
        roleList1.add(role);
        User user1 = new User("dealer2@gmail.com", "12345",roleList1 );
        userService.save(user1);

       Role role2 = roleRepo.getOne(2L);
        List<Role> roleList2 = new ArrayList<>();
        roleList2.add(role2);
        User user2 = new User("admin@gmail.com", "12345",roleList2 );
        userService.save(user2);

        Role role3 = roleRepo.getOne(3L);
        List<Role> roleList3 = new ArrayList<>();
        roleList3.add(role3);
        User user3 = new User("inmanager@gmail.com", "12345",roleList3 );
        userService.save(user3);

        Role role4 = roleRepo.getOne(4L);
        List<Role> roleList4 = new ArrayList<>();
        roleList4.add(role4);
        User user4 = new User("accmanager@gmail.com", "12345",roleList4 );
        userService.save(user4);

        Role role5 = roleRepo.getOne(5L);
        List<Role> roleList5 = new ArrayList<>();
        roleList5.add(role5);
        User user5 = new User("delivery@gmail.com", "12345",roleList5 );
        userService.save(user5);*/

    }
}
