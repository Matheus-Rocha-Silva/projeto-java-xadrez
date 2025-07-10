package xadrez.pecas;

import tabuleiro.Tabuleiro;
import xadrez.Cor;
import xadrez.PecaXadrez;

public class King extends PecaXadrez{

	public King(Tabuleiro board, Cor cor) {
		super(board, cor);
	}
	
	@Override
	public String toString() {
		return "K";
	}
	
}
