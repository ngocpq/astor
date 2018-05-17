package fr.inria.astor.approaches.adqfix.model;

import java.util.List;

public interface Diagnosis <T> {
	public static enum SubsumeCheck{
		UNDER,
		OVER,
		EQUAL,
		UNSUBSUMED
	}
	public SubsumeCheck checkSubsume(List<T> other);
	public int size();
	public T getComponent(int index);
	public List<T> getComponentIdList();
	public boolean contain(T compID);	
}
