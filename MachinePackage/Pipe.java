package MachinePackage;

public class Pipe {

	protected Machine MachineFrom;
	protected Machine MachineTo;
	protected Material MaterialAcross;
	protected int id;

	public Pipe(int id, Machine MachFrom, Machine MachTo, Material MatAcross){
		this.id = id;
		this.MachineFrom = MachFrom;
		this.MachineTo = MachTo;
		this.MaterialAcross = MatAcross;
	}

	public int getId(){return id;}
	public Machine getMachineFrom(){return MachineFrom;}
	public Machine getMachineTo(){return MachineTo;}
	public Material getMaterialAcross(){return MaterialAcross;}
	
}
