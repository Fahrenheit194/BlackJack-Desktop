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
1. Enter starting bankroll
2. Place a bet
3. Player and dealer receive cards
4. Player chooses actions
5. Dealer plays according to rules
6. Round is settled and bankroll is updated

## Project Structure
- `Blackjack_25.java` - game logic and UI
- `images/` - card assets and logo

## License
MIT