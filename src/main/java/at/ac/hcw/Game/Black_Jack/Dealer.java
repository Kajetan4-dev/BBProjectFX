package at.ac.hcw.Game.Black_Jack;

public class Dealer extends Player {

    private boolean hideCard;

    public Dealer() {
        super();
        hideCard = true;
    }

    public boolean isCardHidden() {
        return hideCard;
    }

    public void revealCard() {
        hideCard = false;
    }

    public void resetHideCard() {
        this.hideCard = true;
    }
}
