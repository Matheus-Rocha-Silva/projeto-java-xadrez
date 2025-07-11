package app;

import java.util.InputMismatchException;
import java.util.Scanner;

import xadrez.ChessPosition;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class UI {

	// https://stackoverflow.com/questions/5762491/how-to-print-color-in-console-using-system-out-println

	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";

	public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
	public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
	public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
	public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
	public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
	public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
	public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

	public static void clearScreen() {
		System.out.println("\033[H\033[2J");
		System.out.flush();
	}
	
	
	public static ChessPosition readChessPosition(Scanner sc) {
		try {
			String s = sc.nextLine();
			char coluna = s.charAt(0);
			int linha = Integer.parseInt(s.substring(1));
			return new ChessPosition(coluna, linha);
		}catch(RuntimeException e) {
			throw new InputMismatchException("Erro ao ler ChessPosition. Valores validos são de a1 ao h8");
		}
	}
	
	
	
	public static void printBoard(PecaXadrez[][] pecas) {
		for (int i = 0; i < pecas.length; i++) {
			System.out.println((8 - i) + " ");
			for (int j = 0; j < pecas.length; j++) {
				printPiece(pecas[i][j], false);
			}
			System.out.println();
		}
		System.out.println("  a b c d e f g h");
	}
	
	
	public static void printBoard(PecaXadrez[][] pecas, boolean[][] possibleMoves) {
		for (int i = 0; i < pecas.length; i++) {
			System.out.println((8 - i) + " ");
			for (int j = 0; j < pecas.length; j++) {
				printPiece(pecas[i][j], possibleMoves[i][j]);
			}
			System.out.println();
		}
		System.out.println("  a b c d e f g h");
	}
	

	/*private static void printPiece(PecaXadrez peca) {
		if (peca == null) {
			System.out.println("-");
		} else {
			System.out.println(peca);
		}
		System.out.println(" ");*/

	private static void printPiece(PecaXadrez piece, boolean background) {
		if(background) {
			System.out.println(ANSI_BLUE_BACKGROUND + piece + ANSI_RESET);
		}
		if (piece == null) {
			System.out.print("-" + ANSI_RESET);
		} else {
			if (piece.getCor() == Cor.BRANCO) {
				System.out.print(ANSI_WHITE + piece + ANSI_RESET);
			} else {
				System.out.print(ANSI_YELLOW + piece + ANSI_RESET);
			}
		}
		System.out.println(" ");
	}
	
}
