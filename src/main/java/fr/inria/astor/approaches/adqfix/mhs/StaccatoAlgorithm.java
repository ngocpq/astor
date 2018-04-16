package fr.inria.astor.approaches.adqfix.mhs;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import fr.inria.astor.approaches.adqfix.mhs.model.Diagnosis;
import fr.inria.astor.approaches.adqfix.mhs.model.DiagnosisSet;
import fr.inria.astor.approaches.adqfix.mhs.model.IObservationMatrix;


public class StaccatoAlgorithm {
	private static Logger logger;

	static Logger getLogger() {
		if (logger == null)
			logger = Logger.getLogger(StaccatoAlgorithm.class);
		return logger;
	}
	
	public static <CompType> DiagnosisSet<CompType> Staccato(IObservationMatrix<CompType> a, double lamda, int L){
		Long time = System.nanoTime();
		getLogger().debug("Staccato "+time+" begin: ( ["+a.rowCount()+","+a.colCount()+"], "+a.colCount()+", "+lamda+", "+L+")");
		//getLogger().debug(a.toString());
		//collection conflict sets
		List<Integer> TF = new ArrayList<Integer>();
		for(int i=0;i<a.rowCount();i++)
			if(a.getError(i)==IObservationMatrix.RESULT_FAILED)
				TF.add(i);		
		//rank components
		List<CompType> rank = getComponentRank(a);

		//init diagnosis set
		DiagnosisSet<CompType> diagnoses = new DiagnosisSet<CompType>(); 
		double seen = 0;
		
		for(int i=0;i<a.colCount();i++){
			if (a.getN11(i)==TF.size()){
				CompType compID = a.getComponentIDAt(i);
				Diagnosis<CompType> d =  new Diagnosis(compID);
				//getLogger().debug("time "+time+" diagnoses add: "+d);
				diagnoses.add(d);				
				//strip component j
				a.removeCol(i);
				i--;
				//getLogger().debug("remove col: "+compID);
				//remove i from rank
				rank.remove(compID);				
				seen=seen+ 1.0/a.colCount();
			}
		}
		//getLogger().debug("time "+time+" matrix after remove 1: ");
		//getLogger().debug(a.toString());
		//
		while(rank.size()>0 && seen <= lamda && diagnoses.size()<=L){
			CompType compID = rank.get(0);
			//getLogger().debug("time "+time+": top component: "+compID);
			rank.remove(0);			
			seen = seen +1.0/a.colCount();			
			IObservationMatrix<CompType> a2 = Strip(a, a.getComponentIndex(compID));			
			DiagnosisSet<CompType> diagnosisSet2 = Staccato(a2,lamda,L);
			//check subsumed
			while (diagnosisSet2.size()>0){
				Diagnosis<CompType> di = diagnosisSet2.pop();				
				di.addComponent(compID);				
				if (diagnoses.checkSubsume(di)==false)
					diagnoses.add(di);
			}
		}
		getLogger().debug("Staccato "+time +" return: "+diagnoses.toString());
		return diagnoses;
	}
	
	//remove all row i that a[i,J]=1 and error[i]=1
	static <CompType> IObservationMatrix<CompType> Strip(IObservationMatrix<CompType> matrix,int j){
		IObservationMatrix<CompType> a = matrix.copy();
		for(int i=a.rowCount()-1;i>=0;i--)
			if(a.getHit(i, j)==1 && a.getError(i)==1)
				a.removeRow(i);
		a.removeCol(j);
		return a;
	}
	
	private static <CompType> List<CompType> getComponentRank(IObservationMatrix<CompType> a) {						
		List<Integer> rankedIndex = new ArrayList<Integer>(a.colCount());
		for(int i=0;i<a.colCount();i++)
			if(a.getN11(i)>0)
				rankedIndex.add(i);		
		//sort by score des
		int M=rankedIndex.size();
		for(int i=0;i<M;i++){
			for(int j=i+1;j<M;j++)
				if(a.getScore(rankedIndex.get(i))<a.getScore(rankedIndex.get(j))){
					int tmp = rankedIndex.get(i);
					rankedIndex.set(i,rankedIndex.get(j));
					rankedIndex.set(j, tmp);
				}					
		}			
		List<CompType> rankedName = new ArrayList<CompType>();
		for(int i=0;i<rankedIndex.size();i++)
			rankedName.add(a.getComponentIDAt(rankedIndex.get(i)));
		return rankedName;
	}
	
	
	public static <T> void sortListDES(List<T> list,double[] score){
		double tmpS ;
		T tmpT;
		for(int i=0;i<score.length;i++){
			for(int j=i+1;j<score.length;j++)
				if(score[i]<score[j]){
					tmpS= score[i];
					score[j]=score[i];
					score[i]=tmpS;
					
					tmpT = list.get(i);
					list.set(i, list.get(j));
					list.set(j, tmpT);
					
				}					
		}		
	}
}
