package eli.projects.spprototype.infrastructure;

import eli.projects.spprototype.model.Piece;
import javafx.collections.ObservableList;

public interface PieceService {
	
	ObservableList<Piece> getPieces();
	
}
