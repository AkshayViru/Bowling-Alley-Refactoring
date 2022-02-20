import java.util.Vector;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Date;

public class Lane extends Thread implements PinsetterObserver {

	private Pinsetter setter;
	public Vector subscribers;

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
	public static final int sleepTime = 10;
	private Bowler currentThrower;			// = the thrower who just took a throw
	private ScoreCalculation scoreCalculator;
	/** Lane()
	 * 
	 * Constructs a new lane and starts its thread
	 * 
	 * @pre none
	 * @post a new lane has been created and its thered is executing
	 */
	public Lane() { 
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
			sleep();
		}
	}

	private void playMode() {
		if (scoreCalculator.partyAssigned && !gameFinished) {	// we have a party on this lane,
							// so next bower can take a throw

			while (gameIsHalted) {
				sleep();
			}


			startBowlingGame();
		} else if (scoreCalculator.partyAssigned && gameFinished) {
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
	}

	private void startBowlingGame() {
		if (bowlerIterator.hasNext()) {
			currentThrower = (Bowler)bowlerIterator.next();

			canThrowAgain = true;
			tenthFrameStrike = false;
			ball = 0;
			while (canThrowAgain) {
				setter.ballThrown();		// simulate the thrower's ball hiting
				ball++;
			}

			if (frameNumber == 9){
				finalScores[bowlIndex][gameNumber] = scoreCalculator.cumulScores[bowlIndex][9];
				try{
				Date date = new Date();
				String dateString = "" + date.getHours() + ":" + date.getMinutes() + " " + date.getMonth() + "/" + date.getDay() + "/" + (date.getYear() + 1900);
				ScoreHistoryFile.addScore(currentThrower.getNickName(), dateString, new Integer(scoreCalculator.cumulScores[bowlIndex][9]).toString());
				} catch (Exception e) {System.err.println("Exception in addScore. "+ e );}
			}


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

	public void sleep(){
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
				checkRealThrow(pe);
			} else {								//  this is not a real throw, probably a reset
			}
	}

	private void checkRealThrow(PinsetterEvent pe) {
		scoreCalculator.markScore(currentThrower, this,frameNumber + 1, pe.getThrowNumber(), pe.pinsDownOnThisThrow());

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

	/** resetScores()
	 * 
	 * resets the scoring mechanism, must be called before scoring starts
	 * 
	 * @pre the party has been assigned
	 * @post scoring system is initialized
	 */
//	private void resetScores() {
//
//		for (Object o : scoreCalculator.party.getMembers()) {
//			int[] toPut = new int[25];
//			for (int i = 0; i != 25; i++) {
//				toPut[i] = -1;
//			}
//			scoreCalculator.scores.put(o, toPut);
//		}
//
//		gameFinished = false;
//		frameNumber = 0;
//	}

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

	/** markScore()
	 *
	 * Method that marks a bowlers score on the board.
	 * 
	 * @param Cur		The current bowler
	 * @param frame	The frame that bowler is on
	 * @param ball		The ball the bowler is on
	 * @param score	The bowler's score 
	 */
//	private void markScore( Bowler Cur, int frame, int ball, int score ){
//		int[] curScore;
//		int index =  ( (frame - 1) * 2 + ball);
//
//		curScore = (int[]) scoreCalculator.scores.get(Cur);
//
//
//		curScore[ index - 1] = score;
//		scoreCalculator.scores.put(Cur, curScore);
//		scoreCalculator.getScore( (int[])this.scoreCalculator.scores.get(Cur),Cur, frame ,bowlIndex,ball);
//		publish( lanePublish() );
//	}

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

	/** getScore()
	 *
	 * Method that calculates a bowlers score
	 * 
	 * @param Cur		The bowler that is currently up
	 * @param frame	The frame the current bowler is on
	 * 
	 * @return			The bowlers total score
	 */


	/** isPartyAssigned()
	 * 
	 * checks if a party is assigned to this lane
	 * 
	 * @return true if party assigned, false otherwise
	 */
	public boolean isPartyAssigned() {
		return scoreCalculator.partyAssigned;
	}

	/** isGameFinished
	 *
	 * @return true if the game is done, false otherwise
	 */
	public boolean isGameFinished() {
		return gameFinished;
	}

	/** subscribe
	 * 
	 * Method that will add a subscriber
	 * 

	 */

//	public void subscribe( LaneObserver adding ) {
//		subscribers.add( adding );
//	}

	/** publish
	 *
	 * Method that publishes an event to subscribers
	 * 
	 * @param event	Event that is to be published
	 */

//	public void publish( LaneEvent event ) {
//		if( subscribers.size() > 0 ) {
//			Iterator eventIterator = subscribers.iterator();
//
//			while ( eventIterator.hasNext() ) {
//				( (LaneObserver) eventIterator.next()).receiveLaneEvent( event );
//			}
//		}
//	}

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
		SubscribeLane.publish(this,lanePublish());
	}
	
	/**
	 * Resume the execution of this game
	 */
	public void unPauseGame() {
		gameIsHalted = false;
		SubscribeLane.publish(this,lanePublish());
	}

}
