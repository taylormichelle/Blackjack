import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.util.*;

public class GUI extends JFrame {
	
	//randomizer for cards
	Random rand = new Random();
	
	//temporary integer used for used status
	int tempC;
	
	//boolean that indicates whether the dealer is thinking or not
	boolean dealerHit = false;
	
	//list of messages
	ArrayList<Message> Log = new ArrayList<Message>();
	
	//fonts used
	Font cardFont = new Font("Impact", Font.PLAIN, 40);
	Font questionFont = new Font("Arial", Font.BOLD, 40);
	Font buttonFont = new Font("Calibri", Font.PLAIN, 25);
	Font logFont = new Font("Calibri", Font.PLAIN, 30);
	
	//Log message colors
	Color cDealer = Color.red;
	Color cPlayer = Color.blue;
	
	//strings used
	String questHitStay = new String("Hit or Stay?");
	String questPlayMore = new String("Play more?");
	
	//colors used
	Color colorBackground = new Color(39,119,50);
	Color colorButton = new Color(204,204,0);
	
	//buttons used
	JButton hit = new JButton();
	JButton stay = new JButton();
	JButton yes = new JButton();
	JButton no = new JButton();
	
	//screen resolution
	int sW = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	int sH = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	
	//window resolution
	int aW = 1300;
	int aH = 800;
	
	//card grid position and dimensions
	int gridX = 50;
	int gridY = 50;
	int gridW = 900;
	int gridH = 400;
	
	//card spacing and dimensions
	int spacing = 10;
	int rounding = 10;
	int cardTW = (int) gridW/6;
	int cardTH = (int) gridH/2;
	int cardW = cardTW - spacing*2;
	int cardH = cardTH - spacing*2;
	
	// Boolean conditions
	boolean hit_stay_q = true;
	boolean dealer_turn = false;
	boolean play_more_q = false;
	
	// Stack for deck of cards and linked lists for player hand and dealer hand
	Stack<Card> Cards = new Stack<Card>();
	LinkedList<Card> playerCards = new LinkedList<Card>();
	LinkedList<Card> dealerCards = new LinkedList<Card>();
	
	// Totals
	int playerMinTotal = 0;
	int playerMaxTotal = 0;
	int dealerMinTotal = 0;
	int dealerMaxTotal = 0;
	
	//polygons for diamond shapes
	int[] polyX = new int[4];
	int[] polyY = new int[4];
	
	public GUI() {
		this.setTitle("Michelle's Blackjack Table");
		this.setBounds((sW-aW-6)/2, (sH-aH-29)/2, aW+6, aH+29);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
		Board board = new Board();
		this.setContentPane(board);
		board.setLayout(null);

		//button stuff
		
		ActHit actHit = new ActHit();
		hit.addActionListener(actHit);
		hit.setBounds(1000, 200, 100, 50);
		hit.setBackground(colorButton);
		hit.setFont(buttonFont);
		hit.setText("HIT");
		board.add(hit);
		
		ActStay actStay = new ActStay();
		stay.addActionListener(actStay);
		stay.setBounds(1150, 200, 100, 50);
		stay.setBackground(colorButton);
		stay.setFont(buttonFont);
		stay.setText("STAY");
		board.add(stay);
		
		ActYes actYes = new ActYes();
		yes.addActionListener(actYes);
		yes.setBounds(1000, 600, 100, 50);
		yes.setBackground(colorButton);
		yes.setFont(buttonFont);
		yes.setText("YES");
		board.add(yes);
		
		ActNo actNo = new ActNo();
		no.addActionListener(actNo);
		no.setBounds(1150, 600, 100, 50);
		no.setBackground(colorButton);
		no.setFont(buttonFont);
		no.setText("NO");
		board.add(no);
		
		// Create deck of cards and fill stack
		String temp_str = "starting_temp_str_name";
		for (int i = 0; i < 52; i++) {
			if (i % 4 == 0) {
				temp_str = "Spades";
			} else if (i % 4 == 1) {
				temp_str = "Hearts";
			} else if (i % 4 == 2) {
				temp_str = "Diamonds";
			} else if (i % 4 == 3) {
				temp_str = "Clubs";
			}
			Cards.push(new Card((i/4) + 1, temp_str, i));
		}
		
		Collections.shuffle(Cards);
		//Add two cards to the player hand and two to the dealer
		playerCards.add(Cards.pop());
		dealerCards.add(Cards.pop());
		playerCards.add(Cards.pop());
		dealerCards.add(Cards.pop());

	}
	
