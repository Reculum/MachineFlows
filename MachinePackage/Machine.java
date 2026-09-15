
package MachinePackage;

import java.util.Map;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Machine {

    protected Integer id;

    //std input/output flow amounts of what materials. As a mapping.
    protected Map<Material, Float> inputAmounts;
    protected Map<Material, Float> outputAmounts;

    //what materials are taken as input and outputted by the machine
    protected ArrayList<Material> MaterialList;

    //a mapping representing what material is piped (or received) by what other machine
    protected Map<Material, LinkedList<Machine>> outMachinesByMaterial;
    protected Map<Material, LinkedList<Machine>> inMachinesByMaterial;


    public Machine(Integer id,
                   ArrayList<Material> MaterialList,
                   ArrayList<Float> stdInFlows,
                   ArrayList<Float> stdOutFlows) throws Exception{

            if ( (MaterialList.size() != stdInFlows.size()) ||
               ((MaterialList.size() != stdOutFlows.size())) ){

                throw new Exception("Error: Mismatch between Materials and Flows!!!");

            }

            this.id = id;

            //the input materials are declared
            this.MaterialList = MaterialList;

            //intialize the two mappings
            this.inputAmounts = new HashMap<Material, Float>();
            this.outputAmounts = new HashMap<Material, Float>();

            //we will iterate over the input and output materials...
            Iterator<Material> MaterialIter = MaterialList.iterator();

            //and the corresponding amounts
            Iterator<Float> InFlowsIter = stdInFlows.iterator();
            Iterator<Float> OutFlowsIter = stdOutFlows.iterator();            


            while (MaterialIter.next() != null){
                
                Material nextMaterial = MaterialIter.next();

                Float nextInFlow = InFlowsIter.next();
                Float nextOutFlow = OutFlowsIter.next();

                inputAmounts.put(nextMaterial, nextInFlow);
                outputAmounts.put(nextMaterial, nextOutFlow);

            }  

    }

    public Integer getId(){
        return id;
    }

    @Override
    public boolean equals(Object other){

        if (this == other){return true;}

        if (other == null || getClass() != other.getClass()) return false;

        Machine altro = (Machine) other;

        return  (altro.getId().equals(id));
    }

    @Override 
    public int hashCode(){
        return id;
    }

    
}
