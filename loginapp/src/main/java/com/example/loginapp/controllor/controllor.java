package com.example.loginapp.controllor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.loginapp.daorepo.DaoRepo;
import com.example.loginapp.model.Timings;
import com.example.loginapp.model.UserMaster;
import com.example.loginapp.service.RegisterService;
import com.example.loginapp.service.TimingDaoService;

import ch.qos.logback.core.model.Model;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/")
@Transactional
public class controllor {
	
	
	ModelAndView mv = new ModelAndView();
	ModelAndView modelAndView = new ModelAndView();
	@Autowired
	TimingDaoService tser;
	@Autowired
	RegisterService rs;
	
	
	@RequestMapping("home")
	public String homepage() {
		return "home.html";
	}
	
	@RequestMapping("register")
	public String registerMapping() {
		return "register.html";
	}
	
	///{username}/{password}/{email}/{phoneno}"
	@RequestMapping("dbregistration")
	public ModelAndView dbregistration(@ModelAttribute UserMaster user) {
		if(rs.checkforalreadyexistingemail(user)){
			rs.newuser(user);
			mv.setViewName("home.html");
		}else {
			mv.setViewName("register.html");
			mv.addObject("existserror","email already exists");
		}
		
		return mv;
	}
	
//	@RequestMapping("dbloggedin")
//	public String dblogin(@RequestParam(name="gmail") String email, @RequestParam("password") String password) {
//		
//		if(email.equals("admin")&& password.equals("admin")) {
//			return "admin.html";
//		}
//		if(rs.checkcredentails(email, password)) {
////			rs.changeDbSigningStatustotrue();
////			rs.addsigningtime();
//			String page = rs.signinoutmapping();
//			return page;
//
//		}else {
//
//			//string to be fetched by html javascript page and displayed
//			//if password correct automticaly will be redirected to page string which is html page
//			
//			return "home.html";
//		}
//		
//	}
	
	
	@RequestMapping("dbloggedin")
    public ModelAndView dblogin(@RequestParam(name="gmail") String email, @RequestParam("password") String password) {
        
        
        if(email.equals("admin") && password.equals("admin")) {
            modelAndView.setViewName("admin.html");
        } else if(rs.checkcredentails(email, password)) {
        	
        	UserMaster us = rs.getbyemail(email);
        	
        	System.out.println(us.getEmail() + us.getPhoneno() + us.getUsername());
        	mv.addObject(us);
        	mv.addObject("email", email);
        	mv.addObject("username", us.getUsername());
        	mv.addObject("phoneno", us.getPhoneno());
        	modelAndView.addObject("email", email);
        	modelAndView.addObject("username", us.getUsername());
        	modelAndView.addObject("phoneno", us.getPhoneno());

    
        	modelAndView.addObject("user",us);
            String page = rs.signinoutmapping();
            modelAndView.setViewName(page);
        } else {
            // Password incorrect, return to the same view with error message
            modelAndView.setViewName("home.html");
            modelAndView.addObject("error", "Incorrect Credentials");
        }
        
        return modelAndView;
    }
	
	@RequestMapping("/mappingusers")
	@ResponseBody()
	public List<UserMaster> getusers(){
		return rs.getallusers();
	}
	
	@RequestMapping("signin")
	public String signingdb() {
		rs.changeDbSigningStatustotrue();
		rs.addsigningtime();
		
		return "signout.html";
	}
	
	@RequestMapping("signout")
	public String signoutdb() {
		rs.changeDbSigningStatustofalse();
		rs.addsignouttime();
		return "home.html";
	}
	
	
	
	@RequestMapping("signinedit")
	public String Signedinedit() {
		rs.addsigningtime();
		//rs.getsigntime();
		return "signout.html";
	}
	
	@RequestMapping("/signoutedit")
	public ModelAndView SignedOutedit() {
		rs.addsignouttime();
		modelAndView.setViewName("signout.html");
		return modelAndView;
	}
	

	
	@RequestMapping("/mappinguser/attendance/{phoneno}")
	@ResponseBody
	public List<Timings> findbysomevalue(@PathVariable() String phoneno) {
		return tser.findbyphoneno(Integer.parseInt(phoneno));
	}
	
	//viewreportt
	@RequestMapping(value="/viewreportt")
	@ResponseBody
	public List<Timings> getbyId() {
		System.out.println("getting data intialized controller");
		return rs.gettimings();
	}
	
	@RequestMapping(value="/viewreportui")
	public String viwereportuii() {
		return "viewreport.html";
	}
	
	
	
}
