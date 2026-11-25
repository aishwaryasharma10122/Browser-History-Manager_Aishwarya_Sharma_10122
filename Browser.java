import java.util.Scanner;
import java.util.Stack;

public class Browser  {

    // ---------- Node of the Doubly Linked List ----------
    // Each node stores one visited web page
    static class PageNode {
        String url;      // website address
        PageNode prev;   // link to the previous page
        PageNode next;   // link to the next page

        PageNode(String url) {
            this.url = url;
            this.prev = null;
            this.next = null;
        }
    }

    // ---------- Global variables ----------
    static PageNode head = null;     // first page in the history list
    static PageNode current = null;  // page that is open right now

    static Stack<String> backStack = new Stack<>();     // back history
    static Stack<String> forwardStack = new Stack<>();  // forward history

    static Scanner sc = new Scanner(System.in);

    // ---------- main method ----------
    public static void main(String[] args) {
        System.out.println("Welcome to Browser History Manager!");
        System.out.println();

        int choice = 0;
        while (choice != 8) {
            showMenu();
            choice = readChoice();
            System.out.println();

            switch (choice) {
                case 1: visitNewWebsite(); break;
                case 2: goBack(); break;
                case 3: goForward(); break;
                case 4: displayCurrent(); break;
                case 5: displayHistory(); break;
                case 6: searchWebsite(); break;
                case 7: clearHistory(); break;
                case 8: System.out.println("Thank you for using Browser History Manager. Goodbye!"); break;
                default: System.out.println("Invalid choice! Please enter a number from 1 to 8.");
            }
            System.out.println();
        }
        sc.close();
    }

    // Shows the menu
    static void showMenu() {
        System.out.println("===== BROWSER HISTORY MANAGER =====");
        System.out.println();
        System.out.println("1. Visit New Website");
        System.out.println("2. Go Back");
        System.out.println("3. Go Forward");
        System.out.println("4. Current Website");
        System.out.println("5. Display History");
        System.out.println("6. Search Website");
        System.out.println("7. Clear History");
        System.out.println("8. Exit");
        System.out.println();
        System.out.print("Enter your choice: ");
    }

    // Reads the menu choice. Returns -1 if the user types something that is not a number
    static int readChoice() {
        String input = sc.nextLine().trim();
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Option 1: Visit a new website
    static void visitNewWebsite() {
        System.out.print("Enter website address (e.g. google.com): ");
        String url = sc.nextLine().trim();

        if (url.isEmpty()) {
            System.out.println("Website name cannot be empty.");
            return;
        }

        // Avoid duplicate entry: do not reopen the same page
        if (current != null && current.url.equalsIgnoreCase(url)) {
            System.out.println("You are already on this website.");
            return;
        }

        PageNode newNode = new PageNode(url);

        if (current == null) {
            // This is the very first website of the session
            head = newNode;
        } else {
            // Save the current page in back history
            backStack.push(current.url);

            // Link the new page after the current page.
            // This also removes the old forward pages from the list.
            newNode.prev = current;
            current.next = newNode;
        }

        // A new visit clears the forward history
        forwardStack.clear();

        current = newNode;
        System.out.println("Opened new website: " + url);
    }

    // Option 2: Go back to the previous website
    static void goBack() {
        if (backStack.isEmpty()) {
            System.out.println("No previous website available. You are at the first page.");
            return;
        }

        // The page we are leaving goes to the forward stack
        forwardStack.push(current.url);
        backStack.pop();

        // Move the pointer to the previous node in the list
        current = current.prev;
        System.out.println("Went back to: " + current.url);
    }

    // Option 3: Go forward to the next website
    static void goForward() {
        if (forwardStack.isEmpty()) {
            System.out.println("No next website available. You are at the latest page.");
            return;
        }

        // The page we are leaving goes to the back stack
        backStack.push(current.url);
        forwardStack.pop();

        // Move the pointer to the next node in the list
        current = current.next;
        System.out.println("Went forward to: " + current.url);
    }

    // Option 4: Show the current website
    static void displayCurrent() {
        if (current == null) {
            System.out.println("No website opened yet.");
        } else {
            System.out.println("Current website: " + current.url);
        }
    }

    // Option 5: Show the complete history (traversal of the linked list)
    static void displayHistory() {
        if (head == null) {
            System.out.println("History is empty. No websites visited yet.");
            return;
        }

        System.out.println("----- Browsing History -----");
        PageNode temp = head;
        int count = 1;

        // Walk from the first page to the last page
        while (temp != null) {
            String marker = "";
            if (temp == current) {
                marker = "   <-- current";
            }
            System.out.println(count + ". " + temp.url + marker);
            temp = temp.next;
            count++;
        }
    }

    // Option 6: Search for a website in the history
    static void searchWebsite() {
        System.out.print("Enter website name to search: ");
        String keyword = sc.nextLine().trim().toLowerCase();

        if (keyword.isEmpty()) {
            System.out.println("Please enter something to search.");
            return;
        }

        PageNode temp = head;
        int position = 1;
        boolean found = false;

        // Linear search from the first page to the last page
        while (temp != null) {
            if (temp.url.toLowerCase().contains(keyword)) {
                System.out.println("Found at position " + position + ": " + temp.url);
                found = true;
            }
            temp = temp.next;
            position++;
        }

        if (!found) {
            System.out.println("No matching website found in history.");
        }
    }

    // Option 7: Clear the whole browser history
    static void clearHistory() {
        head = null;
        current = null;
        backStack.clear();
        forwardStack.clear();
        System.out.println("Browser history cleared successfully.");
    }
}