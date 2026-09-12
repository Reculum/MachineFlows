
package MachinePackage;

import java.util.Map;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Machine {

    //std input/output flow amounts of what materials. As a mapping.
    private Map<Material, Float> inputAmounts;
    private Map<Material, Float> outputAmounts;

    //what materials are taken as input and what are outputted.
    private ArrayList<Material> inputMaterials;
    private ArrayList<Material> outputMaterials;

    //a mapping representing what material is piped (or received) by what other machine
    private Map<Material, Machine> outMaterialToMachine;
    private Map<Material, Machine> inMaterialToMachine;

    //two lists: one of the machines supplying the input materials
    //and the other of machines receiving the output materials
    private LinkedList<Machine> inputMachines;
    private LinkedList<Machine> outputMachines;


    public Machine(ArrayList<Material> inputMaterials,
                   ArrayList<Material> outputMaterials,
                   ArrayList<Float> stdInFlows,
                   ArrayList<Float> stdOutFlows) throws Exception{

            if ( (inputMaterials.size() != stdInFlows.size()) ||
               ((outputMaterials.size() != stdOutFlows.size())) ){

                throw new Exception("Error: Mismatch between Materials and Flows!!!");

            }


            //the input materials are declared
            this.inputMaterials = inputMaterials;
            this.outputMaterials = outputMaterials;

            //intialize the two mappings
            this.inputAmounts = new HashMap<Material, Float>();
            this.outputAmounts = new HashMap<Material, Float>();

            //we will iterate over the input and output materials...
            Iterator<Material> inputMatIter = inputMaterials.iterator();
            Iterator<Material> outputMatIter = outputMaterials.iterator();
            
            //and the corresponding amounts
            Iterator<Float> InFlowsIter = stdInFlows.iterator();
            Iterator<Float> OutFlowsIter = stdOutFlows.iterator();            


            while (inputMatIter.next() != null){
                
                Material nextInMaterial = inputMatIter.next();
                Float nextInFlow = InFlowsIter.next();
                (this.inputAmounts).put(nextInMaterial, nextInFlow);

            }

            while (outputMatIter.next() != null){
                
                Material nextOutMaterial = outputMatIter.next();
                Float nextOutFlow = OutFlowsIter.next();
                (this.outputAmounts).put(nextOutMaterial, nextOutFlow);
                
            }
            

    }





    
}
