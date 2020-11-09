package buscamines;
/*
Joc del Buscamines:

Heu d'implementar el joc del buscamines on l'usuari entrarà un nombre enter per terminal
i aleshores el programa crearà un tauler nxn de tipus char amb n bombes col·locades aleatòriament.

L'usuari ha de, com a mínim, poder efectuar mitjançant un menu, les següents operacions durant una partida:
1. Fer una aposta d'on es troba una bomba. Per fer-ho, es demanarà per pantalla dos nombres enters, fila i columna.
2. Descobrir el contingut d'una cel·la on, 
    Si seleccionem una cel·la amb una bomba, perderem la partida i finalitzarem el programa.
    Si no conté cap bomba, el programa calcularà quantes bombes hi ha en les 8 cel·les immediatament contigües (les 4 a en vertical i horitzontal, i les 4 en diagonal)
3. Imprimir per pantalla l'estat actual del joc.
    S'imprimirà un '?' si no sabem que hi ha en una cel·la.
    S'imprimirà una 'B' si hem seleccionat aquella cel·la com a cel·la amb bomba.
    S'imprimirà un int de 0 a 8 si hem descobert aquella cel·la (i no conté cap bomba) indicant el nombre de bombes que té al seu costat.
4. Resoldre la partida. Això és, comprovar si hem col·locat les 'B' sobre les cel·les amb bomba i no ens hem deixat cap.
    Si s'ha realitzat tot correctament apareixerà un missatge indicant que hem guanyat la partida.
    Si ens hem deixat alguna bomba sense marcar, apareixerà un missatge indicant que hem perdut la partida, i ens mostrarà on estaven les bombes.
    En ambdós casos, un cop realitzada aquesta opció, finalitzarem el programa correctament.
5. Sortir de la partida sense resoldre-la.
*/


import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

public class BuscaMines {
   
    enum MenuOption{
    BOMB, DISCOVER, PRINT, RESOLVE, EXIT;}
    
    
    public static void main(String[] args){
        Scanner sc;
        MenuOption option;
        char[][] game;
        char[][] bombs;
        int n, opt;
        sc = new Scanner(System.in);
        System.out.println("Board dimension (n): ");
        n = sc.nextInt();
        while (n <= 0){
            System.out.println("Dimension cannot be negative or zero. Try again");
            System.out.println("Input for dimension n: ");
            n = sc.nextInt();
        }
        
        game = createLayout(n, '?');
        bombs = createLayout(n , '?');
        placeBombs(bombs, n, n);
        System.out.println("The board: \n" + printLayout(bombs, n));
        System.out.println("The board: \n" + printLayout(game, n));
        System.out.println("There are " + n + " bombs, FIND THEM!!!");
        printMenu();
        opt = sc.nextInt();
        option = MenuOption.values()[opt-1];
        while (option != MenuOption.EXIT){
            switch (option){
                case BOMB:
                    betBomb(game,n,sc);
                    break;
                case DISCOVER:
                    if(discoverCellAndCheckNeighborhood(game,bombs,n,sc) == 1)
                        System.exit(0);
                    break;
                case PRINT:
                    System.out.println(printLayout(game, n));
                    break;
                case RESOLVE:{
                    resolve(game,bombs,n);
                    System.exit(0);
                    break;
                }
               case EXIT:
                   System.out.println("Bye! :)");
                   break;
                default:
                    System.out.println("Not a valid option.");
            }
            printMenu();
            opt = sc.nextInt();
            option = MenuOption.values()[opt-1];
        }
    }
    
    static void printMenu(){
        System.out.println();
        System.out.println("Welcome to Minesweeper!");
        System.out.println("1. Bet a bomb (B).");
        System.out.println("2. Discover a cell.");
        System.out.println("3. Print board.");
        System.out.println("4. Resolve Game.");
        System.out.println("5. Exit.");
        System.out.println("Choose an option:");
    }


