package com.example.loginapp.daorepo;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.loginapp.model.Timings;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


@Repository
public interface TimingDao extends JpaRepository<Timings,Integer>{
	
	 
	//List<Timings> findAllByUser(@Param("phoneNo") int phoneNo);
	@Query("SELECT t FROM Timings t JOIN t.user u WHERE u.phoneno = :phoneNo")
	List<Timings> findAllTimingsByPhoneNo(@Param("phoneNo") int phoneNo);

	@Modifying
	@Query("UPDATE Timings t SET t.Singoutime = :newtime WHERE t.user.phoneno = :phoneNo")
	void updatesignouttime(@Param("newtime") String newValue, @Param("phoneNo") int phoneNo);

	

//	@Modifying
//	@Query("SELECT *  FROM Timings where phoneno=:phoneno")
//	Timings getByPhoneno(@Param("phoneno")int phoneno);
//	
//	@Modifying
//	@Query("DELETE FROM Timings where phoneno=:phoneno")
//	void deleteByPhoneno(@Param("phoneno")int phoneno);
//
//	
	
//	@Modifying
//	@Query("update Timings t set t.Singouttime =: time where t.phoneno  =:phoneno")
//	void updatebyid(String time, int phoneno);
}
