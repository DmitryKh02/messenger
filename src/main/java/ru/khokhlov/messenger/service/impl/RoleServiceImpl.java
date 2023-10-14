package ru.khokhlov.messenger.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.khokhlov.messenger.entity.Role;
import ru.khokhlov.messenger.repository.RoleRepository;
import ru.khokhlov.messenger.service.RoleService;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    //TODO написать нормальную логику
    @Override
    public Role getRoleByName(String name){
        return roleRepository.findByName(name);
    }

    @Override
    public void createRole(String roleName) {
        Role role = new Role();
        role.setName(roleName);
        roleRepository.save(role);
    }
}