	public void totalsChecker() {
		
		int acesCount;
		
		// Totals
		playerMinTotal = 0;
		playerMaxTotal = 0;
		acesCount = 0;
		
		for (Card c : playerCards) {
			playerMinTotal += c.value;
			playerMaxTotal += c.value;
			if (c.name == "Ace")
				acesCount++;
			
		}
		
		if (acesCount > 0)
			playerMaxTotal += 10;
		
		dealerMinTotal = 0;
		dealerMaxTotal = 0;
		acesCount = 0;
		
		for (Card c : dealerCards) {
			dealerMinTotal += c.value;
			dealerMaxTotal += c.value;
			if (c.name == "Ace")
				acesCount++;
			
		}
		
		if (acesCount > 0)
			dealerMaxTotal += 10;
	}
	
	public void setWinner() {
		int pPoints = 0;
		int dPoints = 0;
		
		if (playerMaxTotal > 21) {
			pPoints = playerMinTotal;
		} else {
			pPoints = playerMaxTotal;
		}
		
		if (dealerMaxTotal > 21) {
			dPoints = dealerMinTotal;
		} else {
			dPoints = dealerMaxTotal;
		}
		
		if (pPoints > 21 && dPoints > 21) {
			Log.add(new Message("Nobody wins!", "Dealer"));
		} else if (dPoints > 21) {
			Log.add(new Message("You win!", "Player"));
			Main.pWins++;
		} else if (pPoints > 21) {
			Log.add(new Message("Dealer wins!", "Dealer"));
			Main.dWins++;
		} else if (pPoints > dPoints) {
			Log.add(new Message("You win!", "Player"));
			Main.pWins++;
		} else {
			Log.add(new Message("Dealer wins!", "Dealer"));
			Main.dWins++;
		}
		
	}
	
	public void dealerHitStay() {
		dealerHit = true;
		
		int dAvailable = 0;
		if (dealerMaxTotal > 21) {
			dAvailable = dealerMinTotal;
		} else {
			dAvailable = dealerMaxTotal;
		}
		
		int pAvailable = 0;
		if (playerMaxTotal > 21) {
			pAvailable = playerMinTotal;
		} else {
			pAvailable = playerMaxTotal;
		}
		
		repaint();
		
		if ((dAvailable < pAvailable && pAvailable <= 21) || dAvailable < 17) {
			int tempMax = 0;
			if (dealerMaxTotal <= 21) {
				tempMax = dealerMaxTotal;
			} else {
				tempMax = dealerMinTotal;
			}
			String mess = ("Dealer: Hit (total: " + Integer.toString(tempMax) + ")");
			Log.add(new Message(mess, "Dealer"));

			dealerCards.add(Cards.pop());
			
		} else {
			int tempMax = 0;
			if (dealerMaxTotal <= 21) {
				tempMax = dealerMaxTotal;
			} else {
				tempMax = dealerMinTotal;
			}
			String mess = ("Dealer: Stay (total: " + Integer.toString(tempMax) + ")");
			Log.add(new Message(mess, "Dealer"));
			setWinner();
			dealer_turn = false;
			play_more_q = true;
		}
		dealerHit = false;
	}
	
