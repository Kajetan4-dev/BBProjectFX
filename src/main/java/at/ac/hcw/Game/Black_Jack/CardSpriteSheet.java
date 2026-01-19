package at.ac.hcw.Game.Black_Jack;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class CardSpriteSheet {
    private static final String sheetPath = "/at/ac/hcw/Game/Media/images/cards/cardsheet.png";
    private static final int COLS = 13;
    private static final int ROWS = 4;
    private static Image sheet;


    //prevents instantiation of class
    private CardSpriteSheet(){

    }

    // loads the sprite sheet ONLY ONCE
    private static Image getSheet(){
        if (sheet == null){
            var url = CardSpriteSheet.class.getResource(sheetPath);
            if (url == null){
                throw new IllegalStateException("Sprite-Sheet nix gut " + sheetPath);
            }
            sheet = new Image(url.toExternalForm());// Loads the image as JavaFx image
        }
        return sheet;

    }

    //Creates ImageView showing a single card extracted from sprite sheet
    public static ImageView createCardView(int col, int row, double fitW,double fitH){ // builds card with rows collums width and height
        Image s = getSheet();

        double cardW = s.getWidth() /COLS;// width of one card
        double cardH = s.getHeight() / ROWS;// height of one card

        double positionx = col * cardW; // starting point on collum or x
        double positiony = row * cardH; // starting point on rows or y

        ImageView view = new ImageView(s);

        view.setViewport(new Rectangle2D(positionx,positiony,cardW,cardH)); // shows only the card staring at x and y with given width and height
        view.setFitWidth(fitW); // scales ImageView to specific width px on screen
        view.setFitHeight(fitH); // same with height
        view.setPreserveRatio(false);// force exact width and height (streches image) not sure if true or false would be better

        return view;
    }
}
