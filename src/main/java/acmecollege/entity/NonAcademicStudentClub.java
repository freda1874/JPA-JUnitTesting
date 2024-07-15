/********************************************************************************************************2*4*w*
 * File:  NonAcademicStudentClub.java Course materials CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * @author Lei Luo
 * 
 */
package acmecollege.entity;

import java.io.Serializable;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

//Value 1 is academic and value 0 is non-academic.
@Entity
@DiscriminatorValue("0")
public class NonAcademicStudentClub extends StudentClub implements Serializable {
	private static final long serialVersionUID = 1L;

	public NonAcademicStudentClub() {

	}
}