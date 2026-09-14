package MachinePackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;

public class MachineFactory{

    ArrayList<Machine> MachineList;
    ArrayList<Material> MaterialList;

    Integer numMachines;
    Integer numMaterials;
    
    public MachineFactory(){};

    public void buildFactoryFromFile(String FilePath){

        try (BufferedReader br = new BufferedReader(new FileReader(FilePath))) {

            String line;

            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }catch (IOException e){
        System.out.println("Error reading file.");
        }

    }
    
    
}
