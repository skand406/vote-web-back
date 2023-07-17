package com.example.votewebback.Service;

import com.example.votewebback.DTO.UserDTO;
import com.example.votewebback.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String user_id) throws UsernameNotFoundException {
        UserDTO user = userRepository.findByUserid(user_id);
        if(user == null){
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        return user;
    }
}
