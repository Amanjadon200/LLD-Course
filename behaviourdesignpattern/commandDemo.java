package behaviourdesignpattern;

import java.util.Stack;

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