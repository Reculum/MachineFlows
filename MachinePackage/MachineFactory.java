package MachinePackage;

import java.io.BufferedReader;
import java.io.FileReader;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.LinkedList;
import java.util.Iterator;

public class MachineFactory{

    protected Machine[] MachineList;
    protected Material[] MaterialList;
    protected Pipe[] PipeList;

    protected int nMachines;
    protected int nMaterials;
    protected int nPipes;

    protected String[] MachineNames;
    private Map<Material, Integer> FromMaterialToInt;
    
    public MachineFactory(){};

    private int[] ParseMachinelFileIntoTotalMachinesAndMaterials(String MachinePath){

        int nMachines = 0;
        int nMaterials = 0;

        int lineWhileIter;
        char  CurrentChar;
        String line;
        int LenLine;

        try(BufferedReader br = new BufferedReader(new FileReader(MachinePath))){ 

            //special treatment for the first line
            line = br.readLine();
            LenLine = line.length();

            //read the first line and count the number of materials
            for (int k = 0; k < LenLine; k++){
                CurrentChar = line.charAt(k);                
                if ( (CurrentChar == '\t') || (k + 1 == LenLine)){nMaterials += 1;}
            }

            //now, read all the other lines until there are no more
            while ( (line = br.readLine()) != null) {

                LenLine = line.length();
                lineWhileIter = 0;

                //iterate over the line until you find the first tab character
                while ((lineWhileIter < LenLine) && (line.charAt(lineWhileIter) != '\t')){
                    lineWhileIter += 1;
                }

                //then, the character next to it, will tell you weather to count that line
                //as representing a machine or not
                if (line.charAt(lineWhileIter + 1) == 'o'){nMachines += 1;}
            }
            br.close();
        }catch (IOException e){
        System.out.println("Error counting the Machines and Materials in the Machine file.");
        }
        
        int Ret[] = {nMachines, nMaterials};
        return Ret;
    }

    private void ParseMaterialLine(String MaterialLine){

        FromMaterialToInt = new HashMap<Material, Integer>();
        String[] Materials = MaterialLine.split("\t");
        int nMaterials = Materials.length;

        for (int k = 0; k < nMaterials; k++){

            Material tempMaterial = new Material(Materials[k]);
            MaterialList[k] = tempMaterial;
            FromMaterialToInt.put(tempMaterial, k);

        }
    }

    private void PipeMaterialBetweenMachines(Machine MachFrom, Machine MachTo, Material Mat) throws Exception{

        if ( !(MachFrom.outputAmounts.get(Mat) > 0) ){throw new Exception(MachineNames[MachFrom.getId()] + " cannot output " + Mat.getName());}
        if ( !(MachTo.inputAmounts.get(Mat) > 0) ){throw new Exception(MachineNames[MachTo.getId()] + " cannot process " + Mat.getName());}

        Iterator<Machine> MachFromIter = (MachFrom.outMachinesByMaterial.get(Mat)).iterator();
        Iterator<Machine> MachToIter = (MachTo.inMachinesByMaterial.get(Mat)).iterator();

        boolean  MachToWasAlreadyThere = false;
        boolean  MachFromWasAlreadyThere = false;

        while (MachFromIter.hasNext()){

            Machine nextMach = MachFromIter.next();
            if (nextMach.equals(MachTo)){MachToWasAlreadyThere = true;}

        }

        while (MachToIter.hasNext()){

            Machine nextMach = MachToIter.next();
            if (nextMach.equals(MachFrom)){MachFromWasAlreadyThere = true;}
        }

        if (!MachFromWasAlreadyThere){MachTo.inMachinesByMaterial.get(Mat).addLast(MachFrom);}
        if (!MachToWasAlreadyThere){MachFrom.outMachinesByMaterial.get(Mat).addLast(MachTo);}

    }

    public int getNumMachines(){return nMachines;}
    public int getNumMaterials(){return  nMaterials;}
    public int getNumPipes(){return  nPipes;}
    public Machine[] getMachineList(){return MachineList;}
    public Material[] getMaterialList(){return  MaterialList;}
    public Pipe[] getPipeList(){return  PipeList;}
    public String[] getMachineNames(){return MachineNames;}

    public int GetMaterialId(Material mat){return FromMaterialToInt.get(mat);}


