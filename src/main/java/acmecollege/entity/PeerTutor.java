/********************************************************************************************************2*4*w*
 * File:  PeerTutor.java Course materials CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author Lei Luo
 * 
 */
package acmecollege.entity;

import java.io.Serializable;
import java.util.HashSet; 
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType; 
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;



/**
 * The persistent class for the peer tutor database table.
 */

@Entity
@Table(name = "peer_tutor")
//Hint - @NamedQuery attached to this class which uses JPQL/HQL.  SQL cannot be used with NamedQuery.
//Hint - @NamedQuery uses the name which is defined in @Entity for JPQL, if no name is defined use class name.
//Hint - @NamedNativeQuery can optionally be used if there is a need for SQL query.
@NamedQuery(name = "PeerTutor.findAll", query = "SELECT pt FROM PeerTutor pt")
@AttributeOverride(name = "id", column = @Column(name = "peer_tutor_id"))
public class PeerTutor extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	// Hint - @Basic(optional = false) is used when the object cannot be null.
	// Hint - @Basic or none can be used if the object can be null.
	// Hint - @Basic is for checking the state of object at the scope of our code.
	@Basic(optional = false)
	// Hint - @Column is used to define the details of the column which this object will map to.
	// Hint - @Column is for mapping and creation (if needed) of an object to DB.
	// Hint - @Column can also be used to define a specific name for the column if it is different than our object name.
	@Column(name = "first_name", nullable = false, length = 50)
	private String firstName;

	@Basic(optional = false)
	@Column(name = "last_name", nullable = false, length = 50)
	private String lastName;

	@Basic(optional = false)
	@Column(name = "program", nullable = false, length = 50)
	private String program;

	// Hint - @Transient is used to annotate a property or field of an entity class, mapped superclass, or embeddable class which is not persistent.
	@Transient
	private String hobby; // Examples:  Tennis, Cycling, etc.
	
	@Transient
	private String careerGoal; // Examples:  Become a teacher, etc.

	 
	// Hint - @OneToMany option cascade will be ignored if not added, meaning no cascade effect.
	// Hint - @OneToMany option fetch should be lazy to prevent eagerly initializing all the data.
	@OneToMany(cascade=CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "peerTutor")
	// Hint - java.util.Set is used as a collection, however List could have been used as well.
	// Hint - java.util.Set will be unique and also possibly can provide better get performance with HashCode.
	@Column(nullable = true)
	private Set<PeerTutorRegistration> peerTutorRegistrations = new HashSet<>();

	public PeerTutor() {
		super();
	}
	
	public PeerTutor(String firstName, String lastName, String program, Set<PeerTutorRegistration> peerTutorRegistrations) {
		this();
		this.firstName = firstName;
		this.lastName = lastName;
		this.program = program;
		this.peerTutorRegistrations = peerTutorRegistrations;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getCareerGoal() {
		return careerGoal;
	}

	public void setCareerGoal(String careerGoal) {
		this.careerGoal = careerGoal;
	}

	public Set<PeerTutorRegistration> getPeerTutorRegistrations() {
		return peerTutorRegistrations;
	}

	public void setPeerTutorRegistrations(Set<PeerTutorRegistration> peerTutorRegistrationsistrations) {
		this.peerTutorRegistrations = peerTutorRegistrationsistrations;
	}

	public void setPeerTutor(String firstName, String lastName, String program) {
		setFirstName(firstName);
		setLastName(lastName);
		setProgram(program);
	}

	//Inherited hashCode/equals is sufficient for this entity class

}
