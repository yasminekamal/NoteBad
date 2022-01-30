/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package notepad;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Optional;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.IndexRange;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author yassm
 */
public class NotePad extends Application {

    MenuBar bar;

    Menu file;
    Menu edit;
    Menu help;

    MenuItem fNew;
    MenuItem fOpen;
    MenuItem fSave;
    MenuItem fExit;

    MenuItem eUndo;
    MenuItem eCut;
    MenuItem eCopy;
    MenuItem ePaste;
    MenuItem eDelete;
    MenuItem eSelectAll;

    MenuItem hAbout;
    TextArea txt;
    SeparatorMenuItem sep1, sep2, sep3;
    BorderPane pane;
    Scene s;
    Dialog<String> hdialog;
    ButtonType type;
    
    FileChooser fileChooser;
    FileReader fr;
    BufferedReader br;
    FileWriter fw;
    BufferedWriter bw;
    ButtonType save;
    ButtonType dontSave;
    ButtonType cancel;
    boolean isSaved;
    String fileName ;
    String path ;
    String oldTxt;
    String newTxt ;
    Dialog<ButtonType> dialog2;

    public void init() {
        bar = new MenuBar();    // menu bar
        // menus to be set in menu bar
        file = new Menu("File");
        edit = new Menu("Edit");
        help = new Menu("Help");
        // items in menu file
        fNew = new MenuItem("New");
        fOpen = new MenuItem("Open");
        fSave = new MenuItem("Save");
        fExit = new MenuItem("Exit");
        // items in menu edit
        eUndo = new MenuItem("Undo");
        eCut = new MenuItem("Cut");
        eCopy = new MenuItem("Copy");
        ePaste = new MenuItem("Paste");
        eDelete = new MenuItem("Delete");
        eSelectAll = new MenuItem("Select All");
        hAbout = new MenuItem("About");
        //textarea
        txt = new TextArea();
        //initialize
        fileName = "Untitled";
        path = "";
        oldTxt = "";
        newTxt = "";
        isSaved = true;
        //borderpane
        pane = new BorderPane();
        //scene
        s = new Scene(pane, 300, 400);

        //seperator in menu
        sep1 = new SeparatorMenuItem();
        sep2 = new SeparatorMenuItem();
        sep3 = new SeparatorMenuItem();
        // file chooser
        fileChooser = new FileChooser();
        save = new ButtonType("Save");
        dontSave = new ButtonType("Don't Save");
        cancel = new ButtonType("Cancel");
        //shortcuts for menu items
        fNew.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        fOpen.setAccelerator(KeyCombination.keyCombination("Ctrl+O"));
        fSave.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        fExit.setAccelerator(KeyCombination.keyCombination("Alt+Shift+F4"));
        eUndo.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+Z"));
        eCut.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+X"));
        eCopy.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+C"));
        ePaste.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+V"));
        eDelete.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+D"));
        eSelectAll.setAccelerator(KeyCombination.keyCombination("Ctrl+Shift+A"));
        hAbout.setAccelerator(KeyCombination.keyCombination("Ctrl+F1"));

    }

