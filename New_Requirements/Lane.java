import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;
import java.util.Iterator;
import java.util.Date;
import java.util.*;
public class Lane extends Thread implements PinsetterObserver {

	private static final String CURRENTHISTORY_DAT = "CURRENTHISTORY.DAT";
	private Pinsetter setter;
	public Vector subscribers;
	public Vector<Integer> highestScoresOfEach;
	private boolean gameIsHalted;

	private Iterator bowlerIterator;
	private int ball;
	public int bowlIndex;
	private boolean tenthFrameStrike;

	private int[] curScores;
	private boolean canThrowAgain;
	public boolean gameFinished;
	public int frameNumber;

	private int[][] finalScores;
	private int gameNumber;
//	public static final int sleepTime = 10;
	private Bowler currentThrower;			// = the thrower who just took a throw
	private ScoreCalculation scoreCalculator;
	private	 boolean pauseAfterBowl;


	Vector<String> bowlersNickNames;
	HashMap<String, Integer> bowlersIndex;
	int sizeOfHighestScoresOfEach;
	int[] highestScoresArray;
	boolean evenTurn = false, PenaltyInNextFrame = false;

//	String data="";
//
	/** Lane()
	 * 
	 * Constructs a new lane and starts its thread
	 * 
	 * @pre none
	 * @post a new lane has been created and its thered is executing
	 */

	public static int getSecondLargest( int size, int[] arr1){
		Arrays.sort(arr1);
		return arr1[size-2];
	}

	public void getSecondHighestPlayer( ){
		int idx = 0, arrSize = finalScores.length;
		int arr[] = new int[arrSize];
		System.out.println("---------------------------------------------------------------");
		System.out.println( "Final Scores for this Game : " );
		while( idx < arrSize ) {
			arr[idx] = finalScores[idx][0];
			System.out.println( finalScores[idx][0] );
			//System.out.println( finalScores[idx][1] );
			//System.out.println( finalScores[idx][2] );
			idx++;
		}

		int secondHighest = getSecondLargest( arrSize, arr );
		System.out.println("---------------------------------------------------------------");
		System.out.println("Second Highest Score : ");
		System.out.println("---------------------------------------------------------------");
		System.out.println(secondHighest);
		System.out.println("End bowling");
	}



	public Lane() throws IOException {


		BowlerFile bowlerFile = new BowlerFile();
		sizeOfHighestScoresOfEach = bowlerFile.getBowlers().size();
		highestScoresArray = new int[sizeOfHighestScoresOfEach];
		for(int i=0; i<sizeOfHighestScoresOfEach; i++)
			highestScoresArray[i] = -1;
//
//		bowlersNickNames = bowlerFile.getBowlers()
//		bowlersIndex=new HashMap<String, Integer>();
//		for(int i=0; i<bowlersNickNames.size(); i++)
//			bowlersIndex.put(bowlersNickNames.get(i),i);



		setter = new Pinsetter();
		subscribers = new Vector();
		gameIsHalted = false;
		scoreCalculator=new ScoreCalculation();
//		scoreCalculator.partyAssigned = false;
		gameNumber = 0;
		setter.subscribe( this );
		this.start();
	}

	/** run()
	 * 
	 * entry point for execution of this lane 
	 */
	public void run() {
		
		while (true) {
			playMode();
			sleepT(10);
		}
	}

	private void playMode() {
		if (scoreCalculator.partyAssigned && !gameFinished) {	// we have a party on this lane,
							// so next bower can take a throw

			while (gameIsHalted ) {
				sleepT(10);
			}

			startBowlingGame();
//			System.out.println("Playmode"+data);

		} else if (scoreCalculator.partyAssigned && gameFinished) {
			getSecondHighestPlayer( );
			endBowlingGame();
		}
	}

