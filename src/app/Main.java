package app;

import xadrez.PartidaXadrez;

public class Main {
	public static void main(String[] args) {
		
		PartidaXadrez chessMatch = new PartidaXadrez();
		UI.printBoard(chessMatch.getPecas());
		
	}
}
