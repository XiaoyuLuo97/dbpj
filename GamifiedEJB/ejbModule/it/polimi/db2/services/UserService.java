package it.polimi.db2.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.NonUniqueResultException;

import it.polimi.db2.exceptions.UpdateProfileException;
import it.polimi.db2.entities.User;
import it.polimi.db2.exceptions.*;
import java.util.List;

@Stateless
public class UserService {
	@PersistenceContext(unitName = "GamifiedEJB")
	private EntityManager em;

	public UserService() {
	}

	public User checkCredentials(String usrn, String pwd) throws NonUniqueResultException {
		List<User> uList = null;
		try {
			uList = em.createNamedQuery("User.checkCredentials", User.class).setParameter(1, usrn).setParameter(2, pwd)
					.getResultList();
		} catch (PersistenceException e) {
			System.out.print("Could not verify credentals");
		}
		if (uList.isEmpty())
			return null;
		else if (uList.size() == 1)
			return uList.get(0);
		throw new NonUniqueResultException("More than one user registered with same credentials");

	}
	public void registerNewUser(String username,String pwd,String email) throws InvalidInsert{

		User user = null;
		user = em.createNamedQuery("User.checkUsername", User.class).setParameter(1, username).getSingleResult();

		if(user != null){
			throw new InvalidInsert("User name already exist");
		}
		else{
			user.setEmail(email);
			user.setUsername(username);
			user.setPassword(pwd);
			user.setPoints(0);
			this.em.persist(user);
			this.em.flush();
		}

	}
	public void updateProfile(User u) throws UpdateProfileException {
		try {
			em.merge(u);
		} catch (PersistenceException e) {
			throw new UpdateProfileException("Could not change profile");
		}
	}
}
