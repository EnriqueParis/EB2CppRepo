package eb2cpp.rodinhandler.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import org.eventb.core.basis.SCContextRoot;

public class ComponentTopoSorter {
	
	///////////////
	// Variables //
	///////////////
	
	private int numberOfNodes;
	private HashMap<String, ArrayList<String>> incidenceList;
	private HashMap<String, Boolean> visited;
	
	private ArrayList<String> orderedList;

	/////////////
	// Methods //
	/////////////
	public ComponentTopoSorter() {
		// TODO Auto-generated constructor stub
		incidenceList = new HashMap<String, ArrayList<String>>();
		orderedList = new ArrayList<String>();
		visited = new HashMap<String, Boolean>();
	}
	
	public void insertIncidence(String componentName, ArrayList<String> extensionList) {
		incidenceList.put(componentName, extensionList);
	}
	
	//Only call this method once all the incidences have been added
	public void topoSort() {
		numberOfNodes = incidenceList.size();
		
		//Signal that each context hasnt been visited in the sort process
		for (String name : incidenceList.keySet())
			visited.put(name, false);
		
		for (String name : incidenceList.keySet()) {
			if (visited.get(name) == false)
				topoSortRecursion(name);
		}
		
		//Print STACK
		System.out.println("Stack List: "+orderedList);
	}
	
	public void topoSortRecursion(String nodeName) {
		visited.put(nodeName, true);
		
		for (String refinedName : incidenceList.get(nodeName)) {
			if (visited.get(refinedName) == false)
				topoSortRecursion(refinedName);
		}
		
		orderedList.add(nodeName);
	}
	
	public ArrayList<String> getOrderedList(){
		return orderedList;
	}

}