    //inma: método que se les puede pedir en la práctica, solo se le da la cabecera del método
    static int discoverCellAndCheckNeighborhood(char[][] layout, char[][] bombs, int n, Scanner sc){
        System.out.println("Selecciona a quina cel·la on vols descobrir el contingut(fila,columna):");
        int fila = sc.nextInt();
        int columna = sc.nextInt();
        int bombs_counter = 0;
        int [] array = checkIndexes(fila,columna,n,sc);
        
        if(array != null){
            fila = array[0];
            columna = array[1];
        }

        if(bombs[fila][columna] == 'X'){
            System.out.println("Ha explotat una mina! GAME OVER");
            System.out.println(printLayout(bombs,n));
            return 1;
        }
        else{
            
            if((fila == 0 || fila == n-1) || (columna == 0 || columna == n-1) ){
                
                //Cas fila de dalt sense importar columnes
                if (fila == 0 && (columna != 0 && columna != n-1)){
                    for(int i = fila; i <= fila+1; i++){
                        for(int j = columna-1; j <= columna+1; j++){
                            if (i == fila && j == columna)
                                bombs_counter += 0;
                            if(bombs[i][j] == 'X'){
                                bombs_counter += 1;
                            }
                        }
                    }
                }
                //Cas columna esquerra sense importar files
                else if((fila != 0 && fila != n-1) && columna == 0){
                    for(int i = fila-1; i <= fila+1; i++){
                        for(int j = columna; j <= columna+1; j++){
                            if (i == fila && j == columna)
                                bombs_counter += 0;
                            if(bombs[i][j] == 'X'){
                                bombs_counter += 1;
                            }
                        }
                    }
                }
                //Cas columna dreta sense importar files
                if((fila != 0 && fila != n-1) && (columna == n-1)){
                    for(int i = fila-1; i <= fila+1; i++){
                        for(int j = columna-1; j <= columna; j++){
                            if (i == fila && j == columna)
                                bombs_counter += 0;
                            if(bombs[i][j] == 'X'){
                                bombs_counter += 1;
                            }
                        }
                    }
                }
                //Cas còrner esquerre
                else if (fila == 0 && columna == 0){
                    for(int i = fila; i <= fila+1; i++){
                        for(int j = columna; j <= columna+1; j++){
                            if (i == fila && j == columna)
                                bombs_counter += 0;
                            if(bombs[i][j] == 'X'){
                                bombs_counter += 1;
                            }
                        }
                    }
                }
                //Cas còrner dret
                else if (fila == 0 && columna == n-1){
                    for(int i = fila; i <= fila+1; i++){
                        for(int j = columna-1; j <= columna; j++){
                            if (i == fila && j == columna)
                                bombs_counter += 0;
                            if(bombs[i][j] == 'X'){
                                bombs_counter += 1;
                            }
                        }
                    }
                }
                
                //Comprovant els casos de la fila d'abaix
                else if ((fila == n-1) && (columna != 0 && columna != n-1)){
                    for(int i = fila-1; i <= fila; i++){
                        for(int j = columna-1; j <= columna+1; j++){
                            if (i == fila && j == columna)
                                bombs_counter += 0;
                            if(bombs[i][j] == 'X'){
                                bombs_counter += 1;
                            }
                        }
                    }
                }
                //Cas còrner esquerre
                else if (fila == n-1 && columna == 0){
                    for(int i = fila-1; i <= fila; i++){
                        for(int j = columna; j <= columna+1; j++){
                            if (i == fila && j == columna)
                                bombs_counter += 0;
                            if(bombs[i][j] == 'X'){
                                bombs_counter += 1;
                            }
                        }
                    }
                }
                //Cas còrner dret
                else if (fila == n-1 && columna == n-1){
                    for(int i = fila-1; i <= fila; i++){
                        for(int j = columna-1; j <= columna; j++){
                            if (i == fila && j == columna)
                                bombs_counter += 0;
                            if(bombs[i][j] == 'X'){
                                bombs_counter += 1;
                            }
                        }
                    }
                }
            }
            //Cas normal
            else{
                for(int i = fila-1; i <= fila+1; i++){
                    for(int j = columna-1; j <= columna+1; j++){
                        if (i == fila && j == columna)
                            bombs_counter += 0;
                        if(bombs[i][j] == 'X'){
                            bombs_counter += 1;
                        }
                    }
                }
            }
            
            layout[fila][columna] = (char) Character.forDigit(bombs_counter, 10);
            return 0;
        }
    }
    
    

