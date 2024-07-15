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
public class TestCRUDPeerTutor  extends JUnitBase {
	private EntityManager em;
	private EntityTransaction et;
	private static PeerTutor peerTutor; 
	
	
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
		deleteAllFrom(PeerTutor.class,em); 
		 long result = getTotalCount( em,PeerTutor.class);
		 assertThat(result, is(comparesEqualTo(0L)));

	}
	
	@Test
	@Order(2)
	void test02_Create() {
		et.begin();
		peerTutor = new PeerTutor();
		peerTutor.setPeerTutor("Emily", "Johnson", "Electrical Engineering");
		em.persist(peerTutor);
		et.commit();
		
		long result = getCountWithId(em,PeerTutor.class,Integer.class,PeerTutor_.id,peerTutor.getId());

		  
		assertThat(result, is(greaterThanOrEqualTo(1L)));
	}

	@Test
	@Order(3)
	void test03_Read() {
 
		List<PeerTutor> peerTutors = getAll(em,PeerTutor.class);

		assertThat(peerTutors, contains(equalTo(peerTutor)));
	}

	@Test
	@Order(4)
	void test04_Update() {
		 
		PeerTutor returnedPeerTutor =  getWithId(em, PeerTutor.class, Integer.class,PeerTutor_.id, peerTutor.getId());

		String firstNmae = "Mary"; 
		String lastName = "Carter";
		String  program  = "Business Administration";

		et.begin();
		returnedPeerTutor.setFirstName(firstNmae);
		returnedPeerTutor.setLastName(lastName);
		returnedPeerTutor.setProgram(program);
		em.merge(returnedPeerTutor);
		et.commit();

		returnedPeerTutor = getWithId(em, PeerTutor.class, Integer.class,PeerTutor_.id, peerTutor.getId());

		assertThat(returnedPeerTutor.getFirstName(), equalTo(firstNmae));
		assertThat(returnedPeerTutor.getLastName(), equalTo(lastName));
		assertThat(returnedPeerTutor.getProgram(), equalTo(program));
	}
	
	@Test
	@Order(5)
	void test05_Delete() {
		
		 
		PeerTutor returnedPeerTutor =  getWithId(em, PeerTutor.class, Integer.class,PeerTutor_.id, peerTutor.getId());

		et.begin(); 
		
		// Add another row to db to make sure only the correct row is deleted
		PeerTutor peerTutor2 = new PeerTutor();
		peerTutor2.setPeerTutor("Michael", "Brown", "Psychology");
		em.persist(peerTutor2);
		et.commit();

		et.begin();
		em.remove(returnedPeerTutor);
		et.commit();

		 
	 
		long result =   getCountWithId(em, PeerTutor.class, Integer.class,PeerTutor_.id, returnedPeerTutor.getId());

		assertThat(result, is(equalTo(0L)));
	 
		result = getCountWithId(em, PeerTutor.class, Integer.class,PeerTutor_.id, peerTutor2.getId());
		assertThat(result, is(equalTo(1L)));
	}


}
