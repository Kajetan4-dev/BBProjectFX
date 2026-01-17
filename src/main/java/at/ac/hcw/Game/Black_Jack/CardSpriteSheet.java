package at.ac.hcw.Game.Black_Jack;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CardSpriteSheet {
    private static final String sheetPath = "/at/ac/hcw/Game/Media/images/cards/cardsheet.png";

    private static final int COLS = 13;
    private static final int ROWS = 4;

    private static Image sheet;

    private CardSpriteSheet(){

    }

    private static Image getSheet(){
        if (sheet == null){
            var url = CardSpriteSheet.class.getResource(sheetPath);
            if (url == null){
                throw new IllegalStateException("Sprite-Sheet nix gut " + sheetPath);
            }

            sheet = new Image(url.toExternalForm());
        }
        return sheet;

    }

    public static ImageView createCardView(int col, int row, double fitW,double fitH){
        Image s = getSheet();

        double cardW = s.getWidth() /COLS;
        double cardH = s.getHeight() / ROWS;

        double positionx = col * cardW;
        double positiony = row * cardH;

        ImageView view = new ImageView(s);

        view.setViewport(new Rectangle2D(positionx,positiony,cardW,cardH));

        view.setFitWidth(fitW);
        view.setFitHeight(fitH);
        view.setPreserveRatio(false);

        return view;
    }


}