	public void refresher() {
		
		if (hit_stay_q == true) {
			hit.setVisible(true);
			stay.setVisible(true);
		} else {
			hit.setVisible(false);
			stay.setVisible(false);
		}
		
		if (dealer_turn == true) {
			if (dealerHit == false)
				dealerHitStay();
		}
		
		if (play_more_q == true || dealer_turn == true) {
			yes.setVisible(true);
			no.setVisible(true);
		} else {
			yes.setVisible(false);
			no.setVisible(false);
		}
		
		totalsChecker();
		
		if ((playerMaxTotal == 21 || playerMinTotal >= 21) && hit_stay_q == true) {
			int tempMax = 0;
			if (playerMaxTotal <= 21) {
				tempMax = playerMaxTotal;
			} else {
				tempMax = playerMinTotal;
			}
			String mess = ("Player total: " + Integer.toString(tempMax) + ")");
			Log.add(new Message(mess, "Player"));
			hit_stay_q = false;
			dealer_turn = true;
		}
		
		if ((dealerMaxTotal == 21 || dealerMinTotal >= 21) && dealer_turn == true) {
			int tempMax = 0;
			if (dealerMaxTotal <= 21) {
				tempMax = dealerMaxTotal;
			} else {
				tempMax = dealerMinTotal;
			}
			String mess = ("Dealer total: " + Integer.toString(tempMax) + ")");
			Log.add(new Message(mess, "Dealer"));
			setWinner();
			dealer_turn = false;
			play_more_q = true;
		}
		
		repaint();
		
		try {
			Thread.sleep(2500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public class Board extends JPanel {
		
		public void paintComponent(Graphics g) {
			//background
			g.setColor(colorBackground);
			g.fillRect(0, 0, aW, aH);
			
			//questions
			if (hit_stay_q == true) {
				g.setColor(Color.black);
				g.setFont(questionFont);
				g.drawString(questHitStay, gridX+gridW+60, gridY+90);
				g.drawString("Total:", gridX+gridW+60, gridY+290);
				if (playerMinTotal == playerMaxTotal) {
					g.drawString(Integer.toString(playerMaxTotal), gridX+gridW+60, gridY+350);
				} else if (playerMaxTotal <= 21) {
					g.drawString(Integer.toString(playerMinTotal) + " or " + Integer.toString(playerMaxTotal), gridX+gridW+60, gridY+350);
				} else {
					g.drawString(Integer.toString(playerMinTotal), gridX+gridW+60, gridY+350);
				}
			} else if (play_more_q == true) {
				g.setColor(Color.black);
				g.setFont(questionFont);
				g.drawString(questPlayMore, gridX+gridW+70, gridY+490);
			}
			g.setColor(Color.black);
			g.fillRect(gridX, gridY+gridH+50, gridW, 500);
			
			//Log
			g.setFont(logFont);
			int logIndex = 0;
			for (Message L : Log) {
				if (L.getWho().equalsIgnoreCase("Dealer")) {
					g.setColor(cDealer);
				} else {
					g.setColor(cPlayer);
				}
				g.drawString(L.getMessage(), gridX+20, gridY+480+logIndex*35);
				logIndex++;
			}
			
			//score
			g.setColor(Color.BLACK);
			g.setFont(questionFont);
			String score = ("Score: " + Integer.toString(Main.pWins) + " - " + Integer.toString(Main.dWins));
			g.drawString(score, gridX+gridW+70, gridY+gridH+300);
			
			//player cards
			int index = 0;
			for(Card c : playerCards) {
				g.setColor(Color.WHITE);
				g.fillRoundRect(gridX+index*cardTW+spacing, gridY+spacing, cardW, cardH, 10, 10);
				g.setColor(Color.BLACK);
				
				if(c.shape.equalsIgnoreCase("Hearts")||c.shape.equalsIgnoreCase("Diamonds")) {
					g.setColor(Color.RED);
				}
				
				g.setFont(cardFont);
				g.drawString(c.symbol, gridX+index*cardTW+spacing*2, gridY+cardH); // Card numbers

				if(c.shape.equalsIgnoreCase("Clubs")) { // Draw clubs card
					g.setColor(Color.BLACK);
					g.fillOval(gridX+index*cardTW+35, gridY+85, 40, 40);
					g.fillOval(gridX+index*cardTW+75, gridY+85, 40, 40);
					g.fillOval(gridX+index*cardTW+55, gridY+55, 40, 40);
					g.fillRect(gridX+index*cardTW+70, gridY+90, 10, 45);
				}
				else if(c.shape.equalsIgnoreCase("Spades")) { // Draw spades card
					g.setColor(Color.BLACK);
					g.fillOval(gridX+index*cardTW+40, gridY+85, 40, 40);
					g.fillOval(gridX+index*cardTW+70, gridY+85, 40, 40);
					g.fillArc(gridX+index*cardTW+30, gridY+28, 90, 70, 230, 80);
					g.fillRect(gridX+index*cardTW+70, gridY+90, 10, 45);
				}
				else if(c.shape.equalsIgnoreCase("Diamonds")) { // Draw diamond card
					g.setColor(Color.RED);
					int x1, x2, x3, x4, y1, y2, y3, y4;
					x1 = 75 + gridX + index*cardTW;
					y1 = 60 + gridY;
					x2 = 50 + gridX + index*cardTW;;
					y2 = 100 + gridY;
					x3 = 75 + gridX + index*cardTW;;
					y3 = 140 + gridY;
					x4 = 100 + gridX + index*cardTW;;
					y4 = 100 + gridY;
					int[] xPoly = {x1, x2, x3, x4} ;
					int[] yPoly = {y1, y2, y3, y4} ;
					g.fillPolygon(xPoly, yPoly, 4);
				}
				else { // Draw hearts card
					g.setColor(Color.RED);
					g.fillOval(gridX+index*cardTW+40, gridY+70, 40, 40);
					g.fillOval(gridX+index*cardTW+70, gridY+70, 40, 40);
					g.fillArc(gridX+index*cardTW+30, gridY+96, 90, 70, 50, 80);
				}
				index++;
			}
			
			// Dealer cards
			if(dealer_turn == true) {
				index = 0;
				for(Card c: dealerCards) {
					g.setColor(Color.WHITE);
					g.fillRoundRect(gridX+index*cardTW+spacing, gridY+spacing+200, cardW, cardH, 10, 10);
					g.setColor(Color.BLACK);
					
					if(c.shape.equalsIgnoreCase("Hearts")||c.shape.equalsIgnoreCase("Diamonds")) {
						g.setColor(Color.RED);
					}
					
					g.setFont(cardFont);
					g.drawString(c.symbol, gridX+index*cardTW+spacing*2, gridY+cardTH+cardH); // Card numbers
	
					if(c.shape.equalsIgnoreCase("Clubs")) { // Draw clubs card
						g.setColor(Color.BLACK);
						g.fillOval(gridX+index*cardTW+35, gridY+cardTH+85, 40, 40);
						g.fillOval(gridX+index*cardTW+75, gridY+cardTH+85, 40, 40);
						g.fillOval(gridX+index*cardTW+55, gridY+cardTH+55, 40, 40);
						g.fillRect(gridX+index*cardTW+70, gridY+cardTH+90, 10, 45);
					}
					else if(c.shape.equalsIgnoreCase("Spades")) { // Draw spades card
						g.setColor(Color.BLACK);
						g.fillOval(gridX+index*cardTW+40, gridY+cardTH+85, 40, 40);
						g.fillOval(gridX+index*cardTW+70, gridY+cardTH+85, 40, 40);
						g.fillArc(gridX+index*cardTW+30, gridY+cardTH+28, 90, 70, 230, 80);
						g.fillRect(gridX+index*cardTW+70, gridY+cardTH+90, 10, 45);
					}
					else if(c.shape.equalsIgnoreCase("Diamonds")) { // Draw diamond card
						g.setColor(Color.RED);
						int x1, x2, x3, x4, y1, y2, y3, y4;
						x1 = 75 + gridX + index*cardTW;
						y1 = 60 + gridY + cardTH;
						x2 = 50 + gridX + index*cardTW;;
						y2 = 100 + gridY + cardTH;
						x3 = 75 + gridX + index*cardTW;;
						y3 = 140 + gridY + cardTH;
						x4 = 100 + gridX + index*cardTW;;
						y4 = 100 + gridY + cardTH;
						int[] xPoly = {x1, x2, x3, x4} ;
						int[] yPoly = {y1, y2, y3, y4} ;
						g.fillPolygon(xPoly, yPoly, 4);
					}
					else { // Draw hearts card
						g.setColor(Color.RED);
						g.fillOval(gridX+index*cardTW+40, gridY+cardTH+70, 40, 40);
						g.fillOval(gridX+index*cardTW+70, gridY+cardTH+70, 40, 40);
						g.fillArc(gridX+index*cardTW+30, gridY+cardTH+96, 90, 70, 50, 80);
					}
					index++;
				}
				
				g.setColor(Color.black);
				g.setFont(questionFont);
				g.drawString("Your total: ", gridX+gridW+60, gridY+40);
				if (playerMaxTotal <= 21) {
					g.drawString(Integer.toString(playerMaxTotal), gridX+gridW+60, gridY+120);
				} else {
					g.drawString(Integer.toString(playerMinTotal), gridX+gridW+60, gridY+120);
				}
				g.drawString("Dealer's total: ", gridX+gridW+60, gridY+240);
				if (dealerMaxTotal <= 21) {
					g.drawString(Integer.toString(dealerMaxTotal), gridX+gridW+60, gridY+320);
				} else {
					g.drawString(Integer.toString(dealerMinTotal), gridX+gridW+60, gridY+320);
				}
			}
			
		}
		
	}
	

	public class ActHit implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (hit_stay_q == true) {
			//	System.out.println("You clicked 'HIT'");
				
				int tempMax = 0;
				if (playerMaxTotal <= 21) {
					tempMax = playerMaxTotal;
				} else {
					tempMax = playerMinTotal;
				}
				String mess = ("You decided to hit! (total: " + Integer.toString(tempMax) + ")");
				Log.add(new Message(mess, "Player"));
				
				tempC = rand.nextInt(52);
				while (Cards.get(tempC).used == true) {
					tempC = rand.nextInt(52);
				}
				playerCards.add(Cards.get(tempC));
				Cards.get(tempC).setUsed();
			//	System.out.println("Card " + pCards.get(pCards.size()-1).name + " of " + pCards.get(pCards.size()-1).shape + " added to the player's cards.");
			}
		}
		
	}
	
	public class ActStay implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if (hit_stay_q == true) {
			//	System.out.println("You clicked 'STAY'");
				
				int tempMax = 0;
				if (playerMaxTotal <= 21) {
					tempMax = playerMaxTotal;
				} else {
					tempMax = playerMinTotal;
				}
				String mess = ("You decided to stay! (total: " + Integer.toString(tempMax) + ")");
				Log.add(new Message(mess, "Player"));
				
				hit_stay_q = false;
				dealer_turn = true;
			}
		}
		
	}
	
	public class ActYes implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
		//	System.out.println("You clicked 'YES'");
			
			for (Card c : Cards) {
				c.setNotUsed();
			}
			
			playerCards.clear();
			dealerCards.clear();
			Log.clear();
			
			play_more_q = false;
			hit_stay_q = true;
			
			playerCards.add(Cards.pop());
			dealerCards.add(Cards.pop());
			playerCards.add(Cards.pop());
			dealerCards.add(Cards.pop());
		}
	}
	
	public class ActNo implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
		//	System.out.println("You clicked 'NO'");
			Main.sentinel = true;
			dispose();
		}
	}
}