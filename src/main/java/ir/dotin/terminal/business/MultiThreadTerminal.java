package ir.dotin.terminal.business;

public class MultiThreadTerminal {

    public static void main(String[] args) {
        TerminalHandler terminalHandler = new TerminalHandler();
        TerminalInfo.fromXML();
        new Thread(terminalHandler).start();
    }

}
