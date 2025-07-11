package app;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import xadrez.ChessException;
import xadrez.ChessPosition;
import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;

public class Main {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		PartidaXadrez chessMatch = new PartidaXadrez();
		List<PecaXadrez> captured = new ArrayList<>();
		
		while(!chessMatch.getCheckMate()) {
			try {
			UI.clearScreen();
			UI.printMatch(chessMatch, captured);
			System.out.println();
			System.out.print("Source: ");
			ChessPosition source = UI.readChessPosition(sc);
			
			System.out.println();
			System.out.print("Target: ");
			ChessPosition target = UI.readChessPosition(sc);
			
			boolean[][] possibleMoves = chessMatch.possibleMoves(source);
			UI.clearScreen();
			UI.printBoard(chessMatch.getPecas(), possibleMoves);
			
			PecaXadrez capturedPiece = chessMatch.performChessMove(source, target);

			if(capturedPiece != null) {
				captured.add(capturedPiece);
			}
			
			if(chessMatch.getPromoted() != null) {
				System.out.print("Insira uma peça para promoção (B/N/R/Q): ");
				String type = sc.nextLine().toUpperCase();
				while(!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("R")) {
					type = sc.nextLine().toUpperCase();
				}
				chessMatch.replacePromotedPiece(type);
			}
			
			}catch(ChessException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}catch(InputMismatchException e) {
				System.out.println(e.getMessage());
				sc.nextLine();
			}
		}
		UI.clearScreen();
		UI.printMatch(chessMatch, captured);
	}
}
