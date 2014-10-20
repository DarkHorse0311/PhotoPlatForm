package de.htw.sdf.photoplatform.manager.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.htw.sdf.photoplatform.manager.UserManager;
import de.htw.sdf.photoplatform.manager.common.DAOReferenceCollector;
import de.htw.sdf.photoplatform.persistence.User;
import de.htw.sdf.photoplatform.persistence.UserRole;

/**
 * business methods for users
 * 
 * @author Vincent Schwarzer
 * 
 */
@Service
@Transactional
public class UserManagerImpl extends DAOReferenceCollector implements
		UserManager {

	@Override
	public void create(User entity) {
		userDAO.create(entity);
		UserRole userRole = new UserRole();
		userRole.setUser(entity);
		userRole.setRole(roleDAO.findByName("ROLE_USER"));
		userRoleDAO.create(userRole);

	}

	@Override
	public User update(User entity) {
		return userDAO.update(entity);
	}

	@Override
	public void delete(User entity) {
		userDAO.delete(entity);

	}

	@Override
	public User findById(long id) {
		return userDAO.findOne(id);
	}

	@Override
	public List<User> findAll() {
		return userDAO.findAll();
	}

	@Override
	public void deleteAll() {
		userDAO.deleteAll();

	}

	@Override
	public User findByName(String name) {
		return userDAO.findByUserName(name);
	}

	@Override
	public Long getRecipeAmount(String userName) {
		return userDAO.getRecipeAmount(userName);
	}

	@Override
	public Long getRecipeBookAmount(String userName) {
		return userDAO.getRecipeBookAmount(userName);
	}

}
