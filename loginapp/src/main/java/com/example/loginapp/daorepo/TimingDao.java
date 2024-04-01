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

//	@Modifying
//	@Query("UPDATE Timings t SET t.Singoutime = :newtime WHERE t.id = :(SELECT MAX(id) FROM Timings WHERE phoneno =: phoneNo)")
//	void updatesignouttime(@Param("newtime") String newValue, @Param("phoneNo") int phoneNo);

//	@Modifying
//	@Query("UPDATE Timings t SET t.Singoutime = :newtime WHERE t.id = (SELECT MAX(t2.id) FROM Timings t2 WHERE t2.user.phoneno = :phoneNo)")
//	void updatesignouttime(@Param("newtime") String newValue, @Param("phoneNo") int phoneNo);

	@Modifying
	@Query("UPDATE Timings t SET t.Singoutime = :newTime WHERE t.user.phoneno = :phoneNo")
	void updatesignouttime(@Param("newTime") String newTime, @Param("phoneNo") int phoneNo);

	@Query("SELECT t FROM Timings t JOIN t.user u WHERE u.phoneno = :phoneNumber")
    List<Timings> findAllByUserPhoneno(@Param("phoneNumber") int phoneNumber);

	
	@Query("SELECT MAX(id) FROM Timings t where t.user.phoneno = :phoneNumber ")
	int findidtoupdate(@Param("phoneNumber") int phoneNumber);

		//updates using id only latest record
	@Modifying
	@Query("UPDATE Timings t SET t.Singoutime = :newTime WHERE t.id = :idd")
	void updatewithid(@Param("idd") int timingtouupdateid,@Param("newTime") String newTime);
	

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
