
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

    //what materials are taken as input and what are outputted.
    protected ArrayList<Material> inputMaterials;
    protected ArrayList<Material> outputMaterials;

    //a mapping representing what material is piped (or received) by what other machine
    protected Map<Material, LinkedList<Machine>> outMaterialToMachine;
    protected Map<Material, LinkedList<Machine>> inMaterialToMachine;


    public Machine(Integer id,
                   ArrayList<Material> inputMaterials,
                   ArrayList<Material> outputMaterials,
                   ArrayList<Float> stdInFlows,
                   ArrayList<Float> stdOutFlows) throws Exception{

            if ( (inputMaterials.size() != stdInFlows.size()) ||
               ((outputMaterials.size() != stdOutFlows.size())) ){

                throw new Exception("Error: Mismatch between Materials and Flows!!!");

            }

            this.id = id;

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

    public Integer getId(){
        return this.id;
    }

    public Machine pipeMaterialTo(Material outMat, Machine outMach){

        LinkedList<Machine> ThisMachine_OutputList = (this.outMaterialToMachine).get(outMat);
        ThisMachine_OutputList.addLast(outMach);

        LinkedList<Machine> ThatMachine_InputList = (outMach.inMaterialToMachine).get(outMat);
        ThatMachine_InputList.addLast(this);

        return outMach;
    }

    public Boolean isEqualTo(Machine that){
        return (this.id == that.getId());
    }








    
}
