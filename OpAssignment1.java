/*
5i1
Danah Aldhawi 444009172
Raghad Alduaij 444009150
 */
package opassignment1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class OpAssignment1 {

   private static int nextReplaceIndex = 0;
   
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

         System.out.print("Choose an option to run: \n1- Page Replacement \n2- Disk Scheduling"); 
         System.out.print("\nEnter your option: "); 
         int choice = scanner.nextInt();
         
         switch(choice){
            //Page Replacement 
             case 1:
        System.out.print("Enter the number of frames: ");
        int numOfFrames = scanner.nextInt();

       
        int[] pages = new int[20];
        System.out.println("Enter the 20 page in string format separate by space: ");
        for (int i = 0; i < 20; i++) {
            pages[i] = scanner.nextInt();
        }

        
        System.out.println("\nRunning FIFO Algorithm:");
        runningAlgorithm(pages, numOfFrames, "FIFO");

        System.out.println("\nRunning LRU Algorithm:");
        runningAlgorithm(pages, numOfFrames, "LRU");

        System.out.println("\nRunning Optimal Algorithm:");
        runningAlgorithm(pages, numOfFrames, "Optimal");
        break;
        
        //Disk Scheduling
             case 2:
                int[] requestQueue = new int[15];
        System.out.println("Enter the 15 pending requests separate by space: ");
        for (int i = 0; i < 15; i++) {
            requestQueue[i] = scanner.nextInt();
        }

        
        System.out.print("Enter the total number of cylinders: ");
        int totalCylinders = scanner.nextInt();

        
        System.out.print("Enter the current head position: ");
        int headPosition = scanner.nextInt();

        
        System.out.print("Enter the head direction (left/right): ");
        String headDirection = scanner.next().toLowerCase();

        
        System.out.println("\nRunning FCFS Algorithm:");
        runFCFS(requestQueue, headPosition);

        System.out.println("\nRunning SSTF Algorithm:");
        runSSTF(requestQueue, headPosition);

        System.out.println("\nRunning SCAN Algorithm:");
        runSCAN(requestQueue, headPosition, headDirection, totalCylinders);
        break;
    }
    }

    //Page Replacement methods
    public static void runningAlgorithm(int[] pages, int numOfFrames, String algorithm) {
        ArrayList<Integer> frames = new ArrayList<>();
        int pageFaultsWithReplacement = 0;
        int pageFaultsWithoutReplacement = 0;
        int totalPageFaults = 0;

        for (int i = 0; i < pages.length; i++) {
            int page = pages[i];

            if (!frames.contains(page)) {
                
                if (frames.size() < numOfFrames) {
                    frames.add(page);
                    pageFaultsWithoutReplacement++;
                    System.out.println("Page fault without replacement: " + frames);
                } else {
                   
                    if (algorithm.equals("FIFO")) {
                        frames.set(nextReplaceIndex, page);
                        nextReplaceIndex = (nextReplaceIndex + 1) % numOfFrames; 
                    } else if (algorithm.equals("LRU")) {
                        int replaceIndex = findLRUReplaceIndex(frames, pages, i);
                        frames.set(replaceIndex, page);
                        
                    } else if (algorithm.equals("Optimal")) {
                        int replaceIndex = findOptimalReplaceIndex(pages, frames, i);
                        frames.set(replaceIndex, page);
                    }

                    pageFaultsWithReplacement++;
                    System.out.println("Page fault with replacement: " + frames);
                }
                totalPageFaults++;
            } else {

                System.out.println("No page fault: " + frames);
            }
        }

      
        System.out.println("\nResults for " + algorithm + ":");
        System.out.println("Page faults without replacement: " + pageFaultsWithoutReplacement);
        System.out.println("Page faults with replacement: " + pageFaultsWithReplacement);
        System.out.println("Total page faults: " + totalPageFaults);
    }

   
    public static int findOptimalReplaceIndex(int[] pages, ArrayList<Integer> frames, int currentIndex) {
        int farthestIndex = -1;
        int replaceIndex = 0;

        for (int j = 0; j < frames.size(); j++) {
            int page = frames.get(j);
            int nextUse = findNextUseOfPage(pages, currentIndex + 1, page);

            if (nextUse > farthestIndex) {
                farthestIndex = nextUse;
                replaceIndex = j;
            }
        }
        return replaceIndex;
    }

  
    public static int findNextUseOfPage(int[] pages, int startIndex, int page) {
        for (int i = startIndex; i < pages.length; i++) {
            if (pages[i] == page) {
                return i;
            }
        }
        return pages.length + 1; 
    }

 
    public static int findLRUReplaceIndex(ArrayList<Integer> frames, int[] pages, int currentIndex) {
        int leastRecentIndex = Integer.MAX_VALUE;
        int replaceIndex = 0;

        for (int j = 0; j < frames.size(); j++) {
            int page = frames.get(j);
            int lastUsed = findLastUseOfPage(pages, currentIndex - 1, page);

            if (lastUsed < leastRecentIndex) {
                leastRecentIndex = lastUsed;
                replaceIndex = j;
            }
        }
        return replaceIndex;
    }


    public static int findLastUseOfPage(int[] pages, int endIndex, int page) {
        for (int i = endIndex; i >= 0; i--) {
            if (pages[i] == page) {
                return i;
            }
        }
        return -1; 
    }

    //Disk Scheduling methods
    public static void runFCFS(int[] queue, int headPosition) {
    int totalSeekDistance = 0;
    int currentPosition = headPosition;

    System.out.print("Head movement sequence: " + currentPosition);
    
    
    for (int request : queue) {
        if (request != headPosition) {
            totalSeekDistance += Math.abs(request - currentPosition);
            currentPosition = request;
            System.out.print(" -> " + currentPosition);
        }
    }

    System.out.println("\nTotal seek distance: " + totalSeekDistance);
}


    
    public static void runSSTF(int[] queue, int headPosition) {
        int totalSeekDistance = 0;
        int currentPosition = headPosition;
        ArrayList<Integer> requests = new ArrayList<>();
        for (int request : queue) {
            if (request != currentPosition) {  
                requests.add(request);
            }
        }

        System.out.print("Head movement sequence: " + currentPosition);
        while (!requests.isEmpty()) {
            int closestRequest = findClosestRequest(requests, currentPosition);
            totalSeekDistance += Math.abs(closestRequest - currentPosition);
            currentPosition = closestRequest;
            requests.remove(Integer.valueOf(closestRequest));
            System.out.print(" -> " + currentPosition);
        }

        System.out.println("\nTotal seek distance: " + totalSeekDistance);
    }

   
    public static int findClosestRequest(ArrayList<Integer> requests, int currentPosition) {
        int minDistance = Integer.MAX_VALUE;
        int closestRequest = -1;

        for (int request : requests) {
            int distance = Math.abs(request - currentPosition);
            if (distance < minDistance) {
                minDistance = distance;
                closestRequest = request;
            }
        }

        return closestRequest;
    }

   
    public static void runSCAN(int[] queue, int headPosition, String headDirection, int totalCylinders) {
        int totalSeekDistance = 0;
        int currentPosition = headPosition;
        ArrayList<Integer> leftRequests = new ArrayList<>();
        ArrayList<Integer> rightRequests = new ArrayList<>();

      
        for (int request : queue) {
            if (request < currentPosition) {
                leftRequests.add(request);
            } else if (request > currentPosition) { 
                rightRequests.add(request);
            }
        }

       
        Collections.sort(leftRequests);
        Collections.sort(rightRequests);

        System.out.print("Head movement sequence: " + currentPosition);

      
        if (headDirection.equals("left")) {
          
            for (int i = leftRequests.size() - 1; i >= 0; i--) {
                int request = leftRequests.get(i);
                totalSeekDistance += Math.abs(currentPosition - request);
                currentPosition = request;
                System.out.print(" -> " + currentPosition);
            }
         
            totalSeekDistance += currentPosition;
            currentPosition = 0;
            System.out.print(" -> " + currentPosition);

            for (int request : rightRequests) {
                totalSeekDistance += Math.abs(currentPosition - request);
                currentPosition = request;
                System.out.print(" -> " + currentPosition);
            }
        } else { 
            for (int request : rightRequests) {
                totalSeekDistance += Math.abs(currentPosition - request);
                currentPosition = request;
                System.out.print(" -> " + currentPosition);
            }
          
            totalSeekDistance += Math.abs(currentPosition - (totalCylinders - 1));
            currentPosition = totalCylinders - 1;
            System.out.print(" -> " + currentPosition);

            for (int i = leftRequests.size() - 1; i >= 0; i--) {
                int request = leftRequests.get(i);
                totalSeekDistance += Math.abs(currentPosition - request);
                currentPosition = request;
                System.out.print(" -> " + currentPosition);
            }
        }

        System.out.println("\nTotal seek distance: " + totalSeekDistance);
    }
}

