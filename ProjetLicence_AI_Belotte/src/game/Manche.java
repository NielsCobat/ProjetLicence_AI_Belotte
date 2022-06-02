package game;

import java.util.ArrayList;
import java.util.Scanner;

import AI.Entrainement;
import AI.NeuralNetwork;
import assets.Couleur;

public class Manche {

	private Pli[] plis;
	private int idPremierJoueur, equipePreneur, nbPlis;
	private int[] pointsEquipe, belotte;

	//variables pour l'entrainement
	public Joueur j1, j2, j3, j4;
	public Equipe e1, e2;
	public ArrayList<Carte> ensCarte;

	/**
	 * Constructeur de la classe Manche
	 * 
	 * @param atout           l'atout de la premi�re manche
	 * @param idPremierJoueur id du premier joueur � jouer
	 * @param joueurPreneur   id du joueur prenant l'atout
	 * @throws Exception si l'un des id de joueurs n'est pas valide
	 */
	public Manche(int idPremierJoueur, int joueurPreneur) throws Exception {
		if ((this.idPremierJoueur > 4 || this.idPremierJoueur < 1) && (joueurPreneur > 4 || joueurPreneur < 1))
			throw new Exception("game.Manche.Manche() : Un id de joueur n'est pas valide");

		this.plis = new Pli[8];
		this.idPremierJoueur = idPremierJoueur;
		this.equipePreneur = (joueurPreneur + 1) % 2; // soit 0 = joueurs 1 et 3 ; 1 = joueurs 2 et 4
		this.pointsEquipe = new int[2];
		this.pointsEquipe[0] = 0; // joueurs 1 et 3
		this.pointsEquipe[1] = 0; // joueurs 2 et 4
		this.nbPlis = 0;
		this.belotte = new int[2];
		this.j1 = null;
		this.j2 = null;
		this.j3 = null;
		this.j4 = null;
		this.e1 = null;
		this.e2 = null;
		this.ensCarte = null;
	}

	public Manche(int idPremierJoueur, int joueurPreneur, Joueur j1, Joueur j2, Joueur j3, Joueur j4, ArrayList<Carte> ensCarte) throws Exception {
		if ((this.idPremierJoueur > 4 || this.idPremierJoueur < 1) && (joueurPreneur > 4 || joueurPreneur < 1))
			throw new Exception("game.Manche.Manche() : Un id de joueur n'est pas valide");

		this.plis = new Pli[8];
		this.idPremierJoueur = idPremierJoueur;
		this.equipePreneur = (joueurPreneur + 1) % 2; // soit 0 = joueurs 1 et 3 ; 1 = joueurs 2 et 4
		this.pointsEquipe = new int[2];
		this.pointsEquipe[0] = 0; // joueurs 1 et 3
		this.pointsEquipe[1] = 0; // joueurs 2 et 4
		this.nbPlis = 0;
		this.belotte = new int[2];
		this.j1 = j1;
		this.j2 = j2;
		this.j3 = j3;
		this.j4 = j4;
		this.e1 = null;
		this.e2 = null;
		this.ensCarte = ensCarte;
	}

