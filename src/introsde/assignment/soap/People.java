package introsde.assignment.soap;

import introsde.assignment.soap.model.CustomMeasureDefinition;
import introsde.assignment.soap.model.HealthMeasureHistory;
import introsde.assignment.soap.model.LifeStatus;
import introsde.assignment.soap.model.MeasureDefinition;
import introsde.assignment.soap.model.Person;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.WebResult;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

@WebService 
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL)
// optional
public interface People {

	// M #1
	@WebMethod(operationName = "getPersonList")
	@WebResult(name = "person")
	public List<Person> readPersonList();

	// M #2
	@WebMethod(operationName = "readPerson")
	@WebResult(name = "person")
	public Person readPerson(@WebParam(name = "personId") int id);

	// M #3
	@WebMethod(operationName = "updatePerson")
	@WebResult(name = "person")
	public Person updatePerson(@WebParam(name = "person") Person person);

	// M #4
	@WebMethod(operationName = "createPerson")
	@WebResult(name = "person")
	public Person addPerson(@WebParam(name = "person") Person person);

	// M #5
	@WebMethod(operationName = "deletePerson")
	@WebResult(name = "personId")
	public int deletePerson(@WebParam(name = "personId") int id);

	// M #6
	@WebMethod(operationName = "readPersonHisotry")
	@WebResult(name = "history")
	public List<HealthMeasureHistory> readPersonHistory(
			@WebParam(name = "personId") int id,
			@WebParam(name = "measureType") String measureType);

	// M #7
	@WebMethod(operationName = "readMeasureTypes")
	@WebResult(name = "measureType")
	public CustomMeasureDefinition readMeasureTypes();

	// M #8
	@WebMethod(operationName = "readPersonMeasure")
	@WebResult(name = "measure")
	public HealthMeasureHistory readPersonMeasure(
			@WebParam(name = "personId") int id,
			@WebParam(name = "measureType") String measureType,
			@WebParam(name = "mid") int mid);

	// M #9
	@WebMethod(operationName = "savePersonMeasure")
	@WebResult(name = "person")
	public Person savePersonMeasure(
			@WebParam(name = "personId") int id,
			@WebParam(name = "lifeStatus") LifeStatus lifestatus);

	// M #10
	@WebMethod(operationName = "updatePersonMeasure")
	@WebResult(name = "history")
	public HealthMeasureHistory updatePersonMeasure(
			@WebParam(name = "personId") int id,
			@WebParam(name = "history") HealthMeasureHistory history);
	
	

	@WebMethod(operationName = "updatePersonHealthProfile")
	@WebResult(name = "hpId")
	public int updatePersonHP(@WebParam(name = "personId") int id,
			@WebParam(name = "healthProfile") LifeStatus hp);
}