    //inma: método que se les puede pedir en la práctica, solo se le da la cabecera del método
    static void resolve(char[][] layout, char[][] bombs, int n){
        boolean totes_trobades = true;
        int i = 0;
        int j;
        int numBombs = 0;

        while (i < n && numBombs < n && totes_trobades){
            j = 0;
            while (j < n && numBombs < n && totes_trobades){
                if (bombs[i][j] == 'X' && layout[i][j] == 'B'){
                    numBombs++;
                    j++;
                }else if (bombs[i][j] != 'X')
                    j++;
                else
                    totes_trobades = false;
            }
            i++;
        }
        
        if (totes_trobades)
            System.out.println("Has guanyat la partida!");
        else{
            System.out.println("Has perdut la partida!");
            System.out.println(printLayout(bombs,n));
        }
    }
    
    

    //inma: método que se les puede pedir en la práctica, solo se le da la cabecera del método
    static void betBomb(char[][] layout, int n, Scanner sc){
        System.out.println("Digues on vols apostar que hi ha una bomba(fila,columna):");
        int fila,columna;
        fila = sc.nextInt();
        columna = sc.nextInt();
        int [] array = checkIndexes(fila,columna,n,sc);
        
        if(array != null){
            fila = array[0];
            columna = array[1];
        }
        
        layout[fila][columna] = 'B';
    }

    
    
    //inma: método que se les puede pedir en la práctica, solo se le da la cabecera del método y se explica qué tiene que hacer
    static char[][] createLayout(int n, char c){
        char[][] layout = new char[n][n];
        for (int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                layout[i][j] = c;
            }
        }
        return layout;
    }


    
    static void placeBombs(char[][] layout, int n, int numBombs){
        int bombPositioni,bombPositionj;
        for(int i = 0; i < n; i++){
            bombPositioni = ThreadLocalRandom.current().nextInt(0,n);
            bombPositionj = ThreadLocalRandom.current().nextInt(0,n);
            while(layout[bombPositioni][bombPositionj] == 'X'){
                bombPositioni = ThreadLocalRandom.current().nextInt(0,n);
                bombPositionj = ThreadLocalRandom.current().nextInt(0,n);
            }
            layout[bombPositioni][bombPositionj] = 'X';
        }
    }



    //inma: método que se les puede pedir en la práctica, solo se le da la cabecera del método
    static String printLayout(char[][] layout, int n){
        int i, j;
        String output = "  | ";
        for (i = 0; i < n; i++){
            output += i + " ";
        }
        output += '\n' + "  ";
        for (i = 0; i <= n; i++){
            output += "- ";
        }
        output += '\n';
        for (i = 0; i < n; i++){
            output += i + " | ";
            for (j = 0; j < n; j++){
                output += layout[i][j] + " ";
            }
            output += '\n';
        }
        return output;
    }
    
    
    
    //inma: método que se les puede pedir en la práctica, solo se le da la cabecera del método
    static int [] checkIndexes(int i, int j, int n, Scanner sc){
        int [] array = new int[2];
        int fila = i;
        int columna = j;
        if ((fila < 0 || fila > n-1) || (columna < 0 || columna > n-1)){
            while((fila < 0 || fila > n-1) || (columna < 0 || columna > n-1)){
                System.out.println("Torna a introduir una fila,columna dins la dimensió del tauler:");
                fila = sc.nextInt();
                columna = sc.nextInt();
            }
            array[0] = fila;
            array[1] = columna;
            return array;
        }
        else{
            array[0] = fila;
            array[1] = columna;
            return array;
        }
    }
}