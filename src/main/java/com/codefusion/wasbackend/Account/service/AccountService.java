package com.codefusion.wasbackend.Account.service;

import com.codefusion.wasbackend.Account.dto.AccountEntityDto;
import com.codefusion.wasbackend.Account.mapper.AccountEntityMapper;
import com.codefusion.wasbackend.Account.model.AccountEntity;
import com.codefusion.wasbackend.Account.model.Role;
import com.codefusion.wasbackend.Account.repository.AccountRepository;
import com.codefusion.wasbackend.user.dto.UserDTO;
import com.codefusion.wasbackend.user.model.UserEntity;
import com.codefusion.wasbackend.user.repository.UserRepository;
import com.codefusion.wasbackend.user.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.Collections;
import java.util.HashSet;

@Service
public class AccountService {

    private final AccountEntityMapper accountEntityMapper;
    private final AccountRepository accountRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public AccountService(AccountEntityMapper accountEntityMapper, AccountRepository accountRepository, UserService userService,
                          UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.accountEntityMapper = accountEntityMapper;
        this.accountRepository = accountRepository;
        this.userService = userService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Creates a new account.
     *
     * @param userDTO           the UserDTO object containing user information
     * @param file              the profile picture file of the user
     * @param accountEntityDto  the AccountEntityDto object representing the account entity
     * @return the AccountEntityDto object representing the created account entity
     * @throws RuntimeException if there is an error while creating the account
     */
    @Transactional
    public AccountEntityDto createAccount(UserDTO userDTO, MultipartFile file, AccountEntityDto accountEntityDto) {
        try {
            boolean accountExists = accountRepository.existsByUsername(accountEntityDto.getUsername());
            if (accountExists) {
                throw new IllegalArgumentException("A user with the same username already exists");
            }

            if (accountEntityDto.getRoles().contains(Role.EMPLOYEE) || accountEntityDto.getRoles().contains(Role.MANAGER)) {
                if (userDTO.getOwnerId() == null) {
                    throw new IllegalArgumentException("Owner ID is required for EMPLOYEE and MANAGER roles");
                }
            } else {
                userDTO.setOwnerId(null);
            }

            UserDTO createdUserDto = userService.addUser(userDTO, file);

            UserEntity userEntity = userService.getUserByIdforAccount(createdUserDto.getId());

            AccountEntity accountEntity = accountEntityMapper.toEntity(accountEntityDto);

            accountEntity.setPassword(passwordEncoder.encode(accountEntity.getPassword()));
            accountEntity.setId(userEntity.getId());
            accountEntity.setUser(userEntity);
            AccountEntity savedAccountEntity = accountRepository.save(accountEntity);

            userEntity.setAccount(savedAccountEntity);
            userRepository.save(userEntity);

            return accountEntityMapper.toDto(savedAccountEntity);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create account", e);
        }
    }



    public void initializeUsers() {
        if (accountRepository.count() == 0) {
            createUsers();
        }
    }


    /**
     * Creates a new account.
     *
     * @param userDTO           the UserDTO object containing user information
     * @param file              the profile picture file of the user
     * @param accountEntityDto  the AccountEntityDto object representing the account entity
     * @return the AccountEntityDto object representing the created account entity
     * @throws RuntimeException if there is an error while creating the account
     */
    @Transactional
    public AccountEntityDto createAccountAdmin(UserDTO userDTO, MultipartFile file, AccountEntityDto accountEntityDto) {
        try {
            UserDTO createdUserDto = userService.addUser(userDTO, file);

            UserEntity userEntity = userService.getUserByIdforAccount(createdUserDto.getId());

            AccountEntity accountEntity = accountEntityMapper.toEntity(accountEntityDto);

            accountEntity.setPassword(passwordEncoder.encode(accountEntity.getPassword()));
            accountEntity.setId(userEntity.getId());
            accountEntity.setUser(userEntity);
            AccountEntity savedAccountEntity = accountRepository.save(accountEntity);

            userEntity.setAccount(savedAccountEntity);

            userRepository.save(userEntity);

            return accountEntityMapper.toDto(savedAccountEntity);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create account", e);
        }
    }

    public void createUsers() {

        AccountEntityDto adminAccount = createAccountEntity("admin", "admin123", Role.ADMIN);
        UserDTO admin = createUserEntity("admin", "admin", "admin@gmail.com","321231321");
        createAccountAdmin(admin,null,adminAccount);

        AccountEntityDto bossAccount = createAccountEntity("boss", "boss123", Role.BOSS);
        UserDTO boss = createUserEntity("boss", "boss", "boss@gmail.com","321231321");
        createAccountAdmin(boss,null,bossAccount);

        AccountEntityDto managerAccount = createAccountEntity("manager", "manager123", Role.MANAGER);
        UserDTO manager = createUserEntity("manager", "manager", "manager@gmail.com","321231321");
        createAccountAdmin(manager,null,managerAccount);

        AccountEntityDto employeeAccount = createAccountEntity("employee", "employee123", Role.EMPLOYEE);
        UserDTO employee = createUserEntity("employee", "employee", "employee@gmail.com","321231321");
        createAccountAdmin(employee,null,employeeAccount);

    }


    private AccountEntityDto createAccountEntity(String username, String password, Role role) {
        AccountEntityDto account = new AccountEntityDto();
        account.setUsername(username);
        account.setPassword(password);
        account.setRoles(new HashSet<>(Collections.singletonList(role)));
        return account;
    }

    private UserDTO createUserEntity(String name, String surname, String email, String phoneNo) {
        UserDTO account = new UserDTO();
        account.setName(name);
        account.setSurname(surname);
        account.setEmail(email);
        account.setPhoneNo(phoneNo);
        return account;
    }


}
