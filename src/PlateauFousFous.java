import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class PlateauFousFous implements Partie1 {
	
	static final public int TAILLE = 8;

	private enum Player{
			r("r"),b("b"),v("-");
		
			private String symbole;
			
			Player(String s){
				this.symbole = s;
			}
			
			public String toString(){
				 return symbole;
			}
	}
	
	private Player plateau[][] = new Player[TAILLE][TAILLE];
	
	@Override
	public void setFromFile(String fileName) {
		// TODO Auto-generated method stub
		
		// Préparation du fichier
		FileInputStream ips = null;
		
		String file = "";		

		// Ouverture du fichier
		try {
			ips = new FileInputStream(new File(fileName));
			
			InputStreamReader ipsr=new InputStreamReader(ips);
			
			BufferedReader br=new BufferedReader(ipsr);
			
			String ligne;
			while ((ligne=br.readLine())!=null){
				file+=ligne;
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int mem=-1, cpt=0;
		
		for(int i = 0; i < file.length(); i++){
			String carac = "" + file.charAt(i);
			if(isNumeric(carac)){
				//System.out.println(carac);
				
				int numb = Integer.parseInt(carac); 
				
				
				if( numb >0 && numb < 9){
					if(mem != numb){
						mem = numb;
						cpt = 0;
					}
				}
			}
			else{
					
				switch (carac){
					case "-" : plateau[mem-1][cpt] = Player.v; break;
					case "b" : plateau[mem-1][cpt] = Player.b; break;
					case "r" : plateau[mem-1][cpt] = Player.r; break;
				}
				cpt++;
			}
		}
		
	}

	@Override
	public void saveToFile(String fileName) {
		// TODO Auto-generated method stub
		
		String datas = "";
		
		for(int i = 0; i < TAILLE; i ++){
			datas += "" + i;
			
			for(int j = 0; j < TAILLE; j++){
				datas += plateau[i][j];
			}
			
			datas += i + "\n";
		}
		
		try (PrintWriter out = new PrintWriter( fileName )){
			
			out.print(datas);
		}
		
		catch (FileNotFoundException e) {
	         e.printStackTrace();

	    }
		
	}

	@Override
	public boolean estValide(String move, String player) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String[] mouvementsPossibles(String player) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void play(String move, String player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean finDePartie() {
		// TODO Auto-generated method stub
		
		boolean b = false, r = false;
		
		for(int i = 0; i < TAILLE; i++){
			for(int j = 0; j < TAILLE; j++){
				if(plateau[i][j] == Player.b) b = true;
				if(plateau[i][j] == Player.r) r = true;
				if(b && r) break;
			}
			
			if(b && r) break;
		}
		
		return false;
	}
	
	public static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
	
	public void showMap(){
		for(int i = 0; i < TAILLE; i++){
			for(int j = 0; j < TAILLE; j++){
				System.out.print(plateau[i][j]);
			}
			
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		PlateauFousFous p = new PlateauFousFous();
		
		p.setFromFile("test.txt");
		
		p.showMap();
		
		p.saveToFile("out.txt");
	}

}
