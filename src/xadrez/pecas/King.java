package xadrez.pecas;

import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class King extends PecaXadrez {

	public King(Tabuleiro board, Cor cor) {
		super(board, cor);
	}

	@Override
	public String toString() {
		return "K";
	}

	private boolean canMove(Posicao posicao) {
		PecaXadrez p = (PecaXadrez) getBoard().peca(posicao);
		return p == null || p.getCor() != getCor();
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

		return mat;
	}

}
