package com.vizen.service;

import com.vizen.entity.Organization;
import com.vizen.entity.User;
import com.vizen.entity.UserLoginTracker;
import com.vizen.entity.UserPasswordResetRequest;
import com.vizen.repository.OrganizationRepository;
import com.vizen.repository.UserLoginTrackerRepository;
import com.vizen.repository.UserPasswordResetRequestRepository;
import com.vizen.repository.UserRepository;
import com.vizen.request.dto.CreateUpdateUserDto;
import com.vizen.security.VizenUserDetails;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@Transactional
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    private OrganizationRepository organizationRepository;

    private final UserPasswordResetRequestRepository userPasswordResetRequestRepository;

    private UserLoginTrackerRepository userLoginTrackerRepository;

    private BCryptPasswordEncoder bCryptEncoder;

    private EmailSender emailSender;

    @Value("${app.url.prefix:}")
    private String serverUrlPrefix;

    @Value("${app.allowed.email.domains:ensarsolutions.com,vizenanalytics.com}")
    private String alwaysAllowedEmailDomainsSettingStr;

    private List<String> alwaysAllowedEmailDomains;

    @Autowired
    public UserService(UserRepository userRepository,
                       OrganizationRepository organizationRepository,
                       UserPasswordResetRequestRepository userPasswordResetRequestRepository,
                       //BCryptPasswordEncoder bCryptEncoder,
                       UserLoginTrackerRepository userLoginTrackerRepository,
                       EmailSender emailSender) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.userPasswordResetRequestRepository = userPasswordResetRequestRepository;
        this.userLoginTrackerRepository = userLoginTrackerRepository;
        //this.bCryptEncoder = bCryptEncoder;
        this.emailSender = emailSender;
    }

    @PostConstruct
    public void init() {
        alwaysAllowedEmailDomains = Arrays.stream(alwaysAllowedEmailDomainsSettingStr.split(",")).map(a->a.toLowerCase().trim()).collect(Collectors.toList());
        log.info("### Always allowed email domains : " + alwaysAllowedEmailDomains);
    }

    public User getUserById(String id) {
        Optional<User> userOptional = userRepository.findById(id);
        if(!userOptional.isPresent())
            throw new RuntimeException("User with " + id + " not found.");

        return userOptional.get();
    }

    public List<User> getAllAccessibleUsers(String orgId) {
        User user = getLoggedInUser();
        if (user.getRole().equals(User.Role.ROLE_SUPER_ADMIN)) {
            if (orgId != null) {
                return userRepository.findByOrganizationId(orgId);
            }
            return userRepository.findAll();
        }

        if (user.getRole().equals(User.Role.ROLE_ADMIN))
            return userRepository.findByOrganization(user.getOrganization());

        return List.of();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updatePassword(String token, String newPassword) {
        UserPasswordResetRequest userPasswordResetRequest = userPasswordResetRequestRepository.getById(token);
        User user = userRepository.getById(userPasswordResetRequest.getUserId());
        user.setPassword(bCryptEncoder.encode(newPassword));
        userRepository.save(user);
        String subject = "Vizen Account password successfully changed.";
        String message = "You've successfully updated your password for your Vizen Analytics Account. Please login to your account" +
                " using link : " + serverUrlPrefix;
        emailSender.sendSimpleMessage(user.getEmail(), subject, message);

        return user;
    }

    public UserPasswordResetRequest createPasswordResetRequest(String email, boolean newUser) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return null;
        }
        UserPasswordResetRequest userPasswordResetRequest = new UserPasswordResetRequest();
        userPasswordResetRequest.setUserId(user.getId());
        userPasswordResetRequest.setExpireDateTime(Timestamp.valueOf(LocalDateTime.now().plusHours(2)));
        userPasswordResetRequestRepository.save(userPasswordResetRequest);


        String subject = newUser ? "Welcome to Vizen Analytics" :"Reset your Vizen Analytics password";
        String message = "Update your Vizen Analytics account password using link: " + generateResetPasswordUrl(userPasswordResetRequest.getId());
        log.info(message);
        emailSender.sendSimpleMessage(user.getEmail(), subject, message);

        return userPasswordResetRequest;

    }

    public UserPasswordResetRequest getPasswordResetRequest(String id) {
        return userPasswordResetRequestRepository.getById(id);
    }

    @Override
    public VizenUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username.");
        }
        return new VizenUserDetails(user);
    }

    public User createOrUpdateUser(Optional<String> userId, CreateUpdateUserDto createUpdateUserDto) {
        Optional<User> userOptional;
        User user;
        if (userId.isPresent()) {
            userOptional = userRepository.findById(userId.get());
            if (!userOptional.isPresent())
                throw new RuntimeException("User with id " + userId.get() + " not found");
            user = userOptional.get();
        } else {
            if (userRepository.existsByEmail(createUpdateUserDto.getEmail())) {
                throw new RuntimeException("User with email " + createUpdateUserDto.getEmail() + " already exists.");
            }
            user = new User();
        }
        User currentUser = getLoggedInUser();

        Optional<Organization> orgOptional = organizationRepository.findById(createUpdateUserDto.getOrganizationId());
        if (!orgOptional.isPresent() ||
                (User.Role.ROLE_ADMIN.equals(currentUser.getRole()) &&
                        !currentUser.getOrganization().getId().equals(createUpdateUserDto.getOrganizationId())))
            throw new RuntimeException("Invalid organization.");

        if (User.Role.ROLE_SUPER_ADMIN != createUpdateUserDto.getRole()) {
            String userEmailDomain = createUpdateUserDto.getEmail().substring(createUpdateUserDto.getEmail().indexOf("@") + 1).toLowerCase().trim();
            if (!alwaysAllowedEmailDomains.contains(userEmailDomain) &&
                    !orgOptional.get().getDomain().trim().equalsIgnoreCase(userEmailDomain.trim()))
                throw new RuntimeException("Invalid Email domain, must be " + orgOptional.get().getDomain());
        }
        user.setEmail(createUpdateUserDto.getEmail());
        user.setFirstName(createUpdateUserDto.getFirstName());
        user.setLastName(createUpdateUserDto.getLastName());
        user.setOrganization(orgOptional.get());
        user.setRole(createUpdateUserDto.getRole());
        user = userRepository.save(user);

        createPasswordResetRequest(user.getEmail(), true);
        return user;
    }

    public User getLoggedInUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return getUserByEmail(authentication.getName());
    }

    public void enableOrDisableUsers(List<String> userIdList, final boolean disabled) {
        userIdList.forEach(id-> {userRepository.getById(id).setDisabled(disabled);});
    }

    private String generateResetPasswordUrl(String token) {
        return serverUrlPrefix + "/reset-password?token=" + token;
    }

    public String createLoginTracker(String email, String userIp) {
        UserLoginTracker userLoginTracker = new UserLoginTracker();
        userLoginTracker.setUserIp(userIp);
        userLoginTracker.setUserEmail(email);
        userLoginTrackerRepository.save(userLoginTracker);
        return userLoginTracker.getId();
    }

    public void markLoginSuccess (String id) {
        UserLoginTracker userLoginTracker = userLoginTrackerRepository.getById(id);
        userLoginTracker.setSucceeded(true);
        userLoginTrackerRepository.save(userLoginTracker);
        User user = userRepository.findByEmail(userLoginTracker.getUserEmail());
        user.setLastLoginDateTime(Timestamp.valueOf(LocalDateTime.now()));
        userRepository.save(user);
    }

}
