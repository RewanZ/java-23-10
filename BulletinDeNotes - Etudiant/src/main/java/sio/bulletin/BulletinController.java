package sio.bulletin;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import sio.bulletin.Model.Etudiant;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

import static java.lang.Double.parseDouble;

public class BulletinController implements Initializable
{
    DecimalFormat df;
    HashMap<String,HashMap<String, HashMap<String,ArrayList<Etudiant>>>> lesBulletins;
    @FXML
    private AnchorPane apBulletin;
    @FXML
    private ListView lvMatieres;
    @FXML
    private ListView lvDevoirs;
    @FXML
    private ComboBox cboTrimestres;
    @FXML
    private TextField txtNomEtudiant;
    @FXML
    private TextField txtNote;
    @FXML
    private Button btnValider;
    @FXML
    private AnchorPane apMoyenne;
    @FXML
    private Button btnMenuBulletin;
    @FXML
    private Button btnMenuMoyenne;
    @FXML
    private ListView lvMatieresMoyenne;
    @FXML
    private TreeView tvMoyennesParDevoirs;
    @FXML
    private TextField txtMajor;
    @FXML
    private TextField txtNoteMaxi;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        apBulletin.toFront();
        df = new DecimalFormat("#.##");
        lesBulletins = new HashMap<>();
        lvMatieres.getItems().addAll("Maths","Anglais","Economie");
        lvDevoirs.getItems().addAll("Devoir n°1","Devoir n°2","Devoir n°3","Devoir n°4");
        cboTrimestres.getItems().addAll("Trim 1","Trim 2","Trim 3");
        cboTrimestres.getSelectionModel().selectFirst();
    }

    @FXML
    public void btnMenuClicked(Event event)
    {
        if(event.getSource()==btnMenuBulletin)
        {
            apBulletin.toFront();
        }
        else if(event.getSource()==btnMenuMoyenne)
        {
            apMoyenne.toFront();
        }
    }

    @FXML
    public void btnValiderClicked(Event event)
    {
        if(txtNomEtudiant.getText().equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Nom élève");
            alert.setHeaderText("");
            alert.setContentText("Saisir un nom");
            alert.showAndWait();
        }

        else if(txtNote.getText().equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur de saisie");
            alert.setHeaderText("");
            alert.setContentText("Saisir une note");
            alert.showAndWait();
        }
        else
        {

            Etudiant unEtudiant = new Etudiant(txtNomEtudiant.getText(), parseDouble(txtNote.getText()));


            if (!lesBulletins.containsKey(lvMatieres.getSelectionModel().getSelectedItem().toString()))
            {

                ArrayList<Etudiant> lesEtudiants = new ArrayList<>();
                lesEtudiants.add(unEtudiant);

                HashMap<String, ArrayList<Etudiant>> lesTrimestres = new HashMap<>();
                lesTrimestres.put(cboTrimestres.getSelectionModel().getSelectedItem().toString(), lesEtudiants);

                HashMap<String, HashMap<String, ArrayList<Etudiant>>> nomDevoir = new HashMap<>();
                nomDevoir.put(lvDevoirs.getSelectionModel().getSelectedItem().toString(), lesTrimestres);

                lesBulletins.put(lvMatieres.getSelectionModel().getSelectedItem().toString(), nomDevoir);
            }

            }

        }

        @FXML
    public void lvMatieresMoyenneClicked(Event event)
    {
        HashMap<String, Double> moyennesParMatiere = new HashMap<>();
        //HashMap<String, Double> moyennesParDevoir = new HashMap<>();

        String nomMajor = "";
        double meilleureNote = 0;

        for (String matiere : lesBulletins.keySet()) {
            double sommeNotes = 0.0;
            int nbDevoirs = 0;

            for (String devoir : lesBulletins.get(matiere).keySet()) {

                for (String trimestre : lesBulletins.get(matiere).get(devoir).keySet()) {

                    for (Etudiant etudiant : lesBulletins.get(matiere).get(devoir).get(trimestre)) {
                        sommeNotes += etudiant.getNote();

                        if (etudiant.getNote() > meilleureNote) {
                            meilleureNote = etudiant.getNote();
                            nomMajor = etudiant.getNomEtudiant();
                        }
                    }
                    nbDevoirs++;
                }
            }

            if (nbDevoirs > 0) {
                double moyenneMatiere = sommeNotes / nbDevoirs;
                moyennesParMatiere.put(matiere, moyenneMatiere);
            }
        }

        lvMatieresMoyenne.getItems().clear();
        tvMoyennesParDevoirs.getItems().clear();

        for (Map.Entry<String, Double> entry : moyennesParMatiere.entrySet()) {
            String matiere = entry.getKey();
            double moyenne = entry.getValue();
            lvMatieresMoyenne.getItems().add(matiere);
            tvMoyennesParDevoirs.getItems().add(moyenne);
        }
        txtMajor.setText(nomMajor);
        txtNoteMaxi.setText(df.format(meilleureNote));
    }
}