package game.my.bulletin.mygame;

/**
 * Created by RF048808 on 10/24/2016.
 */
public class LevelEnums {
    public enum LevelInfo {
        level1(2,2,4), level2(2,3,5), level3(3,3,8),level4(3,3,6),level5(3,4,10),level6(3,4,8),level7(4,4,15),level8(4,4,12)
        ,level9(4,5,18),level10(4,5,12),level11(5,5,28),level12(5,5,25),level13(5,5,20),level14(5,6,35),
        level15(5,6,30),level16(5,6,27),level17(5,6,25),level18(6,6,45),level19(6,6,40),level20(6,6,35);
        int row;
       int col;
        int seconds;
        private LevelInfo(int row,int col,int seconds) {
            this.row = row;
            this.col = col;
            this.seconds=seconds;
        }
    }
    public enum PositiveBoosterInfo{
        pbooster1(" TIME BOOSTED X 2 ","t2"),pbooster2(" TIME BOOSTED X 4 ","t4"),pbooster3(" Time Reload ","t"),pbooster4(" 2 Steps Up ","s2"),pbooster5(" 4 Steps Up ","s4");
         String task;
        String dialg;
        PositiveBoosterInfo(String dialg,String task) {
            this.dialg=dialg;
            this.task=task;
        }
    }
    public enum NegativeBoosterInfo{

        nbooster1(" TIME LOSS -2S ","t2"),nbooster2(" TIME LOSS -4S ","t4"),nbooster3(" 2 Steps Down ","t"),nbooster4(" 4 Steps Down ","s2"),nbooster(" Half time loss ","s4");
        String dialg;
        String task;
        NegativeBoosterInfo(String dialg,String task) {
            this.dialg=dialg;
            this.task=task;
        }
    }
}
