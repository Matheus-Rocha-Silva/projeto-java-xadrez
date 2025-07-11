package xadrez;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import tabuleiro.Peca;
import tabuleiro.Posicao;
import tabuleiro.Tabuleiro;
import xadrez.pecas.Bishop;
import xadrez.pecas.King;
import xadrez.pecas.Queen;
import xadrez.pecas.Rook;

public class PartidaXadrez {

	private int turn;
	private Cor currentPlayer;
	private Tabuleiro board;
	private boolean check;
	private boolean checkMate;

	private List<Peca> piecesOnTheBoard = new ArrayList<>();
	private List<Peca> capturedPieces = new ArrayList<>();

	public PartidaXadrez() {
		board = new Tabuleiro(8, 8);
		turn = 1;
		currentPlayer = Cor.BRANCO;
		check = false;
		initialSetup();
	}

	public int getTurn() {
		return turn;
	}

	public Cor getCurrentPlayer() {
		return currentPlayer;
	}

	public boolean getCheck() {
		return check;
	}

	public boolean getCheckMate() {
		return checkMate;
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

	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
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

		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("Você não pode se colocar em cheque");
		}

		check = (testCheck(opponent(currentPlayer))) ? true : false;

		if (testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		} else {
			nextTurn();
		}

		return (PecaXadrez) capturedPiece;
	}

	private Peca makeMove(Posicao source, Posicao target) {
		PecaXadrez p = (PecaXadrez) board.removePiece(source);
		p.increaseMoveCount();
		Peca capturedPiece = board.removePiece(target);
		board.placePiece(p, target);

		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}

		// Movimento especial castling kingside rook
		if (p instanceof King && target.getColuna() == source.getColuna() + 2) {
			Posicao sourceT = new Posicao(source.getLinha(), source.getColuna() + 3);
			Posicao targetT = new Posicao(source.getLinha(), source.getColuna() + 1);
			PecaXadrez rook = (PecaXadrez) board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}

		// Movimento especial castling queenside rook
		if (p instanceof King && target.getColuna() == source.getColuna() - 2) {
			Posicao sourceT = new Posicao(source.getLinha(), source.getColuna() - 4);
			Posicao targetT = new Posicao(source.getLinha(), source.getColuna() - 1);
			PecaXadrez rook = (PecaXadrez) board.removePiece(sourceT);
			board.placePiece(rook, targetT);
			rook.increaseMoveCount();
		}

		return capturedPiece;
	}

	private void undoMove(Posicao source, Posicao target, Peca capturedPiece) {
		PecaXadrez p = (PecaXadrez) board.removePiece(target);
		p.decreaseMoveCount();
		board.placePiece(p, source);

		if (capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}

		// Movimento especial castling kingside rook Undo
		if (p instanceof King && target.getColuna() == source.getColuna() + 2) {
			Posicao sourceT = new Posicao(source.getLinha(), source.getColuna() + 3);
			Posicao targetT = new Posicao(source.getLinha(), source.getColuna() + 1);
			PecaXadrez rook = (PecaXadrez) board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}

		// Movimento especial castling queenside rook Undo
		if (p instanceof King && target.getColuna() == source.getColuna() - 2) {
			Posicao sourceT = new Posicao(source.getLinha(), source.getColuna() - 4);
			Posicao targetT = new Posicao(source.getLinha(), source.getColuna() - 1);
			PecaXadrez rook = (PecaXadrez) board.removePiece(targetT);
			board.placePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}
	}

	private void validateSourcePosition(Posicao posicao) {
		if (!board.thereIsAPiece(posicao)) {
			throw new ChessException("Não há posição no tabuleiro");
		}
		if (currentPlayer != ((PecaXadrez) board.peca(posicao)).getCor()) {
			throw new ChessException("A peça escolhida não é sua.");
		}
		if (!board.peca(posicao).isThereAnyPossibleMove()) {
			throw new ChessException("Não há movimentos possiveis para a peça escolhida.");
		}
	}

	private void validateTargetPosition(Posicao source, Posicao target) {
		if (!board.peca(source).possibleMove(target)) {
			throw new ChessException("A peça escolhida não pode se mover para a posição ed destino.");
		}
	}

	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
	}

	private Cor opponent(Cor cor) {
		return (cor == Cor.BRANCO) ? Cor.PRETO : Cor.BRANCO;
	}

	private PecaXadrez king(Cor cor) {
		List<Peca> list = piecesOnTheBoard.stream().filter(x -> ((PecaXadrez) x).getCor() == cor)
				.collect(Collectors.toList());
		for (Peca p : list) {
			if (p instanceof King) {
				return (PecaXadrez) p;
			}
		}
		throw new IllegalStateException("Não há nenhum rei " + cor + " no tabuleiro");
	}

	private boolean testCheck(Cor cor) {
		Posicao kingPosition = king(cor).getChessPosition().toPosition();
		List<Peca> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((PecaXadrez) x).getCor() == opponent(cor))
				.collect(Collectors.toList());

		for (Peca p : opponentPieces) {
			boolean[][] mat = p.possibleMoves();
			if (mat[kingPosition.getLinha()][kingPosition.getColuna()]) {
				return true;
			}
		}
		return false;
	}

	private boolean testCheckMate(Cor cor) {
		if (!testCheck(cor)) {
			return false;
		}
		List<Peca> list = piecesOnTheBoard.stream().filter(x -> ((PecaXadrez) x).getCor() == cor)
				.collect(Collectors.toList());
		for (Peca p : list) {
			boolean[][] mat = p.possibleMoves();
			for (int i = 0; i < board.getLinhas(); i++) {
				for (int j = 0; j < board.getColunas(); j++) {
					if (mat[i][j]) {
						Posicao source = ((PecaXadrez) p).getChessPosition().toPosition();
						Posicao target = new Posicao(i, j);
						Peca capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(cor);
						undoMove(source, target, capturedPiece);
						if (!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

	private void placeNewPiece(char coluna, int linha, PecaXadrez peca) {
		board.placePiece(peca, new ChessPosition(coluna, linha).toPosition());
		piecesOnTheBoard.add(peca);
	}

	private void initialSetup() {
		placeNewPiece('h', 7, new Rook(board, Cor.BRANCO));
		placeNewPiece('c', 1, new Bishop(board, Cor.BRANCO));
		placeNewPiece('d', 1, new Rook(board, Cor.BRANCO));
		placeNewPiece('e', 1, new King(board, Cor.BRANCO, this));
		placeNewPiece('e', 1, new Queen(board, Cor.BRANCO));

		placeNewPiece('b', 8, new Rook(board, Cor.PRETO));
		placeNewPiece('e', 1, new Queen(board, Cor.PRETO));
		placeNewPiece('c', 8, new Bishop(board, Cor.PRETO));
		placeNewPiece('a', 8, new King(board, Cor.PRETO, this));
	}
}
