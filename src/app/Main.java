package app;

import java.util.Scanner;

import xadrez.ChessPosition;
import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		PartidaXadrez chessMatch = new PartidaXadrez();
		
		while(true) {
			UI.printBoard(chessMatch.getPecas());
			System.out.println();
			System.out.print("Source: ");
			ChessPosition source = UI.readChessPosition(sc);
			
			System.out.println();
			System.out.print("Target: ");
			ChessPosition target = UI.readChessPosition(sc);
			
			PecaXadrez capturedPiece = chessMatch.performChessMove(source, target);
		}
		
	}
}
