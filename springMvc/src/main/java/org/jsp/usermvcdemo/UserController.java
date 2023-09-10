package org.jsp.usermvcdemo;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

	@Autowired
	EntityManager manager;

	@RequestMapping(value = "/open")
	public String openView(@RequestParam String view) {
		return view;
	}

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ModelAndView saveUser(User u) {
		ModelAndView view = new ModelAndView();
		EntityTransaction transaction = manager.getTransaction();
		manager.persist(u);
		transaction.begin();
		transaction.commit();
		view.setViewName("print");
		view.addObject("msg", "User register with ID: " + u.getId());
		return view;
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ModelAndView updateUser(User u) {
		ModelAndView view = new ModelAndView();
		EntityTransaction transaction = manager.getTransaction();
		manager.merge(u);
		transaction.begin();
		transaction.commit();
		view.setViewName("print");
		view.addObject("msg", "User updated with ID: " + u.getId());
		return view;
	}

	@PostMapping(value = "/verify")
	public ModelAndView verifyUser(@RequestParam long phone, @RequestParam String password)
	{
		String qry="select u from User u where u.phone=?1 and u.password=?2";
		Query q=manager.createQuery(qry);
		q.setParameter(1, phone);
		q.setParameter(2, password);
		ModelAndView view =new ModelAndView();
	
	try {
		User u = (User) q.getSingleResult();
		view.setViewName("display");
		view.addObject("user", u);
		return view;
	} catch (NoResultException e) {
		view.setViewName("print");
		view.addObject("msg", "Invalid Phone Number or Password");
		return view;
	}
}
	@GetMapping(value = "/find")
	public ModelAndView findUser(@RequestParam int id) {
		User u = manager.find(User.class, id);
		ModelAndView view = new ModelAndView();
		if (u != null) {
			view.setViewName("display");
			view.addObject("user", u);
			return view;
		} else {
			view.setViewName("print");
			view.addObject("msg", "Invalid Id");
			return view;
		}
	}
	

	@GetMapping(value = "/delete")
	public ModelAndView deleteUser(@RequestParam int id) {
		User u = manager.find(User.class, id);
		ModelAndView view = new ModelAndView();
		EntityTransaction transaction = manager.getTransaction();
		if (u != null) {
			manager.remove(u);
			transaction.begin();
			transaction.commit();
			view.setViewName("print");
			view.addObject("msg", "user deleted");
			return view;
		} else {
			view.setViewName("print");
			view.addObject("msg", "Invalid Id,so cannot delete user");
			return view;
		}
	}
	
	
	
}
