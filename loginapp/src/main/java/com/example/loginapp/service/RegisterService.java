package com.example.loginapp.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.loginapp.daorepo.DaoRepo;
import com.example.loginapp.model.Timings;
import com.example.loginapp.model.UserMaster;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import jakarta.websocket.Session;

@Transactional
@Service
public class RegisterService { 
//	public HttpSession session = request.getSession();

	@Autowired
	TimingDaoService td;
	
	@Autowired
	DaoRepo dbop;
	public UserMaster um;

	public void newuser(UserMaster user) {
		// TODO Auto-generated method stub
		dbop.saveAndFlush(user);
	}
	
	public String signinoutmapping() {
		if(um.isLoggedinstatus()!=true) {
			return "loggedin.html";
		}
		else {
			return "signout.html";
		}
	}

	public boolean checkcredentails(String email, String password) {
		// TODO Auto-generated method stub
		um = dbop.findByEmail(email);
		
		System.out.print(email + " " + password + " "+ um.getUsername());
		
		System.out.print(um.getUsername());
		
		
		
		//redirecting onbasis of credentails validatity
		if(um.getPassword().equals(password)) {
			System.out.print("Login successful");
			
			
//			Cookie ck = new Cookie("userid",um.getPhoneno().toString());
//			ck.setAttribute("id",um.getPhoneno().toString());
			//session.setAttribute("phoneno", um.getPhoneno() );
			return true;
		}else {
			return false;
		}
		
	}

	public void changeDbSigningStatustotrue() {
		// TODO Auto-generated method stub
		//setting loggeindstaus flag to true
		um.setLoggedinstatus(true);
		
		dbop.save(um);
	}

	@Transactional
	public void changeDbSigningStatustofalse() {
		// TODO Auto-generated method stub
		um.setLoggedinstatus(false);
		
		dbop.save(um);
	}

	
	@Transactional
	public List<Timings> gettimings() {
		// TODO Auto-generated method stub
		//Long phonneno= (Long) //session.getAttribute("phoneno");
		System.out.println("fetching data registerservice");
		List<Timings> td1 = um.getTimings();
		
		for(Timings ta : td1) {
			System.out.print(ta.getId());
			System.out.print(ta.getSigntime());
			System.out.print(ta.getSingoutime());
		}
		return um.getTimings();
		
	}
	
	
	
	public void addsigningtime() {
		// TODO Auto-generated method stub
		td.addsiginingtime(um);
	}

	public void addsignouttime() {
		// TODO Auto-generated method stub
		td.addsigouttime(um);
	}

	public List<UserMaster> getallusers() {
		// TODO Auto-generated method stub
		return dbop.findAll();
	}

	public void getsigntime() {
		// TODO Auto-generated method stub
		td.findbyphoneno(um.getPhoneno());
	}

	public boolean checkforalreadyexistingemail(UserMaster user) {
		// TODO Auto-generated method stub
		UserMaster temp;
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
		return dbop.findByEmail(email);
	}



	

}
