
public interface Partie1 {
	/**
	 * 
	 * @param fileName
	 */
	public void setFromFile(String fileName);
	
	/**
	 * 
	 * @param fileName
	 */
	public void saveToFile(String fileName);
	
	public boolean estValide(String move, String player);
	
	public String[] mouvementsPossibles(String player);
	
	public void play(String move, String player);
	
	public boolean finDePartie();
}
