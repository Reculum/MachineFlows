
package MachinePackage;


public class Material {

    private String name;

    public Material(String textId){
        name = textId;
    }

    @Override
    public boolean equals(Object other){

        if (this == other){return true;}

        if (other == null || getClass() != other.getClass()) return false;

        Material altro = (Material) other;

        return  (altro.getName().equals(name));
    }

    @Override
    public int hashCode(){
        return name.hashCode();
    }

    public String getName(){return name;}
    
    
}