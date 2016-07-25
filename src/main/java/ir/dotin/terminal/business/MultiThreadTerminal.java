package ir.dotin.terminal.business;

public class MultiThreadTerminal {

    public static void main(String[] args) {
        TerminalHandler terminalHandler = new TerminalHandler();
        terminalHandler.readXMLFile();
        new Thread(terminalHandler).start();
    }

}
