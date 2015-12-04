package introsde.assignment.soap;

import introsde.assignment.soap.model.CustomMeasureDefinition;
import introsde.assignment.soap.model.HealthMeasureHistory;
import introsde.assignment.soap.model.LifeStatus;
import introsde.assignment.soap.model.MeasureDefinition;
import introsde.assignment.soap.model.Person;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

//Service Implementation

@WebService(endpointInterface = "introsde.assignment.soap.People", serviceName = "PeopleService")
public class PeopleImpl implements People {

	// M #1
	@Override
	public List<Person> readPersonList() {
		return Person.getAll();
	}

	// M #2
	@Override
	public Person readPerson(int id) {
		System.out.println("---> Reading Person by id = " + id);
		Person p = Person.getPersonById(id);
		if (p != null) {
			System.out.println("---> Found Person by id = " + id + " => "
					+ p.getName());
		} else {
			System.out.println("---> Didn't find any Person with  id = " + id);
		}
		return p;
	}

	// M #3
	@Override
	public Person updatePerson(Person person) {
        Person existing = Person.getPersonById(person.getIdPerson());

        if (existing == null) {
        	System.err.println("No content" );
        } else {

            Person fromDB = Person.getPersonById(person.getIdPerson());
            
            System.err.println("Exists and id :" +  fromDB.getIdPerson() );
          	if(person.getName()!= null && !fromDB.getName().equals(person.getName())){
        		fromDB.setName(person.getName());
        	}
        	if(person.getLastname()!= null && !fromDB.getLastname().equals(person.getLastname())){
        		fromDB.setLastname(person.getLastname());
        	}
        	if(person.getBirthdate()!= null && fromDB.getBirthdate() == person.getBirthdate()){
        		fromDB.setBirthdate(person.getBirthdate());
        	}
        	if(person.getEmail()!= null && !fromDB.getEmail().equals(person.getEmail())){
        		fromDB.setEmail(person.getEmail());
        	}
        	if(person.getUsername()!= null && !fromDB.getUsername().equals(person.getUsername())){
        		fromDB.setUsername(person.getUsername());
        	}
                        
            Person.updatePerson(fromDB);
        }
		//Person.updatePerson(person);
		return person;
	}

	// M #4
	@Override
	public Person addPerson(Person person) {
		int len = person.getLifeStatus().size();
        if(len != 0){
        	for(int i = 0; i < len; i++){
        		person.getLifeStatus().get(i).setPerson(person);
        	}
        	
        }else{
        	System.err.println("len is null");
        }
		Person.savePerson(person);
		return person;
	}

	// M #5
	@Override
	public int deletePerson(int id) {
		Person p = Person.getPersonById(id);
		if (p != null) {
			Person.removePerson(p);
			return 0;
		} else {
			return -1;
		}
	}

	// M #6
	@Override
	public List<HealthMeasureHistory> readPersonHistory(int id, String measureType) {
		System.err.println("only measure type");
		Person p = Person.getPersonById(id);
		List<HealthMeasureHistory> his = p.getMeasureHistories();
		List<HealthMeasureHistory> hisByType = new ArrayList<>();
		for (int i = 0; i < his.size(); i++) {
			if (measureType.equalsIgnoreCase(his.get(i).getMeasureDefinition()
					.getMeasureName())) {
				hisByType.add(his.get(i));
			}
		}
		if (hisByType.size() == 0)
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		return hisByType;
	}

	// M #7
	@Override
	public CustomMeasureDefinition readMeasureTypes() {
		System.err.println("measure types ...");
		CustomMeasureDefinition def = new CustomMeasureDefinition();
		return def;
	}

	// M #8
	@Override
	public HealthMeasureHistory readPersonMeasure(int id, String measureType,
			int mid) {
		Person person = Person.getPersonById(id);
		List<HealthMeasureHistory> personHis = person.getMeasureHistories();
		HealthMeasureHistory his = null;
		for (int i = 0; i < personHis.size(); i++) {
			if (personHis.get(i).getIdMeasureHistory() == mid
					&& personHis.get(i).getMeasureDefinition().getMeasureName()
							.equalsIgnoreCase(measureType)) {
				his = personHis.get(i);
			}
		}
		if (his == null) {
			System.err.println("Empty history");
			throw new WebApplicationException(Response.Status.NOT_FOUND);
			// throw new RuntimeException("Get: history with " + id +
			// " not found");
		} else {
			System.err.println(his.getMeasureDefinition().getMeasureName());
			System.err.println(measureType);
			System.err.println(his.getMeasureDefinition().getMeasureName()
					.equalsIgnoreCase(measureType));
		}
		return his;
	}

	// M #9
	@Override
	public Person savePersonMeasure(int id, LifeStatus newLifeStatus) {
		Person person = Person.getPersonById(id);
		newLifeStatus.setPerson(person);
		LifeStatus oldLifeStatus = person.getLifeStatusByMeasureType(newLifeStatus.getMeasureDefinition());
		if (oldLifeStatus == null) {
			person.addLifeStatus(newLifeStatus);
			LifeStatus.saveLifeStatus(newLifeStatus);
			Person.updatePerson(person);
		} else {
			System.err.println("Old value: " + oldLifeStatus.getValue());
			//here should go the old lifestatus
			HealthMeasureHistory witOldLFS = new HealthMeasureHistory(oldLifeStatus);
			person.addHistory(witOldLFS);
			HealthMeasureHistory.saveHealthMeasureHistory(witOldLFS);
			person.removeLifeStatus(oldLifeStatus);
			LifeStatus.removeLifeStatus(oldLifeStatus);
			person.addLifeStatus(newLifeStatus);
			LifeStatus.saveLifeStatus(newLifeStatus);
			Person.updatePerson(person);
		}
		return person;
	}

	// M #10
	@Override
	public HealthMeasureHistory updatePersonMeasure(int id, HealthMeasureHistory newHistory) {
		Person person = Person.getPersonById(id);
		List<HealthMeasureHistory> personHis = person.getMeasureHistories();
		HealthMeasureHistory his = null;
		for (int i = 0; i < personHis.size(); i++) {
			if (personHis.get(i).getIdMeasureHistory() == newHistory.getIdMeasureHistory()) {
				his = personHis.get(i);
			}
		}

        if (his == null) {
    		System.err.println("Not found history");
			throw new WebApplicationException(Response.Status.NOT_FOUND);
        } else {
            //newHis.setIdMeasureHistory(this.id);
            //newHis.setPerson(Person.getPersonById(personId));
            if(his.getValue() != newHistory.getValue()){
            	his.setValue(newHistory.getValue());
            	HealthMeasureHistory.updateHealthMeasureHistory(his);
            }
           
        }
		return his;
	}

	@Override
	public int updatePersonHP(int id, LifeStatus hp) {
		LifeStatus ls = LifeStatus.getLifeStatusById(hp.getIdMeasure());
		if (ls.getPerson().getIdPerson() == id) {
			LifeStatus.updateLifeStatus(hp);
			return hp.getIdMeasure();
		} else {
			return -1;
		}
	}

}
