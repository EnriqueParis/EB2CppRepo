package eb2cpp.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.eclipse.core.runtime.CoreException;
import org.eventb.core.ISCExtendsContext;
import org.eventb.core.ISCInternalContext;
import org.eventb.core.ISCRefinesMachine;
import org.eventb.core.basis.SCContextRoot;
import org.eventb.core.basis.SCMachineRoot;
import org.rodinp.core.IInternalElement;
import org.rodinp.core.IRodinDB;
import org.rodinp.core.IRodinElement;
import org.rodinp.core.IRodinFile;
import org.rodinp.core.IRodinProject;
import org.rodinp.core.RodinCore;
import org.rodinp.core.RodinDBException;

import eb2cpp.rodinhandler.tools.ComponentTopoSorter;

public class EB2CppRodinHandler {
	///////////////
	// Variables //
	///////////////
	private IRodinDB rodinDB;
	private String projectName;
	private IRodinProject[] rodinProjects;
	private IRodinProject rodinProject;
	
	private HashMap<String,SCContextRoot> contexts;
	private HashMap<String,ArrayList<String>> contextsExtensions;
	private ComponentTopoSorter contextSorter;
	private ArrayList<String> contextsOrdered;
	
	private HashMap<String,SCMachineRoot> machines;
	private HashMap<String,LinkedList<String>> machinesSeenContexts;
	private HashMap<String,ArrayList<String>> machinesRefinement;
	private ArrayList<String> machinesOrdered;
	
	private String filePath;
	
	
	
	/////////////////
	// Constructor //
	/////////////////
	public EB2CppRodinHandler(String inputProjectName) throws RodinDBException {
		rodinDB = RodinCore.getRodinDB();
		rodinProjects = rodinDB.getRodinProjects();
		
		projectName = inputProjectName;
		
		//rodinProject = rodinDB.getRodinProject(projectName);
		
		contexts = new HashMap<String,SCContextRoot>();
		contextsExtensions = new HashMap<String,ArrayList<String>>();
		contextsOrdered = new ArrayList<String>();
		
		machines = new HashMap<String,SCMachineRoot>();
		machinesSeenContexts = new HashMap<String,LinkedList<String>>();
		machinesRefinement = new HashMap<String,ArrayList<String>>();
		
		machinesOrdered = new ArrayList<String>();
		
		filePath = RodinCore.getRodinDB().getResource().getRawLocation().toString();
		System.out.println(filePath);
	}
	
	
	/////////////
	// Methods //
	/////////////
	public SCContextRoot getContextRoot(String contextName){
		return contexts.get(contextName);
	}
	
	public HashMap<String,ArrayList<String>> getContextsExtensions(){
		return contextsExtensions;
	}
	
	public ArrayList<String> getContextNamesOrdered(){
		return contextsOrdered;
	}
	
	public HashMap<String,SCMachineRoot> getMachines(){
		return machines;
	}
	
	public HashMap<String,LinkedList<String>> getMachinesSeenContexts(){
		return machinesSeenContexts;
	}
	
	public HashMap<String,ArrayList<String>> getMachineRefinements(){
		return machinesRefinement;
	}
	
	public ArrayList<String> getMachinesOrdered(){
		return machinesOrdered;
	}
	
	public SCMachineRoot getMachineRoot(String machineName) {
		return machines.get(machineName);
	}
	
	
	public boolean projectExists() {
		Boolean itExists = false;
		
		//Iterates over the list of projects and compares each project name with name given by user
		for (IRodinProject currentProject : rodinProjects) {
			if (projectName.equals(currentProject.getElementName())) itExists = true;
		}
		
		return itExists;
	}
	
	public void fetchRodinProject() {
		rodinProject = rodinDB.getRodinProject(projectName);
	}
	
