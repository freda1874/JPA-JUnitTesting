package acmecollege.entity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is; 

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction; 

import org.junit.jupiter.api.AfterEach; 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import common.JUnitBase;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestCRUDNonAcademicStudentClub  extends JUnitBase {
	private EntityManager em;
	private EntityTransaction et;
	private static NonAcademicStudentClub studentClub; 
	
	
	@BeforeEach
	void setup() {
		em = getEntityManager();
		et = em.getTransaction();
		
	}
	
	@AfterEach
	void tearDown() {
		em.close();
	}

	@Test
	@Order(1)
	void test01_Empty() {
		deleteAllFrom(NonAcademicStudentClub.class,em); 
		 long result = getTotalCount( em,NonAcademicStudentClub.class);
		 assertThat(result, is(comparesEqualTo(0L)));

	}
	
	@Test
	@Order(2)
	void test02_Create() {
		et.begin();
		studentClub = new NonAcademicStudentClub();
		studentClub.setName("Photography Club");
		em.persist(studentClub);
		et.commit();
		
		long result = getCountWithId(em,NonAcademicStudentClub.class,Integer.class,NonAcademicStudentClub_.id,studentClub.getId());

		  
		assertThat(result, is(greaterThanOrEqualTo(1L)));
	}

	@Test
	@Order(3)
	void test03_Read() {
 
		List<NonAcademicStudentClub> studentClubs = getAll(em,NonAcademicStudentClub.class);

		assertThat(studentClubs, contains(equalTo(studentClub)));
	}

	@Test
	@Order(4)
	void test04_Update() {
		 
	
		NonAcademicStudentClub returnedNonAcademicStudentClub= getWithId(em,NonAcademicStudentClub.class,Integer.class,NonAcademicStudentClub_.id,studentClub.getId());

		String name = "Debate Club";  

		et.begin();
		returnedNonAcademicStudentClub.setName(name);
		em.merge(returnedNonAcademicStudentClub);
		et.commit();

		returnedNonAcademicStudentClub = getWithId(em,NonAcademicStudentClub.class,Integer.class,NonAcademicStudentClub_.id,studentClub.getId());

		assertThat(returnedNonAcademicStudentClub.getName(), equalTo(name)); 
	}
	
	@Test
	@Order(5)
	void test05_Delete() {
		
		 
		 
		NonAcademicStudentClub returnedNonAcademicStudentClub =getWithId(em,NonAcademicStudentClub.class,Integer.class,NonAcademicStudentClub_.id,studentClub.getId());

		et.begin(); 
		
		// Add another row to db to make sure only the correct row is deleted
		NonAcademicStudentClub studentClub2 = new NonAcademicStudentClub();
		studentClub2.setName("Environmental Club");
		em.persist(studentClub2);
		et.commit();

		et.begin();
		em.remove(returnedNonAcademicStudentClub);
		et.commit();
 
 
		long result = getCountWithId(em,NonAcademicStudentClub.class,Integer.class,NonAcademicStudentClub_.id,returnedNonAcademicStudentClub.getId());
		assertThat(result, is(equalTo(0L)));
	 
		result =  getCountWithId(em,NonAcademicStudentClub.class,Integer.class,NonAcademicStudentClub_.id,studentClub2.getId());
		assertThat(result, is(equalTo(1L)));
	}


}
