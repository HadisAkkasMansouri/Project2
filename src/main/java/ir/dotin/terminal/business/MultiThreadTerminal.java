package ir.dotin.terminal.business;

public class MultiThreadTerminal {

    private final int threadNumbers;

    public MultiThreadTerminal(int threadNumbers) {
        this.threadNumbers = threadNumbers;
    }

    public static void main(String[] args) {
        TerminalHandler terminalHandler = new TerminalHandler();
        TerminalInfo.fromXML();
        new Thread(terminalHandler).start();
    }

}