    public void buildFactoryFromFile(String MachinePath, String PipePath){

        int nMachnMat[] = this.ParseMachinelFileIntoTotalMachinesAndMaterials(MachinePath);

        nMachines = nMachnMat[0];
        nMaterials = nMachnMat[1];

        MaterialList = new Material[nMaterials];
        MachineList = new Machine[nMachines];
        PipeList = new Pipe[nMachines * nMaterials];

        //we will need to order and save important informations in array, in order to effeciently combine
        //them later on

        //takes as input the string found in the text and maps it to a Machine
        Map<String, Machine> FromMachineNameToMachine = new HashMap<String, Machine>(nMachines);

        //creates a mapping between the machine name and a unique machine id
        Map<String, Integer> FromMachineNameToId = new HashMap<String, Integer>(nMachines);

        //the inverse mapping of the one above
        MachineNames = new String[nMachines];

        //each index will represent a Machine and will contain a list of the STANDARD input Material Flows to said machine
        double [][] StdInputFlowsByMachine = new double [nMachines][nMaterials];

        //each index will represent a Machine and will contain a list of the STANDARD output Material Flows from said machine
        double [][] StdOutputFlowsByMachine = new double [nMachines][nMaterials];

        //initializing useful variables

        int MachineId = 0;

        String Name = "";
        String Mode = "";
        String line = "";
        String Flow = "";

        try(BufferedReader br = new BufferedReader(new FileReader(MachinePath))){
            
            String FirstLine = br.readLine();
            this.ParseMaterialLine(FirstLine);

            //now we read the remaining lines

            while ( (line = br.readLine()) != null) {

                String[] NameModeFlows = line.split("\t");

                Name = NameModeFlows[0];
                Mode = NameModeFlows[1];

                if ( FromMachineNameToId.get(Name) == null ){

                    FromMachineNameToId.put(Name, MachineId);
                    MachineNames[MachineId] = Name;
                    MachineId += 1;

                }

                for (int k = 2; k < NameModeFlows.length; k++){

                    Flow = NameModeFlows[k];
                    //cast it into Float
                    double  FlowFloat = Double.parseDouble(Flow);

                    if (Mode.equals("in")){
                        StdInputFlowsByMachine[FromMachineNameToId.get(Name)][k - 2] = FlowFloat;
                    }
                    if (Mode.equals("out")){
                        StdOutputFlowsByMachine[FromMachineNameToId.get(Name)][k - 2] = FlowFloat;
                    }

                }

            }

        br.close();

        } catch(Exception e) {System.out.println("Error in reading the Machine Flows");}

        //now, we build and associate the machines to their names

        for (int k = 0; k < nMachines; k++){

            Name = MachineNames[k];
            double[] inputFlows = StdInputFlowsByMachine[k];
            double[] outputFlows = StdOutputFlowsByMachine[k];

            try{

                Machine tempMachine = new Machine(k, MaterialList, inputFlows, outputFlows);
                FromMachineNameToMachine.put(Name, tempMachine);
                MachineList[k] = tempMachine;

            }
            catch(Exception e){
                System.out.println("Error in the building of the Machine!");
                System.out.println(e.getMessage());
            }
           
        }
        
        //finally, we read where we must pipe what material to what machine

        String MachNameFrom;
        String MachNameTo;
        String MatName;
        int PipeId = 0;
        nPipes = 0;

        try(BufferedReader br = new BufferedReader(new FileReader(PipePath))){    
            
            while ( (line = br.readLine()) != null){

                String[] FromToMat = line.split("\t");

                MachNameFrom = FromToMat[0];
                MachNameTo = FromToMat[1];
                MatName = FromToMat[2];

                Machine MachFrom = FromMachineNameToMachine.get(MachNameFrom);
                Machine MachTo = FromMachineNameToMachine.get(MachNameTo);
                Material Mat = new Material(MatName);

                try{
                    this.PipeMaterialBetweenMachines(MachFrom, MachTo, Mat); Pipe tempPipe = new Pipe(PipeId, MachFrom, MachTo, Mat);
                    PipeList[PipeId] = tempPipe;
                    nPipes += 1;
                    PipeId += 1;
                }
                catch(Exception e){
                    System.out.println("Error while piping a material from a machine!");
                    System.out.println(e.getMessage());
                }

            }
            br.close();
        }
        catch (IOException e){
        System.out.println("Error while processing the Pipe file.");
        }

        //we may have initially allocated more memory that was actually necessary for storing all the pipes.
        //Since slicing the array would be, in my opinion, quite the slow operation I suggest filling
        //the remainder of the array with null Pipes instead.

        for (int k = PipeId; k < nMachines * nMaterials; k += 1){
            PipeList[k] = null;
        }

    }
    
    @Override 
    public String toString(){

        String retString = "";

        for (int MachineIdx = 0; MachineIdx < nMachines; MachineIdx++){

            Machine tempMachine = MachineList[MachineIdx];
            retString += MachineNames[MachineIdx] + "\n";

            for (int MaterialIdx = 0; MaterialIdx < nMaterials; MaterialIdx++){

                Material tempMaterial = MaterialList[MaterialIdx];
                LinkedList<Machine> tempOutputMachines = tempMachine.outMachinesByMaterial.get(tempMaterial);
                Iterator<Machine> OutMachineIter = tempOutputMachines.iterator();

                while (OutMachineIter.hasNext()){
                    Machine outMachine = OutMachineIter.next();

                    retString += "|" + "\n";
                    retString += "|" + "\n";
                    retString += "|____" + "\t" + MachineNames[outMachine.getId()] + "\t" + tempMaterial.getName() + "\n";
                }

            }

            retString += "\n";
            retString += "\n";

        }

        return retString;
    }
}
