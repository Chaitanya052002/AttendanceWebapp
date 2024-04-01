package com.example.loginapp.service;



import java.util.Collection;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.loginapp.config.CustomUser;
import com.example.loginapp.daorepo.DaoRepo;
import com.example.loginapp.model.Timings;
import com.example.loginapp.model.UserMaster;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;


@Transactional
@Service
public class RegisterService { 
//	public HttpSession session = request.getSession();

	UserMaster temp;
	static boolean usernotfound; 
	
	@Autowired
	TimingDaoService td;
	
	@Autowired
	DaoRepo dbop;
	
	
	 @Autowired
	    PasswordEncoder passwordEncoder;
	 
	 public UserMaster um;
	    
	    

	public void newuser(UserMaster user) {
		// TODO Auto-generated method stub
		user.setLoggedinstatus(false);
		// i want to set encrypyted password here
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		dbop.saveAndFlush(user);
	}
	
	public String signinoutmapping() {
		UserMaster u = dbop.getById(um.getPhoneno());
		System.out.print(usernotfound);
		boolean temp;
		
		if(u.isLoggedinstatus()) {
			return "singout.html";
		}
		else {
			return "loggedin.html";
		}
	}

	public String checkcredentails(String email, String password) {
		
		// TODO Auto-generated method stub
		System.out.print("                                                                                            ");
 /// chatgpt this is not returning null why
		
//		System.out.print(email + " " + password + " "+ um.getUsername() + " " + um.getPhoneno());
//		
//		System.out.print(um.getUsername());
		//incase user is not registered
		if(um==null) {
			return "usernotfound";
		}
		
		//admin role check and password
		if(um.getRole().equals("ROLE_ADMIN") && um.getPassword().equals(password)) {
			return "adminlogin";
		}
		
		
		//redirecting onbasis of credentails validity
		if(um.getPassword().equals(password)) {
			System.out.print("Login successful");
			
			
//			Cookie ck = new Cookie("userid",um.getPhoneno().toString());
//			ck.setAttribute("id",um.getPhoneno().toString());
			//session.setAttribute("phoneno", um.getPhoneno() );
			return "correct";
		}else {
			return "incorrect";
		}
		
	}

	@Transactional
	public void changeDbSigningStatustotrue() {
		// TODO Auto-generated method stub
		//setting loggeindstaus flag to true
		//um.setLoggedinstatus(true);
		System.out.print("     "+ "  inside signing db status to true" +"     ");
		System.out.print("Inside db status ");
		dbop.updateloggedinstatus(true,um.getPhoneno());
		//dbop.save(um);
	}

	@Transactional
	public void changeDbSigningStatustofalse() {
		// TODO Auto-generated method stub
		//um.setLoggedinstatus(false);
		System.out.print("Inside db status to false");
		dbop.updateloggedinstatus(false,um.getPhoneno());
		
		//dbop.save(um);
	}

	
//	@Transactional
//	public List<Timings> gettimings() throws HibernateException {
//		// TODO Auto-generated method stub
//		//Long phonneno= (Long) //session.getAttribute("phoneno");
//		    
//		    // Perform your database operations here
//		    System.out.println("fetching data registerservice");
//			List<Timings> td1 = um.getTimings();
//			
//			for(Timings ta : td1) {
//				System.out.print(ta.getId());
//				System.out.print(ta.getSigntime());
//				System.out.print(ta.getSingoutime());
//			}
//		    // Commit the transaction
//		    
//		
//		return um.getTimings();
//		
//	}
	
	
	
	
	
	@Transactional
	public void addsigningtime() {
		// TODO Auto-generated method stub
		td.addsiginingtime(um);
	}

	public void addsignouttime() {
		// TODO Auto-generated method stub
		int useridtoupdatetime = td.findidtoupdatee(um.getPhoneno());
		System.out.print("                "+ useridtoupdatetime+ "             ");
		td.updateLastSignOutTime(useridtoupdatetime);
		//td.addsigouttime(um);
	}
	
	public List<UserMaster> getallusers() {
		// TODO Auto-generated method stub
		return dbop.findAllByRole("ROLE_USER");
	}

//	public void getsigntime() {
//		// TODO Auto-generated method stub
//		td.findbyphoneno(um.getPhoneno());
//	}

	public boolean checkforalreadyexistingemail(UserMaster user) {
		// TODO Auto-generated method stub
		 
		temp = dbop.findByEmail(user.getEmail());
		if(temp==null) {
			System.out.println("can add");
			return true;
			}else {
			System.out.println("can not add");
			return false;
		}
	}

	public UserMaster getbyemail(String email) {
		// TODO Auto-generated method stub
		UserMaster temp;
		temp = dbop.findByEmail(email);
		System.out.print("	"+temp+"    ");
		
		System.out.println("curruser one feching"+ temp.getPhoneno());
		return temp;
	}

	public List<Timings> gettimings(int phoneno) {
		// TODO Auto-generated method stub
		List<Timings> ls = td.findAllbyphoneno(phoneno);
		return null;
	}

	public UserMaster getcurruser() {
		// TODO Auto-generated method stub
		return um;
	}

	public void buildum() {
		// TODO Auto-generated method stub
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		 String username = authentication.getName();
		 System.out.print("                 "+username+ "                 ");
		 um = dbop.findByEmail(username);
	}

	

}
