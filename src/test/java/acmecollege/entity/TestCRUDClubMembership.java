package acmecollege.entity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction; 

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import common.JUnitBase;

@TestMethodOrder(MethodOrderer.MethodName.class)
public class TestCRUDClubMembership  extends JUnitBase {
	private EntityManager em;
	private EntityTransaction et;
	private static Student student;
	private static AcademicStudentClub  studentClub;
	private static ClubMembership clubMembership;
	private static DurationAndStatus durationAndStatus; 
	private static final  boolean signed = true;
	
	private static MembershipCard card; 
	
	@BeforeAll
	static void setupAllInit() {
		LocalDateTime now = LocalDateTime.now();
		student = new Student();
		student.setFullName("Michael" , "Brown"); 

		studentClub = new AcademicStudentClub();
		studentClub.setName("Photography Club");

		durationAndStatus=new DurationAndStatus();
		durationAndStatus.setDurationAndStatus(now, now, "+");
		
		 clubMembership = new ClubMembership();
		 clubMembership.setStudentClub(studentClub);
		 clubMembership.setDurationAndStatus(durationAndStatus);
	}

	
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
		deleteAllFrom(MembershipCard.class,em); 
		 long result = getTotalCount( em,MembershipCard.class);
		 assertThat(result, is(comparesEqualTo(0L)));

	}
	
	@Test
	@Order(2)
	void test02_Create() {
		et.begin();
		em.persist(student);
		em.persist(clubMembership);
		
		card = new MembershipCard();
		card.setOwner(student);
		card.setClubMembership(clubMembership);
		card.setSigned(signed);
		em.persist(card);
		
		et.commit();
		long result = getCountWithId(em,MembershipCard.class,Integer.class,MembershipCard_.id,card.getId());

		  
		assertThat(result, is(greaterThanOrEqualTo(1L)));
 

	}

	@Test
	@Order(3)
	void test03_Read() {
 
		List<MembershipCard> cards = getAll(em,MembershipCard.class);

		assertThat(cards, contains(equalTo(card)));
	}
	
	@Test
	@Order(4)
	void test04_ReadDependencies() {
 
	 
		MembershipCard returnedMembershipCard = getWithId(em, MembershipCard.class, Integer.class,MembershipCard_.id,card.getId());
				

		assertThat(returnedMembershipCard.getOwner(), equalTo(student));
		assertThat(returnedMembershipCard.getClubMembership().getId(), equalTo(clubMembership.getId()));
	}

	
	

	@Test
	@Order(5)
	void test05_Update() {
		 
		MembershipCard returnedMembershipCard =   getWithId( em, MembershipCard.class, Integer.class,MembershipCard_.id,card.getId());

		boolean code = false; 
		byte b = 0b0000;
		 

		et.begin();
		returnedMembershipCard.setSigned(code);
		em.merge(returnedMembershipCard);
		et.commit();

		returnedMembershipCard = getWithId(  em, MembershipCard.class, Integer.class,MembershipCard_.id,card.getId());

		assertThat(returnedMembershipCard.getSigned(), equalTo(b)); 
		
	}
	
	@Test
	@Order(6)
	void test06_UpdateDependencies() {
		 


		MembershipCard returnedMembershipCard = getWithId(  em, MembershipCard.class, Integer.class,MembershipCard_.id,card.getId());
		student = returnedMembershipCard.getOwner();
		student.setFullName("Mike" , "Brown"); 
	 

		clubMembership =  returnedMembershipCard.getClubMembership();
		durationAndStatus = clubMembership.getDurationAndStatus();
		
		LocalDateTime startDate = LocalDateTime.of(2024, 7, 15, 9, 0);
		LocalDateTime endDate = LocalDateTime.of(2024, 7, 15, 17, 0);
		String active = "+";
		
		durationAndStatus.setDurationAndStatus(startDate, endDate, active);
		clubMembership.setDurationAndStatus(durationAndStatus);
 

		et.begin();
		returnedMembershipCard.setOwner(student);
		returnedMembershipCard.setClubMembership(clubMembership);
	 
		em.merge(returnedMembershipCard);
		et.commit();

		returnedMembershipCard = getWithId(  em, MembershipCard.class, Integer.class,MembershipCard_.id,returnedMembershipCard.getId());

		assertThat(returnedMembershipCard.getOwner(), equalTo(student));
		assertThat(returnedMembershipCard.getClubMembership(), equalTo(clubMembership)); 
	}

	
	@Test
	@Order(7)
	void test07_Delete() {
		
	 
		MembershipCard returnedMembershipCard = getWithId(em, MembershipCard.class, Integer.class,MembershipCard_.id, card.getId());

		et.begin(); 
		
		// Add another row to db to make sure only the correct row is deleted
		MembershipCard card2 = new MembershipCard();
	 
		Student student2 = new Student();
		student2.setFullName("Aruo", "Davis");
	 
		em.persist(student2);
		StudentClub studentClub2 = new AcademicStudentClub();
		studentClub2.setName("Fish Study Club");
em.persist(studentClub2);
		LocalDateTime startDate = LocalDateTime.of(2024, 7, 15, 9, 0);
		LocalDateTime endDate = LocalDateTime.of(2024, 8, 15, 17, 0);
		 
		DurationAndStatus durationAndStatus2=new DurationAndStatus();
		durationAndStatus2.setDurationAndStatus(startDate, endDate, "+");
	 
		
		ClubMembership clubMembership2 = new ClubMembership();
		clubMembership2.setStudentClub(studentClub2);
		clubMembership2.setDurationAndStatus(durationAndStatus2);
		  em.persist(clubMembership2);
		  
		card2.setClubMembership(clubMembership2);
		card2.setOwner(student2);
		
		
		em.persist(card2);
		et.commit();

		et.begin();
		em.remove(returnedMembershipCard);
		et.commit();

		long result = getCountWithId(  em, MembershipCard.class, Integer.class,MembershipCard_.id,returnedMembershipCard.getId());
		assertThat(result, is(equalTo(0L)));
		
		 
		result = getCountWithId( em, MembershipCard.class, Integer.class,MembershipCard_.id,card2.getId());
		assertThat(result, is(equalTo(1L)));
	}


}
