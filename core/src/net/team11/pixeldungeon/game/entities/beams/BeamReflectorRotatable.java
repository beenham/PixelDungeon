package net.team11.pixeldungeon.game.entities.beams;

import com.badlogic.gdx.math.Rectangle;

import net.team11.pixeldungeon.game.entity.component.BodyComponent;
import net.team11.pixeldungeon.game.entity.component.InteractionComponent;
import net.team11.pixeldungeon.game.entity.system.BeamSystem;
import net.team11.pixeldungeon.game.uicomponents.UIManager;
import net.team11.pixeldungeon.screens.screens.PlayScreen;
import net.team11.pixeldungeon.utils.Direction;
import net.team11.pixeldungeon.utils.assets.Messages;

public class BeamReflectorRotatable extends BeamReflector {
    public BeamReflectorRotatable(Rectangle bounds, String name, Beam beam){
        super(bounds,name,beam);
        addComponent(new InteractionComponent(this));
    }

    @Override
    public void doInteraction(boolean isPlayer) {
        if (!isPlayer) {
            switch (beamOut.getBeamDirection()) {
                case UP:
                    beamOut.setBeamDirection(Direction.RIGHT);
                    break;
                case RIGHT:
                    beamOut.setBeamDirection(Direction.DOWN);
                    break;
                case DOWN:
                    beamOut.setBeamDirection(Direction.LEFT);
                    break;
                case LEFT:
                    beamOut.setBeamDirection(Direction.UP);
                    break;
            }

            BodyComponent body = getComponent(BodyComponent.class);
            super.setupBeam(body.getX(), body.getY() - BeamSystem.yOffset);
            if (hasBeamIn()) {
                beamOut.setOn(true);
            }
        } else {
            PlayScreen.uiManager.initTextBox(Messages.BEAM_ROTATE_INTERACT);
        }
    }
}
