/********************************************************************************************************2*4*w*
 * File:  Course.java Course materials CST 8277
 * 
 * @author Teddy Yap
 * @author Shariar (Shawn) Emami
 * 
 */
package acmecollege.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.AttributeOverride;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@SuppressWarnings("unused")

/**
 * The persistent class for the course database table.
 */

@Entity 
@Table(name = "Course")
@AttributeOverride(name = "id", column = @Column(name = "course_id"))
public class Course extends PojoBase implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "course_code", columnDefinition = "VARCHAR(7)")
	private String courseCode;

	@Column(name = "course_title", columnDefinition = "VARCHAR(100)")
	private String courseTitle;

	@Basic
	private int year;

	@Column(name = "semester", columnDefinition = "VARCHAR(6)")
	private String semester;

	@Column(name = "credit_units" )
	private int creditUnits;

	@Column( columnDefinition = "BIT(1)")
	private byte online;

	@OneToMany(mappedBy = "course")
	private Set<PeerTutorRegistration> peerTutorRegistrations = new HashSet<>();

	public Course() {
		super();
	}

	public Course(String courseCode, String courseTitle, int year, String semester, int creditUnits, byte online) {
		this();
		this.courseCode = courseCode;
		this.courseTitle = courseTitle;
		this.year = year;
		this.semester = semester;
		this.creditUnits = creditUnits;
		this.online = online;
	}

	public Course setCourse(String courseCode, String courseTitle, int year, String semester, int creditUnits,
			byte online) {
		setCourseCode(courseCode);
		setCourseTitle(courseTitle);
		setYear(year);
		setSemester(semester);
		setCreditUnits(creditUnits);
		setOnline(online);
		return this;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getSemester() {
		return semester;
	}

	public void setSemester(String semester) {
		this.semester = semester;
	}

	public int getCreditUnits() {
		return creditUnits;
	}

	public void setCreditUnits(int creditUnits) {
		this.creditUnits = creditUnits;
	}

	public byte getOnline() {
		return online;
	}

	public void setOnline(byte online) {
		this.online = online;
	}

	public Set<PeerTutorRegistration> getPeerTutorRegistrations() {
		return peerTutorRegistrations;
	}

	public void setPeerTutorRegistrations(Set<PeerTutorRegistration> peerTutorRegistrations) {
		this.peerTutorRegistrations = peerTutorRegistrations;
	}

	// Inherited hashCode/equals is sufficient for this Entity class

}
