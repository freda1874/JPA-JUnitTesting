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
public class TestCRUDStudent  extends JUnitBase {
	private EntityManager em;
	private EntityTransaction et;
	private static Student student; 
	
	
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
		deleteAllFrom(Student.class,em); 
		 long result = getTotalCount( em,Student.class);
		 assertThat(result, is(comparesEqualTo(0L)));

	}
	
	@Test
	@Order(2)
	void test02_Create() {
		et.begin();
		student = new Student();
		student.setFullName("Charlotte" , "Wright"); 
		em.persist(student);
		et.commit();
		long result = getCountWithId(em,Student.class,Integer.class,Student_.id,student.getId());

		  
		assertThat(result, is(greaterThanOrEqualTo(1L)));
	}

	@Test
	@Order(3)
	void test03_Read() {
 
		List<Student> students = getAll(em,Student.class);

		assertThat(students, contains(equalTo(student)));
	}

	@Test
	@Order(4)
	void test04_Update() {
		  
		Student returnedStudent = getWithId(em,Student.class,Integer.class,Student_.id,student.getId());

		String firstNmae = "Mary"; 
		String lastName = "Carter";

		et.begin();
		returnedStudent.setFirstName(firstNmae);
		returnedStudent.setLastName(lastName);
		em.merge(returnedStudent);
		et.commit();

		returnedStudent =getWithId(em,Student.class,Integer.class,Student_.id,student.getId());

		assertThat(returnedStudent.getFirstName(), equalTo(firstNmae));
		assertThat(returnedStudent.getLastName(), equalTo(lastName));
	}
	
	@Test
	@Order(5)
	void test05_Delete() {
		
		  
		Student returnedStudent = getWithId(em,Student.class,Integer.class,Student_.id,student.getId());
		et.begin(); 
		
		// Add another row to db to make sure only the correct row is deleted
		Student student2 = new Student();
		student2.setFullName("Mary", "Jane");
		em.persist(student2);
		et.commit();

		et.begin();
		em.remove(returnedStudent);
		et.commit();

		  
		long result = getCountWithId(em,Student.class,Integer.class,Student_.id,returnedStudent.getId());
		et.begin(); 
		assertThat(result, is(equalTo(0L)));
		
		 
		result =  getCountWithId(em,Student.class,Integer.class,Student_.id,student2.getId());
		assertThat(result, is(equalTo(1L)));
	}


}
