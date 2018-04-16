package fr.inria.astor.approaches.adqfix.mhs.model;

import java.util.ArrayList;
import java.util.List;

public class DiagnosisCandidateBase <T> {
	protected List<T> componentIDs;
	protected double health;
	
	public DiagnosisCandidateBase(T ... componentsID)
	{
		componentIDs = new ArrayList<T>();
		for(T cID:componentsID)
			this.componentIDs.add(cID);
		health = -1;
	}
	
	public DiagnosisCandidateBase()
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


	public boolean isSubsumed(DiagnosisCandidateBase<T> di) {
		DiagnosisCandidateBase<T> large,small;
		if (di.size()>this.size()){
			large=di;
			small=this;
		}else{
			large=this;
			small=di;
		}			
		for(T compID:small.getComponentIdList()){
			if (!large.contain(compID))					
				return false;
		}
		return true;
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
