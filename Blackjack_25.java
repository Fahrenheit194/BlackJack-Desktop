import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Blackjack_25 extends JFrame
{
    //------ Game variables ------//
    int round, bankroll, bet;
    int playerSum, dealerSum;
    int playerAceCount, dealerAceCount;
    int playerCardOrder, dealerCardOrder;
    boolean inRound, dealerHoleRevealed;

    //------ Deck (1–52 indexing) ------//
    String[] cardFile = new String[53];
    int[] cardValue = new int[53];
    boolean[] cardUsed = new boolean[53];
    int cardCount;

    //------ Card UI layout ------//
    private static final int MAX_CARDS = 11;      // 11 is the max amount of cards a player can hold in blackjack
    private static final int TABLE_W = 460;
    private static final int CARD_W = 80;
    private static final int CARD_H = 120;
    private static final int CARD_STEP = 20;
    private static final int DEALER_Y = 95;
    private static final int PLAYER_Y = 330;

    //------ UI elements ------//
    JLabel lblBank = new JLabel("Bankroll: $0");
    JLabel lblBet = new JLabel("Current Bet: --");
    JLabel lblDealerSum = new JLabel("Sum: 0");
    JLabel lblPlayerSum = new JLabel("Sum: 0");

    JButton btnStart = new JButton("Play");
    JButton btnHit = new JButton("Hit");
    JButton btnStand = new JButton("Stand");
    JButton btnDouble = new JButton("Double");
    JButton btnAddBank = new JButton("Add to Bankroll");
    JButton btnQuit = new JButton("Quit Game");
    JButton btnHow = new JButton("How to Play?");

    // Card labels shown for the player/dealer
    JLabel[] playerCards = new JLabel[MAX_CARDS];
    JLabel[] dealerCards = new JLabel[MAX_CARDS];

    // Panels
    JPanel stat;
    JPanel betPanel;

    // Home screen overlay
    JPanel homePanel = new JPanel(null);
    JLabel lblHomeLogo = new JLabel();
    JButton btnHowHome = new JButton("How to Play");

    // Card back
    ImageIcon cardBackIcon = loadIcon("/images/cardBack.png");

    public Blackjack_25()
    {
        super("BlackJack");
        initDeck();
        buildUI();
        resetGame();
    }

    //------ UI build ------//
    public void buildUI()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(460, 720);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(null);
        root.setBackground(new Color(0, 51, 0));
        setContentPane(root);

        // Bottom stat bar
        stat = new JPanel(null);
        stat.setBackground(new Color(5, 31, 5));
        stat.setBounds(0, 600, 444, 80);
        root.add(stat);

        lblBank.setForeground(Color.WHITE);
        lblBank.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblBank.setBounds(15, 10, 220, 20);
        stat.add(lblBank);

        btnHow.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnHow.setBounds(325, 12, 105, 17);
        btnHow.addActionListener(this::tutorialButtonActionPerformed);
        stat.add(btnHow);

        btnAddBank.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnAddBank.setBounds(325, 35, 105, 17);
        btnAddBank.addActionListener(this::addBankButtonActionPerformed);
        stat.add(btnAddBank);

        btnQuit.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnQuit.setBounds(325, 57, 105, 17);
        btnQuit.addActionListener(e -> resetGame());
        stat.add(btnQuit);

        // Bet panel
        betPanel = new JPanel(new BorderLayout());
        betPanel.setBackground(new Color(5, 31, 5));
        betPanel.setBounds(100, 260, 260, 32);
        betPanel.setBorder(BorderFactory.createBevelBorder(1,
        new Color(14, 26, 0), new Color(14, 26, 0),
        new Color(22, 58, 22), new Color(22, 58, 22)));
        root.add(betPanel);

        lblBet.setForeground(Color.WHITE);
        lblBet.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblBet.setHorizontalAlignment(SwingConstants.CENTER);
        betPanel.add(lblBet, BorderLayout.CENTER);

        // Sums
        lblDealerSum.setForeground(Color.WHITE);
        lblDealerSum.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblDealerSum.setBounds(205, 70, 100, 20);
        root.add(lblDealerSum);

        lblPlayerSum.setForeground(Color.WHITE);
        lblPlayerSum.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblPlayerSum.setBounds(205, 500, 100, 20);
        root.add(lblPlayerSum);

        // Dealer cards
        for (int i = 0; i < MAX_CARDS; i++)
        {
            dealerCards[i] = new JLabel();
            dealerCards[i].setBounds(0, DEALER_Y, CARD_W, CARD_H);
            dealerCards[i].setVisible(false);
            root.add(dealerCards[i]);
        }

        // Player cards
        for (int i = 0; i < MAX_CARDS; i++)
        {
            playerCards[i] = new JLabel();
            playerCards[i].setBounds(0, PLAYER_Y, CARD_W, CARD_H);
            playerCards[i].setVisible(false);
            root.add(playerCards[i]);
        }

        // Game buttons
        btnHit.setBounds(50, 540, 80, 28);
        btnStand.setBounds(190, 540, 80, 28);
        btnDouble.setBounds(330, 540, 80, 28);

        btnHit.addActionListener(this::btnHitActionPerformed);
        btnStand.addActionListener(this::btnStandActionPerformed);
        btnDouble.addActionListener(this::btnDoubleActionPerformed);

        root.add(btnHit);
        root.add(btnStand);
        root.add(btnDouble);

        // Start button (same instance used on home)
        btnStart.setFont(new Font("Yu Gothic UI", Font.BOLD, 18));
        btnStart.setBounds(180, 360, 100, 38);
        btnStart.addActionListener(this::btnStartButtonActionPerformed);

        // Home screen
        homePanel.setBounds(0, 0, 460, 720);
        homePanel.setBackground(new Color(0, 51, 0));
        homePanel.setOpaque(true);
        homePanel.setLayout(null);

        // Logo
        ImageIcon raw = loadIcon("/images/bj_new_logo.png");
        Image scaled = raw.getImage().getScaledInstance(360, -1, Image.SCALE_SMOOTH);
        lblHomeLogo.setIcon(new ImageIcon(scaled));
        lblHomeLogo.setBounds(32, 60, 380, 280);
        lblHomeLogo.setHorizontalAlignment(SwingConstants.CENTER);
        homePanel.add(lblHomeLogo);

        // Play button on home
        homePanel.add(btnStart);

        // How-to-play on home
        btnHowHome.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        btnHowHome.setBounds(330, 650, 98, 17);
        btnHowHome.addActionListener(this::tutorialButtonActionPerformed);
        btnHowHome.setOpaque(true);
        btnHowHome.setContentAreaFilled(true);
        btnHowHome.setBorderPainted(true);
        homePanel.add(btnHowHome);

        root.add(homePanel);
    }

    //------ Home toggle ------//
    public void setHomeVisible(boolean show)
    {
        homePanel.setVisible(show);

        stat.setVisible(!show);
        betPanel.setVisible(!show);

        btnHit.setVisible(!show);
        btnStand.setVisible(!show);
        btnDouble.setVisible(!show);

        lblDealerSum.setVisible(!show);
        lblPlayerSum.setVisible(!show);

        if (show)
        {
            for (int i = 0; i < MAX_CARDS; i++)
            {
                dealerCards[i].setVisible(false);
                playerCards[i].setVisible(false);
            }
        }
    }

    //------ Card row centering ------//
    private void layoutHand(JLabel[] cards, int count, int y)
    {
        if (count <= 0) return;

        int handW = CARD_W + (count - 1) * CARD_STEP;
        int startX = (TABLE_W - handW) / 2;

        for (int i = 0; i < count && i < cards.length; i++)
        {
            cards[i].setBounds(startX + i * CARD_STEP, y, CARD_W, CARD_H);
        }
    }

    //------ Reset game ------//
    public void resetGame()
    {
        round = 0;
        bankroll = 0;
        bet = 0;

        inRound = false;
        dealerHoleRevealed = false;

        playerSum = 0;
        dealerSum = 0;
        playerAceCount = 0;
        dealerAceCount = 0;

        playerCardOrder = 1;
        dealerCardOrder = 1;

        btnHit.setEnabled(false);
        btnStand.setEnabled(false);
        btnDouble.setEnabled(false);

        lblBank.setText("Bankroll: $0");
        lblBet.setText("Current Bet: --");
        lblDealerSum.setText("Sum: 0");
        lblPlayerSum.setText("Sum: 0");

        for (int i = 0; i < MAX_CARDS; i++)
        {
            dealerCards[i].setVisible(false);
            playerCards[i].setVisible(false);
            dealerCards[i].setIcon(null);
            playerCards[i].setIcon(null);
            dealerCards[i].setName(null);
        }

        resetDeck();
        setHomeVisible(true);
    }

    //------ Start button ------//
    private void btnStartButtonActionPerformed(ActionEvent evt)
    {
        String ini = JOptionPane.showInputDialog(null, "Enter your bankroll:");
        if (ini == null) return;

        ini = ini.trim();

        try
        {
            if (ini.length() == 0)
            {
            bankroll = 1000;
            }
            
            else
            {
                bankroll = Integer.parseInt(ini);
            }
        }
        
        catch (Exception e)
        {
            bankroll = 1000;
        }

        if (bankroll <= 0) bankroll = 1000;
        if (bankroll > 100000000) bankroll = 100000000;

        lblBank.setText("Bankroll: $" + bankroll);

        setHomeVisible(false);
        newRound();
    }

    //------ Round start ------//
    public void newRound()
    {
        if (bankroll <= 0)
        {
            int quitGame = JOptionPane.showConfirmDialog(null,
                    "You have no money left. Would you like to quit the game?",
                    "Warning", JOptionPane.YES_NO_OPTION);

            if (quitGame == JOptionPane.YES_OPTION)
            {
                resetGame();
                return;
            }

            addMoneyFlow();
            if (bankroll <= 0)
            {
                resetGame();
                return;
            }
        }

        round++;
        inRound = true;
        dealerHoleRevealed = false;

        playerSum = 0; dealerSum = 0;
        playerAceCount = 0; dealerAceCount = 0;
        playerCardOrder = 1; dealerCardOrder = 1;

        lblPlayerSum.setText("Sum: 0");
        lblDealerSum.setText("Sum: 0");
        lblBet.setText("Current Bet: --");

        for (int i = 0; i < MAX_CARDS; i++)
        {
            dealerCards[i].setVisible(false);
            playerCards[i].setVisible(false);
            dealerCards[i].setIcon(null);
            playerCards[i].setIcon(null);
            dealerCards[i].setName(null);
        }

        String betString = JOptionPane.showInputDialog(null, "Round " + round + "\nEnter the betting amount:");
        if (betString == null)
        {
            resetGame();
            return;
        }

        int requestedBet;
        try {requestedBet = Integer.parseInt(betString.trim());}
        catch (Exception e) {requestedBet = -1;}

        bet = roundBet(requestedBet);

        btnHit.setEnabled(true);
        btnStand.setEnabled(true);
        btnDouble.setEnabled(bankroll >= bet);

        dealerDraw(true);
        dealerDraw(false);

        playerDraw();
        playerDraw();

        if (playerSum == 21)
        {
            revealDealerHole();
            int win = (int)(bet * 2.5 + 0.5);
            JOptionPane.showMessageDialog(this, "-$- BLACKJACK -$-\n\nYou won $" + win);
            bankroll += win;
            lblBank.setText("Bankroll: $" + bankroll);
            lblBet.setText("Current Bet: --");
            newRound();
        }
    }

    //------ Bet function ------//
    public int roundBet(int b)
    {
        while (b <= 0 || b > bankroll)
        {
            String s;

            if (b <= 0)
            {
                s = JOptionPane.showInputDialog(null,
                        "Please bet an amount greater than 0.");
            }
            else
            {
                s = JOptionPane.showInputDialog(null,
                        "Your current bankroll only has $" + bankroll +
                        ". Please enter a valid amount.");
            }

            if (s == null) return 1;

            try {b = Integer.parseInt(s.trim());}
            catch (Exception e) { b = -1; }
        }

        bankroll -= b;

        betPanel.setVisible(true);
        lblBet.setText("Current Bet: $" + b);
        lblBank.setText("Bankroll: $" + bankroll);

        return b;
    }

    //------ Hit ------//
    private void btnHitActionPerformed(ActionEvent evt)
    {
        if (!inRound) return;

        btnDouble.setEnabled(false);
        playerDraw();

        if (playerSum == 21)
        {
            revealDealerHole();
            int win = (int)(bet * 2.5 + 0.5);
            JOptionPane.showMessageDialog(this, "-$- BLACKJACK -$-\n\nYou won $" + win);
            bankroll += win;
            lblBank.setText("Bankroll: $" + bankroll);
            lblBet.setText("Current Bet: --");
            newRound();
            return;
        }

        if (playerSum > 21)
        {
            revealDealerHole();
            JOptionPane.showMessageDialog(this, "BUST\n\nYou lost $" + bet);
            lblBet.setText("Current Bet: --");
            newRound();
        }
    }

    //------ Stand ------//
    private void btnStandActionPerformed(ActionEvent evt)
    {
        if (!inRound) return;

        revealDealerHole();
        while (dealerSum <= 16) dealerDraw(true);
        settleRound();
    }

    //------ Double ------//
    private void btnDoubleActionPerformed(ActionEvent evt)
    {
        if (!inRound) return;

        if (bankroll < bet)
        {
            JOptionPane.showMessageDialog(this, "Not enough money to double");
            btnDouble.setEnabled(false);
            return;
        }

        bankroll -= bet;
        bet *= 2;

        lblBank.setText("Bankroll: $" + bankroll);
        lblBet.setText("Current Bet: $" + bet);

        btnDouble.setEnabled(false);

        playerDraw();

        if (playerSum > 21)
        {
            revealDealerHole();
            JOptionPane.showMessageDialog(this, "BUST\n\nYou lost $" + bet);
            lblBet.setText("Current Bet: --");
            newRound();
            return;
        }

        revealDealerHole();
        while (dealerSum <= 16) dealerDraw(true);
        settleRound();
    }

    //------ Round settle ------//
    public void settleRound()
    {
        if (dealerSum > 21)
        {
            JOptionPane.showMessageDialog(this, "-WIN!-\nDealer Busts\nYou won $" + bet * 2);
            bankroll += bet * 2;
        }
        else if (dealerSum == 21 && playerSum != 21)
        {
            JOptionPane.showMessageDialog(this, "LOSE\nDealer Blackjack\nYou lost $" + bet);
        }
        else if (21 - playerSum < 21 - dealerSum)
        {
            JOptionPane.showMessageDialog(this, "-WIN!-\n\nYou won $" + bet * 2);
            bankroll += bet * 2;
        }
        else if (21 - playerSum == 21 - dealerSum)
        {
            JOptionPane.showMessageDialog(this, "PUSH\n\nYou tied with the dealer.");
            bankroll += bet;
        }
        else
        {
            JOptionPane.showMessageDialog(this, "LOSE\nDealer wins\nYou lost $" + bet);
        }

        lblBank.setText("Bankroll: $" + bankroll);
        lblBet.setText("Current Bet: --");

        newRound();
    }

    //------ How to play + Add bankroll ------//
    private void tutorialButtonActionPerformed(ActionEvent evt)
    {
        JOptionPane.showMessageDialog(this, "---GAMEPLAY:---\n\n" +
                "*Player gets 2 face-up cards; dealer gets 1 face-up, 1 face-down.\n\n" +
                "AIM: Get closer to 21 than the dealer.\n\n" +
                "CARD VALUES: 2-10 as numbered, face cards 10, aces 1 or 11.\n\n" +
                "ACTIONS:\n" +
                "Hit: Take another card.\n" +
                "Stand: Keep current sum.\n" +
                "Double: Double bet and take one final card.\n\n" +
                "---WINNING:---\n\n" +
                "REGULAR WIN: 1:1 payout.\n" +
                "BLACKJACK WIN: 3:2 payout.");
    }

    private void addBankButtonActionPerformed(ActionEvent evt)
    {
        addMoneyFlow();
    }

    public void addMoneyFlow()
    {
        String addBankString = JOptionPane.showInputDialog(null, "Enter amount to add:");
        if (addBankString == null) return;

        int add;
        try {add = Integer.parseInt(addBankString.trim());}
        catch (Exception e) {add = 0;}

        if (add <= 0)
        {
            JOptionPane.showMessageDialog(this, "You can only add money to your bankroll.");
            return;
        }

        if (bankroll + add > 100000000)
        {
            JOptionPane.showMessageDialog(this, "Bankroll cannot exceed $100 000 000,\nAutomatically setting to $100 000 000");
            bankroll = 100000000;
        }
        
        else
        {
            JOptionPane.showMessageDialog(this, "Adding $" + add + " to your bankroll.");
            bankroll += add;
        }

        lblBank.setText("Bankroll: $" + bankroll);

        if (inRound)
        {
            if (bankroll >= bet)
            {
                btnDouble.setEnabled(true);
            }

            else
            {
                btnDouble.setEnabled(false);
            }
        }
    }

    //------ Draw actions & ace adjustment ------//
    public void playerDraw()
    {
        if (playerCardOrder > MAX_CARDS) return;

        int cardIndex = drawUniqueCard();

        JLabel target = playerCards[playerCardOrder - 1];
        target.setVisible(true);
        target.setIcon(loadIcon("/images/" + cardFile[cardIndex]));

        if (cardValue[cardIndex] == 11) playerAceCount++;
        playerSum += cardValue[cardIndex];

        while (playerSum > 21 && playerAceCount > 0)
        {
            playerSum -= 10;
            playerAceCount--;
        }

        lblPlayerSum.setText("Sum: " + playerSum);

        playerCardOrder++;
        cardCount++;

        layoutHand(playerCards, playerCardOrder - 1, PLAYER_Y);
    }

    public void dealerDraw(boolean showFaceUp)
    {
        if (dealerCardOrder > MAX_CARDS) return;

        int cardIndex = drawUniqueCard();

        JLabel target = dealerCards[dealerCardOrder - 1];
        target.setVisible(true);

        if (showFaceUp)
        {
            target.setIcon(loadIcon("/images/" + cardFile[cardIndex]));
            target.setName(null);
        }
        else
        {
            target.setIcon(cardBackIcon);
            target.setName(cardFile[cardIndex]);
        }

        if (cardValue[cardIndex] == 11) dealerAceCount++;
        dealerSum += cardValue[cardIndex];

        while (dealerSum > 21 && dealerAceCount > 0)
        {
            dealerSum -= 10;
            dealerAceCount--;
        }

        if (showFaceUp || dealerHoleRevealed)
        {
            lblDealerSum.setText("Sum: " + dealerSum);
        }

        dealerCardOrder++;
        cardCount++;

        layoutHand(dealerCards, dealerCardOrder - 1, DEALER_Y);
    }

    public void revealDealerHole()
    {
        if (dealerHoleRevealed) return;

        dealerHoleRevealed = true;

        if (dealerCards[1].isVisible())
        {
            String real = dealerCards[1].getName();
            if (real != null && real.length() > 0)
            {
                dealerCards[1].setIcon(loadIcon("/images/" + real));
                dealerCards[1].setName(null);
            }
        }

        lblDealerSum.setText("Sum: " + dealerSum);
    }

    //------ Deck helpers ------//
    public void resetDeck()
    {
        for (int i = 1; i <= 52; i++) cardUsed[i] = false;
        cardCount = 0;
    }

    public int drawUniqueCard()
    {
        if (cardCount >= 52) resetDeck();

        int pick = (int)(Math.random() * 52) + 1;
        while (cardUsed[pick]) pick = (int)(Math.random() * 52) + 1;

        cardUsed[pick] = true;
        return pick;
    }

    public void initDeck()
    {
        String[] suits = {"clubs", "diamonds", "hearts", "spades"};
        int index = 1;

        for (int s = 0; s < 4; s++)
        {
            for (int r = 2; r <= 10; r++)
            {
                cardFile[index] = r + "_of_" + suits[s] + ".png";
                cardValue[index] = r;
                index++;
            }

            cardFile[index] = "jack_of_" + suits[s] + "2.png";  cardValue[index] = 10; index++;
            cardFile[index] = "queen_of_" + suits[s] + "2.png"; cardValue[index] = 10; index++;
            cardFile[index] = "king_of_" + suits[s] + "2.png";  cardValue[index] = 10; index++;

            if (suits[s].equals("spades"))
            {
                cardFile[index] = "ace_of_spades2.png";
            }
            else
            {
                cardFile[index] = "ace_of_" + suits[s] + ".png";
            }
            
            cardValue[index] = 11;
            index++;
        }
    }
    
    // image loader
    public ImageIcon loadIcon(String path)
    {
        try
        {
            java.net.URL url = getClass().getResource(path);
            if (url == null) return new ImageIcon();
            return new ImageIcon(url);
        }
        catch (Exception e)
        {
            return new ImageIcon();
        }
    }

    //------ Main ------//
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> new Blackjack_25().setVisible(true));
    }
}
