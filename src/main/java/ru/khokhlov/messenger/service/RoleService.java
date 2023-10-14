package ru.khokhlov.messenger.service;

import ru.khokhlov.messenger.entity.Role;

public interface RoleService {
    //TODO написать нормальную логику
    Role getRoleByName(String name);

    void createRole(String roleName);
}
