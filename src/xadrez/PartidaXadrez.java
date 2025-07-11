package xadrez;

import tabuleiro.Peca;
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

	public PecaXadrez[][] getPecas() {
		PecaXadrez[][] mat = new PecaXadrez[board.getLinhas()][board.getColunas()];
		for (int i = 0; i < board.getLinhas(); i++) {
			for (int j = 0; j < board.getColunas(); j++) {
				mat[i][j] = (PecaXadrez) board.peca(i, j);
			}
		}
		return mat;
	}
	
	public boolean[][] possibleMoves(ChessPosition sourcePosition){
		Posicao posicao = sourcePosition.toPosition();
		validateSourcePosition(posicao);
		return board.peca(posicao).possibleMoves();
	}
	

	public PecaXadrez performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Posicao source = sourcePosition.toPosition();
		Posicao target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Peca capturedPiece = makeMove(source, target);
		return (PecaXadrez) capturedPiece;
	}
	
	private Peca makeMove(Posicao source, Posicao target) {
		Peca p = board.removePiece(source);
		Peca capturedPiece = board.removePiece(target);
		board.placePiece(p, target);
		return capturedPiece;
	}
	
	
	private void validateSourcePosition(Posicao posicao) {
		if(!board.thereIsAPiece(posicao)) {
			throw new ChessException("Não há posição no tabuleiro");
		}
		if(!board.peca(posicao).isThereAnyPossibleMove()) {
			throw new ChessException("Não há movimentos possiveis para a peça escolhida.");
		}
	}
	
	private void validateTargetPosition(Posicao source, Posicao target) {
		if(!board.peca(source).possibleMove(target)) {
			throw new ChessException("A peça escolhida não pode se mover para a posição ed destino.");
		}
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
