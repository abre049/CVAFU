package cvafu;
 
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.lang.Integer;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;
import static javafx.application.Application.launch;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import java.awt.image.BufferedImage;
import javafx.scene.image.WritableImage;
 
public class CVAFU extends Application {
    
    CVAFUData data = new CVAFUData();
    Patient currentPatient;
   
    ComboBox name;
    ComboBox nhi;
    TextField age = new TextField("");
    TextField admission;
    TextField ophrs;
    TextField dc;        
    ComboBox pt;
    ComboBox ot;
    ComboBox slt;
    ComboBox drs;
    ComboBox nrs;
    TextField mr = new TextField("");
    ComboBox mrDate;
    ComboBox homePre;
    ComboBox homePost;
    TextArea notes;
    
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ENGLISH);
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CVAFU");
        VBox root = new VBox();
        Scene scene = new Scene(root, 500, 400);
        primaryStage.setScene(scene);
        
        GridPane id = id();
        root.getChildren().add(id);
        GridPane hospDatesGrid = hospDatesGrid();
        root.getChildren().add(hospDatesGrid);
        GridPane visitDateGrid = visitDateGrid();
        root.getChildren().add(visitDateGrid);
        GridPane mrDateGrid = mrDateGrid();
        root.getChildren().add(mrDateGrid);
        GridPane homeGrid = homeGrid();
        root.getChildren().add(homeGrid);
        VBox notesVbox = notes();
        root.getChildren().add(notesVbox);
        HBox menuHbox = menuHbox();
        root.getChildren().add(menuHbox);
        
        primaryStage.show();    
    }
    
    private GridPane id(){
        GridPane grid = new GridPane();
        grid.setHgap(10);
        
        name = new ComboBox();
        name.setEditable(true);
        name.setPromptText("");
        name.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
           @Override
            public void changed(ObservableValue ov, String t, String selectedPt){
                if (data.getNames().contains(selectedPt)){
                    currentPatient = data.getPatientByName(selectedPt);
                    loadPatient();
             }
                 
             }
        });
        
