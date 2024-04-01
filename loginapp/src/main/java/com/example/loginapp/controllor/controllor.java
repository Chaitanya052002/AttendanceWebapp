package com.example.loginapp.controllor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.loginapp.daorepo.DaoRepo;
import com.example.loginapp.model.Timings;
import com.example.loginapp.model.TimingsForUI;
import com.example.loginapp.model.UserMaster;
import com.example.loginapp.service.RegisterService;
import com.example.loginapp.service.TimingDaoService;
import com.example.loginapp.config.CustomUser;

import ch.qos.logback.core.model.Model;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/")
@Transactional
public class controllor {
	
	@Autowired
	HttpSession sessions;
	
	ModelAndView mv = new ModelAndView();
	ModelAndView modelAndView = new ModelAndView();
	@Autowired
	TimingDaoService tser;
	@Autowired
	RegisterService rs;
	
	
	
	boolean signinexists;
	
	
	
	@RequestMapping("/login")
	//@PreAuthorize("hasAuthority('ROLE_USER')")
	public String homepage() {
		return "home.html";
	}
	
	@RequestMapping("/register")
	//@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	//@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public String registerMapping() {
		return "register.html";
	}
	
	///{username}/{password}/{email}/{phoneno}"
	@PostMapping("/dbregistration")
	public ModelAndView dbregistration(@ModelAttribute UserMaster user,@RequestParam(value = "isadmin", required = false) boolean isAdmin) {
		System.out.print("		adminstatus:		"+isAdmin+"	log	");
		
		
		ModelAndView modelAndView = new ModelAndView();
	    
	    // Check if the email already exists
	    if (rs.checkforalreadyexistingemail(user)) {
	        if (isAdmin) {
	            user.setRole("ROLE_ADMIN");
	            modelAndView.addObject("successmessage", "Admin Registration was Successful");
	        } else {
	            user.setRole("ROLE_USER");
	            modelAndView.addObject("successmessage", "User Registration was Successful");
	        }
	        rs.newuser(user);
	    } else {
	        modelAndView.addObject("existserror", "Email already exists");
	    }

	    // Redirect to appropriate page based on registration status
	    modelAndView.setViewName("register.html"); // Change to the appropriate view name
	    return modelAndView;

	}
	
	@GetMapping("/loggedinuser")
	  public ModelAndView afterloggedindblogin() {
	      
		
		    
		    // Extract UserMaster object from CustomUser
			rs.buildum();
			System.out.print("             " + rs.getcurruser() + "             ");
		    UserMaster userMaster = rs.getcurruser();
		    String email = userMaster.getEmail();
		    String password = userMaster.getPassword();
		    signinexists = userMaster.isLoggedinstatus();
		    
		    modelAndView.addObject("email", email);
	      	modelAndView.addObject("username", rs.getcurruser().getUsername());
	      	modelAndView.addObject("phoneno", rs.getcurruser().getPhoneno());
	      	
		    
		    
				if(rs.checkcredentails(email,password).equals("adminlogin")) {
					modelAndView.setViewName("admin.html");
					sessions.setAttribute("user",rs.getcurruser());

				}
				else if(rs.checkcredentails(email, password).equals("correct")) {
	      	
	      	
	      	modelAndView.addObject("user",rs.getcurruser());
	          String page = rs.signinoutmapping();
	          modelAndView.setViewName(page);
	      } else if(rs.checkcredentails(email, password).equals("usernotfound")){
	      	modelAndView.setViewName("home.html");
	          modelAndView.addObject("error","User Not Found");
	      	
	      }
	      else{
	          // Password incorrect, return to the same view with error message
	          modelAndView.setViewName("home.html");
	          modelAndView.addObject("error", "Incorrect Password");
	      }
	      
	      return modelAndView;
	  }

	@RequestMapping("/mappingusers")
	@ResponseBody()
	public List<UserMaster> getusers(){
		return rs.getallusers();
	}
	
	@RequestMapping("/signin")
	public String signingdb() {
		return "singout.html";
	}
	
	
	@RequestMapping("signout")
	public String signoutdb() {
		//rs.changeDbSigningStatustofalse();
		//rs.addsignouttime();
		return "loggedin.html";
	}
	
	
	@Transactional
	@RequestMapping("/signinedit")
	public ModelAndView Signedinedit() {
		
		if(signinexists == false) {
		rs.changeDbSigningStatustotrue();
		rs.addsigningtime();
		signinexists = true;
		}
		
		
		modelAndView.setViewName("singout.html");

		return modelAndView;
	}
	
	
	
	//where problem begins
	@RequestMapping("/singoutedit")
	public ModelAndView SignedOutedit() {
		
		if(signinexists == true) {
			rs.changeDbSigningStatustofalse();
			rs.addsignouttime();
			signinexists = false;
		}
		
		modelAndView.setViewName("loggedin.html");
		return modelAndView;
	}
	

	
	@RequestMapping("/mappinguser/attendance/{phoneno}")
	@ResponseBody
	public List<TimingsForUI> findbysomevalue(@PathVariable() String phoneno) {
		
		List<Timings> ls = tser.findAllbyphoneno(Integer.parseInt(phoneno));
		List<TimingsForUI> toprint = new ArrayList<>();
		
		for(Timings timing : ls) {
			TimingsForUI t = new TimingsForUI();

	        t.setDate(timing.getDate());
	        t.setSigntime(timing.getSigntime());
	        if(timing.getSingoutime()==null) {
	        	t.setSingoutime("user  have not signed out yet");
	        }else {
	        	t.setSingoutime(timing.getSingoutime());
	        }

	        toprint.add(t);
		}
		System.out.print("--------------- " + " report data fetch Ended "+ " ---------------   ");
		return toprint;
	}
	
	//viewreportt
	@RequestMapping("/viewreportt")
	@ResponseBody
	public List<TimingsForUI> getbyId() {
		System.out.print("--------------- " + " report data fetch called"+ " ---------------   ");
		System.out.println("getting daintialized controllerrrr");
		
		UserMaster userforreport = rs.getbyemail(rs.getcurruser().getEmail());
		List<Timings> ls = tser.findAllbyphoneno(userforreport.getPhoneno());
		List<TimingsForUI> toprint = new ArrayList<>();
		
		for(Timings timing : ls) {
			TimingsForUI t = new TimingsForUI();

	        t.setDate(timing.getDate());
	        t.setSigntime(timing.getSigntime());
	        if(timing.getSingoutime()==null) {
	        	t.setSingoutime("You have not signed out yet");
	        }else {
	        	t.setSingoutime(timing.getSingoutime());
	        }

	        toprint.add(t);
		}
		System.out.print("--------------- " + " report data fetch Ended "+ " ---------------   ");
		return toprint;
		//return rs.gettimings(curruser.getPhoneno());
	}
	
	@RequestMapping("/viewreportui")
	public ModelAndView viwereportuii() {
		modelAndView.setViewName("viewreport.html");
		
		System.out.print("--------------- " + "view report called "+ " ---------------   ");
		return modelAndView;
	}
	
	@RequestMapping("/admin")
	public String adminresturn() {
		return "admin.html";
	}
	
	@RequestMapping("/failedauthentication")
	public String failureurl() {
		return "redirect:/login?error";
	}
	
}
