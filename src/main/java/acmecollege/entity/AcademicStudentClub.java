/********************************************************************************************************2*4*w*
 * File:  AcademicStudentClub.java Course materials CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * 
 */
package acmecollege.entity;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

//Value 1 is academic and value 0 is non-academic.
@Entity
@DiscriminatorValue("1")
public class AcademicStudentClub extends StudentClub implements Serializable {
	private static final long serialVersionUID = 1L;

	public AcademicStudentClub() {
	}
}
