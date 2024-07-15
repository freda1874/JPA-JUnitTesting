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
public class TestCRUDMembershipCard extends JUnitBase {
	private EntityManager em;
	private EntityTransaction et;
	private static Student student;
	private static AcademicStudentClub studentClub;
	private static MembershipCard card;
	private static ClubMembership clubMembership;
	private static DurationAndStatus durationAndStatus;
	private static final boolean signed = true;

	@BeforeAll
	static void setupAllInit() {
		LocalDateTime startDate = LocalDateTime.of(2024, 7, 15, 9, 0);
		LocalDateTime endDate = LocalDateTime.of(2024, 7, 15, 17, 0);
		String active = "+";

		student = new Student();
		student.setFullName("Michael", "Brown");

		studentClub = new AcademicStudentClub();
		studentClub.setName("Photography Club");

		durationAndStatus = new DurationAndStatus();
		durationAndStatus.setDurationAndStatus(startDate, endDate, active);

		clubMembership = new ClubMembership();
		clubMembership.setDurationAndStatus(durationAndStatus);
		clubMembership.setStudentClub(studentClub);
		
		card = new MembershipCard();
		card.setOwner(student);
		card.setSigned(signed);
		card.setClubMembership(clubMembership);

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
		deleteAllFrom(MembershipCard.class, em);
		long result = getTotalCount(em, MembershipCard.class);
		assertThat(result, is(comparesEqualTo(0L)));

	}

	@Test
	@Order(2)
	void test02_Create() {
		et.begin();
		em.persist(student); 
		em.persist(studentClub);
 
	 
		  
		em.persist(clubMembership);
		em.persist(card);

		et.commit();
		long result = getCountWithId(em, MembershipCard.class, Integer.class, MembershipCard_.id,
				card.getId());

		assertThat(result, is(greaterThanOrEqualTo(1L)));

	}

	@Test
	@Order(3)
	void test03_Read() {

		List<MembershipCard> cards = getAll(em, MembershipCard.class);

		assertThat(cards, contains(equalTo(card)));
	}

	@Test
	@Order(4)
	void test04_ReadDependencies() {

		MembershipCard returnedcard = getWithId(em, MembershipCard.class, Integer.class, MembershipCard_.id,
				card.getId());

		assertThat(returnedcard.getClubMembership().getId(), equalTo(clubMembership.getId()));
		assertThat(returnedcard.getOwner().getId(), equalTo(student.getId())); 
	}

	@Test
	@Order(5)
	void test05_UpdateDependencies() {

		MembershipCard returnedcard = getWithId(em, MembershipCard.class, Integer.class, MembershipCard_.id,
				card.getId());

		  clubMembership = returnedcard.getClubMembership();
		durationAndStatus = clubMembership.getDurationAndStatus();

		LocalDateTime startDate = LocalDateTime.of(2024, 7, 15, 9, 0);
		LocalDateTime endDate = LocalDateTime.of(2024, 8, 12, 17, 0);
		String active = "+";

		durationAndStatus.setDurationAndStatus(startDate, endDate, active);
		clubMembership.setDurationAndStatus(durationAndStatus);

		studentClub = (AcademicStudentClub) clubMembership.getStudentClub();
		studentClub.setName("Debate Club");
		clubMembership.setStudentClub(studentClub);

student = card.getOwner();
student.setFullName("Leo", "Ding");

		et.begin();
		returnedcard.setClubMembership(clubMembership);
		returnedcard.setOwner(student);

		em.merge(returnedcard);
		et.commit();

		returnedcard = getWithId(em, MembershipCard.class, Integer.class, MembershipCard_.id,
				returnedcard.getId());

		assertThat(returnedcard.getClubMembership().getId(), equalTo(clubMembership.getId()));
		assertThat(returnedcard.getOwner().getId(), equalTo(student.getId()));
	}

	@Test
	@Order(6)
	void test06_Delete() {

		et.begin();

		// Add another row to db to make sure only the correct row is deleted
		MembershipCard card2 = new MembershipCard();

		StudentClub studentClub2 = new AcademicStudentClub();
		studentClub2.setName("Dance Club");
		em.persist(studentClub2);

		ClubMembership club = new ClubMembership();
		club.setStudentClub(studentClub2);

		LocalDateTime startDate = LocalDateTime.of(2024, 7, 15, 9, 0);
		LocalDateTime endDate = LocalDateTime.of(2024, 7, 18, 17, 0);
		String active = "+";
		DurationAndStatus durationAndStatus2 = new DurationAndStatus();
		durationAndStatus2.setDurationAndStatus(startDate, endDate, active);
club.setDurationAndStatus(durationAndStatus2);

em.persist(club);

		Student s2 = new Student();
		s2.setFullName("Janie", "Brownie"); 
		em.persist(s2);
 
 
		card2.setOwner(s2);
		card2.setSigned(false);
		card2.setClubMembership(club);
		em.persist(card2);
 
		card = getWithId(em, MembershipCard.class, Integer.class, MembershipCard_.id, card.getId());
		em.remove(card);
		et.commit();

		long result = getCountWithId(em, MembershipCard.class, Integer.class, MembershipCard_.id, card.getId());
		assertThat(result, is(equalTo(0L)));

		result = getCountWithId(em, MembershipCard.class, Integer.class, MembershipCard_.id, card2.getId());
		assertThat(result, is(equalTo(1L)));
	}

}
