package com.codefusion.wasbackend.Account.service;

import com.codefusion.wasbackend.Account.dto.AccountEntityDto;
import com.codefusion.wasbackend.Account.model.AccountEntity;
import com.codefusion.wasbackend.Account.repository.AccountRepository;
import com.codefusion.wasbackend.company.model.CompanyEntity;
import com.codefusion.wasbackend.config.security.JwtUtil;
import com.codefusion.wasbackend.store.model.StoreEntity;
import com.codefusion.wasbackend.user.model.UserEntity;
import com.codefusion.wasbackend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public String login(AccountEntityDto entity) throws Exception {
        validateUserDTO(entity);

        AccountEntity account = accountRepository.findByUsername(entity.getUsername());
        if (account == null) {
            throw new Exception("User not found");
        }
        if (!passwordEncoder.matches(entity.getPassword(), account.getPassword())) {
            throw new Exception("Invalid password");
        }

        List<String> roles = account.getRoles().stream()
                .map(Enum::name)
                .collect(Collectors.toList());

        Long userId;
        if(account.getUser() != null && account.getUser().getId() != null){
            userId = account.getUser().getId();
        }
        else{
            userId = account.getId();
        }

        Long storeId = null;
        Long companyId = null;
        if (roles.contains("MANAGER") || roles.contains("EMPLOYEE")) {
            Optional<UserEntity> userOpt = userRepository.findById(userId);
            if(userOpt.isPresent()){
                UserEntity user = userOpt.get();
                List<StoreEntity> stores = user.getStores();

                if(stores != null && !stores.isEmpty()){
                    storeId = stores.get(0).getId();
                }
            }
        }
        if (roles.contains("BOSS")) {
            Optional<UserEntity> userOpt = userRepository.findById(userId);
            if(userOpt.isPresent()){
                UserEntity user = userOpt.get();
                CompanyEntity company = user.getCompany();

                if(company != null){
                    companyId = company.getId();
                }
            }
        }

        return jwtUtil.generateToken(entity.getUsername(), roles, userId, storeId, companyId);
    }

    private void validateUserDTO(AccountEntityDto entity) throws Exception {
        if (entity == null || entity.getUsername() == null || entity.getUsername().isEmpty()
                || entity.getPassword() == null || entity.getPassword().isEmpty()) {
            throw new Exception("Invalid request");
        }
    }
}
