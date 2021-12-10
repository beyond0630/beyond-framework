
INSERT INTO `br_role` VALUES (1, 'ADMIN', '管理员', b'0');

# password admin
INSERT INTO `br_user` VALUES (1, 'admin', 'd033e22ae348aeb5660fc2140aec35850c4da997', 'aaaa@beyond.com', '2021-12-09 17:15:45', b'0', b'0', 1, '2021-12-09 17:15:45', 1, '2021-12-09 17:15:56');

INSERT INTO `br_user_role` VALUES (1, 1, 'ADMIN', b'0');

INSERT INTO `br_permission` VALUES (1, 'ADD_ROLE', '添加角色', 'POST-/api/roles', b'0');
INSERT INTO `br_permission` VALUES (2, 'EDIT_ROLE', '编辑角色', 'PUT-/api/roles', b'0');
INSERT INTO `br_permission` VALUES (3, 'DELETE_ROLE', '删除角色', 'DELETE-/api/roles', b'0');
INSERT INTO `br_permission` VALUES (4, 'ADD_PERMISSION', '添加权限', 'POST-/api/permissions', b'0');
INSERT INTO `br_permission` VALUES (5, 'EDIT_PERMISSION', '编辑权限', 'PUT-/api/permissions', b'0');
INSERT INTO `br_permission` VALUES (6, 'DELETE_PERMISSION', '删除权限', 'DELETE-/api/permissions', b'0');
INSERT INTO `br_permission` VALUES (7, 'ADD_USER_ROLE', '添加用户角色', 'POST-/api/users/roles', b'0');
INSERT INTO `br_permission` VALUES (8, 'DELETE_USER_ROLE', '删除用户角色', 'DELETE-/api/users/roles', b'0');
INSERT INTO `br_permission` VALUES (9, 'ADD_ROLE_PERMISSION', '添加角色权限', 'POST-/api/roles/permissions', b'0');
INSERT INTO `br_permission` VALUES (10, 'DELETE_ROLE_PERMISSION', '删除角色权限', 'DELETE-/api/roles/permissions', b'0');


INSERT INTO `br_role_permission` VALUES (1, 'ADMIN', 'ADD_ROLE', b'0');
INSERT INTO `br_role_permission` VALUES (2, 'ADMIN', 'EDIT_ROLE', b'0');
INSERT INTO `br_role_permission` VALUES (3, 'ADMIN', 'DELETE_ROLE', b'0');
INSERT INTO `br_role_permission` VALUES (4, 'ADMIN', 'ADD_PERMISSION', b'0');
INSERT INTO `br_role_permission` VALUES (5, 'ADMIN', 'EDIT_PERMISSION', b'0');
INSERT INTO `br_role_permission` VALUES (6, 'ADMIN', 'DELETE_PERMISSION', b'0');
INSERT INTO `br_role_permission` VALUES (7, 'ADMIN', 'ADD_USER_ROLE', b'0');
INSERT INTO `br_role_permission` VALUES (8, 'ADMIN', 'DELETE_USER_ROLE', b'0');
INSERT INTO `br_role_permission` VALUES (9, 'ADMIN', 'ADD_ROLE_PERMISSION', b'0');
INSERT INTO `br_role_permission` VALUES (10, 'ADMIN', 'DELETE_ROLE_PERMISSION', b'0');
