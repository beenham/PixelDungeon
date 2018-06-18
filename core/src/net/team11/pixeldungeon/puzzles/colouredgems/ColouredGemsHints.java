package net.team11.pixeldungeon.puzzles.colouredgems;

import net.team11.pixeldungeon.items.puzzleitems.ColouredGem;

import java.util.HashMap;
import java.util.Locale;

public class ColouredGemsHints {
    /*
        4 Gems
        blue -> green -> red -> purple
        1 -> 2 -> 3 -> 4

        1 comes before 2
        3 follows 2
        4 comes after 2, but is not next to it.

     */
    private final String FOURGEMS_HINT_ONE = "%s comes before %s.";
    private final String FOURGEMS_HINT_TWO = "%s follows %s.";
    private final String FOURGEMS_HINT_THREE = "%s comes after %s, but is not next to it.";

    /*
        5 Gems
        yellow -> red -> blue -> green -> purple
        1 -> 2 -> 3 -> 4 -> 5

        Yellow is to the left of Red then Blue.
        Purple can only lie beside Green and none else.
        Green is to the right of Yellow.

     */
    private final String FIVEGEMS_HINT_ONE = "%s is to the left of %s then %s.";
    private final String FIVEGEMS_HINT_TWO = "%s can only lie beside %s and none else.";
    private final String FIVEGEMS_HINT_THREE = "%s is to the right of %s.";
    /*
        6 Gems
        yellow -> orange -> red -> blue -> green -> purple
        0      -> 1      -> 2   -> 3    -> 4     -> 5

        Blue goes before Green but somewhere after Yellow
        Blue follows Red
        Orange can't be next to Purple or Green
        Yellow leads the way.

     */

    private final String SIXGEMS_HINT_ONE = "%s goes before %s, but somewhere after %s.";
    private final String SIXGEMS_HINT_TWO = "%s follows %s.";
    private final String SIXGEMS_HINT_THREE = "%s can't be next to %s or %s.";
    private final String SIXGEMS_HINT_FOUR = "%s leads the way.";

    private HashMap<Integer, String> hints = new HashMap<>();
    public ColouredGemsHints(HashMap<Integer, ColouredGem> gems) {
        switch (gems.size()) {
            case 4:
                setupFourGemHints(gems);
                break;
            case 5:
                setupFiveGemHints(gems);
                break;
            case 6:
                setupSixGemHints(gems);
                break;
        }
    }

    private void setupFourGemHints(HashMap<Integer, ColouredGem> gems) {
        hints.put(hints.size(), String.format(Locale.UK,FOURGEMS_HINT_ONE,gems.get(0).getColour(),gems.get(1).getColour()));
        hints.put(hints.size(), String.format(Locale.UK,FOURGEMS_HINT_TWO,gems.get(2).getColour(),gems.get(1).getColour()));
        hints.put(hints.size(), String.format(Locale.UK,FOURGEMS_HINT_THREE,gems.get(3).getColour(),gems.get(1).getColour()));
    }

    private void setupFiveGemHints(HashMap<Integer, ColouredGem> gems) {
        hints.put(hints.size(), String.format(Locale.UK,FIVEGEMS_HINT_ONE,gems.get(0).getColour(),gems.get(1).getColour(),gems.get(2).getColour()));
        hints.put(hints.size(), String.format(Locale.UK,FIVEGEMS_HINT_TWO,gems.get(4).getColour(),gems.get(3).getColour()));
        hints.put(hints.size(), String.format(Locale.UK,FIVEGEMS_HINT_THREE,gems.get(3).getColour(),gems.get(0).getColour()));
    }

    private void setupSixGemHints(HashMap<Integer, ColouredGem> gems) {
        hints.put(hints.size(), String.format(Locale.UK,SIXGEMS_HINT_ONE,gems.get(3).getColour(),gems.get(4).getColour(),gems.get(0).getColour()));
        hints.put(hints.size(), String.format(Locale.UK,SIXGEMS_HINT_TWO,gems.get(3).getColour(),gems.get(2).getColour()));
        hints.put(hints.size(), String.format(Locale.UK,SIXGEMS_HINT_THREE,gems.get(1).getColour(),gems.get(4).getColour(),gems.get(5).getColour()));
        hints.put(hints.size(), String.format(Locale.UK,SIXGEMS_HINT_FOUR,gems.get(0).getColour()));
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (int i = 0 ; i < hints.size() ; i++) {
            s.append(hints.get(i)).append("\n");
        }
        return s.toString();
    }
}
