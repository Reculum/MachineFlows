
package MachinePackage;


public class Material {

    private String name;

    public Material(String textId){
        name = textId;
    }

    public Boolean isEqualTo(Material second){
        return (second.getName() == this.name);
    }

    public String getName(){return this.name;}
    
    
}