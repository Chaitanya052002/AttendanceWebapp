package com.example.loginapp.daorepo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.loginapp.model.UserMaster;

@Repository
public interface DaoRepo extends JpaRepository<UserMaster, Integer> {

	UserMaster findByUsername(String username);


	UserMaster findByEmail(String email);


	UserMaster getByEmail(String email);


	boolean existsByEmail(String email);


	List<UserMaster> findAllByRole(String string);

	@Modifying
	@Query("UPDATE UserMaster u SET u.loggedinstatus= :i WHERE u.phoneno = :phoneno")
	void updateloggedinstatus(@Param("i") boolean ii,@Param("phoneno")int phoneno);


	int findLoggedinstatusByEmail(String email);


}
