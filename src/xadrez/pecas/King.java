package xadrez.pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PartidaXadrez;
import xadrez.PecaXadrez;

public class King extends PecaXadrez {

	private PartidaXadrez chessMatch;

	public King(Tabuleiro board, Cor cor, PartidaXadrez chessMatch) {
		super(board, cor);
		this.chessMatch = chessMatch;
	}

	@Override
	public String toString() {
		return "K";
	}

	private boolean canMove(Posicao posicao) {
		PecaXadrez p = (PecaXadrez) getBoard().peca(posicao);
		return p == null || p.getCor() != getCor();
	}

	private boolean testRookCastling(Posicao posicao) {
		PecaXadrez p = (PecaXadrez) getBoard().peca(posicao);
		return p != null && p instanceof Rook && p.getCor() == getCor() && p.getMoveCount() == 0;
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getLinhas()][getBoard().getColunas()];

		Posicao p = new Posicao(0, 0);

		// Acima
		p.setValues(posicao.getLinha() - 1, posicao.getColuna());
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// Abaixo
		p.setValues(posicao.getLinha() + 1, posicao.getColuna());
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// Esquerda
		p.setValues(posicao.getLinha(), posicao.getColuna() - 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// Direita
		p.setValues(posicao.getLinha(), posicao.getColuna() + 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// NO
		p.setValues(posicao.getLinha() - 1, posicao.getColuna() - 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// NE
		p.setValues(posicao.getLinha() - 1, posicao.getColuna() + 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// SW
		p.setValues(posicao.getLinha() + 1, posicao.getColuna() - 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// SE
		p.setValues(posicao.getLinha() + 1, posicao.getColuna() + 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// Movimento Especial Castling
		if (getMoveCount() == 0 && !chessMatch.getCheck()) {
			// Movimento especial kingside rook
			Posicao posT1 = new Posicao(posicao.getLinha(), posicao.getColuna());
			if (testRookCastling(posT1)) {
				Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() + 1);
				Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() + 2);
				if (getBoard().peca(p1) == null && getBoard().peca(p2) == null) {
					mat[posicao.getLinha()][posicao.getColuna() + 2] = true;
				}

			}

			// Movimento especial queenside rook
			Posicao posT2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 4);

			if (testRookCastling(posT2)) {
				Posicao p1 = new Posicao(posicao.getLinha(), posicao.getColuna() - 1);
				Posicao p2 = new Posicao(posicao.getLinha(), posicao.getColuna() - 2);
				Posicao p3 = new Posicao(posicao.getLinha(), posicao.getColuna() - 3);
				if (getBoard().peca(p1) == null && getBoard().peca(p2) == null && getBoard().peca(p3) == null) {
					mat[posicao.getLinha()][posicao.getColuna() - 2] = true;
				}
			}
		}
		return mat;
	}

}
