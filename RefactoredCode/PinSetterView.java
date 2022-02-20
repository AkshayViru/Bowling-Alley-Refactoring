/*
 * PinSetterView/.java
 *
 * Version:
 *   $Id$
 *
 * Revision:
 *   $Log$
 */

/**
 *  constructs a prototype PinSetter GUI
 *
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Vector;


public class PinSetterView implements PinsetterObserver {

	//This Vector will keep references to the pin labels to show
	//which ones have fallen.
    private Vector pinVect = new Vector ( );

    private JPanel firstRoll;
    private JPanel secondRoll;


	//===========================================================
	/** buildPinGrid( )
	 * 	 @param - pin number
	 *   build a grid for the pin
	 *   @return pin JPanel object
	 */
	public JPanel buildPinGrid( String pinNumber ){
		JPanel pinId = new JPanel ();
		JLabel pinIdLabel = new JLabel ( pinNumber );
		pinId.add ( pinIdLabel );
		pinVect.add ( pinIdLabel );
		return pinId;
	}

	//===========================================================
	/** buildFourthRow( )
	 * 	 @param - pins in the fourth row
	 *   sets pins in fourth row of the grid 
	 *   @return pins JPanel object after updating fourth row
	 */
	public JPanel buildFourthRow ( JPanel pins, JPanel seven, JPanel eight, JPanel nine, JPanel ten ) {
		pins.add ( seven );
		pins.add ( new JPanel ( ) );
		pins.add ( eight );
		pins.add ( new JPanel ( ) );
		pins.add ( nine );
		pins.add ( new JPanel ( ) );
		pins.add ( ten );
		return pins;
	}

	//===========================================================
	/** buildThirdRow( )
	 * 	 @param - pins in the third row
	 *   sets pins in third row of the grid 
	 *   @return pins JPanel object after updating third row
	 */
	public JPanel buildThirdRow ( JPanel pins, JPanel four, JPanel five, JPanel six ) {
		pins.add ( new JPanel ( ) );
		pins.add ( four );
		pins.add ( new JPanel ( ) );
		pins.add ( five );
		pins.add ( new JPanel ( ) );
		pins.add ( six );
		return pins;
	}

	//===========================================================
	/** buildSecondRow( )
	 * 	 @param - pins in the second row
	 *   sets pins in second row of the grid 
	 *   @return pins JPanel object after updating second row
	 */
	public JPanel buildSecondRow ( JPanel pins, JPanel two, JPanel three ) {
		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );
		pins.add ( two );
		pins.add ( new JPanel ( ) );
		pins.add ( three );
		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );
		return pins;
	}

	//===========================================================
	/** buildFirstRow( )
	 * 	 @param - pins in the first row
	 *   sets pins in first row of the grid 
	 *   @return pins JPanel object after updating first row
	 */
	public JPanel buildFirstRow ( JPanel pins, JPanel one ) {
		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );
		pins.add ( one );
		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );
		pins.add ( new JPanel ( ) );
		return pins;
	}

	//===========================================================
	/** buildGrid( )
	 * 	 @param - pins JPanel object
	 *   builds a grid of 4x7 by adding pin grids in their respective rows
	 *   @return updated pins JPanel object after adding all the rows
	 */
	public JPanel buildGrid( JPanel pins ){

		JPanel one = buildPinGrid( "1" );
		JPanel two = buildPinGrid( "2" );
		JPanel three = buildPinGrid( "3" );
		JPanel four = buildPinGrid( "4" );
		JPanel five = buildPinGrid( "5" );
		JPanel six = buildPinGrid( "6" );
		JPanel seven = buildPinGrid( "7" );
		JPanel eight = buildPinGrid( "8" );
		JPanel nine = buildPinGrid( "9" );
		JPanel ten = buildPinGrid( "10" );

		//******************************Fourth Row**************
		pins = buildFourthRow ( pins, seven, eight, nine, ten );
		
		//*****************************Third Row***********	
		pins = buildThirdRow ( pins, four, five, six );
		
		//*****************************Second Row**************
		pins = buildSecondRow ( pins, two, three );
		
		//******************************First Row*****************
		pins = buildFirstRow ( pins, one );

		return pins;
	}


	//===========================================================
	/** zeroPinsDownUpdation( )
	 *   updates foreground and background if no pin is knocked down 
	 */
	public void zeroPinsDownUpdation(  ){
		for ( int i = 0; i != 10; i++){
			((JLabel)pinVect.get(i)).setForeground(Color.black);
		}
		secondRoll.setBackground( Color.black);
	}



    /**
     * Constructs a Pin Setter GUI displaying which roll it is with
     * yellow boxes along the top (1 box for first roll, 2 boxes for second)
     * and displays the pins as numbers in this format:
     *
     *                7   8   9   10
     *                  4   5   6
     *                    2   3
     *                      1
     *
     */
    

	private JFrame frame;
    
    public PinSetterView ( int laneNum ) {
	
		frame = new JFrame ( "Lane " + laneNum + ":" );
		
		Container cpanel = frame.getContentPane ( );
		
		JPanel pins = new JPanel ( );
		
		pins.setLayout ( new GridLayout ( 4, 7 ) );
		
		//********************Top of GUI indicates first or second roll
		
		JPanel top = new JPanel ( );
		
		firstRoll = new JPanel ( );
		firstRoll.setBackground( Color.yellow );
		
		secondRoll = new JPanel ( );
		secondRoll.setBackground ( Color.black );
		
		top.add ( firstRoll, BorderLayout.WEST );
		
		top.add ( secondRoll, BorderLayout.EAST );
	
		
		//**********************Grid of the pins**************************
		pins = buildGrid( pins );
		
		
		//*********************************************************
		
		top.setBackground ( Color.black );
		
		cpanel.add ( top, BorderLayout.NORTH );
		
		pins.setBackground ( Color.black );
		pins.setForeground ( Color.yellow );
		
		cpanel.add ( pins, BorderLayout.CENTER );
		
		frame.pack();
		
	
//	frame.show();
    }
    
    
    /**
     * This method receives a pinsetter event.  The event is the current
     * state of the PinSetter and the method changes how the GUI looks
     * accordingly.  When pins are "knocked down" the corresponding label
     * is grayed out.  When it is the second roll, it is indicated by the
     * appearance of a second yellow box at the top.
     *
     * @param e    The state of the pinsetter is sent in this event.
     */
    

    public void receivePinsetterEvent(PinsetterEvent pe){
		if ( !(pe.isFoulCommited()) ) {
			JLabel tempPin = new JLabel ( );
			for ( int c = 0; c < 10; c++ ) {
				boolean pin = pe.pinKnockedDown ( c );
				tempPin = (JLabel)pinVect.get ( c );
				if ( pin ) {
					tempPin.setForeground ( Color.lightGray );
				}
			}
		}

		if ( pe.getThrowNumber() == 1 ) {
			secondRoll.setBackground ( Color.yellow );
		}

		if ( pe.pinsDownOnThisThrow() == -1) {
			zeroPinsDownUpdation(  );
		}
    }
    
    public void show() {
    	frame.show();
    }

    public void hide() {
    	frame.hide();
    }
    
    public static void main ( String args [ ] ) {
		PinSetterView pg = new PinSetterView ( 1 );
    }
    
}
