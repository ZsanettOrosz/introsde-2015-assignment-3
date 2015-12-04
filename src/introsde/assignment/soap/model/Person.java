package introsde.assignment.soap.model;

import introsde.assignment.soap.dao.LifeCoachDao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;

@Entity
@Table(name = "Person")
@NamedQuery(name = "Person.findAll", query = "SELECT p FROM Person p")
@XmlType(propOrder = { "idPerson", "name", "lastname", "birthdate", "email",
		"username", "lifeStatus" })
//@XmlRootElement
public class Person implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(generator = "sqlite_person")
	@TableGenerator(name = "sqlite_person", table = "sqlite_sequence", pkColumnName = "name", valueColumnName = "seq", pkColumnValue = "Person")
	@Column(name = "idPerson")
	private int idPerson;
	@Column(name = "lastname")
	private String lastname;

	@Column(name = "name")
	private String name;

	@Column(name = "username")
	private String username;

	@Column(name = "birthdate")
	private String birthdate;
	@Column(name = "email")
	private String email;

	@XmlElementWrapper(name = "healthProfile")
	@XmlElement(name = "lifeStatus")
	@OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<LifeStatus> lifeStatus;

	public List<LifeStatus> getLifeStatus() {
		System.err.println("Number of statuses: " + lifeStatus.size());
		if (lifeStatus == null) {
			return null;
		}
		return lifeStatus;
	}

	/*
	 * public void setLifeStatus(LifeStatus newLS) {
	 * System.err.println("Adding a new life status");
	 * this.lifeStatus.add(newLS); }
	 */

	@OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<HealthMeasureHistory> measureHistory;

	@XmlTransient
	// @XmlElementWrapper(name = "measureHistory")
	public List<HealthMeasureHistory> getMeasureHistories() {
		System.err
				.println("History of a person " + measureHistory.size());
		return measureHistory;
	}

	public Person() {
	}

	public int getIdPerson() {
		return idPerson;
	}

	public String getLastname() {
		return lastname;
	}

	@XmlElement(name = "firstname")
	public String getName() {
		return name;
	}

	public String getUsername() {
		return username;
	}

	public String getBirthdate() {
		return birthdate;
	}

	public String getEmail() {
		return email;
	}

	public LifeStatus getLifeStatusByMeasureType(MeasureDefinition measureType) {
		for (int i = 0; i < lifeStatus.size(); i++) {
			if (lifeStatus.get(i).getMeasureDefinition().getMeasureName()
					.equals(measureType.getMeasureName())) {
				return lifeStatus.get(i);
			}
		}
		return null;
	}

	public void removeLifeStatus(LifeStatus old) {
		lifeStatus.remove(old);
	}

	public LifeStatus getLifeStatusByID(int id) {
		return lifeStatus.get(id);
	}

	public void addHistory(HealthMeasureHistory newH) {
		newH.setPerson(this);
		measureHistory.add(newH);
	}

	public void addLifeStatus(LifeStatus newLs) {
		newLs.setPerson(this);
		lifeStatus.add(newLs);
	}

	// setters
	public void setIdPerson(int idPerson) {
		this.idPerson = idPerson;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public static Person getPersonById(int personId) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		Person p = em.find(Person.class, personId);
		LifeCoachDao.instance.closeConnections(em);
		return p;
	}

	public static List<Person> getAll() {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		List<Person> list = em.createNamedQuery("Person.findAll", Person.class)
				.getResultList();
		LifeCoachDao.instance.closeConnections(em);
		return list;
	}

	public static Person savePerson(Person p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(p);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
		System.err.println("Person saved");
		return p;
	}

	public static Person savePersonWithDetail(Person p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		List<LifeStatus> lf = p.getLifeStatus();
		if (lf != null) {
			for (int i = 0; i < lf.size(); i++) {
				LifeStatus.saveLifeStatus(lf.get(i));
			}
		}
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(p);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
		return p;
	}

	public static Person updatePerson(Person p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p = em.merge(p);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
		System.err.println("Person updated");
		return p;
	}

	public static void removePerson(Person p) {
		EntityManager em = LifeCoachDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p = em.merge(p);
		em.remove(p);
		tx.commit();
		LifeCoachDao.instance.closeConnections(em);
	}

}
