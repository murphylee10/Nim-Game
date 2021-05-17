/*
 * Date: March 16, 2021
 * Name: Murphy Lee
 * Description: Use for loops and while loops to create a unique game of nim 
 * */
import java.util.Scanner;
import java.util.Random;

class Nim {
    public static void main (String[] args) {
        // Initialize instance of Scanner
        Scanner reader = new Scanner(System.in);
        // Initialize instance of Random
        Random rand = new Random();

        // Declare variables 
        String currentPlayer; // For the 1st player/the player who's turn it is
        String waitingPlayer; // For the player who isn't currently going
        String player; // For the player to store their name in the player vs computer version
        String pileChoice; // For both users' pile choice
        int counterChoice; // For how many counters the user wants to take
        String winner;
        String loser;
        String playerSwap; // This variable will allow us to swap the current and waiting player through substitution
        
        // Assign variables
        boolean invalid = false; // Boolean variable for if the user's input is invalid - set to true if their input is invalid
        boolean gameWon = false; // Boolean variable for if the game is won - set to false by default

        // These three booleans become true when the pile has 0 counters
        boolean pileAEmpty = false;
        boolean pileBEmpty = false;
        boolean pileCEmpty = false;

        // These declared variables are for the AI to use
        // Binary digits for each pile size
        int pileADigit1;
        int pileADigit2;
        int pileADigit3;
        int pileADigit4;

        int pileBDigit1;
        int pileBDigit2;
        int pileBDigit3;
        int pileBDigit4;

        int pileCDigit1;
        int pileCDigit2;
        int pileCDigit3;
        int pileCDigit4;

        // Nim sum of all the pile sizes
        int totalNimSumDigit1; 
        int totalNimSumDigit2;
        int totalNimSumDigit3;
        int totalNimSumDigit4;

        // Nim sum of the each pile size and the totalNimSum variable
        int sumADigit1;
        int sumADigit2;
        int sumADigit3;
        int sumADigit4;

        int sumBDigit1;
        int sumBDigit2;
        int sumBDigit3;
        int sumBDigit4;

        int sumCDigit1;
        int sumCDigit2;
        int sumCDigit3;
        int sumCDigit4;

        // Decimal form of each pile sum
        int sumADecimal;
        int sumBDecimal;
        int sumCDecimal;

        // We will mark the largest pile, for special nim cases where there is only 1 pile with more than 2 counters
        String largestPile;

        // Ordering of piles - the computer can remove 1 from the largest pile if there's a nim sum of 0
        int largestPileSize;
        int moderatePileSize;
        int smallestPileSize;

        // Variables representing the computer's choices
        String compPileChoice;
        int compCounterChoice;

        // Assign the max and min values of the random numbers into corresponding variables
        // The maximum valid number is 10, so our bound will be 11
        int max = 11;
        int min = 1;

        // Use Random module to place random counters between 1 and 10 into 3 piles
        int pileA = rand.nextInt(max - min) + min;
        int pileB = rand.nextInt(max - min) + min;
        int pileC = rand.nextInt(max - min) + min;

        // Prompt user - they can play PvP or play against the computer
        System.out.print("Welcome! Would you like to play 'PvP' or play against the 'computer'? ");
        String playMode = reader.nextLine().toLowerCase(); // The toLowerCase method ensures that uppercase letters are accepted

        // If the user didn't enter a valid game mode, prompt for reinput
        while ((!playMode.equals("pvp")) && (!playMode.equals("computer"))) {
            System.out.print("Sorry, that play mode is invalid. Please re-enter 'Pvp' or 'computer': ");
            playMode = reader.nextLine().toLowerCase();
        }

        // If the user selected pvp, prompt 2 players for their name
        if (playMode.equals("pvp")) {
            // Prompt 2 players to enter their names
            System.out.print("Player 1, please enter your name: ");
            currentPlayer = reader.nextLine();     // Player 1 will have the first turn - current player
            System.out.print("Player 2, please enter your name: ");
            waitingPlayer = reader.nextLine();

            // If either of the users didn't type a name,  make their names "Player 1" and "Player 2"
            if (currentPlayer.equals("") || waitingPlayer.equals("")) {
                currentPlayer = "Player 1";
                waitingPlayer = "Player 2";
            }
            // If the users tried to name themselves as "Computer", make their names "Player 1 or Player 2"
            else if (currentPlayer.equals("Computer") || waitingPlayer.equals("Computer")) {
                currentPlayer = "Player 1";
                waitingPlayer = "Player 2";
            }
        }
        // Otherwise, the user selected computer mode
        else {
            // Prompt player to enter their name
            System.out.print("Enter your name: ");
            player = reader.nextLine();
            // If the player enters a blank name or tries to name themself as "Computer", name them "Player"
            if (player.equals("") || player.equals("Computer")) {
                player = "Player";
            }

            // Use a random number generator (can choose 0 or 1) to decide who plays first
            int firstPlayer = rand.nextInt(2);
            // If random number generator selects 0, the player plays first
            if (firstPlayer == 0) {
                currentPlayer = player;
                waitingPlayer = "Computer";
                System.out.println(player + ", you have been selected to play first!");
            }
            else {
                currentPlayer = "Computer";
                waitingPlayer = player;
                System.out.println("The computer has been selected to play first!");
            }
        }
        // Output the initial amount of counters in each pile
        // Use for loops to print the counters as asterisks (Fancy Display - Rows)
        System.out.print("A: ");
        for (int i = 0; i < pileA; i++) {
            System.out.print("*");
        }
        // Skip to new line for the next character
        System.out.println();

        System.out.print("B: ");
        for (int i = 0; i < pileB; i++) {
            System.out.print("*");
        }
        // Skip to new line for the next character
        System.out.println();

        System.out.print("C: ");
        for (int i = 0; i < pileC; i++) {
            System.out.print("*");
        }
        // Skip to new line for the next character
        System.out.println();
        
        // While loop - keeps the game running until the game is won
        while (gameWon == false) {
            // If it's the computers turn, run the nim-sum algorithm to decide the computer's choice
            if (currentPlayer == "Computer") {

                // Sort pile sizes from smallest to largest, for special cases where we must choose the largest pile
                // Store the letter of the largest pile in a seperate variable
                if (pileA > pileB) {
                    // If pile A is larger than pile B & C, A is the largest value
                    if (pileA > pileC) {
                        largestPile = "A";
                        largestPileSize = pileA;

                        // Order the 2 smaller numbers
                        if (pileC > pileB) {
                            moderatePileSize = pileC;
                            smallestPileSize = pileB;
                        }
                        else {
                            moderatePileSize = pileB;
                            smallestPileSize = pileC;
                        }
                    }
                    // Otherwise, pile C is greater than pile B and A, and is the largest pile
                    else {
                        largestPile = "C";
                        largestPileSize = pileC;
                        moderatePileSize = pileA;
                        smallestPileSize = pileB;
                    }
                }
                // Otherwise, pileB is greater than pile A
                else {
                    // If pile B is greater than pile C, it is the largest pile
                    if (pileB > pileC) {
                        largestPile = "B";
                        largestPileSize = pileB;

                        // Organize the 2 smaller piles
                        if (pileC > pileA) {
                            moderatePileSize = pileC;
                            smallestPileSize = pileA;
                        }
                        else {
                            moderatePileSize = pileA;
                            smallestPileSize = pileC;
                        }
                    }
                    // Otherwise, pile C is greater than B and A, and should be allocated to the largestPile variable
                    else {
                        largestPile = "C";
                        largestPileSize = pileC;
                        moderatePileSize = pileB;
                        smallestPileSize = pileA;
                    }
                }

                // Convert all pile sizes to binary, storing each digit in seperate variables
                // Finding the remainder of the quotient of pile size/2 will give you each binary digit
                pileADigit1 = pileA % 2;
                pileADigit2 = (pileA / 2) % 2;
                pileADigit3 = ((pileA / 2) / 2) % 2;
                pileADigit4 = (((pileA / 2) / 2) / 2) % 2;
                
                pileBDigit1 = pileB % 2;
                pileBDigit2 = (pileB / 2) % 2;
                pileBDigit3 = ((pileB / 2) / 2) % 2;
                pileBDigit4 = (((pileB / 2) / 2) / 2) % 2;

                pileCDigit1 = pileC % 2;
                pileCDigit2 = (pileC / 2) % 2;
                pileCDigit3 = ((pileC / 2) / 2) % 2;
                pileCDigit4 = (((pileC / 2) / 2) / 2) % 2;

                // Find the num sum - binary sum of all the heap sizes - Add all the numbers, and use modulus-2, so that all carries are ignored
                totalNimSumDigit1 = (pileADigit1 + pileBDigit1 + pileCDigit1) % 2;
                totalNimSumDigit2 = (pileADigit2 + pileBDigit2 + pileCDigit2) % 2;
                totalNimSumDigit3 = (pileADigit3 + pileBDigit3 + pileCDigit3) % 2;
                totalNimSumDigit4 = (pileADigit4 + pileBDigit4 + pileCDigit4) % 2;

                // Find the num sum of each pile size and the totalNimSum
                sumADigit1 = (pileADigit1 + totalNimSumDigit1) % 2;
                sumADigit2 = (pileADigit2 + totalNimSumDigit2) % 2;
                sumADigit3 = (pileADigit3 + totalNimSumDigit3) % 2;
                sumADigit4 = (pileADigit4 + totalNimSumDigit4) % 2;

                sumBDigit1 = (pileBDigit1 + totalNimSumDigit1) % 2;
                sumBDigit2 = (pileBDigit2 + totalNimSumDigit2) % 2;
                sumBDigit3 = (pileBDigit3 + totalNimSumDigit3) % 2;
                sumBDigit4 = (pileBDigit4 + totalNimSumDigit4) % 2;

                sumCDigit1 = (pileCDigit1 + totalNimSumDigit1) % 2;
                sumCDigit2 = (pileCDigit2 + totalNimSumDigit2) % 2;
                sumCDigit3 = (pileCDigit3 + totalNimSumDigit3) % 2;
                sumCDigit4 = (pileCDigit4 + totalNimSumDigit4) % 2;

                // Convert these sums into their decimal number, multiply each digit by their corresponding power of 2
                sumADecimal = (8 * sumADigit4) + (4 * sumADigit3) + (2 * sumADigit2) + (1 * sumADigit1);
                sumBDecimal = (8 * sumBDigit4) + (4 * sumBDigit3) + (2 * sumBDigit2) + (1 * sumBDigit1);
                sumCDecimal = (8 * sumCDigit4) + (4 * sumCDigit3) + (2 * sumCDigit2) + (1 * sumCDigit1);

                // If there is only one pile left with at least 2 counters - take all counters or all counters except 1 from the large pile
                if (smallestPileSize < 2 && moderatePileSize < 2) {
                    // Take away the whole pile if both piles have 1 counter
                    if (smallestPileSize == 1 && moderatePileSize == 1) {
                        compCounterChoice = largestPileSize;
                        compPileChoice = largestPile;
                    }
                    // Take away all except 1 counter if both piles have no counter
                    else if (smallestPileSize == 0 && moderatePileSize == 0) {
                        compCounterChoice = largestPileSize - 1;
                        compPileChoice = largestPile;
                    }
                    // Otherwise, 1 pile has 1 counter, and the other pile has 0 counters - take away the whole pile
                    else {
                        compCounterChoice = largestPileSize;
                        compPileChoice = largestPile;
                    }
                    
                    // Pick pile and subtract counters
                    if (compPileChoice.equals("A")) {
                        pileA -= compCounterChoice;
                    }
                    else if (compPileChoice.equals("B")) {
                        pileB -= compCounterChoice;
                    }
                    // Otherwise, the largest pile is C
                    else {
                        pileC -= compCounterChoice;
                    }
                    // Give user the message of what the computer chose
                    System.out.println("The computer chose pile " + compPileChoice + " and removed " + compCounterChoice + " counters");
                }

                // Find the pile who's sum of the pile size + nim sum is less than their pile size. Subtract the nim sum from that pile to make the nim sum 0
                else if (sumADecimal < pileA) {
                    compCounterChoice = pileA - sumADecimal;
                    pileA -= compCounterChoice;
                    // Store pile choice so it can be referenced in the message to the user
                    compPileChoice = "A";
                    
                }
                
                else if (sumBDecimal < pileB) {
                    compCounterChoice = pileB - sumBDecimal;
                    pileB -= compCounterChoice;
                    // Store pile choice so it can be referenced in the message to the user
                    compPileChoice = "B";
                }

                else if (sumCDecimal < pileC) {
                    compCounterChoice = pileC - sumCDecimal;
                    pileC -= compCounterChoice;
                    // Store pile choice so it can be referenced in the message to the user
                    compPileChoice = "C";
                }

                // If none of the pile sizes are less than their nim sums, the current nim sum is 0
                // When facing a nim sum of 0, remove 1 from the largest pile 
                else {
                    if (largestPile.equals("A")) {
                        pileA -= 1;
                        compPileChoice = "A";
                        compCounterChoice = 1;
                    }
                    else if (largestPile.equals("B")) {
                        pileB -= 1;
                        compPileChoice = "B";
                        compCounterChoice = 1;
                    }
                    // Otherwise, the largest pile is C
                    else {
                        pileC -= 1;
                        compPileChoice = "C";
                        compCounterChoice = 1;
                    }
                }

                System.out.println("The computer chose pile " + compPileChoice + " and removed " + compCounterChoice + " counters");
            }
                
            // Otherwise, its the users turn, or the user is playing PvP, so proceed prompting them for a pile and the number of counters removed
            else {
                // Prompt the current player to pick a pile
                System.out.print(currentPlayer + ", choose a pile: ");
                pileChoice = reader.nextLine().toUpperCase();  // The toUpperCase method ensures that lowercase letters are accepted

                // Set invalid to true if user has picked an empty pile
                if (pileChoice.equals("A") || pileChoice.equals("B") || pileChoice.equals("C")) {
                    if (pileChoice.equals("A") && pileAEmpty == true) {
                        invalid = true;
                    }
                    else if (pileChoice.equals("B") && pileBEmpty == true) {
                        invalid = true;
                    }
                    else if (pileChoice.equals("C") && pileCEmpty == true) {
                        invalid = true;
                    }
                    // Otherwise, the user input is valid and nothing needs to be done
                }
                // Otherwise, the user picked the wrong letter and input is invalid
                else {
                    invalid = true;
                }

                // Cheat Prevention: use a while loop to validate if the user entered the letters a, b, c & the pile doesn't have 0 letters
                while (invalid == true) {
                    // Set invalid to false so that if the users input is valid, the if statements are skipped and we can break out of the loop
                    invalid = false;
                    // Set invalid to true if user has picked an empty pile
                    if (pileChoice.equals("A") || pileChoice.equals("B") || pileChoice.equals("C")) {
                        if (pileChoice.equals("A") && pileAEmpty == true) {
                            System.out.print("Nice try, " + currentPlayer + ". That pile is empty. Choose again: ");
                            pileChoice = reader.nextLine().toUpperCase();
                            invalid = true;
                        }
                        else if (pileChoice.equals("B") && pileBEmpty == true) {
                            System.out.print("Nice try, " + currentPlayer + ". That pile is empty. Choose again: ");
                            pileChoice = reader.nextLine().toUpperCase();
                            invalid = true;
                        }
                        else if (pileChoice.equals("C") && pileCEmpty == true) {
                            System.out.print("Nice try, " + currentPlayer + ". That pile is empty. Choose again: ");
                            pileChoice = reader.nextLine().toUpperCase();
                            invalid = true;
                        }
                        // Otherwise, the user input is valid and nothing needs to be done
                    }
                    // Otherwise, the user picked the wrong letter and input is invalid
                    else {
                        System.out.print("Whoops! You didn't choose one of the selected piles! Try again: ");
                        invalid = true;
                        pileChoice = reader.nextLine().toUpperCase();
                    }
                }

                // Prompt player to pick number of counters to remove
                System.out.print("How many do you want to remove from pile " + pileChoice + "? ");
                counterChoice = reader.nextInt();

                // If user chose pile A, then subtract counterChoice from pile A's count
                if (pileChoice.equals("A")) {
                    // If the user picks too many counters or not enough counters, give an error message
                    while (counterChoice > pileA || counterChoice <= 0) {
                        if (counterChoice > pileA) {
                            System.out.print("Pile " + pileChoice + " doesn't have that many. Try again: ");
                        }
                        
                        else {
                            System.out.print("You must choose at least 1. How many? ");
                        }
                        // Prompt the user for reinput
                        counterChoice = reader.nextInt();
                    }
                    // When their input is valid, subtract it from the pile size
                    pileA -= counterChoice;
                }

                // If user chose pile B, then subtract counterChoice from pile B's count
                else if (pileChoice.equals("B")) {
                    // If the user picks too many counters or not enough counters, give an error message and prompt for reinput
                    while (counterChoice > pileB || counterChoice <= 0) {
                        if (counterChoice > pileB) {
                            System.out.print("Pile " + pileChoice + " doesn't have that many. Try again: ");
                        }

                        else {
                            System.out.print("You must choose at least 1. How many? ");
                        }
                        counterChoice = reader.nextInt();
                    }
                    // When their input is valid, subtract it from the pile size
                    pileB -= counterChoice;
                }

                // Otherwise, subtract counterChoice from pile C's count
                else {
                    // If the user picks too many counters or not enough counters, give an error message and prompt for reinput
                    while (counterChoice > pileC || counterChoice <= 0) {
                        if (counterChoice > pileC) {
                            System.out.print("Pile " + pileChoice + " doesn't have that many. Try again: ");
                        }

                        else {
                            System.out.print("You must choose at least 1. How many? ");
                        }
                        counterChoice = reader.nextInt();
                    }
                    // When their input is valid, subtract it from the pile size
                    pileC -= counterChoice;
                }

                // Empty input prompt, ensures that the "enter" button from previous prompt is not used in the next user prompt
                reader.nextLine();
            }

            // Output the current amount of counters in each pile
            System.out.print("A: ");
            for (int i = 0; i < pileA; i++) {
                System.out.print("*");
            }
            // Skip to new line for the next character
            System.out.println();

            System.out.print("B: ");
            for (int i = 0; i < pileB; i++) {
                System.out.print("*");
            }
            // Skip to new line for the next character
            System.out.println();

            System.out.print("C: ");
            for (int i = 0; i < pileC; i++) {
                System.out.print("*");
            }

            // Skip to new line for the next character
            System.out.println();   
            
            // If the sum is 0, then the current player has picked the last counters available. Hence, that player lost
            if (pileA + pileB + pileC == 0) {
                winner = waitingPlayer;
                loser = currentPlayer;
                // Print winner message
                System.out.println(winner + ", there are no counters left, so you WIN! Sorry " + loser + ", better luck next time!");
                gameWon = true;
            }

            // Switch user turn using the swap variable - if current player is player 1, switch to player 2
            playerSwap = currentPlayer;
            currentPlayer = waitingPlayer;
            waitingPlayer = playerSwap;

            // Check if the sum of counters in the pile is 1 - if it is, the game is won, and the player with the current turn has lost (DIGNITY)
            if (pileA + pileB + pileC == 1) {
                winner = waitingPlayer;
                loser = currentPlayer;
                // Give a more "automated" message if the user won against the computer
                if (loser.equals("Computer")) {
                    System.out.println("Wow!! " + winner + ", you have beaten the computer!!");
                    gameWon = true;
                }
                // Print winner message
                else {
                    System.out.println(loser + ", you must take the last remaining counter, so you lose. " + winner + " wins!");
                    gameWon = true;
                }
            }

            // Set the 3 boolean-pile variables to true if they have 0 counters
            if (pileA == 0) {
                pileAEmpty = true;
            }
            if (pileB == 0) {
                pileBEmpty = true;
            }
            if (pileC == 0) {
                pileCEmpty = true;
            }
        }
        reader.close();
    }
}
