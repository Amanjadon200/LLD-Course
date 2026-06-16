package behaviourdesignpattern;

import java.util.Stack;
/*
when should we use command design pattern?
1. when you want to decouple the sender and receiver of a request.
2. when you want to implement undo/redo functionality.
3. when you want to implement a queue of requests.
4. when you want to implement logging of requests.
    logging of requests means that you want to keep a record of all the commands that have been executed. This can be useful for debugging purposes or for auditing purposes. You can log the command name, the parameters passed to the command, and the result of the command execution.
5. when you want to implement transactions.
    transactions means that you want to execute a set of commands and if any command fails then you want to undo all the previous commands. This can be useful for ensuring data integrity and consistency. For example, if you are transferring money from one account to another, you want to ensure that both the debit and credit operations are successful. If the debit operation fails, you want to undo the credit operation as well.
6 remote controls, GUI buttons, menu items, etc. can be implemented using command design pattern. The command design pattern allows you to encapsulate a request as an object, thereby allowing you to parameterize clients with different requests, queue or log requests, and support undoable operations.
*/
interface Command {
    void execute();

    void undo();
}
public class commandDemo { // client here you will call the commands
    public static void main(String[] args) {
        try {
            
            Clipboard clipboard=new Clipboard();
            TextEditor textEditor=new TextEditor();
            Command writeCommand=new WriteCommand(textEditor,"aman jadon");
            Command writeCommand1=new WriteCommand(textEditor,"is a greatest coder");
            Invoker invoker=new Invoker();
            invoker.executeCommand(writeCommand);
            invoker.executeCommand(writeCommand1);
            Command selectCommand=new SelectTextCommand(textEditor,2,5);
            invoker.executeCommand(selectCommand);
            Command copyCommand=new CopyCommand(textEditor,clipboard);
            invoker.executeCommand(copyCommand);
            Command pasteCommand=new PasteCommand(textEditor,clipboard);
            invoker.executeCommand(pasteCommand);
                
        System.out.println("total text : " +textEditor.getData()+ " select content : "+textEditor.getSelectedText()+ " copied content : "+clipboard.getData() );

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e);
        }

    }
}

class TextEditor { // Receiver
    private StringBuilder data;
    private String selectedText;
    public TextEditor() {
        this.data = new StringBuilder();
        this.selectedText = "";
    }
    public void write(String text) {
        data.append(text);
    }
    public String getData() {
        return data.toString();
    }
    public void paste(String text) {
        data.append(text);
    }

    public void selectText(int si,int ei) {
        this.selectedText= data.substring(si,ei+1);
    }

    public String getSelectedText() {
        return selectedText;
    }
    public void removeLast(int length){
         if (length > data.length()) {
            length = data.length();
        }

        data.delete(
            data.length() - length,
            data.length()
        );
    }
}

class Invoker {
    Stack<Command> historyCommands=new Stack<>();
    public void executeCommand(Command command){
        command.execute();
        // if()
        historyCommands.push(command);
    }
    public void undoCommand(){
        Command command=historyCommands.pop();
        command.undo();
    }
}


class WriteCommand implements Command {
    TextEditor textEditor;
    String text;

    public WriteCommand(TextEditor textEditor, String text) {
        this.textEditor = textEditor;
        this.text = text;
    }

    public void execute() {
        textEditor.write(text);
    }

    public void undo() {
        textEditor.removeLast(this.text.length());
    }
}

class PasteCommand implements Command {
    TextEditor textEditor;
    String pastedText;
    Clipboard clipboard;
    public PasteCommand(TextEditor textEditor, Clipboard clipboard) {
        this.textEditor = textEditor;
        this.clipboard = clipboard;
    }

    public void execute() {
        pastedText=clipboard.getData();
        textEditor.paste(pastedText);
    }

    public void undo() {
        textEditor.removeLast(pastedText.length());
    }
}


class CopyCommand implements Command {
    TextEditor textEditor;
    Clipboard clipboard;
    public CopyCommand(TextEditor textEditor, Clipboard clipboard) {
        this.textEditor = textEditor;
        this.clipboard = clipboard;
    }
    
    public void execute() {
        clipboard.setData(textEditor.getSelectedText());
    }
    
    public void undo() {
        // textEditor.undo();
        throw new Error("can't undo for copy");
    }
}
class SelectTextCommand implements Command {

    private TextEditor editor;
    private int start;
    private int end;

    public SelectTextCommand(TextEditor editor, int start, int end) {
        this.editor = editor;
        this.start = start;
        this.end = end;
    }

    @Override
    public void execute() {
        editor.selectText(start, end);
    }

    @Override
    public void undo() {
        // restore previous selection if desired
        throw new Error("can't undo for select");
    }
}
class Clipboard{
    private String data;
    public String getData(){
        return data;
    }
    public void setData(String data){
        this.data=data;
    }
}