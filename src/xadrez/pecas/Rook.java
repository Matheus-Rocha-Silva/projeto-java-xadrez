package xadrez.pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class Rook extends PecaXadrez {

	public Rook(Tabuleiro board, Cor cor) {
		super(board, cor);
	}

	@Override
	public String toString() {
		return "R";
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getLinhas()][getBoard().getColunas()];

		Posicao p = new Posicao(0, 0);

		// Acima
		p.setValues(posicao.getLinha() - 1, posicao.getColuna());

		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setLinha(p.getColuna() - 1);
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// Esquerda
		p.setValues(posicao.getLinha(), posicao.getColuna() - 1);

		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setColuna(p.getColuna() - 1);
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// Direita
		p.setValues(posicao.getLinha(), posicao.getColuna() + 1);

		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setColuna(p.getColuna() + 1);
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		// Abaixo
		p.setValues(posicao.getLinha() + 1, posicao.getColuna());

		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
			p.setColuna(p.getColuna() + 1);
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getLinha()][p.getColuna()] = true;
		}

		return mat;
	}
}