	//Extract contexts of target project from RodinDB
	public void fetchContexts() throws RodinDBException {
		
		for (IRodinElement projectChild : rodinProject.getChildren()) { // O( files in project )
			IInternalElement elementRoot = ((IRodinFile)projectChild).getRoot();
			
			if (elementRoot instanceof SCContextRoot) {
				SCContextRoot contextRoot = (SCContextRoot) elementRoot;
				String contextName = elementRoot.getElementName();
				contexts.put(contextName, contextRoot);
				
				//See which contexts it extends
				ArrayList<String> emptyListExtensions = new ArrayList<String>();
				contextsExtensions.put(contextName, emptyListExtensions);
				for (ISCExtendsContext extendClause : contextRoot.getSCExtendsClauses()) {
					try {
						String extendedContextName = extendClause.getAbstractSCContext().getComponentName();
						contextsExtensions.get(contextName).add(extendedContextName);				
					}
					catch (CoreException e) {
						e.printStackTrace();
					}
				}
				
			}
		}
		
		//Calculate incidency of each component. Then you can use that to sort in linear complexity
		
		contextSorter = new ComponentTopoSorter();
		for (String contextNameToOrder : contexts.keySet()) {
			contextSorter.insertIncidence(contextNameToOrder, contextsExtensions.get(contextNameToOrder));
		}
		
		contextSorter.topoSort(); // O( ctxs )
		contextsOrdered = contextSorter.getOrderedList();
		
		//To define order of translation, we need to know which contexts
		//are at the bottom of the sequence of extensions. You can't translate
		//a context B that extends context A, without translating context A first
		/*
		while (contextsOrdered.size() < contexts.size()) {
			for (String contextNameToOrder : contexts.keySet()) {
				
				//If its not already in the list of ordered contexts
				if (!contextsOrdered.contains(contextNameToOrder)) {
					
					//If the context doesn't extend a previous context, it can be
					//translated without trouble
					if (!contextsExtensions.containsKey(contextNameToOrder)) {
						contextsOrdered.add(contextNameToOrder);
					}
					
					//The context to be ordered 'A' extends another context 'B'. If context B
					//is already in the order of context translation, we can now add
					//context A, since B will be translated first.
					else if (contextsOrdered.contains(contextsExtensions.get(contextNameToOrder))) {
						contextsOrdered.add(contextNameToOrder);
					}
				}
			}
		}
		*/
		
		System.out.println(contextsOrdered);
		System.out.println(contextsExtensions);
	}
	
	//Extract machines for target project in Rodin
	public void fetchMachines() throws RodinDBException {
		
		for (IRodinElement projectChild : rodinProject.getChildren()) { // O( files in project )
			IInternalElement elementRoot = ((IRodinFile)projectChild).getRoot();
			
			//It iterates over all of the elements of the project. If it's a machine,
			//proceed to get info from it
			if (elementRoot instanceof SCMachineRoot) {
				SCMachineRoot machineRoot = (SCMachineRoot) elementRoot;
				String machineName = elementRoot.getElementName();
				machines.put(machineName, machineRoot);
				System.out.println("Machine: "+machineName);
				
				//Get contexts that this machine sees
				LinkedList<String> seenContexts = new LinkedList<String>();
				for(ISCInternalContext InternalContext : machineRoot.getChildrenOfType(ISCInternalContext.ELEMENT_TYPE)) {
					seenContexts.add(InternalContext.getElementName());
				}
				
				machinesSeenContexts.put(machineName, seenContexts);
				
				//Get the machines that this machine refines
				ArrayList<String> emptyListRefinements = new ArrayList<String>();
				machinesRefinement.put(machineName, emptyListRefinements);
				for (ISCRefinesMachine refinedMachine : machineRoot.getSCRefinesClauses()) {
					try {
						String refinedMachineName = refinedMachine.getAbstractSCMachine().getBareName();
						machinesRefinement.get(machineName).add(refinedMachineName);
					}
					catch (CoreException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		ComponentTopoSorter machineSorter = new ComponentTopoSorter();
		for (String machineNameToOrder : machines.keySet()) {
			machineSorter.insertIncidence(machineNameToOrder, machinesRefinement.get(machineNameToOrder));
		}
		machineSorter.topoSort(); // O( Mchs )
		machinesOrdered = machineSorter.getOrderedList();
		
		//To define order of translation, we need to know which machines
		//are at the bottom of the sequence of refinements. You can't translate
		//a machine B that refines machine A, without translating machine A first
		/*
		while (machinesOrdered.size() < machines.size()) {
			for (String machineNameToOrder : machines.keySet()) {
				//If its not already in the list of ordered machines
				if (!machinesOrdered.contains(machineNameToOrder)) {
					
					//If the machine doesn't refine a previous context, it can be
					//translated without trouble
					if (!machinesRefinement.containsKey(machineNameToOrder)) {
						machinesOrdered.add(machineNameToOrder);
					}
					//The machine to be ordered 'B' refines another machine 'A'. If machine A
					//is already in the order of machine translation, we can now add
					//machine B, since A will be translated first. Otherwise, we can't
					// yet add machine B to the translation order. We have to find machine A first.
					else if (machinesOrdered.contains(machinesRefinement.get(machineNameToOrder))) {
						machinesOrdered.add(machineNameToOrder);
					}
				}
			}
		}
		*/
		System.out.println(machinesRefinement);
		System.out.println(machinesOrdered);
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	public int getContextCount() throws RodinDBException {
		int contextNumber = 0;
		
		for (IRodinElement projectChild : rodinProject.getChildren()) {
			IInternalElement elementRoot = ((IRodinFile)projectChild).getRoot();
			if (elementRoot instanceof SCContextRoot) {contextNumber += 1;}
		}
		
		return contextNumber;
	}
	
}