//        name.valueProperty().addListener(new ChangeListener<String>(){
//            @Override
//             public void changed(ObservableValue ov, String t, String selectedPt){
//                 currentPatient = data.getPatientByName(selectedPt);
//                 loadPatient();
//             }
//        });

        
        nhi = new ComboBox();
        nhi.setPromptText("");
        nhi.setEditable(true);
        nhi.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
           @Override
            public void changed(ObservableValue ov, String t, String selectedPt){
                if (data.getNHIs().contains(selectedPt)){
                    currentPatient = data.getPatientByNHI(selectedPt);
                    loadPatient();
             }
                 
             }
        });
        
        
        
        Button addPtBtn = new Button();
        addPtBtn.setText("Add");
        addPtBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override 
            public void handle(ActionEvent e){
                currentPatient = data.addPatient(name.getEditor().getText(), 
                        nhi.getEditor().getText(), Integer.parseInt(age.getText()));
                currentPatient.print();
            }
        });
        
        
        Button removePtBtn = new Button();
        removePtBtn.setText("Remove");
        
        grid.add(new Label("Name"), 0, 0);
        grid.add(name, 0, 1);
        grid.add(new Label("NHI"), 1, 0);
        grid.add(nhi, 1, 1);
        grid.add(new Label("Age"), 2, 0);
        grid.add(age, 2, 1);
        grid.add(addPtBtn, 3, 1);
        grid.add(removePtBtn, 4, 1);
        return grid;
    }
    private GridPane hospDatesGrid(){
        GridPane grid = new GridPane();
        grid.setHgap(10);
//        grid.setPadding(new Insets(0, 10, 0, 10));

        admission = new TextField();
        admission.setPromptText("__/__/____");
        ophrs = new TextField();
        ophrs.setPromptText("__/__/____");
        dc = new TextField();
        dc.setPromptText("__/__/____");
        
        Button admitBtn = new Button("Enter");
        admitBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override 
            public void handle(ActionEvent e){
                String stringDate = admission.getText();
                currentPatient.updateAdmitDate(LocalDate.parse(stringDate, dateFormat));
                currentPatient.print();
            }
        });
        
        Button ophrsBtn = new Button("Enter");
        ophrsBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override 
            public void handle(ActionEvent e){
                String stringDate = ophrs.getText();
                currentPatient.updateOphrsDate(LocalDate.parse(stringDate, dateFormat));
                currentPatient.print();
            }
        });
        
        Button dcBtn = new Button("Enter");
        dcBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override 
            public void handle(ActionEvent e){
                String stringDate = dc.getText();
                currentPatient.updateDcDate(LocalDate.parse(stringDate, dateFormat));
                currentPatient.print();
            }
        });
    
        grid.add(new Label("Admission Date"), 0, 0);
        grid.add(new Label("OPHRS Transfer Date"), 1, 0);
        grid.add(new Label("Discharge Date"), 2, 0);
        grid.add(admission, 0, 1);
        grid.add(ophrs, 1, 1);
        grid.add(dc, 2, 1);
        grid.add(admitBtn, 0, 2);
        grid.add(ophrsBtn, 1, 2);
        grid.add(dcBtn, 2, 2);
        return grid;
    }
    private GridPane visitDateGrid(){
        GridPane grid = new GridPane();
        grid.setHgap(10);
        
        pt = new ComboBox();
        pt.setEditable(true);
        pt.setPromptText("__/__/____");
        
        ot = new ComboBox();
        ot.setEditable(true);
        ot.setPromptText("__/__/____");
        
        slt = new ComboBox();
        slt.setEditable(true);
        slt.setPromptText("__/__/____");
        
        drs = new ComboBox();
        drs.setEditable(true);
        drs.setPromptText("__/__/____");
        
        nrs = new ComboBox();
        nrs.setEditable(true);
        nrs.setPromptText("__/__/____");
        
        Button ptAddBtn = new Button("Add");
        ptAddBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override 
            public void handle(ActionEvent e){
                String stringDate = pt.getEditor().getText();
                currentPatient.pt.add(LocalDate.parse(stringDate, dateFormat));
                loadPatient();
                pt.getEditor().clear();
                currentPatient.print();
            }
        });
        Button otAddBtn = new Button("Add");
        otAddBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override 
            public void handle(ActionEvent e){
                String stringDate = ot.getEditor().getText();
                currentPatient.ot.add(LocalDate.parse(stringDate, dateFormat));
                loadPatient();
                ot.getEditor().clear();
                currentPatient.print();
            }
        });
        Button sltAddBtn = new Button("Add");
        sltAddBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override 
            public void handle(ActionEvent e){
                String stringDate = slt.getEditor().getText();
                currentPatient.slt.add(LocalDate.parse(stringDate, dateFormat));
                loadPatient();
                slt.getEditor().clear();
                currentPatient.print();
            }
        });
        Button drsAddBtn = new Button("Add");
        drsAddBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override 
            public void handle(ActionEvent e){
                String stringDate = drs.getEditor().getText();
                currentPatient.drs.add(LocalDate.parse(stringDate, dateFormat));
                loadPatient();
                drs.getEditor().clear();
                currentPatient.print();
            }
        });
        Button nrsAddBtn = new Button("Add");
        nrsAddBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override 
            public void handle(ActionEvent e){
                String stringDate = nrs.getEditor().getText();
                currentPatient.nrs.add(LocalDate.parse(stringDate, dateFormat));
                loadPatient();
                nrs.getEditor().clear();
                currentPatient.print();
            }
        });
        
        Button ptRemBtn = new Button("Remove");
        Button otRemBtn = new Button("Remove");
        Button sltRemBtn = new Button("Remove");
        Button drsRemBtn = new Button("Remove");
        Button nrsRemBtn = new Button("Remove");
        
        grid.add(new Label("PT"), 0, 0);
        grid.add(pt, 0, 1);
        grid.add(ptAddBtn, 0, 2);
        grid.add(ptRemBtn, 0, 3);
        grid.add(new Label("OT"), 1, 0);
        grid.add(ot, 1, 1);
        grid.add(otAddBtn, 1, 2);
        grid.add(otRemBtn, 1, 3);
        grid.add(new Label("SLT"), 2, 0);
        grid.add(slt, 2, 1);
        grid.add(sltAddBtn, 2, 2);
        grid.add(sltRemBtn, 2, 3);
        grid.add(new Label("Drs"), 3, 0);
        grid.add(drs, 3, 1);
        grid.add(drsAddBtn, 3, 2);
        grid.add(drsRemBtn, 3, 3);
        grid.add(new Label("Nrs"), 4, 0);
        grid.add(nrs, 4, 1);
        grid.add(nrsAddBtn, 4, 2);
        grid.add(nrsRemBtn, 4, 3);
        
        return grid;
    }
    private GridPane mrDateGrid(){
        GridPane grid = new GridPane();
        grid.setHgap(10);
        
        mrDate = new ComboBox();
        mrDate.setEditable(true);
        mrDate.setPromptText("__/__/____");
        mrDate.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
           @Override
            public void changed(ObservableValue ov, String t, String selectedDate){
                if (mrDate.getItems().contains(selectedDate)){
                    int score = currentPatient.getMrScore(LocalDate.parse(selectedDate, dateFormat));
                    mr.setText(Integer.toString(score));
//                    currentPatient.removeMr(LocalDate.parse(selectedDate, dateFormat));
//                    loadPatient();
                }  
            }
        });
        
        
        Button addMrBtn = new Button("Add");
        addMrBtn.setOnAction(new EventHandler<ActionEvent>(){
           @Override 
            public void handle(ActionEvent e){
                String mrScore = mr.getText();
                String mrDateString = mrDate.getEditor().getText();
                currentPatient.addMr(Integer.parseInt(mrScore), LocalDate.parse(mrDateString, dateFormat));
                mr.setText("");
                loadPatient();
            }
        });
        Button remMrBtn = new Button("Remove");
        
        grid.add(new Label("MR"), 0, 0);
        grid.add(new Label("Date"), 1, 0);
        grid.add(mr, 0, 1);
        grid.add(mrDate, 1, 1);
        grid.add(addMrBtn, 2, 1);
        grid.add(remMrBtn, 3, 1);

        return grid;
    }
    private GridPane homeGrid(){
        GridPane grid = new GridPane();
        grid.setHgap(10);
        List options = Patient.Housing.getAllStrings();
        
        homePre = new ComboBox();
        homePre.setPromptText("");
        homePre.getItems().addAll(
            options
        );
        homePre.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
           @Override
            public void changed(ObservableValue ov, String t, String selectedHome){
                currentPatient.homePre = Patient.Housing.getHousing(selectedHome);
            }
        });
        
        homePost = new ComboBox();
        homePost.setPromptText("");
        homePost.getItems().addAll(
            options
        );
                homePost.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>(){
           @Override
            public void changed(ObservableValue ov, String t, String selectedHome){
                currentPatient.homePost = Patient.Housing.getHousing(selectedHome);
            }
        });
        
        grid.add(new Label("Home Before Stroke"), 0, 0);
        grid.add(new Label("Home After Stroke"), 1, 0);
        grid.add(homePre, 0, 1);
        grid.add(homePost, 1, 1 );
        
        return grid;
    }
    private VBox notes(){
        VBox vbox = new VBox();
        
        notes = new TextArea(); 
        notes.setPromptText("Notes");
        
        Button  updateBtn = new Button("Update");
        updateBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override 
            public void handle(ActionEvent e){
                currentPatient.notes = notes.getText();
            }
        });
        
        vbox.getChildren().add(notes);
        vbox.getChildren().add(updateBtn);
        
        return vbox;
    }
    private HBox menuHbox(){
        HBox hbox = new HBox();
        
        Button saveBtn = new Button("SAVE");
        saveBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override 
            public void handle(ActionEvent e){
                data.save();
            }
        });
        
        Button loadBtn = new Button("LOAD");
        loadBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override 
            public void handle(ActionEvent e){
                loadData();
            }
        });
        
        Button viewBtn = new Button("VIEW");
        viewBtn.setOnAction(new EventHandler<ActionEvent>(){
            @Override 
            public void handle(ActionEvent e){
                Stage stage = new Stage();
                stage.setTitle(currentPatient.name);
                HBox hbox = new HBox();
                ImageView iv = new ImageView();
                Image img = SwingFXUtils.toFXImage(currentPatient.makeImage(),null);
                iv.setImage(img);
                hbox.getChildren().add(iv);
                Scene scene = new Scene(hbox, 1024, 512);
                stage.setScene(scene);
                stage.show();
            }
        });
        
        hbox.getChildren().add(saveBtn);
        hbox.getChildren().add(loadBtn);
        hbox.getChildren().add(viewBtn);
        return hbox;
    }
    
    private void loadData(){
        data.load();
        nhi.getItems().addAll(data.getNHIs());
        name.getItems().addAll(data.getNames());
        
    }
    private void loadPatient(){
        name.getEditor().setText(currentPatient.name);
        nhi.getEditor().setText(currentPatient.nhi);
        age.setText(Integer.toString(currentPatient.age));
        
        currentPatient.print();
        if (currentPatient.admitDate != null){
            admission.setText(dateFormat.format(currentPatient.admitDate));
        } else {admission.setText(null);}
        if (currentPatient.ophrsDate != null){
            admission.setText(dateFormat.format(currentPatient.ophrsDate));
        } else {admission.setText(null);}
        if (currentPatient.dcDate != null){
            admission.setText(dateFormat.format(currentPatient.dcDate));
        } else {admission.setText(null);}
        

        pt.getItems().clear();
        if (!currentPatient.pt.isEmpty()){
            pt.getItems().addAll(datesToStrings(currentPatient.pt));
        }
        ot.getItems().clear();
        if (!currentPatient.ot.isEmpty()){
            ot.getItems().addAll(datesToStrings(currentPatient.ot));
        }
        slt.getItems().clear();
        if (!currentPatient.slt.isEmpty()){
            slt.getItems().addAll(datesToStrings(currentPatient.slt));
        }
        drs.getItems().clear();
        if (!currentPatient.drs.isEmpty()){
            drs.getItems().addAll(datesToStrings(currentPatient.drs));
        }
        nrs.getItems().clear();
        if (!currentPatient.nrs.isEmpty()){
            nrs.getItems().addAll(datesToStrings(currentPatient.nrs));
        }
        
        mr.setText("");
        mrDate.getItems().clear();
        if (!currentPatient.mr.isEmpty()){
            mrDate.getItems().addAll(datesToStrings(currentPatient.getMrDates()));
        }
        mrDate.setValue(null);
        mrDate.getEditor().clear();
        
        if (currentPatient.homePre != null){
            homePre.setValue(currentPatient.homePre.getString());
        } else {homePre.setValue("");}
        if (currentPatient.homePost != null){
            homePre.setValue(currentPatient.homePost.getString());
        } else {homePre.setValue("");}
        

        
    }
    
    private List datesToStrings(List<LocalDate> dates){
        List strings = new ArrayList();
        for (LocalDate date : dates){
            strings.add(dateFormat.format(date));
        }
        return strings;
    }
}


/* Known bugs
- can handle age that is not a number
- change if hbox to grid
- change admission,oprhrs and dc to prompt __/__/____
*/