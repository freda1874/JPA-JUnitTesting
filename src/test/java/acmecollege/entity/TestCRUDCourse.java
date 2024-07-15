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
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root; 

import org.junit.jupiter.api.AfterEach; 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import common.JUnitBase;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestCRUDCourse  extends JUnitBase {
	private EntityManager em;
	private EntityTransaction et;
	private static Course course; 
	
	
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
		deleteAllFrom(Course.class,em); 
		 long result = getTotalCount( em,Course.class);
		 assertThat(result, is(comparesEqualTo(0L)));

	}
	
	@Test
	@Order(2)
	void test02_Create() {
		et.begin();
		course = new Course();
		course.setCourse("CST8005", "Biology", 2024, "Fall", 2,  (byte) 0);
	 
		em.persist(course);
		et.commit();
		
		long result = getCountWithId(em,Course.class,Integer.class,Course_.id,course.getId());

		  
		assertThat(result, is(greaterThanOrEqualTo(1L)));
	}
 

	@Test
	@Order(3)
	void test03_Read() {
 
		List<Course> courses = getAll(em,Course.class);

		assertThat(courses, contains(equalTo(course)));
	}

	@Test
	@Order(4)
	void test04_Update() {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Course> query = builder.createQuery(Course.class);
		Root<Course> root = query.from(Course.class);
		query.select(root);
		
		query.where(builder.equal(root.get(Course_.id), builder.parameter(Integer.class, "id")));
		TypedQuery<Course> tq = em.createQuery(query);
		tq.setParameter("id", course.getId());
		Course returnedCourse = tq.getSingleResult();

		String code = "CST8116"; 
		String title = "Introduction to Computer Programming";
		int year = 2023;
		String semester="Winter";
		int credit= 4;
		 byte online = (byte)1;
		  

		et.begin();
		returnedCourse.setCourse(code, title, year, semester, credit, online);

		em.merge(returnedCourse);
		et.commit();

		returnedCourse = tq.getSingleResult();

		assertThat(returnedCourse.getCourseCode(), equalTo(code));
		assertThat(returnedCourse.getCourseTitle(), equalTo(title));
		assertThat(returnedCourse.getYear(), equalTo(year));
		assertThat(returnedCourse.getSemester(), equalTo(semester));
		assertThat(returnedCourse.getCreditUnits(), equalTo(credit));
		assertThat(returnedCourse.getOnline(), equalTo(online));
		
	}
	
	@Test
	@Order(5)
	void test05_Delete() {
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Course> query = builder.createQuery(Course.class);
	 
		Root<Course> root = query.from(Course.class);
		query.select(root);
		query.where(builder.equal(root.get(Course_.id), builder.parameter(Integer.class, "id")));
		TypedQuery<Course> tq = em.createQuery(query);
		tq.setParameter("id", course.getId());
		Course returnedCourse = tq.getSingleResult();

		et.begin(); 
		
		// Add another row to db to make sure only the correct row is deleted
		Course course2 = new Course();
		course2.setCourse("CST8106", "Electrical Engineering", 2021, "WINTER", 3, (byte) 0);
		
		em.persist(course2);
		et.commit();

		et.begin();
		em.remove(returnedCourse);
		et.commit();

		CriteriaQuery<Long> query2 = builder.createQuery(Long.class);	
		Root<Course> root2 = query2.from(Course.class);
		query2.select(builder.count(root2));
		query2.where(builder.equal(root2.get(Course_.id), builder.parameter(Integer.class, "id")));
		TypedQuery<Long> tq2 = em.createQuery(query2);
		tq2.setParameter("id", returnedCourse.getId());
		long result = tq2.getSingleResult();
		assertThat(result, is(equalTo(0L)));
		
		TypedQuery<Long> tq3 = em.createQuery(query2);
		tq3.setParameter("id", course2.getId());
		result = tq3.getSingleResult();
		assertThat(result, is(equalTo(1L)));
	}


}
