package xadrez;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;

public abstract class PecaXadrez extends Peca{
	
	private Cor cor;

	public PecaXadrez(Tabuleiro board, Cor cor) {
		super(board);
		this.cor = cor;
	}

	public Cor getCor() {
		return cor;
	}

	
	protected boolean isThereOpponentPiece(Posicao posicao) {
		PecaXadrez p = (PecaXadrez)getBoard().peca(posicao);
		return p != null && p.getCor() != cor;
	}
}