	/**
	 * Gestion de la fin d'une manche (points etc)
	 */
	public void finManche() {
		if (this.nbPlis == 8) {

			// calcul des points dans la classe Partie � la fin

			if (Table.joueur1.aBelote || Table.joueur3.aBelote)
				this.belotte[0] = 20;
			else if (Table.joueur2.aBelote || Table.joueur4.aBelote)
				this.belotte[1] = 20;
			System.out.println("Fin de Manche ! ");
			System.out.print("Points de l'�quipe 1 : " + this.pointsEquipe[0]);
			if (this.belotte[0] == 20) System.out.print(" + " + this.belotte[0] + " points de Belote");
			System.out.println();
			System.out.print("Points de l'�quipe 2 : " + this.pointsEquipe[1]);
			if (this.belotte[1] == 20) System.out.print(" + " + this.belotte[1] + " points de Belote");
			System.out.println();

			if (this.pointsEquipe[0] == 162) { //�quipe 2 capot
				this.pointsEquipe[0] = 252;
				System.out.println("�quipe 2 capot ! ");
			}


			else if (this.pointsEquipe[1] == 162) {//�quipe 1 capot
				this.pointsEquipe[1] = 252;
				System.out.println("�quipe 2 capot ! ");
			}


			else if (this.pointsEquipe[(this.equipePreneur)] < 82	  
					&& this.belotte[(this.equipePreneur)] + this.belotte[(this.equipePreneur + 1) % 2] == 0 //Si une �quipe prend et qu'il n'y a pas de Belote
					|| (this.pointsEquipe[(this.equipePreneur + 1) % 2] + this.belotte[(this.equipePreneur + 1) % 2] >= 91  //Ou
					&& this.belotte[(this.equipePreneur)] + this.belotte[(this.equipePreneur + 1) % 2] == 20 )) { //Si une �quipe prend et qu'il y a une Belote	
				this.pointsEquipe[this.equipePreneur] = 0;
				this.pointsEquipe[(this.equipePreneur + 1) % 2] = 162;
				if (this.equipePreneur == 0) System.out.println("�quipe 1 dedans ! ");
				else System.out.println("�quipe 2 dedans ! ");

			} 

			Table.equipe1.addScore(this.pointsEquipe[0] + this.belotte[0]);
			Table.equipe2.addScore(this.pointsEquipe[1] + this.belotte[1]);

			System.out.println("Score �quipe 1 : " + Table.equipe1.score);
			System.out.println("Score �quipe 2 : " + Table.equipe2.score);
			// On remet les cartes des plis dans le paquet, en commen�ant par les plis de l'�quipe qui ne prend pas.
			ArrayList<Carte> pasPris = new ArrayList<Carte>();
			ArrayList<Carte> pris = new ArrayList<Carte>();
			for (int i = 0; i < plis.length; i++){ 
				if (plis[i].equipeGagnante() != equipePreneur) //L'�quipe qui n'a pas pris retourne son tas de plis et compte les cartes une � une
					for(int j = plis[i].getCartes().length - 1; j >= 0; j--) 
						pasPris.add(plis[i].getCartes()[j]);
				else
					for(int j = 0; j < plis[i].getCartes().length; j++)
						pris.add(0, plis[i].getCartes()[j]);
			}
			Table.ensCartes.addAll(pasPris);
			Table.ensCartes.addAll(pris);
		}
		Table.joueur1.hasBelote(Table.atout);
		Table.joueur2.hasBelote(Table.atout);
		Table.joueur3.hasBelote(Table.atout);
		Table.joueur4.hasBelote(Table.atout);
		Table.mancheCour++;
	}

	/**
	 * Reset de la manche courante
	 * 
	 * @param atout         le nouvel atout
	 * @param joueurPreneur le joueur prenant
	 * @throws Exception si l'id du joueur prenant n'est pas valide
	 */
	public void reset(int joueurPreneur) throws Exception {
		if (joueurPreneur > 4 || joueurPreneur < 1)
			throw new Exception("game.Manche.reset() : Un id de joueur n'est pas valide");

		if (this.idPremierJoueur < 4)
			this.idPremierJoueur++;
		else
			this.idPremierJoueur = 1;
		this.equipePreneur = (joueurPreneur + 1) % 2; // soit 0 = joueurs 1 et 3 ; 1 = joueurs 2 et 4
		this.pointsEquipe[0] = 0; // joueurs 1 et 3
		this.pointsEquipe[1] = 0; // joueurs 2 et 4
		this.nbPlis = 0;
		this.belotte[0] = 0;
		this.belotte[1] = 0;
		//this.initPliSuivant(); // pas s�r que �a doive �tre ici

		//resetManche des IA
		if(Table.joueur1 instanceof NeuralNetwork) {
			((NeuralNetwork) Table.joueur1).resetManche();
		}
		if(Table.joueur2 instanceof NeuralNetwork) {
			((NeuralNetwork) Table.joueur2).resetManche();
		}
		if(Table.joueur3 instanceof NeuralNetwork) {
			((NeuralNetwork) Table.joueur3).resetManche();
		}
		if(Table.joueur4 instanceof NeuralNetwork) {
			((NeuralNetwork) Table.joueur4).resetManche();
		}
	}

