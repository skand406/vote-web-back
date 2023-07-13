package com.example.votewebback.Service;

import com.example.votewebback.DTO.*;
import com.example.votewebback.Entity.*;
import com.example.votewebback.Repository.ManagerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



@RequiredArgsConstructor
@Service
public class ManagerService {
    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;

    public String createManager(RequestDTO.ManagerDTO managerDTO){
        if(managerRepository.findByManagerid(managerDTO.getManager_id())==null){
            ManagerEntity manager = new ManagerEntity();
            manager.setManageremail(managerDTO.getManager_email());
            manager.setManagerid(managerDTO.getManager_id());
            manager.setManagername(managerDTO.getManager_name());
            manager.setManagertel(managerDTO.getManager_tel());
            manager.setManagerpassword(passwordEncoder.encode(managerDTO.getManager_password()));
            this.managerRepository.save(manager);
            return "suscces";
        }
        else return "fail: already register manager";
    }


}
