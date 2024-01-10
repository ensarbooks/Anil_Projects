package com.vizen.controller;

import com.vizen.entity.User;
import com.vizen.request.dto.CreateUpdateUserDto;
import com.vizen.security.CurrentUser;
import com.vizen.security.VizenUserDetails;
import com.vizen.service.UserService;
import io.swagger.annotations.Api;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


import java.util.List;
import java.util.Optional;

@Api(tags = "User Mgmt")
@RestController
@Validated
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/auth")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/current")
    public ResponseEntity<User> getLoggedInUser() {
        return ResponseEntity.ok(userService.getLoggedInUser());
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ROLE_SUPER_ADMIN')")
    public ResponseEntity<User> getUserByEmail(@NotBlank @PathVariable("email") String email) {
        User user = userService.getUserByEmail(email);
        if (user == null)
            throw new ResourceNotFoundException();
        return ResponseEntity.ok(user);
    }

    @GetMapping("/")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<List<User>> getUsers(
        @RequestParam(name = "orgId", required = false) final String orgId
    ) {
        return ResponseEntity.ok(userService.getAllAccessibleUsers(orgId));
    }

    @PostMapping("/")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<User> createUser(
            @ApiIgnore @CurrentUser final VizenUserDetails currentUser,
            @Valid @RequestBody CreateUpdateUserDto createUpdateUserDto
    ) {
        if (createUpdateUserDto.getOrganizationId() == null) {
            createUpdateUserDto.setOrganizationId(currentUser.getOrganization().getId());
        }
        if (createUpdateUserDto.getRole() == null) {
            createUpdateUserDto.setRole(User.Role.ROLE_USER);
        }
        return ResponseEntity.ok(userService.createOrUpdateUser(Optional.empty(), createUpdateUserDto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable String id,
                                           @Valid @RequestBody CreateUpdateUserDto createUpdateUserDto) {
        return ResponseEntity.ok(userService.createOrUpdateUser(Optional.of(id), createUpdateUserDto));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PutMapping("/enable")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<Void> enableUsers(@RequestBody List<String> idList) {
        userService.enableOrDisableUsers(idList, false);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/disable")
    @PreAuthorize("hasAnyRole('ROLE_SUPER_ADMIN', 'ROLE_ADMIN')")
    public ResponseEntity<Void> disableUsers(@RequestBody List<String> idList) {
        userService.enableOrDisableUsers(idList, true);
        return ResponseEntity.ok().build();
    }

}