	/**
	 * Ajout du score d'un pli � une �quipe
	 * 
	 * @param equipe l'id de l'�quipe (0 pour joueurs 1 3 et 1 pour joueurs 2 4)
	 * @param score  le score � incr�menter
	 */
	public void addScoreEquipe(int equipe, int score) {
		this.pointsEquipe[equipe] += score;
	}

	//	/**
	//	 * Initialisation du pli suivant et incr�mentation du score du pli pr�c�dent au
	//	 * score de la manche
	//	 * 
	//	 * @throws Exception si le pli pr�c�dent n'est pas termin� ou si le nombre de
	//	 *                   pli maximum a d�j� �t� atteint
	//	 */
	//	public void initPliSuivant() throws Exception {
	//		if (this.nbPlis == 8)
	//			throw new Exception("game.Manche.initPliSuivant() : la manche contient d�j� 8 plis");
	//		try {
	//			if (this.plis[this.nbPlis - 1].getNbCarte() < 4)
	//				throw new Exception("game.Manche.initPliSuivant() : le pli actuel n'est pas termin�");
	//			this.pointsEquipe[this.plis[this.nbPlis - 1].equipeGagnante()] += this.plis[this.nbPlis - 1].calculPoints();
	//			this.plis[this.nbPlis] = new Pli(this.plis[this.nbPlis - 1].getIdJoueurGagnant());
	//		} catch (Exception e) {
	//			this.plis[0] = new Pli(this.idPremierJoueur);
	//		}
	//		this.nbPlis++;
	//	}

	public void runManche(Couleur atout) {
		boolean half = false; //Pour savoir si la moiti� de la belote a �t� utilis�e.
		Joueur jCourant = Table.joueurCourant;
		Scanner sc = Table.scannerString;
		String carteAJouer = "";
		Table.joueur1.hasBelote(atout);
		Table.joueur2.hasBelote(atout);
		Table.joueur3.hasBelote(atout);
		Table.joueur4.hasBelote(atout);


		//init des IA

		if(Table.joueur1 instanceof NeuralNetwork) {
			((NeuralNetwork) Table.joueur1).initInput();
		}
		if(Table.joueur2 instanceof NeuralNetwork) {
			((NeuralNetwork) Table.joueur2).initInput();
		}
		if(Table.joueur3 instanceof NeuralNetwork) {
			((NeuralNetwork) Table.joueur3).initInput();
		}
		if(Table.joueur4 instanceof NeuralNetwork) {
			((NeuralNetwork) Table.joueur4).initInput();

		}


		//Une manche == 8 plis
		for (int i=0 ; i<8 ; i++) {


			System.out.println("Pli num�ro " + (i+1));
			System.out.println("L'�quipe " + (equipePreneur+ 1 ) + " a pris.");

			//resetPli des IA
			if(Table.joueur1 instanceof NeuralNetwork) {
				((NeuralNetwork) Table.joueur1).resetPli();
			}
			if(Table.joueur2 instanceof NeuralNetwork) {
				((NeuralNetwork) Table.joueur2).resetPli();
			}
			if(Table.joueur3 instanceof NeuralNetwork) {
				((NeuralNetwork) Table.joueur3).resetPli();
			}
			if(Table.joueur4 instanceof NeuralNetwork) {
				((NeuralNetwork) Table.joueur4).resetPli();
			}

			/*try {
				initPliSuivant();
			} catch (Exception e) {
				e.printStackTrace();
			}*/

			//initialisation du i �me pli

			plis[i] = new Pli(idPremierJoueur); 
			while (idPremierJoueur != Table.joueurCourant.id) {
				jCourant = Table.joueurSuivant();
				Table.joueurCourant = Table.joueurSuivant();
			}

			//Tour de table
			boolean aLaCarte;
			boolean played;

			for(int j=0 ; j<4 ; j++) {
				aLaCarte = false;
				played = false;
				while (!played) {
					if ( jCourant.id == 1 || jCourant.id == 3 ) System.out.println("Joueur " + jCourant.id + " �quipe 1");
					else System.out.println("Joueur " + jCourant.id + " �quipe 2");
					System.out.println("Entrez le nom de la carte � jouer.");
					System.out.println("Atout : " + atout);
					System.out.print("Main : ");
					jCourant.printMain();
					System.out.println("\n�tat actuel du pli : " + plis[i].toString());
					if (j > 0) System.out.println("�quipe ma�tresse : " + (plis[i].equipeGagnante()));
					carteAJouer = sc.nextLine();

					for(Carte carte : jCourant.main) {
						if (carte.toString().toLowerCase().equals(carteAJouer.toLowerCase())) {
							aLaCarte = true;

							if (jCourant.isLegalMove(carte)) {
								if (jCourant.aBelote && ((carte.getValeur().name().equals("Dame") || (carte.getValeur().name().equals("Roi"))
										&& carte.getCouleur().name().equals(atout.name())))) {
									if(!half) {
										System.out.println("Belote ! ");
										half = !half;
									}
									else {
										System.out.println("Rebelote ! ");
									}
								}
								if(jCourant instanceof NeuralNetwork) {
									((NeuralNetwork) jCourant).joueCoup();
								}else {
									jCourant.joueCoup(carte);
								}
								played = true;
								jCourant = Table.joueurSuivant();
								Table.joueurCourant = Table.joueurSuivant();
							}
							else System.out.println("Coup impossible.");
							break;
						}
					}

					if (!aLaCarte) System.out.println("Vous n'avez pas cette carte ! ");
				}

			}

			//recuperation id equipe gagnante et id premier joueur du pli suivant	
			int idGagnante = plis[i].equipeGagnante() - 1; 
			idPremierJoueur = plis[i].getIdJoueurGagnant();
			System.out.println("\n\nId du joueur gagnant est " + idPremierJoueur + "\n\n");

			//attribution des points du pli a l'equipe gagnante
			pointsEquipe[idGagnante] += plis[i].calculPoints();
			if (i == 7) pointsEquipe[idGagnante] += 10; // 10 de der
			nbPlis++;
		}
		//attribution des points remportes par chaque equipe
		finManche();
	}