    @Override
    public void start(Stage primaryStage) {

        file.getItems().addAll(fNew, fOpen, fSave, sep1, fExit);
        edit.getItems().addAll(eUndo, sep2, eCut, eCopy, ePaste, eDelete, sep3, eSelectAll);
        help.getItems().addAll(hAbout);
        bar.getMenus().addAll(file, edit, help);
        pane.setTop(bar);
        pane.setCenter(txt);
        primaryStage.setScene(s);
        primaryStage.show();

        /// handling edit menu
        eUndo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (txt.isUndoable()) {
                    txt.undo();         // undo
                }
            }
        });
        eCut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                txt.cut();              // cut
            }
        });
        eCopy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                txt.copy();             // copy
            }
        });
        ePaste.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                txt.paste();            // paste
            }
        });
        eDelete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                IndexRange r = txt.getSelection();
                txt.deleteText(r);      // delete

            }
        });
        eSelectAll.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                txt.selectAll();        // select all
            }
        });

        hAbout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //dialog
                //Creating a dialog
                hdialog = new Dialog<String>();
                //Setting the title
                hdialog.setTitle("NotePad");
                type = new ButtonType("Ok", ButtonData.OK_DONE);
                //Setting the content of the dialog
                hdialog.setContentText("This is a simple notepad");
                //Adding buttons to the dialog pane
                hdialog.getDialogPane().getButtonTypes().add(type);
                hdialog.showAndWait();
            }
        });

        //check if text saved
        txt.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                // check if change happened in the text area --> not saved
                oldTxt = newTxt;
                newTxt = txt.getText();
                if (!newTxt.equals(oldTxt)) {
                    isSaved = false;
                    primaryStage.setTitle("*" + fileName + "NotePad"); // add "*" before file name if not saved
                }
            }
        });
        // save dialoge
        dialog2 = new Dialog<ButtonType>();
        dialog2.setTitle("Save");
        dialog2.setContentText("Do you want to save last changes?");
        dialog2.getDialogPane().getButtonTypes().addAll(save, dontSave, cancel);
           fNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if(!isSaved){
                    // if not saved --> show ask to save dialog
                    Optional<ButtonType> result = dialog2.showAndWait();
                    if(!result.isPresent()){
                        // do nothing
                    }
                    else if(result.get() == save){
                        // if save button save is clicked --> save then initialize to a new document
                        fSave.fire();
                        if(isSaved){
                            txt.clear();              
                            fileName = "Untitled";
                            path = "";
                        }
                    }
                    else if(result.get() == dontSave){
                        // if don't save is clicked --> re initialize to a new document without saving
                        txt.clear();
                        isSaved = true;
                        oldTxt = "";
                        newTxt = "";
                        fileName = "Untitled";
                        path = "";
                    }
                    else if(result.get() == cancel){
                        // if cancel is clicked --> just close the dialog and return
                        dialog2.close();
                        return;
                    }                
                }
                else{
                    // if saved --> reinitialize to a new document
                    fileName = "Untitled";
                    path = "";
                    oldTxt= newTxt = "";
                    txt.clear();
                }
                primaryStage.setTitle(fileName + " NotePad ");         // set the title
            }
        });
                   fSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try{
                    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
                    File f = fileChooser.showSaveDialog(primaryStage);          // choose the file name to be saved
                    fileName = f.getName();                                     // get file name
                    path = f.getParent();                                       // get directory path
                    fw = new FileWriter(f);                                     // file writer
                    bw = new BufferedWriter(fw);                                // buffer writer for the file writer
                    bw.write(txt.getText());                                    // write to buffer writer
                    bw.close();                                                 // close buffer writer
                    fw.close();                                                 // close file writer
                    isSaved = true;                                             // set the issaved flag to true
                    // initialize old and new txt
                    oldTxt = txt.getText();
                    newTxt = txt.getText();
                    primaryStage.setTitle(fileName + "  NotePad ");         // set the title
                }
                catch(Exception e){}                
            }
        });
         fOpen.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // save the file is not saved
                if(!isSaved)
                    fNew.fire();
                // if saved show open dialog
                if(isSaved){
                    fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
                    try{
                        File f = fileChooser.showOpenDialog(primaryStage);      // choose the file to open
                        fileName = f.getName();                                 // get file name
                        txt.clear();                                            // clear the text area
                        path = f.getParent();                                   // get directory path
                        fr = new FileReader(f);                                 // file reader
                        br = new BufferedReader(fr);                            // buffer reader to read the file
                        String line;
                        do{
                            line = br.readLine();                               // read lines
                            if(line != null)
                                txt.appendText(line+"\n");                      // set on text area

                        }while(line != null);
                        br.close();                                             // close the buffer reader
                        fr.close();                                             // close the file reader
                        isSaved = true;                                         // issaved flag is true
                        // initialize old and new txt
                        oldTxt = txt.getText();
                        newTxt = txt.getText();
                        primaryStage.setTitle(fileName + "  NotePad ");     // set the title
                    
                    
                    }
                    catch(Exception e){}
                }
            }
        });
              fExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!isSaved){
                    // if not saved --> show ask to save dialog
                    Optional<ButtonType> result = dialog2.showAndWait();
                    if(!result.isPresent()){
                    }
                    else if(result.get() == save){
                        // if saved is clicked --> fire save menu item
                        fSave.fire();
                        if(isSaved)
                            Platform.exit();        // exit if saved
                    }
                    else if(result.get() == dontSave){
                        Platform.exit();            // if don't save is clicked --> just exit!
                    }
                    else if(result.get() == cancel){
                        dialog2.close();             // cancel is clicked --> just lcose the dialog!
                    }                
                }
                else
                    Platform.exit();                // if saved --> exit
            }
        });

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
