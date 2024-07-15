/********************************************************************************************************2*4*w*
 * File:  MembershipCard.java Course materials CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * 
 */
package acmecollege.entity;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * The persistent class for the membership_card database table.
 */
//MC01 - Add the missing annotations.
@Entity
@Table(name = "membership_card")
@AttributeOverride(name = "id", column = @Column(name = "card_id"))
// MC02 - Do we need a mapped super class?  If so, which one?
public class MembershipCard extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	// Add annotations for 1:1 mapping. Changes here should cascade.
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "card", optional = true)
	private ClubMembership clubMembership;

	// Add annotations for M:1 mapping. Changes here should not cascade.
	@ManyToOne
	@JoinColumn(name = "student_id", referencedColumnName = "id")
	private Student owner;

	// Add annotations.
	@Column(columnDefinition = "BIT(1)")
	private byte signed;

	public MembershipCard() {
		super();
	}

	public MembershipCard(ClubMembership clubMembership, Student owner, byte signed) {
		this();
		this.clubMembership = clubMembership;
		this.owner = owner;
		this.signed = signed;
	}

	public ClubMembership getClubMembership() {
		return clubMembership;
	}

	public void setClubMembership(ClubMembership clubMembership) {
		this.clubMembership = clubMembership;
		// We must manually set the 'other' side of the relationship (JPA does not 'do'
		// auto-management of relationships)
		if (clubMembership != null) {
			clubMembership.setCard(this);
		}
	}

	public Student getOwner() {
		return owner;
	}

	public void setOwner(Student owner) {
		this.owner = owner;
		// We must manually set the 'other' side of the relationship (JPA does not 'do'
		// auto-management of relationships)
		if (owner != null) {
			owner.getMembershipCards().add(this);
		}
	}

	public byte getSigned() {
		return signed;
	}

	public void setSigned(byte signed) {
		this.signed = signed;
	}

	public void setSigned(boolean signed) {
		this.signed = (byte) (signed ? 0b0001 : 0b0000);
	}

	// Inherited hashCode/equals is sufficient for this entity class

}