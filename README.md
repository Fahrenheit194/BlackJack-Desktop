# BlackJack Desktop

BlackJack Desktop is a **Blackjack desktop game** built in **Java using Swing**.  
It implements standard game rules, betting logic, deck management, and ace handling.

Originally developed in 2024 and revised for improved structure in 2025.

## Features
- Full Blackjack ruleset (Hit, Stand, Double)
- Dealer rules (hit on <= 16, stand on >= 17)
- Bankroll and per-round betting system
- Ace value handling (1 or 11)
- Deck tracking to prevent duplicate card draws
- Automatic deck reset after running out

## Gameplay Flow

### 1. Enter starting bankroll

When the game starts, the player enters their initial bankroll.
This value determines how much money the player can bet during the session.

<img width="446" height="714" alt="Starting bankroll input" src="https://github.com/user-attachments/assets/7cc4cfef-ca0d-490a-8781-542f6ca6fb08" />

---

### 2. Place a bet

At the beginning of each round, the player enters a betting amount.
The bet is deducted from the bankroll before cards are dealt.

<img width="448" height="714" alt="Bet input screen" src="https://github.com/user-attachments/assets/3f19a0b9-acac-47a3-aa11-b92c07987cfc" />

---

### 3. Cards are dealt

Both the player and dealer receive their starting cards.
The dealer's hidden card is revealed later in the round.

<img width="450" height="716" alt="Cards dealt" src="https://github.com/user-attachments/assets/91b889fb-94e5-4c2c-a477-38f551782535" />

---

### 4. Player actions

The player can choose one of the available actions:

* **Hit** - draw another card
* **Stand** - keep the current hand
* **Double** - double the bet and receive one final card

---

### 5. Dealer turn

After the player stands or busts, the dealer reveals their hidden card and plays according to standard blackjack rules.

---

### 6. Round result

The round outcome is displayed and the bankroll is updated accordingly.

<img width="448" height="715" alt="Round result screen" src="https://github.com/user-attachments/assets/bd007fb6-a099-4849-883a-05b1d61851dc" /> <img width="448" height="714" alt="image" src="https://github.com/user-attachments/assets/d5006486-98ef-4712-bc68-52a630334c87" />




## Project Structure
- `Blackjack_25.java` - game logic and UI
- `images/` - card assets and logo

## License

MIT