	private void endBowlingGame() {
		EndGamePrompt egp = new EndGamePrompt( ((Bowler) scoreCalculator.party.getMembers().get(0)).getNickName() + "'s Party" );
		int result = egp.getResult();
		egp.distroy();
		egp = null;


		System.out.println("result was: " + result);

		// TODO: send record of scores to control desk
		if (result == 1) {					// yes, want to play again
			scoreCalculator.resetScores();
			resetBowlerIterator();
			gameFinished = false;
			frameNumber = 0;

		} else if (result == 2) {// no, dont want to play another game
			Vector printVector;
			EndGameReport egr = new EndGameReport( ((Bowler)scoreCalculator.party.getMembers().get(0)).getNickName() + "'s Party", scoreCalculator.party);
			printVector = egr.getResult();
			scoreCalculator.partyAssigned = false;
			Iterator scoreIt = scoreCalculator.party.getMembers().iterator();
			scoreCalculator.party = null;
			scoreCalculator.partyAssigned = false;

			SubscribeLane.publish(this,lanePublish());

			int myIndex = 0;
			while (scoreIt.hasNext()){
				Bowler thisBowler = (Bowler)scoreIt.next();
				ScoreReport sr = new ScoreReport( thisBowler, finalScores[myIndex++], gameNumber );
				sr.sendEmail(thisBowler.getEmail());
				Iterator printIt = printVector.iterator();
				while (printIt.hasNext()){
					if (thisBowler.getNickName() == (String)printIt.next()){
						System.out.println("Printing " + thisBowler.getNickName());
						sr.sendPrintout();
					}
				}

			}
		}

//		out.skipBytes((int) out.length());

	}

