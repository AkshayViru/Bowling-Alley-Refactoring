import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.invoke.VolatileCallSite;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.Vector;

public class ScoreSearch {
    private static String SCOREHISTORY_FILE = "SCOREHISTORY.DAT";

    public static int getOverallMin() throws IOException {
        Vector<Integer> scoreList=new Vector<Integer>();
        BufferedReader scoreFileReader=new BufferedReader(new FileReader(SCOREHISTORY_FILE));
        String raw_data;
        while((raw_data=scoreFileReader.readLine())!=null){
            String[] scoreData=raw_data.split("\t");
            scoreList.add(Integer.parseInt(scoreData[2]));
        }
        scoreFileReader.close();
        return Collections.min(scoreList);
    }

    public static int getOverallMax() throws IOException {
        Vector<Integer> scoreList=new Vector<Integer>();
        BufferedReader scoreFileReader=new BufferedReader(new FileReader(SCOREHISTORY_FILE));
        String raw_data;
        while((raw_data=scoreFileReader.readLine())!=null){
            String[] scoreData=raw_data.split("\t");
            scoreList.add(Integer.parseInt(scoreData[2]));
        }
        scoreFileReader.close();
        return Collections.max(scoreList);
    }

    public static int getPlayerLowest(String fetchNick) throws IOException {
        Vector<Integer> scoreList=new Vector<Integer>();
        BufferedReader scoreFileReader=new BufferedReader(new FileReader(SCOREHISTORY_FILE));
        String raw_data;
        while((raw_data=scoreFileReader.readLine())!=null){
            String[] scoreData=raw_data.split("\t");
            if(Objects.equals(scoreData[0], fetchNick)){
                scoreList.add(Integer.parseInt(scoreData[2]));
            }
        }
        scoreFileReader.close();
        return Collections.min(scoreList);
    }
    public static int getPlayerHighest(String fetchNick) throws IOException {
        Vector<Integer> scoreList=new Vector<Integer>();
        BufferedReader scoreFileReader=new BufferedReader(new FileReader(SCOREHISTORY_FILE));
        String raw_data;
        while((raw_data=scoreFileReader.readLine())!=null){
            String[] scoreData=raw_data.split("\t");
            if(Objects.equals(scoreData[0], fetchNick)){
                scoreList.add(Integer.parseInt(scoreData[2]));
            }
        }
        scoreFileReader.close();
        return Collections.max(scoreList);
    }
    public static Vector<Integer> getPlayerMin(String fetchNick) throws IOException {
        Vector<Integer> scoreList=new Vector<Integer>();
        BufferedReader scoreFileReader=new BufferedReader(new FileReader(SCOREHISTORY_FILE));
        String raw_data;
        while((raw_data=scoreFileReader.readLine())!=null){
            String[] scoreData=raw_data.split("\t");
            if(Objects.equals(scoreData[0], fetchNick)){
            scoreList.add(Integer.parseInt(scoreData[2]));
        }
        }
        scoreFileReader.close();
        Collections.sort(scoreList);
        return scoreList;
    }

    public static Vector<Integer> getPlayerMax(String fetchNick) throws IOException{
        Vector<Integer> scoreList=new Vector<Integer>();
        BufferedReader scoreFileReader=new BufferedReader(new FileReader(SCOREHISTORY_FILE));
        String raw_data;
        while((raw_data=scoreFileReader.readLine())!=null){
            String[] scoreData=raw_data.split("\t");
            if(Objects.equals(scoreData[0], fetchNick)){
                scoreList.add(Integer.parseInt(scoreData[2]));
            }

        }
        Collections.sort(scoreList,Collections.reverseOrder());
        scoreFileReader.close();
        return scoreList;
    }

	public static Vector getPlayerLastFive(String fetchNick) throws IOException{
        Vector scoreList=new Vector();
        BufferedReader scoreFileReader=new BufferedReader(new FileReader(SCOREHISTORY_FILE));
        String raw_data;
        while((raw_data=scoreFileReader.readLine())!=null){
            String[] scoreData=raw_data.split("\t");
            if(Objects.equals(scoreData[0], fetchNick)){
                scoreList.add(new Score(scoreData[0],scoreData[1],scoreData[2]));
            }

        }
        Collections.reverse(scoreList);
        scoreFileReader.close();
        return scoreList;
	}

    public static Vector getLastTen() throws IOException {
        Vector scoreList=new Vector();
        BufferedReader scoreFileReader=new BufferedReader(new FileReader(SCOREHISTORY_FILE));
        String raw_data;
        while((raw_data=scoreFileReader.readLine())!=null){
            String[] scoreData=raw_data.split("\t");
            scoreList.add(new Score(scoreData[0],scoreData[1],scoreData[2]));
           
        }
        Collections.reverse(scoreList);
        scoreFileReader.close();
        return scoreList;
    }

	public static Vector getTenHighest() throws IOException {
        Vector scoreList=new Vector();
        BufferedReader scoreFileReader=new BufferedReader(new FileReader(SCOREHISTORY_FILE));
        String raw_data;
        while((raw_data=scoreFileReader.readLine())!=null){
            String[] scoreData=raw_data.split("\t");
            scoreList.add(Integer.parseInt(scoreData[2]) );
           
        }
        Collections.sort(scoreList,Collections.reverseOrder());
        scoreFileReader.close();
        return scoreList;	
    
    }


    public static String getTopPlayer() throws IOException {
        Vector<Score> scoreList=new Vector<Score>();
        BufferedReader scoreFileReader=new BufferedReader(new FileReader(SCOREHISTORY_FILE));
        String raw_data;
        while((raw_data=scoreFileReader.readLine())!=null){
            String[] scoreData=raw_data.split("\t");
            scoreList.add(new Score(scoreData[0],scoreData[1],scoreData[2]));
           
        }
        Collections.sort(scoreList,new Comparator<Score>(){
            @Override
            public int compare(Score p1, Score p2) {
                if(Integer.parseInt(p1.getScore())>Integer.parseInt(p2.getScore()))
                {return -1;}
            else if(Integer.parseInt(p1.getScore())<Integer.parseInt(p2.getScore())){return 1;}
            else{return 0;}
            }
        });
        scoreFileReader.close();
        return scoreList.get(0).getNickName();
    
    }
}
