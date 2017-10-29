import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class PlateauFousFous implements Partie1 {
	
	static final public int TAILLE = 8;
	
	//Boolean servant à savoir à quel joueur de jouer
	private boolean joueurCourant = false;
	
	public boolean getJoueurCourant(){
		return joueurCourant;
	}
	
	public void changeJoueurCourant(){
		joueurCourant = !joueurCourant;
	}

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
				if(!((ligne.charAt(0) + "").equals("%")))
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
		
		int pLettre = changeLettreParChiffre(move.charAt(0));
		int sLettre = changeLettreParChiffre(move.charAt(3));
		
		if(pLettre != -1 && sLettre != -1 && isNumeric(""+move.charAt(1)) && isNumeric(""+move.charAt(4))){
			
			//On déplace les pions
			plateau[sLettre][Integer.parseInt(move.charAt(4) + "")] = plateau[pLettre][Integer.parseInt(move.charAt(1) + "")];
			
			//Fin du tour on change de joueur
			changeJoueurCourant();
		}
	}

	private int changeLettreParChiffre(char caract){
		int i = -1;
		
		switch(caract){
		case 'A' : i = 0; break;
		case 'B' : i = 1; break;
		case 'C' : i = 2; break;
		case 'D' : i = 3; break;
		case 'E' : i = 4; break;
		case 'F' : i = 5; break;
		case 'G' : i = 6; break;
		case 'H' : i = 7; break;
		default : i = -1; break;
		}
		
		return i;
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
		
		return !(b && r);
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
		
		//Test de chargement à partir d'un fichier
		//p.setFromFile("test.txt");
		//Affichage de la map
		//p.showMap();
		//Ecriture dans un fichier
		//p.saveToFile("out.txt");
		
		Scanner sc = new Scanner(System.in); //Sert à lire les entrées
		String str = null; //On écrit les entrées dans ce string
		Player e = null; //Enumération de joueur
		
		System.out.println("Bonjour appuyé sur 1 si vous voulez charger un fichier, sinon sur 2 pour charger une partie vierge : ");
		str = sc.nextLine();
		if(isNumeric(str)){
			switch(str) {
				case "1" : System.out.println("Veuillez rentrer le nom du fichier :");
						   str = sc.nextLine();
						   p.setFromFile(str);
						   break;
				case "2" : System.out.println("Chargement d'un terrain classique");
						   p.saveToFile("out.txt");
						   break;
			}
			
			while(!p.finDePartie()){
				do
				{
					e = p.getJoueurCourant() ? Player.b : Player.r;
					System.out.println("Joueur " + e + "quel cout voulez vous jouez :");
					str = sc.nextLine();
				}while(!p.estValide(str,e.toString()));
				
				//Finir le déplacement
				p.play(str,e.toString());
				
			}
			
		}
	}

}
