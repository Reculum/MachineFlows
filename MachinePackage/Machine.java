
package MachinePackage;

import java.util.Map;
import java.util.LinkedList;
import java.util.HashMap;

public class Machine {

    protected Integer id;

    //std input/output flow amounts of what materials. As a mapping.
    protected Map<Material, Float> inputAmounts;
    protected Map<Material, Float> outputAmounts;

    //what materials are taken as input and outputted by the machine
    protected Material[] MaterialList;

    //a mapping representing what material is piped (or received) by what other machine
    protected Map<Material, LinkedList<Machine>> outMachinesByMaterial = new HashMap<Material, LinkedList<Machine>>();
    protected Map<Material, LinkedList<Machine>> inMachinesByMaterial = new HashMap<Material, LinkedList<Machine>>();


    public Machine(Integer id,
                   Material[] MaterialList,
                   Float[] stdInFlows,
                   Float[] stdOutFlows) throws Exception{

            if ( (MaterialList.length != stdInFlows.length) ||
               ((MaterialList.length != stdOutFlows.length)) ){

                throw new Exception("Error: Mismatch between Materials and Flows!!!");

            }

            this.id = id;

            //the input materials are declared
            this.MaterialList = MaterialList;

            //intialize the two mappings
            this.inputAmounts = new HashMap<Material, Float>();
            this.outputAmounts = new HashMap<Material, Float>();
            
            Integer L = MaterialList.length;

            for (int k = 0; k < L; k ++){
                
                Material nextMaterial = MaterialList[k];

                Float nextInFlow = stdInFlows[k];
                Float nextOutFlow = stdOutFlows[k];

                inputAmounts.put(nextMaterial, nextInFlow);
                outputAmounts.put(nextMaterial, nextOutFlow);

            }
            
            for (int k = 0; k < L; k++){

                Material nextMaterial = MaterialList[k];

                outMachinesByMaterial.put(nextMaterial, new LinkedList<Machine>());
                inMachinesByMaterial.put(nextMaterial, new LinkedList<Machine>());

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
