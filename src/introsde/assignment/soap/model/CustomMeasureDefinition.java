package introsde.assignment.soap.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import introsde.assignment.soap.dao.LifeCoachDao;

//@XmlRootElement(name="measureTypes")
public class CustomMeasureDefinition implements Serializable{
	@XmlElement(name="measureType")
	private List<String> measureDefinition = new ArrayList<String>();
	
	public CustomMeasureDefinition(){
		List<MeasureDefinition> md = MeasureDefinition.getAll();
		for(int i = 0; i < md.size(); i++){
			measureDefinition.add(md.get(i).getMeasureName());
		}
	} 
}