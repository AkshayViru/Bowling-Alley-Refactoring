//This class handles score calculation
import java.util.HashMap;

public class ScoreCalculation {
    public int[][] cumulScores;
    public Party party;
    public HashMap scores;
    public boolean partyAssigned;


    public ScoreCalculation(){
        partyAssigned=false;
        scores = new HashMap();


    }
    public Party getParty() {
        return party;
    }

    public int getScore(  int[] curScore,Bowler Cur, int frame,int bowlIndex,int ball) {

        int strikeballs = 0;
        int totalScore = 0;
        for (int i = 0; i != 10; i++){
            cumulScores[bowlIndex][i] = 0;
        }
        int current = 2*(frame - 1)+ball-1;
        for (int i = 0; i != current+2; i++){
            if( i%2 == 1 && curScore[i - 1] + curScore[i] == 10 && i < current - 1 && i < 19){

                cumulScores[bowlIndex][(i/2)] += curScore[i+1] + curScore[i];
                if (i > 1) {
                }
            } else if( i < current && i%2 == 0 && curScore[i] == 10  && i < 18){
                strikeballs = 0;
                if (curScore[i+2] != -1) {
                    strikeballs = 1;
                    if(curScore[i+3] != -1) {
                        strikeballs = 2;
                    } else if(curScore[i+4] != -1) {
                        strikeballs = 2;
                    }
                }
                if (strikeballs == 2){
                    cumulScores[bowlIndex][i/2] += 10;
                    if(curScore[i+1] != -1) {
                        cumulScores[bowlIndex][i/2] += curScore[i+1] + cumulScores[bowlIndex][(i/2)-1];
                        if (curScore[i+2] != -1){
                            if( curScore[i+2] != -2){
                                cumulScores[bowlIndex][(i/2)] += curScore[i+2];
                            }
                        } else {
                            if( curScore[i+3] != -2){
                                cumulScores[bowlIndex][(i/2)] += curScore[i+3];
                            }
                        }
                    } else {
                        if ( i/2 > 0 ){
                            cumulScores[bowlIndex][i/2] += curScore[i+2] + cumulScores[bowlIndex][(i/2)-1];
                        } else {
                            cumulScores[bowlIndex][i/2] += curScore[i+2];
                        }
                        if (curScore[i+3] != -1){
                            if( curScore[i+3] != -2){
                                cumulScores[bowlIndex][(i/2)] += curScore[i+3];
                            }
                        } else {
                            cumulScores[bowlIndex][(i/2)] += curScore[i+4];
                        }
                    }
                } else {
                    break;
                }
            }else {
                if( i%2 == 0 && i < 18){
                    if ( i/2 == 0 ) {
                        if(curScore[i] != -2){
                            cumulScores[bowlIndex][i/2] += curScore[i];
                        }
                    } else if (i/2 != 9){
                        if(curScore[i] != -2){
                            cumulScores[bowlIndex][i/2] += cumulScores[bowlIndex][i/2 - 1] + curScore[i];
                        } else {
                            cumulScores[bowlIndex][i/2] += cumulScores[bowlIndex][i/2 - 1];
                        }
                    }
                } else if (i < 18){
                    if(curScore[i] != -1 && i > 2){
                        if(curScore[i] != -2){
                            cumulScores[bowlIndex][i/2] += curScore[i];
                        }
                    }
                }
                if (i/2 == 9){
                    if (i == 18){
                        cumulScores[bowlIndex][9] += cumulScores[bowlIndex][8];
                    }
                    if(curScore[i] != -2){
                        cumulScores[bowlIndex][9] += curScore[i];
                    }
                } else if (i/2 == 10) {
                    if(curScore[i] != -2){
                        cumulScores[bowlIndex][9] += curScore[i];
                    }
                }
            }
        }
        return totalScore;
    }

    public void resetScores() {

        for (Object o : party.getMembers()) {
            int[] toPut = new int[25];
            for (int i = 0; i != 25; i++) {
                toPut[i] = -1;
            }
            scores.put(o, toPut);
        }


    }
    public void markScore( Bowler Cur, Lane lane,int frame, int ball, int score ){
        int[] curScore;
        int index =  ( (frame - 1) * 2 + ball);

        curScore = (int[]) scores.get(Cur);


        curScore[ index - 1] = score;
        scores.put(Cur, curScore);
        getScore( (int[])this.scores.get(Cur),Cur, frame ,lane.bowlIndex,ball);
        SubscribeLane.publish( lane,lane.lanePublish() );
    }


}
