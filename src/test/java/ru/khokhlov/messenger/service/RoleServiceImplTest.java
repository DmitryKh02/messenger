package ru.khokhlov.messenger.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.khokhlov.messenger.entity.Role;
import ru.khokhlov.messenger.repository.RoleRepository;
import ru.khokhlov.messenger.service.impl.RoleServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @InjectMocks
    private RoleServiceImpl roleService;

    @Mock
    private RoleRepository roleRepository;

    @Test
    void testGetRoleByName_Success() {
        // Create test data
        String roleName = "USER";
        Role role = new Role();
        role.setName(roleName);

        // Mock RoleRepository behavior
        when(roleRepository.findByName(roleName)).thenReturn(role);

        // Call the getRoleByName method
        Role result = roleService.getRoleByName(roleName);

        // Assertions
        verify(roleRepository).findByName(roleName); // Verify role lookup
        assertNotNull(result); // Verify that a Role is returned
        assertEquals(roleName, result.getName()); // Verify the role name in the result
    }

    @Test
    void testCreateRole_Success() {
        // Create test data
        String roleName = "ADMIN";

        // Call the createRole method
        roleService.createRole(roleName);

        // Assertions
        verify(roleRepository).save(any(Role.class)); // Verify role creation
    }
    }
