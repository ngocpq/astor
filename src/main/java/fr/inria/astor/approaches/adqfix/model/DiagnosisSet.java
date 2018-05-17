package fr.inria.astor.approaches.adqfix.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fr.inria.astor.approaches.adqfix.model.Diagnosis.SubsumeCheck;
import fr.inria.astor.core.faultlocalization.entity.SuspiciousCode;

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
	public Diagnosis<CompType> get(int i) {
		return candidates.get(i);
	}
	public void add(Diagnosis<CompType> d){
		candidates.add(d);
	}
	public Diagnosis<CompType> pop(){
		Diagnosis<CompType> top = this.candidates.get(0);
		candidates.remove(0);
		return top;
	}
	public void sort(Comparator<Diagnosis<CompType>> comparator)
	{		
		Collections.sort(candidates, comparator);
	}
	public void display() {
		for(int i=0;i<this.candidates.size();i++){
			Diagnosis<CompType> c =this.candidates.get(i);
			System.out.println(i+". "+ c.toString());
		}
	}

	public static <T> boolean isSubsumebTogether(Diagnosis<T> a,Diagnosis<T> b) {
		if (a.size()<b.size())
			return checkSubsume(a, b);
		else
			return checkSubsume(b, a);
	}
	public static <T> boolean checkSubsume(Diagnosis<T> large,Diagnosis<T> small) {
		if (large.size()<small.size())
			return false;
		for(T compID:small.getComponentIdList()){
			if (!large.contain(compID))					
				return false;
		}
		return true;
	}
	
	public boolean checkSubsume(Diagnosis<CompType> di) {				
		for(int i=0;i<this.candidates.size();i++)
			if (isSubsumebTogether(candidates.get(i),di)){
				return true;
			}
		return false;		
	}
	public int size() {
		return candidates.size();
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
	
	public int contain(List<CompType> mod1) {
		for(int i=0;i<candidates.size();i++){
			SubsumeCheck subsume = candidates.get(i).checkSubsume(mod1);
			if (subsume == SubsumeCheck.EQUAL)
				return i;
		}
		return -1;
	}
	
	public List<Diagnosis<CompType>> findSubsumedSets(List<CompType> d){
		List<Diagnosis<CompType>> result = new ArrayList<>();
		for(int i=0;i<candidates.size();i++){
			Diagnosis<CompType> item = candidates.get(i);
			if (item.size()>d.size())
				return result;
			SubsumeCheck subsume = item.checkSubsume(d);
			if (subsume == SubsumeCheck.EQUAL || subsume == SubsumeCheck.UNDER)
				result.add(item);
		}
		return result;
	}
}
