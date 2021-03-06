package fr.inria.astor.approaches.adqfix.mhs.model;

import java.util.ArrayList;
import java.util.List;

import fr.inria.astor.approaches.adqfix.model.Diagnosis;
import fr.inria.astor.approaches.adqfix.model.Diagnosis.SubsumeCheck;

public class MinimalHittingSet <T> implements Diagnosis<T>{
	protected List<T> componentIDs;
	protected double health;
	
	public MinimalHittingSet(T ... componentsID)
	{
		componentIDs = new ArrayList<T>();
		for(T cID:componentsID)
			this.componentIDs.add(cID);
		health = -1;
	}
	
	public MinimalHittingSet()
	{
		componentIDs = new ArrayList<T>();
		health = -1;
	}
	public void addComponent(T componentID)
	{
		componentIDs.add(componentID);
	}
	
	public List<T> getComponentIdList() {
		return componentIDs;
	}	
	
	public void setHealth(double h)
	{
		this.health=h;
	}
	public double getHealth(){
		return this.health;
	}
	
	

	public boolean contain(T compID) {
		return componentIDs.contains(compID);
	}


	public SubsumeCheck checkSubsume(List<T> di){
		SubsumeCheck result;
		List<T> large,small;
		if (di.size()>this.size()){
			large=di;
			small=this.getComponentIdList();
			result = SubsumeCheck.UNDER;
		}else{
			large=this.getComponentIdList();
			small=di;
			result = SubsumeCheck.OVER;
		}			
		for(T compID:small){
			boolean contain = false;
			for(T i:large){
				if (i.equals(compID)){
					contain = true;
					break;
				}					
			}
			if (!contain)					
				return SubsumeCheck.UNSUBSUMED;
		}
		if (this.size()==di.size())
			result = SubsumeCheck.EQUAL;
		return result;
	}

	public int size() {
		return componentIDs.size();
	}

	public T getComponent(int j) {
		return this.componentIDs.get(j);
	}

	public void setGoodness(double g) {
		goodness=g;
	}
	double goodness;

	
	@Override
	public String toString() {
		return getString();
	}
	
	public String getString() {
		StringBuilder s= new StringBuilder();
		s.append("{");
		for(int i=0;i<componentIDs.size()-1;i++)
			s.append(componentIDs.get(i)+",");
		s.append( componentIDs.get(componentIDs.size()-1));
		s.append("}: h = "+health+", g= "+goodness);
		return s.toString();
	}
	
	public String getStringDisplay() {
		StringBuilder s= new StringBuilder();
		s.append("{");
		for(int i=0;i<componentIDs.size()-1;i++)
			s.append(componentIDs.get(i)+",");
		s.append( componentIDs.get(componentIDs.size()-1));
		s.append("}: h = "+health+", g= "+goodness);
		return s.toString();
	}
	
	public String getCSVString(){
		StringBuilder sb = new StringBuilder("{");		
		for(int i=0;i<componentIDs.size()-1;i++)
			sb.append(componentIDs.get(i)+";");
		sb.append(componentIDs.get(componentIDs.size()-1)+"}");
		sb.append(",");
		sb.append(health);		
		return sb.toString();
	}
}
