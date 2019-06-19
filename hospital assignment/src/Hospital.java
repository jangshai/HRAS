public class Hospital{
	public int name;
	public int level;
	public int distance;
	public int EOB; //Emergency observation bed
	public int emptybed;
	public Hospital(int name,int level, int distance,int EOB,int emptybed) {
		this.name=name;
		this.level=level;
		this.distance=distance;
		this.EOB=EOB;
		this.emptybed=emptybed;
			// TODO Auto-generated constructor stub
		}
	public int getlevel(){
		return this.level;
	}
	public int getdistance(){
		return this.distance;
	}
	public int getEOB()
	{
		return this.EOB;
	}
	public int getemptybed(){
		return this.emptybed;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}

