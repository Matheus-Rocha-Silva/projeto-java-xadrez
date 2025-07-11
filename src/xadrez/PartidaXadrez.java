package xadrez;

import tabuleiro.Tabuleiro;
import xadrez.pecas.King;
import xadrez.pecas.Rook;

public class PartidaXadrez {

	private Tabuleiro board;

	public PartidaXadrez() {
		board = new Tabuleiro(8, 8);
		initialSetup();
	}

	public PecaXadrez[][] getPecas() {
		PecaXadrez[][] mat = new PecaXadrez[board.getLinhas()][board.getColunas()];
		for (int i = 0; i < board.getLinhas(); i++) {
			for (int j = 0; j < board.getColunas(); j++) {
				mat[i][j] = (PecaXadrez) board.peca(i, j);
			}
		}
		return mat;
	}

	private void placeNewPiece(char coluna, int linha, PecaXadrez peca) {
		board.placePiece(peca, new ChessPosition(coluna, linha).toPosition());
	}

	private void initialSetup() {
		placeNewPiece('c', 1, new Rook(board, Cor.BRANCO));
		placeNewPiece('c', 2, new Rook(board, Cor.BRANCO));
		placeNewPiece('d', 2, new Rook(board, Cor.BRANCO));
		placeNewPiece('e', 2, new Rook(board, Cor.BRANCO));
		placeNewPiece('e', 1, new Rook(board, Cor.BRANCO));
		placeNewPiece('d', 1, new King(board, Cor.BRANCO));
		placeNewPiece('c', 7, new Rook(board, Cor.PRETO));
		placeNewPiece('c', 8, new Rook(board, Cor.PRETO));
		placeNewPiece('d', 7, new Rook(board, Cor.PRETO));
		placeNewPiece('e', 7, new Rook(board, Cor.PRETO));
		placeNewPiece('e', 8, new Rook(board, Cor.PRETO));
		placeNewPiece('d', 8, new King(board, Cor.PRETO));
	}
}