	public void runMancheEntrainement(Couleur atout) throws Exception {

		boolean half = false; //Pour savoir si la moiti� de la belote a �t� utilis�e.
		j1 = new NeuralNetwork("", 1, 3);
		j2 = new NeuralNetwork("", 2, 4);
		j3 = new NeuralNetwork("", 3, 1);
		j4 = new NeuralNetwork("", 4, 2);
		e1 = new Equipe(1, j1, j3, 0);
		e2 = new Equipe(2, j2, j4, 0);
		e1.score = 0;
		e2.score = 0;
		Joueur joueurCourant;
		int idJoueurCourant = (int) (Math.random() * (4 - 1 + 1) + 1); // selectionne un int entre 1 et 4
		switch (idJoueurCourant) {
		case 1:
			joueurCourant = j1;
			break;
		case 2:
			joueurCourant = j2;
			break;
		case 3:
			joueurCourant = j3;
			break;
		case 4:
			joueurCourant = j4;
			break;
		default:
			throw new Exception("identifiant du joueur non compatible");
		}
		ArrayList<Carte> ensCartes = new ArrayList<Carte>();
		Entrainement.setEnsCartes();

		//distribution directe sans choix de l'atout
		for(int i=0; i<8;i++) {
			j1.main.add(ensCartes.get(0));
			ensCartes.remove(0);
		}
		for(int i=0; i<8;i++) {
			j2.main.add(ensCartes.get(0));
			ensCartes.remove(0);
		}
		for(int i=0; i<8;i++) {
			j3.main.add(ensCartes.get(0));
			ensCartes.remove(0);
		}
		for(int i=0; i<8;i++) {
			j4.main.add(ensCartes.get(0));
			ensCartes.remove(0);
		}

		//Scanner sc = Table.scannerString;
		//String carteAJouer = "";
		j1.hasBelote(atout);
		j2.hasBelote(atout);
		j3.hasBelote(atout);
		j4.hasBelote(atout);


		//init des IA

		if(j1 instanceof NeuralNetwork) {
			((NeuralNetwork) j1).initInput();
		}
		if(j2 instanceof NeuralNetwork) {
			((NeuralNetwork) j2).initInput();
		}
		if(j3 instanceof NeuralNetwork) {
			((NeuralNetwork) j3).initInput();
		}
		if(j4 instanceof NeuralNetwork) {
			((NeuralNetwork) j4).initInput();

		}


		//Une manche == 8 plis
		for (int i=0 ; i<8 ; i++) {


			//System.out.println("Pli num�ro " + (i+1));
			//System.out.println("L'�quipe " + (equipePreneur+ 1 ) + " a pris.");

			//resetPli des IA
			if(j1 instanceof NeuralNetwork) {
				((NeuralNetwork) j1).resetPli();
			}
			if(j2 instanceof NeuralNetwork) {
				((NeuralNetwork) j2).resetPli();
			}
			if(j3 instanceof NeuralNetwork) {
				((NeuralNetwork) j3).resetPli();
			}
			if(j4 instanceof NeuralNetwork) {
				((NeuralNetwork) j4).resetPli();
			}

			/*try {
				initPliSuivant();
			} catch (Exception e) {
				e.printStackTrace();
			}*/

			//initialisation du i �me pli

			plis[i] = new Pli(idPremierJoueur); 
			if(idPremierJoueur != joueurCourant.id) {
				switch(idPremierJoueur) {
				case 1: joueurCourant = j1;
				break;
				case 2: joueurCourant = j2;
				break;
				case 3: joueurCourant = j3;
				break;
				case 4: joueurCourant = j4;
				break;
				}
			}

			//Tour de table
			boolean played;

			for(int j=0 ; j<4 ; j++) {
				played = false;
				while (!played) {

					if(joueurCourant instanceof NeuralNetwork) {
						//joue coup
						Carte carteAJouer = ((NeuralNetwork) joueurCourant).joueCoup();
						
						//met � jour si belote
						if (joueurCourant.aBelote && ((carteAJouer.getValeur().name().equals("Dame") || (carteAJouer.getValeur().name().equals("Roi"))
								&& carteAJouer.getCouleur().name().equals(atout.name())))) {
							if(!half) {
								half = !half;
							}
						}
						
					}
					else {
						System.out.println("Erreur: joueurCourant doit �tre une ia");
					}
					
					played = true;
					
					switch(joueurCourant.id) {
					case 1: joueurCourant = j2;
					break;
					case 2: joueurCourant = j3;
					break;
					case 3: joueurCourant = j4;
					break;
					case 4: joueurCourant = j1;
					break;
					}
				}

			}

			//recuperation id equipe gagnante et id premier joueur du pli suivant	
			int idGagnante = plis[i].equipeGagnante() - 1; 
			idPremierJoueur = plis[i].getIdJoueurGagnant();

			//attribution des points du pli a l'equipe gagnante
			pointsEquipe[idGagnante] += plis[i].calculPoints();
			if (i == 7) pointsEquipe[idGagnante] += 10; // 10 de der
			nbPlis++;
		}
		//attribution des points remportes par chaque equipe
		finManche();
	}

