package ru.khokhlov.messenger.service;

import ru.khokhlov.messenger.entity.Role;

public interface RoleService {
    /**
     * Retrieves a role by its name.
     *
     * @param name The name of the role to retrieve.
     * @return The role with the specified name.
     */
    Role getRoleByName(String name);

    /**
     * Creates a new role with the given name.
     *
     * @param roleName The name of the new role to be created.
     */
    void createRole(String roleName);
}