	private void startBowlingGame() {

		while(pauseAfterBowl){
			sleepT(100);
		}

		if (bowlerIterator.hasNext()) {
			currentThrower = (Bowler)bowlerIterator.next();

			canThrowAgain = true;
			tenthFrameStrike = false;
			ball = 0;

			while (canThrowAgain) {
				setter.ballThrown();		// simulate the thrower's ball hitting
				ball++;
			}

			if (frameNumber == 9){
				finalScores[bowlIndex][gameNumber] = scoreCalculator.cumulScores[bowlIndex][9];
				try{
				Date date = new Date();
				String dateString = "" + date.getHours() + ":" + date.getMinutes() + " " + date.getMonth() + "/" + date.getDay() + "/" + (date.getYear() + 1900);
//				data = data+currentThrower.getNickName() + "\t" + dateString + "\t" + new Integer(scoreCalculator.cumulScores[bowlIndex][9]).toString() + "\n";
//				System.out.println(data);
				ScoreHistoryFile.addScore(currentThrower.getNickName(), dateString, new Integer(scoreCalculator.cumulScores[bowlIndex][9]).toString());
				ScoreHistoryFile.currentScore(currentThrower.getNickName(), dateString, new Integer(scoreCalculator.cumulScores[bowlIndex][9]).toString());

				} catch (Exception e) {System.err.println("Exception in addScore. "+ e );}
			}

			pauseAfterBowl=true;
			setter.reset();
			bowlIndex++;

		} else {
			frameNumber++;
			resetBowlerIterator();
			bowlIndex = 0;
			if (frameNumber > 9) {
				gameFinished = true;
				gameNumber++;
			}
		}
	}
//	public void getReport(){
//		System.out.println("Get Report Clicked");
//		System.out.println(this.data);
//	}
	private void sleepT(int sleepTime){
		try {
			sleep(sleepTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/** recievePinsetterEvent()
	 * 
	 * recieves the thrown event from the pinsetter
	 *
	 * @pre none
	 * @post the event has been acted upon if desiered
	 * 
	 * @param pe 		The pinsetter event that has been received.
	 */
	public void receivePinsetterEvent(PinsetterEvent pe) {
		
			if (pe.pinsDownOnThisThrow() >=  0) {			// this is a real throw
				highestScoresArray[bowlIndex] = highestScoresArray[bowlIndex]> pe.pinsDownOnThisThrow() ?
						highestScoresArray[bowlIndex] : pe.pinsDownOnThisThrow();
				int pinsDownOnThisThrow = pe.pinsDownOnThisThrow(), temp=0;
				if(PenaltyInNextFrame == true){
					if(evenTurn == false) {
						temp = pinsDownOnThisThrow;
						evenTurn = true;
					}
					else if(evenTurn == true){
						temp = temp + pinsDownOnThisThrow;
						pinsDownOnThisThrow -= temp/2;
						evenTurn = false;
						PenaltyInNextFrame = false;
					}
				}
				else{
				if(pinsDownOnThisThrow == 0){
					if(evenTurn == true){
						if(highestScoresArray[bowlIndex] != -1){
							pinsDownOnThisThrow -= highestScoresArray[bowlIndex]/2;
						}
						else{
							PenaltyInNextFrame = true;
						}
						evenTurn = false;
					}
					else
						evenTurn = true;
				}
				else{
					evenTurn = false;
				}
				}

				checkRealThrow(pe, pinsDownOnThisThrow);
			}

	}

	private void checkRealThrow(PinsetterEvent pe, int pinsDownOnThisThrow) {
//		scoreCalculator.markScore(currentThrower, this,frameNumber + 1, pe.getThrowNumber(), pe.pinsDownOnThisThrow());

		scoreCalculator.markScore(currentThrower, this,frameNumber + 1, pe.getThrowNumber(), pinsDownOnThisThrow);



		// next logic handles the ?: what conditions dont allow them another throw?
		// handle the case of 10th frame first
		if (frameNumber == 9) {
			tenthFrameScoring(pe);
		} else { // scoring logic for all the frames except the 10th frame
			normalFrameScoring(pe);
		}
	}

	private void normalFrameScoring(PinsetterEvent pe) {
		if (pe.pinsDownOnThisThrow() == 10) {		// threw a strike
			canThrowAgain = false;
			//publish( lanePublish() );
		} else if (pe.getThrowNumber() == 2) {
			canThrowAgain = false;
			//publish( lanePublish() );
		} else if (pe.getThrowNumber() == 3)
			System.out.println("I'm here...");
	}

	private void tenthFrameScoring(PinsetterEvent pe) {
		if (pe.totalPinsDown() == 10) {
			setter.resetPins();
			if(pe.getThrowNumber() == 1) {
				tenthFrameStrike = true;
			}
		}

		if ((pe.totalPinsDown() != 10) && (pe.getThrowNumber() == 2 && tenthFrameStrike == false)) {
			canThrowAgain = false;
			//publish( lanePublish() );
		}

		if (pe.getThrowNumber() == 3) {
			canThrowAgain = false;
			//publish( lanePublish() );
		}
	}

	/** resetBowlerIterator()
	 * 
	 * sets the current bower iterator back to the first bowler
	 * 
	 * @pre the party as been assigned
	 * @post the iterator points to the first bowler in the party
	 */
	private void resetBowlerIterator() {
		bowlerIterator = (scoreCalculator.party.getMembers()).iterator();
	}
	/** assignParty()
	 * 
	 * assigns a party to this lane
	 * 
	 * @pre none
	 * @post the party has been assigned to the lane
	 * 
	 * @param theParty		Party to be assigned
	 */
	public void assignParty( Party theParty ) {
		scoreCalculator.party = theParty;
		resetBowlerIterator();
		scoreCalculator.partyAssigned = true;
		
		curScores = new int[scoreCalculator.party.getMembers().size()];
		scoreCalculator.cumulScores = new int[scoreCalculator.party.getMembers().size()][10];
		finalScores = new int[scoreCalculator.party.getMembers().size()][128]; //Hardcoding a max of 128 games, bite me.
		gameNumber = 0;
		
		scoreCalculator.resetScores();
		gameFinished = false;
		frameNumber = 0;
	}

	/** lanePublish()
	 *
	 * Method that creates and returns a newly created laneEvent
	 * 
	 * @return		The new lane event
	 */
	public LaneEvent lanePublish(  ) {
		LaneEvent laneEvent = new LaneEvent(scoreCalculator.party, bowlIndex, currentThrower, scoreCalculator.cumulScores, scoreCalculator.scores, frameNumber+1, curScores, ball, gameIsHalted);
		return laneEvent;
	}



	/** isPartyAssigned()
	 * 
	 * checks if a party is assigned to this lane
	 * 
	 * @return true if party assigned, false otherwise
	 */
	public boolean isPartyAssigned() {
		return scoreCalculator.partyAssigned;
	}




	/**
	 * Accessor to get this Lane's pinsetter
	 * 
	 * @return		A reference to this lane's pinsetter
	 */

	public Pinsetter getPinsetter() {
		return setter;	
	}

	/**
	 * Pause the execution of this game
	 */
	public void pauseGame() {
		gameIsHalted = true;
		System.out.println("maintain button clicked");
		SubscribeLane.publish(this,lanePublish());
	}
	
	/**
	 * Resume the execution of this game
	 */
	public void unPauseGame() {
		gameIsHalted = false;
		SubscribeLane.publish(this,lanePublish());
	}

	public void bowlButtonClick() {
//		System.out.println("bowl button clicked");
		pauseAfterBowl=false;
	}
}
