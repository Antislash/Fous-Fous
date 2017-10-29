import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
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

	//Enumération pour les joueurs
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
	
	//Plateau de jeu
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
		
		ArrayList<String> coupsPossible = mouvementsPossibles(player);
		
		return coupsPossible.contains(move);
	}

	@Override
	public ArrayList<String> mouvementsPossibles(String player) {
		// TODO Auto-generated method stub
		boolean prise1 = false, prise2 = false, prise3 = false, prise4 = false; //Servira à savoir si le pion courant peut faire au moins une prise
		String adversaire = player == "r" ? "b" : "r";
		
		ArrayList<String> coupsPossible = new ArrayList<String>();
		
		//Pour chaque pion on regarde si ils peuvent d'abord faire des prises et cb ou sinon si ils peuvent faire des menaces
		for(int i = 0; i < TAILLE; i ++){
			for(int j = 0; j < TAILLE; j++){
				if(plateau[i][j].toString().equals(player)){
					
					/**
					 * On regarde d'abord pour les prises
					 */
					int abs = j, ord = i;
					
					//i++ et j--					
					prise1 = cherchePionEnnemi(abs-1,ord+1, -1, +1, player, adversaire, true, coupsPossible, i, j);

					//i++ et j++
					prise2 = cherchePionEnnemi(abs+1,ord+1, +1, +1, player, adversaire, true, coupsPossible, i, j);

					//i-- et j++
					prise3 = cherchePionEnnemi(abs+1,ord-1, +1, -1, player, adversaire, true, coupsPossible, i, j);
					
					//i-- et j--
					prise4 = cherchePionEnnemi(abs-1,ord-1, -1, -1, player, adversaire, true, coupsPossible, i, j);
					
					/**
					 * FIN de vérification pour les prises
					 */
					
					//Si le pion ne peut faire aucune prise on regarde si il peut faire une menace
					if(!prise1 && !prise2 && !prise3 && !prise4){
						//On vérifie sur chaque diagonales si on peut menacer un pion ennemi
						
						abs = j-1; ord = i+1;
						//Diagonale i++ et j--
						while(abs >= 0 && ord < TAILLE && plateau[ord][abs] == Player.v){
							//On regarde si on menace un pion dans une des 4 diagonales
							if(cherchePionEnnemi(abs,ord, -1, +1, player, adversaire, false, null, i, j) || 
									cherchePionEnnemi(abs,ord, +1, +1, player, adversaire, false, null, i, j) ||
									cherchePionEnnemi(abs,ord, +1, -1, player, adversaire, false, null, i, j) ||
									cherchePionEnnemi(abs,ord, -1, -1, player, adversaire, false, null, i, j)){
								coupsPossible.add(changeChiffreParLettre(j)+i+"-"+changeChiffreParLettre(abs)+ord);
							}
							abs--; ord++;
						}

						//Diagonale i++ et j++
						abs = j+1; ord = i+1;
						while(abs < TAILLE && ord < TAILLE && plateau[ord][abs] == Player.v){
							//On regarde si on menace un pion dans une des 4 diagonales
							if(cherchePionEnnemi(abs,ord, -1, +1, player, adversaire, false, null, i, j) || 
									cherchePionEnnemi(abs,ord, +1, +1, player, adversaire, false, null, i, j) ||
									cherchePionEnnemi(abs,ord, +1, -1, player, adversaire, false, null, i, j) ||
									cherchePionEnnemi(abs,ord, -1, -1, player, adversaire, false, null, i, j)){
								coupsPossible.add(changeChiffreParLettre(j)+i+"-"+changeChiffreParLettre(abs)+ord);
							}

							abs++; ord++;
						}

						//Diagonale i-- et j++
						abs = j+1; ord = i-1;
						while(abs < TAILLE && ord >= 0 && plateau[ord][abs] == Player.v){
							
							//On regarde si on menace un pion dans une des 4 diagonales
							if(cherchePionEnnemi(abs,ord, -1, +1, player, adversaire, false, null, i, j) || 
									cherchePionEnnemi(abs,ord, +1, +1, player, adversaire, false, null, i, j) ||
									cherchePionEnnemi(abs,ord, +1, -1, player, adversaire, false, null, i, j) ||
									cherchePionEnnemi(abs,ord, -1, -1, player, adversaire, false, null, i, j)){
								coupsPossible.add(changeChiffreParLettre(j)+i+"-"+changeChiffreParLettre(abs)+ord);
							}
							abs++; ord--;
						}
						
						//Diagonale i-- et j--
						abs = j-1; ord = i-1;
						while(abs >= 0 && ord >= 0 && plateau[ord][abs] == Player.v){
							
							//On regarde si on menace un pion dans une des 4 diagonales
							if(cherchePionEnnemi(abs,ord, -1, +1, player, adversaire, false, null, i, j) || 
									cherchePionEnnemi(abs,ord, +1, +1, player, adversaire, false, null, i, j) ||
									cherchePionEnnemi(abs,ord, +1, -1, player, adversaire, false, null, i, j) ||
									cherchePionEnnemi(abs,ord, -1, -1, player, adversaire, false, null, i, j)){
								coupsPossible.add(changeChiffreParLettre(j)+i+"-"+changeChiffreParLettre(abs)+ord);
							}
							abs--; ord--;
						}
					}
				}
			}
		}
		
		
		return coupsPossible;
	}

	//Fonction récursive pour trouver les coups valides
	private boolean cherchePionEnnemi(int abs, int ord, int depX, int depY, String player, String adversaire, boolean ajouterArrayList, ArrayList<String> coups, int i, int j){
		
		if(ord >= plateau.length || ord < 0 || abs >= plateau.length || abs < 0) return false;
		if(plateau[ord][abs].toString().equals(player)) return false;
		if(plateau[ord][abs].toString().equals(adversaire)) {
			if(ajouterArrayList){
				coups.add(changeChiffreParLettre(j)+i+"-"+changeChiffreParLettre(abs)+ord);
			}
			return true;
		}
		
		return cherchePionEnnemi(abs + depX, ord + depY, depX, depY, player, adversaire, ajouterArrayList, coups, i, j);
	}
	
	@Override
	public void play(String move, String player) {
		// TODO Auto-generated method stub
		
		int pLettre = changeLettreParChiffre(move.charAt(0));
		int sLettre = changeLettreParChiffre(move.charAt(3));
		
		if(pLettre != -1 && sLettre != -1 && isNumeric(""+move.charAt(1)) && isNumeric(""+move.charAt(4))){
			
			//On déplace les pions
			plateau[Integer.parseInt(move.charAt(4) + "")][sLettre] = plateau[ Integer.parseInt(move.charAt(1) + "")][ pLettre];
			plateau[ Integer.parseInt(move.charAt(1) + "")][ pLettre]= Player.v;
			
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
	
	private String changeChiffreParLettre(int nb){
		String caract = "";
		
		switch(nb){
		case 0 : caract = "A"; break;
		case 1 : caract = "B"; break;
		case 2 : caract = "C"; break;
		case 3 : caract = "D"; break;
		case 4 : caract = "E"; break;
		case 5 : caract = "F"; break;
		case 6 : caract = "G"; break;
		case 7 : caract = "H"; break;
		default : caract = ""; break;
		}
		
		return caract;
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
		
		return !(b || r);
	}
	
	public static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
	
	//Affichage de la map
	public void showMap(){
		System.out.println("   A B C D E F G H");
		for(int i = 0; i < TAILLE; i++){
			System.out.print(i + "|");
			for(int j = 0; j < TAILLE; j++){
				System.out.print(" " + plateau[i][j]);
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
						   p.setFromFile("test.txt");
						   break;
			}
			
			while(!p.finDePartie()){
				p.showMap();
				e = p.getJoueurCourant() ? Player.b : Player.r;
				p.showMouvementPossible(e.toString());
				do
				{
					System.out.println("Joueur " + e + " quel coup voulez vous jouez (exemple A0-B5) :");
					str = sc.nextLine();
				}while(!p.estValide(str,e.toString()));
				
				//Finir le déplacement
				p.play(str,e.toString());
				
			}
			
		}
	}

	//Fonction de test interne
	public void showMouvementPossible(String player){
		ArrayList<String> test = mouvementsPossibles(player);
		
		for(String t : test){
			System.out.print(t+ " / ");
		}
	}
}
