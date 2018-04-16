package fr.inria.astor.approaches.adqfix.mhs.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DiagnosisSet<CompType> {
	final List<Diagnosis<CompType>> candidates; 
	public DiagnosisSet()
	{
		candidates = new ArrayList<Diagnosis<CompType>>();
	}
	public List<Diagnosis<CompType>> getCandidateList()
	{
		return candidates;
	}
	public void add(Diagnosis<CompType> d){
		candidates.add(d);
	}
	public void Sort()
	{
		Comparator<Diagnosis<CompType>> comparatorDesending = new Comparator<Diagnosis<CompType>>() {			
			public int compare(Diagnosis<CompType> o1, Diagnosis<CompType> o2) {
				return Double.compare(o2.getHealth(), o1.getHealth());
			}
		};
		
		Collections.sort(candidates, comparatorDesending);
	}
	public void display() {
		for(int i=0;i<this.candidates.size();i++){
			Diagnosis<CompType> c =this.candidates.get(i);
			System.out.println(i+". "+ c.getString());
		}
	}
	public String getCSVString(){
		if(this.candidates.size()==0)
			return "";
		StringBuilder sb = new StringBuilder();
		int i=0;
		for(;i<this.candidates.size()-1;i++){
			Diagnosis<CompType> c =this.candidates.get(i);
			sb.append(c.getCSVString());
			sb.append("\n");
		}
		sb.append(this.candidates.get(i).getCSVString());
		return sb.toString();
	}
	
	public boolean checkSubsume(Diagnosis<CompType> di) {				
		for(int i=0;i<this.candidates.size();i++)
			if (candidates.get(i).isSubsumed(di)){
				return true;
			}
		return false;		
	}
	public int size() {
		return candidates.size();
	}
	public Diagnosis<CompType> get(int i) {
		return candidates.get(i);
	}
	public Diagnosis<CompType> pop(){
		Diagnosis<CompType> top = this.candidates.get(0);
		candidates.remove(0);
		return top;
	}
	
	@Override
	public String toString() {		
		StringBuilder sb = new StringBuilder();
		sb.append("DiagnosisSet: ");
		sb.append("{");
		if (!candidates.isEmpty()){
			sb.append(" "+candidates.get(0).toString());
			for(int i=1;i<this.candidates.size();i++){
				Diagnosis<CompType> c =this.candidates.get(i);
				sb.append(", "+ c.toString());
			}
		}
		sb.append("}");
		return sb.toString();
	}
}
