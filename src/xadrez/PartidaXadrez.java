package xadrez;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.King;
import xadrez.pecas.Rook;

public class PartidaXadrez {
	
	private Tabuleiro board;
	
	public PartidaXadrez() {
		board = new Tabuleiro(8, 8);
		initialSetup();
	}
	
	public PecaXadrez[][] getPecas(){
		PecaXadrez[][] mat = new PecaXadrez[board.getLinhas()][board.getColunas()];
		for(int i = 0; i < board.getLinhas(); i++) {
			for(int j = 0; j < board.getColunas(); j++) {
				mat[i][j] = (PecaXadrez) board.peca(i, j);
			}
		}
		return mat;
	}
	
	private void initialSetup() {
		board.placePiece(new Rook(board, Cor.BRANCO), new Posicao(2, 1));
		board.placePiece(new King(board, Cor.PRETO), new Posicao(0, 4));
		board.placePiece(new King(board, Cor.BRANCO), new Posicao(7, 4));
	}
}
