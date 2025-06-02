package sk.tuke.bricksbreaking.game.core;

public class MagicWand {
    private int remainingUses;

    public MagicWand(int attempts){
        this.remainingUses = attempts;

    }

    public int getRemainingUses() {
        return this.remainingUses;

    }

    public void useMagicWand() {
        this.remainingUses--;

    }


}