	/**
	 * Getter d'un seul pli
	 * 
	 * @param noPli le num�ro du pli souhait� (de 0 � 7)
	 * @return l'objet du pli en question
	 */
	public Pli getPli(int noPli) {
		return this.plis[noPli];
	}

	//	/**
	//	 * Getter de l'id du premier joueur de la manche (joueur apr�s le distribueur)
	//	 * 
	//	 * @return id du joueur en question
	//	 */
	//	public int getIdPremierJoueur() {
	//		return idPremierJoueur;
	//	}

	//	/**
	//	 * Getter de l'�quipe qui a pris � l'atout
	//	 * 
	//	 * @return 0 pour joueurs 1 3 et 1 pour joueurs 2 4
	//	 */
	//	public int getEquipePreneur() {
	//		return equipePreneur;
	//	}

	/**
	 * Getter du nombre de plis jou�s dans la manche
	 * 
	 * @return nombre de plis jou�s
	 */
	public int getNbPlis() {
		return nbPlis;
	}

	/**
	 * Getter du nombre de points d'une �quipe
	 * 
	 * @param l'id du joueur souhait�e (entre 1 et 4)
	 * @return le score de cette �quipe
	 */
	public int getPointsEquipe(int idJoueur) {
		return pointsEquipe[(idJoueur + 1) % 2];
	}

}
